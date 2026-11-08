package me.pablorincon.proyecto_app.ui.screens.lesson

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import me.pablorincon.proyecto_app.ui.theme.SyntaxComment
import me.pablorincon.proyecto_app.ui.theme.SyntaxFunction
import me.pablorincon.proyecto_app.ui.theme.SyntaxKeyword
import me.pablorincon.proyecto_app.ui.theme.SyntaxNumber
import me.pablorincon.proyecto_app.ui.theme.SyntaxString

// Resaltado de sintaxis mínimo para el Python de las lecciones (colores de la guía §0).
// Un comentario o texto sin cerrar se pinta hasta el final de la línea: nunca falla.
internal object PythonHighlighter {

    private val keywords = setOf(
        "if", "elif", "else", "for", "while", "in", "def", "return", "and", "or", "not",
        "True", "False", "None", "break", "continue", "import", "from"
    )

    private val token = Regex(
        """(#.*)|("[^"\n]*"?|'[^'\n]*'?)|(\b\d+(?:\.\d+)?\b)|([A-Za-z_]\w*)"""
    )

    fun highlight(line: String): AnnotatedString = buildAnnotatedString {
        append(line)
        for (match in token.findAll(line)) {
            val style = when {
                match.groups[1] != null -> SpanStyle(color = SyntaxComment)
                match.groups[2] != null -> SpanStyle(color = SyntaxString)
                match.groups[3] != null -> SpanStyle(color = SyntaxNumber)
                else -> {
                    val word = match.value
                    when {
                        word in keywords -> SpanStyle(color = SyntaxKeyword)
                        line.getOrNull(match.range.last + 1) == '(' -> SpanStyle(color = SyntaxFunction)
                        else -> null
                    }
                }
            }
            if (style != null) addStyle(style, match.range.first, match.range.last + 1)
        }
    }
}
