package me.pablorincon.proyecto_app.ui.theme

import androidx.compose.ui.unit.dp

// Medidas tomadas del mockup (1 px = 1 dp).

object AppDimens {

    // ---------- Pantalla ----------
    val ScreenHorizontal = 22.dp   // Margen lateral (.app-view)
    val ScreenTop = 10.dp          // Bajo la barra de estado (Registro y Login)
    val ScreenBottom = 98.dp       // Espacio libre al final del scroll (.app-view)
    val WelcomeTop = 20.dp         // Bajo la barra de estado (Bienvenida)
    val BottomFixed = 26.dp        // Distancia de los botones fijos al borde inferior
    val BottomFixedTop = 18.dp     // Relleno superior de la zona fija inferior
    val BottomFade = 20.dp         // Alto del degradado que oculta el contenido al hacer scroll

    // ---------- Botones ----------
    val ButtonHeight = 48.dp
    val SmallButtonHeight = 39.dp
    val ButtonPaddingH = 16.dp
    val ButtonPaddingV = 13.dp
    val ButtonIconGap = 8.dp       // Espacio antes de la flecha →
    val LinkGap = 3.dp             // Espacio antes del enlace de pie
    val SmallButtonPaddingH = 12.dp
    val SmallButtonPaddingV = 10.dp
    val BackButtonSize = 36.dp

    // ---------- Formularios ----------
    val FieldSpacing = 16.dp       // Margen vertical entre campos
    val LabelGap = 7.dp            // Espacio entre label e input
    val LabelStart = 2.dp          // Sangría del label
    val InputPadding = 13.dp
    val BorderWidth = 1.dp
    val FocusHalo = 3.dp           // Halo azul alrededor del campo con foco
    val ErrorToButton = 12.dp      // Del error al botón (.btn.mt12)
    val FinePrintTop = 16.dp       // Del formulario a la letra pequeña (.mt16)
    val ErrorMinHeight = 16.dp     // Espacio reservado para el error
    val PasswordEndPadding = 70.dp // Espacio del input reservado para "Mostrar"
    val ToggleEnd = 6.dp
    val TogglePadding = 8.dp

    // ---------- Encabezado ----------
    val TopLineGap = 12.dp         // Entre botón atrás y etiqueta
    val TopLineBottom = 22.dp
    val EyebrowBottom = 10.dp
    val TitleBottom = 10.dp

    // ---------- Pills y tarjetas ----------
    val PillPaddingH = 9.dp
    val PillPaddingV = 6.dp
    val PillGap = 5.dp
    val CardPadding = 16.dp
    val CardTop = 20.dp            // Del botón a la tarjeta de ejemplo (.mt20)
    val CardTextTop = 8.dp         // Del título de la tarjeta a sus datos (.mt8)
    val CardButtonTop = 12.dp      // De los datos al botón (.mt12)
    val CardShadowRadius = 15.dp   // box-shadow: 0 4px 15px
    val CardShadowOffsetY = 4.dp

    // ---------- Barra de pestañas (.tabbar) ----------
    val TabBarHeight = 73.dp       // height: 73px
    val TabIconBox = 26.dp         // Cuadro del icono: 26×26
    val TabIcon = 19.dp            // Icono de trazo: 19×19
    val TabLabelGap = 3.dp         // no está en el mockup: separación entre icono y texto

    // ---------- Secciones (.section-heading) ----------
    val SectionTop = 22.dp         // margin-top: 22px
    val SectionBottom = 10.dp      // margin-bottom: 10px

    // ---------- Filas (.rowline) ----------
    val RowIconSize = 36.dp        // .row-icon 36×36
    val RowPaddingV = 13.dp        // padding: 13px 0
    val RowSubtitleTop = 4.dp      // margin-top: 4px del subtítulo
    val RowIconGap = 12.dp         // no está en el mockup: separación entre icono y texto

    // ---------- Personalización ----------
    val StepBarWidth = 19.dp       // Barrita del indicador de pasos: 19×4
    val StepBarHeight = 4.dp
    val StepBarGap = 4.dp          // no está en el mockup: separación entre barritas
    val ChoiceListTop = 23.dp      // Del subtítulo a la primera opción
    val ChoiceGap = 9.dp           // Separación entre opciones
    val ChoicePadding = 14.dp
    val ChoiceBorderWidth = 1.5.dp
    val ChoiceIconSize = 34.dp     // Cuadro del emoji 34×34
    val ChoiceIconGap = 12.dp      // no está en el mockup: separación entre emoji y texto
    val ChoiceTextGap = 3.dp       // no está en el mockup: título a subtexto
    val RadioSize = 17.dp
    val RadioDotSize = 9.dp
    val PathIllustrationSize = 90.dp
    val PathIllustrationTop = 23.dp
    val PathIllustrationBottom = 19.dp
    val PathIllustrationShadowRadius = 20.dp // no está en el mockup: "sombra azul suave"
    val PathIllustrationShadowOffsetY = 8.dp
    val PathCardTop = 20.dp        // De los textos a la tarjeta de 3 filas
    val ChoiceErrorBottom = 10.dp  // no está en el mockup: del error al botón fijo

    // ---------- Ruta de aprendizaje ----------
    val ProgressBarHeight = 7.dp
    val TimelineStart = 5.dp       // Margen izquierdo de la lista
    val TimelinePadding = 25.dp    // Relleno izquierdo de la lista
    val TimelineLineX = 10.dp      // Posición de la línea respecto al borde
    val NodeSize = 21.dp
    val NodeBorder = 3.dp
    val ModuleGap = 10.dp          // no está en la guía: separación entre tarjetas
    val ModuleCardPaddingH = 13.dp
    val ModuleCardPaddingTop = 13.dp
    val ModuleCardPaddingBottom = 12.dp
    val CurrentCardBorderWidth = 1.5.dp
    val ProgressTop = 14.dp        // no está en la guía: del subtítulo al progreso general

    // ---------- Login ----------
    // "¿Olvidaste tu contraseña?" no está en el mockup: separación propia
    val ForgotLinkGap = 8.dp

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
    val FloatDotEnd = 42.dp
    val FloatDotTop = 45.dp
    val FloatCardPaddingH = 12.dp
    val FloatCardPaddingV = 10.dp
    val FloatCardInset = 24.dp     // Distancia vertical de las tarjetitas al borde
    val FloatCardOneStart = 13.dp
    val FloatCardTwoEnd = 11.dp
    val FloatCardRotation = 5f     // Grados (la primera gira -5, la segunda +5)
    val CheckMiniSize = 16.dp
    val PerksTop = 18.dp
    val PerksGap = 14.dp
    val PerkIconGap = 4.dp
    val CenterLinkTop = 13.dp

    // ---- Comunes de las pantallas con pestaña ----
    val LinkButtonPadding = 4.dp   // no está en el mockup: área táctil del .link-btn

    // ---- Inicio (guía §2) ----
    val GreetingBottom = 19.dp     // topline de Inicio: margen inferior 19
    val GreetingGap = 10.dp        // no está en el mockup: entre avatar y saludo
    val HeroPadding = 18.dp
    val HeroCircle = 120.dp        // Círculo decorativo blanco 15 %
    val HeroTitleMaxWidth = 205.dp
    val HeroTextMaxWidth = 220.dp
    val HeroBarHeight = 7.dp
    val HeroButtonHeight = 40.dp
    val HeroButtonMaxWidth = 170.dp // La guía dice 140; "Seguir aprendiendo →" no cabe en 140
    val StatGap = 10.dp
    val RecommendIcon = 42.dp
}
