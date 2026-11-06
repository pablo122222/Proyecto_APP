package me.pablorincon.proyecto_app.ui.screens.startup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import me.pablorincon.proyecto_app.R
import me.pablorincon.proyecto_app.ui.components.PrimaryButton
import me.pablorincon.proyecto_app.ui.theme.AppBackground
import me.pablorincon.proyecto_app.ui.theme.AppDimens
import me.pablorincon.proyecto_app.ui.theme.AppTextStyles
import me.pablorincon.proyecto_app.ui.theme.CodeMentorTheme
import me.pablorincon.proyecto_app.ui.theme.Muted
import me.pablorincon.proyecto_app.ui.theme.Primary

// Pantalla breve de arranque (no está en el mockup): aparece solo mientras se
// consulta el perfil con la sesión abierta, o si esa consulta falló.
@Composable
fun StartupScreen(
    isError: Boolean,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = AppDimens.ScreenHorizontal),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isError) {
            Text(
                text = stringResource(R.string.startup_error),
                style = AppTextStyles.SmallBody,
                color = Muted,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(AppDimens.ErrorToButton))
            PrimaryButton(text = stringResource(R.string.startup_retry), onClick = onRetry)
        } else {
            val loading = stringResource(R.string.startup_loading)
            CircularProgressIndicator(
                color = Primary,
                modifier = Modifier.semantics { contentDescription = loading }
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun StartupScreenErrorPreview() {
    CodeMentorTheme { StartupScreen(isError = true, onRetry = {}) }
}
