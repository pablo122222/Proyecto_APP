package me.pablorincon.proyecto_app.ui.screens.lab

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import me.pablorincon.proyecto_app.R
import me.pablorincon.proyecto_app.domain.glitchcity.ChoiceParam
import me.pablorincon.proyecto_app.domain.glitchcity.GlitchCityCatalog
import me.pablorincon.proyecto_app.domain.glitchcity.LabParam
import me.pablorincon.proyecto_app.domain.glitchcity.LabResult
import me.pablorincon.proyecto_app.domain.glitchcity.LabValues
import me.pablorincon.proyecto_app.domain.glitchcity.NpcAction
import me.pablorincon.proyecto_app.domain.glitchcity.NpcRulesLab
import me.pablorincon.proyecto_app.domain.glitchcity.SliderParam
import me.pablorincon.proyecto_app.domain.glitchcity.TextParam
import me.pablorincon.proyecto_app.domain.glitchcity.TrafficLight
import me.pablorincon.proyecto_app.ui.components.AchievementUnlockedDialog
import me.pablorincon.proyecto_app.ui.components.AppTextField
import me.pablorincon.proyecto_app.ui.components.BackButton
import me.pablorincon.proyecto_app.ui.components.Pill
import me.pablorincon.proyecto_app.ui.components.PrimaryButton
import me.pablorincon.proyecto_app.ui.components.SectionHeading
import me.pablorincon.proyecto_app.ui.screens.glitchcity.GlitchCityText
import me.pablorincon.proyecto_app.ui.theme.AccentYellow
import me.pablorincon.proyecto_app.ui.theme.AppBackground
import me.pablorincon.proyecto_app.ui.theme.AppDimens
import me.pablorincon.proyecto_app.ui.theme.AppTextStyles
import me.pablorincon.proyecto_app.ui.theme.CityNodeDone
import me.pablorincon.proyecto_app.ui.theme.FeedbackGoodBg
import me.pablorincon.proyecto_app.ui.theme.Ink
import me.pablorincon.proyecto_app.ui.theme.InputBorder
import me.pablorincon.proyecto_app.ui.theme.Muted
import me.pablorincon.proyecto_app.ui.theme.Paper
import me.pablorincon.proyecto_app.ui.theme.Primary
import me.pablorincon.proyecto_app.ui.theme.ProgressTrack

// Pantalla "Actividad" en modo laboratorio (guía §5.2): controles, código Python en vivo,
// resultado y descubrimientos. Mantiene activa la pestaña Retos.
@Composable
fun LabScreen(
    activityId: String,
    onBack: () -> Unit,
    onOpenAchievements: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: LabViewModel = viewModel(
        key = activityId,
        factory = viewModelFactory { initializer { LabViewModel(activityId) } }
    )
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    LabContent(
        state = state,
        onBack = onBack,
        onNumberChange = viewModel::onNumberChange,
        onTextChange = viewModel::onTextChange,
        onPublish = viewModel::publish,
        onRetry = viewModel::load,
        modifier = modifier
    )
    val ready = state as? LabUiState.Ready
    // Avisos cortos (toast de la guía §0)
    val isCreation = ready?.isCreation == true
    val toastText = when (val message = ready?.message) {
        is LabMessage.Discovered -> stringResource(R.string.lab_reward, message.reward.xp, message.reward.gems)
        is LabMessage.Published -> stringResource(R.string.lab_published, message.reward.xp, message.reward.gems)
        LabMessage.Updated -> stringResource(R.string.lab_updated)
        LabMessage.NoConnection -> stringResource(if (isCreation) R.string.lab_publish_no_connection else R.string.lab_save_no_connection)
        LabMessage.Failed -> stringResource(if (isCreation) R.string.lab_publish_failed else R.string.lab_save_failed)
        null -> null
    }
    ready?.why?.let { why ->
        WhySheet(state = why, onAnswer = viewModel::answerWhy, onClose = viewModel::closeWhy)
    }
    LaunchedEffect(ready?.message) {
        if (toastText != null) {
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()
            viewModel.onMessageShown()
        }
    }
    // Logros ganados (por ejemplo, Hábito al subir la racha); espera a que se cierre la pregunta
    AchievementUnlockedDialog(
        unlocks = if (ready?.why == null) ready?.unlocks.orEmpty() else emptyList(),
        onSeeAchievements = {
            viewModel.onUnlocksSeen()
            onOpenAchievements()
        },
        onDismiss = viewModel::onUnlocksSeen
    )
}

@Composable
private fun LabContent(
    state: LabUiState,
    onBack: () -> Unit,
    onNumberChange: (String, Int) -> Unit,
    onTextChange: (String, String) -> Unit,
    onPublish: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppBackground)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
            .padding(
                start = AppDimens.ScreenHorizontal,
                end = AppDimens.ScreenHorizontal,
                top = AppDimens.ScreenTop,
                bottom = AppDimens.ScreenBottom // Libre para la barra de pestañas
            )
    ) {
        when (state) {
            LabUiState.Loading -> {
                BackButton(onClick = onBack)
                val loading = stringResource(R.string.city_loading)
                Box(Modifier.fillMaxWidth().padding(top = 48.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Primary, modifier = Modifier.semantics { contentDescription = loading })
                }
            }
            LabUiState.Error -> {
                BackButton(onClick = onBack)
                Spacer(Modifier.height(AppDimens.TopLineBottom))
                Text(
                    text = stringResource(R.string.city_error),
                    style = AppTextStyles.SmallBody,
                    color = Muted,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(AppDimens.ErrorToButton))
                PrimaryButton(text = stringResource(R.string.startup_retry), onClick = onRetry)
            }
            is LabUiState.Ready -> ReadyBody(state, onBack, onNumberChange, onTextChange, onPublish)
        }
    }
}

@Composable
private fun ReadyBody(
    state: LabUiState.Ready,
    onBack: () -> Unit,
    onNumberChange: (String, Int) -> Unit,
    onTextChange: (String, String) -> Unit,
    onPublish: () -> Unit
) {
    val activityId = state.simulation.activityId
    val emoji = GlitchCityCatalog.lab(activityId)?.emoji ?: GlitchCityCatalog.creation(activityId)?.emoji
    val (name, _) = GlitchCityText.activityNames.getValue(activityId)

    // topline: atrás · "Explorar" / "Crear"
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        BackButton(onClick = onBack)
        Spacer(Modifier.weight(1f))
        Pill(text = stringResource(if (state.isCreation) R.string.city_tab_create else R.string.city_tab_explore))
    }
    Spacer(Modifier.height(AppDimens.TopLineBottom))
    Text(
        text = stringResource(R.string.lab_title, emoji.orEmpty(), stringResource(name)),
        style = AppTextStyles.ScreenTitleSmall,
        color = Ink
    )
    Spacer(Modifier.height(AppDimens.TitleBottom))
    Text(
        text = stringResource(if (state.isCreation) R.string.lab_create_subtitle else R.string.lab_explore_subtitle),
        style = AppTextStyles.Subtitle,
        color = Muted
    )

    Spacer(Modifier.height(18.dp))
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        state.simulation.params.forEach { param ->
            ParamControl(param, state.values, onNumberChange, onTextChange)
        }
    }

    Spacer(Modifier.height(18.dp))
    TracedCode(state.run)
    Spacer(Modifier.height(12.dp))
    ResultBox(state.run.result)

    if (state.isCreation) {
        // Crear: los objetivos se marcan en vivo con los valores actuales
        SectionHeading(title = stringResource(R.string.lab_objectives))
        GoalList(activityId, state.simulation.goalIds, state.run.met)
        Spacer(Modifier.height(18.dp))
        PrimaryButton(
            text = stringResource(if (state.published) R.string.lab_update else R.string.lab_publish),
            onClick = onPublish,
            enabled = state.canPublish
        )
    } else {
        SectionHeading(title = stringResource(R.string.lab_discoveries))
        GoalList(activityId, state.simulation.goalIds, state.done)
    }
}

// ---------- Controles ----------

@Composable
private fun ParamControl(
    param: LabParam,
    values: LabValues,
    onNumberChange: (String, Int) -> Unit,
    onTextChange: (String, String) -> Unit
) {
    val label = stringResource(LabText.paramLabels.getValue(param.id))
    when (param) {
        is SliderParam -> {
            val value = values.number(param.id)
            Column {
                // "{nombre}: **{valor}**"
                Text(
                    text = buildAnnotatedString {
                        append("$label: ")
                        withStyle(SpanStyle(fontWeight = FontWeight.ExtraBold, color = Primary)) { append(value.toString()) }
                    },
                    style = AppTextStyles.RowTitle,
                    color = Ink
                )
                Slider(
                    value = value.toFloat(),
                    onValueChange = { onNumberChange(param.id, snap(it, param)) },
                    valueRange = param.min.toFloat()..param.max.toFloat(),
                    steps = (param.max - param.min) / param.step - 1,
                    colors = SliderDefaults.colors(
                        thumbColor = Primary,
                        activeTrackColor = Primary,
                        inactiveTrackColor = ProgressTrack,
                        activeTickColor = Color.Transparent,
                        inactiveTickColor = Color.Transparent
                    )
                )
            }
        }
        is ChoiceParam -> ChoiceControl(label, param, values.text(param.id)) { onTextChange(param.id, it) }
        is TextParam -> AppTextField(
            label = label,
            value = values.text(param.id),
            onValueChange = { onTextChange(param.id, it) },
            placeholder = stringResource(R.string.lab_param_packages_placeholder)
        )
    }
}

// El slider devuelve un decimal: se lleva al paso más cercano.
private fun snap(raw: Float, param: SliderParam): Int {
    val steps = Math.round((raw - param.min) / param.step)
    return (param.min + steps * param.step).coerceIn(param.min, param.max)
}

private val ChoiceShape = RoundedCornerShape(12.dp)

// Selector desplegable: muestra la opción elegida y al tocarlo abre la lista.
@Composable
private fun ChoiceControl(label: String, param: ChoiceParam, selected: String, onSelect: (String) -> Unit) {
    var open by remember { mutableStateOf(false) }
    val description = stringResource(R.string.lab_choice_description, label, selected)
    Column {
        Text(text = label, style = AppTextStyles.RowTitle, color = Ink)
        Spacer(Modifier.height(8.dp))
        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(ChoiceShape)
                    .background(Paper)
                    .border(AppDimens.BorderWidth, InputBorder, ChoiceShape)
                    .semantics(mergeDescendants = true) { contentDescription = description }
                    .clickable(role = Role.DropdownList) { open = true }
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = selected, style = AppTextStyles.FieldInput, color = Ink, modifier = Modifier.weight(1f))
                Text(text = "▾", style = AppTextStyles.FieldInput, color = Muted)
            }
            DropdownMenu(expanded = open, onDismissRequest = { open = false }, containerColor = Paper) {
                param.options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option, style = AppTextStyles.FieldInput, color = Ink) },
                        onClick = {
                            open = false
                            onSelect(option)
                        }
                    )
                }
            }
        }
    }
}

// ---------- Resultado ----------

private val ResultShape = RoundedCornerShape(16.dp)
private val LightRed = Color(0xFFE5646A)  // Guía §5.2: rojo del semáforo

@Composable
private fun ResultBox(result: LabResult) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(ResultShape)
            .background(FeedbackGoodBg)
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        when (result) {
            is LabResult.Light -> Row(verticalAlignment = Alignment.CenterVertically) {
                val (color, word) = when (result.light) {
                    TrafficLight.Green -> CityNodeDone to R.string.lab_light_green
                    TrafficLight.Yellow -> AccentYellow to R.string.lab_light_yellow
                    TrafficLight.Red -> LightRed to R.string.lab_light_red
                }
                Box(Modifier.size(14.dp).clip(CircleShape).background(color))
                Spacer(Modifier.width(8.dp))
                ResultText(stringResource(R.string.lab_light, stringResource(word)))
            }
            is LabResult.Total -> ResultText(stringResource(R.string.lab_total, result.total))
            is LabResult.Floors -> ResultText(
                pluralStringResource(R.plurals.lab_floors, result.floors.size, result.floors.joinToString(", "), result.floors.size)
            )
            LabResult.EmptyList -> ResultText(stringResource(R.string.lab_robot_empty))
            is LabResult.RobotOutput -> ResultText(stringResource(R.string.lab_robot_result, result.output))
            is LabResult.Prices -> result.lines.forEach { (purchase, pays) ->
                ResultText(stringResource(R.string.lab_price_line, purchase, pays))
            }
            is LabResult.Npc -> {
                // Fila de casillas: 🧙 en 0, 🧑 en la distancia de prueba, "·" en el resto
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    (0..NpcRulesLab.MAX_DISTANCE).forEach { cell ->
                        val symbol = when (cell) {
                            result.distance -> "🧑"
                            0 -> "🧙"
                            else -> "·"
                        }
                        Text(symbol, style = AppTextStyles.SmallBody, color = Ink)
                    }
                }
                val action = when (result.action) {
                    NpcAction.Flee -> R.string.lab_npc_flee
                    NpcAction.Greet -> R.string.lab_npc_greet
                    NpcAction.Wander -> R.string.lab_npc_wander
                }
                ResultText(stringResource(R.string.lab_npc_result, result.distance, stringResource(action)))
            }
        }
    }
}

@Composable
private fun ResultText(text: String) {
    Text(text = text, style = AppTextStyles.SmallBody, color = Ink)
}

// ---------- Descubrimientos / objetivos ----------

@Composable
internal fun GoalList(activityId: String, goalIds: List<String>, done: Set<String>) {
    val doneText = stringResource(R.string.lab_goal_done)
    val pendingText = stringResource(R.string.lab_goal_pending)
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        goalIds.forEach { goalId ->
            val text = stringResource(LabText.goal(activityId, goalId))
            val isDone = goalId in done
            Row(
                modifier = Modifier.semantics(mergeDescendants = true) {
                    contentDescription = "$text, ${if (isDone) doneText else pendingText}"
                },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(if (isDone) "✅" else "⬜", style = AppTextStyles.SmallBody)
                Spacer(Modifier.width(10.dp))
                Text(text = text, style = AppTextStyles.SmallBody, color = if (isDone) Ink else Muted)
            }
        }
    }
}
