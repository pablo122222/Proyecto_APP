package me.pablorincon.proyecto_app.domain

import kotlin.random.Random

// Un ejercicio de una lección. Todos se validan en el teléfono con funciones puras
// (sin ejecutar Python de verdad), así se prueban sin pantalla.
// El mensaje de fallo explica por qué la respuesta no sirve, sin revelar la correcta:
// el ejercicio volverá al final de la cola.
sealed interface Exercise {
    val prompt: String
    val correctFeedback: String
    val wrongFeedback: String
}

// Opción múltiple: se usa muy poco (decisión del dueño del proyecto).
data class MultipleChoice(
    override val prompt: String,
    val options: List<String>,
    val correctIndex: Int,
    override val correctFeedback: String,
    override val wrongFeedback: String
) : Exercise {
    init {
        require(correctIndex in options.indices) { "La respuesta correcta no existe: $prompt" }
    }

    fun isCorrect(index: Int): Boolean = index == correctIndex

    // Orden en que se muestran las opciones (índices originales). Cambia en cada vuelta
    // (step) para que la correcta no quede siempre en la misma posición.
    fun displayOrder(step: Int): List<Int> = shuffledIndices(options.size, seed = prompt.hashCode() * 31 + step)
}

// Ordena el código: las líneas se dan en el orden correcto y la pantalla las muestra mezcladas.
data class OrderLines(
    override val prompt: String,
    val lines: List<String>,
    override val correctFeedback: String,
    override val wrongFeedback: String
) : Exercise {
    init {
        require(lines.size >= 2) { "Ordenar necesita al menos 2 líneas: $prompt" }
        require(lines.toSet().size > 1) { "Todas las líneas son iguales: $prompt" }
    }

    // Orden inicial mezclado (índices de `lines`), nunca igual al correcto.
    fun initialOrder(step: Int): List<Int> {
        val seed = prompt.hashCode() * 31 + step
        var order = shuffledIndices(lines.size, seed)
        // Con líneas repetidas, otro orden de índices puede dar el mismo texto
        var attempt = 0
        while (isCorrect(order.map { lines[it] }) && attempt < MAX_RESHUFFLES) {
            attempt++
            order = shuffledIndices(lines.size, seed + attempt)
        }
        return order
    }

    // Compara el texto de las líneas: dos líneas iguales son intercambiables.
    fun isCorrect(arranged: List<String>): Boolean = arranged == lines

    private companion object {
        const val MAX_RESHUFFLES = 50
    }
}

// Completa el hueco: el código lleva "___" en cada hueco y se rellena con fichas
// (la lista de fichas incluye distractores).
data class FillBlanks(
    override val prompt: String,
    val codeTemplate: String,
    val answers: List<String>,   // Ficha correcta de cada hueco, en orden
    val chips: List<String>,     // Todas las fichas disponibles
    override val correctFeedback: String,
    override val wrongFeedback: String
) : Exercise {
    val blankCount: Int = codeTemplate.split(BLANK).size - 1

    init {
        require(blankCount > 0) { "No hay huecos: $prompt" }
        require(answers.size == blankCount) { "Huecos y respuestas no coinciden: $prompt" }
        require(answers.all { it in chips }) { "Falta una ficha correcta: $prompt" }
    }

    fun chipOrder(step: Int): List<Int> = shuffledIndices(chips.size, seed = prompt.hashCode() * 31 + step)

    // `filled` trae la ficha elegida en cada hueco (null = vacío).
    fun isCorrect(filled: List<String?>): Boolean = filled == answers

    companion object {
        const val BLANK = "___"
    }
}

// Caza el bug: se toca la línea que tiene el error.
data class FindBug(
    override val prompt: String,
    val codeLines: List<String>,
    val bugLineIndex: Int,
    override val correctFeedback: String,
    override val wrongFeedback: String
) : Exercise {
    init {
        require(bugLineIndex in codeLines.indices) { "La línea del bug no existe: $prompt" }
    }

    fun isCorrect(lineIndex: Int): Boolean = lineIndex == bugLineIndex
}

// Predice la salida: se escribe lo que mostraría el programa.
// Tolerante con mayúsculas (si ignoreCase), espacios y saltos de línea.
data class PredictOutput(
    override val prompt: String,
    val code: String,
    val acceptedAnswers: List<String>,
    val ignoreCase: Boolean = true,
    override val correctFeedback: String,
    override val wrongFeedback: String
) : Exercise {
    init {
        require(acceptedAnswers.isNotEmpty()) { "Sin respuestas aceptadas: $prompt" }
    }

    fun isCorrect(typed: String): Boolean {
        val answer = ExerciseText.normalizeOutput(typed, ignoreCase)
        return answer.isNotEmpty() && acceptedAnswers.any { ExerciseText.normalizeOutput(it, ignoreCase) == answer }
    }
}

// Escribe el código: una línea corta. Tolerante con espacios y con comillas simples/dobles,
// pero distingue mayúsculas (Print no es print).
data class TypeCode(
    override val prompt: String,
    val acceptedAnswers: List<String>,
    val placeholder: String = "",
    override val correctFeedback: String,
    override val wrongFeedback: String
) : Exercise {
    init {
        require(acceptedAnswers.isNotEmpty()) { "Sin respuestas aceptadas: $prompt" }
    }

    fun isCorrect(typed: String): Boolean {
        val answer = ExerciseText.normalizeCode(typed)
        return answer.isNotEmpty() && acceptedAnswers.any { ExerciseText.normalizeCode(it) == answer }
    }
}

// Mezcla determinista: la misma semilla da siempre el mismo orden (no cambia al girar el teléfono).
internal fun shuffledIndices(size: Int, seed: Int): List<Int> = List(size) { it }.shuffled(Random(seed))
