package me.pablorincon.proyecto_app.data

import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
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

    sealed interface LoginResult {
        data class Success(val name: String?) : LoginResult // Nombre guardado al registrarse
        data object WrongCredentials : LoginResult
        data object NoConnection : LoginResult
        data object Failed : LoginResult
    }

    // Firebase recuerda la sesión por sí solo: al volver a abrir la app sigue iniciada.
    suspend fun login(email: String, password: String): LoginResult {
        // Firebase no acepta campos vacíos; como en el mockup, cuenta como datos incorrectos.
        if (email.isEmpty() || password.isEmpty()) return LoginResult.WrongCredentials
        return try {
            val user = auth.signInWithEmailAndPassword(email, password).awaitResult().user
            LoginResult.Success(user?.displayName)
        } catch (e: CancellationException) {
            throw e
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            LoginResult.WrongCredentials // Contraseña incorrecta o correo mal escrito
        } catch (e: FirebaseAuthInvalidUserException) {
            LoginResult.WrongCredentials // No existe una cuenta con ese correo
        } catch (e: FirebaseNetworkException) {
            LoginResult.NoConnection
        } catch (e: Exception) {
            LoginResult.Failed
        }
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
