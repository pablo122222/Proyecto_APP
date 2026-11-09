package me.pablorincon.proyecto_app.domain

// Cola de ejercicios de una lección. Regla acordada: un ejercicio fallado no se pierde ni
// penaliza; pasa al final de la cola y vuelve a aparecer cuando se terminan los demás.
// La lección se completa cuando la cola queda vacía (todos resueltos).
// Inmutable: cada acción devuelve un estado nuevo, así se prueba sin pantalla.
data class LessonQuiz(
    private val pending: List<Exercise>,
    val total: Int,
    val lastResult: Boolean? = null,   // Resultado del ejercicio actual, tras comprobarlo
    val step: Int = 0,                 // Sube con cada "Continuar": la pantalla lo usa para reiniciar el ejercicio
    private val failed: Set<Exercise> = emptySet() // Ejercicios fallados al menos una vez
) {
    constructor(exercises: List<Exercise>) : this(exercises, exercises.size)

    val current: Exercise? get() = pending.firstOrNull()
    val isComplete: Boolean get() = pending.isEmpty()
    val isAnswered: Boolean get() = lastResult != null
    val isLastCorrect: Boolean get() = lastResult == true

    // Ejercicios que se fallaron y luego se acertaron (logro "Aprender del error").
    // Al completar la lección todos los fallados ya se acertaron.
    val recoveredCount: Int get() = if (isComplete) failed.size else 0

    // Ejercicios ya resueltos (el que se acaba de acertar cuenta de inmediato).
    val correctCount: Int get() = total - pending.size + if (isLastCorrect) 1 else 0

    // Comprobar: solo el primer resultado de cada vuelta cuenta.
    fun submit(correct: Boolean): LessonQuiz {
        if (current == null || isAnswered) return this
        return copy(lastResult = correct)
    }

    // Pasar al siguiente: el acertado sale de la cola, el fallado va al final.
    fun next(): LessonQuiz {
        val exercise = current ?: return this
        if (!isAnswered) return this
        val rest = pending.drop(1)
        return copy(
            pending = if (isLastCorrect) rest else rest + exercise,
            lastResult = null,
            step = step + 1,
            failed = if (isLastCorrect) failed else failed + exercise
        )
    }
}
