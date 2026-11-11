package me.pablorincon.proyecto_app.domain.tutor

// Estado del chat con el tutor. Inmutable y puro: el ViewModel solo pide la respuesta
// al repositorio y aplica estos cambios, así la conversación se prueba sin pantalla.
data class TutorChat(
    val messages: List<TutorMessage> = listOf(TutorMessage.Greeting(id = 0)),
    val input: String = "",
    val isAnswering: Boolean = false,   // El tutor está "escribiendo…"
    val usesAi: Boolean = false,        // Responde un modelo de IA (si no, solo la demo)
    private val nextId: Int = 1
) {
    val canSend: Boolean get() = input.isNotBlank() && !isAnswering
    val hasConversation: Boolean get() = messages.size > 1

    fun withInput(text: String): TutorChat = copy(input = text.take(TUTOR_MAX_QUESTION))

    // Agrega la pregunta y queda esperando la respuesta. null si no se puede preguntar ahora
    // (texto vacío o el tutor aún responde la anterior).
    fun ask(question: String, clearInput: Boolean): TutorChat? {
        val text = question.trim().take(TUTOR_MAX_QUESTION)
        if (text.isEmpty() || isAnswering) return null
        return copy(
            messages = messages + TutorMessage.FromUser(nextId, text),
            input = if (clearInput) "" else input,
            isAnswering = true,
            nextId = nextId + 1
        )
    }

    // Aún no llega ninguna palabra: se muestra "escribiendo…" en lugar de la burbuja en curso.
    val awaitingFirstWords: Boolean get() = isAnswering && messages.lastOrNull()?.id != nextId

    // Texto que va llegando del tutor: se muestra como su mensaje en curso (mismo id que tendrá
    // la respuesta final, así la burbuja crece en su sitio).
    fun withPartial(text: String): TutorChat {
        if (!isAnswering || text.isBlank()) return this
        return copy(messages = withoutPending() + TutorMessage.FromTutor(nextId, TutorReply.Free(text)))
    }

    fun withReply(reply: TutorReply): TutorChat = copy(
        messages = withoutPending() + TutorMessage.FromTutor(nextId, reply),
        isAnswering = false,
        nextId = nextId + 1
    )

    private fun withoutPending(): List<TutorMessage> =
        if (messages.lastOrNull()?.id == nextId) messages.dropLast(1) else messages

    // Empieza de cero (solo el saludo), sin perder lo que se está escribiendo.
    fun cleared(): TutorChat = if (isAnswering) this else TutorChat(input = input, usesAi = usesAi)
}
