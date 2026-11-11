package me.pablorincon.proyecto_app.data

import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.Content
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.content
import com.google.firebase.ai.type.generationConfig
import me.pablorincon.proyecto_app.domain.tutor.TutorPrompt
import me.pablorincon.proyecto_app.domain.tutor.TutorTurn

// Tutor con Gemini en la nube mediante Firebase AI Logic (API para desarrolladores de Gemini,
// plan gratuito Spark). No hay clave de API en la app: Firebase hace de intermediario y App Check
// (ver AppCheckSetup) comprueba que la petición viene de esta app.
// Las preguntas viajan a Google solo para responderlas; la app no las guarda.
class GeminiClient {

    private val model by lazy {
        Firebase.ai(backend = GenerativeBackend.googleAI()).generativeModel(
            modelName = MODEL,
            // Los Gemini 3.x ignoran temperature: solo se limita el largo de la respuesta.
            generationConfig = generationConfig { maxOutputTokens = MAX_TOKENS },
            systemInstruction = content { text(TutorPrompt.instructions) }
        )
    }

    suspend fun chat(turns: List<TutorTurn>, onPartial: (String) -> Unit): String {
        val conversation = conversation(turns)
        val question = conversation.last()
        val chat = model.startChat(history = conversation.dropLast(1).map(::toContent))
        val text = StringBuilder()
        chat.sendMessageStream(question.text).collect { chunk ->
            val piece = chunk.text.orEmpty()
            if (piece.isNotEmpty()) {
                text.append(piece)
                onPartial(text.toString())
            }
        }
        return text.toString()
    }

    companion object {
        // Modelo estable y sin facturación en el plan Spark (noviembre de 2026). Si Google lo
        // retira, responde la demo hasta cambiar este nombre.
        const val MODEL = "gemini-3.8-flash"

        // Tope de seguridad. Gemini 3 puede gastar parte en "pensar" antes de escribir, por eso
        // es más alto que en Ollama; las instrucciones ya piden menos de 120 palabras.
        const val MAX_TOKENS = 1024

        // Gemini recibe las instrucciones aparte (systemInstruction, fijas en TutorPrompt) y la
        // conversación con roles "user" y "model". La última debe ser la pregunta del estudiante.
        internal fun conversation(turns: List<TutorTurn>): List<TutorTurn> {
            val conversation = turns.filter { it.role != TutorTurn.Role.SYSTEM }
            require(conversation.lastOrNull()?.role == TutorTurn.Role.USER) { "Falta la pregunta" }
            return conversation
        }

        internal fun roleFor(turn: TutorTurn): String =
            if (turn.role == TutorTurn.Role.ASSISTANT) "model" else "user"

        private fun toContent(turn: TutorTurn): Content = content(role = roleFor(turn)) { text(turn.text) }
    }
}
