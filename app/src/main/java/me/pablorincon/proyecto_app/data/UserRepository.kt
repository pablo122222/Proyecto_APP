package me.pablorincon.proyecto_app.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.withTimeoutOrNull
import me.pablorincon.proyecto_app.domain.LearningGoal
import me.pablorincon.proyecto_app.domain.LearningLevel
import me.pablorincon.proyecto_app.domain.UserProfile

// Único lugar de la app que habla con Firestore para el perfil del usuario (users/{uid}).
// Igual que AuthRepository: las pantallas solo ven resultados simples.
// Firestore guarda una copia en el teléfono: las lecturas ya vistas funcionan sin red.
class UserRepository(private val db: FirebaseFirestore = FirebaseFirestore.getInstance()) {

    sealed interface ProfileResult {
        data class Success(val profile: UserProfile) : ProfileResult
        data object NoConnection : ProfileResult
        data object Failed : ProfileResult
    }

    enum class SaveResult { Saved, NoConnection, Failed }

    private fun document(uid: String) = db.collection(USERS).document(uid)

    // Crea el documento de un usuario nuevo con los valores iniciales (decisión E).
    suspend fun createInitialProfile(uid: String, name: String): SaveResult = try {
        document(uid).set(UserProfileMapper.toMap(UserProfile.initial(name))).awaitResult()
        SaveResult.Saved
    } catch (e: CancellationException) {
        throw e // La pantalla se cerró: no es un error que mostrar
    } catch (e: Exception) {
        if (e.isNetworkError()) SaveResult.NoConnection else SaveResult.Failed
    }

    // Lee el perfil. Si el documento no existe todavía (registro que no alcanzó a
    // guardarlo), lo crea: así el login siempre termina con un perfil válido.
    // Sin red da NoConnection: nunca se asume que el usuario es nuevo. Por eso un "no existe"
    // que viene de la copia del teléfono no cuenta: crear el perfil ahí dejaría en cola una
    // escritura que, al volver la red, borraría el progreso real del usuario.
    suspend fun ensureProfile(uid: String, fallbackName: String): ProfileResult = try {
        val snapshot = document(uid).read()
        val data = snapshot.data
        when {
            snapshot.exists() && data != null -> ProfileResult.Success(UserProfileMapper.fromMap(data, fallbackName))
            snapshot.metadata.isFromCache -> ProfileResult.NoConnection
            else -> {
                val profile = UserProfile.initial(fallbackName)
                withTimeoutOrNull(SAVE_TIMEOUT_MILLIS) {
                    document(uid).set(UserProfileMapper.toMap(profile)).awaitResult()
                    ProfileResult.Success(profile)
                } ?: ProfileResult.NoConnection
            }
        }
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        if (e.isNetworkError()) ProfileResult.NoConnection else ProfileResult.Failed
    }

    // Guarda nivel y objetivo juntos al terminar Personalización. Sin red Firestore deja la
    // escritura en cola y la tarea no termina: pasado el límite se avisa como falta de
    // conexión. Repetir el guardado es seguro (escribe los mismos campos).
    suspend fun savePersonalization(uid: String, level: LearningLevel, goal: LearningGoal): SaveResult = try {
        val fields = mapOf(UserProfileMapper.LEVEL to level.id, UserProfileMapper.GOAL to goal.id)
        withTimeoutOrNull(SAVE_TIMEOUT_MILLIS) {
            document(uid).set(fields, SetOptions.merge()).awaitResult()
            SaveResult.Saved
        } ?: SaveResult.NoConnection
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        if (e.isNetworkError()) SaveResult.NoConnection else SaveResult.Failed
    }

    private companion object {
        const val USERS = "users"
        const val SAVE_TIMEOUT_MILLIS = 8_000L
    }
}
