package me.pablorincon.proyecto_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.tooling.preview.Preview
import me.pablorincon.proyecto_app.R
import me.pablorincon.proyecto_app.ui.theme.AppDimens
import me.pablorincon.proyecto_app.ui.theme.AppTextStyles
import me.pablorincon.proyecto_app.ui.theme.CodeMentorTheme
import me.pablorincon.proyecto_app.ui.theme.Ink
import me.pablorincon.proyecto_app.ui.theme.Muted
import me.pablorincon.proyecto_app.ui.theme.Primary
import me.pablorincon.proyecto_app.ui.theme.StepInactive

// Encabezado de los pasos de Personalización: botón atrás, barritas de avance y "1 de 2",
// seguido de eyebrow, título y subtítulo. Con onBack = null no hay pantalla anterior:
// se deja el hueco del botón para que el resto no se desplace.
@Composable
fun StepHeader(
    step: Int,
    totalSteps: Int,
    eyebrow: String,
    title: String,
    subtitle: String,
    onBack: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppDimens.TopLineGap),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (onBack != null) {
                BackButton(onClick = onBack)
            } else {
                Spacer(Modifier.size(AppDimens.BackButtonSize))
            }
            StepIndicator(step = step, totalSteps = totalSteps)
            Spacer(Modifier.weight(1f))
            Text(
                text = stringResource(R.string.step_of, step, totalSteps),
                style = AppTextStyles.StepLabel,
                color = Muted
            )
        }
        Spacer(Modifier.height(AppDimens.TopLineBottom))

        Text(text = eyebrow.uppercase(), style = AppTextStyles.Eyebrow, color = Primary)
        Spacer(Modifier.height(AppDimens.EyebrowBottom))

        Text(text = title, style = AppTextStyles.ScreenTitle, color = Ink)
        Spacer(Modifier.height(AppDimens.TitleBottom))

        Text(text = subtitle, style = AppTextStyles.Subtitle, color = Muted)
    }
}

// Barritas de 19×4: las del paso actual y anteriores en azul, el resto en gris.
// Solo decoran: "1 de 2" ya lo lee el lector de pantalla.
@Composable
private fun StepIndicator(step: Int, totalSteps: Int) {
    Row(
        modifier = Modifier.clearAndSetSemantics { },
        horizontalArrangement = Arrangement.spacedBy(AppDimens.StepBarGap)
    ) {
        repeat(totalSteps) { index ->
            Spacer(
                Modifier
                    .width(AppDimens.StepBarWidth)
                    .height(AppDimens.StepBarHeight)
                    .clip(CircleShape)
                    .background(if (index < step) Primary else StepInactive)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StepHeaderPreview() {
    CodeMentorTheme {
        StepHeader(
            step = 1, totalSteps = 2,
            eyebrow = "Personalicemos tu ruta",
            title = "¿Qué tanto sabes de programación?",
            subtitle = "No hay respuestas buenas o malas.",
            onBack = null
        )
    }
}
