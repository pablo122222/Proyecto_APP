package me.pablorincon.proyecto_app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import me.pablorincon.proyecto_app.ui.theme.AppDimens
import me.pablorincon.proyecto_app.ui.theme.AppShapes
import me.pablorincon.proyecto_app.ui.theme.AppTextStyles
import me.pablorincon.proyecto_app.ui.theme.Ink
import me.pablorincon.proyecto_app.ui.theme.Primary

// Título de sección (.section-heading): 14 sp, margen 22 arriba y 10 abajo, con un
// enlace opcional a la derecha (.link-btn: 10 sp, azul, peso 800).
@Composable
fun SectionHeading(
    title: String,
    modifier: Modifier = Modifier,
    linkText: String? = null,
    onLinkClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = AppDimens.SectionTop, bottom = AppDimens.SectionBottom),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, style = AppTextStyles.SectionTitle, color = Ink, modifier = Modifier.weight(1f))
        if (linkText != null && onLinkClick != null) {
            Text(
                text = linkText,
                style = AppTextStyles.LinkButton,
                color = Primary,
                modifier = Modifier
                    .clip(AppShapes.SmallButton)
                    .clickable(role = Role.Button, onClick = onLinkClick)
                    .padding(AppDimens.LinkButtonPadding)
            )
        }
    }
}
