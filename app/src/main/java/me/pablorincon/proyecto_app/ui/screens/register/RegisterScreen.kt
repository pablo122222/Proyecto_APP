package me.pablorincon.proyecto_app.ui.screens.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import me.pablorincon.proyecto_app.R
import me.pablorincon.proyecto_app.domain.AuthValidator.RegisterError
import me.pablorincon.proyecto_app.ui.components.AppTextField
import me.pablorincon.proyecto_app.ui.components.FooterLink
import me.pablorincon.proyecto_app.ui.components.FormError
import me.pablorincon.proyecto_app.ui.components.PasswordTextField
import me.pablorincon.proyecto_app.ui.components.PrimaryButton
import me.pablorincon.proyecto_app.ui.components.ScreenHeader
import me.pablorincon.proyecto_app.ui.theme.AppBackground
import me.pablorincon.proyecto_app.ui.theme.AppDimens
import me.pablorincon.proyecto_app.ui.theme.AppTextStyles
import me.pablorincon.proyecto_app.ui.theme.CodeMentorTheme
import me.pablorincon.proyecto_app.ui.theme.FinePrintText

// Pantalla Crear cuenta (#view-register del mockup).
@Composable
fun RegisterScreen(
    onBack: () -> Unit,
    onLogin: () -> Unit,
    onRegistered: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    RegisterContent(
        state = state,
        onNameChange = viewModel::onNameChange,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onSubmit = { if (viewModel.submit()) onRegistered() },
        onBack = onBack,
        onLogin = onLogin,
        modifier = modifier
    )
}

// Solo dibuja: recibe el estado y avisa de lo que hace el usuario.
// Separarlo del ViewModel permite verlo en la vista previa.
@Composable
private fun RegisterContent(
    state: RegisterUiState,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onBack: () -> Unit,
    onLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppBackground)
            .imePadding() // Que el teclado no tape los campos
            .verticalScroll(rememberScrollState())
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(
                start = AppDimens.ScreenHorizontal,
                end = AppDimens.ScreenHorizontal,
                top = AppDimens.ScreenTop,
                bottom = AppDimens.ScreenBottom
            )
    ) {
        ScreenHeader(
            stepLabel = stringResource(R.string.register_step_label),
            eyebrow = stringResource(R.string.register_eyebrow),
            title = stringResource(R.string.register_title),
            subtitle = stringResource(R.string.register_subtitle),
            onBack = onBack
        )

        Spacer(Modifier.height(AppDimens.FieldSpacing))
        AppTextField(
            label = stringResource(R.string.register_name_label),
            value = state.name,
            onValueChange = onNameChange,
            placeholder = stringResource(R.string.register_name_placeholder),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            )
        )

        Spacer(Modifier.height(AppDimens.FieldSpacing))
        AppTextField(
            label = stringResource(R.string.field_email_label),
            value = state.email,
            onValueChange = onEmailChange,
            placeholder = stringResource(R.string.field_email_placeholder),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next)
        )

        Spacer(Modifier.height(AppDimens.FieldSpacing))
        PasswordTextField(
            label = stringResource(R.string.field_password_label),
            value = state.password,
            onValueChange = onPasswordChange,
            placeholder = stringResource(R.string.register_password_placeholder),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done)
        )

        Spacer(Modifier.height(AppDimens.FieldSpacing))
        FormError(message = state.error?.let { stringResource(it.messageRes()) })

        Spacer(Modifier.height(AppDimens.ErrorToButton))
        PrimaryButton(text = stringResource(R.string.register_submit), onClick = onSubmit)

        Spacer(Modifier.height(AppDimens.FinePrintTop))
        Text(
            text = stringResource(R.string.register_fineprint),
            style = AppTextStyles.FinePrint,
            color = FinePrintText
        )

        FooterLink(
            text = stringResource(R.string.register_has_account),
            linkText = stringResource(R.string.register_login),
            onClick = onLogin,
            modifier = Modifier.padding(top = AppDimens.CenterLinkTop)
        )
    }
}

// Texto que se muestra para cada error de validación.
private fun RegisterError.messageRes(): Int = when (this) {
    RegisterError.NameTooShort -> R.string.register_error_name
    RegisterError.InvalidEmail -> R.string.register_error_email
    RegisterError.PasswordTooShort -> R.string.register_error_password
}

@Preview(showSystemUi = true)
@Composable
private fun RegisterScreenPreview() {
    CodeMentorTheme {
        RegisterContent(
            state = RegisterUiState(error = RegisterError.InvalidEmail),
            onNameChange = {}, onEmailChange = {}, onPasswordChange = {},
            onSubmit = {}, onBack = {}, onLogin = {}
        )
    }
}
