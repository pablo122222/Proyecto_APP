package me.pablorincon.proyecto_app.ui.screens.lesson

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.pablorincon.proyecto_app.R
import me.pablorincon.proyecto_app.ui.theme.AnswerCorrectBg
import me.pablorincon.proyecto_app.ui.theme.AnswerWrongBg
import me.pablorincon.proyecto_app.ui.theme.CodeBg
import me.pablorincon.proyecto_app.ui.theme.CodeLineNumber
import me.pablorincon.proyecto_app.ui.theme.CodeText

// Estilo del código: monoespaciado 11 sp, interlineado 1.85 (guía §4).
internal val CodeTextStyle = TextStyle(
    fontFamily = FontFamily.Monospace,
    fontSize = 11.sp,
    lineHeight = 20.sp,
    color = CodeText
)

// Bloque de código (CodeBg, números de línea, scroll horizontal, resaltado de sintaxis).
// Con onLineClick cada línea se puede tocar (ejercicio "Caza el bug"); markedLine pinta la
// línea elegida de verde (markedGood = true) o de rojo.
@Composable
internal fun CodeBlock(
    code: String,
    modifier: Modifier = Modifier,
    onLineClick: ((Int) -> Unit)? = null,
    markedLine: Int? = null,
    markedGood: Boolean? = null
) {
    val description = stringResource(R.string.lesson_code_description)
    val lines = code.lines()
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(CodeBg)
            .horizontalScroll(rememberScrollState())
            .padding(vertical = 8.dp)
            .semantics { contentDescription = description + ". " + code }
    ) {
        lines.forEachIndexed { index, line ->
            val mark = when {
                index != markedLine -> null
                markedGood == true -> AnswerCorrectBg.copy(alpha = 0.25f)
                else -> AnswerWrongBg.copy(alpha = 0.25f)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(if (mark != null) Modifier.background(mark) else Modifier)
                    .then(if (onLineClick != null) Modifier.clickable { onLineClick(index) } else Modifier)
                    .padding(horizontal = 14.dp)
            ) {
                Text(
                    text = (index + 1).toString(),
                    style = CodeTextStyle.copy(color = CodeLineNumber),
                    textAlign = TextAlign.End,
                    modifier = Modifier.width(20.dp)
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    text = PythonHighlighter.highlight(line.ifEmpty { " " }),
                    style = CodeTextStyle,
                    softWrap = false
                )
            }
        }
    }
}
