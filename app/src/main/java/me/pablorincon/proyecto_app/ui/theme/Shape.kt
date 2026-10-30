package me.pablorincon.proyecto_app.ui.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// Radios de esquina tomados del mockup (1 px = 1 dp).

object AppShapes {

    // Botones y campos
    val Button = RoundedCornerShape(14.dp)        // .btn
    val SmallButton = RoundedCornerShape(12.dp)   // .btn.sm
    val Input = RoundedCornerShape(12.dp)         // .form-field input
    val BackButton = RoundedCornerShape(12.dp)    // .back-btn

    // Contenedores
    val Card = RoundedCornerShape(18.dp)          // .card (tarjeta demo del login)
    val Pill = CircleShape                        // .pill (radio 99 px = cápsula)

    // Marca
    val LogoIcon = RoundedCornerShape(10.dp)      // .logo-icon

    // Ilustración de bienvenida
    val Illustration = RoundedCornerShape(26.dp)  // .welcome-art
    val Orb = RoundedCornerShape(36.dp)           // .art-orb
    val FloatCard = RoundedCornerShape(13.dp)     // .float-card
    val CheckMini = RoundedCornerShape(5.dp)      // .check-mini
}

// Formas por defecto de Material 3, alineadas con las del mockup.
val AppMaterialShapes = Shapes(
    small = AppShapes.Input,
    medium = AppShapes.Button,
    large = AppShapes.Card
)
