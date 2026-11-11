package me.pablorincon.proyecto_app.domain

import kotlinx.coroutines.runBlocking
import me.pablorincon.proyecto_app.data.OllamaTutorRepository
import me.pablorincon.proyecto_app.domain.tutor.TutorBlock
import me.pablorincon.proyecto_app.domain.tutor.TutorBlock.Run
import me.pablorincon.proyecto_app.domain.tutor.TutorChat
import me.pablorincon.proyecto_app.domain.tutor.TutorFormat
import me.pablorincon.proyecto_app.domain.tutor.TutorMessage
import me.pablorincon.proyecto_app.domain.tutor.TutorPrompt
import me.pablorincon.proyecto_app.domain.tutor.TutorReply
import me.pablorincon.proyecto_app.domain.tutor.TutorRepository
import me.pablorincon.proyecto_app.domain.tutor.TutorTopic
import me.pablorincon.proyecto_app.domain.tutor.TutorTurn
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OllamaTutorTest {

    private val demo = object : TutorRepository {
        override suspend fun reply(question: String, history: List<TutorMessage>, onPartial: (String) -> Unit) =
            TutorReply.Topic(TutorTopic.UNKNOWN)
    }

    // ---------- Lo que se envía al modelo ----------
    @Test
    fun elPromptEmpiezaConLasInstruccionesYTerminaConLaPregunta() {
        val turns = TutorPrompt.turns("¿Qué es un for?", listOf(TutorMessage.Greeting(0)))
        assertEquals(TutorTurn.Role.SYSTEM, turns.first().role)
        assertEquals(TutorTurn(TutorTurn.Role.USER, "¿Qué es un for?"), turns.last())
        assertEquals(TutorPrompt.examples.size + 2, turns.size) // el saludo no se envía
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
        // instrucciones + ejemplos + últimos turnos + pregunta; la respuesta de la demo (tema) no cuenta
        assertEquals(TutorPrompt.examples.size + TutorPrompt.MAX_HISTORY_TURNS + 2, turns.size)
        assertEquals(TutorTurn(TutorTurn.Role.ASSISTANT, "respuesta 10"), turns[turns.size - 2])
    }

    // ---------- Respuesta y respaldo ----------
    @Test
    fun elTextoLlegaPorPartesYTerminaComoTextoLibre() = runBlocking {
        val repo = OllamaTutorRepository(chat = { _, onPartial ->
            onPartial("Un for")
            onPartial("Un for repite.  ")
            "Un for repite.  "
        }, fallback = demo)
        val partials = mutableListOf<String>()
        assertEquals(TutorReply.Free("Un for repite."), repo.reply("for", emptyList()) { partials += it })
        assertEquals(listOf("Un for", "Un for repite.  "), partials)
    }

    @Test
    fun sinOllamaOConRespuestaVaciaContestaLaDemo() = runBlocking {
        val caido = OllamaTutorRepository(chat = { _, _ -> error("sin conexión") }, fallback = demo)
        val vacio = OllamaTutorRepository(chat = { _, _ -> "   " }, fallback = demo)
        assertEquals(TutorReply.Topic(TutorTopic.UNKNOWN), caido.reply("for", emptyList()))
        assertEquals(TutorReply.Topic(TutorTopic.UNKNOWN), vacio.reply("for", emptyList()))
    }

    @Test
    fun siSeCortaAMitadSeQuedaLoYaEscrito() = runBlocking {
        val cortado = OllamaTutorRepository(chat = { _, onPartial ->
            onPartial("Un for repite")
            error("se cortó la wifi")
        }, fallback = demo)
        assertEquals(TutorReply.Free("Un for repite"), cortado.reply("for", emptyList()))
    }

    // ---------- Chat mientras el tutor escribe ----------
    @Test
    fun laRespuestaEnCursoCreceEnSuSitioYLuegoQuedaFija() {
        val asking = TutorChat().ask("¿Qué es un for?", clearInput = true)!!
        assertTrue(asking.awaitingFirstWords)

        val writing = asking.withPartial("Un for").withPartial("Un for repite")
        assertFalse(writing.awaitingFirstWords)
        assertTrue(writing.isAnswering)
        assertEquals(3, writing.messages.size) // saludo, pregunta y una sola burbuja en curso
        assertEquals(TutorReply.Free("Un for repite"), (writing.messages.last() as TutorMessage.FromTutor).reply)

        val done = writing.withReply(TutorReply.Free("Un for repite código."))
        assertEquals(3, done.messages.size)
        assertFalse(done.isAnswering)
        assertEquals(writing.messages.last().id, done.messages.last().id)
        // la siguiente pregunta tiene un id nuevo
        assertEquals(done.messages.last().id + 1, done.ask("otra", true)!!.messages.last().id)
    }

    @Test
    fun sinPreguntaPendienteNoSeAgregaTextoEnCurso() {
        assertEquals(TutorChat(), TutorChat().withPartial("hola"))
    }

    // ---------- Formato de las respuestas del modelo ----------
    @Test
    fun separaTextoYBloquesDeCodigo() {
        val text = "Un **for** repite.\n\n```python\nfor i in range(3):\n    print(i)\n```\nUsa `range`."
        assertEquals(
            listOf(
                TutorBlock.Prose(listOf(Run("Un "), Run("for", bold = true), Run(" repite."))),
                TutorBlock.Code("for i in range(3):\n    print(i)"),
                TutorBlock.Prose(listOf(Run("Usa "), Run("range", code = true), Run(".")))
            ),
            TutorFormat.blocks(text)
        )
    }

    @Test
    fun unBloqueSinCerrarEsCodigoHastaElFinal() {
        // mientras llega la respuesta, el ``` de cierre aún no está
        assertEquals(
            listOf(TutorBlock.Prose(listOf(Run("Mira:"))), TutorBlock.Code("x = 1")),
            TutorFormat.blocks("Mira:\n```python\nx = 1")
        )
    }

    @Test
    fun titulosYViñetasQuedanComoTextoSencillo() {
        assertEquals(
            listOf(TutorBlock.Prose(listOf(Run("Listas\n• Guardan varios valores")))),
            TutorFormat.blocks("## Listas\n* Guardan varios valores")
        )
    }
}
