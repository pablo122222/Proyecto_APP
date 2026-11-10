package me.pablorincon.proyecto_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import me.pablorincon.proyecto_app.ui.theme.AppDimens
import me.pablorincon.proyecto_app.ui.theme.AppShapes
import me.pablorincon.proyecto_app.ui.theme.AppTextStyles
import me.pablorincon.proyecto_app.ui.theme.ChoiceIconBackground
import me.pablorincon.proyecto_app.ui.theme.ChoiceSelectedBackground
import me.pablorincon.proyecto_app.ui.theme.CodeMentorTheme
import me.pablorincon.proyecto_app.ui.theme.Ink
import me.pablorincon.proyecto_app.ui.theme.Line
import me.pablorincon.proyecto_app.ui.theme.Muted
import me.pablorincon.proyecto_app.ui.theme.Paper
import me.pablorincon.proyecto_app.ui.theme.Primary
import me.pablorincon.proyecto_app.ui.theme.RadioBorder

// Opción de respuesta única (.choice): emoji, título, subtexto y radio a la derecha.
// Se usa dentro de una columna con selectableGroup() para que TalkBack
// anuncie "elegido / no elegido" y la posición dentro del grupo.
@Composable
fun ChoiceOption(
    icon: String,
    title: String,
    subtitle: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(AppShapes.Choice)
            .background(if (selected) ChoiceSelectedBackground else Paper)
            .border(AppDimens.ChoiceBorderWidth, if (selected) Primary else Line, AppShapes.Choice)
            .selectable(
                selected = selected,
                interactionSource = remember { MutableInteractionSource() },
                indication = null, // El mockup no tiene efecto ripple
                role = Role.RadioButton,
                onClick = onClick
            )
            .padding(AppDimens.ChoicePadding),
        horizontalArrangement = Arrangement.spacedBy(AppDimens.ChoiceIconGap),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(AppDimens.ChoiceIconSize)
                .clip(AppShapes.ChoiceIcon)
                .background(ChoiceIconBackground),
            contentAlignment = Alignment.Center
        ) {
            Text(text = icon, style = AppTextStyles.RowIcon, modifier = Modifier.decorative())
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = AppTextStyles.ChoiceTitle, color = Ink)
            Spacer(Modifier.height(AppDimens.ChoiceTextGap))
            Text(text = subtitle, style = AppTextStyles.ChoiceSubtitle, color = Muted)
        }
        Radio(selected = selected)
    }
}

// Círculo de 17 dp; al elegirlo, borde azul y punto interior de 9 dp.
@Composable
private fun Radio(selected: Boolean) {
    Box(
        modifier = Modifier
            .size(AppDimens.RadioSize)
            .border(AppDimens.ChoiceBorderWidth, if (selected) Primary else RadioBorder, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (selected) {
            Box(
                modifier = Modifier
                    .size(AppDimens.RadioDotSize)
                    .clip(CircleShape)
                    .background(Primary)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChoiceOptionPreview() {
    CodeMentorTheme {
        Column(
            modifier = Modifier.padding(AppDimens.ScreenHorizontal),
            verticalArrangement = Arrangement.spacedBy(AppDimens.ChoiceGap)
        ) {
            ChoiceOption("🌱", "Estoy empezando", "Quiero aprender desde cero.", selected = true, onClick = {})
            ChoiceOption("🧩", "Conozco lo básico", "He visto variables y condiciones.", selected = false, onClick = {})
        }
    }
}
