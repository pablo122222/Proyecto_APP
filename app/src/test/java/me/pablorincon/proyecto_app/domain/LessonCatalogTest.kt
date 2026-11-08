package me.pablorincon.proyecto_app.domain

import me.pablorincon.proyecto_app.domain.content.FundamentosLessons
import me.pablorincon.proyecto_app.domain.content.LessonCatalog
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class LessonCatalogTest {

    @Test
    fun cadaLeccionConContenidoExisteEnElCurriculo() {
        FundamentosLessons.all.forEach {
            assertNotNull("Sin lección en el currículo: ${it.lessonId}", Curriculum.Base.lesson(it.lessonId))
        }
    }

    @Test
    fun cadaLeccionTieneTresPreguntasConCuatroOpciones() {
        FundamentosLessons.all.forEach { lesson ->
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
        FundamentosLessons.all.flatMap { it.questions }.forEach {
            assertTrue(it.prompt, it.options.toSet().size == it.options.size)
        }
    }
}
