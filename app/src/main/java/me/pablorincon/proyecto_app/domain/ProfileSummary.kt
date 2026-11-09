package me.pablorincon.proyecto_app.domain

import java.time.LocalDate

// Rango que acompaña al nivel de usuario ("Nivel 5 · Explorador de código").
enum class UserRank { Apprentice, Explorer, Builder, Expert }

// Nivel de usuario según su XP (regla aprobada: un nivel cada 250 XP).
data class UserLevel(val level: Int, val xpInLevel: Int, val xpForNext: Int) {
    val fraction: Float get() = xpInLevel.toFloat() / xpForNext
    val xpToNext: Int get() = xpForNext - xpInLevel
    val percent: Int get() = (fraction * 100).toInt()

    // Rangos (propuesta): 1-3 Aprendiz, 4-7 Explorador, 8-12 Constructor, 13+ Experto.
    val rank: UserRank
        get() = when {
            level >= 13 -> UserRank.Expert
            level >= 8 -> UserRank.Builder
            level >= 4 -> UserRank.Explorer
            else -> UserRank.Apprentice
        }

    companion object {
        const val XP_PER_LEVEL = 250

        fun fromXp(xp: Int): UserLevel {
            val safe = xp.coerceAtLeast(0)
            return UserLevel(level = safe / XP_PER_LEVEL + 1, xpInLevel = safe % XP_PER_LEVEL, xpForNext = XP_PER_LEVEL)
        }
    }
}

// Todo lo que muestran Perfil y sus pantallas internas, calculado del perfil y del progreso.
data class ProfileSummary(
    val profile: UserProfile,
    val email: String,
    val initials: String,
    val userLevel: UserLevel,
    val streak: Int,
    val lessonsCompleted: Int,
    val bossesDefeated: Int,      // = niveles terminados (el jefe cierra el nivel)
    val totalLessons: Int,
    val totalLevels: Int,
    val achievements: List<AchievementProgress>
) {
    val achievementsUnlocked: Int get() = achievements.count { it.tiersReached > 0 }
    val achievementsAvailable: Int get() = achievements.size

    companion object {
        fun build(
            profile: UserProfile,
            email: String,
            progress: CurriculumProgress,
            curriculum: Curriculum,
            today: LocalDate
        ): ProfileSummary {
            val stats = AchievementStats.of(profile, curriculum.steps.filter { progress.isCompleted(it.id) })
            return ProfileSummary(
                profile = profile,
                email = email,
                initials = HomeSummary.initialsOf(profile.name),
                userLevel = UserLevel.fromXp(profile.xp),
                streak = profile.dailyActivity.streakOn(today),
                lessonsCompleted = curriculum.lessons.count { progress.isCompleted(it.id) },
                bossesDefeated = stats.levelsCompleted,
                totalLessons = curriculum.lessons.size,
                totalLevels = curriculum.modules.size,
                achievements = Achievements.progress(stats, profile.achievementTiers)
            )
        }
    }
}
