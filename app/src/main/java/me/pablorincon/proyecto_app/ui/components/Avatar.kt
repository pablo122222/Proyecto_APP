package me.pablorincon.proyecto_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.pablorincon.proyecto_app.ui.theme.AvatarEnd
import me.pablorincon.proyecto_app.ui.theme.AvatarStart
import me.pablorincon.proyecto_app.ui.theme.InterFamily
import me.pablorincon.proyecto_app.ui.theme.PrimaryDark

// Avatar con iniciales (guía §0): degradado #C9DAFB → #BDEFD9, iniciales azules peso 900.
// Inicio usa 40×40 (esquina 14, 17 sp); Perfil, 76×76 (esquina 25, 30 sp).
@Composable
fun Avatar(
    initials: String,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    corner: Dp = 14.dp,
    fontSize: TextUnit = 17.sp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(corner))
            .background(Brush.linearGradient(listOf(AvatarStart, AvatarEnd))),
        contentAlignment = Alignment.Center
    ) {
        Text(text = initials, style = TextStyle(fontFamily = InterFamily, fontSize = fontSize, fontWeight = FontWeight(900)), color = PrimaryDark)
    }
}
