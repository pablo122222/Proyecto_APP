package me.pablorincon.proyecto_app.domain

import me.pablorincon.proyecto_app.domain.tutor.DemoTutorAnswers
import me.pablorincon.proyecto_app.domain.tutor.TUTOR_MAX_QUESTION
import me.pablorincon.proyecto_app.domain.tutor.TutorChat
import me.pablorincon.proyecto_app.domain.tutor.TutorMessage
import me.pablorincon.proyecto_app.domain.tutor.TutorReply
import me.pablorincon.proyecto_app.domain.tutor.TutorTopic
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class TutorTest {

    // ---------- Palabras clave de la demo (guía §9) ----------
    @Test
    fun lasPreguntasSugeridasRecibenSuRespuesta() {
        assertEquals(TutorTopic.IF, DemoTutorAnswers.topicFor("¿Qué es un if?"))
        assertEquals(TutorTopic.ELSE, DemoTutorAnswers.topicFor("¿Cuál es la diferencia entre if y else?"))
        assertEquals(TutorTopic.VARIABLE, DemoTutorAnswers.topicFor("Explícame las variables con un ejemplo"))
    }

    @Test
    fun noImportanMayusculasNiTildes() {
        assertEquals(TutorTopic.IF, DemoTutorAnswers.topicFor("CONDICIÓN"))
        assertEquals(TutorTopic.LOOP, DemoTutorAnswers.topicFor("¿Cómo funciona un While?"))
        assertEquals(TutorTopic.ELSE, DemoTutorAnswers.topicFor("y si no, ¿sino qué?"))
    }

    @Test
    fun soloCuentanPalabrasCompletas() {
        // "diferencia" contiene "if" y "formato" contiene "for": no deben confundirse
        assertEquals(TutorTopic.UNKNOWN, DemoTutorAnswers.topicFor("¿Qué formato uso?"))
        assertEquals(TutorTopic.UNKNOWN, DemoTutorAnswers.topicFor("Hola tutor"))
        assertEquals(TutorTopic.LOOP, DemoTutorAnswers.topicFor("for numero in range(3)"))
    }

    // ---------- Conversación ----------
    @Test
    fun preguntarAgregaLaPreguntaYEsperaLaRespuesta() {
        val chat = TutorChat().withInput("  ¿Qué es una variable?  ")
        assertTrue(chat.canSend)
        val asking = chat.ask(chat.input, clearInput = true)!!
        assertTrue(asking.isAnswering)
        assertFalse(asking.canSend)
        assertEquals("", asking.input)
        assertEquals("¿Qué es una variable?", (asking.messages.last() as TutorMessage.FromUser).text)

        val answered = asking.withReply(TutorReply.Topic(TutorTopic.VARIABLE))
        assertFalse(answered.isAnswering)
        assertEquals(3, answered.messages.size)
        assertEquals(3, answered.messages.map { it.id }.toSet().size) // Ids únicos
    }

    @Test
    fun noSePreguntaVacioNiMientrasResponde() {
        assertNull(TutorChat().ask("   ", clearInput = true))
        val asking = TutorChat().ask("¿Qué es un if?", clearInput = false)!!
        assertNull(asking.ask("¿Otra?", clearInput = false))
    }

    @Test
    fun laSugerenciaNoBorraLoQueSeEstaEscribiendo() {
        val asking = TutorChat().withInput("borrador").ask("¿Qué es un if?", clearInput = false)!!
        assertEquals("borrador", asking.input)
    }

    @Test
    fun reiniciarDejaSoloElSaludo() {
        val asking = TutorChat().ask("¿Qué es un if?", clearInput = true)!!
        assertEquals(asking, asking.cleared()) // No se borra a mitad de una respuesta
        val cleared = asking.withReply(TutorReply.Topic(TutorTopic.IF)).withInput("hola").cleared()
        assertFalse(cleared.hasConversation)
        assertEquals("hola", cleared.input)
    }

    @Test
    fun laPreguntaTieneLimite() {
        assertEquals(TUTOR_MAX_QUESTION, TutorChat().withInput("a".repeat(TUTOR_MAX_QUESTION + 50)).input.length)
    }
}
