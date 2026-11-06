package me.pablorincon.proyecto_app.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import me.pablorincon.proyecto_app.ui.theme.AppDimens
import me.pablorincon.proyecto_app.ui.theme.AppTextStyles
import me.pablorincon.proyecto_app.ui.theme.Ink

// Título de sección (.section-heading): 14 sp, margen 22 arriba y 10 abajo.
// El enlace opcional de la derecha se agregará cuando una pantalla lo necesite.
@Composable
fun SectionHeading(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        style = AppTextStyles.SectionTitle,
        color = Ink,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = AppDimens.SectionTop, bottom = AppDimens.SectionBottom)
    )
}
