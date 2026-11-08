package me.pablorincon.proyecto_app.data

import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.withTimeoutOrNull

// Único lugar de la app que habla con Firebase Authentication.
// Las pantallas solo ven resultados simples, nunca las excepciones de Firebase.
class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val userRepository: UserRepository = UserRepository()
) {

    // Firebase guarda la sesión en el teléfono: se consulta al instante, sin red.
    fun isLoggedIn(): Boolean = auth.currentUser != null

    // Datos de la sesión abierta (null si no hay sesión).
    fun currentUid(): String? = auth.currentUser?.uid
    fun currentName(): String? = auth.currentUser?.displayName
    fun currentEmail(): String? = auth.currentUser?.email

    fun signOut() = auth.signOut()

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
        // El perfil se crea con la sesión aún abierta (las reglas de Firestore exigen
        // ser el dueño). Si falla o tarda, el registro sigue siendo válido: el primer
        // login lo crea. El límite de tiempo evita quedar esperando sin red.
        user?.let {
            withTimeoutOrNull(PROFILE_TIMEOUT_MILLIS) { userRepository.createInitialProfile(it.uid, name) }
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
        // Nombre guardado al registrarse e id de la cuenta
        data class Success(val uid: String, val name: String?) : LoginResult
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
                ?: return LoginResult.Failed
            LoginResult.Success(user.uid, user.displayName)
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

    enum class ResetResult { Sent, InvalidEmail, NoConnection, Failed }

    // Firebase envía un correo con un enlace para crear una contraseña nueva.
    suspend fun sendPasswordReset(email: String): ResetResult = try {
        auth.setLanguageCode("es") // El correo de Firebase llega en español
        auth.sendPasswordResetEmail(email).awaitResult()
        ResetResult.Sent
    } catch (e: CancellationException) {
        throw e
    } catch (e: FirebaseAuthInvalidUserException) {
        // No hay cuenta con ese correo. Se responde igual que si existiera,
        // para no revelar qué correos están registrados.
        ResetResult.Sent
    } catch (e: FirebaseAuthInvalidCredentialsException) {
        ResetResult.InvalidEmail // Firebase rechazó el formato del correo
    } catch (e: FirebaseNetworkException) {
        ResetResult.NoConnection
    } catch (e: Exception) {
        ResetResult.Failed
    }

    private companion object {
        const val PROFILE_TIMEOUT_MILLIS = 5_000L
    }
}
