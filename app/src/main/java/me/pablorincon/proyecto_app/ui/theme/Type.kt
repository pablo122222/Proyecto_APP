package me.pablorincon.proyecto_app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import me.pablorincon.proyecto_app.R

// ---------- Fuente Inter (archivo variable) ----------

// Crea una variante de Inter con el grosor exacto que pide el mockup.
@OptIn(ExperimentalTextApi::class)
private fun interFont(weight: Int) = Font(
    resId = R.font.inter_variable,
    weight = FontWeight(weight),
    variationSettings = FontVariation.Settings(FontVariation.weight(weight))
)

val InterFamily = FontFamily(
    interFont(400),
    interFont(500),
    interFont(600),
    interFont(650),
    interFont(700),
    interFont(750),
    interFont(800),
    interFont(850),
    interFont(900)
)

// ---------- Tipografía base de Material 3 ----------
// Mantiene los tamaños por defecto de Material, pero con Inter como fuente.

private val Baseline = Typography()

val Typography = Typography(
    displayLarge = Baseline.displayLarge.copy(fontFamily = InterFamily),
    displayMedium = Baseline.displayMedium.copy(fontFamily = InterFamily),
    displaySmall = Baseline.displaySmall.copy(fontFamily = InterFamily),
    headlineLarge = Baseline.headlineLarge.copy(fontFamily = InterFamily),
    headlineMedium = Baseline.headlineMedium.copy(fontFamily = InterFamily),
    headlineSmall = Baseline.headlineSmall.copy(fontFamily = InterFamily),
    titleLarge = Baseline.titleLarge.copy(fontFamily = InterFamily),
    titleMedium = Baseline.titleMedium.copy(fontFamily = InterFamily),
    titleSmall = Baseline.titleSmall.copy(fontFamily = InterFamily),
    bodyLarge = Baseline.bodyLarge.copy(fontFamily = InterFamily),
    bodyMedium = Baseline.bodyMedium.copy(fontFamily = InterFamily),
    bodySmall = Baseline.bodySmall.copy(fontFamily = InterFamily),
    labelLarge = Baseline.labelLarge.copy(fontFamily = InterFamily),
    labelMedium = Baseline.labelMedium.copy(fontFamily = InterFamily),
    labelSmall = Baseline.labelSmall.copy(fontFamily = InterFamily)
)

// ---------- Estilos exactos del mockup ----------
// Conversión: 1 px del mockup = 1 sp. Interlineado = tamaño × line-height del CSS.

object AppTextStyles {

    // Títulos
    val WelcomeTitle = TextStyle(
        fontFamily = InterFamily, fontWeight = FontWeight(850),
        fontSize = 29.sp, lineHeight = 34.sp, letterSpacing = (-0.8).sp
    )
    val ScreenTitle = TextStyle(
        fontFamily = InterFamily, fontWeight = FontWeight(850),
        fontSize = 26.sp, lineHeight = 30.sp, letterSpacing = (-0.8).sp
    )
    val Subtitle = TextStyle(
        fontFamily = InterFamily, fontWeight = FontWeight.Normal,
        fontSize = 13.sp, lineHeight = 20.sp
    )
    val Eyebrow = TextStyle( // Se muestra en MAYÚSCULAS
        fontFamily = InterFamily, fontWeight = FontWeight(850),
        fontSize = 10.sp, lineHeight = 14.sp, letterSpacing = 1.2.sp
    )

    // Marca
    val Logo = TextStyle(
        fontFamily = InterFamily, fontWeight = FontWeight(850),
        fontSize = 15.sp, letterSpacing = (-0.4).sp
    )
    val LogoIcon = TextStyle(
        fontFamily = InterFamily, fontWeight = FontWeight(900),
        fontSize = 14.sp
    )

    // Botones y etiquetas
    val Button = TextStyle(
        fontFamily = InterFamily, fontWeight = FontWeight(800),
        fontSize = 13.sp, letterSpacing = 0.05.sp
    )
    val SmallButton = TextStyle(
        fontFamily = InterFamily, fontWeight = FontWeight(800),
        fontSize = 12.sp
    )
    val Pill = TextStyle(
        fontFamily = InterFamily, fontWeight = FontWeight(800),
        fontSize = 10.sp
    )
    val StepLabel = TextStyle(
        fontFamily = InterFamily, fontWeight = FontWeight(700),
        fontSize = 11.sp
    )

    val BackArrow = TextStyle( // "‹" del botón atrás
        fontFamily = InterFamily, fontWeight = FontWeight.Normal,
        fontSize = 21.sp
    )

    // Formularios
    val FieldLabel = TextStyle(
        fontFamily = InterFamily, fontWeight = FontWeight(750),
        fontSize = 11.sp
    )
    val FieldInput = TextStyle(
        fontFamily = InterFamily, fontWeight = FontWeight.Normal,
        fontSize = 13.sp
    )
    val FieldAction = TextStyle( // "Mostrar / Ocultar"
        fontFamily = InterFamily, fontWeight = FontWeight(800),
        fontSize = 11.sp
    )
    val ErrorText = TextStyle(
        fontFamily = InterFamily, fontWeight = FontWeight.Normal,
        fontSize = 11.sp, lineHeight = 15.sp
    )
    val FinePrint = TextStyle(
        fontFamily = InterFamily, fontWeight = FontWeight.Normal,
        fontSize = 10.sp, lineHeight = 15.sp
    )

    // Enlaces inferiores ("¿Ya tienes cuenta? Inicia sesión")
    val FooterText = TextStyle(
        fontFamily = InterFamily, fontWeight = FontWeight.Normal,
        fontSize = 11.sp
    )
    val FooterLink = TextStyle(
        fontFamily = InterFamily, fontWeight = FontWeight(800),
        fontSize = 11.sp
    )

    // Textos pequeños
    val Perk = TextStyle(
        fontFamily = InterFamily, fontWeight = FontWeight.Normal,
        fontSize = 10.sp
    )
    val CheckMini = TextStyle( // ✓ verde de los perks
        fontFamily = InterFamily, fontWeight = FontWeight(900),
        fontSize = 10.sp
    )
    val SmallBody = TextStyle( // Datos de la cuenta demo
        fontFamily = InterFamily, fontWeight = FontWeight.Normal,
        fontSize = 12.sp, lineHeight = 20.sp
    )
    val CardTitleSmall = TextStyle( // "Cuenta de demostración"
        fontFamily = InterFamily, fontWeight = FontWeight.Bold,
        fontSize = 11.sp
    )

    // Código (ilustración de bienvenida)
    val CodeCard = TextStyle(
        fontFamily = FontFamily.Monospace, fontWeight = FontWeight(700),
        fontSize = 10.sp, lineHeight = 14.sp
    )
    val OrbSymbol = TextStyle(
        fontFamily = FontFamily.Monospace, fontWeight = FontWeight(800),
        fontSize = 31.sp
    )
}