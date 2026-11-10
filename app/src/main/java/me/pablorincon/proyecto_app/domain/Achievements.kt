package me.pablorincon.proyecto_app.domain

import me.pablorincon.proyecto_app.domain.glitchcity.GlitchCityProgress

// Logros con niveles de dominio Bronce / Plata / Oro (guía §6). Cada nivel alcanzado se
// desbloquea una sola vez, da sus gemas y se avisa al usuario.
// Los textos visibles (nombre, descripción) viven en strings.xml, por id.

enum class AchievementCategory { Comprehension, ProblemSolving, Algorithmic, Debugging, Consistency }

enum class Tier { Bronze, Silver, Gold }

// De dónde sale el número de cada logro (los cuatro últimos, de Glitch City).
enum class AchievementMetric {
    BestStreak, LevelsCompleted, BugsFound, Recovered, ExplainedWhy,
    CleanSolves, PerfectFirstTries, EdgeCases, RecordsImproved
}

data class Achievement(
    val id: String,
    val category: AchievementCategory,
    val emoji: String,
    val metric: AchievementMetric,
    val thresholds: List<Int>,   // Valor para Bronce, Plata, Oro (algunos solo tienen 2 niveles)
    val gems: List<Int>          // Gemas de cada nivel
) {
    init {
        require(thresholds.isNotEmpty() && thresholds.size == gems.size) { "Logro mal definido: $id" }
        require(thresholds.zipWithNext().all { (a, b) -> b > a }) { "Umbrales no crecientes: $id" }
    }

    val tierCount: Int get() = thresholds.size

    // Cuántos niveles corresponden a un valor (0 = ninguno).
    fun tiersFor(value: Int): Int = thresholds.count { value >= it }
}

// Los números que miden los logros.
data class AchievementStats(
    val bestStreak: Int = 0,
    val levelsCompleted: Int = 0,
    val bugsFound: Int = 0,
    val recovered: Int = 0,
    val explainedWhy: Int = 0,
    val cleanSolves: Int = 0,        // Niveles del dron resueltos al primer intento, sin chocar ni ver la idea
    val perfectFirstTries: Int = 0,  // Niveles del dron cuyo primer programa entregado dio 3 estrellas
    val edgeCases: Int = 0,          // Retos de casos límite resueltos
    val recordsImproved: Int = 0     // Veces que se mejoró un récord del dron (menos bloques)
) {
    fun valueOf(metric: AchievementMetric): Int = when (metric) {
        AchievementMetric.BestStreak -> bestStreak
        AchievementMetric.LevelsCompleted -> levelsCompleted
        AchievementMetric.BugsFound -> bugsFound
        AchievementMetric.Recovered -> recovered
        AchievementMetric.ExplainedWhy -> explainedWhy
        AchievementMetric.CleanSolves -> cleanSolves
        AchievementMetric.PerfectFirstTries -> perfectFirstTries
        AchievementMetric.EdgeCases -> edgeCases
        AchievementMetric.RecordsImproved -> recordsImproved
    }

    companion object {
        // completedSteps: pasos de la ruta ya completados (lecciones y jefes); city: progreso de Glitch City.
        fun of(profile: UserProfile, completedSteps: List<PathStep>, city: GlitchCityProgress = GlitchCityProgress()) =
            AchievementStats(
                bestStreak = maxOf(profile.bestStreak, profile.streak),
                levelsCompleted = completedSteps.count { it is Boss },
                bugsFound = profile.bugsFound,
                recovered = profile.recoveredExercises,
                explainedWhy = profile.explainedWhy.size,
                cleanSolves = city.cleanSolves.size,
                perfectFirstTries = city.perfectFirstTries.size,
                edgeCases = city.edgeCasesSolved,
                recordsImproved = city.recordsImproved
            )
    }
}

// Un nivel de logro recién ganado.
data class AchievementUnlock(val achievementId: String, val tier: Tier, val gems: Int) {
    val achievement: Achievement get() = Achievements.byId(achievementId)!!
}

// Estado de un logro para mostrarlo. tiersReached sale de lo ya desbloqueado y guardado.
data class AchievementProgress(val achievement: Achievement, val value: Int, val tiersReached: Int) {
    val isMaxed: Boolean get() = tiersReached >= achievement.tierCount
    val currentTier: Tier? get() = if (tiersReached == 0) null else Tier.entries[tiersReached - 1]
    val nextTier: Tier? get() = if (isMaxed) null else Tier.entries[tiersReached]
    val nextThreshold: Int? get() = achievement.thresholds.getOrNull(tiersReached)
    val nextGems: Int? get() = achievement.gems.getOrNull(tiersReached)
    val gemsEarned: Int get() = achievement.gems.take(tiersReached).sum()

    // Barra hacia el siguiente nivel (llena si ya está al máximo).
    val fraction: Float get() = nextThreshold?.let { (value.toFloat() / it).coerceIn(0f, 1f) } ?: 1f
}

object Achievements {

    // Mismo orden y valores que la guía §6 (el logro de corazones se eliminó: no hay vidas).
    val all: List<Achievement> = listOf(
        Achievement("explain_why", AchievementCategory.Comprehension, "🧠", AchievementMetric.ExplainedWhy, listOf(1, 3, 5), listOf(10, 25, 50)),
        Achievement("autonomous", AchievementCategory.ProblemSolving, "🧩", AchievementMetric.CleanSolves, listOf(1, 3, 5), listOf(15, 30, 60)),
        Achievement("strategist", AchievementCategory.Algorithmic, "♟️", AchievementMetric.PerfectFirstTries, listOf(1, 2, 3), listOf(15, 30, 60)),
        Achievement("bug_hunter", AchievementCategory.Debugging, "🐛", AchievementMetric.BugsFound, listOf(1, 2), listOf(15, 30)),
        Achievement("edge_cases", AchievementCategory.Debugging, "🎯", AchievementMetric.EdgeCases, listOf(1, 2), listOf(15, 30)),
        Achievement("cost_meter", AchievementCategory.Algorithmic, "⏱️", AchievementMetric.RecordsImproved, listOf(1, 2), listOf(20, 40)),
        Achievement("learn_from_error", AchievementCategory.Consistency, "🔁", AchievementMetric.Recovered, listOf(1, 3, 5), listOf(10, 25, 40)),
        Achievement("level_up", AchievementCategory.Consistency, "🚩", AchievementMetric.LevelsCompleted, listOf(1, 3, 6), listOf(20, 40, 80)),
        Achievement("habit", AchievementCategory.Consistency, "🔥", AchievementMetric.BestStreak, listOf(3, 7, 14), listOf(5, 10, 15))
    )

    fun byId(id: String): Achievement? = all.firstOrNull { it.id == id }

    // Total de niveles de dominio que se pueden ganar.
    val totalTiers: Int get() = all.sumOf { it.tierCount }

    // Progreso de cada logro con lo ya desbloqueado (stored: id → niveles guardados).
    fun progress(stats: AchievementStats, stored: Map<String, Int>): List<AchievementProgress> =
        all.map { a ->
            AchievementProgress(a, stats.valueOf(a.metric), (stored[a.id] ?: 0).coerceIn(0, a.tierCount))
        }

    // Niveles que los números ya alcanzan y aún no se habían desbloqueado. Si un número salta
    // varios niveles de una vez, se desbloquean todos (y se suman sus gemas).
    fun newlyReached(stats: AchievementStats, stored: Map<String, Int>): List<AchievementUnlock> =
        all.flatMap { a ->
            val before = (stored[a.id] ?: 0).coerceIn(0, a.tierCount)
            val now = a.tiersFor(stats.valueOf(a.metric))
            (before until now).map { index -> AchievementUnlock(a.id, Tier.entries[index], a.gems[index]) }
        }

    // Niveles guardados después de desbloquear.
    fun applyUnlocks(stored: Map<String, Int>, unlocks: List<AchievementUnlock>): Map<String, Int> {
        val result = stored.toMutableMap()
        unlocks.forEach { u -> result[u.achievementId] = maxOf(result[u.achievementId] ?: 0, u.tier.ordinal + 1) }
        return result
    }
}
