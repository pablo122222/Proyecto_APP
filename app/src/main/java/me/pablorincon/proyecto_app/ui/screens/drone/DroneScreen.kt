package me.pablorincon.proyecto_app.ui.screens.drone

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.delay
import me.pablorincon.proyecto_app.R
import me.pablorincon.proyecto_app.domain.glitch.Cell
import me.pablorincon.proyecto_app.domain.glitchcity.DroneBlock
import me.pablorincon.proyecto_app.domain.glitchcity.DroneDirection
import me.pablorincon.proyecto_app.domain.glitchcity.DroneMove
import me.pablorincon.proyecto_app.domain.glitchcity.DroneOutcome
import me.pablorincon.proyecto_app.domain.glitchcity.DronePython
import me.pablorincon.proyecto_app.ui.components.AchievementUnlockedDialog
import me.pablorincon.proyecto_app.ui.components.BackButton
import me.pablorincon.proyecto_app.ui.components.Pill
import me.pablorincon.proyecto_app.ui.components.PrimaryButton
import me.pablorincon.proyecto_app.ui.components.SecondaryButton
import me.pablorincon.proyecto_app.ui.components.SectionHeading
import me.pablorincon.proyecto_app.ui.screens.glitchcity.GlitchCityText
import me.pablorincon.proyecto_app.ui.screens.lab.WhySheet
import me.pablorincon.proyecto_app.ui.screens.lesson.CodeBlock
import me.pablorincon.proyecto_app.ui.theme.AnswerWrongBorder
import me.pablorincon.proyecto_app.ui.theme.AppBackground
import me.pablorincon.proyecto_app.ui.theme.AppDimens
import me.pablorincon.proyecto_app.ui.theme.AppShapes
import me.pablorincon.proyecto_app.ui.theme.AppTextStyles
import me.pablorincon.proyecto_app.ui.theme.ErrorRed
import me.pablorincon.proyecto_app.ui.theme.FeedbackBadBg
import me.pablorincon.proyecto_app.ui.theme.FeedbackBadText
import me.pablorincon.proyecto_app.ui.theme.FeedbackGoodBg
import me.pablorincon.proyecto_app.ui.theme.FeedbackGoodText
import me.pablorincon.proyecto_app.ui.theme.Ink
import me.pablorincon.proyecto_app.ui.theme.LockedPillBackground
import me.pablorincon.proyecto_app.ui.theme.Muted
import me.pablorincon.proyecto_app.ui.theme.Primary
import me.pablorincon.proyecto_app.ui.theme.PrimarySoft
import me.pablorincon.proyecto_app.ui.theme.PrimarySoftBorder
import me.pablorincon.proyecto_app.ui.theme.Yellow
import me.pablorincon.proyecto_app.ui.theme.YellowInk
import me.pablorincon.proyecto_app.ui.theme.motionMillis

// Pantalla "Actividad" en modo nivel del dron (guía §5.3): cuadrícula 5×5, paleta de movimientos,
// programa, ejecutar y resultado. Mantiene activa la pestaña Retos.
@Composable
fun DroneScreen(
    levelId: String,
    onBack: () -> Unit,
    onOpenAchievements: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: DroneViewModel = viewModel(
        key = levelId,
        factory = viewModelFactory { initializer { DroneViewModel(levelId) } }
    )
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val ready = state as? DroneUiState.Ready

    // La animación avanza casilla por casilla; los avisos esperan a que termine
    // (Cada ejecución reinicia el contador; con "Reducir animaciones" se ve el final de una vez.)
    val path = ready?.attempt?.run?.path.orEmpty()
    val attemptId = ready?.attempt?.id
    val stepMillis = motionMillis(STEP_MILLIS)
    var visibleSteps by remember(attemptId) { mutableIntStateOf(if (stepMillis == 0) path.size else minOf(1, path.size)) }
    LaunchedEffect(attemptId) {
        while (visibleSteps < path.size) {
            delay(stepMillis.toLong())
            visibleSteps += 1
        }
    }
    val animationDone = visibleSteps >= path.size
    // Al ejecutar se sube hasta el mapa: el vuelo, la línea en curso y el resultado se ven juntos
    // (el botón Ejecutar queda más abajo)
    val scroll = rememberScrollState()
    LaunchedEffect(attemptId) {
        if (attemptId != null) scroll.animateScrollTo(0)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppBackground)
            .statusBarsPadding()
            .verticalScroll(scroll)
            .navigationBarsPadding()
            .padding(
                start = AppDimens.ScreenHorizontal,
                end = AppDimens.ScreenHorizontal,
                top = AppDimens.ScreenTop,
                bottom = AppDimens.ScreenBottom // Libre para la barra de pestañas
            )
    ) {
        when (val s = state) {
            DroneUiState.Loading -> {
                BackButton(onClick = onBack)
                val loading = stringResource(R.string.city_loading)
                Box(Modifier.fillMaxWidth().padding(top = 48.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Primary, modifier = Modifier.semantics { contentDescription = loading })
                }
            }
            DroneUiState.Error -> Message(R.string.city_error, onBack, onRetry = viewModel::load)
            DroneUiState.Locked -> Message(R.string.drone_locked, onBack)
            is DroneUiState.Ready -> ReadyBody(
                state = s,
                visibleSteps = if (s.attempt == null) 0 else visibleSteps,
                animationDone = animationDone,
                actions = DroneActions(
                    onBack = onBack,
                    onRepeat = viewModel::selectRepeat,
                    onAdd = viewModel::addBlock,
                    onRemoveLast = viewModel::removeLast,
                    onClear = viewModel::clear,
                    onIdea = viewModel::toggleIdea,
                    onRun = viewModel::execute
                )
            )
        }
    }

    // Avisos cortos (toast de la guía §0), al terminar la animación
    val toastText = when (val message = ready?.message) {
        is DroneMessage.Rewarded -> stringResource(R.string.drone_reward, message.reward.xp, message.reward.gems)
        DroneMessage.NoConnection -> stringResource(R.string.drone_save_no_connection)
        DroneMessage.Failed -> stringResource(R.string.drone_save_failed)
        null -> null
    }
    LaunchedEffect(toastText, animationDone) {
        if (toastText != null && animationDone) {
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()
            viewModel.onMessageShown()
        }
    }
    val why = ready?.why
    if (why != null && animationDone) {
        WhySheet(state = why, onAnswer = viewModel::answerWhy, onClose = viewModel::closeWhy)
    }
    // Logros ganados; esperan a que se cierre la pregunta
    AchievementUnlockedDialog(
        unlocks = if (why == null && animationDone) ready?.unlocks.orEmpty() else emptyList(),
        onSeeAchievements = {
            viewModel.onUnlocksSeen()
            onOpenAchievements()
        },
        onDismiss = viewModel::onUnlocksSeen
    )
}

private const val STEP_MILLIS = 250
private const val MAX_STARS = 3

private class DroneActions(
    val onBack: () -> Unit,
    val onRepeat: (Int) -> Unit,
    val onAdd: (DroneMove) -> Unit,
    val onRemoveLast: () -> Unit,
    val onClear: () -> Unit,
    val onIdea: () -> Unit,
    val onRun: () -> Unit
)

// Mensaje a pantalla completa (error o nivel bloqueado) con botón atrás.
@Composable
private fun Message(text: Int, onBack: () -> Unit, onRetry: (() -> Unit)? = null) {
    BackButton(onClick = onBack)
    Spacer(Modifier.height(AppDimens.TopLineBottom))
    Text(
        text = stringResource(text),
        style = AppTextStyles.SmallBody,
        color = Muted,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
    if (onRetry != null) {
        Spacer(Modifier.height(AppDimens.ErrorToButton))
        PrimaryButton(text = stringResource(R.string.startup_retry), onClick = onRetry)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ReadyBody(state: DroneUiState.Ready, visibleSteps: Int, animationDone: Boolean, actions: DroneActions) {
    val level = state.level
    // topline: atrás · "Competir · Nivel n"
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        BackButton(onClick = actions.onBack)
        Spacer(Modifier.weight(1f))
        Pill(text = stringResource(R.string.drone_label, state.number))
    }
    Spacer(Modifier.height(AppDimens.TopLineBottom))
    Text(
        text = stringResource(GlitchCityText.droneLevelNames.getValue(level.id)),
        style = AppTextStyles.ScreenTitleSmall,
        color = Ink
    )
    Spacer(Modifier.height(AppDimens.TitleBottom))
    val subtitle = stringResource(R.string.drone_subtitle)
    Text(
        text = level.maxBlocks?.let { stringResource(R.string.drone_subtitle_max, subtitle, it) } ?: subtitle,
        style = AppTextStyles.Subtitle,
        color = Muted
    )
    state.record?.let { record ->
        Spacer(Modifier.height(10.dp))
        Text(
            text = pluralStringResource(R.plurals.drone_record, record, record, stars(level.starsFor(record))),
            style = AppTextStyles.RowTitle,
            color = Primary
        )
    }

    Spacer(Modifier.height(16.dp))
    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        // Tamaño limitado: así el mapa y el programa caben juntos en pantalla
        Grid(state, visibleSteps, animationDone, Modifier.widthIn(max = GridMaxWidth))
    }

    // Justo bajo el mapa, para ver a la vez el dron, la línea que lo mueve y el resultado
    if (state.program.blocks.isNotEmpty()) {
        Spacer(Modifier.height(16.dp))
        PythonView(state, visibleSteps, animationDone)
    }
    val attempt = state.attempt
    if (attempt != null && animationDone) {
        Spacer(Modifier.height(12.dp))
        Feedback(attempt)
    }

    // Repetir: ×1 ×2 ×3 ×4
    Spacer(Modifier.height(16.dp))
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(stringResource(R.string.drone_repeat), style = AppTextStyles.RowTitle, color = Ink)
        (1..DroneBlock.MAX_REPEAT).forEach { times ->
            val active = times == state.repeat
            val description = pluralStringResource(R.plurals.drone_repeat_description, times, times)
            SmallChip(
                text = stringResource(R.string.drone_repeat_option, times),
                active = active,
                modifier = Modifier.semantics {
                    contentDescription = description
                    selected = active
                },
                onClick = { actions.onRepeat(times) }
            )
        }
    }

    // Paleta de movimientos
    Spacer(Modifier.height(12.dp))
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        DroneMove.entries.forEach { move ->
            val description = stringResource(moveDescription(move))
            SmallChip(
                text = symbol(move),
                active = false,
                enabled = state.program.canAdd,
                modifier = Modifier.semantics { contentDescription = description },
                onClick = { actions.onAdd(move) }
            )
        }
    }

    // Tu programa
    SectionHeading(
        title = pluralStringResource(R.plurals.drone_program_title, state.program.blocks.size, state.program.blocks.size)
    )
    if (state.program.blocks.isEmpty()) {
        Text(stringResource(R.string.drone_program_empty), style = AppTextStyles.SmallBody, color = Muted)
    } else {
        FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            state.program.blocks.forEach { block ->
                Pill(text = symbol(block.move) + if (block.times > 1) "×${block.times}" else "")
            }
        }
    }

    Spacer(Modifier.height(16.dp))
    PrimaryButton(text = stringResource(R.string.drone_run), onClick = actions.onRun)
    Spacer(Modifier.height(10.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        SecondaryButton(stringResource(R.string.drone_remove_last), actions.onRemoveLast, Modifier.weight(1f))
        SecondaryButton(stringResource(R.string.drone_clear), actions.onClear, Modifier.weight(1f))
        SecondaryButton(stringResource(R.string.drone_idea), actions.onIdea, Modifier.weight(1f))
    }

    if (state.showIdea) {
        Spacer(Modifier.height(12.dp))
        Box(
            Modifier
                .fillMaxWidth()
                .clip(FeedbackShape)
                .background(Yellow)
                .padding(12.dp)
        ) {
            Text(
                text = stringResource(R.string.drone_idea_text, stringResource(ideaOf(level.id))),
                style = AppTextStyles.SmallBody,
                color = YellowInk
            )
        }
    }
}

private val CellShape = RoundedCornerShape(10.dp)
private val GridMaxWidth = 300.dp
private val FeedbackShape = RoundedCornerShape(12.dp)
private val PathColor: Color get() = PrimarySoftBorder        // #C5D6F7: camino recorrido
private val CrashColor: Color get() = AnswerWrongBorder       // #F3A9A6: choque

// Cuadrícula 5×5: libre, muro, camino recorrido y choque; 🚁 donde va el dron y 📦 en la meta.
@Composable
private fun Grid(state: DroneUiState.Ready, visibleSteps: Int, animationDone: Boolean, modifier: Modifier = Modifier) {
    val map = state.level.map
    val run = state.attempt?.run
    val visible = run?.path.orEmpty().take(visibleSteps)
    val drone = visible.lastOrNull() ?: map.start
    val crash = run?.crash?.takeIf { animationDone }
    val description = stringResource(R.string.drone_map_description, drone.y + 1, drone.x + 1, map.goal.y + 1, map.goal.x + 1)
    Column(
        modifier = modifier.clearAndSetSemantics {
            contentDescription = description
            liveRegion = LiveRegionMode.Polite
        },
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        (0 until map.height).forEach { y ->
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                (0 until map.width).forEach { x ->
                    val cell = Cell(x, y)
                    val color = when {
                        cell in map.walls -> if (cell == crash) CrashColor else Ink
                        cell in visible && visible.size > 1 -> PathColor
                        else -> LockedPillBackground // #EEF1F8: libre
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .clip(CellShape)
                            .background(color),
                        contentAlignment = Alignment.Center
                    ) {
                        when (cell) {
                            drone -> Text("🚁", fontSize = 22.sp)
                            map.goal -> Text("📦", fontSize = 22.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SmallChip(
    text: String,
    active: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Text(
        text = text,
        style = AppTextStyles.SmallButton,
        color = when {
            active -> Color.White
            enabled -> Primary
            else -> Primary.copy(alpha = 0.4f)
        },
        modifier = modifier
            .clip(AppShapes.SmallButton)
            .background(if (active) Primary else PrimarySoft)
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 9.dp)
    )
}

// Mensaje del resultado: verde si entregó, rojo si no.
@Composable
private fun Feedback(attempt: DroneAttempt) {
    val outcome = attempt.run.outcome
    val good = outcome is DroneOutcome.Delivered
    val main = when (outcome) {
        DroneOutcome.Empty -> stringResource(R.string.drone_empty)
        DroneOutcome.OutOfMap -> stringResource(R.string.drone_out_of_map)
        DroneOutcome.HitWall -> stringResource(R.string.drone_wall)
        DroneOutcome.WrongCell -> stringResource(R.string.drone_wrong_cell)
        is DroneOutcome.TooManyBlocks -> stringResource(R.string.drone_too_many, outcome.blocks, outcome.max)
        is DroneOutcome.Delivered -> pluralStringResource(R.plurals.drone_delivered, outcome.blocks, outcome.blocks, stars(outcome.stars))
    }
    val note = when (attempt.recordNote) {
        RecordNote.NewRecord -> stringResource(R.string.drone_new_record)
        RecordNote.NotBetter -> attempt.previousRecord?.let { pluralStringResource(R.plurals.drone_not_better, it, it) }
        null -> null
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(FeedbackShape)
            .background(if (good) FeedbackGoodBg else FeedbackBadBg)
            .semantics(mergeDescendants = true) { liveRegion = LiveRegionMode.Polite }
            .padding(12.dp)
    ) {
        val color = if (good) FeedbackGoodText else FeedbackBadText
        Text(main, style = AppTextStyles.SmallBody, color = color)
        if (note != null) {
            Spacer(Modifier.height(4.dp))
            Text(note, style = AppTextStyles.RowTitle, color = color)
        }
    }
}

private fun stars(count: Int): String = "★".repeat(count) + "☆".repeat(MAX_STARS - count)

private fun symbol(move: DroneMove): String = when (move) {
    DroneMove.UP -> "↑"
    DroneMove.DOWN -> "↓"
    DroneMove.LEFT -> "←"
    DroneMove.RIGHT -> "→"
    DroneMove.RIGHT_DOWN -> "→↓"
    DroneMove.DOWN_RIGHT -> "↓→"
}

private fun moveDescription(move: DroneMove): Int = when (move) {
    DroneMove.UP -> R.string.drone_move_up
    DroneMove.DOWN -> R.string.drone_move_down
    DroneMove.LEFT -> R.string.drone_move_left
    DroneMove.RIGHT -> R.string.drone_move_right
    DroneMove.RIGHT_DOWN -> R.string.drone_move_right_down
    DroneMove.DOWN_RIGHT -> R.string.drone_move_down_right
}

private fun ideaOf(levelId: String): Int = when (levelId) {
    "d1" -> R.string.drone_idea_d1
    "d2" -> R.string.drone_idea_d2
    "d3" -> R.string.drone_idea_d3
    "d4" -> R.string.drone_idea_d4
    else -> R.string.drone_idea_d5
}

// El programa en Python: mientras el dron vuela se resalta la línea que lo mueve (en rojo si
// en esa línea choca o se sale) y debajo se explica qué hace.
@Composable
private fun PythonView(state: DroneUiState.Ready, visibleSteps: Int, animationDone: Boolean) {
    val code = remember(state.program) { DronePython.of(state.program.blocks) }
    val run = state.attempt?.run
    val step = run?.let { DronePython.stepAt(code, it, visibleSteps, animationDone) }
    val failed = run?.outcome == DroneOutcome.OutOfMap || run?.outcome == DroneOutcome.HitWall
    val failing = failed && animationDone

    Text(stringResource(R.string.drone_python_title), style = AppTextStyles.RowTitle, color = Ink)
    Spacer(Modifier.height(8.dp))
    CodeBlock(
        code = code.lines.joinToString("\n"),
        markedLine = step?.line,
        // Azul mientras vuela; rojo en la línea donde choca o se sale
        markColor = if (failing) ErrorRed.copy(alpha = 0.55f) else Primary.copy(alpha = 0.5f)
    )
    Spacer(Modifier.height(8.dp))
    val caption = when {
        step == null -> stringResource(R.string.drone_python_intro)
        failing && run?.outcome == DroneOutcome.OutOfMap -> stringResource(R.string.drone_python_out_of_map)
        failing -> stringResource(R.string.drone_python_wall)
        step.times > 1 -> stringResource(
            R.string.drone_python_loop_step, step.iteration, step.times, step.direction.function, stringResource(directionText(step.direction))
        )
        else -> stringResource(R.string.drone_python_step, step.direction.function, stringResource(directionText(step.direction)))
    }
    Text(
        text = caption,
        style = AppTextStyles.SmallBody,
        color = if (failing) FeedbackBadText else Muted,
        modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
    )
}

private fun directionText(direction: DroneDirection): Int = when (direction) {
    DroneDirection.Up -> R.string.drone_dir_up
    DroneDirection.Down -> R.string.drone_dir_down
    DroneDirection.Left -> R.string.drone_dir_left
    DroneDirection.Right -> R.string.drone_dir_right
}
