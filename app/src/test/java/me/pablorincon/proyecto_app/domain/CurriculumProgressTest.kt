package me.pablorincon.proyecto_app.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CurriculumProgressTest {

    private val base = Curriculum.Base

    private fun progress(vararg completed: String) = CurriculumProgress(base, completed.toSet())

    // Ids de todas las lecciones de los primeros n módulos
    private fun lessonsOfFirstModules(count: Int): Array<String> =
        base.modules.take(count).flatMap { m -> m.lessons.map { it.id } }.toTypedArray()

    private fun statuses(p: CurriculumProgress) = p.modules.map { it.status }

    @Test
    fun usuarioNuevoEmpiezaEnElPrimerModuloConCeroPorCiento() {
        val p = progress()
        assertEquals(
            listOf(ModuleStatus.InProgress) + List(5) { ModuleStatus.Locked },
            statuses(p)
        )
        assertEquals(0, p.percent)
        assertEquals("fundamentos_1", p.nextLesson?.id)
        assertEquals("fundamentos", p.currentModule?.id)
    }

    @Test
    fun alCompletarUnaLeccionAvanzaLaSiguienteDentroDelModulo() {
        val p = progress("fundamentos_1")
        assertEquals("fundamentos_2", p.nextLesson?.id)
        assertEquals(ModuleStatus.InProgress, p.modules.first().status)
        assertEquals(1, p.modules.first().completedSteps)
        assertEquals(4, p.percent) // 1 de 25 = 4 %
    }

    @Test
    fun alTerminarUnModuloSeCompletaYSeAbreElSiguiente() {
        val p = progress(*lessonsOfFirstModules(1))
        assertEquals(
            listOf(ModuleStatus.Completed, ModuleStatus.InProgress) + List(4) { ModuleStatus.Locked },
            statuses(p)
        )
        assertEquals("variables_1", p.nextLesson?.id)
        assertEquals("variables", p.currentModule?.id)
    }

    @Test
    fun alTerminarTodoEstaAlCienSinSiguienteLeccion() {
        val p = progress(*lessonsOfFirstModules(6))
        assertTrue(statuses(p).all { it == ModuleStatus.Completed })
        assertEquals(100, p.percent)
        assertNull(p.nextLesson)
        assertNull(p.currentModule)
    }

    @Test
    fun elPorcentajeSeRedondeaHaciaAbajoYNoLlegaAlCienSinTerminar() {
        // 24 de 25 = 96 %
        val almostAll = base.lessons.dropLast(1).map { it.id }.toTypedArray()
        assertEquals(96, progress(*almostAll).percent)
        // 2 de 25 = 8 %
        assertEquals(8, progress("fundamentos_1", "fundamentos_2").percent)
        // 3 de 25 = 12 % (12.0)
        assertEquals(12, progress("fundamentos_1", "fundamentos_2", "fundamentos_3").percent)
    }

    @Test
    fun losIdsDesconocidosSeIgnoran() {
        val p = progress("leccion_borrada", "fundamentos_1")
        assertEquals(1, p.modules.first().completedSteps)
        assertEquals(4, p.percent)
        assertFalse(p.isCompleted("leccion_borrada"))
    }

    @Test
    fun unaLeccionSoloSeAbreSiTodasLasAnterioresEstanCompletadas() {
        val p = progress("fundamentos_1")
        assertTrue(p.isStepUnlocked("fundamentos_1")) // ya completada: se puede repasar
        assertTrue(p.isStepUnlocked("fundamentos_2")) // la siguiente
        assertFalse(p.isStepUnlocked("fundamentos_3")) // falta la 2
        assertFalse(p.isStepUnlocked("variables_1")) // falta terminar Fundamentos
    }

    @Test
    fun laPrimeraLeccionSiempreEstaAbierta() {
        assertTrue(progress().isStepUnlocked("fundamentos_1"))
    }

    @Test
    fun unIdDesconocidoNuncaSeAbre() {
        assertFalse(progress(*lessonsOfFirstModules(6)).isStepUnlocked("no_existe"))
    }

    @Test
    fun siSeSaltaUnaLeccionElModuloSigueEnCursoYLaSiguienteEsLaQueFalta() {
        val p = progress("fundamentos_1", "fundamentos_3")
        assertEquals("fundamentos_2", p.nextLesson?.id)
        assertEquals(ModuleStatus.InProgress, p.modules.first().status)
        assertFalse(p.isStepUnlocked("fundamentos_4"))
    }

    @Test
    fun elMiniProyectoSeBloqueaHastaCompletarFunciones() {
        val untilBuckets = progress(*lessonsOfFirstModules(4))
        assertEquals(ModuleStatus.Locked, untilBuckets.modules.last().status)

        val untilFunctions = progress(*lessonsOfFirstModules(5))
        assertEquals(ModuleStatus.InProgress, untilFunctions.modules.last().status)
        assertEquals("proyecto_1", untilFunctions.nextLesson?.id)
        assertEquals(96, untilFunctions.percent)
    }

    @Test
    fun unCurriculoVacioNoDivideEntreCero() {
        val p = CurriculumProgress(Curriculum(emptyList()), emptySet())
        assertEquals(0, p.percent)
        assertNull(p.nextLesson)
    }
}
