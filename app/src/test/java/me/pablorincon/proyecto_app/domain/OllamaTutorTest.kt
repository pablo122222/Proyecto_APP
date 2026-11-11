package me.pablorincon.proyecto_app.domain

import kotlinx.coroutines.runBlocking
import me.pablorincon.proyecto_app.data.OllamaTutorRepository
import me.pablorincon.proyecto_app.domain.tutor.TutorMessage
import me.pablorincon.proyecto_app.domain.tutor.TutorPrompt
import me.pablorincon.proyecto_app.domain.tutor.TutorReply
import me.pablorincon.proyecto_app.domain.tutor.TutorRepository
import me.pablorincon.proyecto_app.domain.tutor.TutorTopic
import me.pablorincon.proyecto_app.domain.tutor.TutorTurn
import org.junit.Assert.assertEquals
import org.junit.Test

class OllamaTutorTest {

    private val demo = object : TutorRepository {
        override suspend fun reply(question: String, history: List<TutorMessage>) =
            TutorReply.Topic(TutorTopic.UNKNOWN)
    }

    @Test
    fun elPromptEmpiezaConLasInstruccionesYTerminaConLaPregunta() {
        val turns = TutorPrompt.turns("¿Qué es un for?", listOf(TutorMessage.Greeting(0)))
        assertEquals(TutorTurn.Role.SYSTEM, turns.first().role)
        assertEquals(TutorTurn(TutorTurn.Role.USER, "¿Qué es un for?"), turns.last())
        assertEquals(2, turns.size) // el saludo no se envía
    }

    @Test
    fun elHistorialSoloLlevaTextoRealYSeRecorta() {
        val history = listOf(TutorMessage.Greeting(0)) + (1..10).flatMap { i ->
            listOf(
                TutorMessage.FromUser(i * 2, "pregunta $i"),
                TutorMessage.FromTutor(i * 2 + 1, TutorReply.Free("respuesta $i"))
            )
        } + TutorMessage.FromTutor(99, TutorReply.Topic(TutorTopic.IF))
        val turns = TutorPrompt.turns("nueva", history)
        // instrucciones + últimos turnos + pregunta; la respuesta de la demo (tema) no cuenta
        assertEquals(TutorPrompt.MAX_HISTORY_TURNS + 2, turns.size)
        assertEquals(TutorTurn(TutorTurn.Role.ASSISTANT, "respuesta 10"), turns[turns.size - 2])
    }

    @Test
    fun conRespuestaDelModeloDevuelveTextoLibre() = runBlocking {
        val repo = OllamaTutorRepository(chat = { "  Un for repite.  " }, fallback = demo)
        assertEquals(TutorReply.Free("Un for repite."), repo.reply("for", emptyList()))
    }

    @Test
    fun sinOllamaOConRespuestaVaciaContestaLaDemo() = runBlocking {
        val caido = OllamaTutorRepository(chat = { error("sin conexión") }, fallback = demo)
        val vacio = OllamaTutorRepository(chat = { "   " }, fallback = demo)
        assertEquals(TutorReply.Topic(TutorTopic.UNKNOWN), caido.reply("for", emptyList()))
        assertEquals(TutorReply.Topic(TutorTopic.UNKNOWN), vacio.reply("for", emptyList()))
    }
}
