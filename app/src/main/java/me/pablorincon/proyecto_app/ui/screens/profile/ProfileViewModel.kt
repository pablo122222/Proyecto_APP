package me.pablorincon.proyecto_app.ui.screens.profile

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import me.pablorincon.proyecto_app.data.AuthRepository

// Todo lo que la pantalla de Perfil necesita mostrar.
// En la Fase 17 se suman los datos reales (nivel, XP, estadísticas, meta diaria).
data class ProfileUiState(
    val signedOut: Boolean = false // Se cerró la sesión: la app debe volver a Bienvenida
)

class ProfileViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    // Cerrar sesión en Firebase es local e inmediato: no necesita red.
    fun signOut() {
        repository.signOut()
        _uiState.update { it.copy(signedOut = true) }
    }

    // La pantalla avisa que ya navegó, para no repetirlo al girar el teléfono.
    fun onSignedOutHandled() = _uiState.update { it.copy(signedOut = false) }
}
