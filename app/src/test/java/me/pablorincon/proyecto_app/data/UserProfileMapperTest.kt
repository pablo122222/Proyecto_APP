package me.pablorincon.proyecto_app.data

import me.pablorincon.proyecto_app.domain.LearningGoal
import me.pablorincon.proyecto_app.domain.LearningLevel
import me.pablorincon.proyecto_app.domain.TextSize
import me.pablorincon.proyecto_app.domain.UserPreferences
import me.pablorincon.proyecto_app.domain.UserProfile
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class UserProfileMapperTest {

    @Test
    fun `ida y vuelta conserva todos los campos`() {
        val profile = UserProfile(
            name = "Ana",
            level = LearningLevel.Intermediate,
            goal = LearningGoal.SolveProblems,
            dailyGoal = 5,
            xp = 120,
            gems = 7,
            streak = 3,
            lastActiveDate = "2026-11-05",
            activitiesOnLastDay = 3,
            bestStreak = 9,
            bugsFound = 4,
            recoveredExercises = 2,
            explainedWhy = setOf("offers_shop"),
            achievementTiers = mapOf("habit" to 2, "bug_hunter" to 1),
            equippedTitleId = "t1",
            preferences = UserPreferences(
                textSize = TextSize.ExtraLarge,
                highContrast = true,
                reduceMotion = true,
                soundCues = true,
                vibration = false,
                reminders = true,
                reminderHour = 8,
                reminderMinute = 30
            )
        )
        assertEquals(profile, UserProfileMapper.fromMap(UserProfileMapper.toMap(profile)))
    }

    @Test
    fun `el perfil inicial guarda nivel y objetivo como nulos`() {
        val map = UserProfileMapper.toMap(UserProfile.initial("Ana"))
        assertNull(map[UserProfileMapper.LEVEL])
        assertNull(map[UserProfileMapper.GOAL])
        assertEquals(0, map[UserProfileMapper.XP])
    }

    @Test
    fun `un documento vacio da los valores iniciales con el nombre de respaldo`() {
        val profile = UserProfileMapper.fromMap(emptyMap(), fallbackName = "Ana")
        assertEquals(UserProfile.initial("Ana"), profile)
    }

    @Test
    fun `campos con tipo equivocado toman el valor por defecto`() {
        val profile = UserProfileMapper.fromMap(
            mapOf(
                UserProfileMapper.XP to "muchos",
                UserProfileMapper.LEVEL to 3,
                UserProfileMapper.PREFERENCES to "no es un mapa"
            ),
            fallbackName = "Ana"
        )
        assertEquals(0, profile.xp)
        assertNull(profile.level)
        assertEquals(UserPreferences(), profile.preferences)
    }

    @Test
    fun `firestore devuelve numeros como Long y se aceptan`() {
        val profile = UserProfileMapper.fromMap(
            mapOf(UserProfileMapper.XP to 40L, UserProfileMapper.DAILY_GOAL to 3L)
        )
        assertEquals(40, profile.xp)
        assertEquals(3, profile.dailyGoal)
    }
}
