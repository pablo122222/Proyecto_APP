package me.pablorincon.proyecto_app.ui.screens.personalization

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import me.pablorincon.proyecto_app.R
import me.pablorincon.proyecto_app.domain.LearningLevel
import me.pablorincon.proyecto_app.ui.components.ChoiceOption
import me.pablorincon.proyecto_app.ui.components.FixedBottomLayout
import me.pablorincon.proyecto_app.ui.components.PrimaryButton
import me.pablorincon.proyecto_app.ui.components.StepHeader
import me.pablorincon.proyecto_app.ui.theme.AppDimens
import me.pablorincon.proyecto_app.ui.theme.CodeMentorTheme

// Pantalla Nivel, paso 1 de 2 (guía de diseño §1.1).
// Es la primera pantalla tras el login, así que no tiene a dónde volver: sin botón atrás.
@Composable
fun LevelScreen(
    viewModel: PersonalizationViewModel,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    LevelContent(
        selected = state.level,
        canContinue = state.canContinueFromLevel,
        onSelect = viewModel::selectLevel,
        onContinue = onContinue,
        modifier = modifier
    )
}

// Solo dibuja: recibe el estado y avisa de lo que hace el usuario.
@Composable
private fun LevelContent(
    selected: LearningLevel?,
    canContinue: Boolean,
    onSelect: (LearningLevel) -> Unit,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier
) {
    FixedBottomLayout(
        modifier = modifier,
        bottomBar = {
            PrimaryButton(
                text = stringResource(R.string.level_continue),
                trailingSymbol = stringResource(R.string.arrow_right),
                enabled = canContinue,
                onClick = onContinue
            )
        }
    ) {
        StepHeader(
            step = 1,
            totalSteps = 2,
            eyebrow = stringResource(R.string.personalization_eyebrow),
            title = stringResource(R.string.title_level),
            subtitle = stringResource(R.string.level_subtitle),
            onBack = null
        )

        Spacer(Modifier.height(AppDimens.ChoiceListTop))
        Column(
            modifier = Modifier.selectableGroup(),
            verticalArrangement = Arrangement.spacedBy(AppDimens.ChoiceGap)
        ) {
            LearningLevel.entries.forEach { level ->
                ChoiceOption(
                    icon = stringResource(level.iconRes),
                    title = stringResource(level.titleRes),
                    subtitle = stringResource(level.subtitleRes),
                    selected = level == selected,
                    onClick = { onSelect(level) }
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun LevelScreenPreview() {
    CodeMentorTheme {
        LevelContent(
            selected = LearningLevel.Basic, canContinue = true,
            onSelect = {}, onContinue = {}
        )
    }
}
