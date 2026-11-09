package me.pablorincon.proyecto_app.ui.screens.achievements

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
import me.pablorincon.proyecto_app.domain.AchievementProgress
import me.pablorincon.proyecto_app.domain.AchievementStats
import me.pablorincon.proyecto_app.domain.Achievements
import me.pablorincon.proyecto_app.domain.Curriculum

sealed interface AchievementsUiState {
    data object Loading : AchievementsUiState
    data object Error : AchievementsUiState
    data class Ready(
        val items: List<AchievementProgress>,
        val gems: Int,
        val selected: AchievementProgress? = null   // Detalle abierto (hoja inferior)
    ) : AchievementsUiState {
        val unlocked: Int get() = items.count { it.tiersReached > 0 }
        val tiersReached: Int get() = items.sumOf { it.tiersReached }
        val tiersTotal: Int get() = Achievements.availableTiers
    }
}

// Carga los logros del usuario. Antes de mostrarlos entrega los que estén pendientes
// (así nunca se ve un logro "alcanzado" sin su recompensa).
class AchievementsViewModel(
    private val auth: AuthRepository = AuthRepository(),
    private val loader: CurriculumProgressLoader = CurriculumProgressLoader(),
    private val progress: ProgressRepository = ProgressRepository(),
    private val curriculum: Curriculum = Curriculum.Base
) : ViewModel() {

    private val _uiState = MutableStateFlow<AchievementsUiState>(AchievementsUiState.Loading)
    val uiState: StateFlow<AchievementsUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        val uid = auth.currentUid() ?: return run { _uiState.value = AchievementsUiState.Error }
        _uiState.value = AchievementsUiState.Loading
        viewModelScope.launch {
            progress.claimPendingAchievements(uid)
            _uiState.value = when (val result = loader.load(uid, auth.currentName().orEmpty(), AdminAccess.isAdmin(auth.currentEmail()))) {
                is LoadResult.Loaded -> {
                    val profile = result.profile
                    val done = curriculum.steps.filter { result.progress.isCompleted(it.id) }
                    AchievementsUiState.Ready(
                        items = Achievements.progress(AchievementStats.of(profile, done), profile.achievementTiers),
                        gems = profile.gems
                    )
                }
                LoadResult.Failed -> AchievementsUiState.Error
            }
        }
    }

    fun select(item: AchievementProgress?) = _uiState.update {
        if (it is AchievementsUiState.Ready) it.copy(selected = item) else it
    }
}
