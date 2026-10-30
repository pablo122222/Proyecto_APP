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
import androidx.compose.ui.semantics.Role
import me.pablorincon.proyecto_app.ui.theme.AppDimens
import me.pablorincon.proyecto_app.ui.theme.AppTextStyles
import me.pablorincon.proyecto_app.ui.theme.Muted
import me.pablorincon.proyecto_app.ui.theme.Primary

// Enlace centrado de pie de pantalla (.center-link): "¿Ya tienes una cuenta? Inicia sesión".
// Solo la parte azul es pulsable.
@Composable
fun FooterLink(
    text: String,
    linkText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // El espacio final reproduce el espacio que hay en el HTML antes del enlace.
        Text(text = "$text ", style = AppTextStyles.FooterText, color = Muted)
        Text(
            text = linkText,
            style = AppTextStyles.FooterLink,
            color = Primary,
            modifier = Modifier
                .clickable(role = Role.Button, onClick = onClick)
                .padding(start = AppDimens.LinkGap)
        )
    }
}
