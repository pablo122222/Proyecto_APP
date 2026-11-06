package me.pablorincon.proyecto_app

import android.graphics.Color as AndroidColor
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import me.pablorincon.proyecto_app.ui.navigation.AppNavHost
import me.pablorincon.proyecto_app.ui.screens.startup.StartupScreen
import me.pablorincon.proyecto_app.ui.screens.startup.StartupUiState
import me.pablorincon.proyecto_app.ui.screens.startup.StartupViewModel
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
                // El ViewModel decide una sola vez (sobrevive al giro del teléfono);
                // una vez creado, el grafo conserva su propio historial.
                val startup: StartupViewModel = viewModel()
                val state by startup.uiState.collectAsState()
                when (val current = state) {
                    is StartupUiState.Ready -> AppNavHost(startDestination = current.startDestination)
                    StartupUiState.Loading -> StartupScreen(isError = false, onRetry = startup::load)
                    StartupUiState.Error -> StartupScreen(isError = true, onRetry = startup::load)
                }
            }
        }
    }
}
