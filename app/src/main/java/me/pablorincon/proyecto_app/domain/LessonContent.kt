package me.pablorincon.proyecto_app.domain

// Una pregunta de comprobación: opciones, cuál es la correcta y qué se le dice al usuario
// en cada caso (el mensaje de fallo explica por qué la opción elegida es una trampa).
data class QuizQuestion(
    val prompt: String,
    val options: List<String>,
    val correctIndex: Int,
    val correctFeedback: String,
    val wrongFeedback: String
) {
    init {
        require(correctIndex in options.indices) { "La respuesta correcta no existe: $prompt" }
    }
}

// Contenido de una lección (texto en español, escrito para el usuario). El id es el de
// Lesson, así el currículo (orden, XP) y el contenido quedan separados.
data class LessonContent(
    val lessonId: String,
    val title: String,
    val hook: String,           // Gancho breve con Glitch
    val explanation: String,
    val code: String,
    val expectedOutput: String,
    val questions: List<QuizQuestion>
) {
    init {
        require(questions.isNotEmpty()) { "La lección $lessonId no tiene preguntas" }
    }
}
