package me.pablorincon.proyecto_app.ui.screens.personalization

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import me.pablorincon.proyecto_app.R
import me.pablorincon.proyecto_app.domain.LearningGoal
import me.pablorincon.proyecto_app.ui.components.ChoiceOption
import me.pablorincon.proyecto_app.ui.components.FixedBottomLayout
import me.pablorincon.proyecto_app.ui.components.FormError
import me.pablorincon.proyecto_app.ui.components.PrimaryButton
import me.pablorincon.proyecto_app.ui.components.StepHeader
import me.pablorincon.proyecto_app.ui.theme.AppDimens
import me.pablorincon.proyecto_app.ui.theme.CodeMentorTheme

// Pantalla Objetivo, paso 2 de 2 (guía de diseño §1.2).
// "Crear mi ruta →" guarda nivel y objetivo en Firestore y avanza a "Ruta lista".
@Composable
fun GoalScreen(
    viewModel: PersonalizationViewModel,
    onBack: () -> Unit,
    onSaved: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    // Cuando Firestore confirma el guardado: avanzar (una sola vez).
    LaunchedEffect(state.saved) {
        if (state.saved) {
            viewModel.onSavedHandled()
            onSaved()
        }
    }

    GoalContent(
        selected = state.goal,
        canCreateRoute = state.canCreateRoute,
        showError = state.saveError,
        onSelect = viewModel::selectGoal,
        onCreateRoute = viewModel::saveRoute,
        onBack = onBack,
        modifier = modifier
    )
}

// Solo dibuja: recibe el estado y avisa de lo que hace el usuario.
@Composable
private fun GoalContent(
    selected: LearningGoal?,
    canCreateRoute: Boolean,
    showError: Boolean,
    onSelect: (LearningGoal) -> Unit,
    onCreateRoute: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    FixedBottomLayout(
        modifier = modifier,
        bottomBar = {
            // Aviso de error (no está en el mockup). Reserva su lugar siempre,
            // para que el botón no salte cuando aparece.
            FormError(message = if (showError) stringResource(R.string.goal_error_save) else null)
            Spacer(Modifier.height(AppDimens.ChoiceErrorBottom))
            PrimaryButton(
                text = stringResource(R.string.goal_create_route),
                trailingSymbol = stringResource(R.string.arrow_right),
                enabled = canCreateRoute,
                onClick = onCreateRoute
            )
        }
    ) {
        StepHeader(
            step = 2,
            totalSteps = 2,
            eyebrow = stringResource(R.string.goal_eyebrow),
            title = stringResource(R.string.title_goal),
            subtitle = stringResource(R.string.goal_subtitle),
            onBack = onBack
        )

        Spacer(Modifier.height(AppDimens.ChoiceListTop))
        Column(
            modifier = Modifier.selectableGroup(),
            verticalArrangement = Arrangement.spacedBy(AppDimens.ChoiceGap)
        ) {
            LearningGoal.entries.forEach { goal ->
                ChoiceOption(
                    icon = stringResource(goal.iconRes),
                    title = stringResource(goal.titleRes),
                    subtitle = stringResource(goal.subtitleRes),
                    selected = goal == selected,
                    onClick = { onSelect(goal) }
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun GoalScreenPreview() {
    CodeMentorTheme {
        GoalContent(
            selected = LearningGoal.SolveProblems, canCreateRoute = true, showError = false,
            onSelect = {}, onCreateRoute = {}, onBack = {}
        )
    }
}
