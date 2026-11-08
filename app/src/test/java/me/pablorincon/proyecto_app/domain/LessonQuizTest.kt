package me.pablorincon.proyecto_app.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class LessonQuizTest {

    private fun question(name: String) = QuizQuestion(
        prompt = name,
        options = listOf("a", "b", "c"),
        correctIndex = 1,
        correctFeedback = "bien",
        wrongFeedback = "mal"
    )

    private val q1 = question("q1")
    private val q2 = question("q2")
    private val q3 = question("q3")

    @Test
    fun empiezaEnLaPrimeraPreguntaSinAciertos() {
        val quiz = LessonQuiz(listOf(q1, q2, q3))
        assertEquals(q1, quiz.current)
        assertEquals(0, quiz.correctCount)
        assertEquals(3, quiz.total)
        assertFalse(quiz.isComplete)
        assertFalse(quiz.isAnswered)
    }

    @Test
    fun unaRespuestaCorrectaAvanzaYCuentaDeInmediato() {
        val answered = LessonQuiz(listOf(q1, q2)).select(1)
        assertTrue(answered.isSelectionCorrect)
        assertEquals(1, answered.correctCount)
        assertEquals(q2, answered.next().current)
        assertEquals(1, answered.next().correctCount)
    }

    @Test
    fun unaPreguntaFalladaPasaAlFinalDeLaCola() {
        val quiz = LessonQuiz(listOf(q1, q2, q3)).select(0).next()
        assertEquals(q2, quiz.current)
        val afterQ2 = quiz.select(1).next()
        assertEquals(q3, afterQ2.current)
        val afterQ3 = afterQ2.select(1).next()
        assertEquals(q1, afterQ3.current) // La fallada vuelve cuando se acaban las demás
        assertEquals(2, afterQ3.correctCount)
    }

    @Test
    fun fallarNoPenalizaYSePuedeReintentarHastaAcertar() {
        var quiz = LessonQuiz(listOf(q1))
        repeat(3) { quiz = quiz.select(2).next() } // Falla tres veces
        assertFalse(quiz.isComplete)
        assertEquals(0, quiz.correctCount)
        quiz = quiz.select(1).next()
        assertTrue(quiz.isComplete)
        assertNull(quiz.current)
    }

    @Test
    fun laLeccionTerminaSoloCuandoTodasEstanAcertadas() {
        var quiz = LessonQuiz(listOf(q1, q2))
        quiz = quiz.select(0).next() // q1 fallada
        quiz = quiz.select(1).next() // q2 acertada
        assertFalse(quiz.isComplete)
        quiz = quiz.select(1).next() // q1 acertada
        assertTrue(quiz.isComplete)
        assertEquals(2, quiz.correctCount)
    }

    @Test
    fun noSePuedeCambiarLaRespuestaYaDada() {
        val quiz = LessonQuiz(listOf(q1)).select(0).select(1)
        assertEquals(0, quiz.selectedIndex)
        assertFalse(quiz.isSelectionCorrect)
    }

    @Test
    fun noSePuedePasarSinResponderNiElegirUnaOpcionInexistente() {
        val quiz = LessonQuiz(listOf(q1))
        assertEquals(q1, quiz.next().current)
        assertFalse(quiz.select(9).isAnswered)
    }
}
