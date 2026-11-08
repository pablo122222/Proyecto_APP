package me.pablorincon.proyecto_app.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CurriculumTest {

    private val base = Curriculum.Base

    @Test
    fun elCurriculoBaseTieneQuinceNivelesEnOrden() {
        assertEquals(15, base.modules.size)
        assertEquals("n01", base.modules.first().id)
        assertEquals("n15", base.modules.last().id)
        assertEquals((1..15).map { Curriculum.levelId(it) }, base.modules.map { it.id })
    }

    @Test
    fun cadaNivelTieneOnceLeccionesYUnJefeFinal() {
        base.modules.forEach { module ->
            assertEquals(module.id, 11, module.lessons.size)
            assertEquals(module.id, 12, module.steps.size)
            assertEquals("${module.id}_boss", module.boss?.id)
            assertTrue(module.steps.last() is Boss) // El jefe es el último paso
        }
        assertEquals(165, base.totalLessons)
        assertEquals(180, base.totalSteps)
    }

    @Test
    fun losIdsSeNumeranDesdeUnoDentroDelNivel() {
        assertEquals(
            (1..11).map { "n01_l${it.toString().padStart(2, '0')}" },
            base.modules.first().lessons.map { it.id }
        )
        assertEquals("n01_l01", Curriculum.lessonId(1, 1))
        assertEquals("n15_boss", Curriculum.bossId(15))
    }

    @Test
    fun elXpDeLeccionesYJefesEsElAprobado() {
        assertTrue(base.lessons.all { it.xp == 20 })
        assertTrue(base.modules.all { it.boss?.xp == 50 })
    }

    @Test
    fun losIdsSonSegurosParaRutasDeNavegacion() {
        val safe = Regex("[a-z0-9_-]+")
        assertTrue(base.steps.all { safe.matches(it.id) })
    }

    @Test
    fun moduleOfEncuentraElNivelDeUnaLeccionOUnJefe() {
        assertEquals("n03", base.moduleOf("n03_l07")?.id)
        assertEquals("n03", base.moduleOf("n03_boss")?.id)
        assertNull(base.moduleOf("no_existe"))
    }

    @Test
    fun stepAfterSigueLaRutaYCambiaDeNivel() {
        assertEquals("n01_l02", base.stepAfter("n01_l01")?.id)
        assertEquals("n01_boss", base.stepAfter("n01_l11")?.id) // Tras la última lección viene el jefe
        assertEquals("n02_l01", base.stepAfter("n01_boss")?.id)  // Vencer al jefe abre el siguiente nivel
        assertNull(base.stepAfter("n15_boss")) // Último paso de la ruta
        assertNull(base.stepAfter("no_existe"))
    }

    @Test
    fun lessonYStepDistinguenLeccionesDeJefes() {
        assertNotNull(base.lesson("n02_l05"))
        assertNull(base.lesson("n02_boss")) // Un jefe no es una lección
        assertTrue(base.step("n02_boss") is Boss)
        assertTrue(base.step("n02_l05") is Lesson)
    }

    @Test
    fun elPuntoDeInicioDependeDelNivelDePersonalizacion() {
        assertEquals(0, base.startModuleIndex(LearningLevel.Beginner))
        assertEquals(0, base.startModuleIndex(null))
        assertEquals("n07", base.modules[base.startModuleIndex(LearningLevel.Basic)].id)
        assertEquals("n10", base.modules[base.startModuleIndex(LearningLevel.Intermediate)].id)
    }
}
