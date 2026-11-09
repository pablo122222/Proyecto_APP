package me.pablorincon.proyecto_app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.graphics.Color

// Solo modo claro y sin colores dinámicos (Material You):
// la app se ve igual que el mockup en cualquier teléfono.
private val CodeMentorColorScheme = lightColorScheme(
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
    outline = InputBorder,
    outlineVariant = Line,
    error = ErrorRed,
    onError = Color.White,
    // Evita que Material tiña de azul las superficies elevadas.
    surfaceTint = Color.Transparent
)

@Composable
fun CodeMentorTheme(content: @Composable () -> Unit) {
    // "Tamaño del texto" de Configuración: escala todo el texto (sp) sobre la escala del sistema
    val density = LocalDensity.current
    val scaled = Density(density.density, density.fontScale * AppAppearance.textScale)
    CompositionLocalProvider(LocalDensity provides scaled) {
        MaterialTheme(
            colorScheme = CodeMentorColorScheme,
            typography = Typography,
            shapes = AppMaterialShapes,
            content = content
        )
    }
}
