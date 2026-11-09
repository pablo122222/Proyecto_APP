package me.pablorincon.proyecto_app.domain.glitch

// Bloques del simulador. Cada uno se muestra como una línea de Python real.
sealed interface GlitchBlock {
    val code: String
    val opensBody: Boolean get() = false   // Termina en ":" y necesita líneas con sangría debajo

    data object Forward : GlitchBlock { override val code = "avanzar()" }
    data object TurnLeft : GlitchBlock { override val code = "girar_izquierda()" }
    data object TurnRight : GlitchBlock { override val code = "girar_derecha()" }

    // times = 0 significa número sin elegir: se muestra "range(___)"
    data class Repeat(val times: Int) : GlitchBlock {
        override val code = "for i in range(${if (times == 0) "___" else times}):"
        override val opensBody = true
        fun nextTimes(): Int = if (times >= MAX_TIMES) 1 else times + 1
    }

    data object WhileNotGoal : GlitchBlock {
        override val code = "while not en_meta():"
        override val opensBody = true
    }

    data class IfWall(val negated: Boolean = false) : GlitchBlock {
        override val code = if (negated) "if not hay_pared():" else "if hay_pared():"
        override val opensBody = true
    }

    data object Else : GlitchBlock {
        override val code = "else:"
        override val opensBody = true
    }

    companion object {
        const val MAX_TIMES = 9
        private val fixed by lazy { listOf(Forward, TurnLeft, TurnRight, WhileNotGoal, IfWall(false), IfWall(true), Else) }
        private val repeatRegex = Regex("""for i in range\((\d|___)\):""")

        fun parse(code: String): GlitchBlock? {
            val text = code.trim()
            fixed.firstOrNull { it.code == text }?.let { return it }
            val match = repeatRegex.matchEntire(text) ?: return null
            val value = match.groupValues[1]
            return Repeat(if (value == "___") 0 else value.toInt())
        }
    }
}

// Una línea del editor: bloque (null = hueco por rellenar), sangría y si se puede tocar.
// slot = hueco de bloque (modo Completa): se puede rellenar o cambiar con la paleta.
data class GlitchLine(
    val block: GlitchBlock?,
    val indent: Int,
    val locked: Boolean = false,
    val slot: Boolean = false
) {
    val code: String get() = "    ".repeat(indent) + (block?.code ?: "___")

    companion object {
        // Programa escrito como Python: cada sangría son 4 espacios y "___" es un hueco.
        // Con lockFilled = true (modo Completa) las líneas escritas quedan fijas y solo se tocan
        // los huecos y los range(___).
        fun parseProgram(source: String, lockFilled: Boolean = false): List<GlitchLine> =
            source.trimIndent().lines().filter { it.isNotBlank() }.map { raw ->
                val spaces = raw.length - raw.trimStart().length
                require(spaces % 4 == 0) { "Sangría que no es múltiplo de 4: '$raw'" }
                val text = raw.trim()
                val block = if (text == "___") null else requireNotNull(GlitchBlock.parse(text)) { "Línea desconocida: '$raw'" }
                val hasHole = block == null || (block is GlitchBlock.Repeat && block.times == 0)
                GlitchLine(block, spaces / 4, locked = lockFilled && !hasHole, slot = block == null)
            }
    }
}

// Programa ya armado como árbol, con la línea de cada instrucción para iluminarla al ejecutar.
sealed interface Statement {
    val line: Int

    data class Action(override val line: Int, val block: GlitchBlock) : Statement
    data class Loop(override val line: Int, val times: Int, val body: List<Statement>) : Statement
    data class While(override val line: Int, val body: List<Statement>) : Statement
    data class If(
        override val line: Int,
        val negated: Boolean,
        val then: List<Statement>,
        val otherwise: List<Statement>
    ) : Statement
}

enum class ProgramError {
    EMPTY,              // No hay ninguna línea
    HOLE,               // Queda un hueco o un range(___) sin rellenar
    UNEXPECTED_INDENT,  // Línea con más sangría de la que toca
    EMPTY_BODY,         // Un for/while/if sin nada adentro
    ELSE_WITHOUT_IF     // else: que no va justo después de un if
}

sealed interface ParseResult {
    data class Ok(val program: List<Statement>) : ParseResult
    data class Error(val error: ProgramError, val line: Int) : ParseResult
}

// Convierte las líneas del editor en un árbol, con las mismas reglas de sangría que Python.
object GlitchParser {
    fun parse(lines: List<GlitchLine>): ParseResult {
        if (lines.isEmpty()) return ParseResult.Error(ProgramError.EMPTY, 0)
        lines.forEachIndexed { index, line ->
            val block = line.block
            if (block == null || (block is GlitchBlock.Repeat && block.times == 0)) {
                return ParseResult.Error(ProgramError.HOLE, index)
            }
        }
        if (lines.first().indent != 0) return ParseResult.Error(ProgramError.UNEXPECTED_INDENT, 0)
        return try {
            val (program, end) = parseBlock(lines, 0, 0)
            check(end == lines.size)
            ParseResult.Ok(program)
        } catch (e: ParseException) {
            ParseResult.Error(e.error, e.line)
        }
    }

    private class ParseException(val error: ProgramError, val line: Int) : Exception()

    // Lee las líneas con sangría exacta `indent` desde `from`. Devuelve las instrucciones y
    // el índice de la primera línea que ya no pertenece a este bloque.
    private fun parseBlock(lines: List<GlitchLine>, from: Int, indent: Int): Pair<List<Statement>, Int> {
        val result = mutableListOf<Statement>()
        var i = from
        while (i < lines.size) {
            val line = lines[i]
            if (line.indent < indent) break
            if (line.indent > indent) throw ParseException(ProgramError.UNEXPECTED_INDENT, i)
            val block = line.block!!
            if (block == GlitchBlock.Else) throw ParseException(ProgramError.ELSE_WITHOUT_IF, i)
            if (!block.opensBody) {
                result += Statement.Action(i, block)
                i++
                continue
            }
            val (body, afterBody) = parseBody(lines, i, indent)
            when (block) {
                is GlitchBlock.Repeat -> {
                    result += Statement.Loop(i, block.times, body)
                    i = afterBody
                }
                GlitchBlock.WhileNotGoal -> {
                    result += Statement.While(i, body)
                    i = afterBody
                }
                is GlitchBlock.IfWall -> {
                    var otherwise = emptyList<Statement>()
                    var next = afterBody
                    if (next < lines.size && lines[next].indent == indent && lines[next].block == GlitchBlock.Else) {
                        val (elseBody, afterElse) = parseBody(lines, next, indent)
                        otherwise = elseBody
                        next = afterElse
                    }
                    result += Statement.If(i, block.negated, body, otherwise)
                    i = next
                }
                else -> error("Bloque sin cuerpo inesperado: $block")
            }
        }
        return result to i
    }

    // Cuerpo de la cabecera en `header`: las líneas siguientes con una sangría más.
    private fun parseBody(lines: List<GlitchLine>, header: Int, indent: Int): Pair<List<Statement>, Int> {
        val first = header + 1
        if (first >= lines.size || lines[first].indent != indent + 1) {
            // Con más de una sangría extra, el error real es la sangría
            if (first < lines.size && lines[first].indent > indent + 1) {
                throw ParseException(ProgramError.UNEXPECTED_INDENT, first)
            }
            throw ParseException(ProgramError.EMPTY_BODY, header)
        }
        return parseBlock(lines, first, indent + 1)
    }
}
