package me.pablorincon.proyecto_app.domain.tutor

// Partes de una respuesta del modelo: texto (con negritas y código en línea) o un bloque de código.
sealed interface TutorBlock {
    data class Prose(val runs: List<Run>) : TutorBlock
    data class Code(val code: String) : TutorBlock

    data class Run(val text: String, val bold: Boolean = false, val code: Boolean = false)
}

// Convierte el Markdown sencillo que escribe el modelo (```python, **negrita**, `código`,
// viñetas y títulos) en partes que la pantalla sabe dibujar. Tolera texto a medio llegar:
// un bloque ``` sin cerrar es código hasta el final.
object TutorFormat {

    fun blocks(text: String): List<TutorBlock> {
        val blocks = mutableListOf<TutorBlock>()
        val prose = mutableListOf<String>()
        val code = mutableListOf<String>()
        var inCode = false

        fun flushProse() {
            val joined = prose.joinToString("\n").trim('\n', ' ')
            if (joined.isNotBlank()) blocks += TutorBlock.Prose(runs(joined))
            prose.clear()
        }

        fun flushCode() {
            val joined = code.joinToString("\n").trimEnd()
            if (joined.isNotBlank()) blocks += TutorBlock.Code(joined.trimStart('\n'))
            code.clear()
        }

        for (line in text.lines()) {
            if (line.trimStart().startsWith("```")) {
                if (inCode) flushCode() else flushProse()
                inCode = !inCode
                continue
            }
            if (inCode) code += line else prose += cleanLine(line)
        }
        if (inCode) flushCode() else flushProse()
        return blocks
    }

    // Títulos (#) quedan como texto normal y las viñetas (* o -) como "•".
    private fun cleanLine(line: String): String {
        val trimmed = line.trimStart()
        return when {
            trimmed.startsWith("#") -> trimmed.trimStart('#').trimStart()
            trimmed.startsWith("* ") || trimmed.startsWith("- ") -> "• " + trimmed.drop(2)
            else -> line
        }
    }

    // **negrita** y `código`. Una marca sin cerrar (texto a medio llegar) sigue hasta el final.
    internal fun runs(text: String): List<TutorBlock.Run> {
        val runs = mutableListOf<TutorBlock.Run>()
        val current = StringBuilder()
        var bold = false
        var code = false
        var i = 0

        fun flush() {
            if (current.isNotEmpty()) runs += TutorBlock.Run(current.toString(), bold, code)
            current.clear()
        }

        while (i < text.length) {
            when {
                !code && text.startsWith("**", i) -> { flush(); bold = !bold; i += 2 }
                text[i] == '`' -> { flush(); code = !code; i++ }
                else -> { current.append(text[i]); i++ }
            }
        }
        flush()
        return runs
    }
}
