package me.pablorincon.proyecto_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import me.pablorincon.proyecto_app.R
import me.pablorincon.proyecto_app.ui.theme.AppDimens
import me.pablorincon.proyecto_app.ui.theme.AppShapes
import me.pablorincon.proyecto_app.ui.theme.AppTextStyles
import me.pablorincon.proyecto_app.ui.theme.Ink
import me.pablorincon.proyecto_app.ui.theme.Line
import me.pablorincon.proyecto_app.ui.theme.Paper

// Botón cuadrado para volver (.back-btn): 36×36, fondo blanco, borde gris y "‹".
@Composable
fun BackButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    val description = stringResource(R.string.back)
    Box(
        modifier = modifier
            .size(AppDimens.BackButtonSize)
            .clip(AppShapes.BackButton)
            .background(Paper)
            .border(AppDimens.BorderWidth, Line, AppShapes.BackButton)
            .clickable(onClick = onClick)
            // Los lectores de pantalla dicen "Volver, botón" en vez de leer el símbolo.
            .clearAndSetSemantics {
                contentDescription = description
                role = Role.Button
            },
        contentAlignment = Alignment.Center
    ) {
        Text(text = stringResource(R.string.back_symbol), style = AppTextStyles.BackArrow, color = Ink)
    }
}
