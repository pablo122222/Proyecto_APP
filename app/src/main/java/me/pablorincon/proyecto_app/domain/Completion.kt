package me.pablorincon.proyecto_app.domain

import java.time.LocalDate

// Lo que aporta una lección o un jefe terminado a los logros.
data class CompletionStats(
    val bugsFound: Int = 0,   // Ejercicios "Caza el bug" resueltos
    val recovered: Int = 0    // Ejercicios acertados después de fallarlos
) {
    companion object {
        fun of(exercises: List<Exercise>, quiz: LessonQuiz) = CompletionStats(
            bugsFound = exercises.count { it is FindBug },
            recovered = quiz.recoveredCount
        )
    }
}

// Cambios en el perfil al completar un paso por primera vez (o al revisar logros pendientes).
// Puro: el repositorio lo usa dentro de la transacción de Firestore y los tests lo prueban solo.
data class CompletionOutcome(
    val activity: DailyActivity,
    val bestStreak: Int,
    val bugsFound: Int,
    val recovered: Int,
    val unlocks: List<AchievementUnlock>,
    val achievementTiers: Map<String, Int>
) {
    val gemsGained: Int get() = unlocks.sumOf { it.gems }

    companion object {
        // completedSteps: pasos ya completados INCLUYENDO el que se acaba de terminar.
        fun completing(
            user: UserProfile,
            completedSteps: List<PathStep>,
            stats: CompletionStats,
            today: LocalDate
        ): CompletionOutcome {
            val activity = user.dailyActivity.record(today)
            val updated = user.copy(
                streak = activity.streak,
                bestStreak = maxOf(user.bestStreak, activity.streak),
                bugsFound = user.bugsFound + stats.bugsFound,
                recoveredExercises = user.recoveredExercises + stats.recovered
            )
            return withUnlocks(updated, activity, completedSteps)
        }

        // Sin actividad nueva: solo desbloquea lo que los números ya alcanzan (por ejemplo,
        // progreso hecho antes de que existieran los logros).
        fun pending(user: UserProfile, completedSteps: List<PathStep>): CompletionOutcome =
            withUnlocks(user, user.dailyActivity, completedSteps)

        private fun withUnlocks(user: UserProfile, activity: DailyActivity, completedSteps: List<PathStep>): CompletionOutcome {
            val stats = AchievementStats.of(user, completedSteps)
            val unlocks = Achievements.newlyReached(stats, user.achievementTiers)
            return CompletionOutcome(
                activity = activity,
                bestStreak = stats.bestStreak,
                bugsFound = user.bugsFound,
                recovered = user.recoveredExercises,
                unlocks = unlocks,
                achievementTiers = Achievements.applyUnlocks(user.achievementTiers, unlocks)
            )
        }
    }
}
