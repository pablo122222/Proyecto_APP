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
import me.pablorincon.proyecto_app.data.GlitchCityRepository.Result
import me.pablorincon.proyecto_app.domain.glitchcity.GlitchCityState
import me.pablorincon.proyecto_app.domain.glitchcity.GlitchTitles
import java.time.LocalDate

// Pestañas de la pantalla (fila de 4 botones de la guía §5.1).
enum class CityTab { Explore, Create, Compete, Titles }

// Avisos cortos (toast de la guía §0).
enum class CityMessage { Reset, NoConnection, Failed }

data class GlitchCityUiState(
    val tab: CityTab = CityTab.Explore,
    val content: CityContent = CityContent.Loading,
    val showResetSheet: Boolean = false,
    val busy: Boolean = false,                 // Guardando: evita tocar dos veces
    val message: CityMessage? = null
)

sealed interface CityContent {
    data object Loading : CityContent
    data object Error : CityContent
    data class Ready(val city: GlitchCityState, val streak: Int) : CityContent
}

// Carga el perfil y el progreso de Glitch City, equipa títulos y reinicia el progreso.
// La racha se calcula con el día de hoy del teléfono.
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

    // Solo se equipa un título ganado; el cambio se ve al confirmar que se guardó.
    fun equipTitle(titleId: String) {
        val state = _uiState.value
        val ready = state.content as? CityContent.Ready ?: return
        val uid = auth.currentUid() ?: return
        if (state.busy || !GlitchTitles.canEquip(titleId, ready.city.progress)) return
        _uiState.update { it.copy(busy = true, message = null) }
        viewModelScope.launch {
            val result = repository.equipTitle(uid, titleId)
            _uiState.update { current ->
                val content = current.content
                if (result == Result.Done && content is CityContent.Ready) {
                    val profile = content.city.profile.copy(equippedTitleId = titleId)
                    current.copy(busy = false, content = content.copy(city = content.city.copy(profile = profile)))
                } else {
                    current.copy(busy = false, message = result.toMessage())
                }
            }
        }
    }

    fun openResetSheet() = _uiState.update { it.copy(showResetSheet = true, message = null) }

    fun closeResetSheet() = _uiState.update { it.copy(showResetSheet = false) }

    fun reset() {
        val uid = auth.currentUid() ?: return
        if (_uiState.value.busy) return
        _uiState.update { it.copy(busy = true, showResetSheet = false, message = null) }
        viewModelScope.launch {
            val result = repository.reset(uid)
            _uiState.update {
                it.copy(busy = false, message = if (result == Result.Done) CityMessage.Reset else result.toMessage())
            }
            if (result == Result.Done) load(silent = true)
        }
    }

    fun onMessageShown() = _uiState.update { it.copy(message = null) }

    private fun Result.toMessage(): CityMessage? = when (this) {
        Result.Done -> null
        Result.NoConnection -> CityMessage.NoConnection
        Result.Failed -> CityMessage.Failed
    }
}
