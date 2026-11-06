package me.pablorincon.proyecto_app.ui.screens.personalization

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import me.pablorincon.proyecto_app.R
import me.pablorincon.proyecto_app.domain.LearningGoal
import me.pablorincon.proyecto_app.domain.LearningLevel
import me.pablorincon.proyecto_app.ui.components.AppCard
import me.pablorincon.proyecto_app.ui.components.BackButton
import me.pablorincon.proyecto_app.ui.components.FixedBottomLayout
import me.pablorincon.proyecto_app.ui.components.Pill
import me.pablorincon.proyecto_app.ui.components.PrimaryButton
import me.pablorincon.proyecto_app.ui.components.RowLine
import me.pablorincon.proyecto_app.ui.theme.AppDimens
import me.pablorincon.proyecto_app.ui.theme.AppShapes
import me.pablorincon.proyecto_app.ui.theme.AppTextStyles
import me.pablorincon.proyecto_app.ui.theme.ButtonShadow
import me.pablorincon.proyecto_app.ui.theme.CodeMentorTheme
import me.pablorincon.proyecto_app.ui.theme.Ink
import me.pablorincon.proyecto_app.ui.theme.Mint
import me.pablorincon.proyecto_app.ui.theme.MintInk
import me.pablorincon.proyecto_app.ui.theme.Muted
import me.pablorincon.proyecto_app.ui.theme.PathIllustrationEnd
import me.pablorincon.proyecto_app.ui.theme.PathIllustrationStart
import me.pablorincon.proyecto_app.ui.theme.Primary

// Pantalla Ruta lista (guía de diseño §1.3). Muestra lo que el usuario eligió de verdad.
@Composable
fun PathReadyScreen(
    viewModel: PersonalizationViewModel,
    onBack: () -> Unit,
    onEnter: () -> Unit,
    onIncomplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val level = state.level
    val goal = state.goal

    // Si el sistema recreó la app y se perdieron las elecciones (todavía sin guardar),
    // se vuelve al primer paso en lugar de mostrar datos vacíos.
    LaunchedEffect(level, goal) {
        if (level == null || goal == null) onIncomplete()
    }

    if (level != null && goal != null) {
        PathReadyContent(
            level = level,
            goal = goal,
            onBack = onBack,
            onEnter = onEnter,
            modifier = modifier
        )
    }
}

// Solo dibuja: recibe lo elegido y avisa de lo que hace el usuario.
@Composable
private fun PathReadyContent(
    level: LearningLevel,
    goal: LearningGoal,
    onBack: () -> Unit,
    onEnter: () -> Unit,
    modifier: Modifier = Modifier
) {
    FixedBottomLayout(
        modifier = modifier,
        bottomBar = {
            PrimaryButton(
                text = stringResource(R.string.path_enter),
                trailingSymbol = stringResource(R.string.arrow_right),
                onClick = onEnter
            )
        }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackButton(onClick = onBack)
            Pill(
                text = stringResource(R.string.path_pill),
                containerColor = Mint,
                contentColor = MintInk
            )
        }

        RocketIllustration(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = AppDimens.PathIllustrationTop, bottom = AppDimens.PathIllustrationBottom)
        )

        CenteredText(
            text = stringResource(R.string.path_eyebrow).uppercase(),
            style = AppTextStyles.Eyebrow,
            color = Primary
        )
        Spacer(Modifier.height(AppDimens.EyebrowBottom))
        CenteredText(text = stringResource(R.string.title_path_ready), style = AppTextStyles.ScreenTitle, color = Ink)
        Spacer(Modifier.height(AppDimens.TitleBottom))
        CenteredText(text = stringResource(R.string.path_subtitle), style = AppTextStyles.Subtitle, color = Muted)

        Spacer(Modifier.height(AppDimens.PathCardTop))
        AppCard {
            RowLine(
                icon = stringResource(R.string.path_level_icon),
                title = stringResource(R.string.path_level_title),
                subtitle = stringResource(level.valueRes)
            )
            RowLine(
                icon = stringResource(R.string.path_goal_icon),
                title = stringResource(R.string.path_goal_title),
                subtitle = stringResource(goal.titleRes)
            )
            RowLine(
                icon = stringResource(R.string.path_pace_icon),
                title = stringResource(R.string.path_pace_title),
                subtitle = stringResource(R.string.path_pace_value),
                showDivider = false
            )
        }
    }
}

@Composable
private fun CenteredText(
    text: String,
    style: TextStyle,
    color: Color
) {
    Text(
        text = text,
        style = style,
        color = color,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}

// Cuadro 90×90 con degradado y el cohete 🚀 (decorativo: el lector de pantalla lo omite).
@Composable
private fun RocketIllustration(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(AppDimens.PathIllustrationSize)
            .dropShadow(
                shape = AppShapes.PathIllustration,
                shadow = Shadow(
                    radius = AppDimens.PathIllustrationShadowRadius,
                    color = ButtonShadow,
                    offset = DpOffset(0.dp, AppDimens.PathIllustrationShadowOffsetY)
                )
            )
            .clip(AppShapes.PathIllustration)
            .background(Brush.linearGradient(listOf(PathIllustrationStart, PathIllustrationEnd)))
            .clearAndSetSemantics { },
        contentAlignment = Alignment.Center
    ) {
        Text(text = stringResource(R.string.path_rocket), style = AppTextStyles.PathEmoji)
    }
}

@Preview(showSystemUi = true)
@Composable
private fun PathReadyScreenPreview() {
    CodeMentorTheme {
        PathReadyContent(
            level = LearningLevel.Beginner,
            goal = LearningGoal.UnderstandProgramming,
            onBack = {}, onEnter = {}
        )
    }
}
