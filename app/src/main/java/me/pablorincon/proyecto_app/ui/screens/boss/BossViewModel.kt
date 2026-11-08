package me.pablorincon.proyecto_app.ui.screens.boss

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.pablorincon.proyecto_app.data.AdminAccess
import me.pablorincon.proyecto_app.data.AuthRepository
import me.pablorincon.proyecto_app.data.CurriculumProgressLoader
import me.pablorincon.proyecto_app.data.CurriculumProgressLoader.LoadResult
import me.pablorincon.proyecto_app.data.ProgressRepository
import me.pablorincon.proyecto_app.data.ProgressRepository.CompleteResult
import me.pablorincon.proyecto_app.domain.Boss
import me.pablorincon.proyecto_app.domain.BossContent
import me.pablorincon.proyecto_app.domain.Curriculum
import me.pablorincon.proyecto_app.domain.LessonQuiz
import me.pablorincon.proyecto_app.domain.PathStep
import me.pablorincon.proyecto_app.domain.content.BossCatalog
import me.pablorincon.proyecto_app.ui.screens.lesson.SaveState

data class BossReady(
    val boss: Boss,
    val content: BossContent,
    val level: Int,                // Nivel al que pertenece (1 a 15)
    val quiz: LessonQuiz,
    val save: SaveState = SaveState.Idle,
    val nextStep: PathStep?        // Null si es el último paso de la ruta
) {
    // Vida que le queda al jefe (100 → 0 a medida que se aciertan ejercicios)
    val hpPercent: Int get() = 100 - quiz.correctCount * 100 / quiz.total
}

sealed interface BossUiState {
    data object Loading : BossUiState
    data object Error : BossUiState
    data object Locked : BossUiState        // Sin terminar las lecciones del nivel no se puede enfrentar
    data object ComingSoon : BossUiState    // Existe en la ruta pero su contenido aún no está
    data class Ready(val data: BossReady) : BossUiState
}

// Igual que la lección: cola de ejercicios (fallados al final), XP una sola vez al terminar.
class BossViewModel(
    private val bossId: String,
    private val auth: AuthRepository = AuthRepository(),
    private val progressRepository: ProgressRepository = ProgressRepository(),
    private val loader: CurriculumProgressLoader = CurriculumProgressLoader(),
    private val curriculum: Curriculum = Curriculum.Base
) : ViewModel() {

    private val _uiState = MutableStateFlow<BossUiState>(BossUiState.Loading)
    val uiState: StateFlow<BossUiState> = _uiState.asStateFlow()

    private var alreadyCompleted = false

    init {
        load()
    }

    fun load() {
        val uid = auth.currentUid()
        val boss = curriculum.step(bossId) as? Boss
        if (uid == null || boss == null) {
            _uiState.value = BossUiState.Error
            return
        }
        _uiState.value = BossUiState.Loading
        viewModelScope.launch {
            val loaded = loader.load(uid, auth.currentName().orEmpty(), AdminAccess.isAdmin(auth.currentEmail()))
            if (loaded !is LoadResult.Loaded) {
                _uiState.value = BossUiState.Error
                return@launch
            }
            val progress = loaded.progress
            val content = BossCatalog.contentFor(bossId)
            _uiState.value = when {
                !progress.isStepUnlocked(bossId) -> BossUiState.Locked
                content == null -> BossUiState.ComingSoon
                else -> {
                    alreadyCompleted = progress.isCompleted(bossId)
                    BossUiState.Ready(
                        BossReady(
                            boss = boss,
                            content = content,
                            level = curriculum.modules.indexOf(curriculum.moduleOf(bossId)) + 1,
                            quiz = LessonQuiz(content.exercises),
                            nextStep = curriculum.stepAfter(bossId)
                        )
                    )
                }
            }
        }
    }

    fun onSubmit(correct: Boolean) = updateReady { it.copy(quiz = it.quiz.submit(correct)) }

    // "Continuar" tras ver la retroalimentación. Si era el último ejercicio resuelto, se guarda.
    fun onNext() {
        val ready = (_uiState.value as? BossUiState.Ready)?.data ?: return
        val quiz = ready.quiz.next()
        updateReady { it.copy(quiz = quiz) }
        if (quiz.isComplete) complete()
    }

    fun onRetrySave() = complete()

    private fun complete() {
        val ready = (_uiState.value as? BossUiState.Ready)?.data ?: return
        if (ready.save is SaveState.Saving || ready.save is SaveState.Saved) return
        if (alreadyCompleted) { // Repetir un jefe vencido no vuelve a dar XP
            updateReady { it.copy(save = SaveState.Saved(0)) }
            return
        }
        val uid = auth.currentUid() ?: return updateReady { it.copy(save = SaveState.Failed) }
        updateReady { it.copy(save = SaveState.Saving) }
        viewModelScope.launch {
            val save = when (val result = progressRepository.completeLesson(uid, bossId, ready.boss.xp)) {
                is CompleteResult.Completed -> SaveState.Saved(result.xpAwarded)
                CompleteResult.AlreadyCompleted -> SaveState.Saved(0)
                CompleteResult.NoConnection -> SaveState.NoConnection
                CompleteResult.Failed -> SaveState.Failed
            }
            updateReady { it.copy(save = save) }
        }
    }

    private fun updateReady(transform: (BossReady) -> BossReady) {
        _uiState.update { state ->
            if (state is BossUiState.Ready) BossUiState.Ready(transform(state.data)) else state
        }
    }
}
