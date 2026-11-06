package me.pablorincon.proyecto_app.ui.screens.personalization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.pablorincon.proyecto_app.data.AuthRepository
import me.pablorincon.proyecto_app.data.UserRepository
import me.pablorincon.proyecto_app.data.UserRepository.SaveResult
import me.pablorincon.proyecto_app.domain.LearningGoal
import me.pablorincon.proyecto_app.domain.LearningLevel

// Lo que eligió el usuario en los pasos de Personalización.
data class PersonalizationUiState(
    val level: LearningLevel? = null,
    val goal: LearningGoal? = null,
    val isSaving: Boolean = false,  // Esperando que Firestore guarde
    val saveError: Boolean = false, // No se pudo guardar: se muestra el aviso
    val saved: Boolean = false      // Guardado: la pantalla debe avanzar
) {
    val canContinueFromLevel: Boolean get() = level != null
    val canCreateRoute: Boolean get() = level != null && goal != null && !isSaving
}

// Compartido por Nivel, Objetivo y Ruta lista (vive mientras dura el grafo de
// Personalización). Las elecciones se guardan juntas al terminar el paso 2:
// así nunca queda un perfil a medias.
class PersonalizationViewModel(
    private val auth: AuthRepository = AuthRepository(),
    private val users: UserRepository = UserRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(PersonalizationUiState())
    val uiState: StateFlow<PersonalizationUiState> = _uiState.asStateFlow()

    fun selectLevel(level: LearningLevel) = _uiState.update { it.copy(level = level) }

    fun selectGoal(goal: LearningGoal) = _uiState.update { it.copy(goal = goal, saveError = false) }

    fun saveRoute() {
        val state = _uiState.value
        val level = state.level
        val goal = state.goal
        if (level == null || goal == null || state.isSaving) return // Evita doble toque

        val uid = auth.currentUid()
        if (uid == null) {
            _uiState.update { it.copy(saveError = true) }
            return
        }
        _uiState.update { it.copy(isSaving = true, saveError = false) }
        viewModelScope.launch {
            val result = users.savePersonalization(uid, level, goal)
            _uiState.update {
                if (result == SaveResult.Saved) {
                    it.copy(isSaving = false, saved = true)
                } else {
                    it.copy(isSaving = false, saveError = true)
                }
            }
        }
    }

    // La pantalla avisa que ya navegó, para no repetirlo al girar el teléfono.
    fun onSavedHandled() = _uiState.update { it.copy(saved = false) }
}
