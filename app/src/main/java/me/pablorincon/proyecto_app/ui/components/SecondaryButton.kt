package me.pablorincon.proyecto_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
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
import me.pablorincon.proyecto_app.ui.theme.Primary
import me.pablorincon.proyecto_app.ui.theme.PrimarySoft

// Botón secundario pequeño (.btn.secondary.sm): fondo azul suave, texto azul, alto 39, sin sombra.
// Ej. "Usar cuenta de prueba" en el login.
@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = AppDimens.SmallButtonHeight)
            .clip(AppShapes.SmallButton)
            .background(PrimarySoft)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = AppDimens.SmallButtonPaddingH, vertical = AppDimens.SmallButtonPaddingV),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, style = AppTextStyles.SmallButton, color = Primary)
    }
}
