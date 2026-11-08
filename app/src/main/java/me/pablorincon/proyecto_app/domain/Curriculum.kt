package me.pablorincon.proyecto_app.domain

// Un paso de la ruta: una lección o el jefe de un nivel. El id es estable (se guarda en
// Firestore al completarlo) y el XP es lo que da, una sola vez.
sealed interface PathStep {
    val id: String
    val xp: Int
}

// Una lección de la ruta; el orden es su posición dentro del nivel.
data class Lesson(override val id: String, override val xp: Int) : PathStep

// Jefe de un nivel: mini juego obligatorio que cierra el nivel y desbloquea el siguiente.
data class Boss(override val id: String, override val xp: Int) : PathStep

// Un módulo (= nivel) de la ruta: sus lecciones en orden y, al final, su jefe si lo tiene.
// Su título y descripción (texto visible) viven en strings.xml, no aquí.
data class Module(val id: String, val lessons: List<Lesson>, val boss: Boss? = null) {
    val steps: List<PathStep> = lessons + listOfNotNull(boss)
}

// Currículo completo: niveles y pasos en el orden en que se desbloquean.
class Curriculum(val modules: List<Module>) {

    val steps: List<PathStep> = modules.flatMap { it.steps }
    val lessons: List<Lesson> = modules.flatMap { it.lessons }
    val totalLessons: Int get() = lessons.size
    val totalSteps: Int get() = steps.size

    fun lesson(lessonId: String): Lesson? = lessons.firstOrNull { it.id == lessonId }

    fun moduleOf(stepId: String): Module? = modules.firstOrNull { m -> m.steps.any { it.id == stepId } }

    fun step(stepId: String): PathStep? = steps.firstOrNull { it.id == stepId }

    // El paso que sigue en la ruta (lección o jefe), aunque cambie de nivel. Null si es el último (o no existe).
    fun stepAfter(stepId: String): PathStep? {
        val index = steps.indexOfFirst { it.id == stepId }
        return if (index < 0) null else steps.getOrNull(index + 1)
    }

    // Nivel donde empieza el usuario según su nivel de Personalización (los anteriores quedan
    // como repaso opcional). Sin nivel guardado se empieza por el primero.
    fun startModuleIndex(level: LearningLevel?): Int {
        val startId = when (level) {
            LearningLevel.Basic -> levelId(BASIC_START_LEVEL)
            LearningLevel.Intermediate -> levelId(INTERMEDIATE_START_LEVEL)
            LearningLevel.Beginner, null -> return 0
        }
        return modules.indexOfFirst { it.id == startId }.coerceAtLeast(0)
    }

    init {
        require(steps.map { it.id }.toSet().size == steps.size) { "Hay ids de paso repetidos" }
        require(modules.map { it.id }.toSet().size == modules.size) { "Hay ids de módulo repetidos" }
    }

    companion object {
        const val LEVEL_COUNT = 15            // niveles de la ruta
        const val LESSONS_PER_LEVEL = 11      // + el jefe = 12 pasos por nivel
        const val LESSON_XP = 20              // XP de una lección (guía de diseño §4 y §10)
        const val BOSS_XP = 50                // el jefe vale más

        private const val BASIC_START_LEVEL = 7         // «He visto variables y condiciones»: Bucles while
        private const val INTERMEDIATE_START_LEVEL = 10 // «Ya he programado»: Listas

        // Ids seguros para usar dentro de una ruta de navegación: "n03", "n03_l05", "n03_boss".
        fun levelId(level: Int) = "n${level.toString().padStart(2, '0')}"
        fun lessonId(level: Int, number: Int) = "${levelId(level)}_l${number.toString().padStart(2, '0')}"
        fun bossId(level: Int) = "${levelId(level)}_boss"

        // Currículo base: 15 niveles de 12 pasos (11 lecciones y el jefe final del nivel).
        // El contenido de cada lección se escribe nivel por nivel (decisión C); una lección
        // sin contenido aún se muestra como "llegará pronto".
        val Base = Curriculum(
            (1..LEVEL_COUNT).map { level ->
                Module(
                    id = levelId(level),
                    lessons = (1..LESSONS_PER_LEVEL).map { Lesson(lessonId(level, it), LESSON_XP) },
                    boss = Boss(bossId(level), BOSS_XP)
                )
            }
        )
    }
}
