package me.pablorincon.proyecto_app.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import me.pablorincon.proyecto_app.R
import me.pablorincon.proyecto_app.ui.components.AppCard
import me.pablorincon.proyecto_app.ui.components.ConfirmSheet
import me.pablorincon.proyecto_app.ui.components.RowLine
import me.pablorincon.proyecto_app.ui.components.SectionHeading
import me.pablorincon.proyecto_app.ui.theme.AppBackground
import me.pablorincon.proyecto_app.ui.theme.AppDimens
import me.pablorincon.proyecto_app.ui.theme.AppTextStyles
import me.pablorincon.proyecto_app.ui.theme.CodeMentorTheme
import me.pablorincon.proyecto_app.ui.theme.Ink

// Pantalla Perfil. Por ahora solo tiene el título y la tarjeta "Cuenta";
// el resto (avatar, XP, estadísticas, meta diaria) llega en la Fase 17.
@Composable
fun ProfileScreen(
    onAchievements: () -> Unit,
    onSettings: () -> Unit,
    onSignedOut: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    // Cerrar sesión pide confirmación: un toque sin querer no debe sacar al usuario
    var confirmingSignOut by rememberSaveable { mutableStateOf(false) }

    // Tras cerrar sesión: volver a Bienvenida (una sola vez).
    LaunchedEffect(state.signedOut) {
        if (state.signedOut) {
            viewModel.onSignedOutHandled()
            onSignedOut()
        }
    }

    ProfileContent(
        onAchievements = onAchievements,
        onSettings = onSettings,
        onSignOut = { confirmingSignOut = true },
        modifier = modifier
    )

    if (confirmingSignOut) {
        ConfirmSheet(
            title = stringResource(R.string.logout_confirm_title),
            message = stringResource(R.string.logout_confirm_message),
            confirmLabel = stringResource(R.string.logout_confirm_yes),
            cancelLabel = stringResource(R.string.logout_confirm_cancel),
            onConfirm = {
                confirmingSignOut = false
                viewModel.signOut()
            },
            onDismiss = { confirmingSignOut = false }
        )
    }
}

// Solo dibuja: avisa de lo que hace el usuario.
@Composable
private fun ProfileContent(
    onAchievements: () -> Unit,
    onSettings: () -> Unit,
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppBackground)
            .verticalScroll(rememberScrollState())
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(
                start = AppDimens.ScreenHorizontal,
                end = AppDimens.ScreenHorizontal,
                top = AppDimens.ScreenTop,
                bottom = AppDimens.ScreenBottom // Libre para la barra de pestañas
            )
    ) {
        Text(text = stringResource(R.string.profile_title), style = AppTextStyles.ScreenTitleSmall, color = Ink)

        // El margen superior de la sección (22) hace de separación con el título
        SectionHeading(title = stringResource(R.string.profile_account_section))
        AppCard {
            RowLine(
                icon = stringResource(R.string.profile_achievements_icon),
                title = stringResource(R.string.profile_achievements_title),
                subtitle = stringResource(R.string.profile_achievements_subtitle),
                onClick = onAchievements
            )
            RowLine(
                icon = stringResource(R.string.profile_settings_icon),
                title = stringResource(R.string.profile_settings_title),
                subtitle = stringResource(R.string.profile_settings_subtitle),
                onClick = onSettings
            )
            RowLine(
                icon = stringResource(R.string.profile_logout_icon),
                title = stringResource(R.string.profile_logout_title),
                subtitle = stringResource(R.string.profile_logout_subtitle),
                onClick = onSignOut,
                showDivider = false,
                danger = true
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ProfileScreenPreview() {
    CodeMentorTheme {
        ProfileContent(onAchievements = {}, onSettings = {}, onSignOut = {})
    }
}
