package me.pablorincon.proyecto_app.ui.screens.lesson

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.pablorincon.proyecto_app.R
import me.pablorincon.proyecto_app.domain.Exercise
import me.pablorincon.proyecto_app.domain.FillBlanks
import me.pablorincon.proyecto_app.domain.FindBug
import me.pablorincon.proyecto_app.domain.LessonQuiz
import me.pablorincon.proyecto_app.domain.MultipleChoice
import me.pablorincon.proyecto_app.domain.OrderLines
import me.pablorincon.proyecto_app.domain.PredictOutput
import me.pablorincon.proyecto_app.domain.TypeCode
import me.pablorincon.proyecto_app.ui.components.Pill
import me.pablorincon.proyecto_app.ui.components.PrimaryButton
import me.pablorincon.proyecto_app.ui.theme.AnswerCorrectBg
import me.pablorincon.proyecto_app.ui.theme.AnswerCorrectBorder
import me.pablorincon.proyecto_app.ui.theme.AnswerWrongBg
import me.pablorincon.proyecto_app.ui.theme.AnswerWrongBorder
import me.pablorincon.proyecto_app.ui.theme.AppDimens
import me.pablorincon.proyecto_app.ui.theme.AppTextStyles
import me.pablorincon.proyecto_app.ui.theme.ChoiceSelectedBackground
import me.pablorincon.proyecto_app.ui.theme.CodeBg
import me.pablorincon.proyecto_app.ui.theme.CodeText
import me.pablorincon.proyecto_app.ui.theme.FeedbackBadBg
import me.pablorincon.proyecto_app.ui.theme.FeedbackBadText
import me.pablorincon.proyecto_app.ui.theme.FeedbackGoodBg
import me.pablorincon.proyecto_app.ui.theme.FeedbackGoodText
import me.pablorincon.proyecto_app.ui.theme.Ink
import me.pablorincon.proyecto_app.ui.theme.Line
import me.pablorincon.proyecto_app.ui.theme.Muted
import me.pablorincon.proyecto_app.ui.theme.Paper
import me.pablorincon.proyecto_app.ui.theme.Placeholder
import me.pablorincon.proyecto_app.ui.theme.Primary
import me.pablorincon.proyecto_app.ui.theme.SyntaxNumber

private val ItemShape = RoundedCornerShape(12.dp)

// Ejercicio actual de la lección: etiqueta del tipo, enunciado, el ejercicio en sí,
// retroalimentación y botón para continuar. Cada vuelta (step) reinicia su estado.
@Composable
internal fun ExerciseCard(quiz: LessonQuiz, onSubmit: (Boolean) -> Unit, onNext: () -> Unit) {
    val exercise = quiz.current ?: return
    val haptic = LocalHapticFeedback.current
    val submit: (Boolean) -> Unit = { correct ->
        haptic.performHapticFeedback(if (correct) HapticFeedbackType.Confirm else HapticFeedbackType.Reject)
        onSubmit(correct)
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Pill(text = stringResource(kindLabel(exercise)))
        Spacer(Modifier.width(8.dp))
        Text(
            text = stringResource(R.string.lesson_correct_counter, quiz.correctCount, quiz.total),
            style = AppTextStyles.RowSubtitle,
            color = Muted
        )
    }
    Spacer(Modifier.height(10.dp))
    Text(text = exercise.prompt, style = AppTextStyles.Subtitle.copy(fontWeight = FontWeight.SemiBold), color = Ink)
    Spacer(Modifier.height(12.dp))

    key(quiz.step) {
        val enabled = !quiz.isAnswered
        when (exercise) {
            is MultipleChoice -> MultipleChoiceView(exercise, quiz.step, enabled, submit)
            is OrderLines -> OrderLinesView(exercise, quiz.step, enabled, quiz.lastResult, submit)
            is FillBlanks -> FillBlanksView(exercise, quiz.step, enabled, quiz.lastResult, submit)
            is FindBug -> FindBugView(exercise, enabled, quiz.lastResult, submit)
            is PredictOutput -> PredictOutputView(exercise, enabled, submit)
            is TypeCode -> TypeCodeView(exercise, enabled, submit)
        }
    }

    if (quiz.isAnswered) {
        Spacer(Modifier.height(12.dp))
        Feedback(
            good = quiz.isLastCorrect,
            text = if (quiz.isLastCorrect) exercise.correctFeedback else exercise.wrongFeedback
        )
        if (!quiz.isLastCorrect) {
            Spacer(Modifier.height(6.dp))
            Text(text = stringResource(R.string.lesson_retry_later), style = AppTextStyles.RowSubtitle, color = Muted)
        }
        Spacer(Modifier.height(12.dp))
        PrimaryButton(text = stringResource(R.string.lesson_continue), onClick = onNext)
    }
}

private fun kindLabel(exercise: Exercise): Int = when (exercise) {
    is MultipleChoice -> R.string.exercise_kind_quick
    is OrderLines -> R.string.exercise_kind_order
    is FillBlanks -> R.string.exercise_kind_fill
    is FindBug -> R.string.exercise_kind_bug
    is PredictOutput -> R.string.exercise_kind_predict
    is TypeCode -> R.string.exercise_kind_type
}

// ---------- Opción múltiple (se usa muy poco) ----------
@Composable
private fun MultipleChoiceView(
    exercise: MultipleChoice,
    step: Int,
    enabled: Boolean,
    onSubmit: (Boolean) -> Unit
) {
    var selected by remember { mutableStateOf<Int?>(null) }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        exercise.displayOrder(step).forEach { index ->
            val (border, fill) = when {
                selected != index -> Line to Paper
                exercise.isCorrect(index) -> AnswerCorrectBorder to AnswerCorrectBg
                else -> AnswerWrongBorder to AnswerWrongBg
            }
            Text(
                text = exercise.options[index],
                style = AppTextStyles.ChoiceTitle.copy(fontWeight = FontWeight(600)),
                color = Ink,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(ItemShape)
                    .background(fill)
                    .border(AppDimens.BorderWidth, border, ItemShape)
                    .clickable(enabled = enabled) {
                        selected = index
                        onSubmit(exercise.isCorrect(index))
                    }
                    .padding(12.dp)
            )
        }
    }
}

// ---------- Ordena el código: se tocan dos líneas para intercambiarlas ----------
@Composable
private fun OrderLinesView(
    exercise: OrderLines,
    step: Int,
    enabled: Boolean,
    result: Boolean?,
    onSubmit: (Boolean) -> Unit
) {
    val order = remember { mutableStateListOf<Int>().apply { addAll(exercise.initialOrder(step)) } }
    var picked by remember { mutableStateOf<Int?>(null) }

    Hint(R.string.exercise_hint_order)
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        order.forEachIndexed { position, lineIndex ->
            val isPicked = picked == position
            val border = when {
                result == true -> AnswerCorrectBorder
                result == false -> AnswerWrongBorder
                isPicked -> Primary
                else -> Line
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(ItemShape)
                    .background(if (isPicked) ChoiceSelectedBackground else CodeBg)
                    .border(if (isPicked) 2.dp else AppDimens.BorderWidth, border, ItemShape)
                    .clickable(enabled = enabled) {
                        val first = picked
                        when (first) {
                            null -> picked = position
                            position -> picked = null
                            else -> {
                                val tmp = order[first]
                                order[first] = order[position]
                                order[position] = tmp
                                picked = null
                            }
                        }
                    }
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${position + 1}",
                    style = CodeTextStyle.copy(color = SyntaxNumber),
                    modifier = Modifier.width(22.dp)
                )
                Text(
                    text = PythonHighlighter.highlight(exercise.lines[lineIndex]),
                    style = CodeTextStyle
                )
            }
        }
    }
    if (enabled) {
        Spacer(Modifier.height(12.dp))
        PrimaryButton(
            text = stringResource(R.string.exercise_check),
            onClick = { onSubmit(exercise.isCorrect(order.map { exercise.lines[it] })) }
        )
    }
}

// ---------- Completa el hueco: fichas para rellenar los "___" ----------
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FillBlanksView(
    exercise: FillBlanks,
    step: Int,
    enabled: Boolean,
    result: Boolean?,
    onSubmit: (Boolean) -> Unit
) {
    // Ficha (índice en exercise.chips) puesta en cada hueco; null = vacío
    val placed = remember { mutableStateListOf<Int?>().apply { repeat(exercise.blankCount) { add(null) } } }

    Hint(R.string.exercise_hint_fill)
    Text(
        text = fillCode(exercise, placed),
        style = CodeTextStyle,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(CodeBg)
            .border(
                AppDimens.BorderWidth,
                when (result) {
                    true -> AnswerCorrectBorder
                    false -> AnswerWrongBorder
                    null -> CodeBg
                },
                RoundedCornerShape(15.dp)
            )
            .padding(horizontal = 14.dp, vertical = 12.dp)
    )
    Spacer(Modifier.height(12.dp))
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        exercise.chipOrder(step).forEach { chipIndex ->
            val used = chipIndex in placed
            Text(
                text = exercise.chips[chipIndex],
                style = CodeTextStyle.copy(color = if (used) Muted else Ink),
                modifier = Modifier
                    .clip(ItemShape)
                    .background(if (used) Line else Paper)
                    .border(AppDimens.BorderWidth, if (used) Line else Primary, ItemShape)
                    .clickable(enabled = enabled) {
                        val holder = placed.indexOf(chipIndex)
                        if (holder >= 0) {
                            placed[holder] = null // Tocar una ficha ya puesta la devuelve
                        } else {
                            val empty = placed.indexOf(null)
                            if (empty >= 0) placed[empty] = chipIndex
                        }
                    }
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            )
        }
    }
    if (enabled) {
        Spacer(Modifier.height(12.dp))
        PrimaryButton(
            text = stringResource(R.string.exercise_check),
            onClick = { onSubmit(exercise.isCorrect(placed.map { it?.let { chip -> exercise.chips[chip] } })) }
        )
    }
}

// Código con los huecos vacíos como [ ___ ] y los rellenos con su ficha.
private fun fillCode(exercise: FillBlanks, placed: List<Int?>): AnnotatedString = buildAnnotatedString {
    val parts = exercise.codeTemplate.split(FillBlanks.BLANK)
    parts.forEachIndexed { index, part ->
        append(PythonHighlighter.highlight(part))
        if (index < parts.lastIndex) {
            val chip = placed[index]?.let { exercise.chips[it] }
            val start = length
            append(if (chip == null) "[ ___ ]" else "[ $chip ]")
            addStyle(
                SpanStyle(color = SyntaxNumber, fontWeight = FontWeight.Bold),
                start, length
            )
        }
    }
}

// ---------- Caza el bug: se toca la línea con el error ----------
@Composable
private fun FindBugView(exercise: FindBug, enabled: Boolean, result: Boolean?, onSubmit: (Boolean) -> Unit) {
    var selected by remember { mutableStateOf<Int?>(null) }
    Hint(R.string.exercise_hint_bug)
    CodeBlock(
        code = exercise.codeLines.joinToString("\n"),
        onLineClick = if (enabled) { index ->
            selected = index
            onSubmit(exercise.isCorrect(index))
        } else null,
        markedLine = selected,
        markedGood = result
    )
}

// ---------- Predice la salida ----------
@Composable
private fun PredictOutputView(exercise: PredictOutput, enabled: Boolean, onSubmit: (Boolean) -> Unit) {
    var typed by remember { mutableStateOf("") }
    CodeBlock(code = exercise.code)
    Spacer(Modifier.height(12.dp))
    Hint(R.string.exercise_hint_predict)
    CodeField(
        value = typed,
        onValueChange = { typed = it },
        placeholder = stringResource(R.string.exercise_predict_placeholder),
        singleLine = false,
        enabled = enabled
    )
    if (enabled) {
        Spacer(Modifier.height(12.dp))
        PrimaryButton(
            text = stringResource(R.string.exercise_check),
            enabled = typed.isNotBlank(),
            onClick = { onSubmit(exercise.isCorrect(typed)) }
        )
    }
}

// ---------- Escribe el código ----------
@Composable
private fun TypeCodeView(exercise: TypeCode, enabled: Boolean, onSubmit: (Boolean) -> Unit) {
    var typed by remember { mutableStateOf("") }
    Hint(R.string.exercise_hint_type)
    CodeField(
        value = typed,
        onValueChange = { typed = it },
        placeholder = exercise.placeholder,
        singleLine = true,
        enabled = enabled,
        onDone = { if (typed.isNotBlank()) onSubmit(exercise.isCorrect(typed)) }
    )
    if (enabled) {
        Spacer(Modifier.height(12.dp))
        PrimaryButton(
            text = stringResource(R.string.exercise_check),
            enabled = typed.isNotBlank(),
            onClick = { onSubmit(exercise.isCorrect(typed)) }
        )
    }
}

// Campo de texto con aspecto de terminal (fondo oscuro, monoespaciado, sin autocorrección).
@Composable
private fun CodeField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    singleLine: Boolean,
    enabled: Boolean,
    onDone: (() -> Unit)? = null
) {
    val textStyle = CodeTextStyle.copy(fontSize = 13.sp, lineHeight = 20.sp, fontFamily = FontFamily.Monospace)
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        singleLine = singleLine,
        textStyle = textStyle,
        cursorBrush = SolidColor(CodeText),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.None,
            autoCorrectEnabled = false,
            imeAction = if (singleLine) ImeAction.Done else ImeAction.Default
        ),
        keyboardActions = androidx.compose.foundation.text.KeyboardActions(onDone = { onDone?.invoke() }),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(CodeBg)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        decorationBox = { inner ->
            Box {
                if (value.isEmpty()) {
                    Text(text = placeholder, style = textStyle.copy(color = Placeholder))
                }
                inner()
            }
        }
    )
}

@Composable
private fun Hint(text: Int) {
    Text(text = stringResource(text), style = AppTextStyles.RowSubtitle, color = Muted)
    Spacer(Modifier.height(8.dp))
}

@Composable
private fun Feedback(good: Boolean, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(ItemShape)
            .background(if (good) FeedbackGoodBg else FeedbackBadBg)
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = if (good) "🤖✨" else "🤖🤔", style = AppTextStyles.RowIcon)
        Text(
            text = text,
            style = AppTextStyles.SmallBody,
            color = if (good) FeedbackGoodText else FeedbackBadText,
            modifier = Modifier.weight(1f)
        )
    }
}

