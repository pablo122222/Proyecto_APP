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
    fun cadaLeccionTieneTresPreguntasConCuatroOpciones() {
        (FundamentosLessons.all + VariablesLessons.all).forEach { lesson ->
            assertEquals(lesson.lessonId, 3, lesson.questions.size)
            lesson.questions.forEach { assertEquals(lesson.lessonId, 4, it.options.size) }
        }
    }

    @Test
    fun elCatalogoDevuelveElContenidoPorId() {
        assertEquals("fundamentos_1", LessonCatalog.contentFor("fundamentos_1")?.lessonId)
        assertNull(LessonCatalog.contentFor("no_existe"))
    }

    @Test
    fun lasPreguntasNoRepitenOpcionesDentroDeLaMismaPregunta() {
        (FundamentosLessons.all + VariablesLessons.all).flatMap { it.questions }.forEach {
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
    fun todasLasLeccionesDelCatalogoTienenTresPreguntasConCuatroOpciones() {
        Curriculum.Base.lessons.mapNotNull { LessonCatalog.contentFor(it.id) }.forEach { lesson ->
            assertEquals(lesson.lessonId, 3, lesson.questions.size)
            lesson.questions.forEach { assertEquals(lesson.lessonId, 4, it.options.size) }
        }
    }
}
