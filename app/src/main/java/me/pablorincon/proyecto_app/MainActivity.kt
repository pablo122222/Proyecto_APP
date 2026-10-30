package me.pablorincon.proyecto_app

import android.graphics.Color as AndroidColor
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import me.pablorincon.proyecto_app.ui.screens.register.RegisterScreen
import me.pablorincon.proyecto_app.ui.screens.welcome.WelcomeScreen
import me.pablorincon.proyecto_app.ui.theme.CodeMentorTheme

// Navegación temporal mientras se construyen las pantallas;
// en el Paso 6 se reemplaza por Navigation Compose.
private enum class TempScreen { Welcome, Register }

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
                var screen by rememberSaveable { mutableStateOf(TempScreen.Welcome) }

                when (screen) {
                    TempScreen.Welcome -> WelcomeScreen(
                        onCreateAccount = { screen = TempScreen.Register },
                        onLogin = {} // Iniciar sesión llega en el Paso 5
                    )
                    TempScreen.Register -> {
                        // El botón "atrás" del teléfono vuelve a la Bienvenida
                        BackHandler { screen = TempScreen.Welcome }
                        RegisterScreen(
                            onBack = { screen = TempScreen.Welcome },
                            onLogin = {},       // Paso 5
                            // Debe ir a Iniciar sesión; mientras se construye (Paso 5), vuelve a la Bienvenida
                            onRegistered = { screen = TempScreen.Welcome }
                        )
                    }
                }
            }
        }
    }
}
