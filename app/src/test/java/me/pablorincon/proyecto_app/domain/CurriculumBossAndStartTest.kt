package me.pablorincon.proyecto_app.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

// Jefes por módulo y punto de partida según el nivel de Personalización.
class CurriculumBossAndStartTest {

    // Tres módulos pequeños, los dos primeros con jefe
    private val curriculum = Curriculum(
        listOf(
            Module("a", listOf(Lesson("a_1", 20), Lesson("a_2", 20)), Boss("a_jefe", 50)),
            Module("b", listOf(Lesson("b_1", 20), Lesson("b_2", 20)), Boss("b_jefe", 50)),
            Module("c", listOf(Lesson("c_1", 20)))
        )
    )

    private fun progress(start: Int = 0, vararg completed: String) =
        CurriculumProgress(curriculum, completed.toSet(), start)

    // ---------- Jefes ----------
    @Test
    fun elJefeEsElUltimoPasoDelModuloYCuentaEnElTotal() {
        assertEquals(listOf("a_1", "a_2", "a_jefe"), curriculum.modules.first().steps.map { it.id })
        assertEquals(7, curriculum.totalSteps)
        assertEquals(5, curriculum.totalLessons)
        assertEquals("a", curriculum.moduleOf("a_jefe")?.id)
    }

    @Test
    fun noSePuedenRepetirIdsEntreLeccionesYJefes() {
        val error = runCatching {
            Curriculum(listOf(Module("a", listOf(Lesson("x", 20)), Boss("x", 50))))
        }.exceptionOrNull()
        assertTrue(error is IllegalArgumentException)
    }

    @Test
    fun elJefeEsObligatorioParaDesbloquearElSiguienteModulo() {
        val sinJefe = progress(0, "a_1", "a_2")
        assertEquals("a_jefe", sinJefe.nextStep?.id)
        assertNull(sinJefe.nextLesson) // El siguiente paso es el jefe, no una lección
        assertEquals(ModuleStatus.InProgress, sinJefe.modules[0].status)
        assertFalse(sinJefe.isStepUnlocked("b_1"))

        val conJefe = progress(0, "a_1", "a_2", "a_jefe")
        assertEquals(ModuleStatus.Completed, conJefe.modules[0].status)
        assertTrue(conJefe.isStepUnlocked("b_1"))
        assertEquals("b_1", conJefe.nextLesson?.id)
    }

    @Test
    fun elJefeSoloSeAbreAlTerminarLasLeccionesDeSuModulo() {
        assertFalse(progress(0, "a_1").isStepUnlocked("a_jefe"))
        assertTrue(progress(0, "a_1", "a_2").isStepUnlocked("a_jefe"))
    }

    @Test
    fun elPorcentajeCuentaLosJefes() {
        assertEquals(42, progress(0, "a_1", "a_2", "a_jefe").percent) // 3 de 7
    }

    // ---------- Punto de partida ----------
    @Test
    fun elNivelDePersonalizacionDecideElModuloDeInicio() {
        val base = Curriculum.Base
        assertEquals(0, base.startModuleIndex(LearningLevel.Beginner))
        assertEquals(0, base.startModuleIndex(null)) // Sin nivel guardado: desde el principio
        assertEquals(3, base.startModuleIndex(LearningLevel.Basic))
        assertEquals("bucles", base.modules[3].id)
        assertEquals(4, base.startModuleIndex(LearningLevel.Intermediate))
        assertEquals("funciones", base.modules[4].id)
    }

    @Test
    fun siElModuloDeInicioNoExisteSeEmpiezaPorElPrimero() {
        assertEquals(0, curriculum.startModuleIndex(LearningLevel.Basic))
    }

    @Test
    fun quienEmpiezaEnUnModuloAvanzadoVeLosAnterioresComoRepasoOpcional() {
        val p = progress(start = 1)
        assertEquals(
            listOf(ModuleStatus.Optional, ModuleStatus.InProgress, ModuleStatus.Locked),
            p.modules.map { it.status }
        )
        assertEquals("b_1", p.nextLesson?.id)
        assertEquals("b", p.currentModule?.id)
    }

    @Test
    fun losModulosSaltadosNoDanProgresoNiXpAlRegistrarse() {
        val p = progress(start = 1)
        assertEquals(0, p.percent)
        assertTrue(curriculum.steps.take(3).none { p.isCompleted(it.id) })
    }

    @Test
    fun losModulosDeRepasoEstanAbiertosEnOrdenInternoYSinBloquearElResto() {
        val p = progress(start = 1)
        assertTrue(p.isStepUnlocked("a_1"))
        assertFalse(p.isStepUnlocked("a_2")) // Dentro del módulo se sigue el orden
        assertFalse(p.isStepUnlocked("a_jefe"))
        assertTrue(progress(1, "a_1").isStepUnlocked("a_2"))
        // El módulo de inicio no espera a los de repaso
        assertTrue(p.isStepUnlocked("b_1"))
        assertFalse(p.isStepUnlocked("b_jefe"))
    }

    @Test
    fun completarUnModuloDeRepasoSumaAlPorcentajeYLoMarcaCompletado() {
        val p = progress(1, "a_1", "a_2", "a_jefe")
        assertEquals(ModuleStatus.Completed, p.modules[0].status)
        assertEquals(42, p.percent)
        assertEquals("b_1", p.nextLesson?.id) // Lo siguiente sigue siendo el módulo de inicio
    }

    @Test
    fun unModuloDeRepasoAbiertoMuestraSuSiguientePaso() {
        val p = progress(1, "a_1")
        assertEquals("a_2", p.modules[0].nextStep?.id)
        assertEquals(1, p.modules[0].completedSteps)
        assertNull(progress(1, "a_1", "a_2", "a_jefe").modules[0].nextStep)
    }

    @Test
    fun alTerminarTodoLoActivoNoQuedaSiguientePaso() {
        val p = progress(1, "b_1", "b_2", "b_jefe", "c_1")
        assertNull(p.nextStep)
        assertNull(p.currentModule)
        assertEquals(listOf(ModuleStatus.Optional, ModuleStatus.Completed, ModuleStatus.Completed), p.modules.map { it.status })
    }
}
