package me.pablorincon.proyecto_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import me.pablorincon.proyecto_app.ui.theme.AppDimens
import me.pablorincon.proyecto_app.ui.theme.AppShapes
import me.pablorincon.proyecto_app.ui.theme.AppTextStyles
import me.pablorincon.proyecto_app.ui.theme.Primary
import me.pablorincon.proyecto_app.ui.theme.PrimarySoft

// Etiqueta en forma de cápsula (.pill del mockup): fondo azul suave y texto azul.
@Composable
fun Pill(
    text: String,
    modifier: Modifier = Modifier,
    containerColor: Color = PrimarySoft, // Azul suave; "mint" para estados positivos
    contentColor: Color = Primary
) {
    Text(
        text = text,
        style = AppTextStyles.Pill,
        color = contentColor,
        maxLines = 1,
        modifier = modifier
            .clip(AppShapes.Pill)
            .background(containerColor)
            .padding(horizontal = AppDimens.PillPaddingH, vertical = AppDimens.PillPaddingV)
    )
}
