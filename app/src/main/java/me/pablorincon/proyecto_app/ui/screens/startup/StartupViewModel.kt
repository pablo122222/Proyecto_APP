package me.pablorincon.proyecto_app.ui.screens.startup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.pablorincon.proyecto_app.data.AuthRepository
import me.pablorincon.proyecto_app.data.UserRepository
import me.pablorincon.proyecto_app.data.UserRepository.ProfileResult
import me.pablorincon.proyecto_app.ui.navigation.AppRoutes

sealed interface StartupUiState {
    data object Loading : StartupUiState                          // Consultando el perfil
    data object Error : StartupUiState                            // No se pudo cargar: reintentar
    data class Ready(val startDestination: String) : StartupUiState
}

// Decide con qué pantalla abre la app (decisión A): sin sesión, Bienvenida; con
// sesión, hay que mirar el perfil para saber si falta Personalización.
class StartupViewModel(
    private val auth: AuthRepository = AuthRepository(),
    private val users: UserRepository = UserRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<StartupUiState>(StartupUiState.Loading)
    val uiState: StateFlow<StartupUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        val uid = auth.currentUid()
        if (uid == null) {
            _uiState.value = StartupUiState.Ready(AppRoutes.startDestination(isLoggedIn = false))
            return
        }
        _uiState.value = StartupUiState.Loading
        viewModelScope.launch {
            _uiState.value = when (val result = users.ensureProfile(uid, auth.currentName().orEmpty())) {
                is ProfileResult.Success -> StartupUiState.Ready(
                    AppRoutes.startDestination(
                        isLoggedIn = true,
                        hasPersonalization = result.profile.hasPersonalization
                    )
                )
                // Sin red y sin copia local no se asume que el usuario es nuevo
                else -> StartupUiState.Error
            }
        }
    }
}
