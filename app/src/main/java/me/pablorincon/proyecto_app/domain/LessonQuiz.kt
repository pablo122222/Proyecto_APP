package me.pablorincon.proyecto_app.domain

// Cola de preguntas de una lección. Regla acordada: una pregunta fallada no se pierde ni
// penaliza; pasa al final de la cola y vuelve a aparecer cuando se terminan las demás.
// La lección se completa cuando la cola queda vacía (todas acertadas).
// Inmutable: cada acción devuelve un estado nuevo, así se prueba sin pantalla.
data class LessonQuiz(
    private val pending: List<QuizQuestion>,
    val total: Int,
    val selectedIndex: Int? = null
) {
    constructor(questions: List<QuizQuestion>) : this(questions, questions.size)

    val current: QuizQuestion? get() = pending.firstOrNull()
    val isComplete: Boolean get() = pending.isEmpty()
    val isAnswered: Boolean get() = selectedIndex != null
    val isSelectionCorrect: Boolean get() = current != null && selectedIndex == current?.correctIndex

    // Preguntas ya acertadas (la que se acaba de acertar cuenta de inmediato).
    val correctCount: Int get() = total - pending.size + if (isAnswered && isSelectionCorrect) 1 else 0

    // Responder: solo la primera elección de cada vuelta cuenta.
    fun select(index: Int): LessonQuiz {
        val question = current ?: return this
        if (isAnswered || index !in question.options.indices) return this
        return copy(selectedIndex = index)
    }

    // Pasar a la siguiente: la acertada sale de la cola, la fallada va al final.
    fun next(): LessonQuiz {
        val question = current ?: return this
        if (!isAnswered) return this
        val rest = pending.drop(1)
        return copy(
            pending = if (isSelectionCorrect) rest else rest + question,
            selectedIndex = null
        )
    }
}
