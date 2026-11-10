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
import me.pablorincon.proyecto_app.domain.glitchcity.Creation
import me.pablorincon.proyecto_app.domain.glitchcity.GlitchCityCatalog
import me.pablorincon.proyecto_app.domain.glitchcity.GlitchReward
import me.pablorincon.proyecto_app.domain.glitchcity.Lab
import me.pablorincon.proyecto_app.domain.glitchcity.LabRun
import me.pablorincon.proyecto_app.domain.glitchcity.LabSimulation
import me.pablorincon.proyecto_app.domain.glitchcity.LabValues
import me.pablorincon.proyecto_app.domain.glitchcity.Labs
import me.pablorincon.proyecto_app.domain.glitchcity.WhyQuestion
import me.pablorincon.proyecto_app.domain.glitchcity.WhyQuestions

// Avisos cortos (toast de la guía §0).
sealed interface LabMessage {
    data class Discovered(val reward: GlitchReward) : LabMessage
    data class Published(val reward: GlitchReward) : LabMessage
    data object Updated : LabMessage
    data object NoConnection : LabMessage
    data object Failed : LabMessage
}

// Pregunta "¿Por qué funciona?" (decisión D). answer: opción elegida (null = sin responder).
data class WhyState(val question: WhyQuestion, val answer: Int? = null) {
    val isCorrect: Boolean get() = answer == question.correctIndex
}

sealed interface LabUiState {
    data object Loading : LabUiState
    data object Error : LabUiState
    data class Ready(
        val simulation: LabSimulation,
        val isCreation: Boolean,
        val values: LabValues,
        val run: LabRun,
        val done: Set<String>,                       // Explorar: descubrimientos ya guardados
        val published: Boolean = false,              // Crear: ya se publicó alguna vez
        val publishing: Boolean = false,
        val why: WhyState? = null,
        val message: LabMessage? = null,
        val unlocks: List<AchievementUnlock> = emptyList()
    ) : LabUiState {
        // Crear: los objetivos se marcan en vivo; se publica cuando se cumplen todos.
        val canPublish: Boolean get() = isCreation && !publishing && run.met.containsAll(simulation.goalIds)
    }
}

// Laboratorio de Explorar o de Crear (guía §5.2): cada cambio de un control vuelve a ejecutar
// el código. Explorar: un descubrimiento se guarda en cuanto se cumple, pero solo después de
// que el usuario mueva algún control (con los valores iniciales el Cajero ya cumpliría uno).
// Crear: se publica con todos los objetivos cumplidos; "Editar" abre los valores publicados.
class LabViewModel(
    private val activityId: String,
    private val auth: AuthRepository = AuthRepository(),
    private val repository: GlitchCityRepository = GlitchCityRepository()
) : ViewModel() {

    private val simulation: LabSimulation? = Labs.byId(activityId)
    private val lab: Lab? = GlitchCityCatalog.lab(activityId)
    private val creation: Creation? = GlitchCityCatalog.creation(activityId)

    private val _uiState = MutableStateFlow<LabUiState>(LabUiState.Loading)
    val uiState: StateFlow<LabUiState> = _uiState.asStateFlow()

    // Opciones probadas en esta visita, por control (para "prueba las tres funciones")
    private var tried: Map<String, Set<String>> = initialTried()
    private val saving = mutableSetOf<String>()       // Descubrimientos que se están guardando
    private var alreadyExplained = false              // La pregunta de esta creación ya se acertó antes

    init {
        load()
    }

    fun load() {
        val uid = auth.currentUid()
        if (simulation == null || (lab == null && creation == null) || uid == null) {
            _uiState.value = LabUiState.Error
            return
        }
        _uiState.value = LabUiState.Loading
        viewModelScope.launch {
            _uiState.value = when (val result = repository.load(uid)) {
                is LoadResult.Success -> {
                    val progress = result.city.progress
                    alreadyExplained = activityId in result.city.profile.explainedWhy
                    // Una creación publicada se abre con sus valores guardados
                    val saved = progress.creationValues[activityId].orEmpty()
                        .filterKeys { id -> simulation.params.any { it.id == id } }
                    val values = LabValues.initial(simulation.params).let { it.copy(numbers = it.numbers + saved) }
                    LabUiState.Ready(
                        simulation = simulation,
                        isCreation = creation != null,
                        values = values,
                        run = simulation.run(values, tried),
                        done = lab?.let { l -> l.discoveryIds.filter { l.key(it) in progress.discoveries }.toSet() }.orEmpty(),
                        published = creation?.let(progress::isPublished) ?: false
                    )
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
        if (!ready.isCreation) (run.met - ready.done - saving).forEach(::discover)
    }

    private fun discover(discoveryId: String) {
        val uid = auth.currentUid() ?: return
        val lab = lab ?: return
        saving += discoveryId
        viewModelScope.launch {
            val result = repository.discover(uid, lab, discoveryId)
            saving -= discoveryId
            update { state ->
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

    // Publicar (primera vez: recompensa y pregunta) o actualizar (solo guarda los valores).
    fun publish() {
        val ready = _uiState.value as? LabUiState.Ready ?: return
        val uid = auth.currentUid() ?: return
        val creation = creation ?: return
        if (!ready.canPublish) return
        _uiState.value = ready.copy(publishing = true, message = null)
        viewModelScope.launch {
            val result = repository.publish(uid, creation, ready.values.numbers)
            update { state ->
                when (result) {
                    is SaveResult.Saved -> {
                        val reward = result.reward
                        state.copy(
                            publishing = false,
                            published = true,
                            message = if (reward != null) LabMessage.Published(reward) else LabMessage.Updated,
                            unlocks = state.unlocks + result.unlocks,
                            // La pregunta aparece solo la primera vez que se publica
                            why = WhyQuestions.forActivity(creation.id)
                                ?.takeIf { reward != null && !alreadyExplained }
                                ?.let { WhyState(it) }
                        )
                    }
                    SaveResult.NoConnection -> state.copy(publishing = false, message = LabMessage.NoConnection)
                    SaveResult.Failed -> state.copy(publishing = false, message = LabMessage.Failed)
                }
            }
        }
    }

    // Una sola respuesta. Acertar suma al logro "Explica el porqué"; fallar no penaliza.
    fun answerWhy(option: Int) {
        val ready = _uiState.value as? LabUiState.Ready ?: return
        val why = ready.why ?: return
        if (why.answer != null) return
        val answered = why.copy(answer = option)
        _uiState.value = ready.copy(why = answered)
        val uid = auth.currentUid() ?: return
        if (!answered.isCorrect) return
        alreadyExplained = true
        viewModelScope.launch {
            // Si no se pudo guardar no se avisa: la pregunta es opcional
            val result = repository.explainWhy(uid, activityId)
            if (result is SaveResult.Saved) update { it.copy(unlocks = it.unlocks + result.unlocks) }
        }
    }

    fun closeWhy() = update { it.copy(why = null) }

    fun onMessageShown() = update { it.copy(message = null) }

    fun onUnlocksSeen() = update { it.copy(unlocks = emptyList()) }

    private fun update(block: (LabUiState.Ready) -> LabUiState.Ready) =
        _uiState.update { if (it is LabUiState.Ready) block(it) else it }

    // Cada selector empieza con su opción inicial ya "probada": su resultado se ve al entrar.
    private fun initialTried(): Map<String, Set<String>> =
        simulation?.params.orEmpty().filterIsInstance<ChoiceParam>().associate { it.id to setOf(it.initial) }
}
