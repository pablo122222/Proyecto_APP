package me.pablorincon.proyecto_app.ui.screens.learningpath

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.pablorincon.proyecto_app.data.AuthRepository
import me.pablorincon.proyecto_app.data.ProgressRepository
import me.pablorincon.proyecto_app.data.ProgressRepository.ProgressResult
import me.pablorincon.proyecto_app.domain.Curriculum
import me.pablorincon.proyecto_app.domain.CurriculumProgress

sealed interface LearningPathUiState {
    data object Loading : LearningPathUiState
    data object Error : LearningPathUiState
    data class Ready(val progress: CurriculumProgress) : LearningPathUiState
}

// Carga el progreso del usuario y lo entrega ya calculado (estados, % y siguiente lección).
class LearningPathViewModel(
    private val auth: AuthRepository = AuthRepository(),
    private val progress: ProgressRepository = ProgressRepository(),
    private val curriculum: Curriculum = Curriculum.Base
) : ViewModel() {

    private val _uiState = MutableStateFlow<LearningPathUiState>(LearningPathUiState.Loading)
    val uiState: StateFlow<LearningPathUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        val uid = auth.currentUid()
        if (uid == null) { // Sin sesión no hay progreso que mostrar
            _uiState.value = LearningPathUiState.Error
            return
        }
        _uiState.value = LearningPathUiState.Loading
        viewModelScope.launch {
            _uiState.value = when (val result = progress.loadCompletedLessons(uid)) {
                is ProgressResult.Success ->
                    LearningPathUiState.Ready(CurriculumProgress(curriculum, result.completedLessonIds))
                else -> LearningPathUiState.Error
            }
        }
    }
}
