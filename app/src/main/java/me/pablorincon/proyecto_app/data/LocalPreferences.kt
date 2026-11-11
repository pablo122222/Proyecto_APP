package me.pablorincon.proyecto_app.data

import android.content.Context
import androidx.core.content.edit
import me.pablorincon.proyecto_app.domain.TextSize
import me.pablorincon.proyecto_app.domain.ThemeMode
import me.pablorincon.proyecto_app.domain.UserPreferences

// Copia de las preferencias en el teléfono. Firestore es la fuente de verdad, pero esta copia
// permite aplicarlas desde el primer cuadro al abrir la app (sin esperar la red) y
// reprogramar el recordatorio al reiniciar el teléfono.
class LocalPreferences(context: Context) {

    private val prefs = context.applicationContext.getSharedPreferences(FILE, Context.MODE_PRIVATE)

    fun load(): UserPreferences {
        val d = UserPreferences()
        return UserPreferences(
            textSize = TextSize.fromId(prefs.getString(TEXT_SIZE, null)),
            themeMode = ThemeMode.fromId(prefs.getString(THEME_MODE, null)),
            highContrast = prefs.getBoolean(HIGH_CONTRAST, d.highContrast),
            reduceMotion = prefs.getBoolean(REDUCE_MOTION, d.reduceMotion),
            soundCues = prefs.getBoolean(SOUND_CUES, d.soundCues),
            vibration = prefs.getBoolean(VIBRATION, d.vibration),
            reminders = prefs.getBoolean(REMINDERS, d.reminders),
            reminderHour = prefs.getInt(REMINDER_HOUR, d.reminderHour),
            reminderMinute = prefs.getInt(REMINDER_MINUTE, d.reminderMinute)
        )
    }

    fun save(value: UserPreferences) = prefs.edit {
        putString(TEXT_SIZE, value.textSize.id)
        putString(THEME_MODE, value.themeMode.id)
        putBoolean(HIGH_CONTRAST, value.highContrast)
        putBoolean(REDUCE_MOTION, value.reduceMotion)
        putBoolean(SOUND_CUES, value.soundCues)
        putBoolean(VIBRATION, value.vibration)
        putBoolean(REMINDERS, value.reminders)
        putInt(REMINDER_HOUR, value.reminderHour)
        putInt(REMINDER_MINUTE, value.reminderMinute)
    }

    // Al cerrar sesión o eliminar la cuenta, el teléfono vuelve a los valores por defecto.
    fun clear() = prefs.edit { clear() }

    private companion object {
        const val FILE = "preferencias"
        const val TEXT_SIZE = "textSize"
        const val THEME_MODE = "themeMode"
        const val HIGH_CONTRAST = "highContrast"
        const val REDUCE_MOTION = "reduceMotion"
        const val SOUND_CUES = "soundCues"
        const val VIBRATION = "vibration"
        const val REMINDERS = "reminders"
        const val REMINDER_HOUR = "reminderHour"
        const val REMINDER_MINUTE = "reminderMinute"
    }
}
