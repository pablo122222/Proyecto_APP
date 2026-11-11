package me.pablorincon.proyecto_app.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Paleta de CodeMentor AI tomada del mockup (prototipo v5).
// Cada color tiene versión clara y oscura (Configuración > Tema), y los grises de texto y bordes
// cambian además con "Alto contraste". Se leen con get() para que las pantallas se redibujen al
// cambiar cualquiera de las dos opciones.

// Color según el tema: claro u oscuro
private fun pick(light: Long, dark: Long): Color = Color(if (AppAppearance.dark) dark else light)

// Igual, pero con un valor distinto para "Alto contraste"
private fun pick(light: Long, lightHc: Long, dark: Long, darkHc: Long): Color =
    if (AppAppearance.highContrast) pick(lightHc, darkHc) else pick(light, dark)

// ---- Fondos y superficies ----
val AppBackground: Color get() = pick(0xFFF5F7FC, 0xFF0F1524)     // Fondo de todas las pantallas
val Paper: Color get() = pick(0xFFFFFFFF, 0xFF1A2236)             // Tarjetas, campos de texto, botón atrás

// ---- Texto ----
val Ink: Color get() = pick(0xFF1F2A44, 0xFFE6EBF5)               // Texto principal
val Muted: Color get() = pick(0xFF66708A, 0xFF3B4562, 0xFF9AA5BE, 0xFFC9D1E3)          // Subtítulos y textos secundarios
val LabelInk: Color get() = pick(0xFF2F3B5C, 0xFF1F2A44, 0xFFC4CCDD, 0xFFE6EBF5)       // Etiquetas de los campos
val FinePrintText: Color get() = pick(0xFF8590AB, 0xFF4A5470, 0xFF7E89A3, 0xFFB4BDD2)  // Letra pequeña

// ---- Color primario ----
val Primary: Color get() = pick(0xFF4B7BE5, 0xFF5A88EC)           // Botones, enlaces, acentos
val PrimaryDark: Color get() = pick(0xFF3A63C2, 0xFFA9C2F7)       // Botón presionado, texto sobre azul suave
val PrimarySoft: Color get() = pick(0xFFE5EDFD, 0xFF22304F)       // Pills, botón secundario, tarjeta demo
val PrimarySoftBorder: Color get() = pick(0xFFC5D6F7, 0xFF34477A) // Borde de la tarjeta demo

// ---- Bordes ----
val Line: Color get() = pick(0xFFE4E9F3, 0xFF9AA5BD, 0xFF2A3450, 0xFF5B6680)           // Bordes generales
val InputBorder: Color get() = pick(0xFFDDE4F2, 0xFF8590AB, 0xFF34405E, 0xFF6B7690)    // Borde de los campos de texto
val FocusHaloColor: Color get() = pick(0x144B7BE5, 0x335A88EC)    // Halo azul del campo con foco
val Placeholder: Color get() = pick(0xFFA9A9A9, 0xFF5E6680, 0xFF6E7891, 0xFF9AA5BE)    // El mockup no lo define: gris por defecto de Chrome

// ---- Estados ----
val ErrorRed: Color get() = pick(0xFFC24A4A, 0xFFF07B7B)          // Mensajes de error
val Mint: Color get() = pick(0xFFDDF5EC, 0xFF173A31)              // Fondo de los checks y del degradado
val MintInk: Color get() = pick(0xFF1F8F6D, 0xFF4FD1A5)           // Check (✓) verde

// ---- Ilustración de bienvenida ----
val AiViolet = Color(0xFF7B6BE0)                                  // Inicio del degradado del cubo {;}
val AiVioletSoft: Color get() = pick(0xFFEEEAFD, 0xFF2A2650)      // Inicio del degradado del fondo
val AccentYellow = Color(0xFFF4B13E)                              // Punto amarillo decorativo
val FloatCardText: Color get() = pick(0xFF39445D, 0xFFD5DBE8)     // Texto de las tarjetitas flotantes
val FloatCardBorder: Color get() = pick(0xB8FFFFFF, 0x33FFFFFF)   // Borde semitransparente de las tarjetitas

// ---- Barra de pestañas inferior ----
val TabBarBackground: Color get() = pick(0xEDFFFFFF, 0xED1A2236)  // Superficie al 93 % (rgba(255,255,255,.93))
val TabInactive: Color get() = pick(0xFF8A96B3, 0xFF4A5470, 0xFF7E89A3, 0xFFB4BDD2)    // Icono y texto de las pestañas inactivas

// ---- Filas (.rowline) ----
val RowIconBackground: Color get() = pick(0xFFE5EDFD, 0xFF22304F)    // Fondo del icono de la fila
val Chevron: Color get() = pick(0xFF9AA5BD, 0xFF4A5470, 0xFF5E6982, 0xFFB4BDD2)        // Flecha "›" de las filas
val LogoutIconBackground: Color get() = pick(0xFFFDECEC, 0xFF3A1F26) // Fondo del icono de "Cerrar sesión"

// ---- Personalización: opciones, pasos e ilustración de "Ruta lista" ----
val ChoiceSelectedBackground: Color get() = pick(0xFFEEF3FE, 0xFF22304F) // Fondo de la opción elegida (choice.selected)
val ChoiceIconBackground: Color get() = pick(0xFFEAF0FD, 0xFF26324D)     // Cuadro del emoji de la opción
val RadioBorder: Color get() = pick(0xFFCBD3E6, 0xFF66708A, 0xFF4A5672, 0xFF9AA5BE)    // Círculo del radio sin elegir
val StepInactive: Color get() = pick(0xFFE1E6F1, 0xFF2A3450)             // Barrita de paso sin completar
val PathIllustrationStart: Color get() = pick(0xFFCFE0FC, 0xFF22355E)    // Degradado del cuadro 🚀 (inicio)
val PathIllustrationEnd: Color get() = pick(0xFFC9F2E1, 0xFF1B4A3C)      // Degradado del cuadro 🚀 (fin)
// no está en el mockup: botón principal deshabilitado (Primary al 55 % sobre el fondo)
val PrimaryDisabled: Color get() = pick(0xFF98B3EF, 0xFF33456F)

// ---- Ruta de aprendizaje y barra de progreso (guía §0 y §3) ----
val ProgressTrack: Color get() = pick(0xFFE4E9F3, 0xFF2A3450)         // Pista de la barra de progreso
val ProgressFill = Brush.horizontalGradient(listOf(Color(0xFF5B8DEF), Color(0xFF35C79A)))
val TimelineLine: Color get() = pick(0xFFD5DDF0, 0xFF2E3854)          // Línea punteada vertical
val NodeDone = Color(0xFF2FB38A)
val NodeDoneRing: Color get() = pick(0xFFCDF2E3, 0xFF1B4A3C)
val NodeCurrentRing: Color get() = pick(0xFFD3E2FC, 0xFF26365E)
val NodeLockedRing: Color get() = pick(0xFFD8DFEF, 0xFF2A3450)
val CurrentCardBorder: Color get() = pick(0xFF8FB0F5, 0xFF4B6FBF)
val CurrentCardBackground: Color get() = pick(0xFFF4F8FF, 0xFF1C2742)
val LockedPillBackground: Color get() = pick(0xFFEEF1F8, 0xFF242D45)
val LockedPillText: Color get() = pick(0xFF7A86A3, 0xFF3B4562, 0xFF8A95AF, 0xFFC9D1E3)

// ---- Sombras ----
val ButtonShadow: Color get() = pick(0x264B7BE5, 0x40000000)      // Botón primario
val OrbShadow: Color get() = pick(0x404B7BE5, 0x55000000)         // Cubo {;}
val FloatCardShadow: Color get() = pick(0x18283047, 0x40000000)   // Tarjetitas flotantes
val CardShadow: Color get() = pick(0x081B2540, 0x30000000)        // Tarjetas (.card)

// ---- Lección (guía §0 y §4) ----
// El bloque de código ya es oscuro; en modo oscuro se hunde un poco para separarse de la tarjeta.
val CodeBg: Color get() = pick(0xFF1C2540, 0xFF0B1020)
val CodeText = Color(0xFFE9EDFA)
val CodeLineNumber: Color get() = if (AppAppearance.highContrast) Color(0xFFB8C1D9) else Color(0xFF7F8AA6)
val SyntaxKeyword = Color(0xFFB3A8FF)
val SyntaxNumber = Color(0xFFFFC46B)
val SyntaxString = Color(0xFF8CE0B7)
val SyntaxFunction = Color(0xFF80C9FF)
val SyntaxComment: Color get() = if (AppAppearance.highContrast) Color(0xFFB8C1D9) else Color(0xFF7F8AA6)     // no está en la guía: igual que los números de línea
val OutputBg: Color get() = pick(0xFFEDFAF4, 0xFF13342A)
val OutputBorder: Color get() = pick(0xFFCDEEDD, 0xFF1E5243)
val OutputText: Color get() = pick(0xFF1B7A5C, 0xFF6FDDB4)
val AnswerCorrectBorder = Color(0xFF2FB38A)
val AnswerCorrectBg: Color get() = pick(0xFFEAF9F2, 0xFF15362C)
val AnswerWrongBorder: Color get() = pick(0xFFF3A9A6, 0xFFB35A57)
val AnswerWrongBg: Color get() = pick(0xFFFEF1F0, 0xFF3A1F24)
val FeedbackGoodBg: Color get() = pick(0xFFE4F7EE, 0xFF15362C)
val FeedbackGoodText: Color get() = pick(0xFF1B7A5C, 0xFF6FDDB4)
val FeedbackBadBg: Color get() = pick(0xFFFDECEC, 0xFF3A1F24)
val FeedbackBadText: Color get() = pick(0xFFA83E3E, 0xFFF5A3A0)
val SoftBlueCardBg: Color get() = pick(0xFFE5EDFD, 0xFF22304F)
val SoftBlueCardBorder: Color get() = pick(0xFFD3E2FC, 0xFF2E4170)


// ---- Camino de la ruta (nodos de lecciones y jefes, guía §0 y §3) ----
val NodeDoneShadow = Color(0xFF229A77)
val NodeLockedFill: Color get() = pick(0xFFDDE4F2, 0xFF2A3450)
val NodeLockedShadow: Color get() = pick(0xFFC2CCE0, 0xFF1A2032)
val NodeLockedText: Color get() = pick(0xFF8A96B3, 0xFF3B4562, 0xFF7E89A3, 0xFFC9D1E3)
val HeroStart = Color(0xFF3F5FD8)         // Degradado del nivel en curso
val HeroEnd = Color(0xFF6A5CE6)

// ---- Tutor IA (guía §9) ----
val TutorHeaderText = Color(0xFFDCE7FF)       // Texto de la cabecera
val TutorHeaderIconBg = Color(0x21FFFFFF)     // Cuadro del ✦: blanco al 13 %
val TutorUserBubbleBg: Color get() = pick(0xFFE1EAFD, 0xFF263A6B)
val TutorUserBubbleText: Color get() = pick(0xFF2F4CA8, 0xFFD3E0FF)
val TutorChipBorder: Color get() = pick(0xFFCFDCF8, 0xFF34477A)
val TutorChipBg: Color get() = pick(0xFFF5F8FF, 0xFF1C2742)

// ---- Comunes (guía §0): avatar y pills de color ----
val AvatarStart: Color get() = pick(0xFFC9DAFB, 0xFF2F4A80)
val AvatarEnd: Color get() = pick(0xFFBDEFD9, 0xFF1F5C4A)
val Yellow: Color get() = pick(0xFFFDF0DA, 0xFF3D3020)               // Pill amarillo (racha)
val YellowInk: Color get() = pick(0xFFB9731A, 0xFFF4B13E)

// ---- Inicio (guía §2) ----
val HeroEyebrow = Color(0xFFCBDAFF)
val HeroText = Color(0xFFDCE4FA)
val HeroCircle = Color(0x26FFFFFF)          // Blanco al 15 %
val HeroTrack = Color(0x30FFFFFF)           // Blanco al 19 %
val HeroFill = Color(0xFF6FDDB4)

// ---- Glitch City (guía §5.1) ----
val CityNodeDone = Color(0xFF35C79A)        // Nivel del dron completado (sombra: NodeDoneShadow)

// ---- Listas de ajustes (Perfil y Configuración, estilo de lista a ancho completo) ----
val SettingsBand: Color get() = pick(0xFFEEF1F7, 0xFFC9D0E0, 0xFF080C16, 0xFF3A4560) // Franja entre secciones
val SwitchOff: Color get() = pick(0xFFD5DCEC, 0xFF8A96B3, 0xFF3A4560, 0xFF6B7690)    // Interruptor apagado (guía §8)

// ---- Logros: medallas por nivel (bronce, plata, oro y sin ganar) ----
val TierBronze = Color(0xFFC0773A)
val TierSilver = Color(0xFF8C98AE)
val TierGold = Color(0xFFE0A21B)
val TierNone: Color get() = pick(0xFFCBD3E6, 0xFF3A4560)
val TierBronzeBg: Color get() = pick(0xFFFBE9DA, 0xFF3A2A1E)
val TierSilverBg: Color get() = pick(0xFFEDF0F5, 0xFF2A3245)
val TierGoldBg: Color get() = pick(0xFFFDF0D2, 0xFF3D3020)       // Fondo de la medalla de la guía §6
val TierNoneBg: Color get() = pick(0xFFF1F3F8, 0xFF1E2639)
