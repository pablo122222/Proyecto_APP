package me.pablorincon.proyecto_app.domain

enum class ModuleStatus { Completed, InProgress, Locked }

// Estado calculado de un módulo para un usuario.
data class ModuleProgress(
    val module: Module,
    val status: ModuleStatus,
    val completedLessons: Int
)

// Progreso de un usuario sobre el currículo. Lo único que se guarda son los ids de
// las lecciones completadas: todo lo demás se calcula aquí, así nunca se desincroniza.
class CurriculumProgress(
    private val curriculum: Curriculum,
    completedLessonIds: Set<String>
) {
    // Un id que ya no existe en el currículo (lección quitada) se ignora.
    private val completed: Set<String> =
        completedLessonIds.intersect(curriculum.lessons.map { it.id }.toSet())

    // Siguiente lección: la primera sin completar, en el orden de la ruta. Null si terminó todo.
    val nextLesson: Lesson? = curriculum.lessons.firstOrNull { it.id !in completed }

    // Módulo en curso: el que contiene la siguiente lección. Null si terminó todo.
    val currentModule: Module? = nextLesson?.let { next ->
        curriculum.modules.first { module -> module.lessons.any { it.id == next.id } }
    }

    val modules: List<ModuleProgress> = curriculum.modules.map { module ->
        val done = module.lessons.count { it.id in completed }
        val status = when {
            done == module.lessons.size -> ModuleStatus.Completed
            module.id == currentModule?.id -> ModuleStatus.InProgress
            else -> ModuleStatus.Locked
        }
        ModuleProgress(module, status, done)
    }

    // Se redondea hacia abajo: nunca marca 100 % sin haber terminado.
    val percent: Int =
        if (curriculum.totalLessons == 0) 0 else completed.size * 100 / curriculum.totalLessons

    fun isCompleted(lessonId: String): Boolean = lessonId in completed

    // Desbloqueo secuencial: una lección se abre solo si todas las anteriores están completadas.
    // Un id desconocido nunca se abre.
    fun isLessonUnlocked(lessonId: String): Boolean {
        val index = curriculum.lessons.indexOfFirst { it.id == lessonId }
        if (index < 0) return false
        return curriculum.lessons.take(index).all { it.id in completed }
    }
}
