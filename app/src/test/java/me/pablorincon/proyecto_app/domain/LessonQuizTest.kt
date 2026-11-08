package me.pablorincon.proyecto_app.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class LessonQuizTest {

    private fun exercise(name: String) = MultipleChoice(
        prompt = name,
        options = listOf("a", "b", "c"),
        correctIndex = 1,
        correctFeedback = "bien",
        wrongFeedback = "mal"
    )

    private val e1 = exercise("e1")
    private val e2 = exercise("e2")
    private val e3 = exercise("e3")

    @Test
    fun empiezaEnElPrimerEjercicioSinAciertos() {
        val quiz = LessonQuiz(listOf(e1, e2, e3))
        assertEquals(e1, quiz.current)
        assertEquals(0, quiz.correctCount)
        assertEquals(3, quiz.total)
        assertFalse(quiz.isComplete)
        assertFalse(quiz.isAnswered)
    }

    @Test
    fun unResultadoCorrectoAvanzaYCuentaDeInmediato() {
        val answered = LessonQuiz(listOf(e1, e2)).submit(true)
        assertTrue(answered.isLastCorrect)
        assertEquals(1, answered.correctCount)
        assertEquals(e2, answered.next().current)
        assertEquals(1, answered.next().correctCount)
    }

    @Test
    fun unEjercicioFalladoPasaAlFinalDeLaCola() {
        val quiz = LessonQuiz(listOf(e1, e2, e3)).submit(false).next()
        assertEquals(e2, quiz.current)
        val afterE2 = quiz.submit(true).next()
        assertEquals(e3, afterE2.current)
        val afterE3 = afterE2.submit(true).next()
        assertEquals(e1, afterE3.current) // El fallado vuelve cuando se acaban los demás
        assertEquals(2, afterE3.correctCount)
    }

    @Test
    fun fallarNoPenalizaYSePuedeReintentarHastaAcertar() {
        var quiz = LessonQuiz(listOf(e1))
        repeat(3) { quiz = quiz.submit(false).next() }
        assertFalse(quiz.isComplete)
        assertEquals(0, quiz.correctCount)
        quiz = quiz.submit(true).next()
        assertTrue(quiz.isComplete)
        assertNull(quiz.current)
    }

    @Test
    fun laLeccionTerminaSoloCuandoTodosEstanResueltos() {
        var quiz = LessonQuiz(listOf(e1, e2))
        quiz = quiz.submit(false).next() // e1 fallado
        quiz = quiz.submit(true).next()  // e2 resuelto
        assertFalse(quiz.isComplete)
        quiz = quiz.submit(true).next()  // e1 resuelto
        assertTrue(quiz.isComplete)
        assertEquals(2, quiz.correctCount)
    }

    @Test
    fun noSePuedeCambiarElResultadoYaDado() {
        val quiz = LessonQuiz(listOf(e1)).submit(false).submit(true)
        assertFalse(quiz.isLastCorrect)
        assertTrue(quiz.isAnswered)
    }

    @Test
    fun noSePuedePasarSinComprobar() {
        val quiz = LessonQuiz(listOf(e1))
        assertEquals(e1, quiz.next().current)
        assertEquals(0, quiz.next().step)
    }

    @Test
    fun cadaContinuarSubeElPasoParaReiniciarElEjercicio() {
        val quiz = LessonQuiz(listOf(e1, e2)).submit(true).next()
        assertEquals(1, quiz.step)
        assertFalse(quiz.isAnswered)
    }
}
