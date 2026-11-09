package me.pablorincon.proyecto_app.data

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.withTimeoutOrNull

// Acciones sobre la cuenta desde Perfil y Configuración: cambiar nombre y meta diaria,
// restablecer el progreso y eliminar la cuenta. Igual que los demás repositorios,
// las pantallas solo ven resultados simples.
class AccountRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    enum class Result { Done, NoConnection, WrongPassword, Failed }

    private fun user(uid: String) = db.collection(USERS).document(uid)
    private fun progress(uid: String) = user(uid).collection(PROGRESS).document(CURRICULUM)

    // Nombre visible: en el perfil de Firestore (lo que muestra la app) y en Firebase Auth.
    suspend fun updateName(uid: String, name: String): Result = attempt {
        user(uid).set(mapOf(UserProfileMapper.NAME to name), SetOptions.merge()).awaitResult()
        auth.currentUser?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name).build())?.awaitResult()
    }

    suspend fun updateDailyGoal(uid: String, goal: Int): Result = attempt {
        user(uid).set(mapOf(UserProfileMapper.DAILY_GOAL to goal), SetOptions.merge()).awaitResult()
    }

    // Borra lecciones completadas, XP, gemas, racha, contadores y logros. Conserva el nombre,
    // el nivel y objetivo de Personalización, la meta diaria y las preferencias.
    suspend fun resetProgress(uid: String): Result = attempt {
        val batch = db.batch()
        batch.delete(progress(uid))
        batch.set(
            user(uid),
            mapOf(
                UserProfileMapper.XP to 0,
                UserProfileMapper.GEMS to 0,
                UserProfileMapper.STREAK to 0,
                UserProfileMapper.LAST_ACTIVE_DATE to FieldValue.delete(),
                UserProfileMapper.ACTIVITIES_ON_LAST_DAY to 0,
                UserProfileMapper.BEST_STREAK to 0,
                UserProfileMapper.BUGS_FOUND to 0,
                UserProfileMapper.RECOVERED_EXERCISES to 0,
                UserProfileMapper.ACHIEVEMENT_TIERS to emptyMap<String, Int>()
            ),
            SetOptions.merge()
        )
        batch.commit().awaitResult()
    }

    // Elimina la cuenta para siempre. Firebase exige un inicio de sesión reciente, así que
    // primero se confirma la contraseña; luego se borran los datos (mientras aún hay permiso
    // para hacerlo) y al final la cuenta.
    suspend fun deleteAccount(password: String): Result {
        val current = auth.currentUser ?: return Result.Failed
        val email = current.email ?: return Result.Failed
        return try {
            current.reauthenticate(EmailAuthProvider.getCredential(email, password)).awaitResult()
            withTimeoutOrNull(TIMEOUT_MILLIS) {
                progress(current.uid).delete().awaitResult()
                user(current.uid).delete().awaitResult()
                current.delete().awaitResult()
                Result.Done
            } ?: Result.NoConnection
        } catch (e: CancellationException) {
            throw e
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Result.WrongPassword
        } catch (e: FirebaseNetworkException) {
            Result.NoConnection
        } catch (e: Exception) {
            if (e.isNetworkError()) Result.NoConnection else Result.Failed
        }
    }

    // Sin red, Firestore deja la escritura en cola y la tarea no termina: pasado el límite
    // se avisa como falta de conexión (la escritura se completará sola al volver la red).
    private suspend fun attempt(block: suspend () -> Unit): Result = try {
        withTimeoutOrNull(TIMEOUT_MILLIS) {
            block()
            Result.Done
        } ?: Result.NoConnection
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        if (e.isNetworkError()) Result.NoConnection else Result.Failed
    }

    private companion object {
        const val USERS = "users"
        const val PROGRESS = "progress"
        const val CURRICULUM = "curriculum"
        const val TIMEOUT_MILLIS = 8_000L
    }
}
