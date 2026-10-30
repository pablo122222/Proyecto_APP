package me.pablorincon.proyecto_app.ui.theme

import androidx.compose.ui.unit.dp

// Medidas tomadas del mockup (1 px = 1 dp).

object AppDimens {

    // ---------- Pantalla ----------
    val ScreenHorizontal = 22.dp   // Margen lateral (.app-view)
    val ScreenTop = 10.dp          // Bajo la barra de estado (Registro y Login)
    val WelcomeTop = 20.dp         // Bajo la barra de estado (Bienvenida)
    val BottomFixed = 26.dp        // Distancia de los botones fijos al borde inferior

    // ---------- Botones ----------
    val ButtonHeight = 48.dp
    val SmallButtonHeight = 39.dp
    val ButtonPaddingH = 16.dp
    val SmallButtonPaddingH = 12.dp
    val BackButtonSize = 36.dp

    // ---------- Formularios ----------
    val FieldSpacing = 16.dp       // Margen vertical entre campos
    val LabelGap = 7.dp            // Espacio entre label e input
    val LabelStart = 2.dp          // Sangría del label
    val InputPadding = 13.dp
    val BorderWidth = 1.dp
    val FocusHalo = 3.dp           // Halo azul alrededor del campo con foco
    val ErrorTop = 6.dp
    val ErrorMinHeight = 16.dp     // Espacio reservado para el error

    // ---------- Encabezado ----------
    val EyebrowBottom = 10.dp
    val TitleBottom = 10.dp

    // ---------- Pills y tarjetas ----------
    val PillPaddingH = 9.dp
    val PillPaddingV = 6.dp
    val PillGap = 5.dp
    val CardPadding = 16.dp

    // ---------- Marca ----------
    val LogoIconSize = 30.dp
    val LogoGap = 9.dp

    // ---------- Bienvenida ----------
    val IllustrationHeight = 225.dp
    val IllustrationVertical = 25.dp
    val IllustrationOutset = 2.dp  // Margen negativo lateral (-2 px)
    val OrbSize = 124.dp
    val OrbRotation = -8f          // Grados
    val FloatDotSize = 11.dp
    val CheckMiniSize = 16.dp
    val PerksTop = 18.dp
    val PerksGap = 14.dp
    val PerkIconGap = 4.dp
    val CenterLinkTop = 13.dp
}
