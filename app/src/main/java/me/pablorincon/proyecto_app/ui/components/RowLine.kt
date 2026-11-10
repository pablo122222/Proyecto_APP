package me.pablorincon.proyecto_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import me.pablorincon.proyecto_app.R
import me.pablorincon.proyecto_app.ui.theme.AppDimens
import me.pablorincon.proyecto_app.ui.theme.AppShapes
import me.pablorincon.proyecto_app.ui.theme.AppTextStyles
import me.pablorincon.proyecto_app.ui.theme.Chevron
import me.pablorincon.proyecto_app.ui.theme.ErrorRed
import me.pablorincon.proyecto_app.ui.theme.Ink
import me.pablorincon.proyecto_app.ui.theme.Line
import me.pablorincon.proyecto_app.ui.theme.LogoutIconBackground
import me.pablorincon.proyecto_app.ui.theme.Muted
import me.pablorincon.proyecto_app.ui.theme.Primary
import me.pablorincon.proyecto_app.ui.theme.RowIconBackground

// Fila de lista (.rowline): icono 36×36, título, subtítulo y "›" opcional.
// `danger` la pinta en rojo (ej. "Cerrar sesión"). La última fila de una tarjeta
// va con showDivider = false.
@Composable
fun RowLine(
    icon: String,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    showDivider: Boolean = true,
    danger: Boolean = false
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable(role = Role.Button, onClick = onClick) else Modifier)
            .then(if (showDivider) Modifier.bottomDivider() else Modifier)
            .padding(vertical = AppDimens.RowPaddingV),
        horizontalArrangement = Arrangement.spacedBy(AppDimens.RowIconGap),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(AppDimens.RowIconSize)
                .clip(AppShapes.RowIcon)
                .background(if (danger) LogoutIconBackground else RowIconBackground),
            contentAlignment = Alignment.Center
        ) {
            Text(text = icon, style = AppTextStyles.RowIcon, color = if (danger) ErrorRed else Primary, modifier = Modifier.decorative())
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = AppTextStyles.RowTitle, color = if (danger) ErrorRed else Ink)
            Text(
                text = subtitle,
                style = AppTextStyles.RowSubtitle,
                color = Muted,
                modifier = Modifier.padding(top = AppDimens.RowSubtitleTop)
            )
        }
        if (onClick != null) {
            Text(text = stringResource(R.string.chevron_symbol), style = AppTextStyles.RowChevron, color = Chevron, modifier = Modifier.decorative())
        }
    }
}

// Separador inferior de 1 dp (border-bottom: 1px solid Line).
private fun Modifier.bottomDivider() = drawBehind {
    val stroke = AppDimens.BorderWidth.toPx()
    val y = size.height - stroke / 2
    drawLine(Line, Offset(0f, y), Offset(size.width, y), stroke)
}
