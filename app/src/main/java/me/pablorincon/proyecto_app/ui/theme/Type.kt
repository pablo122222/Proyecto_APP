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
    val ScreenTitleSmall = TextStyle( // .title.sm: 23 px, interlineado 1.16
        fontFamily = InterFamily, fontWeight = FontWeight(850),
        fontSize = 23.sp, lineHeight = 27.sp, letterSpacing = (-0.8).sp
    )
    val SectionTitle = TextStyle( // .section-heading h3
        fontFamily = InterFamily, fontWeight = FontWeight.Bold,
        fontSize = 14.sp
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

    // Barra de pestañas
    val TabLabel = TextStyle(
        fontFamily = InterFamily, fontWeight = FontWeight(650),
        fontSize = 9.sp
    )

    // Filas (.rowline)
    val RowTitle = TextStyle(
        fontFamily = InterFamily, fontWeight = FontWeight.Bold,
        fontSize = 12.sp
    )
    val RowSubtitle = TextStyle(
        fontFamily = InterFamily, fontWeight = FontWeight.Normal,
        fontSize = 10.sp
    )
    val RowIcon = TextStyle( // no está en el mockup: tamaño del emoji de la fila
        fontFamily = InterFamily, fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
    val RowChevron = TextStyle( // "›"
        fontFamily = InterFamily, fontWeight = FontWeight.Normal,
        fontSize = 19.sp
    )

    // Opciones de Personalización (.choice)
    val ChoiceTitle = TextStyle(
        fontFamily = InterFamily, fontWeight = FontWeight(750),
        fontSize = 12.sp
    )
    val ChoiceSubtitle = TextStyle(
        fontFamily = InterFamily, fontWeight = FontWeight(500),
        fontSize = 10.sp
    )
    val PathEmoji = TextStyle( // 🚀 de "Ruta lista"
        fontFamily = InterFamily, fontWeight = FontWeight.Normal,
        fontSize = 42.sp
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

    // ---- Comunes de las pantallas con pestaña (guía §0) ----
    val LinkButton = TextStyle( // .link-btn
        fontFamily = InterFamily, fontWeight = FontWeight(800),
        fontSize = 10.sp
    )
    val StatEmoji = TextStyle(fontFamily = InterFamily, fontSize = 17.sp)
    val StatValue = TextStyle( // Número del .statbox
        fontFamily = InterFamily, fontWeight = FontWeight(800),
        fontSize = 20.sp
    )
    val StatCaption = TextStyle(fontFamily = InterFamily, fontSize = 10.sp, lineHeight = 14.sp)

    // ---- Inicio (guía §2) ----
    val Greeting = TextStyle( // "Hola, {nombre} 👋"
        fontFamily = InterFamily, fontWeight = FontWeight(800),
        fontSize = 22.sp, lineHeight = 26.sp, letterSpacing = (-0.4).sp
    )
    val GreetingSub = TextStyle(fontFamily = InterFamily, fontSize = 11.sp)
    val HeroTitle = TextStyle(
        fontFamily = InterFamily, fontWeight = FontWeight(800),
        fontSize = 19.sp, lineHeight = 23.sp
    )
    val HeroText = TextStyle(fontFamily = InterFamily, fontSize = 11.sp, lineHeight = 16.sp)
    val HeroButton = TextStyle(
        fontFamily = InterFamily, fontWeight = FontWeight(800),
        fontSize = 12.sp
    )
    val RecommendTitle = TextStyle(
        fontFamily = InterFamily, fontWeight = FontWeight.Bold,
        fontSize = 12.sp, lineHeight = 16.sp
    )

    // ---- Listas de ajustes (Perfil y Configuración) ----
    val SubScreenTitle = TextStyle(
        fontFamily = InterFamily, fontWeight = FontWeight.Bold,
        fontSize = 18.sp, lineHeight = 22.sp
    )
    val SettingsHeader = TextStyle(
        fontFamily = InterFamily, fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp
    )
    val SettingsTitle = TextStyle(
        fontFamily = InterFamily, fontWeight = FontWeight.Medium,
        fontSize = 15.sp, lineHeight = 20.sp
    )
    val SettingsSubtitle = TextStyle(fontFamily = InterFamily, fontSize = 12.sp, lineHeight = 16.sp)
    val SettingsValue = TextStyle(fontFamily = InterFamily, fontSize = 13.sp)
    val SettingsNote = TextStyle(fontFamily = InterFamily, fontSize = 11.sp, lineHeight = 16.sp)
}
