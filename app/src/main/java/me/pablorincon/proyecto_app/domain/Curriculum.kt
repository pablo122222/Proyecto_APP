package me.pablorincon.proyecto_app.domain

// Un paso de la ruta: una lección o el jefe de un módulo. El id es estable (se guarda en
// Firestore al completarlo) y el XP es lo que da, una sola vez.
sealed interface PathStep {
    val id: String
    val xp: Int
}

// Una lección de la ruta; el orden es su posición dentro del módulo.
data class Lesson(override val id: String, override val xp: Int) : PathStep

// Jefe de un módulo: mini juego obligatorio que cierra el nivel y desbloquea el siguiente.
data class Boss(override val id: String, override val xp: Int) : PathStep

// Un módulo (nivel) de la ruta: sus lecciones en orden y, al final, su jefe si lo tiene.
// Su título y descripción (texto visible) viven en strings.xml, no aquí.
data class Module(val id: String, val lessons: List<Lesson>, val boss: Boss? = null) {
    val steps: List<PathStep> = lessons + listOfNotNull(boss)
}

// Currículo completo: módulos y pasos en el orden en que se desbloquean.
class Curriculum(val modules: List<Module>) {

    val steps: List<PathStep> = modules.flatMap { it.steps }
    val lessons: List<Lesson> = modules.flatMap { it.lessons }
    val totalLessons: Int get() = lessons.size
    val totalSteps: Int get() = steps.size

    fun lesson(lessonId: String): Lesson? = lessons.firstOrNull { it.id == lessonId }

    fun moduleOf(stepId: String): Module? = modules.firstOrNull { m -> m.steps.any { it.id == stepId } }

    // La lección que sigue en la ruta, aunque cambie de módulo. Null si es la última (o no existe).
    fun lessonAfter(lessonId: String): Lesson? {
        val index = lessons.indexOfFirst { it.id == lessonId }
        return if (index < 0) null else lessons.getOrNull(index + 1)
    }

    // Módulo donde empieza el usuario según el nivel de Personalización (los anteriores quedan
    // como repaso opcional). Sin nivel guardado se empieza por el primero.
    fun startModuleIndex(level: LearningLevel?): Int {
        val startId = when (level) {
            LearningLevel.Basic -> BASIC_START_MODULE
            LearningLevel.Intermediate -> INTERMEDIATE_START_MODULE
            LearningLevel.Beginner, null -> return 0
        }
        return modules.indexOfFirst { it.id == startId }.coerceAtLeast(0)
    }

    init {
        require(steps.map { it.id }.toSet().size == steps.size) { "Hay ids de paso repetidos" }
        require(modules.map { it.id }.toSet().size == modules.size) { "Hay ids de módulo repetidos" }
    }

    companion object {
        const val PROJECT_ID = "proyecto"   // módulo final: se muestra como "Próximamente" mientras esté bloqueado
        private const val BASIC_START_MODULE = "bucles"          // «He visto variables y condiciones»
        private const val INTERMEDIATE_START_MODULE = "funciones" // «Ya he programado»

        private const val LESSON_XP = 20    // XP de una lección (guía de diseño §4 y §10)
        private const val PROJECT_XP = 50   // propuesta aprobada: el proyecto final vale más

        // Currículo base aprobado en la Fase 9 (25 lecciones). Los jefes de cada módulo se
        // agregan junto con la pantalla del jefe (Fase 10, punto c): sin ella nadie podría pasar.
        val Base = Curriculum(
            listOf(
                module("fundamentos", lessonCount = 4, xp = LESSON_XP),
                module("variables", lessonCount = 5, xp = LESSON_XP),
                module("condicionales", lessonCount = 5, xp = LESSON_XP),
                module("bucles", lessonCount = 5, xp = LESSON_XP),
                module("funciones", lessonCount = 5, xp = LESSON_XP),
                module(PROJECT_ID, lessonCount = 1, xp = PROJECT_XP)
            )
        )

        // Ids como "fundamentos_1": seguros para usar dentro de una ruta de navegación.
        private fun module(id: String, lessonCount: Int, xp: Int, bossXp: Int? = null) = Module(
            id = id,
            lessons = (1..lessonCount).map { Lesson(id = "${id}_$it", xp = xp) },
            boss = bossXp?.let { Boss(id = "${id}_jefe", xp = it) }
        )
    }
}
