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
import me.pablorincon.proyecto_app.ui.screens.forgotpassword.ForgotPasswordScreen
import me.pablorincon.proyecto_app.ui.screens.login.LoginScreen
import me.pablorincon.proyecto_app.ui.screens.register.RegisterScreen
import me.pablorincon.proyecto_app.ui.screens.welcome.WelcomeScreen
import me.pablorincon.proyecto_app.ui.theme.CodeMentorTheme

// Navegación temporal mientras se construyen las pantallas;
// en el Paso 6 se reemplaza por Navigation Compose.
private enum class TempScreen { Welcome, Register, Login, ForgotPassword }

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
                        onLogin = { screen = TempScreen.Login }
                    )
                    TempScreen.Register -> {
                        // El botón "atrás" del teléfono vuelve a la Bienvenida
                        BackHandler { screen = TempScreen.Welcome }
                        RegisterScreen(
                            onBack = { screen = TempScreen.Welcome },
                            onLogin = { screen = TempScreen.Login },
                            onRegistered = { screen = TempScreen.Login }
                        )
                    }
                    TempScreen.Login -> {
                        BackHandler { screen = TempScreen.Welcome }
                        LoginScreen(
                            onBack = { screen = TempScreen.Welcome },
                            onCreateAccount = { screen = TempScreen.Register },
                            onForgotPassword = { screen = TempScreen.ForgotPassword },
                            // Debe ir a Personalización o Inicio (Paso 6); por ahora vuelve a la Bienvenida
                            onLoggedIn = { screen = TempScreen.Welcome }
                        )
                    }
                    TempScreen.ForgotPassword -> {
                        BackHandler { screen = TempScreen.Login }
                        ForgotPasswordScreen(
                            onBack = { screen = TempScreen.Login },
                            onSent = { screen = TempScreen.Login }
                        )
                    }
                }
            }
        }
    }
}
