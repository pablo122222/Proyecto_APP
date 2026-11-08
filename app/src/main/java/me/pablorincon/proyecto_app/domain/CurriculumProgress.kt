package me.pablorincon.proyecto_app.domain

// Locked: bloqueado hasta terminar lo anterior · Optional: módulo anterior al nivel de
// inicio del usuario; está abierto para repasar y cada paso suma su XP al completarlo.
enum class ModuleStatus { Completed, InProgress, Locked, Optional }

// Estado de un paso en el camino: Done ya completado (se puede repasar), Current el que toca,
// Available abierto sin ser el siguiente (repaso opcional) y Locked bloqueado.
enum class StepStatus { Done, Current, Available, Locked }

// Estado calculado de un módulo para un usuario.
data class ModuleProgress(
    val module: Module,
    val status: ModuleStatus,
    val completedSteps: Int,
    val nextStep: PathStep?   // Primer paso sin completar del módulo; null si ya lo terminó
)

// Progreso de un usuario sobre el currículo. Lo único que se guarda son los ids de los
// pasos completados (lecciones y jefes): todo lo demás se calcula aquí, así nunca se desincroniza.
// startModuleIndex: módulo donde empieza según su nivel; los anteriores no bloquean ni cuentan
// como completados (no dan XP hasta que los haga), pero están abiertos.
class CurriculumProgress(
    private val curriculum: Curriculum,
    completedStepIds: Set<String>,
    private val startModuleIndex: Int = 0,
    private val unlockAll: Boolean = false // Cuenta de administrador: todo abierto para revisar el contenido
) {
    // Un id que ya no existe en el currículo (paso quitado) se ignora.
    private val completed: Set<String> =
        completedStepIds.intersect(curriculum.steps.map { it.id }.toSet())

    private val activeModules: List<Module> = curriculum.modules.drop(startModuleIndex)

    // Siguiente paso: el primero sin completar desde el módulo de inicio. Null si terminó todo.
    val nextStep: PathStep? = activeModules.flatMap { it.steps }.firstOrNull { it.id !in completed }

    // Compatibilidad con la pantalla de Ruta: la siguiente lección, si el siguiente paso lo es.
    val nextLesson: Lesson? = nextStep as? Lesson

    // Módulo en curso: el que contiene el siguiente paso. Null si terminó todo.
    val currentModule: Module? = nextStep?.let { next -> curriculum.moduleOf(next.id) }

    val modules: List<ModuleProgress> = curriculum.modules.mapIndexed { index, module ->
        val done = module.steps.count { it.id in completed }
        val status = when {
            done == module.steps.size -> ModuleStatus.Completed
            index < startModuleIndex -> ModuleStatus.Optional
            module.id == currentModule?.id -> ModuleStatus.InProgress
            unlockAll -> ModuleStatus.Optional
            else -> ModuleStatus.Locked
        }
        ModuleProgress(module, status, done, module.steps.firstOrNull { it.id !in completed })
    }

    // Se redondea hacia abajo: nunca marca 100 % sin haber terminado. Solo cuenta lo que el
    // usuario completó de verdad: quien empieza en un módulo avanzado parte de 0 %.
    val percent: Int =
        if (curriculum.totalSteps == 0) 0 else completed.size * 100 / curriculum.totalSteps

    fun isCompleted(stepId: String): Boolean = stepId in completed

    fun stepStatus(stepId: String): StepStatus = when {
        stepId in completed -> StepStatus.Done
        stepId == nextStep?.id -> StepStatus.Current
        isStepUnlocked(stepId) -> StepStatus.Available
        else -> StepStatus.Locked
    }

    // Desbloqueo secuencial: un paso se abre solo si todos los anteriores están completados.
    // En un módulo de repaso (anterior al inicio) solo cuentan los anteriores de su propio módulo.
    // Un id desconocido nunca se abre.
    fun isStepUnlocked(stepId: String): Boolean {
        val module = curriculum.moduleOf(stepId) ?: return false
        if (unlockAll) return true
        val moduleIndex = curriculum.modules.indexOf(module)
        val before = if (moduleIndex < startModuleIndex) {
            module.steps.takeWhile { it.id != stepId }
        } else {
            val fromStart = activeModules.flatMap { it.steps }
            fromStart.take(fromStart.indexOfFirst { it.id == stepId })
        }
        return before.all { it.id in completed }
    }
}
