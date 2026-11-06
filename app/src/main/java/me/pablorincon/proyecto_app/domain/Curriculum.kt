package me.pablorincon.proyecto_app.domain

// Una lección de la ruta. El id es estable (se guarda en Firestore al completarla);
// el orden es su posición dentro del módulo.
data class Lesson(val id: String, val xp: Int)

// Un módulo de la ruta: lista ordenada de lecciones. Su título y descripción
// (texto visible) viven en strings.xml, no aquí.
data class Module(val id: String, val lessons: List<Lesson>)

// Currículo completo: módulos y lecciones en el orden en que se desbloquean.
class Curriculum(val modules: List<Module>) {

    val lessons: List<Lesson> = modules.flatMap { it.lessons }
    val totalLessons: Int get() = lessons.size

    init {
        require(lessons.map { it.id }.toSet().size == lessons.size) { "Hay ids de lección repetidos" }
        require(modules.map { it.id }.toSet().size == modules.size) { "Hay ids de módulo repetidos" }
    }

    companion object {
        private const val LESSON_XP = 20    // XP de una lección (guía de diseño §4 y §10)
        private const val PROJECT_XP = 50   // propuesta aprobada: el proyecto final vale más

        // Currículo base aprobado en la Fase 9 (25 lecciones). Los títulos de cada lección
        // se escriben en la Fase 10, junto con su contenido.
        val Base = Curriculum(
            listOf(
                module("fundamentos", lessonCount = 4, xp = LESSON_XP),
                module("variables", lessonCount = 5, xp = LESSON_XP),
                module("condicionales", lessonCount = 5, xp = LESSON_XP),
                module("bucles", lessonCount = 5, xp = LESSON_XP),
                module("funciones", lessonCount = 5, xp = LESSON_XP),
                module("proyecto", lessonCount = 1, xp = PROJECT_XP)
            )
        )

        // Ids como "fundamentos_1": seguros para usar dentro de una ruta de navegación.
        private fun module(id: String, lessonCount: Int, xp: Int) = Module(
            id = id,
            lessons = (1..lessonCount).map { Lesson(id = "${id}_$it", xp = xp) }
        )
    }
}
