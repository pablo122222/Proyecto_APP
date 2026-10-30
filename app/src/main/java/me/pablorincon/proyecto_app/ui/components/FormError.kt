package me.pablorincon.proyecto_app.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import me.pablorincon.proyecto_app.ui.theme.AppDimens
import me.pablorincon.proyecto_app.ui.theme.AppTextStyles
import me.pablorincon.proyecto_app.ui.theme.ErrorRed

// Mensaje de error del formulario (.error): siempre reserva 16 dp de alto
// para que el botón de abajo no salte cuando aparece el error.
// La separación con el campo de arriba la pone la pantalla (en el mockup, el
// margen de 6 px se colapsa con los 16 px del campo y queda en 16).
@Composable
fun FormError(message: String?, modifier: Modifier = Modifier) {
    Text(
        text = message.orEmpty(),
        style = AppTextStyles.ErrorText,
        color = ErrorRed,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = AppDimens.ErrorMinHeight)
            // Equivale a role="alert": el lector de pantalla anuncia el error.
            .semantics { liveRegion = LiveRegionMode.Polite }
    )
}
