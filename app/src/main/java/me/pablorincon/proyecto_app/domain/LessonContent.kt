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
    val exercises: List<Exercise>,
    val walkthrough: List<WalkStep> = emptyList()   // "Ver paso a paso" del ejemplo (opcional)
) {
    init {
        require(exercises.isNotEmpty()) { "La lección $lessonId no tiene ejercicios" }
        val lines = code.lines().size
        require(walkthrough.all { it.line in 1..lines }) { "Un paso apunta a una línea que no existe: $lessonId" }
    }
}

// Un paso de "Ver paso a paso" (código visible): la línea que se ejecuta, numerada como en
// pantalla (desde 1); las variables después de ejecutarla; todo lo impreso hasta ahí y una nota.
// Se escriben a mano con el contenido; un test comprueba que la salida final sea la esperada.
data class WalkStep(
    val line: Int,
    val note: String,
    val variables: List<Pair<String, String>> = emptyList(),
    val output: List<String> = emptyList()
)
