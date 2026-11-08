package me.pablorincon.proyecto_app.ui.screens.learningpath

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.pablorincon.proyecto_app.data.AuthRepository
import me.pablorincon.proyecto_app.data.CurriculumProgressLoader
import me.pablorincon.proyecto_app.data.CurriculumProgressLoader.LoadResult
import me.pablorincon.proyecto_app.domain.CurriculumProgress

sealed interface LearningPathUiState {
    data object Loading : LearningPathUiState
    data object Error : LearningPathUiState
    data class Ready(val progress: CurriculumProgress) : LearningPathUiState
}

// Carga el progreso del usuario y lo entrega ya calculado (estados, % y siguiente lección).
class LearningPathViewModel(
    private val auth: AuthRepository = AuthRepository(),
    private val loader: CurriculumProgressLoader = CurriculumProgressLoader()
) : ViewModel() {

    private val _uiState = MutableStateFlow<LearningPathUiState>(LearningPathUiState.Loading)
    val uiState: StateFlow<LearningPathUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    // silent: refresca sin mostrar el indicador de carga (al volver de una lección).
    fun load(silent: Boolean = false) {
        val uid = auth.currentUid()
        if (uid == null) { // Sin sesión no hay progreso que mostrar
            _uiState.value = LearningPathUiState.Error
            return
        }
        if (!silent || _uiState.value !is LearningPathUiState.Ready) _uiState.value = LearningPathUiState.Loading
        viewModelScope.launch {
            _uiState.value = when (val result = loader.load(uid, auth.currentName().orEmpty())) {
                is LoadResult.Loaded -> LearningPathUiState.Ready(result.progress)
                else -> if (silent && _uiState.value is LearningPathUiState.Ready) _uiState.value else LearningPathUiState.Error
            }
        }
    }
}
