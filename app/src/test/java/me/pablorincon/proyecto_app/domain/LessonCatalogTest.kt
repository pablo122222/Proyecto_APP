package me.pablorincon.proyecto_app.domain

import me.pablorincon.proyecto_app.domain.content.FundamentosLessons
import me.pablorincon.proyecto_app.domain.content.LessonCatalog
import me.pablorincon.proyecto_app.domain.content.VariablesLessons
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class LessonCatalogTest {

    @Test
    fun cadaLeccionConContenidoExisteEnElCurriculo() {
        (FundamentosLessons.all + VariablesLessons.all).forEach {
            assertNotNull("Sin lección en el currículo: ${it.lessonId}", Curriculum.Base.lesson(it.lessonId))
        }
    }

    @Test
    fun cadaLeccionTieneTresEjercicios() {
        (FundamentosLessons.all + VariablesLessons.all).forEach { lesson ->
            assertEquals(lesson.lessonId, 3, lesson.exercises.size)
            lesson.exercises.filterIsInstance<MultipleChoice>().forEach { assertEquals(lesson.lessonId, 4, it.options.size) }
        }
    }

    @Test
    fun elCatalogoDevuelveElContenidoPorId() {
        assertEquals("fundamentos_1", LessonCatalog.contentFor("fundamentos_1")?.lessonId)
        assertNull(LessonCatalog.contentFor("no_existe"))
    }

    @Test
    fun lasPreguntasNoRepitenOpcionesDentroDeLaMismaPregunta() {
        (FundamentosLessons.all + VariablesLessons.all).flatMap { it.exercises }.filterIsInstance<MultipleChoice>().forEach {
            assertTrue(it.prompt, it.options.toSet().size == it.options.size)
        }
    }

    @Test
    fun elModuloFundamentosTieneContenidoParaTodasSusLecciones() {
        val ids = Curriculum.Base.modules.first { it.id == "fundamentos" }.lessons.map { it.id }
        assertEquals(ids, FundamentosLessons.all.map { it.lessonId })
    }

    @Test
    fun elModuloVariablesTieneContenidoParaTodasSusLecciones() {
        val ids = Curriculum.Base.modules.first { it.id == "variables" }.lessons.map { it.id }
        assertEquals(ids, VariablesLessons.all.map { it.lessonId })
    }

    @Test
    fun todasLasLeccionesDelCatalogoTienenTresEjercicios() {
        Curriculum.Base.lessons.mapNotNull { LessonCatalog.contentFor(it.id) }.forEach { lesson ->
            assertEquals(lesson.lessonId, 3, lesson.exercises.size)
            lesson.exercises.filterIsInstance<MultipleChoice>().forEach { assertEquals(lesson.lessonId, 4, it.options.size) }
        }
    }

    @Test
    fun todoEjercicioDelCatalogoSePuedeResolverConSusPropiasRespuestas() {
        Curriculum.Base.lessons.mapNotNull { LessonCatalog.contentFor(it.id) }.forEach { lesson ->
            lesson.exercises.forEach { ex ->
                val ok = when (ex) {
                    is MultipleChoice -> ex.isCorrect(ex.correctIndex)
                    is OrderLines -> ex.isCorrect(ex.lines)
                    is FillBlanks -> ex.isCorrect(ex.answers)
                    is FindBug -> ex.isCorrect(ex.bugLineIndex)
                    is PredictOutput -> ex.acceptedAnswers.all { ex.isCorrect(it) }
                    is TypeCode -> ex.acceptedAnswers.all { ex.isCorrect(it) }
                }
                assertTrue("${lesson.lessonId}: ${ex.prompt}", ok)
            }
        }
    }

    @Test
    fun cadaLeccionUsaPocaOpcionMultiple() {
        Curriculum.Base.lessons.mapNotNull { LessonCatalog.contentFor(it.id) }
            .filter { it.lessonId.startsWith("fundamentos") } // Módulos ya rediseñados
            .forEach { lesson ->
                val choices = lesson.exercises.count { it is MultipleChoice }
                assertTrue("${lesson.lessonId} tiene $choices de opción múltiple", choices <= 1)
            }
    }
}
