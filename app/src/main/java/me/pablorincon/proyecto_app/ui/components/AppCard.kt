package me.pablorincon.proyecto_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import me.pablorincon.proyecto_app.ui.theme.AppDimens
import me.pablorincon.proyecto_app.ui.theme.AppShapes
import me.pablorincon.proyecto_app.ui.theme.CardShadow
import me.pablorincon.proyecto_app.ui.theme.Line
import me.pablorincon.proyecto_app.ui.theme.Paper

// Tarjeta común (.card): fondo blanco, borde gris, esquina 18, padding 16 y sombra suave.
@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .dropShadow(
                shape = AppShapes.Card,
                shadow = Shadow(
                    radius = AppDimens.CardShadowRadius,
                    color = CardShadow,
                    offset = DpOffset(0.dp, AppDimens.CardShadowOffsetY)
                )
            )
            .clip(AppShapes.Card)
            .background(Paper)
            .border(AppDimens.BorderWidth, Line, AppShapes.Card)
            .padding(AppDimens.CardPadding),
        content = content
    )
}
