package me.pablorincon.proyecto_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import me.pablorincon.proyecto_app.ui.theme.AppDimens
import me.pablorincon.proyecto_app.ui.theme.AppShapes
import me.pablorincon.proyecto_app.ui.theme.AppTextStyles
import me.pablorincon.proyecto_app.ui.theme.FocusHaloColor
import me.pablorincon.proyecto_app.ui.theme.Ink
import me.pablorincon.proyecto_app.ui.theme.InputBorder
import me.pablorincon.proyecto_app.ui.theme.LabelInk
import me.pablorincon.proyecto_app.ui.theme.Paper
import me.pablorincon.proyecto_app.ui.theme.Placeholder
import me.pablorincon.proyecto_app.ui.theme.Primary

// Campo de formulario con etiqueta (.form-field).
// Con foco, el borde se vuelve azul y aparece un halo azul de 3 dp alrededor.
@Composable
fun AppTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    FieldLayout(
        label = label,
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder,
        keyboardOptions = keyboardOptions,
        modifier = modifier
    )
}

// Base compartida por AppTextField y PasswordTextField.
// `trailing` es el contenido a la derecha del input (ej. "Mostrar") y
// `endPadding` el espacio que se le reserva para que el texto no pase por debajo.
@Composable
internal fun FieldLayout(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardOptions: KeyboardOptions,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    endPadding: Dp = AppDimens.InputPadding,
    trailing: (@Composable () -> Unit)? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val focused by interactionSource.collectIsFocusedAsState()

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = AppTextStyles.FieldLabel,
            color = LabelInk,
            modifier = Modifier.padding(start = AppDimens.LabelStart)
        )
        Spacer(Modifier.height(AppDimens.LabelGap))

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = AppTextStyles.FieldInput.copy(color = Ink),
            cursorBrush = SolidColor(Primary),
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation,
            interactionSource = interactionSource,
            modifier = Modifier
                .fillMaxWidth()
                .then(if (focused) Modifier.focusHalo() else Modifier),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(AppShapes.Input)
                        .background(Paper)
                        .border(AppDimens.BorderWidth, if (focused) Primary else InputBorder, AppShapes.Input),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = AppDimens.InputPadding,
                                top = AppDimens.InputPadding,
                                bottom = AppDimens.InputPadding,
                                end = endPadding
                            )
                    ) {
                        if (value.isEmpty()) {
                            Text(text = placeholder, style = AppTextStyles.FieldInput, color = Placeholder)
                        }
                        innerTextField()
                    }
                    if (trailing != null) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = AppDimens.ToggleEnd)
                        ) { trailing() }
                    }
                }
            }
        )
    }
}

// Halo de foco (box-shadow: 0 0 0 3px): un rectángulo redondeado 3 dp más grande
// dibujado por detrás del campo, sin cambiar su tamaño.
private fun Modifier.focusHalo() = drawBehind {
    val halo = AppDimens.FocusHalo.toPx()
    val radius = 12.dp.toPx() + halo
    drawRoundRect(
        color = FocusHaloColor,
        topLeft = Offset(-halo, -halo),
        size = Size(size.width + halo * 2, size.height + halo * 2),
        cornerRadius = CornerRadius(radius, radius)
    )
}
