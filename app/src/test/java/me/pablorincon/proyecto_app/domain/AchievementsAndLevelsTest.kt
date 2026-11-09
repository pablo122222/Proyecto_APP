package me.pablorincon.proyecto_app.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime

class AchievementsAndLevelsTest {

    private val today = LocalDate.of(2026, 11, 9)
    private val curriculum = Curriculum.Base
    private fun boss(level: Int): PathStep = curriculum.modules[level - 1].boss!!

    // ---------- Logros ----------
    @Test
    fun hayNueveLogrosYCuatroSeMidenHoy() {
        assertEquals(9, Achievements.all.size)
        assertEquals(setOf("bug_hunter", "learn_from_error", "level_up", "habit"), Achievements.all.filter { it.available }.map { it.id }.toSet())
        assertEquals(2 + 3 + 3 + 3, Achievements.availableTiers)
    }

    @Test
    fun sinProgresoNoSeDesbloqueaNada() {
        assertTrue(Achievements.newlyReached(AchievementStats(), emptyMap()).isEmpty())
    }

    @Test
    fun alcanzarUnUmbralDesbloqueaSuNivelConSusGemas() {
        val unlocks = Achievements.newlyReached(AchievementStats(bestStreak = 3), emptyMap())
        assertEquals(listOf(AchievementUnlock("habit", Tier.Bronze, 5)), unlocks)
    }

    @Test
    fun saltarVariosNivelesDeUnaVezLosEntregaTodos() {
        val unlocks = Achievements.newlyReached(AchievementStats(levelsCompleted = 6), emptyMap())
        assertEquals(listOf(Tier.Bronze, Tier.Silver, Tier.Gold), unlocks.map { it.tier })
        assertEquals(20 + 40 + 80, unlocks.sumOf { it.gems })
    }

    @Test
    fun loYaDesbloqueadoNoSeEntregaDosVeces() {
        val stats = AchievementStats(bugsFound = 2)
        val first = Achievements.newlyReached(stats, emptyMap())
        val stored = Achievements.applyUnlocks(emptyMap(), first)
        assertEquals(mapOf("bug_hunter" to 2), stored)
        assertTrue(Achievements.newlyReached(stats, stored).isEmpty())
    }

    @Test
    fun elProgresoMuestraLaBarraHaciaElSiguienteNivel() {
        val progress = Achievements.progress(AchievementStats(bestStreak = 5), mapOf("habit" to 1)).first { it.achievement.id == "habit" }
        assertEquals(Tier.Bronze, progress.currentTier)
        assertEquals(Tier.Silver, progress.nextTier)
        assertEquals(7, progress.nextThreshold)
        assertEquals(5f / 7f, progress.fraction)
        assertEquals(5, progress.gemsEarned)
        assertFalse(progress.isMaxed)
    }

    @Test
    fun lasEstadisticasSalenDelPerfilYDeLosJefesVencidos() {
        val profile = UserProfile.initial("Ana").copy(bestStreak = 4, streak = 6, bugsFound = 3, recoveredExercises = 2)
        val stats = AchievementStats.of(profile, listOf(boss(1), boss(2), curriculum.lessons.first()))
        assertEquals(AchievementStats(bestStreak = 6, levelsCompleted = 2, bugsFound = 3, recovered = 2), stats)
    }

    // ---------- Completar un paso ----------
    @Test
    fun completarSumaContadoresRachaYLogros() {
        val user = UserProfile.initial("Ana")
        val outcome = CompletionOutcome.completing(user, listOf(curriculum.lessons.first()), CompletionStats(bugsFound = 1, recovered = 1), today)
        assertEquals(1, outcome.activity.streak)
        assertEquals(1, outcome.bestStreak)
        assertEquals(1, outcome.bugsFound)
        assertEquals(1, outcome.recovered)
        assertEquals(setOf("bug_hunter", "learn_from_error"), outcome.unlocks.map { it.achievementId }.toSet())
        assertEquals(15 + 10, outcome.gemsGained)
        assertEquals(mapOf("bug_hunter" to 1, "learn_from_error" to 1), outcome.achievementTiers)
    }

    @Test
    fun vencerUnJefeDesbloqueaSubirDeEtapa() {
        val outcome = CompletionOutcome.completing(UserProfile.initial("Ana"), listOf(boss(1)), CompletionStats(), today)
        assertEquals(listOf(AchievementUnlock("level_up", Tier.Bronze, 20)), outcome.unlocks)
    }

    @Test
    fun losPendientesEntreganElProgresoAnterior() {
        val user = UserProfile.initial("Ana").copy(streak = 3, lastActiveDate = today.toString())
        val pending = CompletionOutcome.pending(user, listOf(boss(1), boss(2), boss(3)))
        assertEquals(setOf("level_up" to Tier.Bronze, "level_up" to Tier.Silver, "habit" to Tier.Bronze), pending.unlocks.map { it.achievementId to it.tier }.toSet())
        assertEquals(3, pending.bestStreak)
    }

    @Test
    fun recuperadosSoloCuentanAlTerminarLaLeccion() {
        val a = TypeCode("a", listOf("x"), correctFeedback = "ok", wrongFeedback = "no")
        val b = TypeCode("b", listOf("y"), correctFeedback = "ok", wrongFeedback = "no")
        var quiz = LessonQuiz(listOf(a, b))
        quiz = quiz.submit(false).next()        // Falla a: va al final
        assertEquals(0, quiz.recoveredCount)
        quiz = quiz.submit(true).next()         // Acierta b
        quiz = quiz.submit(true).next()         // Acierta a, que había fallado
        assertTrue(quiz.isComplete)
        assertEquals(1, quiz.recoveredCount)
        assertEquals(CompletionStats(bugsFound = 0, recovered = 1), CompletionStats.of(listOf(a, b), quiz))
    }

    // ---------- Nivel de usuario ----------
    @Test
    fun unNivelCada250Xp() {
        assertEquals(UserLevel(1, 0, 250), UserLevel.fromXp(0))
        assertEquals(UserLevel(1, 249, 250), UserLevel.fromXp(249))
        assertEquals(UserLevel(2, 0, 250), UserLevel.fromXp(250))
        assertEquals(17, UserLevel.fromXp(4050).level)
        assertEquals(1, UserLevel.fromXp(-5).level)
    }

    @Test
    fun losRangosSubenConElNivel() {
        assertEquals(UserRank.Apprentice, UserLevel.fromXp(0).rank)
        assertEquals(UserRank.Explorer, UserLevel.fromXp(750).rank)   // Nivel 4
        assertEquals(UserRank.Builder, UserLevel.fromXp(1750).rank)   // Nivel 8
        assertEquals(UserRank.Expert, UserLevel.fromXp(3000).rank)    // Nivel 13
    }

    // ---------- Recordatorio ----------
    @Test
    fun elRecordatorioEsHoySiAunNoPasaYSiNoManana() {
        val morning = LocalDateTime.of(2026, 11, 9, 8, 0)
        assertEquals(LocalDateTime.of(2026, 11, 9, 19, 0), ReminderTime.next(morning, 19, 0))
        val night = LocalDateTime.of(2026, 11, 9, 20, 0)
        assertEquals(LocalDateTime.of(2026, 11, 10, 19, 0), ReminderTime.next(night, 19, 0))
        assertEquals(LocalDateTime.of(2026, 11, 10, 19, 0), ReminderTime.next(LocalDateTime.of(2026, 11, 9, 19, 0), 19, 0))
    }
}
