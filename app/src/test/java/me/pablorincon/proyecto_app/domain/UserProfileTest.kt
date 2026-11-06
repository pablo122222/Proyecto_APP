package me.pablorincon.proyecto_app.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class UserProfileTest {

    @Test
    fun `un usuario nuevo empieza desde cero`() {
        val profile = UserProfile.initial("Ana")
        assertEquals("Ana", profile.name)
        assertEquals(0, profile.xp)
        assertEquals(0, profile.gems)
        assertEquals(0, profile.streak)
        assertNull(profile.lastActiveDate)
        assertEquals("t0", profile.equippedTitleId)
        assertEquals(2, profile.dailyGoal)
    }

    @Test
    fun `subtitulos y recordatorios vienen activados por defecto`() {
        val prefs = UserProfile.initial("Ana").preferences
        assertTrue(prefs.captions)
        assertTrue(prefs.reminders)
        assertFalse(prefs.highContrast)
        assertFalse(prefs.reduceMotion)
        assertFalse(prefs.soundCues)
        assertEquals(TextSize.Standard, prefs.textSize)
    }

    @Test
    fun `sin nivel ni objetivo falta personalizacion`() {
        assertFalse(UserProfile.initial("Ana").hasPersonalization)
        assertFalse(UserProfile.initial("Ana").copy(level = LearningLevel.Basic).hasPersonalization)
        assertFalse(UserProfile.initial("Ana").copy(goal = LearningGoal.PassCourse).hasPersonalization)
    }

    @Test
    fun `con nivel y objetivo la personalizacion esta completa`() {
        val profile = UserProfile.initial("Ana")
            .copy(level = LearningLevel.Basic, goal = LearningGoal.BuildProjects)
        assertTrue(profile.hasPersonalization)
    }

    @Test
    fun `los ids desconocidos no producen valores`() {
        assertNull(LearningLevel.fromId("experto"))
        assertNull(LearningGoal.fromId(null))
        assertEquals(TextSize.Standard, TextSize.fromId("gigante"))
    }
}
