package me.pablorincon.proyecto_app.ui.screens.lab

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.pablorincon.proyecto_app.R
import me.pablorincon.proyecto_app.ui.components.PrimaryButton
import me.pablorincon.proyecto_app.ui.components.SecondaryButton
import me.pablorincon.proyecto_app.ui.theme.AnswerCorrectBg
import me.pablorincon.proyecto_app.ui.theme.AnswerCorrectBorder
import me.pablorincon.proyecto_app.ui.theme.AnswerWrongBg
import me.pablorincon.proyecto_app.ui.theme.AnswerWrongBorder
import me.pablorincon.proyecto_app.ui.theme.AppDimens
import me.pablorincon.proyecto_app.ui.theme.AppTextStyles
import me.pablorincon.proyecto_app.ui.theme.FeedbackBadBg
import me.pablorincon.proyecto_app.ui.theme.FeedbackBadText
import me.pablorincon.proyecto_app.ui.theme.FeedbackGoodBg
import me.pablorincon.proyecto_app.ui.theme.FeedbackGoodText
import me.pablorincon.proyecto_app.ui.theme.Ink
import me.pablorincon.proyecto_app.ui.theme.InterFamily
import me.pablorincon.proyecto_app.ui.theme.Line
import me.pablorincon.proyecto_app.ui.theme.Muted
import me.pablorincon.proyecto_app.ui.theme.Paper

private val OptionShape = RoundedCornerShape(12.dp)

// Hoja "¿Por qué funciona?" (decisión D): pregunta opcional de opción múltiple tras completar
// una actividad por primera vez. Un toque responde; luego se ve si acertó y la opción correcta.
// Mismo estilo que ConfirmSheet (guía §0).
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WhySheet(state: WhyState, onAnswer: (Int) -> Unit, onClose: () -> Unit) {
    val texts = LabText.why.getValue(state.question.activityId)
    ModalBottomSheet(
        onDismissRequest = onClose,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Paper,
        scrimColor = Color(0x731F2A44), // #1F2A44 al 45 %
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(start = 22.dp, end = 22.dp, top = 6.dp, bottom = 30.dp)
        ) {
            Text(
                text = stringResource(R.string.why_title),
                style = TextStyle(fontFamily = InterFamily, fontWeight = FontWeight.Bold, fontSize = 18.sp),
                color = Ink
            )
            Spacer(Modifier.height(6.dp))
            Text(text = stringResource(R.string.why_optional), style = AppTextStyles.SmallBody, color = Muted)
            Spacer(Modifier.height(14.dp))
            Text(text = stringResource(texts.first), style = AppTextStyles.RowTitle, color = Ink)
            Spacer(Modifier.height(12.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                texts.second.forEachIndexed { index, option ->
                    val answered = state.answer != null
                    // Tras responder: la correcta en verde y la elegida (si falló) en rojo
                    val (border, fill) = when {
                        answered && index == state.question.correctIndex -> AnswerCorrectBorder to AnswerCorrectBg
                        answered && index == state.answer -> AnswerWrongBorder to AnswerWrongBg
                        else -> Line to Paper
                    }
                    Text(
                        text = stringResource(option),
                        style = AppTextStyles.SmallBody,
                        color = Ink,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(OptionShape)
                            .background(fill)
                            .border(AppDimens.BorderWidth, border, OptionShape)
                            .clickable(enabled = !answered, role = Role.Button) { onAnswer(index) }
                            .padding(12.dp)
                    )
                }
            }
            Spacer(Modifier.height(14.dp))
            if (state.answer == null) {
                SecondaryButton(text = stringResource(R.string.why_skip), onClick = onClose)
            } else {
                Text(
                    text = stringResource(if (state.isCorrect) R.string.why_correct else R.string.why_wrong),
                    style = AppTextStyles.SmallBody,
                    color = if (state.isCorrect) FeedbackGoodText else FeedbackBadText,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(OptionShape)
                        .background(if (state.isCorrect) FeedbackGoodBg else FeedbackBadBg)
                        .semantics { liveRegion = LiveRegionMode.Polite }
                        .padding(12.dp)
                )
                Spacer(Modifier.height(12.dp))
                PrimaryButton(text = stringResource(R.string.why_continue), onClick = onClose)
            }
        }
    }
}
