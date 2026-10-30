package me.pablorincon.proyecto_app.ui.screens.forgotpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.pablorincon.proyecto_app.data.AuthRepository
import me.pablorincon.proyecto_app.data.AuthRepository.ResetResult
import me.pablorincon.proyecto_app.domain.AuthValidator

enum class ForgotPasswordError { InvalidEmail, NoConnection, Unknown }

// Todo lo que la pantalla de recuperación necesita mostrar.
data class ForgotPasswordUiState(
    val email: String = "",
    val error: ForgotPasswordError? = null,
    val isLoading: Boolean = false, // Esperando respuesta de Firebase
    val sent: Boolean = false       // El correo se envió: la pantalla debe salir
)

class ForgotPasswordViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState.asStateFlow()

    // Igual que en el resto de formularios, el error se borra al escribir.
    fun onEmailChange(value: String) = _uiState.update { it.copy(email = value, error = null) }

    fun submit() {
        val state = _uiState.value
        if (state.isLoading) return // Evita enviar dos correos con doble toque

        val email = AuthValidator.normalizeEmail(state.email)
        if (!AuthValidator.isValidEmail(email)) {
            _uiState.update { it.copy(error = ForgotPasswordError.InvalidEmail) }
            return
        }

        _uiState.update { it.copy(error = null, isLoading = true) }
        viewModelScope.launch {
            when (repository.sendPasswordReset(email)) {
                ResetResult.Sent -> _uiState.value = ForgotPasswordUiState(sent = true)
                ResetResult.InvalidEmail -> showError(ForgotPasswordError.InvalidEmail)
                ResetResult.NoConnection -> showError(ForgotPasswordError.NoConnection)
                ResetResult.Failed -> showError(ForgotPasswordError.Unknown)
            }
        }
    }

    // La pantalla avisa que ya navegó, para no repetirlo al girar el teléfono.
    fun onSentHandled() = _uiState.update { it.copy(sent = false) }

    private fun showError(error: ForgotPasswordError) =
        _uiState.update { it.copy(error = error, isLoading = false) }
}
