package me.pablorincon.proyecto_app.domain

import java.time.LocalDate

// Lo que muestra Inicio, calculado del perfil y del progreso reales (nada escrito a mano).
data class HomeSummary(
    val name: String,
    val initials: String,
    val streak: Int,
    val xp: Int,
    val todayDone: Int,
    val dailyGoal: Int,
    val next: NextUp,
    val review: ReviewSuggestion?   // null: aún no hay lecciones completadas que repasar
) {
    val goalMet: Boolean get() = DailyGoal.isMet(todayDone, dailyGoal)

    companion object {
        // `titleOf` da el título visible de un paso con contenido escrito (null = aún sin contenido).
        fun build(
            profile: UserProfile,
            progress: CurriculumProgress,
            curriculum: Curriculum,
            today: LocalDate,
            titleOf: (PathStep) -> String?
        ): HomeSummary {
            val activity = profile.dailyActivity
            return HomeSummary(
                name = profile.name,
                initials = initialsOf(profile.name),
                streak = activity.streakOn(today),
                xp = profile.xp,
                todayDone = activity.countOn(today),
                dailyGoal = profile.dailyGoal,
                next = nextUp(progress, curriculum, titleOf),
                review = review(progress, curriculum, titleOf)
            )
        }

        private fun nextUp(progress: CurriculumProgress, curriculum: Curriculum, titleOf: (PathStep) -> String?): NextUp {
            val step = progress.nextStep ?: return NextUp.Finished
            val module = curriculum.moduleOf(step.id) ?: return NextUp.Finished
            val level = curriculum.modules.indexOf(module) + 1
            val title = titleOf(step) ?: return NextUp.ComingSoon(level)
            val done = module.steps.count { progress.isCompleted(it.id) }
            return NextUp.Step(
                step = step,
                level = level,
                title = title,
                doneInLevel = done,
                stepsInLevel = module.steps.size,
                firstTime = curriculum.steps.none { progress.isCompleted(it.id) }
            )
        }

        // Repaso: la última lección completada (en el orden de la ruta) que tenga contenido.
        private fun review(progress: CurriculumProgress, curriculum: Curriculum, titleOf: (PathStep) -> String?): ReviewSuggestion? =
            curriculum.lessons.lastOrNull { progress.isCompleted(it.id) && titleOf(it) != null }
                ?.let { ReviewSuggestion(it, titleOf(it)!!) }

        // Hasta 2 iniciales: "Juan David Bohorquez" → "JD"; nombre vacío → "?".
        fun initialsOf(name: String): String {
            val letters = name.trim().split(Regex("\\s+"))
                .mapNotNull { word -> word.firstOrNull { it.isLetter() } }
                .take(2)
                .joinToString("") { it.uppercase() }
            return letters.ifEmpty { "?" }
        }
    }
}

sealed interface NextUp {
    // El siguiente paso de la ruta. firstTime: el usuario aún no ha completado nada.
    data class Step(
        val step: PathStep,
        val level: Int,
        val title: String,
        val doneInLevel: Int,
        val stepsInLevel: Int,
        val firstTime: Boolean
    ) : NextUp {
        val isBoss: Boolean get() = step is Boss
    }

    // El siguiente paso existe pero su contenido aún no está escrito.
    data class ComingSoon(val level: Int) : NextUp

    // Completó toda la ruta.
    data object Finished : NextUp
}

data class ReviewSuggestion(val lesson: Lesson, val title: String)
