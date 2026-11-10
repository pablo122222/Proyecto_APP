package me.pablorincon.proyecto_app.ui.screens.glitchcity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.pablorincon.proyecto_app.data.AuthRepository
import me.pablorincon.proyecto_app.data.GlitchCityRepository
import me.pablorincon.proyecto_app.data.GlitchCityRepository.LoadResult
import me.pablorincon.proyecto_app.domain.glitchcity.GlitchCityState
import java.time.LocalDate

// Pestañas de la pantalla (fila de 4 botones de la guía §5.1).
enum class CityTab { Explore, Create, Compete }

data class GlitchCityUiState(
    val tab: CityTab = CityTab.Explore,
    val content: CityContent = CityContent.Loading
)

sealed interface CityContent {
    data object Loading : CityContent
    data object Error : CityContent
    data class Ready(val city: GlitchCityState, val streak: Int) : CityContent
}

// Carga el perfil y el progreso de Glitch City. La racha se calcula con el día de hoy del teléfono.
class GlitchCityViewModel(
    private val auth: AuthRepository = AuthRepository(),
    private val repository: GlitchCityRepository = GlitchCityRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(GlitchCityUiState())
    val uiState: StateFlow<GlitchCityUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    // silent: refresca sin mostrar el indicador de carga (al volver de una actividad u otra pestaña).
    fun load(silent: Boolean = false) {
        val uid = auth.currentUid()
        if (uid == null) {
            _uiState.update { it.copy(content = CityContent.Error) }
            return
        }
        val hasData = _uiState.value.content is CityContent.Ready
        if (!silent || !hasData) _uiState.update { it.copy(content = CityContent.Loading) }
        viewModelScope.launch {
            val content = when (val result = repository.load(uid)) {
                is LoadResult.Success -> CityContent.Ready(
                    city = result.city,
                    streak = result.city.profile.dailyActivity.streakOn(LocalDate.now())
                )
                // Si ya había datos en pantalla, un refresco fallido no los borra
                else -> if (silent && hasData) _uiState.value.content else CityContent.Error
            }
            _uiState.update { it.copy(content = content) }
        }
    }

    fun selectTab(tab: CityTab) = _uiState.update { it.copy(tab = tab) }
}
