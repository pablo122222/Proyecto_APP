package me.pablorincon.proyecto_app.data

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import me.pablorincon.proyecto_app.CodeMentorApp
import me.pablorincon.proyecto_app.domain.UserPreferences
import me.pablorincon.proyecto_app.notifications.ReminderScheduler
import me.pablorincon.proyecto_app.ui.theme.AppAppearance

// Guarda y aplica las preferencias de Configuración en tres lugares a la vez:
// la app en vivo (AppAppearance), la copia del teléfono y la cuenta en Firestore.
class PreferencesRepository(
    private val context: Context = CodeMentorApp.context,
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val local = LocalPreferences(context)

    fun current(): UserPreferences = local.load()

    // Cambio hecho por el usuario: se aplica al instante y se sube a la cuenta.
    // Firestore deja la escritura en cola si no hay red y la sube al volver la conexión.
    fun save(uid: String?, prefs: UserPreferences) {
        applyOnDevice(prefs)
        if (uid != null) {
            db.collection(USERS).document(uid)
                .set(mapOf(UserProfileMapper.PREFERENCES to UserProfileMapper.preferencesToMap(prefs)), SetOptions.merge())
        }
    }

    // Preferencias que llegan de la cuenta (al abrir la app o iniciar sesión).
    fun applyOnDevice(prefs: UserPreferences) {
        AppAppearance.apply(prefs)
        local.save(prefs)
        ReminderScheduler.apply(context, prefs)
    }

    // Al salir de la cuenta el teléfono vuelve a los valores por defecto (y sin recordatorio).
    fun resetDevice() = applyOnDevice(UserPreferences()).also { local.clear() }

    private companion object {
        const val USERS = "users"
    }
}
