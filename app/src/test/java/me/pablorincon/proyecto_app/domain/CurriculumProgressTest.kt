package me.pablorincon.proyecto_app.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

// Progreso sobre el currículo real (15 niveles de 12 pasos).
class CurriculumProgressTest {

    private val base = Curriculum.Base

    private fun progress(vararg completed: String) = CurriculumProgress(base, completed.toSet())

    // Ids de todos los pasos (lecciones y jefe) de los primeros n niveles
    private fun stepsOfFirstLevels(count: Int): Array<String> =
        base.modules.take(count).flatMap { m -> m.steps.map { it.id } }.toTypedArray()

    private fun statuses(p: CurriculumProgress) = p.modules.map { it.status }

    @Test
    fun usuarioNuevoEmpiezaEnElPrimerNivelConCeroPorCiento() {
        val p = progress()
        assertEquals(listOf(ModuleStatus.InProgress) + List(14) { ModuleStatus.Locked }, statuses(p))
        assertEquals(0, p.percent)
        assertEquals("n01_l01", p.nextLesson?.id)
        assertEquals("n01", p.currentModule?.id)
    }

    @Test
    fun alCompletarUnaLeccionAvanzaLaSiguienteDentroDelNivel() {
        val p = progress("n01_l01")
        assertEquals("n01_l02", p.nextLesson?.id)
        assertEquals(ModuleStatus.InProgress, p.modules.first().status)
        assertEquals(1, p.modules.first().completedSteps)
        assertEquals(0, p.percent) // 1 de 180 = 0 % (se redondea hacia abajo)
    }

    @Test
    fun conTodasLasLeccionesDelNivelElSiguientePasoEsElJefe() {
        val lessons = base.modules.first().lessons.map { it.id }.toTypedArray()
        val p = progress(*lessons)
        assertEquals("n01_boss", p.nextStep?.id)
        assertNull(p.nextLesson)
        assertEquals(ModuleStatus.InProgress, p.modules.first().status)
        assertFalse(p.isStepUnlocked("n02_l01")) // El jefe es obligatorio
        assertTrue(p.isStepUnlocked("n01_boss"))
    }

    @Test
    fun alVencerAlJefeSeCompletaElNivelYSeAbreElSiguiente() {
        val p = progress(*stepsOfFirstLevels(1))
        assertEquals(listOf(ModuleStatus.Completed, ModuleStatus.InProgress) + List(13) { ModuleStatus.Locked }, statuses(p))
        assertEquals("n02_l01", p.nextLesson?.id)
        assertEquals("n02", p.currentModule?.id)
        assertEquals(6, p.percent) // 12 de 180 = 6,6 %
    }

    @Test
    fun alTerminarTodoEstaAlCienSinSiguientePaso() {
        val p = progress(*stepsOfFirstLevels(15))
        assertTrue(statuses(p).all { it == ModuleStatus.Completed })
        assertEquals(100, p.percent)
        assertNull(p.nextStep)
        assertNull(p.currentModule)
    }

    @Test
    fun elPorcentajeNoLlegaAlCienSinTerminar() {
        val almostAll = base.steps.dropLast(1).map { it.id }.toTypedArray()
        assertEquals(99, progress(*almostAll).percent) // 179 de 180
    }

    @Test
    fun losIdsDesconocidosSeIgnoran() {
        val p = progress("leccion_borrada", "n01_l01")
        assertEquals(1, p.modules.first().completedSteps)
        assertFalse(p.isCompleted("leccion_borrada"))
    }

    @Test
    fun unaLeccionSoloSeAbreSiTodasLasAnterioresEstanCompletadas() {
        val p = progress("n01_l01")
        assertTrue(p.isStepUnlocked("n01_l01")) // ya completada: se puede repasar
        assertTrue(p.isStepUnlocked("n01_l02")) // la siguiente
        assertFalse(p.isStepUnlocked("n01_l03")) // falta la 2
        assertFalse(p.isStepUnlocked("n02_l01")) // falta terminar el nivel 1
    }

    @Test
    fun laPrimeraLeccionSiempreEstaAbiertaYUnIdDesconocidoNunca() {
        assertTrue(progress().isStepUnlocked("n01_l01"))
        assertFalse(progress(*stepsOfFirstLevels(15)).isStepUnlocked("no_existe"))
    }

    @Test
    fun siSeSaltaUnaLeccionElNivelSigueEnCursoYLaSiguienteEsLaQueFalta() {
        val p = progress("n01_l01", "n01_l03")
        assertEquals("n01_l02", p.nextLesson?.id)
        assertFalse(p.isStepUnlocked("n01_l04"))
    }

    @Test
    fun stepStatusDescribeCadaNodoDelCamino() {
        val p = progress("n01_l01", "n01_l02")
        assertEquals(StepStatus.Done, p.stepStatus("n01_l01"))
        assertEquals(StepStatus.Current, p.stepStatus("n01_l03"))
        assertEquals(StepStatus.Locked, p.stepStatus("n01_l04"))
        assertEquals(StepStatus.Locked, p.stepStatus("n01_boss"))
        assertEquals(StepStatus.Locked, p.stepStatus("n02_l01"))
    }

    @Test
    fun enUnNivelDeRepasoLosPasosAbiertosSonDisponiblesYNoLaSiguiente() {
        val p = CurriculumProgress(base, emptySet(), startModuleIndex = 6)
        assertEquals(StepStatus.Current, p.stepStatus("n07_l01"))
        assertEquals(StepStatus.Available, p.stepStatus("n01_l01")) // Repaso: abierto
        assertEquals(StepStatus.Locked, p.stepStatus("n01_l02"))
    }

    @Test
    fun unCurriculoVacioNoDivideEntreCero() {
        val p = CurriculumProgress(Curriculum(emptyList()), emptySet())
        assertEquals(0, p.percent)
        assertNull(p.nextLesson)
    }
}
