package me.pablorincon.proyecto_app.ui.screens.drone

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
import me.pablorincon.proyecto_app.domain.glitchcity.Drone
import me.pablorincon.proyecto_app.domain.glitchcity.DroneLevel
import me.pablorincon.proyecto_app.domain.glitchcity.DroneLevelStatus
import me.pablorincon.proyecto_app.domain.glitchcity.DroneMove
import me.pablorincon.proyecto_app.domain.glitchcity.DroneOutcome
import me.pablorincon.proyecto_app.domain.glitchcity.DroneProgram
import me.pablorincon.proyecto_app.domain.glitchcity.DroneRun
import me.pablorincon.proyecto_app.domain.glitchcity.GlitchCityCatalog
import me.pablorincon.proyecto_app.domain.glitchcity.GlitchReward
import me.pablorincon.proyecto_app.domain.glitchcity.WhyQuestions
import me.pablorincon.proyecto_app.ui.screens.lab.WhyState

// Cómo se compara una entrega con el récord que había antes de ejecutar.
enum class RecordNote { NewRecord, NotBetter }

// Última ejecución: lo que se anima en la cuadrícula y el mensaje que se muestra al terminar.
// id cambia en cada ejecución para que la pantalla vuelva a animar aunque el resultado se repita.
data class DroneAttempt(val id: Int, val run: DroneRun, val previousRecord: Int?) {
    val recordNote: RecordNote?
        get() = (run.outcome as? DroneOutcome.Delivered)?.let { delivered ->
            if (previousRecord == null || delivered.blocks < previousRecord) RecordNote.NewRecord else RecordNote.NotBetter
        }
}

sealed interface DroneMessage {
    data class Rewarded(val reward: GlitchReward) : DroneMessage
    data object NoConnection : DroneMessage
    data object Failed : DroneMessage
}

sealed interface DroneUiState {
    data object Loading : DroneUiState
    data object Error : DroneUiState
    data object Locked : DroneUiState
    data class Ready(
        val level: DroneLevel,
        val number: Int,                      // 1 a 5
        val program: DroneProgram,
        val repeat: Int = 1,                  // ×n elegido para el próximo bloque
        val record: Int? = null,
        val attempt: DroneAttempt? = null,
        val showIdea: Boolean = false,
        val why: WhyState? = null,
        val message: DroneMessage? = null,
        val unlocks: List<AchievementUnlock> = emptyList()
    ) : DroneUiState
}

// Nivel del dron (guía §5.3): se arma un programa de bloques y se ejecuta. Una entrega se guarda
// al momento: la primera vez completa el nivel; después solo cuenta si mejora el récord.
class DroneViewModel(
    private val levelId: String,
    private val auth: AuthRepository = AuthRepository(),
    private val repository: GlitchCityRepository = GlitchCityRepository()
) : ViewModel() {

    private val level: DroneLevel? = GlitchCityCatalog.droneLevel(levelId)

    private val _uiState = MutableStateFlow<DroneUiState>(DroneUiState.Loading)
    val uiState: StateFlow<DroneUiState> = _uiState.asStateFlow()

    private var attempts = 0
    private var alreadyExplained = false   // La pregunta de este nivel ya se acertó antes
    private var clean = true               // Sin ejecuciones fallidas ni idea abierta (logro "Solución autónoma")

    init {
        load()
    }

    fun load() {
        val uid = auth.currentUid()
        val level = level
        if (level == null || uid == null) {
            _uiState.value = DroneUiState.Error
            return
        }
        _uiState.value = DroneUiState.Loading
        viewModelScope.launch {
            _uiState.value = when (val result = repository.load(uid)) {
                is LoadResult.Success -> {
                    val progress = result.city.progress
                    val index = GlitchCityCatalog.droneLevels.indexOf(level)
                    alreadyExplained = level.id in result.city.profile.explainedWhy
                    if (progress.status(index) == DroneLevelStatus.Locked) DroneUiState.Locked
                    else DroneUiState.Ready(level, index + 1, DroneProgram.startOf(level), record = progress.record(level))
                }
                else -> DroneUiState.Error
            }
        }
    }

    fun selectRepeat(times: Int) = update { it.copy(repeat = times) }

    // Al cambiar el programa se borra el resultado anterior: ya no corresponde
    fun addBlock(move: DroneMove) = update { it.copy(program = it.program.add(move, it.repeat), attempt = null) }

    fun removeLast() = update { it.copy(program = it.program.removeLast(), attempt = null) }

    fun clear() = update { it.copy(program = it.program.clear(), attempt = null) }

    fun toggleIdea() = update {
        if (!it.showIdea) clean = false
        it.copy(showIdea = !it.showIdea)
    }

    fun execute() {
        val ready = _uiState.value as? DroneUiState.Ready ?: return
        val run = Drone.run(ready.level, ready.program.blocks)
        attempts += 1
        _uiState.value = ready.copy(attempt = DroneAttempt(attempts, run, ready.record), message = null)
        val outcome = run.outcome
        if (outcome is DroneOutcome.Delivered && (ready.record == null || outcome.blocks < ready.record)) {
            save(ready.level, outcome.blocks, isFirst = ready.record == null, clean = clean)
        }
        // Ejecutar sin bloques no cuenta como intento fallido
        if (outcome !is DroneOutcome.Delivered && outcome !is DroneOutcome.Empty) clean = false
    }

    private fun save(level: DroneLevel, blocks: Int, isFirst: Boolean, clean: Boolean) {
        val uid = auth.currentUid() ?: return
        viewModelScope.launch {
            val result = repository.completeDrone(uid, level, blocks, clean)
            update { state ->
                when (result) {
                    is SaveResult.Saved -> state.copy(
                        record = minOf(state.record ?: blocks, blocks),
                        message = result.reward?.let { DroneMessage.Rewarded(it) },
                        unlocks = state.unlocks + result.unlocks,
                        // La pregunta aparece solo la primera vez que se completa el nivel
                        why = WhyQuestions.forActivity(level.id)
                            ?.takeIf { isFirst && result.reward != null && !alreadyExplained }
                            ?.let { WhyState(it) }
                    )
                    // Sin guardar: el récord no cambia y se puede volver a ejecutar
                    SaveResult.NoConnection -> state.copy(message = DroneMessage.NoConnection)
                    SaveResult.Failed -> state.copy(message = DroneMessage.Failed)
                }
            }
        }
    }

    // Una sola respuesta. Acertar suma al logro "Explica el porqué"; fallar no penaliza.
    fun answerWhy(option: Int) {
        val ready = _uiState.value as? DroneUiState.Ready ?: return
        val why = ready.why ?: return
        if (why.answer != null) return
        val answered = why.copy(answer = option)
        _uiState.value = ready.copy(why = answered)
        val uid = auth.currentUid() ?: return
        if (!answered.isCorrect) return
        alreadyExplained = true
        viewModelScope.launch {
            // Si no se pudo guardar no se avisa: la pregunta es opcional
            val result = repository.explainWhy(uid, levelId)
            if (result is SaveResult.Saved) update { it.copy(unlocks = it.unlocks + result.unlocks) }
        }
    }

    fun closeWhy() = update { it.copy(why = null) }

    fun onMessageShown() = update { it.copy(message = null) }

    fun onUnlocksSeen() = update { it.copy(unlocks = emptyList()) }

    private fun update(block: (DroneUiState.Ready) -> DroneUiState.Ready) =
        _uiState.update { if (it is DroneUiState.Ready) block(it) else it }
}
