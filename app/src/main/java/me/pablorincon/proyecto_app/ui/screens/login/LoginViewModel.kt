package me.pablorincon.proyecto_app.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.pablorincon.proyecto_app.BuildConfig
import me.pablorincon.proyecto_app.data.AuthRepository
import me.pablorincon.proyecto_app.data.AuthRepository.LoginResult
import me.pablorincon.proyecto_app.data.PreferencesRepository
import me.pablorincon.proyecto_app.data.UserRepository
import me.pablorincon.proyecto_app.data.UserRepository.ProfileResult
import me.pablorincon.proyecto_app.domain.AuthValidator

enum class LoginError { WrongCredentials, NoConnection, Unknown, ProfileNotLoaded }

// Todo lo que la pantalla de inicio de sesión necesita mostrar.
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val error: LoginError? = null,
    val isLoading: Boolean = false,  // Esperando respuesta de Firebase
    val loggedIn: Boolean = false,   // La sesión se inició: la pantalla debe salir
    val userName: String? = null,    // Para el saludo "¡Bienvenido de nuevo, …!"
    val hasPersonalization: Boolean = false // Decide si sigue Inicio o Personalización
)

// Datos de la tarjeta "Cuenta de ejemplo". La cuenta debe existir en Firebase.
const val EXAMPLE_EMAIL = "ejemplo@correo.com"
const val EXAMPLE_PASSWORD = "ejemplo123"

class LoginViewModel(
    private val repository: AuthRepository = AuthRepository(),
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    // Como en el mockup, el error se borra en cuanto el usuario escribe.
    fun onEmailChange(value: String) = _uiState.update { it.copy(email = value, error = null) }
    fun onPasswordChange(value: String) = _uiState.update { it.copy(password = value, error = null) }

    // Botón "Usar cuenta de ejemplo": rellena los campos, no inicia sesión. Solo existe en las
    // versiones de prueba; en release no hace nada (y R8 quita los datos de la cuenta del instalable).
    fun fillExampleAccount() {
        if (!BuildConfig.DEBUG) return
        _uiState.update { it.copy(email = EXAMPLE_EMAIL, password = EXAMPLE_PASSWORD, error = null) }
    }

    fun submit() {
        val state = _uiState.value
        if (state.isLoading) return // Evita enviar dos veces con doble toque

        // Como el mockup, no se valida el formato: cualquier fallo da el mismo mensaje.
        val email = AuthValidator.normalizeEmail(state.email)
        _uiState.update { it.copy(error = null, isLoading = true) }
        viewModelScope.launch {
            when (val result = repository.login(email, state.password)) {
                is LoginResult.Success -> onSignedIn(result)
                LoginResult.WrongCredentials -> showError(LoginError.WrongCredentials)
                LoginResult.NoConnection -> showError(LoginError.NoConnection)
                LoginResult.Failed -> showError(LoginError.Unknown)
            }
        }
    }

    // La sesión ya está abierta: se carga (o crea) el perfil para decidir a dónde ir.
    // Si no se puede cargar, el formulario se conserva y "Iniciar sesión" reintenta;
    // nunca se asume que el usuario es nuevo.
    private suspend fun onSignedIn(result: LoginResult.Success) {
        val fallbackName = result.name.orEmpty()
        when (val profile = userRepository.ensureProfile(result.uid, fallbackName)) {
            // Se limpia el formulario para que no quede la contraseña escrita
            is ProfileResult.Success -> {
                // Las preferencias de la cuenta (texto, contraste, recordatorio…) se aplican ya
                PreferencesRepository().applyOnDevice(profile.profile.preferences)
                _uiState.value = LoginUiState(
                    loggedIn = true,
                    userName = result.name,
                    hasPersonalization = profile.profile.hasPersonalization
                )
            }
            else -> showError(LoginError.ProfileNotLoaded)
        }
    }

    // La pantalla avisa que ya navegó, para no repetirlo al girar el teléfono.
    fun onLoggedInHandled() =
        _uiState.update { it.copy(loggedIn = false, userName = null, hasPersonalization = false) }

    private fun showError(error: LoginError) =
        _uiState.update { it.copy(error = error, isLoading = false) }
}
