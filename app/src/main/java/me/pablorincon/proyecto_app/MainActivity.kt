package me.pablorincon.proyecto_app

import android.graphics.Color as AndroidColor
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import me.pablorincon.proyecto_app.ui.screens.welcome.WelcomeScreen
import me.pablorincon.proyecto_app.ui.theme.CodeMentorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Barras del sistema transparentes con íconos oscuros,
        // incluso si el teléfono está en modo oscuro.
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(AndroidColor.TRANSPARENT, AndroidColor.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(AndroidColor.TRANSPARENT, AndroidColor.TRANSPARENT)
        )
        super.onCreate(savedInstanceState)
        setContent {
            CodeMentorTheme {
                // Los botones aún no navegan: Crear cuenta e Iniciar sesión
                // se construyen en los pasos 3 y 5, y la navegación en el paso 6.
                WelcomeScreen(onCreateAccount = {}, onLogin = {})
            }
        }
    }
}
