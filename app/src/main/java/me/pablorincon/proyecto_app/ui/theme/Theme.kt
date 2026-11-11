package me.pablorincon.proyecto_app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Density
import androidx.core.view.WindowCompat

// Sin colores dinámicos (Material You): la app se ve igual que el mockup en cualquier teléfono.
// Los colores salen de Color.kt, que ya cambian con el modo oscuro y el alto contraste.
private fun codeMentorColorScheme(dark: Boolean): ColorScheme {
    val base = if (dark) darkColorScheme() else lightColorScheme()
    return base.copy(
        primary = Primary,
        onPrimary = Color.White,
        primaryContainer = PrimarySoft,
        onPrimaryContainer = PrimaryDark,
        secondary = AiViolet,
        onSecondary = Color.White,
        secondaryContainer = AiVioletSoft,
        onSecondaryContainer = Ink,
        tertiary = MintInk,
        onTertiary = Color.White,
        tertiaryContainer = Mint,
        onTertiaryContainer = MintInk,
        background = AppBackground,
        onBackground = Ink,
        surface = Paper,
        onSurface = Ink,
        surfaceVariant = AppBackground,
        onSurfaceVariant = Muted,
        surfaceContainerHigh = Paper,
        surfaceContainerHighest = Paper,
        surfaceContainer = Paper,
        surfaceContainerLow = Paper,
        outline = InputBorder,
        outlineVariant = Line,
        error = ErrorRed,
        onError = Color.White,
        // Evita que Material tiña de azul las superficies elevadas.
        surfaceTint = Color.Transparent
    )
}

@Composable
fun CodeMentorTheme(content: @Composable () -> Unit) {
    // "Sistema" en Configuración sigue el modo del teléfono
    val systemDark = isSystemInDarkTheme()
    if (AppAppearance.systemDark != systemDark) AppAppearance.systemDark = systemDark
    val dark = AppAppearance.dark

    // Íconos de la barra de estado y de navegación: oscuros en modo claro, claros en oscuro
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window ?: return@SideEffect
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !dark
                isAppearanceLightNavigationBars = !dark
            }
        }
    }

    // "Tamaño del texto" de Configuración: escala todo el texto (sp) sobre la escala del sistema
    val density = LocalDensity.current
    val scaled = Density(density.density, density.fontScale * AppAppearance.textScale)
    CompositionLocalProvider(LocalDensity provides scaled) {
        MaterialTheme(
            colorScheme = codeMentorColorScheme(dark),
            typography = Typography,
            shapes = AppMaterialShapes,
            content = content
        )
    }
}
