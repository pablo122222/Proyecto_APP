package me.pablorincon.proyecto_app.ui.feedback

import android.media.AudioManager
import android.media.ToneGenerator
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import me.pablorincon.proyecto_app.ui.theme.AppAppearance

// Respuesta al comprobar un ejercicio, según Configuración:
// "Vibración" (por defecto activada) y "Sonidos de respuesta" (un tono corto del sistema,
// sin archivos de audio: acierto = tono de confirmación, fallo = tono de error).
object AnswerFeedback {

    private const val VOLUME = 70        // 0..100
    private const val TONE_MILLIS = 180

    fun onAnswer(correct: Boolean, haptics: HapticFeedback) {
        if (AppAppearance.vibration) {
            haptics.performHapticFeedback(if (correct) HapticFeedbackType.Confirm else HapticFeedbackType.Reject)
        }
        if (AppAppearance.soundCues) playTone(correct)
    }

    private fun playTone(correct: Boolean) {
        // Si el teléfono no puede crear el tono (sin audio disponible) simplemente no suena
        val generator = runCatching { ToneGenerator(AudioManager.STREAM_MUSIC, VOLUME) }.getOrNull() ?: return
        generator.startTone(if (correct) ToneGenerator.TONE_PROP_ACK else ToneGenerator.TONE_PROP_NACK, TONE_MILLIS)
        // Se libera cuando termina de sonar
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({ generator.release() }, TONE_MILLIS * 3L)
    }
}
