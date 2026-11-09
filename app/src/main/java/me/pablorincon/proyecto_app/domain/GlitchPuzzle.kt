package me.pablorincon.proyecto_app.domain

import me.pablorincon.proyecto_app.domain.glitch.GlitchBlock
import me.pablorincon.proyecto_app.domain.glitch.GlitchBoard
import me.pablorincon.proyecto_app.domain.glitch.GlitchEditor
import me.pablorincon.proyecto_app.domain.glitch.GlitchLine
import me.pablorincon.proyecto_app.domain.glitch.GlitchParser
import me.pablorincon.proyecto_app.domain.glitch.GlitchRun
import me.pablorincon.proyecto_app.domain.glitch.GlitchRunner
import me.pablorincon.proyecto_app.domain.glitch.ParseResult
import me.pablorincon.proyecto_app.domain.glitch.ProgramError
import me.pablorincon.proyecto_app.domain.glitch.RunOutcome

enum class GlitchMode {
    BUILD,     // Construye: desde cero con la paleta y un límite de líneas
    COMPLETE,  // Completa: el programa viene hecho con huecos
    FIX        // Arregla: el programa tiene un bug y Glitch falla
}

// Simulador de Glitch: se arma un programa con bloques de Python y Glitch lo ejecuta en un tablero.
// En las lecciones se puede ejecutar todas las veces que haga falta; en los jefes un fallo cuenta.
// `solution` no se muestra: los tests la usan para comprobar que cada tablero tiene solución.
data class GlitchPuzzle(
    override val prompt: String,
    val board: List<String>,
    val mode: GlitchMode,
    val palette: List<GlitchBlock>,
    val solution: String,
    val starter: String = "",
    val maxLines: Int? = null,
    override val correctFeedback: String,
    override val wrongFeedback: String
) : Exercise {
    val parsedBoard: GlitchBoard = GlitchBoard.parse(board)
    val starterLines: List<GlitchLine> = GlitchLine.parseProgram(starter, lockFilled = mode == GlitchMode.COMPLETE)
    val solutionLines: List<GlitchLine> = GlitchLine.parseProgram(solution)

    init {
        require(mode == GlitchMode.BUILD || starterLines.isNotEmpty()) { "Falta el programa inicial: $prompt" }
        require(mode != GlitchMode.BUILD || palette.isNotEmpty()) { "Falta la paleta: $prompt" }
        require(maxLines == null || maxLines >= starterLines.size) { "El límite es menor que el programa: $prompt" }
    }

    fun newEditor(): GlitchEditor = GlitchEditor(
        lines = starterLines,
        freeEditing = mode != GlitchMode.COMPLETE,
        maxLines = maxLines
    )

    fun attempt(lines: List<GlitchLine>): GlitchAttempt {
        if (maxLines != null && lines.size > maxLines) return GlitchAttempt.TooLong(lines.size, maxLines)
        return when (val parsed = GlitchParser.parse(lines)) {
            is ParseResult.Error -> GlitchAttempt.Invalid(parsed.error, parsed.line)
            is ParseResult.Ok -> GlitchAttempt.Ran(GlitchRunner.run(parsedBoard, parsed.program))
        }
    }

    fun isCorrect(lines: List<GlitchLine>): Boolean = attempt(lines).isSuccess
}

sealed interface GlitchAttempt {
    val isSuccess: Boolean get() = this is Ran && run.outcome == RunOutcome.SUCCESS

    data class Invalid(val error: ProgramError, val line: Int) : GlitchAttempt
    data class TooLong(val lines: Int, val max: Int) : GlitchAttempt
    data class Ran(val run: GlitchRun) : GlitchAttempt
}
