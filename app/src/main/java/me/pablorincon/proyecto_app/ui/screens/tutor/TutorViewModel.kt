package me.pablorincon.proyecto_app.ui.screens.tutor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.pablorincon.proyecto_app.data.DemoTutorRepository
import me.pablorincon.proyecto_app.data.OllamaTutorRepository
import me.pablorincon.proyecto_app.domain.tutor.TutorChat
import me.pablorincon.proyecto_app.domain.tutor.TutorRepository

// Chat del tutor. La conversación vive mientras la app está abierta (no se guarda en Firebase:
// las preguntas pueden tener datos personales). Con Ollama configurado responde la IA local;
// si no, la demo.
class TutorViewModel(
    private val repository: TutorRepository =
        OllamaTutorRepository.fromBuildConfig() ?: DemoTutorRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(TutorChat())
    val uiState: StateFlow<TutorChat> = _uiState.asStateFlow()

    // Enter en el teclado envía la pregunta en lugar de agregar un salto de línea.
    fun onInputChange(text: String) {
        val pressedEnter = '\n' in text
        val clean = if (pressedEnter) text.replace('\n', ' ').trimEnd() else text
        _uiState.update { it.withInput(clean) }
        if (pressedEnter) send()
    }

    fun send() = ask(_uiState.value.input, clearInput = true)

    // Chip de pregunta sugerida: se envía tal cual, sin tocar lo que el usuario está escribiendo.
    fun askSuggestion(question: String) = ask(question, clearInput = false)

    private fun ask(question: String, clearInput: Boolean) {
        val history = _uiState.value.messages
        val asking = _uiState.value.ask(question, clearInput) ?: return
        _uiState.value = asking
        viewModelScope.launch {
            val reply = repository.reply(question.trim(), history) { partial ->
                _uiState.update { it.withPartial(partial) }
            }
            _uiState.update { it.withReply(reply) }
        }
    }

    fun clearConversation() = _uiState.update { it.cleared() }
}
