package me.pablorincon.proyecto_app.domain

// Contenido de una lección (texto en español, escrito para el usuario). El id es el de
// Lesson, así el currículo (orden, XP) y el contenido quedan separados.
data class LessonContent(
    val lessonId: String,
    val title: String,
    val hook: String,           // Gancho breve con Glitch
    val explanation: String,
    val code: String,
    val expectedOutput: String,
    val exercises: List<Exercise>
) {
    init {
        require(exercises.isNotEmpty()) { "La lección $lessonId no tiene ejercicios" }
    }
}
