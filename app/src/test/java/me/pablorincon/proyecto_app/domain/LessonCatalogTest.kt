package me.pablorincon.proyecto_app.domain

import me.pablorincon.proyecto_app.domain.content.BossCatalog
import me.pablorincon.proyecto_app.domain.content.LessonCatalog
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

// Integridad del contenido escrito: cada nivel con contenido debe estar completo y ser resoluble.
class LessonCatalogTest {

    private val curriculum = Curriculum.Base

    // Niveles con contenido escrito (se amplía a medida que se escriben más)
    private val writtenLevels = (1..12).toList()

    private fun lessonsOf(level: Int) = curriculum.modules[level - 1].lessons.map { it.id }

    private val allWritten: List<LessonContent> =
        curriculum.lessons.mapNotNull { LessonCatalog.contentFor(it.id) }

    @Test
    fun cadaNivelEscritoTieneContenidoParaLasOnceLeccionesEnOrdenYSuJefe() {
        writtenLevels.forEach { level ->
            val ids = lessonsOf(level)
            assertEquals("Nivel $level", 11, ids.size)
            ids.forEach { assertNotNull("Falta la lección $it", LessonCatalog.contentFor(it)) }
            assertNotNull("Falta el jefe del nivel $level", BossCatalog.contentFor(Curriculum.bossId(level)))
        }
    }

    @Test
    fun elContenidoCoincideConSuIdYNoHayLeccionesFueraDelCurriculo() {
        allWritten.forEach { assertEquals(it.lessonId, LessonCatalog.contentFor(it.lessonId)?.lessonId) }
        assertEquals(writtenLevels.size * 11, allWritten.size)
        assertNull(LessonCatalog.contentFor("no_existe"))
    }

    @Test
    fun cadaLeccionTieneTresEjerciciosYUsaPocaOpcionMultiple() {
        allWritten.forEach { lesson ->
            assertEquals(lesson.lessonId, 3, lesson.exercises.size)
            val choices = lesson.exercises.count { it is MultipleChoice }
            assertTrue("${lesson.lessonId} tiene $choices de opción múltiple", choices <= 1)
            lesson.exercises.filterIsInstance<MultipleChoice>().forEach {
                assertEquals(lesson.lessonId, 4, it.options.size)
            }
        }
    }

    @Test
    fun cadaJefeTieneCincoEjerciciosYUsaPocaOpcionMultiple() {
        writtenLevels.forEach { level ->
            val boss = BossCatalog.contentFor(Curriculum.bossId(level))!!
            assertEquals(Curriculum.bossId(level), boss.bossId)
            assertEquals(boss.bossId, 5, boss.exercises.size)
            assertTrue(boss.exercises.count { it is MultipleChoice } <= 1)
        }
    }

    @Test
    fun todoEjercicioSePuedeResolverConSusPropiasRespuestas() {
        val exercises = allWritten.flatMap { l -> l.exercises.map { l.lessonId to it } } +
            writtenLevels.flatMap { level ->
                BossCatalog.contentFor(Curriculum.bossId(level))!!.exercises.map { Curriculum.bossId(level) to it }
            }
        exercises.forEach { (owner, ex) ->
            val ok = when (ex) {
                is MultipleChoice -> ex.isCorrect(ex.correctIndex)
                is OrderLines -> ex.isCorrect(ex.lines)
                is FillBlanks -> ex.isCorrect(ex.answers)
                is FindBug -> ex.isCorrect(ex.bugLineIndex)
                is PredictOutput -> ex.acceptedAnswers.all { ex.isCorrect(it) }
                is TypeCode -> ex.acceptedAnswers.all { ex.isCorrect(it) }
                is GlitchPuzzle -> ex.isCorrect(ex.solutionLines)
            }
            assertTrue("$owner: ${ex.prompt}", ok)
        }
    }

    @Test
    fun losEjerciciosDeOrdenNuncaEmpiezanResueltos() {
        val exercises = allWritten.flatMap { it.exercises } +
            writtenLevels.flatMap { BossCatalog.contentFor(Curriculum.bossId(it))!!.exercises }
        exercises.filterIsInstance<OrderLines>().forEach { ex ->
            (0..10).forEach { step ->
                assertTrue(ex.prompt, !ex.isCorrect(ex.initialOrder(step).map { ex.lines[it] }))
            }
        }
    }

    @Test
    fun noHayTextosVaciosNiOpcionesRepetidasEnLasOpcionesMultiples() {
        val exercises = allWritten.flatMap { it.exercises }.filterIsInstance<MultipleChoice>()
        exercises.forEach {
            assertTrue(it.prompt, it.options.toSet().size == it.options.size)
            assertTrue(it.prompt, it.options.all { option -> option.isNotBlank() })
        }
    }

    @Test
    fun todasLasLeccionesYJefesTienenMensajesDeAciertoYFallo() {
        val exercises = allWritten.flatMap { it.exercises } +
            writtenLevels.flatMap { BossCatalog.contentFor(Curriculum.bossId(it))!!.exercises }
        exercises.forEach {
            assertTrue(it.prompt, it.correctFeedback.isNotBlank() && it.wrongFeedback.isNotBlank())
        }
    }

    @Test
    fun losTablerosDeGlitchEstanBienArmados() {
        val puzzles = (allWritten.flatMap { it.exercises } +
            writtenLevels.flatMap { BossCatalog.contentFor(Curriculum.bossId(it))!!.exercises })
            .filterIsInstance<GlitchPuzzle>()
        puzzles.forEach { ex ->
            // La solución solo usa bloques de la paleta (el número del for puede cambiar)
            val kinds = ex.palette.map { it::class }
            ex.solutionLines.forEach { line -> assertTrue("${ex.prompt}: ${line.code}", line.block!!::class in kinds || ex.mode == GlitchMode.COMPLETE) }
            if (ex.maxLines != null) assertTrue(ex.prompt, ex.solutionLines.size <= ex.maxLines)
            // Arregla y Completa no empiezan resueltos
            if (ex.mode != GlitchMode.BUILD) assertTrue(ex.prompt, !ex.isCorrect(ex.starterLines))
            // En Completa la solución respeta las líneas fijas y la sangría del programa inicial
            if (ex.mode == GlitchMode.COMPLETE) {
                assertEquals(ex.prompt, ex.starterLines.size, ex.solutionLines.size)
                ex.starterLines.zip(ex.solutionLines).forEach { (start, sol) ->
                    assertEquals(ex.prompt, start.indent, sol.indent)
                    if (start.locked) assertEquals(ex.prompt, start.block, sol.block)
                    if (start.slot) assertTrue("${ex.prompt}: ${sol.code}", sol.block!!::class in kinds)
                }
            }
        }
    }
}
