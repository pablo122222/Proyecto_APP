package me.pablorincon.proyecto_app.domain.tutor

import java.text.Normalizer

// Temas con respuesta predefinida en la versión de demostración (guía §9).
// El texto de cada uno vive en strings.xml; aquí solo se decide cuál toca.
enum class TutorTopic { ELSE, IF, VARIABLE, LOOP, UNKNOWN }

// Lo que dice el tutor. Con una IA real llegaría texto libre (Free); la demo usa temas.
sealed interface TutorReply {
    data class Topic(val topic: TutorTopic) : TutorReply
    data class Free(val text: String) : TutorReply
}

sealed interface TutorMessage {
    val id: Int

    data class Greeting(override val id: Int) : TutorMessage
    data class FromUser(override val id: Int, val text: String) : TutorMessage
    data class FromTutor(override val id: Int, val reply: TutorReply) : TutorMessage
}

// Quien responde las preguntas. Hoy es la demo; mañana puede ser un servicio de IA
// sin tocar la pantalla (decisión del proyecto: aislar las respuestas detrás de esta interfaz).
interface TutorRepository {
    suspend fun reply(question: String, history: List<TutorMessage>): TutorReply
}

// Reglas de la demo: busca palabras clave en la pregunta, sin importar mayúsculas ni tildes.
// El orden importa: "diferencia entre if y else" debe responder sobre else, no sobre if.
object DemoTutorAnswers {

    private val rules: List<Pair<TutorTopic, Set<String>>> = listOf(
        TutorTopic.ELSE to setOf("else", "sino", "diferencia"),
        TutorTopic.IF to setOf("if", "condicional", "condicionales", "condicion", "condiciones"),
        TutorTopic.VARIABLE to setOf("variable", "variables"),
        TutorTopic.LOOP to setOf("bucle", "bucles", "for", "while", "ciclo", "ciclos")
    )

    fun topicFor(question: String): TutorTopic {
        val words = words(question)
        return rules.firstOrNull { (_, keys) -> keys.any { it in words } }?.first ?: TutorTopic.UNKNOWN
    }

    // Palabras completas en minúsculas y sin tildes: "for" no debe coincidir con "formato"
    // ni "if" con "diferencia".
    internal fun words(text: String): Set<String> {
        val plain = Normalizer.normalize(text.lowercase(), Normalizer.Form.NFD)
            .replace(Regex("\\p{Mn}+"), "")
        return plain.split(Regex("[^a-z0-9ñ_]+")).filter { it.isNotEmpty() }.toSet()
    }
}

// Límite de la pregunta escrita (no está en el mockup): evita textos enormes en el chat.
const val TUTOR_MAX_QUESTION = 300
