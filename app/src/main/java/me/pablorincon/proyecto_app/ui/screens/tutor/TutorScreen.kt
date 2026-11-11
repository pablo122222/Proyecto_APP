package me.pablorincon.proyecto_app.ui.screens.tutor

import androidx.annotation.StringRes
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import me.pablorincon.proyecto_app.R
import me.pablorincon.proyecto_app.domain.tutor.TUTOR_MAX_QUESTION
import me.pablorincon.proyecto_app.domain.tutor.TutorBlock
import me.pablorincon.proyecto_app.domain.tutor.TutorChat
import me.pablorincon.proyecto_app.domain.tutor.TutorFormat
import me.pablorincon.proyecto_app.domain.tutor.TutorMessage
import me.pablorincon.proyecto_app.domain.tutor.TutorReply
import me.pablorincon.proyecto_app.domain.tutor.TutorTopic
import me.pablorincon.proyecto_app.ui.components.Pill
import me.pablorincon.proyecto_app.ui.screens.lesson.CodeBlock
import me.pablorincon.proyecto_app.ui.components.decorative
import me.pablorincon.proyecto_app.ui.theme.AppAppearance
import me.pablorincon.proyecto_app.ui.theme.AppBackground
import me.pablorincon.proyecto_app.ui.theme.AppDimens
import me.pablorincon.proyecto_app.ui.theme.AppTextStyles
import me.pablorincon.proyecto_app.ui.theme.CodeMentorTheme
import me.pablorincon.proyecto_app.ui.theme.FinePrintText
import me.pablorincon.proyecto_app.ui.theme.HeroEnd
import me.pablorincon.proyecto_app.ui.theme.HeroStart
import me.pablorincon.proyecto_app.ui.theme.Ink
import me.pablorincon.proyecto_app.ui.theme.Line
import me.pablorincon.proyecto_app.ui.theme.Muted
import me.pablorincon.proyecto_app.ui.theme.Paper
import me.pablorincon.proyecto_app.ui.theme.Placeholder
import me.pablorincon.proyecto_app.ui.theme.Primary
import me.pablorincon.proyecto_app.ui.theme.PrimaryDark
import me.pablorincon.proyecto_app.ui.theme.PrimaryDisabled
import me.pablorincon.proyecto_app.ui.theme.TutorChipBg
import me.pablorincon.proyecto_app.ui.theme.TutorChipBorder
import me.pablorincon.proyecto_app.ui.theme.TutorHeaderIconBg
import me.pablorincon.proyecto_app.ui.theme.TutorHeaderText
import me.pablorincon.proyecto_app.ui.theme.TutorUserBubbleBg
import me.pablorincon.proyecto_app.ui.theme.TutorUserBubbleText

// Medidas de la guía §9
private val TutorBubbleShape = RoundedCornerShape(topStart = 5.dp, topEnd = 14.dp, bottomEnd = 14.dp, bottomStart = 14.dp)
private val UserBubbleShape = RoundedCornerShape(topStart = 14.dp, topEnd = 5.dp, bottomEnd = 14.dp, bottomStart = 14.dp)
private val HeaderShape = RoundedCornerShape(18.dp)
private val InputShape = RoundedCornerShape(12.dp)
private val BubbleText = TextStyle(fontSize = 11.sp, lineHeight = 17.6.sp) // 11 sp, interlineado 1.6
private val SmallLabel = TextStyle(fontSize = 10.sp, fontWeight = FontWeight(800), letterSpacing = 0.6.sp)

// Preguntas sugeridas: texto del chip y pregunta que envía
private enum class Suggestion(@param:StringRes val label: Int, @param:StringRes val question: Int) {
    IF(R.string.tutor_chip_if, R.string.tutor_chip_if_question),
    ELSE(R.string.tutor_chip_else, R.string.tutor_chip_else_question),
    VARIABLE(R.string.tutor_chip_variable, R.string.tutor_chip_variable_question)
}

// Pantalla "Tutor IA" (pestaña Tutor IA). Versión de demostración: respuestas predefinidas.
@Composable
fun TutorScreen(modifier: Modifier = Modifier, viewModel: TutorViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsState()
    TutorContent(
        state = state,
        onInputChange = viewModel::onInputChange,
        onSend = viewModel::send,
        onSuggestion = viewModel::askSuggestion,
        onClear = viewModel::clearConversation,
        modifier = modifier
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TutorContent(
    state: TutorChat,
    onInputChange: (String) -> Unit,
    onSend: () -> Unit,
    onSuggestion: (String) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    // Cada mensaje nuevo (o el "escribiendo…") se deja a la vista; también mientras la
    // respuesta del tutor va creciendo (cambia el último mensaje).
    LaunchedEffect(state.messages.size, state.isAnswering, state.messages.lastOrNull()) {
        if (state.hasConversation) listState.animateScrollToItem(listState.layoutInfo.totalItemsCount.coerceAtLeast(1) - 1)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppBackground)
            .statusBarsPadding()
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(
                start = AppDimens.ScreenHorizontal,
                end = AppDimens.ScreenHorizontal,
                top = AppDimens.ScreenTop,
                bottom = 12.dp
            ),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item { TopLine() }
            item { HeaderCard() }
            items(state.messages, key = { it.id }) { message -> MessageBubble(message) }
            if (state.awaitingFirstWords) item(key = "typing") { TypingBubble() }
            item(key = "suggestions") {
                Column {
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = stringResource(R.string.tutor_suggestions_label).uppercase(),
                        style = SmallLabel,
                        color = Ink
                    )
                    Spacer(Modifier.height(8.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Suggestion.entries.forEach { suggestion ->
                            val question = stringResource(suggestion.question)
                            SuggestionChip(
                                text = stringResource(suggestion.label),
                                enabled = !state.isAnswering,
                                onClick = { onSuggestion(question) }
                            )
                        }
                    }
                    if (state.hasConversation) {
                        Spacer(Modifier.height(12.dp))
                        // no está en el mockup: volver a empezar la conversación
                        Text(
                            text = stringResource(R.string.tutor_clear),
                            style = AppTextStyles.RowSubtitle.copy(fontWeight = FontWeight.SemiBold),
                            color = if (state.isAnswering) Muted else Primary,
                            modifier = Modifier
                                .clip(InputShape)
                                .clickable(enabled = !state.isAnswering, onClick = onClear)
                                .padding(vertical = 6.dp)
                        )
                    }
                }
            }
        }

        QuestionInput(
            state = state,
            onInputChange = onInputChange,
            onSend = onSend,
            modifier = Modifier
                .windowInsetsPadding(
                    // Sobre la barra de pestañas; con el teclado abierto, sobre el teclado
                    WindowInsets.ime.union(WindowInsets.navigationBars.add(WindowInsets(bottom = AppDimens.ScreenBottom)))
                )
                .padding(horizontal = AppDimens.ScreenHorizontal)
        )
    }
}

// topline: "Siempre puedes preguntar" + "Tutor IA" y pill "Demo".
@Composable
private fun TopLine() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = AppDimens.TopLineBottom - 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column(Modifier.weight(1f)) {
            Text(text = stringResource(R.string.tutor_eyebrow).uppercase(), style = AppTextStyles.Eyebrow, color = Primary)
            Spacer(Modifier.height(AppDimens.EyebrowBottom))
            Text(text = stringResource(R.string.tab_tutor), style = AppTextStyles.ScreenTitleSmall, color = Ink)
        }
        Spacer(Modifier.width(AppDimens.TopLineGap))
        Pill(text = stringResource(R.string.tutor_demo_pill))
    }
}

// Cabecera con degradado: cuadro ✦, título y texto.
@Composable
private fun HeaderCard() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(HeaderShape)
            .background(Brush.linearGradient(listOf(HeroStart, HeroEnd)))
            .padding(17.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(41.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(TutorHeaderIconBg),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "✦", fontSize = 22.sp, color = Paper, modifier = Modifier.decorative())
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.tutor_header_title),
                style = TextStyle(fontSize = 17.sp, fontWeight = FontWeight(800), lineHeight = 21.sp),
                color = Paper
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.tutor_header_text),
                style = TextStyle(fontSize = 10.sp, lineHeight = 15.sp),
                color = TutorHeaderText
            )
        }
    }
}

@Composable
private fun MessageBubble(message: TutorMessage) {
    when (message) {
        is TutorMessage.Greeting -> TutorBubble(stringResource(R.string.tutor_greeting))
        is TutorMessage.FromTutor -> when (val reply = message.reply) {
            is TutorReply.Free -> ModelReplyBubble(reply.text)
            is TutorReply.Topic -> TutorBubble(replyText(reply))
        }
        is TutorMessage.FromUser -> UserBubble(message.text)
    }
}

// Respuesta del modelo de IA: párrafos con negritas y código en línea, y los bloques de
// código con el mismo estilo que en las lecciones.
@Composable
private fun ModelReplyBubble(text: String) {
    val blocks = TutorFormat.blocks(text)
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .widthIn(max = maxWidth * 0.94f)
                .clip(TutorBubbleShape)
                .background(Paper)
                .border(AppDimens.BorderWidth, Line, TutorBubbleShape)
                .padding(horizontal = 13.dp, vertical = 11.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            blocks.forEach { block ->
                when (block) {
                    is TutorBlock.Prose -> Text(text = proseText(block), style = BubbleText, color = Ink)
                    is TutorBlock.Code -> CodeBlock(code = block.code)
                }
            }
        }
    }
}

private fun proseText(block: TutorBlock.Prose): AnnotatedString = buildAnnotatedString {
    block.runs.forEach { run ->
        val style = when {
            run.code -> SpanStyle(fontFamily = FontFamily.Monospace, color = PrimaryDark)
            run.bold -> SpanStyle(fontWeight = FontWeight.Bold)
            else -> null
        }
        if (style == null) append(run.text) else withStyle(style) { append(run.text) }
    }
}

@Composable
private fun replyText(reply: TutorReply): String = when (reply) {
    is TutorReply.Free -> reply.text
    is TutorReply.Topic -> stringResource(
        when (reply.topic) {
            TutorTopic.ELSE -> R.string.tutor_answer_else
            TutorTopic.IF -> R.string.tutor_answer_if
            TutorTopic.VARIABLE -> R.string.tutor_answer_variable
            TutorTopic.LOOP -> R.string.tutor_answer_loop
            TutorTopic.UNKNOWN -> R.string.tutor_answer_unknown
        }
    )
}

// Burbuja del tutor: blanca, a la izquierda, ancho máximo 94 %.
@Composable
private fun TutorBubble(text: String) {
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        Text(
            text = text,
            style = BubbleText,
            color = Ink,
            modifier = Modifier
                .widthIn(max = maxWidth * 0.94f)
                .clip(TutorBubbleShape)
                .background(Paper)
                .border(AppDimens.BorderWidth, Line, TutorBubbleShape)
                .padding(horizontal = 13.dp, vertical = 11.dp)
        )
    }
}

// Burbuja del usuario: azul suave, a la derecha.
@Composable
private fun UserBubble(text: String) {
    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
        Text(
            text = text,
            style = BubbleText.copy(fontWeight = FontWeight.SemiBold),
            color = TutorUserBubbleText,
            modifier = Modifier
                .padding(start = 40.dp)
                .clip(UserBubbleShape)
                .background(TutorUserBubbleBg)
                .padding(horizontal = 13.dp, vertical = 11.dp)
        )
    }
}

// "El tutor está escribiendo…" con tres puntos que laten.
@Composable
private fun TypingBubble() {
    val description = stringResource(R.string.tutor_typing)
    val transition = rememberInfiniteTransition(label = "typing")
    Row(
        modifier = Modifier
            .clip(TutorBubbleShape)
            .background(Paper)
            .border(AppDimens.BorderWidth, Line, TutorBubbleShape)
            .semantics {
                contentDescription = description
                liveRegion = LiveRegionMode.Polite
            }
            .padding(horizontal = 16.dp, vertical = 13.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        repeat(3) { index ->
            val pulse by transition.animateFloat(
                initialValue = 0.25f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(tween(450, delayMillis = index * 150), RepeatMode.Reverse),
                label = "dot$index"
            )
            // Con "Reducir animaciones" los puntos quedan quietos
            val alpha = if (AppAppearance.reduceMotion) 0.7f else pulse
            Box(
                Modifier
                    .size(7.dp)
                    .alpha(alpha)
                    .clip(RoundedCornerShape(50))
                    .background(Primary)
            )
        }
    }
}

@Composable
private fun SuggestionChip(text: String, enabled: Boolean, onClick: () -> Unit) {
    Text(
        text = text,
        style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight(700)),
        color = if (enabled) PrimaryDark else Muted,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(TutorChipBg)
            .border(AppDimens.BorderWidth, TutorChipBorder, RoundedCornerShape(50))
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    )
}

// "TU PREGUNTA": campo de texto + botón ➤ y el aviso de demostración.
@Composable
private fun QuestionInput(
    state: TutorChat,
    onInputChange: (String) -> Unit,
    onSend: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier.padding(top = 8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.tutor_input_label).uppercase(),
                style = SmallLabel,
                color = Ink,
                modifier = Modifier.weight(1f)
            )
            // no está en el mockup: contador cuando la pregunta se acerca al límite
            if (state.input.length > TUTOR_MAX_QUESTION * 3 / 4) {
                Text(
                    text = stringResource(R.string.tutor_counter, state.input.length, TUTOR_MAX_QUESTION),
                    style = TextStyle(fontSize = 10.sp),
                    color = Muted
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            val textStyle = TextStyle(fontSize = 11.sp, lineHeight = 16.sp, color = Ink)
            BasicTextField(
                value = state.input,
                onValueChange = onInputChange,
                textStyle = textStyle,
                maxLines = 4,
                cursorBrush = SolidColor(Primary),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = { if (state.canSend) onSend() }),
                modifier = Modifier
                    .weight(1f)
                    .clip(InputShape)
                    .background(Paper)
                    .border(AppDimens.BorderWidth, Line, InputShape)
                    .padding(12.dp),
                decorationBox = { inner ->
                    Box {
                        if (state.input.isEmpty()) {
                            Text(text = stringResource(R.string.tutor_input_placeholder), style = textStyle.copy(color = Placeholder))
                        }
                        inner()
                    }
                }
            )
            Spacer(Modifier.width(8.dp))
            val sendDescription = stringResource(R.string.tutor_send)
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(InputShape)
                    .background(if (state.canSend) Primary else PrimaryDisabled)
                    .clickable(enabled = state.canSend, onClick = onSend)
                    .semantics { contentDescription = sendDescription },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "➤", color = Paper, fontSize = 16.sp)
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.tutor_notice),
            style = TextStyle(fontSize = 9.sp, lineHeight = 13.sp),
            color = FinePrintText
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun TutorScreenPreview() {
    CodeMentorTheme {
        TutorContent(
            state = TutorChat(
                messages = listOf(
                    TutorMessage.Greeting(0),
                    TutorMessage.FromUser(1, "¿Qué es un if?"),
                    TutorMessage.FromTutor(2, TutorReply.Topic(TutorTopic.IF))
                ),
                isAnswering = true
            ),
            onInputChange = {},
            onSend = {},
            onSuggestion = {},
            onClear = {}
        )
    }
}
