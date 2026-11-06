package me.pablorincon.proyecto_app.ui.screens.personalization

import me.pablorincon.proyecto_app.domain.LearningGoal
import me.pablorincon.proyecto_app.domain.LearningLevel
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PersonalizationUiStateTest {

    @Test
    fun alInicioNoHayNadaElegidoYNoSePuedeContinuar() {
        val state = PersonalizationUiState()
        assertFalse(state.canContinueFromLevel)
        assertFalse(state.canCreateRoute)
    }

    @Test
    fun conNivelElegidoSePuedeContinuarPeroNoCrearLaRuta() {
        val state = PersonalizationUiState(level = LearningLevel.Basic)
        assertTrue(state.canContinueFromLevel)
        assertFalse(state.canCreateRoute)
    }

    @Test
    fun conNivelYObjetivoSePuedeCrearLaRuta() {
        val state = PersonalizationUiState(
            level = LearningLevel.Basic,
            goal = LearningGoal.BuildProjects
        )
        assertTrue(state.canCreateRoute)
    }

    @Test
    fun mientrasSeGuardaNoSePuedeVolverAPulsar() {
        val state = PersonalizationUiState(
            level = LearningLevel.Basic,
            goal = LearningGoal.BuildProjects,
            isSaving = true
        )
        assertFalse(state.canCreateRoute)
    }
}
