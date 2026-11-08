package me.pablorincon.proyecto_app.ui.screens.lesson

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.pablorincon.proyecto_app.data.AuthRepository
import me.pablorincon.proyecto_app.data.ProgressRepository
import me.pablorincon.proyecto_app.data.ProgressRepository.CompleteResult
import me.pablorincon.proyecto_app.data.ProgressRepository.ProgressResult
import me.pablorincon.proyecto_app.domain.Curriculum
import me.pablorincon.proyecto_app.domain.CurriculumProgress
import me.pablorincon.proyecto_app.domain.Lesson
import me.pablorincon.proyecto_app.domain.LessonContent
import me.pablorincon.proyecto_app.domain.LessonQuiz
import me.pablorincon.proyecto_app.domain.content.LessonCatalog

// Qué pasó al guardar la lección completada.
sealed interface SaveState {
    data object Idle : SaveState                       // Aún no termina el quiz
    data object Saving : SaveState
    data class Saved(val xpAwarded: Int) : SaveState   // 0 si ya estaba completada antes
    data object NoConnection : SaveState
    data object Failed : SaveState
}

data class LessonReady(
    val lesson: Lesson,
    val content: LessonContent,
    val numberInModule: Int,       // "Lección X de N" dentro de su módulo
    val moduleLessonCount: Int,
    val quiz: LessonQuiz,
    val save: SaveState = SaveState.Idle,
    val nextLessonId: String?      // Null si es la última de la ruta
)

sealed interface LessonUiState {
    data object Loading : LessonUiState
    data object Error : LessonUiState
    data object Locked : LessonUiState        // Una lección bloqueada no se puede abrir
    data object ComingSoon : LessonUiState    // Existe en la ruta pero su contenido aún no está
    data class Ready(val data: LessonReady) : LessonUiState
}

class LessonViewModel(
    private val lessonId: String,
    private val auth: AuthRepository = AuthRepository(),
    private val progressRepository: ProgressRepository = ProgressRepository(),
    private val curriculum: Curriculum = Curriculum.Base
) : ViewModel() {

    private val _uiState = MutableStateFlow<LessonUiState>(LessonUiState.Loading)
    val uiState: StateFlow<LessonUiState> = _uiState.asStateFlow()

    private var alreadyCompleted = false

    init {
        load()
    }

    fun load() {
        val uid = auth.currentUid()
        val lesson = curriculum.lesson(lessonId)
        if (uid == null || lesson == null) {
            _uiState.value = LessonUiState.Error
            return
        }
        _uiState.value = LessonUiState.Loading
        viewModelScope.launch {
            val result = progressRepository.loadCompletedLessons(uid)
            if (result !is ProgressResult.Success) {
                _uiState.value = LessonUiState.Error
                return@launch
            }
            val progress = CurriculumProgress(curriculum, result.completedLessonIds)
            val content = LessonCatalog.contentFor(lessonId)
            _uiState.value = when {
                !progress.isLessonUnlocked(lessonId) -> LessonUiState.Locked
                content == null -> LessonUiState.ComingSoon
                else -> {
                    alreadyCompleted = progress.isCompleted(lessonId)
                    val module = curriculum.moduleOf(lessonId)!!
                    LessonUiState.Ready(
                        LessonReady(
                            lesson = lesson,
                            content = content,
                            numberInModule = module.lessons.indexOfFirst { it.id == lessonId } + 1,
                            moduleLessonCount = module.lessons.size,
                            quiz = LessonQuiz(content.exercises),
                            nextLessonId = curriculum.lessonAfter(lessonId)?.id
                        )
                    )
                }
            }
        }
    }

    fun onSubmit(correct: Boolean) = updateReady { it.copy(quiz = it.quiz.submit(correct)) }

    // "Continuar" tras ver la retroalimentación. Si era la última pregunta acertada, se guarda.
    fun onNext() {
        val ready = (_uiState.value as? LessonUiState.Ready)?.data ?: return
        val quiz = ready.quiz.next()
        updateReady { it.copy(quiz = quiz) }
        if (quiz.isComplete) complete()
    }

    fun onRetrySave() = complete()

    private fun complete() {
        val ready = (_uiState.value as? LessonUiState.Ready)?.data ?: return
        if (ready.save is SaveState.Saving || ready.save is SaveState.Saved) return
        if (alreadyCompleted) { // Repasar una lección no vuelve a dar XP
            updateReady { it.copy(save = SaveState.Saved(0)) }
            return
        }
        val uid = auth.currentUid() ?: return updateReady { it.copy(save = SaveState.Failed) }
        updateReady { it.copy(save = SaveState.Saving) }
        viewModelScope.launch {
            val save = when (val result = progressRepository.completeLesson(uid, lessonId, ready.lesson.xp)) {
                is CompleteResult.Completed -> SaveState.Saved(result.xpAwarded)
                CompleteResult.AlreadyCompleted -> SaveState.Saved(0)
                CompleteResult.NoConnection -> SaveState.NoConnection
                CompleteResult.Failed -> SaveState.Failed
            }
            updateReady { it.copy(save = save) }
        }
    }

    private fun updateReady(transform: (LessonReady) -> LessonReady) {
        _uiState.update { state ->
            if (state is LessonUiState.Ready) LessonUiState.Ready(transform(state.data)) else state
        }
    }
}
