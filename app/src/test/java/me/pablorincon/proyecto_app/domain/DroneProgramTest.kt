package me.pablorincon.proyecto_app.domain

import me.pablorincon.proyecto_app.domain.glitchcity.Drone
import me.pablorincon.proyecto_app.domain.glitchcity.DroneBlock
import me.pablorincon.proyecto_app.domain.glitchcity.DroneMove.DOWN
import me.pablorincon.proyecto_app.domain.glitchcity.DroneMove.RIGHT
import me.pablorincon.proyecto_app.domain.glitchcity.DroneOutcome
import me.pablorincon.proyecto_app.domain.glitchcity.DroneProgram
import me.pablorincon.proyecto_app.domain.glitchcity.GlitchCityCatalog
import me.pablorincon.proyecto_app.domain.glitchcity.WhyQuestions
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DroneProgramTest {

    private val levels = GlitchCityCatalog.droneLevels

    @Test
    fun agregarUsaLaRepeticionElegida() {
        val program = DroneProgram().add(RIGHT, 4).add(DOWN, 2)
        assertEquals(listOf(DroneBlock(RIGHT, 4), DroneBlock(DOWN, 2)), program.blocks)
    }

    @Test
    fun noSePuedenPasarLos12Bloques() {
        val full = (1..DroneProgram.MAX_BLOCKS).fold(DroneProgram()) { p, _ -> p.add(RIGHT, 1) }
        assertFalse(full.canAdd)
        assertEquals(DroneProgram.MAX_BLOCKS, full.add(DOWN, 1).blocks.size)
        assertTrue(full.removeLast().canAdd)
    }

    @Test
    fun borrarYLimpiar() {
        val program = DroneProgram().add(RIGHT, 1).add(DOWN, 3)
        assertEquals(listOf(DroneBlock(RIGHT, 1)), program.removeLast().blocks)
        assertTrue(program.clear().blocks.isEmpty())
        assertTrue(DroneProgram().removeLast().blocks.isEmpty()) // Borrar sin bloques no falla
    }

    @Test
    fun soloElNivel4EmpiezaConProgramaYEstaMal() {
        levels.forEachIndexed { index, level ->
            val start = DroneProgram.startOf(level)
            if (index == 3) {
                assertEquals(4, start.blocks.size)
                assertEquals(DroneOutcome.OutOfMap, Drone.run(level, start.blocks).outcome)
            } else {
                assertTrue(level.id, start.blocks.isEmpty())
            }
        }
    }

    @Test
    fun cadaNivelTieneSuPregunta() {
        levels.forEach { level ->
            val question = WhyQuestions.forActivity(level.id)!!
            assertTrue(question.correctIndex in 0 until question.optionCount)
        }
    }
}
