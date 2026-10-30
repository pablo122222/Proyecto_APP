package me.pablorincon.proyecto_app.ui.screens.forgotpassword

import android.widget.Toast
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import me.pablorincon.proyecto_app.R
import me.pablorincon.proyecto_app.ui.components.AppTextField
import me.pablorincon.proyecto_app.ui.components.FooterLink
import me.pablorincon.proyecto_app.ui.components.FormError
import me.pablorincon.proyecto_app.ui.components.PrimaryButton
import me.pablorincon.proyecto_app.ui.components.ScreenHeader
import me.pablorincon.proyecto_app.ui.theme.AppBackground
import me.pablorincon.proyecto_app.ui.theme.AppDimens
import me.pablorincon.proyecto_app.ui.theme.CodeMentorTheme

// Pantalla Recuperar contraseña. No existe en el mockup: se arma solo con
// los componentes de Registro y Login para que se vea igual que ellas.
@Composable
fun ForgotPasswordScreen(
    onBack: () -> Unit,
    onSent: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ForgotPasswordViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val sentMessage = stringResource(R.string.forgot_sent)

    // Cuando Firebase envía el correo: aviso y volver al login (una sola vez).
    LaunchedEffect(state.sent) {
        if (state.sent) {
            Toast.makeText(context, sentMessage, Toast.LENGTH_LONG).show()
            viewModel.onSentHandled()
            onSent()
        }
    }

    ForgotPasswordContent(
        state = state,
        onEmailChange = viewModel::onEmailChange,
        onSubmit = viewModel::submit,
        onBack = onBack,
        modifier = modifier
    )
}

// Solo dibuja: recibe el estado y avisa de lo que hace el usuario.
@Composable
private fun ForgotPasswordContent(
    state: ForgotPasswordUiState,
    onEmailChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppBackground)
            .imePadding() // Que el teclado no tape el campo
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
            stepLabel = stringResource(R.string.forgot_step_label),
            eyebrow = stringResource(R.string.forgot_eyebrow),
            title = stringResource(R.string.forgot_title),
            subtitle = stringResource(R.string.forgot_subtitle),
            onBack = onBack
        )

        Spacer(Modifier.height(AppDimens.FieldSpacing))
        AppTextField(
            label = stringResource(R.string.field_email_label),
            value = state.email,
            onValueChange = onEmailChange,
            placeholder = stringResource(R.string.field_email_placeholder),
            // Sin autocorrector: el teclado no debe "corregir" letras del correo
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                autoCorrectEnabled = false,
                imeAction = ImeAction.Done
            )
        )

        Spacer(Modifier.height(AppDimens.FieldSpacing))
        FormError(message = state.error?.let { stringResource(it.messageRes()) })

        Spacer(Modifier.height(AppDimens.ErrorToButton))
        PrimaryButton(text = stringResource(R.string.forgot_submit), onClick = onSubmit)

        FooterLink(
            text = stringResource(R.string.forgot_remembered),
            linkText = stringResource(R.string.forgot_login),
            onClick = onBack,
            modifier = Modifier.padding(top = AppDimens.CenterLinkTop)
        )
    }
}

// Texto que se muestra para cada error.
private fun ForgotPasswordError.messageRes(): Int = when (this) {
    ForgotPasswordError.InvalidEmail -> R.string.forgot_error_email
    ForgotPasswordError.NoConnection -> R.string.forgot_error_no_connection
    ForgotPasswordError.Unknown -> R.string.forgot_error_unknown
}

@Preview(showSystemUi = true)
@Composable
private fun ForgotPasswordScreenPreview() {
    CodeMentorTheme {
        ForgotPasswordContent(
            state = ForgotPasswordUiState(error = ForgotPasswordError.InvalidEmail),
            onEmailChange = {}, onSubmit = {}, onBack = {}
        )
    }
}
