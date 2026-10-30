package me.pablorincon.proyecto_app.data

import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

// Único lugar de la app que habla con Firebase Authentication.
// Las pantallas solo ven resultados simples, nunca las excepciones de Firebase.
class AuthRepository(private val auth: FirebaseAuth = FirebaseAuth.getInstance()) {

    enum class RegisterResult { Success, EmailAlreadyInUse, NoConnection, Failed }

    // Crea la cuenta y guarda el nombre en el perfil de Firebase.
    // Firebase deja la sesión abierta al registrar; se cierra porque,
    // según lo acordado, después del registro el usuario debe iniciar sesión.
    suspend fun register(name: String, email: String, password: String): RegisterResult = try {
        val user = auth.createUserWithEmailAndPassword(email, password).awaitResult().user
        try {
            val profile = UserProfileChangeRequest.Builder().setDisplayName(name).build()
            user?.updateProfile(profile)?.awaitResult()
        } catch (e: FirebaseException) {
            // La cuenta ya existe; si el nombre no se guardó no vale la pena fallar por eso.
        }
        auth.signOut()
        RegisterResult.Success
    } catch (e: CancellationException) {
        throw e // La pantalla se cerró: no es un error que mostrar
    } catch (e: FirebaseAuthUserCollisionException) {
        RegisterResult.EmailAlreadyInUse
    } catch (e: FirebaseNetworkException) {
        RegisterResult.NoConnection
    } catch (e: Exception) {
        RegisterResult.Failed
    }
}

// Convierte una tarea de Firebase (callbacks) en una función suspend,
// así no hace falta agregar la librería kotlinx-coroutines-play-services.
private suspend fun <T> Task<T>.awaitResult(): T = suspendCancellableCoroutine { continuation ->
    addOnCompleteListener { task ->
        val error = task.exception
        when {
            error != null -> continuation.resumeWithException(error)
            task.isCanceled -> continuation.cancel()
            else -> continuation.resume(task.result)
        }
    }
}
