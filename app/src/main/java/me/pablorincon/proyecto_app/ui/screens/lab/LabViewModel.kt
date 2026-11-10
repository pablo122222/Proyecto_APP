package me.pablorincon.proyecto_app.ui.screens.lab

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
import me.pablorincon.proyecto_app.data.GlitchCityRepository.SaveResult
import me.pablorincon.proyecto_app.domain.AchievementUnlock
import me.pablorincon.proyecto_app.domain.glitchcity.ChoiceParam
import me.pablorincon.proyecto_app.domain.glitchcity.GlitchCityCatalog
import me.pablorincon.proyecto_app.domain.glitchcity.GlitchReward
import me.pablorincon.proyecto_app.domain.glitchcity.Lab
import me.pablorincon.proyecto_app.domain.glitchcity.LabRun
import me.pablorincon.proyecto_app.domain.glitchcity.LabSimulation
import me.pablorincon.proyecto_app.domain.glitchcity.LabValues
import me.pablorincon.proyecto_app.domain.glitchcity.Labs

// Avisos cortos (toast de la guía §0).
sealed interface LabMessage {
    data class Discovered(val reward: GlitchReward) : LabMessage
    data object NoConnection : LabMessage
    data object Failed : LabMessage
}

sealed interface LabUiState {
    data object Loading : LabUiState
    data object Error : LabUiState
    data class Ready(
        val simulation: LabSimulation,
        val values: LabValues,
        val run: LabRun,
        val done: Set<String>,                       // Descubrimientos ya guardados
        val message: LabMessage? = null,
        val unlocks: List<AchievementUnlock> = emptyList()
    ) : LabUiState
}

// Laboratorio de Explorar (guía §5.2): cada cambio de un control vuelve a ejecutar el código.
// Un descubrimiento se guarda en cuanto se cumple, pero solo después de que el usuario mueva
// algún control (con los valores iniciales el Cajero ya cumpliría uno: no se regala).
class LabViewModel(
    private val activityId: String,
    private val auth: AuthRepository = AuthRepository(),
    private val repository: GlitchCityRepository = GlitchCityRepository()
) : ViewModel() {

    private val simulation: LabSimulation? = Labs.byId(activityId)
    private val lab: Lab? = GlitchCityCatalog.lab(activityId)

    private val _uiState = MutableStateFlow<LabUiState>(LabUiState.Loading)
    val uiState: StateFlow<LabUiState> = _uiState.asStateFlow()

    // Opciones probadas en esta visita, por control (para "prueba las tres funciones")
    private var tried: Map<String, Set<String>> = initialTried()
    private val saving = mutableSetOf<String>()       // Descubrimientos que se están guardando

    init {
        load()
    }

    fun load() {
        val uid = auth.currentUid()
        if (simulation == null || lab == null || uid == null) {
            _uiState.value = LabUiState.Error
            return
        }
        _uiState.value = LabUiState.Loading
        viewModelScope.launch {
            _uiState.value = when (val result = repository.load(uid)) {
                is LoadResult.Success -> {
                    val values = LabValues.initial(simulation.params)
                    val done = lab.discoveryIds.filter { lab.key(it) in result.city.progress.discoveries }.toSet()
                    LabUiState.Ready(simulation, values, simulation.run(values, tried), done)
                }
                else -> LabUiState.Error
            }
        }
    }

    fun onNumberChange(id: String, value: Int) = change { it.with(id, value) }

    fun onTextChange(id: String, value: String) = change { it.with(id, value) }

    private fun change(update: (LabValues) -> LabValues) {
        val ready = _uiState.value as? LabUiState.Ready ?: return
        val values = update(ready.values)
        val run = ready.simulation.run(values, tried)
        tried = tried.mapValues { (id, seen) -> seen + values.text(id) }
        _uiState.value = ready.copy(values = values, run = run)
        (run.met - ready.done - saving).forEach(::save)
    }

    private fun save(discoveryId: String) {
        val uid = auth.currentUid() ?: return
        val lab = lab ?: return
        saving += discoveryId
        viewModelScope.launch {
            val result = repository.discover(uid, lab, discoveryId)
            saving -= discoveryId
            _uiState.update { state ->
                if (state !is LabUiState.Ready) return@update state
                when (result) {
                    // reward null: ya estaba guardado (por ejemplo, desde otro teléfono)
                    is SaveResult.Saved -> state.copy(
                        done = state.done + discoveryId,
                        message = result.reward?.let { LabMessage.Discovered(it) } ?: state.message,
                        unlocks = state.unlocks + result.unlocks
                    )
                    // Sin guardar: se reintenta la próxima vez que se cumpla
                    SaveResult.NoConnection -> state.copy(message = LabMessage.NoConnection)
                    SaveResult.Failed -> state.copy(message = LabMessage.Failed)
                }
            }
        }
    }

    fun onMessageShown() = _uiState.update { if (it is LabUiState.Ready) it.copy(message = null) else it }

    fun onUnlocksSeen() = _uiState.update { if (it is LabUiState.Ready) it.copy(unlocks = emptyList()) else it }

    // Cada selector empieza con su opción inicial ya "probada": su resultado se ve al entrar.
    private fun initialTried(): Map<String, Set<String>> =
        simulation?.params.orEmpty().filterIsInstance<ChoiceParam>().associate { it.id to setOf(it.initial) }
}
