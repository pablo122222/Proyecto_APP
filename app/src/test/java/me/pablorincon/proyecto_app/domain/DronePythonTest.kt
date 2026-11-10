package me.pablorincon.proyecto_app.domain

import me.pablorincon.proyecto_app.domain.glitchcity.Drone
import me.pablorincon.proyecto_app.domain.glitchcity.DroneBlock
import me.pablorincon.proyecto_app.domain.glitchcity.DroneDirection
import me.pablorincon.proyecto_app.domain.glitchcity.DroneMove.DOWN
import me.pablorincon.proyecto_app.domain.glitchcity.DroneMove.RIGHT
import me.pablorincon.proyecto_app.domain.glitchcity.DroneMove.RIGHT_DOWN
import me.pablorincon.proyecto_app.domain.glitchcity.DronePython
import me.pablorincon.proyecto_app.domain.glitchcity.GlitchCityCatalog
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class DronePythonTest {

    private val levels = GlitchCityCatalog.droneLevels

    @Test
    fun unBloqueSinRepetirEsUnaLlamada() {
        val code = DronePython.of(listOf(DroneBlock(RIGHT)))
        assertEquals(listOf("mover_derecha()"), code.lines)
        assertEquals(1, code.steps.size)
    }

    @Test
    fun unBloqueConRepeticionEsUnFor() {
        val code = DronePython.of(listOf(DroneBlock(RIGHT, 4), DroneBlock(DOWN, 2)))
        assertEquals(
            listOf(
                "for paso in range(4):",
                "    mover_derecha()",
                "for paso in range(2):",
                "    mover_abajo()"
            ),
            code.lines
        )
        // Cada vuelta del for es un paso en la línea de la llamada
        assertEquals(listOf(1, 1, 1, 1, 3, 3), code.steps.map { it.line })
        assertEquals(listOf(1, 2, 3, 4, 1, 2), code.steps.map { it.iteration })
    }

    @Test
    fun losBloquesDeDosPasosTienenDosLlamadas() {
        val code = DronePython.of(listOf(DroneBlock(RIGHT_DOWN, 2)))
        assertEquals(listOf("for paso in range(2):", "    mover_derecha()", "    mover_abajo()"), code.lines)
        assertEquals(listOf(1, 2, 1, 2), code.steps.map { it.line })
        assertEquals(listOf(DroneDirection.Right, DroneDirection.Down, DroneDirection.Right, DroneDirection.Down), code.steps.map { it.direction })
    }

    @Test
    fun duranteElVueloSeMarcaLaLineaDelPasoMostrado() {
        val program = listOf(DroneBlock(RIGHT, 4), DroneBlock(DOWN, 4))
        val code = DronePython.of(program)
        val run = Drone.run(levels[0], program)
        assertNull(DronePython.stepAt(code, run, visibleCells = 1, finished = false)) // Aún en la salida
        assertEquals(1, DronePython.stepAt(code, run, visibleCells = 2, finished = false)!!.line)
        val fifth = DronePython.stepAt(code, run, visibleCells = 6, finished = false)!!
        assertEquals(3, fifth.line)
        assertEquals(1, fifth.iteration)
    }

    @Test
    fun alChocarSeMarcaLaLineaQueFallo() {
        val program = listOf(DroneBlock(DOWN), DroneBlock(RIGHT)) // Nivel 2: baja 1 y choca al avanzar
        val code = DronePython.of(program)
        val run = Drone.run(levels[1], program)
        val failed = DronePython.stepAt(code, run, visibleCells = run.path.size, finished = true)!!
        assertEquals(1, failed.line)
    }

    @Test
    fun alSalirseDelMapaSeMarcaLaLineaQueFallo() {
        val level4 = levels[3]
        val code = DronePython.of(level4.starter)
        val run = Drone.run(level4, level4.starter)
        // →×4, ↓×2 y luego el →×2 que sobra: falla en su llamada (línea 5)
        assertEquals(5, DronePython.stepAt(code, run, visibleCells = run.path.size, finished = true)!!.line)
    }
}
