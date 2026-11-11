package me.pablorincon.proyecto_app.data

import kotlinx.coroutines.delay
import me.pablorincon.proyecto_app.domain.tutor.DemoTutorAnswers
import me.pablorincon.proyecto_app.domain.tutor.TutorMessage
import me.pablorincon.proyecto_app.domain.tutor.TutorReply
import me.pablorincon.proyecto_app.domain.tutor.TutorRepository

// Tutor de demostración: respuestas predefinidas por palabra clave, sin red ni IA real.
// La pausa imita el tiempo que tardaría un servicio real, para que se vea "escribiendo…".
class DemoTutorRepository(private val thinkingMillis: Long = THINKING_MILLIS) : TutorRepository {

    override suspend fun reply(
        question: String,
        history: List<TutorMessage>,
        onPartial: (String) -> Unit
    ): TutorReply {
        delay(thinkingMillis)
        return TutorReply.Topic(DemoTutorAnswers.topicFor(question))
    }

    private companion object {
        const val THINKING_MILLIS = 900L
    }
}
