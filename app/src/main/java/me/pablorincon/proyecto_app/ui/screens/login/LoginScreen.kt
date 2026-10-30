package me.pablorincon.proyecto_app.ui.screens.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import me.pablorincon.proyecto_app.R
import me.pablorincon.proyecto_app.ui.components.AppTextField
import me.pablorincon.proyecto_app.ui.components.FooterLink
import me.pablorincon.proyecto_app.ui.components.FormError
import me.pablorincon.proyecto_app.ui.components.PasswordTextField
import me.pablorincon.proyecto_app.ui.components.PrimaryButton
import me.pablorincon.proyecto_app.ui.components.ScreenHeader
import me.pablorincon.proyecto_app.ui.components.SecondaryButton
import me.pablorincon.proyecto_app.ui.theme.AppBackground
import me.pablorincon.proyecto_app.ui.theme.AppDimens
import me.pablorincon.proyecto_app.ui.theme.AppShapes
import me.pablorincon.proyecto_app.ui.theme.AppTextStyles
import me.pablorincon.proyecto_app.ui.theme.CardShadow
import me.pablorincon.proyecto_app.ui.theme.CodeMentorTheme
import me.pablorincon.proyecto_app.ui.theme.Primary
import me.pablorincon.proyecto_app.ui.theme.PrimaryDark
import me.pablorincon.proyecto_app.ui.theme.PrimarySoft
import me.pablorincon.proyecto_app.ui.theme.PrimarySoftBorder

// Pantalla Iniciar sesión (#view-login del mockup).
@Composable
fun LoginScreen(
    onBack: () -> Unit,
    onCreateAccount: () -> Unit,
    onForgotPassword: () -> Unit,
    onLoggedIn: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val exampleLoaded = stringResource(R.string.login_example_loaded)
    // Como el mockup, el saludo usa solo el primer nombre
    val welcome = state.userName?.trim()?.split(Regex("\\s+"))?.firstOrNull()?.takeIf { it.isNotEmpty() }
        ?.let { stringResource(R.string.login_welcome, it) }
        ?: stringResource(R.string.login_welcome_no_name)

    // Cuando Firebase confirma la sesión: saludo y salir de la pantalla (una sola vez).
    LaunchedEffect(state.loggedIn) {
        if (state.loggedIn) {
            Toast.makeText(context, welcome, Toast.LENGTH_SHORT).show()
            viewModel.onLoggedInHandled()
            onLoggedIn()
        }
    }

    LoginContent(
        state = state,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onSubmit = viewModel::submit,
        onUseExample = {
            viewModel.fillExampleAccount()
            Toast.makeText(context, exampleLoaded, Toast.LENGTH_SHORT).show()
        },
        onForgotPassword = onForgotPassword,
        onBack = onBack,
        onCreateAccount = onCreateAccount,
        modifier = modifier
    )
}

// Solo dibuja: recibe el estado y avisa de lo que hace el usuario.
@Composable
private fun LoginContent(
    state: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onUseExample: () -> Unit,
    onForgotPassword: () -> Unit,
    onBack: () -> Unit,
    onCreateAccount: () -> Unit,
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
            stepLabel = stringResource(R.string.login_step_label),
            eyebrow = stringResource(R.string.login_eyebrow),
            title = stringResource(R.string.login_title),
            subtitle = stringResource(R.string.login_subtitle),
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
                imeAction = ImeAction.Next
            )
        )

        Spacer(Modifier.height(AppDimens.FieldSpacing))
        PasswordTextField(
            label = stringResource(R.string.field_password_label),
            value = state.password,
            onValueChange = onPasswordChange,
            placeholder = stringResource(R.string.login_password_placeholder),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done)
        )

        // Agregado (no está en el mockup): lleva a recuperar la contraseña por correo
        Text(
            text = stringResource(R.string.login_forgot_password),
            style = AppTextStyles.FooterLink,
            color = Primary,
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = AppDimens.ForgotLinkGap)
                .clickable(role = Role.Button, onClick = onForgotPassword)
        )

        Spacer(Modifier.height(AppDimens.ForgotLinkGap))
        FormError(message = state.error?.let { stringResource(it.messageRes()) })

        Spacer(Modifier.height(AppDimens.ErrorToButton))
        PrimaryButton(text = stringResource(R.string.login_submit), onClick = onSubmit)

        Spacer(Modifier.height(AppDimens.CardTop))
        ExampleAccountCard(onUseExample = onUseExample)

        FooterLink(
            text = stringResource(R.string.login_new),
            linkText = stringResource(R.string.login_create_account),
            onClick = onCreateAccount,
            modifier = Modifier.padding(top = AppDimens.CenterLinkTop)
        )
    }
}

// Tarjeta azul suave con los datos de la cuenta de ejemplo (.card de la demo).
@Composable
private fun ExampleAccountCard(onUseExample: () -> Unit) {
    val data = buildAnnotatedString {
        append(stringResource(R.string.login_example_email_label) + " ")
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append(EXAMPLE_EMAIL) }
        append("\n" + stringResource(R.string.login_example_password_label) + " ")
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append(EXAMPLE_PASSWORD) }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .dropShadow(
                shape = AppShapes.Card,
                shadow = Shadow(
                    radius = AppDimens.CardShadowRadius,
                    color = CardShadow,
                    offset = DpOffset(0.dp, AppDimens.CardShadowOffsetY)
                )
            )
            .clip(AppShapes.Card)
            .background(PrimarySoft)
            .border(AppDimens.BorderWidth, PrimarySoftBorder, AppShapes.Card)
            .padding(AppDimens.CardPadding)
    ) {
        Text(
            text = stringResource(R.string.login_example_title),
            style = AppTextStyles.CardTitleSmall,
            color = Primary
        )
        Spacer(Modifier.height(AppDimens.CardTextTop))
        Text(text = data, style = AppTextStyles.SmallBody, color = PrimaryDark)
        Spacer(Modifier.height(AppDimens.CardButtonTop))
        SecondaryButton(text = stringResource(R.string.login_example_button), onClick = onUseExample)
    }
}

// Texto que se muestra para cada error.
private fun LoginError.messageRes(): Int = when (this) {
    LoginError.WrongCredentials -> R.string.login_error_credentials
    LoginError.NoConnection -> R.string.login_error_no_connection
    LoginError.Unknown -> R.string.login_error_unknown
}

@Preview(showSystemUi = true)
@Composable
private fun LoginScreenPreview() {
    CodeMentorTheme {
        LoginContent(
            state = LoginUiState(error = LoginError.WrongCredentials),
            onEmailChange = {}, onPasswordChange = {}, onSubmit = {}, onUseExample = {},
            onForgotPassword = {}, onBack = {}, onCreateAccount = {}
        )
    }
}
