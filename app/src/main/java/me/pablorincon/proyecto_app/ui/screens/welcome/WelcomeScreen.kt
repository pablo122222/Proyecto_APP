package me.pablorincon.proyecto_app.ui.screens.welcome

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import me.pablorincon.proyecto_app.R
import me.pablorincon.proyecto_app.ui.components.FooterLink
import me.pablorincon.proyecto_app.ui.components.Pill
import me.pablorincon.proyecto_app.ui.components.PrimaryButton
import me.pablorincon.proyecto_app.ui.theme.AppBackground
import me.pablorincon.proyecto_app.ui.theme.AppDimens
import me.pablorincon.proyecto_app.ui.theme.AppShapes
import me.pablorincon.proyecto_app.ui.theme.AppTextStyles
import me.pablorincon.proyecto_app.ui.theme.CodeMentorTheme
import me.pablorincon.proyecto_app.ui.theme.Ink
import me.pablorincon.proyecto_app.ui.theme.Mint
import me.pablorincon.proyecto_app.ui.theme.MintInk
import me.pablorincon.proyecto_app.ui.theme.Muted
import me.pablorincon.proyecto_app.ui.theme.Primary

// Pantalla de Bienvenida (#view-welcome del mockup).
// Arriba va el contenido con scroll; abajo, fijos, el botón "Crear mi cuenta" y el enlace a login.
@Composable
fun WelcomeScreen(
    onCreateAccount: () -> Unit,
    onLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Alto de la zona fija inferior, para que el final del contenido no quede debajo de ella.
    var bottomBarHeight by remember { mutableIntStateOf(0) }
    val bottomBarHeightDp = with(LocalDensity.current) { bottomBarHeight.toDp() }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .padding(
                    start = AppDimens.ScreenHorizontal,
                    end = AppDimens.ScreenHorizontal,
                    top = AppDimens.WelcomeTop,
                    bottom = bottomBarHeightDp
                )
        ) {
            // Logo + pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppLogo()
                Pill(text = stringResource(R.string.welcome_pill))
            }

            WelcomeIllustration(
                modifier = Modifier
                    .padding(vertical = AppDimens.IllustrationVertical)
                    .horizontalOutset(AppDimens.IllustrationOutset)
            )

            Text(
                text = stringResource(R.string.welcome_eyebrow).uppercase(),
                style = AppTextStyles.Eyebrow,
                color = Primary
            )
            Spacer(Modifier.height(AppDimens.EyebrowBottom))

            // Título con "paso a paso." en azul
            val titleStart = stringResource(R.string.welcome_title_start)
            val titleHighlight = stringResource(R.string.welcome_title_highlight)
            Text(
                text = buildAnnotatedString {
                    append(titleStart)
                    append(" ")
                    withStyle(SpanStyle(color = Primary)) { append(titleHighlight) }
                },
                style = AppTextStyles.WelcomeTitle,
                color = Ink
            )
            Spacer(Modifier.height(AppDimens.TitleBottom))

            Text(
                text = stringResource(R.string.welcome_subtitle),
                style = AppTextStyles.Subtitle,
                color = Muted
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = AppDimens.PerksTop),
                horizontalArrangement = Arrangement.spacedBy(AppDimens.PerksGap, Alignment.CenterHorizontally)
            ) {
                Perk(text = stringResource(R.string.welcome_perk_lessons))
                Perk(text = stringResource(R.string.welcome_perk_challenges))
            }
        }

        // Zona fija inferior (.bottom-fixed): el degradado de arriba difumina el
        // contenido que pasa por debajo al hacer scroll.
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .onSizeChanged { bottomBarHeight = it.height }
                .drawBehind {
                    drawRect(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, AppBackground),
                            startY = 0f,
                            endY = AppDimens.BottomFade.toPx()
                        )
                    )
                }
                .padding(
                    start = AppDimens.ScreenHorizontal,
                    end = AppDimens.ScreenHorizontal,
                    top = AppDimens.BottomFixedTop
                )
                .navigationBarsPadding()
                .padding(bottom = AppDimens.BottomFixed)
        ) {
            PrimaryButton(
                text = stringResource(R.string.welcome_create_account),
                trailingSymbol = stringResource(R.string.arrow_right),
                onClick = onCreateAccount
            )
            FooterLink(
                text = stringResource(R.string.welcome_has_account),
                linkText = stringResource(R.string.welcome_login),
                onClick = onLogin,
                modifier = Modifier.padding(top = AppDimens.CenterLinkTop)
            )
        }
    }
}

// Logo: cuadro azul con </> + "CodeMentor" (.app-logo).
@Composable
private fun AppLogo() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(AppDimens.LogoGap),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(AppDimens.LogoIconSize)
                .clip(AppShapes.LogoIcon)
                .background(Primary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.brand_icon),
                style = AppTextStyles.LogoIcon,
                color = Color.White
            )
        }
        Text(
            text = stringResource(R.string.brand_name),
            style = AppTextStyles.Logo,
            color = Ink
        )
    }
}

// Ventaja con check verde (.welcome-perks span).
@Composable
private fun Perk(text: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(AppDimens.PerkIconGap),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(AppDimens.CheckMiniSize)
                .clip(AppShapes.CheckMini)
                .background(Mint),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.check_mark),
                style = AppTextStyles.CheckMini,
                color = MintInk
            )
        }
        Text(text = text, style = AppTextStyles.Perk, color = Muted)
    }
}

// Equivale al margen negativo del CSS (margin: 0 -2px): ensancha el elemento
// hacia ambos lados sin cambiar el margen de la pantalla.
private fun Modifier.horizontalOutset(outset: Dp) = layout { measurable, constraints ->
    val extra = (outset * 2).roundToPx()
    val placeable = measurable.measure(
        constraints.copy(
            minWidth = constraints.minWidth + extra,
            maxWidth = constraints.maxWidth + extra
        )
    )
    layout(placeable.width - extra, placeable.height) {
        placeable.place(-extra / 2, 0)
    }
}

@Preview(showSystemUi = true)
@Composable
private fun WelcomeScreenPreview() {
    CodeMentorTheme {
        WelcomeScreen(onCreateAccount = {}, onLogin = {})
    }
}
