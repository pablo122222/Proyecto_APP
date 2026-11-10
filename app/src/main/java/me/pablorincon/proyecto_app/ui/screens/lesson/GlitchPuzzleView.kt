package me.pablorincon.proyecto_app.ui.screens.lesson

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import me.pablorincon.proyecto_app.R
import me.pablorincon.proyecto_app.domain.GlitchAttempt
import me.pablorincon.proyecto_app.domain.GlitchMode
import me.pablorincon.proyecto_app.domain.GlitchPuzzle
import me.pablorincon.proyecto_app.domain.glitch.Cell
import me.pablorincon.proyecto_app.domain.glitch.Direction
import me.pablorincon.proyecto_app.domain.glitch.GlitchBlock
import me.pablorincon.proyecto_app.domain.glitch.GlitchBoard
import me.pablorincon.proyecto_app.domain.glitch.GlitchEditor
import me.pablorincon.proyecto_app.domain.glitch.GlitchStep
import me.pablorincon.proyecto_app.domain.glitch.ProgramError
import me.pablorincon.proyecto_app.domain.glitch.RunOutcome
import me.pablorincon.proyecto_app.ui.components.PrimaryButton
import me.pablorincon.proyecto_app.ui.theme.AppAppearance
import me.pablorincon.proyecto_app.ui.theme.AppDimens
import me.pablorincon.proyecto_app.ui.theme.motionMillis
import me.pablorincon.proyecto_app.ui.theme.AppTextStyles
import me.pablorincon.proyecto_app.ui.theme.ChoiceSelectedBackground
import me.pablorincon.proyecto_app.ui.theme.CodeBg
import me.pablorincon.proyecto_app.ui.theme.CodeLineNumber
import me.pablorincon.proyecto_app.ui.theme.FeedbackBadBg
import me.pablorincon.proyecto_app.ui.theme.FeedbackBadText
import me.pablorincon.proyecto_app.ui.theme.Ink
import me.pablorincon.proyecto_app.ui.theme.LabelInk
import me.pablorincon.proyecto_app.ui.theme.Line
import me.pablorincon.proyecto_app.ui.theme.Mint
import me.pablorincon.proyecto_app.ui.theme.Muted
import me.pablorincon.proyecto_app.ui.theme.Paper
import me.pablorincon.proyecto_app.ui.theme.Placeholder
import me.pablorincon.proyecto_app.ui.theme.Primary
import me.pablorincon.proyecto_app.ui.theme.PrimaryDark
import me.pablorincon.proyecto_app.ui.theme.PrimarySoft
import me.pablorincon.proyecto_app.ui.theme.SyntaxNumber

private val ChipShape = RoundedCornerShape(12.dp)
private val MaxCell = 56.dp
private const val STEP_MILLIS = 380L
private const val REDUCED_STEP_MILLIS = 150L // Con "Reducir animaciones": salta de casilla sin deslizarse

// Colores propios del tablero (no están en el mockup)
private val SelectedLineBg = Color(0x334B7BE5)
private val RunningLineBg = Color(0x40FFC46B)

// Simulador de Glitch. En las lecciones (freeRetries) se ejecuta las veces que haga falta y solo
// se envía el acierto, o "Saltar por ahora" para dejarlo al final de la cola. En los jefes cada
// ejecución fallida cuenta como fallo.
@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun GlitchPuzzleView(
    exercise: GlitchPuzzle,
    enabled: Boolean,
    freeRetries: Boolean,
    onSubmit: (Boolean) -> Unit
) {
    var editor by remember { mutableStateOf(exercise.newEditor()) }
    var attempt by remember { mutableStateOf<GlitchAttempt?>(null) }
    var runId by remember { mutableIntStateOf(0) }
    var frame by remember { mutableIntStateOf(-1) }        // Paso que se muestra (-1 = inicio)
    var running by remember { mutableStateOf(false) }
    var failed by remember { mutableStateOf(false) }       // Ya falló al menos una vez

    val steps = (attempt as? GlitchAttempt.Ran)?.run?.steps.orEmpty()
    val shown: GlitchStep? = steps.getOrNull(frame)
    val finished = attempt != null && !running
    val editable = enabled && !running

    fun edit(change: (GlitchEditor) -> GlitchEditor) {
        editor = change(editor)
        attempt = null   // Al cambiar el programa, Glitch vuelve a la salida
        frame = -1
    }

    fun finish(result: GlitchAttempt) {
        if (result.isSuccess) {
            onSubmit(true)
        } else {
            failed = true
            if (!freeRetries) onSubmit(false)
        }
    }

    LaunchedEffect(runId) {
        val result = attempt ?: return@LaunchedEffect
        if (result is GlitchAttempt.Ran) {
            running = true
            for (index in result.run.steps.indices) {
                frame = index
                delay(if (AppAppearance.reduceMotion) REDUCED_STEP_MILLIS else STEP_MILLIS)
            }
            running = false
        }
        finish(result)
    }

    Hint(
        when (exercise.mode) {
            GlitchMode.BUILD -> R.string.glitch_hint_build
            GlitchMode.COMPLETE -> R.string.glitch_hint_complete
            GlitchMode.FIX -> R.string.glitch_hint_fix
        }
    )

    BoardView(
        board = exercise.parsedBoard,
        cell = shown?.cell ?: exercise.parsedBoard.start,
        direction = shown?.direction ?: exercise.parsedBoard.startDirection,
        batteries = shown?.batteriesLeft ?: exercise.parsedBoard.batteries,
        bump = shown?.bump == true,
        celebrating = finished && attempt?.isSuccess == true
    )

    Spacer(Modifier.height(12.dp))
    ProgramView(
        editor = editor,
        runningLine = if (running) shown?.line else null,
        enabled = editable,
        onSelect = { index -> editor = editor.select(index) },
        onCycleNumber = { index -> edit { it.cycleNumber(index) } }
    )

    if (enabled) {
        Spacer(Modifier.height(10.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            exercise.palette.forEach { block ->
                val canUse = editable && (editor.canAdd || !editor.freeEditing)
                CodeChip(text = block.code, enabled = canUse) { edit { it.tapBlock(block) } }
            }
        }
        if (editor.freeEditing) {
            Spacer(Modifier.height(8.dp))
            val canTool = editable && editor.canEditSelected
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                ToolChip(stringResource(R.string.glitch_outdent), canTool) { edit { it.outdent() } }
                ToolChip(stringResource(R.string.glitch_indent), canTool) { edit { it.indent() } }
                ToolChip(stringResource(R.string.glitch_delete), canTool) { edit { it.delete() } }
            }
        }
        exercise.maxLines?.let { max ->
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.glitch_lines_counter, editor.lines.size, max),
                style = AppTextStyles.RowSubtitle,
                color = if (editor.lines.size > max) FeedbackBadText else Muted
            )
        }
    }

    // Lo que pasó (solo los fallos: el acierto lo celebra la tarjeta del ejercicio)
    val result = attempt
    if (finished && result != null && !result.isSuccess) {
        Spacer(Modifier.height(10.dp))
        Text(
            text = attemptMessage(result),
            style = AppTextStyles.SmallBody,
            color = FeedbackBadText,
            modifier = Modifier
                .fillMaxWidth()
                .clip(ChipShape)
                .background(FeedbackBadBg)
                .padding(12.dp)
        )
    }

    if (enabled) {
        Spacer(Modifier.height(12.dp))
        PrimaryButton(
            text = stringResource(if (running) R.string.glitch_running else R.string.glitch_run),
            enabled = !running,
            onClick = {
                attempt = exercise.attempt(editor.lines)
                frame = -1
                runId++
            }
        )
        if (failed && freeRetries && !running) {
            Spacer(Modifier.height(10.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                TextAction(stringResource(R.string.glitch_reset)) {
                    edit { exercise.newEditor() }
                }
                TextAction(stringResource(R.string.glitch_skip)) { onSubmit(false) }
            }
        }
    }
}

@Composable
private fun attemptMessage(attempt: GlitchAttempt): String = when (attempt) {
    is GlitchAttempt.TooLong -> stringResource(R.string.glitch_too_long, attempt.lines, attempt.max)
    is GlitchAttempt.Invalid -> {
        val line = attempt.line + 1
        when (attempt.error) {
            ProgramError.EMPTY -> stringResource(R.string.glitch_error_empty)
            ProgramError.HOLE -> stringResource(R.string.glitch_error_hole, line)
            ProgramError.UNEXPECTED_INDENT -> stringResource(R.string.glitch_error_indent, line)
            ProgramError.EMPTY_BODY -> stringResource(R.string.glitch_error_empty_body, line)
            ProgramError.ELSE_WITHOUT_IF -> stringResource(R.string.glitch_error_else, line)
        }
    }
    is GlitchAttempt.Ran -> when (attempt.run.outcome) {
        RunOutcome.SUCCESS -> stringResource(R.string.glitch_outcome_success)
        RunOutcome.CRASH -> stringResource(R.string.glitch_outcome_crash, (attempt.run.steps.lastOrNull()?.line ?: 0) + 1)
        RunOutcome.NOT_AT_GOAL -> stringResource(R.string.glitch_outcome_not_at_goal)
        RunOutcome.MISSING_BATTERIES -> stringResource(R.string.glitch_outcome_missing_batteries)
        RunOutcome.TOO_MANY_STEPS -> stringResource(R.string.glitch_outcome_too_many_steps)
    }
}

// ---------- Tablero: casillas fijas y Glitch encima, deslizándose de una a otra ----------
@Composable
private fun BoardView(
    board: GlitchBoard,
    cell: Cell,
    direction: Direction,
    batteries: Set<Cell>,
    bump: Boolean,
    celebrating: Boolean
) {
    val description = stringResource(R.string.glitch_board_description)
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .semantics { contentDescription = description },
        contentAlignment = Alignment.Center
    ) {
        val gap = 4.dp
        val cellSize: Dp = minOf((maxWidth - gap * (board.width - 1)) / board.width, MaxCell)
        val stride = cellSize + gap
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(Paper)
                .border(AppDimens.BorderWidth, Line, RoundedCornerShape(16.dp))
                .padding(8.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(gap)) {
                repeat(board.height) { y ->
                    Row(horizontalArrangement = Arrangement.spacedBy(gap)) {
                        repeat(board.width) { x ->
                            BoardCell(board, Cell(x, y), batteries, cellSize)
                        }
                    }
                }
            }
            // Glitch
            val x by animateDpAsState(stride * cell.x, tween(motionMillis(STEP_MILLIS.toInt() - 60)), label = "glitchX")
            val y by animateDpAsState(stride * cell.y, tween(motionMillis(STEP_MILLIS.toInt() - 60)), label = "glitchY")
            val angle by animateFloatAsState(direction.angle(), tween(motionMillis(200)), label = "glitchAngle")
            Box(
                modifier = Modifier
                    .offset { IntOffset((x + if (bump) 3.dp else 0.dp).roundToPx(), y.roundToPx()) }
                    .size(cellSize),
                contentAlignment = Alignment.Center
            ) {
                Text(text = if (celebrating) "🤩" else "🤖", fontSize = (cellSize.value * 0.5f).sp)
                // Flecha que indica hacia dónde mira
                Box(Modifier.size(cellSize).rotate(angle), contentAlignment = Alignment.CenterEnd) {
                    Text(text = "▸", color = Primary, fontSize = (cellSize.value * 0.32f).sp, fontWeight = FontWeight.Bold)
                }
                if (bump) Text(text = "💥", fontSize = (cellSize.value * 0.4f).sp, modifier = Modifier.align(Alignment.TopEnd))
            }
        }
    }
}

@Composable
private fun BoardCell(board: GlitchBoard, cell: Cell, batteries: Set<Cell>, size: Dp) {
    val wall = cell in board.walls
    val goal = cell == board.goal
    Box(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(10.dp))
            .background(
                when {
                    wall -> LabelInk
                    goal -> Mint
                    else -> ChoiceSelectedBackground
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        val emoji = when {
            goal -> "🏁"
            cell in batteries -> "🔋"
            else -> null
        }
        if (emoji != null) Text(text = emoji, fontSize = (size.value * 0.42f).sp)
    }
}

// Ángulo de la flecha: 0° mira a la derecha (este).
private fun Direction.angle(): Float = when (this) {
    Direction.EAST -> 0f
    Direction.SOUTH -> 90f
    Direction.WEST -> 180f
    Direction.NORTH -> 270f
}

// ---------- Programa: líneas de código; se toca una para elegirla ----------
@Composable
private fun ProgramView(
    editor: GlitchEditor,
    runningLine: Int?,
    enabled: Boolean,
    onSelect: (Int) -> Unit,
    onCycleNumber: (Int) -> Unit
) {
    val shape = RoundedCornerShape(15.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(CodeBg)
            .padding(vertical = 8.dp)
    ) {
        if (editor.lines.isEmpty()) {
            Text(
                text = stringResource(R.string.glitch_empty_program),
                style = CodeTextStyle.copy(color = Placeholder),
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
            )
        }
        editor.lines.forEachIndexed { index, line ->
            val background = when {
                runningLine == index -> RunningLineBg
                editor.selected == index && enabled -> SelectedLineBg
                else -> Color.Transparent
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(background)
                    .clickable(enabled = enabled && !line.locked) { onSelect(index) }
                    .padding(horizontal = 14.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${index + 1}",
                    style = CodeTextStyle.copy(color = CodeLineNumber),
                    modifier = Modifier.width(22.dp)
                )
                Row(
                    modifier = Modifier.weight(1f).horizontalScroll(rememberScrollState()),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = PythonHighlighter.highlight(line.code),
                        style = CodeTextStyle.copy(
                            color = if (line.block == null) SyntaxNumber else CodeTextStyle.color
                        )
                    )
                }
                val repeat = line.block as? GlitchBlock.Repeat
                if (repeat != null && !line.locked && enabled) {
                    NumberChip(times = repeat.times) { onCycleNumber(index) }
                }
            }
        }
    }
}

@Composable
private fun NumberChip(times: Int, onClick: () -> Unit) {
    val description = stringResource(R.string.glitch_number_description)
    Text(
        text = if (times == 0) "? ↻" else "$times ↻",
        style = CodeTextStyle.copy(color = CodeBg, fontWeight = FontWeight.Bold),
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(SyntaxNumber)
            .semantics { contentDescription = description }
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 2.dp)
    )
}

@Composable
private fun CodeChip(text: String, enabled: Boolean, onClick: () -> Unit) {
    Text(
        text = text,
        style = CodeTextStyle.copy(color = if (enabled) Ink else Muted),
        modifier = Modifier
            .clip(ChipShape)
            .background(if (enabled) Paper else Line)
            .border(AppDimens.BorderWidth, if (enabled) Primary else Line, ChipShape)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    )
}

@Composable
private fun ToolChip(text: String, enabled: Boolean, onClick: () -> Unit) {
    Text(
        text = text,
        style = AppTextStyles.RowSubtitle.copy(fontWeight = FontWeight.SemiBold),
        color = if (enabled) PrimaryDark else Muted,
        modifier = Modifier
            .clip(ChipShape)
            .background(if (enabled) PrimarySoft else Line)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    )
}

@Composable
private fun TextAction(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        style = AppTextStyles.RowSubtitle.copy(fontWeight = FontWeight.SemiBold),
        color = Primary,
        modifier = Modifier
            .clip(ChipShape)
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 6.dp)
    )
}

