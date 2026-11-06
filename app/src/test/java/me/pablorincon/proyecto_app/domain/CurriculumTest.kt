package me.pablorincon.proyecto_app.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CurriculumTest {

    private val base = Curriculum.Base

    @Test
    fun elCurriculoBaseTieneSeisModulosEnOrden() {
        assertEquals(
            listOf("fundamentos", "variables", "condicionales", "bucles", "funciones", "proyecto"),
            base.modules.map { it.id }
        )
    }

    @Test
    fun laCantidadDeLeccionesPorModuloEsLaAprobada() {
        assertEquals(listOf(4, 5, 5, 5, 5, 1), base.modules.map { it.lessons.size })
        assertEquals(25, base.totalLessons)
    }

    @Test
    fun losIdsDeLeccionSeNumeranDesdeUnoDentroDelModulo() {
        assertEquals(
            listOf("fundamentos_1", "fundamentos_2", "fundamentos_3", "fundamentos_4"),
            base.modules.first().lessons.map { it.id }
        )
        assertEquals("proyecto_1", base.modules.last().lessons.single().id)
    }

    @Test
    fun cadaLeccionValeVeinteXpYElProyectoCincuenta() {
        assertTrue(base.modules.dropLast(1).flatMap { it.lessons }.all { it.xp == 20 })
        assertEquals(50, base.modules.last().lessons.single().xp)
    }

    @Test
    fun losIdsSonSegurosParaUsarEnUnaRuta() {
        val safe = Regex("[a-z0-9_-]+")
        assertTrue(base.lessons.all { safe.matches(it.id) })
        assertTrue(base.modules.all { safe.matches(it.id) })
    }

    @Test(expected = IllegalArgumentException::class)
    fun unCurriculoConLeccionesRepetidasSeRechaza() {
        val lesson = Lesson("a_1", 20)
        Curriculum(listOf(Module("a", listOf(lesson)), Module("b", listOf(lesson))))
    }
}
