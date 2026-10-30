package me.pablorincon.proyecto_app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import me.pablorincon.proyecto_app.ui.theme.AppDimens
import me.pablorincon.proyecto_app.ui.theme.AppTextStyles
import me.pablorincon.proyecto_app.ui.theme.Ink
import me.pablorincon.proyecto_app.ui.theme.Muted
import me.pablorincon.proyecto_app.ui.theme.Primary

// Encabezado de Registro y Login:
// fila superior (.topline) con botón atrás y etiqueta a la derecha,
// luego eyebrow en mayúsculas, título grande y subtítulo gris.
@Composable
fun ScreenHeader(
    stepLabel: String,
    eyebrow: String,
    title: String,
    subtitle: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppDimens.TopLineGap),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackButton(onClick = onBack)
            Spacer(Modifier.weight(1f))
            Text(text = stepLabel, style = AppTextStyles.StepLabel, color = Muted)
        }
        Spacer(Modifier.height(AppDimens.TopLineBottom))

        Text(text = eyebrow.uppercase(), style = AppTextStyles.Eyebrow, color = Primary)
        Spacer(Modifier.height(AppDimens.EyebrowBottom))

        Text(text = title, style = AppTextStyles.ScreenTitle, color = Ink)
        Spacer(Modifier.height(AppDimens.TitleBottom))

        Text(text = subtitle, style = AppTextStyles.Subtitle, color = Muted)
    }
}
