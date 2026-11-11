package me.pablorincon.proyecto_app.domain.tutor

// Un turno de la conversación tal como lo recibe un modelo de IA (rol + texto).
data class TutorTurn(val role: Role, val text: String) {
    enum class Role { SYSTEM, USER, ASSISTANT }
}

// Arma lo que se le envía al modelo: instrucciones + últimos turnos + la pregunta nueva.
// Puro y sin red, para probarlo sin Ollama.
object TutorPrompt {

    // Turnos anteriores que se reenvían: suficiente para seguir el hilo sin hacer lento al modelo.
    const val MAX_HISTORY_TURNS = 6

    val instructions = """
        Eres CodeMentor, un tutor de programación en Python para principiantes.
        Responde siempre en español, de forma breve y clara.
    """.trimIndent()

    fun turns(question: String, history: List<TutorMessage>): List<TutorTurn> {
        val previous = history.mapNotNull { message ->
            when (message) {
                is TutorMessage.FromUser -> TutorTurn(TutorTurn.Role.USER, message.text)
                // Solo las respuestas de texto libre: las de la demo (temas) no vienen del modelo
                is TutorMessage.FromTutor -> (message.reply as? TutorReply.Free)
                    ?.let { TutorTurn(TutorTurn.Role.ASSISTANT, it.text) }
                is TutorMessage.Greeting -> null
            }
        }.takeLast(MAX_HISTORY_TURNS)
        return listOf(TutorTurn(TutorTurn.Role.SYSTEM, instructions)) +
            previous +
            TutorTurn(TutorTurn.Role.USER, question)
    }
}
