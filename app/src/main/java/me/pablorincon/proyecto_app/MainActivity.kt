package me.pablorincon.proyecto_app

import android.graphics.Color as AndroidColor
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.saveable.rememberSaveable
import me.pablorincon.proyecto_app.data.AuthRepository
import me.pablorincon.proyecto_app.ui.navigation.AppNavHost
import me.pablorincon.proyecto_app.ui.navigation.AppRoutes
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
                // Se decide una sola vez: al girar el teléfono el grafo conserva su historial.
                val startDestination = rememberSaveable {
                    AppRoutes.startDestination(isLoggedIn = AuthRepository().isLoggedIn())
                }
                AppNavHost(startDestination = startDestination)
            }
        }
    }
}
