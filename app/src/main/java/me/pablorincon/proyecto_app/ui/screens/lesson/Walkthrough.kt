package me.pablorincon.proyecto_app.ui.screens.lesson

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.pablorincon.proyecto_app.R
import me.pablorincon.proyecto_app.domain.LessonContent
import me.pablorincon.proyecto_app.ui.components.PrimaryButton
import me.pablorincon.proyecto_app.ui.components.SecondaryButton
import me.pablorincon.proyecto_app.ui.theme.AppTextStyles
import me.pablorincon.proyecto_app.ui.theme.Ink
import me.pablorincon.proyecto_app.ui.theme.Muted
import me.pablorincon.proyecto_app.ui.theme.OutputText
import me.pablorincon.proyecto_app.ui.theme.Primary
import me.pablorincon.proyecto_app.ui.theme.SoftBlueCardBg

private val PanelShape = RoundedCornerShape(12.dp)
private val ValueStyle = TextStyle(fontFamily = FontFamily.Monospace, fontSize = 11.sp, lineHeight = 17.sp)

// Ejemplo de la lección con "Ver paso a paso" (código visible): la línea en curso se resalta y
// debajo se ven las variables, lo impreso hasta ahí y una nota. Sin pasos escritos, solo el código.
@Composable
internal fun ExampleCode(content: LessonContent, expectedOutput: @Composable () -> Unit) {
    val steps = content.walkthrough
    var current by rememberSaveable(content.lessonId) { mutableStateOf<Int?>(null) } // null = cerrado
    val step = current?.let { steps.getOrNull(it) }

    CodeBlock(
        code = content.code,
        markedLine = step?.let { it.line - 1 },
        markColor = Primary.copy(alpha = 0.5f)
    )
    Spacer(Modifier.height(12.dp))

    if (step == null) {
        expectedOutput()
        if (steps.isNotEmpty()) {
            Spacer(Modifier.height(10.dp))
            SecondaryButton(text = stringResource(R.string.lesson_walk_start), onClick = { current = 0 })
        }
        return
    }

    val index = current ?: 0
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(PanelShape)
            .background(SoftBlueCardBg)
            .semantics(mergeDescendants = true) { liveRegion = LiveRegionMode.Polite }
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = stringResource(R.string.lesson_walk_step, index + 1, steps.size, step.line),
            style = AppTextStyles.RowTitle,
            color = Primary
        )
        Text(text = step.note, style = AppTextStyles.SmallBody, color = Ink)
        Text(stringResource(R.string.lesson_walk_variables), style = AppTextStyles.RowTitle, color = Ink)
        Text(
            text = step.variables.joinToString("\n") { (name, value) -> "$name = $value" }
                .ifEmpty { stringResource(R.string.lesson_walk_none) },
            style = ValueStyle,
            color = if (step.variables.isEmpty()) Muted else Ink
        )
        Text(stringResource(R.string.lesson_walk_output), style = AppTextStyles.RowTitle, color = Ink)
        Text(
            text = step.output.joinToString("\n").ifEmpty { stringResource(R.string.lesson_walk_none) },
            style = ValueStyle,
            color = if (step.output.isEmpty()) Muted else OutputText
        )
    }
    Spacer(Modifier.height(10.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        if (index > 0) {
            SecondaryButton(
                text = stringResource(R.string.lesson_walk_previous),
                onClick = { current = index - 1 },
                modifier = Modifier.weight(1f)
            )
        }
        val last = index == steps.lastIndex
        PrimaryButton(
            text = stringResource(if (last) R.string.lesson_walk_done else R.string.lesson_walk_next),
            onClick = { current = if (last) null else index + 1 },
            modifier = Modifier.weight(1f)
        )
    }
}
