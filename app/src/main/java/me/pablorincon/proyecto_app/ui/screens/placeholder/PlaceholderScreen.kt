package me.pablorincon.proyecto_app.ui.screens.placeholder

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import me.pablorincon.proyecto_app.R
import me.pablorincon.proyecto_app.ui.components.BackButton
import me.pablorincon.proyecto_app.ui.theme.AppBackground
import me.pablorincon.proyecto_app.ui.theme.AppDimens
import me.pablorincon.proyecto_app.ui.theme.AppTextStyles
import me.pablorincon.proyecto_app.ui.theme.CodeMentorTheme
import me.pablorincon.proyecto_app.ui.theme.Ink

// Destino provisional para las pantallas que aún no se construyen: solo muestra
// su título (tomado de la guía) y, si corresponde, el botón atrás.
// Cada pantalla lo reemplaza en su propia fase.
@Composable
fun PlaceholderScreen(
    @StringRes title: Int,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null
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
                bottom = AppDimens.ScreenBottom
            )
    ) {
        if (onBack != null) {
            BackButton(onClick = onBack)
            Spacer(Modifier.height(AppDimens.TopLineBottom))
        }
        Text(text = stringResource(title), style = AppTextStyles.ScreenTitleSmall, color = Ink)
    }
}

@Preview(showSystemUi = true)
@Composable
private fun PlaceholderScreenPreview() {
    CodeMentorTheme {
        PlaceholderScreen(title = R.string.title_learning_path, onBack = {})
    }
}
