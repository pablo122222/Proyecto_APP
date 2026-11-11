package me.pablorincon.proyecto_app.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import me.pablorincon.proyecto_app.domain.TextSize
import me.pablorincon.proyecto_app.domain.ThemeMode
import me.pablorincon.proyecto_app.domain.UserPreferences

// Preferencias de Configuración que cambian cómo se ve y se siente TODA la app.
// Es estado de Compose: al cambiar un valor, todas las pantallas que lo leen se redibujan
// al instante (tamaño del texto en el tema, colores de alto contraste, animaciones, sonido).
object AppAppearance {
    var textSize by mutableStateOf(TextSize.Standard)
    var themeMode by mutableStateOf(ThemeMode.Light)

    // Si el teléfono está en modo oscuro (lo actualiza CodeMentorTheme). Solo cuenta con "Sistema".
    var systemDark by mutableStateOf(false)

    // true = la app se dibuja con los colores oscuros de Color.kt.
    val dark: Boolean
        get() = when (themeMode) {
            ThemeMode.Light -> false
            ThemeMode.Dark -> true
            ThemeMode.System -> systemDark
        }

    var highContrast by mutableStateOf(false)
    var reduceMotion by mutableStateOf(false)
    var soundCues by mutableStateOf(false)
    var vibration by mutableStateOf(true)

    // Multiplicador del texto sobre el tamaño que ya tenga el sistema.
    val textScale: Float
        get() = when (textSize) {
            TextSize.Standard -> 1f
            TextSize.Large -> 1.15f
            TextSize.ExtraLarge -> 1.3f
        }

    fun apply(prefs: UserPreferences) {
        textSize = prefs.textSize
        themeMode = prefs.themeMode
        highContrast = prefs.highContrast
        reduceMotion = prefs.reduceMotion
        soundCues = prefs.soundCues
        vibration = prefs.vibration
    }
}

// Duración de una animación respetando "Reducir animaciones" (0 = sin animación).
fun motionMillis(millis: Int): Int = if (AppAppearance.reduceMotion) 0 else millis
