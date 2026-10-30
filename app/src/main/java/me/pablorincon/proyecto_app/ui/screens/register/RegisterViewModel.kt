package me.pablorincon.proyecto_app.ui.screens.register

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import me.pablorincon.proyecto_app.domain.AuthValidator

// Todo lo que la pantalla de registro necesita mostrar.
data class RegisterUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val error: AuthValidator.RegisterError? = null
)

// Guarda el estado del formulario (sobrevive al girar el teléfono) y valida al enviar.
class RegisterViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    // Como en el mockup, el error se borra en cuanto el usuario escribe.
    fun onNameChange(value: String) = _uiState.update { it.copy(name = value, error = null) }
    fun onEmailChange(value: String) = _uiState.update { it.copy(email = value, error = null) }
    fun onPasswordChange(value: String) = _uiState.update { it.copy(password = value, error = null) }

    // Devuelve true si los datos son válidos. Guardar la cuenta llega en el Paso 4.
    fun submit(): Boolean {
        val state = _uiState.value
        val error = AuthValidator.validateRegister(
            name = AuthValidator.normalizeName(state.name),
            email = AuthValidator.normalizeEmail(state.email),
            password = state.password
        )
        _uiState.update { it.copy(error = error) }
        return error == null
    }
}
