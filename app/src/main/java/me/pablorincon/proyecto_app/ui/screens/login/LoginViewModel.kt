package me.pablorincon.proyecto_app.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.pablorincon.proyecto_app.data.AuthRepository
import me.pablorincon.proyecto_app.data.AuthRepository.LoginResult
import me.pablorincon.proyecto_app.domain.AuthValidator

enum class LoginError { WrongCredentials, NoConnection, Unknown }

// Todo lo que la pantalla de inicio de sesión necesita mostrar.
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val error: LoginError? = null,
    val isLoading: Boolean = false,  // Esperando respuesta de Firebase
    val loggedIn: Boolean = false,   // La sesión se inició: la pantalla debe salir
    val userName: String? = null     // Para el saludo "¡Bienvenido de nuevo, …!"
)

// Datos de la tarjeta "Cuenta de ejemplo". La cuenta debe existir en Firebase.
const val EXAMPLE_EMAIL = "ejemplo@correo.com"
const val EXAMPLE_PASSWORD = "ejemplo123"

class LoginViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    // Como en el mockup, el error se borra en cuanto el usuario escribe.
    fun onEmailChange(value: String) = _uiState.update { it.copy(email = value, error = null) }
    fun onPasswordChange(value: String) = _uiState.update { it.copy(password = value, error = null) }

    // Botón "Usar cuenta de ejemplo": rellena los campos, no inicia sesión.
    fun fillExampleAccount() = _uiState.update {
        it.copy(email = EXAMPLE_EMAIL, password = EXAMPLE_PASSWORD, error = null)
    }

    fun submit() {
        val state = _uiState.value
        if (state.isLoading) return // Evita enviar dos veces con doble toque

        // Como el mockup, no se valida el formato: cualquier fallo da el mismo mensaje.
        val email = AuthValidator.normalizeEmail(state.email)
        _uiState.update { it.copy(error = null, isLoading = true) }
        viewModelScope.launch {
            when (val result = repository.login(email, state.password)) {
                // Se limpia el formulario para que no quede la contraseña escrita
                is LoginResult.Success ->
                    _uiState.value = LoginUiState(loggedIn = true, userName = result.name)
                LoginResult.WrongCredentials -> showError(LoginError.WrongCredentials)
                LoginResult.NoConnection -> showError(LoginError.NoConnection)
                LoginResult.Failed -> showError(LoginError.Unknown)
            }
        }
    }

    // La pantalla avisa que ya navegó, para no repetirlo al girar el teléfono.
    fun onLoggedInHandled() = _uiState.update { it.copy(loggedIn = false, userName = null) }

    private fun showError(error: LoginError) =
        _uiState.update { it.copy(error = error, isLoading = false) }
}
