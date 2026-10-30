package me.pablorincon.proyecto_app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import me.pablorincon.proyecto_app.R
import me.pablorincon.proyecto_app.ui.theme.AppDimens
import me.pablorincon.proyecto_app.ui.theme.AppTextStyles
import me.pablorincon.proyecto_app.ui.theme.Primary

// Campo de contraseña (.pw-wrap): igual que AppTextField, con "Mostrar / Ocultar"
// a la derecha para ver u ocultar lo escrito.
@Composable
fun PasswordTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
) {
    var visible by rememberSaveable { mutableStateOf(false) }

    FieldLayout(
        label = label,
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder,
        keyboardOptions = keyboardOptions,
        modifier = modifier,
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        endPadding = AppDimens.PasswordEndPadding,
        trailing = {
            Text(
                text = stringResource(if (visible) R.string.password_hide else R.string.password_show),
                style = AppTextStyles.FieldAction,
                color = Primary,
                modifier = Modifier
                    .clickable(role = Role.Button) { visible = !visible }
                    .padding(AppDimens.TogglePadding)
            )
        }
    )
}
