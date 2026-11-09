package me.pablorincon.proyecto_app.ui.screens.home

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
import me.pablorincon.proyecto_app.domain.AchievementUnlock
import me.pablorincon.proyecto_app.domain.Boss
import me.pablorincon.proyecto_app.domain.Curriculum
import me.pablorincon.proyecto_app.domain.HomeSummary
import me.pablorincon.proyecto_app.domain.Lesson
import me.pablorincon.proyecto_app.domain.PathStep
import me.pablorincon.proyecto_app.domain.content.BossCatalog
import me.pablorincon.proyecto_app.domain.content.LessonCatalog
import java.time.LocalDate

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data object Error : HomeUiState
    // unlocks: logros entregados al abrir Inicio (progreso anterior a los logros): se celebran
    data class Ready(val summary: HomeSummary, val unlocks: List<AchievementUnlock> = emptyList()) : HomeUiState
}

// Carga perfil y progreso y arma el resumen de Inicio con la fecha de hoy del teléfono.
class HomeViewModel(
    private val auth: AuthRepository = AuthRepository(),
    private val loader: CurriculumProgressLoader = CurriculumProgressLoader(),
    private val progress: ProgressRepository = ProgressRepository(),
    private val curriculum: Curriculum = Curriculum.Base
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    // silent: refresca sin mostrar el indicador de carga (al volver de una lección u otra pestaña).
    fun load(silent: Boolean = false) {
        val uid = auth.currentUid()
        if (uid == null) {
            _uiState.value = HomeUiState.Error
            return
        }
        if (!silent || _uiState.value !is HomeUiState.Ready) _uiState.value = HomeUiState.Loading
        viewModelScope.launch {
            // Primero se entregan los logros pendientes, así las gemas ya aparecen sumadas
            val unlocks = progress.claimPendingAchievements(uid)
            val result = loader.load(uid, auth.currentName().orEmpty(), AdminAccess.isAdmin(auth.currentEmail()))
            _uiState.value = when (result) {
                is LoadResult.Loaded -> HomeUiState.Ready(
                    HomeSummary.build(result.profile, result.progress, curriculum, LocalDate.now(), ::titleOf),
                    unlocks = unlocks.ifEmpty { (_uiState.value as? HomeUiState.Ready)?.unlocks.orEmpty() }
                )
                else -> if (silent && _uiState.value is HomeUiState.Ready) _uiState.value else HomeUiState.Error
            }
        }
    }

    fun onUnlocksSeen() = _uiState.update { if (it is HomeUiState.Ready) it.copy(unlocks = emptyList()) else it }

    // Título del paso si ya tiene contenido escrito (lección: su título; jefe: su nombre).
    private fun titleOf(step: PathStep): String? = when (step) {
        is Lesson -> LessonCatalog.contentFor(step.id)?.title
        is Boss -> BossCatalog.contentFor(step.id)?.name
    }
}
