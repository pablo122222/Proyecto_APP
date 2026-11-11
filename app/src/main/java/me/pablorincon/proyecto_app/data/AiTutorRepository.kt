package me.pablorincon.proyecto_app.data

import android.util.Log
import me.pablorincon.proyecto_app.BuildConfig
import me.pablorincon.proyecto_app.domain.tutor.TutorMessage
import me.pablorincon.proyecto_app.domain.tutor.TutorPrompt
import me.pablorincon.proyecto_app.domain.tutor.TutorReply
import me.pablorincon.proyecto_app.domain.tutor.TutorRepository
import me.pablorincon.proyecto_app.domain.tutor.TutorTurn
import kotlin.coroutines.cancellation.CancellationException

// Tutor con un modelo de IA: Gemini en la nube (Firebase AI Logic) o, solo en debug y si se
// configura, Ollama en el PC. Si el modelo no responde (sin internet, cuota agotada, PC apagado),
// contesta la demo: el chat nunca se queda sin respuesta.
// Si se corta a mitad de la respuesta, se queda lo que ya se había escrito.
class AiTutorRepository(
    private val chat: suspend (turns: List<TutorTurn>, onPartial: (String) -> Unit) -> String,
    private val fallback: TutorRepository = DemoTutorRepository(),
    private val onError: (Exception) -> Unit = {}
) : TutorRepository {

    override val usesAi: Boolean = true

    override suspend fun reply(
        question: String,
        history: List<TutorMessage>,
        onPartial: (String) -> Unit
    ): TutorReply {
        var shown = ""
        val text = try {
            chat(TutorPrompt.turns(question, history)) { partial ->
                shown = partial
                onPartial(partial)
            }.trim()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            onError(e)
            shown.trim()
        }
        return if (text.isNotEmpty()) TutorReply.Free(text) else fallback.reply(question, history)
    }

    companion object {
        private const val TAG = "AiTutor"

        // Ollama si local.properties tiene ollama.url (solo debug, para probar sin internet);
        // si no, Gemini en la nube, que es lo que usa la versión release.
        fun create(): AiTutorRepository {
            val ollamaUrl = BuildConfig.OLLAMA_URL
            if (ollamaUrl.isNotBlank()) {
                val client = OllamaClient(ollamaUrl, BuildConfig.OLLAMA_MODEL)
                return AiTutorRepository(
                    chat = client::chat,
                    onError = { Log.w(TAG, "Ollama no respondió en $ollamaUrl; contesta la demo", it) }
                )
            }
            val client = GeminiClient()
            return AiTutorRepository(
                chat = client::chat,
                onError = { Log.w(TAG, "Gemini no respondió; contesta la demo", it) }
            )
        }
    }
}
