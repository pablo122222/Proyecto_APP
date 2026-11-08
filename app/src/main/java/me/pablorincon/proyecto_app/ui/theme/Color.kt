package me.pablorincon.proyecto_app.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Paleta de CodeMentor AI tomada del mockup (prototipo v5).

// ---- Fondos y superficies ----
val AppBackground = Color(0xFFF5F7FC)     // Fondo de todas las pantallas
val Paper = Color(0xFFFFFFFF)             // Tarjetas, campos de texto, botón atrás

// ---- Texto ----
val Ink = Color(0xFF1F2A44)               // Texto principal
val Muted = Color(0xFF66708A)             // Subtítulos y textos secundarios
val LabelInk = Color(0xFF2F3B5C)          // Etiquetas de los campos
val FinePrintText = Color(0xFF8590AB)     // Letra pequeña

// ---- Color primario ----
val Primary = Color(0xFF4B7BE5)           // Botones, enlaces, acentos
val PrimaryDark = Color(0xFF3A63C2)       // Botón presionado, texto sobre azul suave
val PrimarySoft = Color(0xFFE5EDFD)       // Pills, botón secundario, tarjeta demo
val PrimarySoftBorder = Color(0xFFC5D6F7) // Borde de la tarjeta demo

// ---- Bordes ----
val Line = Color(0xFFE4E9F3)              // Bordes generales
val InputBorder = Color(0xFFDDE4F2)       // Borde de los campos de texto
val FocusHaloColor = Color(0x144B7BE5)    // Halo azul del campo con foco
val Placeholder = Color(0xFFA9A9A9)       // El mockup no lo define: gris por defecto de Chrome

// ---- Estados ----
val ErrorRed = Color(0xFFC24A4A)          // Mensajes de error
val Mint = Color(0xFFDDF5EC)              // Fondo de los checks y del degradado
val MintInk = Color(0xFF1F8F6D)           // Check (✓) verde

// ---- Ilustración de bienvenida ----
val AiViolet = Color(0xFF7B6BE0)          // Inicio del degradado del cubo {;}
val AiVioletSoft = Color(0xFFEEEAFD)      // Inicio del degradado del fondo
val AccentYellow = Color(0xFFF4B13E)      // Punto amarillo decorativo
val FloatCardText = Color(0xFF39445D)     // Texto de las tarjetitas flotantes
val FloatCardBorder = Color(0xB8FFFFFF)   // Borde blanco semitransparente de las tarjetitas

// ---- Barra de pestañas inferior ----
val TabBarBackground = Color(0xEDFFFFFF)  // Blanco al 93 % (rgba(255,255,255,.93))
val TabInactive = Color(0xFF8A96B3)       // Icono y texto de las pestañas inactivas

// ---- Filas (.rowline) ----
val RowIconBackground = Color(0xFFE5EDFD) // Fondo del icono de la fila
val Chevron = Color(0xFF9AA5BD)           // Flecha "›" de las filas
val LogoutIconBackground = Color(0xFFFDECEC) // Fondo del icono de "Cerrar sesión"

// ---- Personalización: opciones, pasos e ilustración de "Ruta lista" ----
val ChoiceSelectedBackground = Color(0xFFEEF3FE) // Fondo de la opción elegida (choice.selected)
val ChoiceIconBackground = Color(0xFFEAF0FD)     // Cuadro del emoji de la opción
val RadioBorder = Color(0xFFCBD3E6)              // Círculo del radio sin elegir
val StepInactive = Color(0xFFE1E6F1)             // Barrita de paso sin completar
val PathIllustrationStart = Color(0xFFCFE0FC)    // Degradado del cuadro 🚀 (inicio)
val PathIllustrationEnd = Color(0xFFC9F2E1)      // Degradado del cuadro 🚀 (fin)
// no está en el mockup: botón principal deshabilitado (Primary al 55 % sobre el fondo)
val PrimaryDisabled = Color(0xFF98B3EF)

// ---- Ruta de aprendizaje y barra de progreso (guía §0 y §3) ----
val ProgressTrack = Color(0xFFE4E9F3)         // Pista de la barra de progreso
val ProgressFill = Brush.horizontalGradient(listOf(Color(0xFF5B8DEF), Color(0xFF35C79A)))
val TimelineLine = Color(0xFFD5DDF0)          // Línea punteada vertical
val NodeDone = Color(0xFF2FB38A)
val NodeDoneRing = Color(0xFFCDF2E3)
val NodeCurrentRing = Color(0xFFD3E2FC)
val NodeLockedRing = Color(0xFFD8DFEF)
val CurrentCardBorder = Color(0xFF8FB0F5)
val CurrentCardBackground = Color(0xFFF4F8FF)
val LockedPillBackground = Color(0xFFEEF1F8)
val LockedPillText = Color(0xFF7A86A3)

// ---- Sombras ----
val ButtonShadow = Color(0x264B7BE5)      // Botón primario
val OrbShadow = Color(0x404B7BE5)         // Cubo {;}
val FloatCardShadow = Color(0x18283047)   // Tarjetitas flotantes
val CardShadow = Color(0x081B2540)        // Tarjetas (.card)

// ---- Lección (guía §0 y §4) ----
val CodeBg = Color(0xFF1C2540)
val CodeText = Color(0xFFE9EDFA)
val CodeLineNumber = Color(0xFF7F8AA6)
val SyntaxKeyword = Color(0xFFB3A8FF)
val SyntaxNumber = Color(0xFFFFC46B)
val SyntaxString = Color(0xFF8CE0B7)
val SyntaxFunction = Color(0xFF80C9FF)
val SyntaxComment = Color(0xFF7F8AA6)     // no está en la guía: igual que los números de línea
val OutputBg = Color(0xFFEDFAF4)
val OutputBorder = Color(0xFFCDEEDD)
val OutputText = Color(0xFF1B7A5C)
val AnswerCorrectBorder = Color(0xFF2FB38A)
val AnswerCorrectBg = Color(0xFFEAF9F2)
val AnswerWrongBorder = Color(0xFFF3A9A6)
val AnswerWrongBg = Color(0xFFFEF1F0)
val FeedbackGoodBg = Color(0xFFE4F7EE)
val FeedbackGoodText = Color(0xFF1B7A5C)
val FeedbackBadBg = Color(0xFFFDECEC)
val FeedbackBadText = Color(0xFFA83E3E)
val SoftBlueCardBg = Color(0xFFE5EDFD)
val SoftBlueCardBorder = Color(0xFFD3E2FC)
