package me.pablorincon.proyecto_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.dp
import me.pablorincon.proyecto_app.ui.theme.AppDimens
import me.pablorincon.proyecto_app.ui.theme.AppTextStyles
import me.pablorincon.proyecto_app.ui.theme.Ink
import me.pablorincon.proyecto_app.ui.theme.Line
import me.pablorincon.proyecto_app.ui.theme.Muted
import me.pablorincon.proyecto_app.ui.theme.Paper

private val StatShape = RoundedCornerShape(15.dp)

// Caja de estadística (.statbox): emoji 17 sp, número 20 sp peso 800 y leyenda 10 sp.
// Se usan de a dos, una al lado de la otra.
@Composable
fun StatBox(
    emoji: String,
    value: String,
    caption: String,
    modifier: Modifier = Modifier,
    captionColor: Color = Muted
) {
    Column(
        modifier = modifier
            .clip(StatShape)
            .background(Paper)
            .border(AppDimens.BorderWidth, Line, StatShape)
            .clearAndSetSemantics { contentDescription = "$value $caption" }
            .padding(13.dp)
    ) {
        Text(text = emoji, style = AppTextStyles.StatEmoji)
        Spacer(Modifier.height(8.dp))
        Text(text = value, style = AppTextStyles.StatValue, color = Ink)
        Spacer(Modifier.height(2.dp))
        Text(text = caption, style = AppTextStyles.StatCaption, color = captionColor)
    }
}
