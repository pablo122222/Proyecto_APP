package me.pablorincon.proyecto_app.domain

import me.pablorincon.proyecto_app.domain.content.LessonCatalog
import org.junit.Assert.assertEquals
import org.junit.Test

class WalkthroughTest {

    private val code = "n = 1\nwhile n <= 2:\n    print(n)\n    n += 1"
    private val exercise = MultipleChoice("¿?", listOf("a", "b"), 0, "bien", "mal")

    @Test(expected = IllegalArgumentException::class)
    fun unPasoNoPuedeApuntarAUnaLineaQueNoExiste() {
        LessonContent("x", "t", "h", "e", code, "1\n2", listOf(exercise), listOf(WalkStep(line = 5, note = "nota")))
    }

    @Test
    fun laLineaSeNumeraComoEnPantalla() {
        // Las líneas 1 y 4 existen: no falla
        LessonContent("x", "t", "h", "e", code, "1\n2", listOf(exercise), listOf(WalkStep(1, "a"), WalkStep(4, "b")))
    }

    // Todo paso a paso escrito debe terminar mostrando exactamente la salida esperada de la lección.
    @Test
    fun cadaPasoAPasoTerminaConLaSalidaEsperada() {
        Curriculum.Base.steps.mapNotNull { LessonCatalog.contentFor(it.id) }
            .filter { it.walkthrough.isNotEmpty() }
            .forEach { lesson ->
                assertEquals(lesson.lessonId, lesson.expectedOutput, lesson.walkthrough.last().output.joinToString("\n"))
            }
    }

    // La salida solo crece: cada paso conserva lo impreso antes.
    @Test
    fun laSalidaDeCadaPasoIncluyeLaAnterior() {
        Curriculum.Base.steps.mapNotNull { LessonCatalog.contentFor(it.id) }.forEach { lesson ->
            lesson.walkthrough.zipWithNext().forEach { (before, after) ->
                assertEquals(lesson.lessonId, before.output, after.output.take(before.output.size))
            }
        }
    }
}
