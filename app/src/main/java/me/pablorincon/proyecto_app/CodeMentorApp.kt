package me.pablorincon.proyecto_app

import android.app.Application
import android.content.Context
import me.pablorincon.proyecto_app.data.LocalPreferences
import me.pablorincon.proyecto_app.notifications.ReminderScheduler
import me.pablorincon.proyecto_app.ui.theme.AppAppearance

// Arranque de la app: aplica las preferencias guardadas en el teléfono antes de dibujar
// la primera pantalla y crea el canal de notificaciones del recordatorio.
class CodeMentorApp : Application() {
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        AppAppearance.apply(LocalPreferences(this).load())
        ReminderScheduler.createChannel(this)
    }

    companion object {
        // Contexto de la aplicación (nunca de una pantalla) para los repositorios que lo necesitan.
        lateinit var context: Context
            private set
    }
}
