package me.pablorincon.proyecto_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import me.pablorincon.proyecto_app.ui.theme.AppDimens
import me.pablorincon.proyecto_app.ui.theme.AppShapes
import me.pablorincon.proyecto_app.ui.theme.AppTextStyles
import me.pablorincon.proyecto_app.ui.theme.ButtonShadow
import me.pablorincon.proyecto_app.ui.theme.Primary
import me.pablorincon.proyecto_app.ui.theme.PrimaryDark

// Botón azul principal (.btn del mockup): ancho completo, alto 48, sombra azul suave.
// Al presionarlo se oscurece a PrimaryDark, como el :hover/:active del mockup.
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    trailingSymbol: String? = null // Ej. "→" en "Crear mi cuenta →"
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = AppDimens.ButtonHeight)
            .dropShadow(
                shape = AppShapes.Button,
                shadow = Shadow(radius = 16.dp, color = ButtonShadow, offset = DpOffset(0.dp, 7.dp))
            )
            .clip(AppShapes.Button)
            .background(if (pressed) PrimaryDark else Primary)
            .clickable(
                interactionSource = interactionSource,
                indication = null, // El mockup no tiene efecto ripple
                role = Role.Button,
                onClick = onClick
            )
            .padding(horizontal = AppDimens.ButtonPaddingH, vertical = AppDimens.ButtonPaddingV),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = text, style = AppTextStyles.Button, color = Color.White)
        if (trailingSymbol != null) {
            Spacer(Modifier.width(AppDimens.ButtonIconGap))
            Text(text = trailingSymbol, style = AppTextStyles.Button, color = Color.White)
        }
    }
}
