package me.pablorincon.proyecto_app.ui.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.pablorincon.proyecto_app.data.AuthRepository
import me.pablorincon.proyecto_app.data.AuthRepository.RegisterResult
import me.pablorincon.proyecto_app.domain.AuthValidator
import me.pablorincon.proyecto_app.domain.AuthValidator.RegisterError

// Todo lo que la pantalla de registro necesita mostrar.
data class RegisterUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val error: RegisterError? = null,
    val isLoading: Boolean = false, // Esperando respuesta de Firebase
    val registered: Boolean = false // La cuenta se creó: la pantalla debe salir
)

// Guarda el estado del formulario (sobrevive al girar el teléfono),
// valida y le pide al repositorio que cree la cuenta.
class RegisterViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    // Como en el mockup, el error se borra en cuanto el usuario escribe.
    fun onNameChange(value: String) = _uiState.update { it.copy(name = value, error = null) }
    fun onEmailChange(value: String) = _uiState.update { it.copy(email = value, error = null) }
    fun onPasswordChange(value: String) = _uiState.update { it.copy(password = value, error = null) }

    fun submit() {
        val state = _uiState.value
        if (state.isLoading) return // Evita crear la cuenta dos veces con doble toque

        val name = AuthValidator.normalizeName(state.name)
        val email = AuthValidator.normalizeEmail(state.email)
        val validationError = AuthValidator.validateRegister(name, email, state.password)
        if (validationError != null) {
            _uiState.update { it.copy(error = validationError) }
            return
        }

        _uiState.update { it.copy(error = null, isLoading = true) }
        viewModelScope.launch {
            when (repository.register(name, email, state.password)) {
                // Se limpia el formulario para que no quede la contraseña escrita
                RegisterResult.Success -> _uiState.value = RegisterUiState(registered = true)
                RegisterResult.EmailAlreadyInUse -> showError(RegisterError.EmailAlreadyInUse)
                RegisterResult.NoConnection -> showError(RegisterError.NoConnection)
                RegisterResult.Failed -> showError(RegisterError.Unknown)
            }
        }
    }

    // La pantalla avisa que ya navegó, para no repetirlo al girar el teléfono.
    fun onRegisteredHandled() = _uiState.update { it.copy(registered = false) }

    private fun showError(error: RegisterError) =
        _uiState.update { it.copy(error = error, isLoading = false) }
}
