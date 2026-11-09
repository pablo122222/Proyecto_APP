package me.pablorincon.proyecto_app.domain

import me.pablorincon.proyecto_app.domain.glitch.Cell
import me.pablorincon.proyecto_app.domain.glitch.Direction
import me.pablorincon.proyecto_app.domain.glitch.GlitchBlock
import me.pablorincon.proyecto_app.domain.glitch.GlitchBoard
import me.pablorincon.proyecto_app.domain.glitch.GlitchEditor
import me.pablorincon.proyecto_app.domain.glitch.GlitchLine
import me.pablorincon.proyecto_app.domain.glitch.GlitchParser
import me.pablorincon.proyecto_app.domain.glitch.GlitchRunner
import me.pablorincon.proyecto_app.domain.glitch.ParseResult
import me.pablorincon.proyecto_app.domain.glitch.ProgramError
import me.pablorincon.proyecto_app.domain.glitch.RunOutcome
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GlitchSimulatorTest {

    private fun run(rows: List<String>, source: String): RunOutcome {
        val parsed = GlitchParser.parse(GlitchLine.parseProgram(source))
        check(parsed is ParseResult.Ok) { "No compila: $parsed" }
        return GlitchRunner.run(GlitchBoard.parse(rows), parsed.program).outcome
    }

    private fun parseError(source: String): ParseResult.Error =
        GlitchParser.parse(GlitchLine.parseProgram(source)) as ParseResult.Error

    // ---------- Tablero ----------
    @Test
    fun elTableroSeLeeDesdeTexto() {
        val board = GlitchBoard.parse(listOf(">.B", "#.M"))
        assertEquals(3, board.width)
        assertEquals(2, board.height)
        assertEquals(Cell(0, 0), board.start)
        assertEquals(Direction.EAST, board.startDirection)
        assertEquals(setOf(Cell(2, 0)), board.batteries)
        assertEquals(setOf(Cell(0, 1)), board.walls)
        assertEquals(Cell(2, 1), board.goal)
        assertTrue(board.isBlocked(Cell(-1, 0)))
    }

    @Test(expected = IllegalArgumentException::class)
    fun unTableroSinMetaNoSeAcepta() {
        GlitchBoard.parse(listOf(">.."))
    }

    @Test
    fun girarDaLaVuelta() {
        assertEquals(Direction.NORTH, Direction.EAST.turnLeft())
        assertEquals(Direction.SOUTH, Direction.EAST.turnRight())
        assertEquals(Direction.WEST, Direction.NORTH.turnLeft())
    }

    // ---------- Bloques y líneas ----------
    @Test
    fun cadaBloqueSeLeeDesdeSuCodigo() {
        listOf(
            GlitchBlock.Forward, GlitchBlock.TurnLeft, GlitchBlock.TurnRight, GlitchBlock.Repeat(3),
            GlitchBlock.WhileNotGoal, GlitchBlock.IfWall(), GlitchBlock.IfWall(negated = true), GlitchBlock.Else
        ).forEach { assertEquals(it, GlitchBlock.parse(it.code)) }
        assertEquals(GlitchBlock.Repeat(0), GlitchBlock.parse("for i in range(___):"))
        assertNull(GlitchBlock.parse("saltar()"))
    }

    @Test
    fun elProgramaDelModoCompletaFijaLasLineasEscritas() {
        val lines = GlitchLine.parseProgram(
            """
            for i in range(___):
                avanzar()
                ___
            """,
            lockFilled = true
        )
        assertEquals(listOf(false, true, false), lines.map { it.locked })
        assertEquals(listOf(false, false, true), lines.map { it.slot })
        assertEquals(listOf(0, 1, 1), lines.map { it.indent })
        assertEquals("    ___", lines[2].code)
    }

    // ---------- Sangría (reglas de Python) ----------
    @Test
    fun losErroresDeSangriaSeDetectan() {
        assertEquals(ProgramError.UNEXPECTED_INDENT, parseError("avanzar()\n    avanzar()").error)
        assertEquals(ProgramError.EMPTY_BODY, parseError("for i in range(3):\navanzar()").error)
        assertEquals(ProgramError.ELSE_WITHOUT_IF, parseError("avanzar()\nelse:\n    avanzar()").error)
        assertEquals(ProgramError.HOLE, parseError("for i in range(___):\n    avanzar()").error)
        assertEquals(ProgramError.EMPTY, (GlitchParser.parse(emptyList()) as ParseResult.Error).error)
    }

    @Test
    fun elErrorSenalaLaLinea() {
        val error = parseError("avanzar()\nfor i in range(2):\nwhile not en_meta():\n    avanzar()")
        assertEquals(ProgramError.EMPTY_BODY, error.error)
        assertEquals(1, error.line)
    }

    // ---------- Ejecución ----------
    @Test
    fun avanzarHastaLaMetaEsExito() {
        assertEquals(RunOutcome.SUCCESS, run(listOf(">..M"), "avanzar()\navanzar()\navanzar()"))
    }

    @Test
    fun unForRepiteSuCuerpo() {
        assertEquals(RunOutcome.SUCCESS, run(listOf(">...M"), "for i in range(4):\n    avanzar()"))
        assertEquals(RunOutcome.NOT_AT_GOAL, run(listOf(">...M"), "for i in range(3):\n    avanzar()"))
    }

    @Test
    fun chocarConParedOBordeTerminaElPrograma() {
        assertEquals(RunOutcome.CRASH, run(listOf(">#M"), "avanzar()"))
        assertEquals(RunOutcome.CRASH, run(listOf("<.M"), "avanzar()"))
    }

    @Test
    fun hayQueRecogerTodasLasBaterias() {
        assertEquals(RunOutcome.SUCCESS, run(listOf(">BM"), "avanzar()\navanzar()"))
        val board = listOf(
            ">.M",
            "..B"
        )
        assertEquals(RunOutcome.MISSING_BATTERIES, run(board, "avanzar()\navanzar()"))
        // Recoge la batería de abajo y vuelve a subir a la meta
        val program = "avanzar()\navanzar()\ngirar_derecha()\navanzar()\ngirar_derecha()\ngirar_derecha()\navanzar()"
        assertEquals(RunOutcome.SUCCESS, run(board, program))
    }

    @Test
    fun alLlegarConTodasLasBateriasElProgramaSeDetiene() {
        // Las líneas de más ya no se ejecutan: no choca contra el borde
        assertEquals(RunOutcome.SUCCESS, run(listOf(">M"), "avanzar()\navanzar()\navanzar()"))
    }

    @Test
    fun whileAvanzaHastaLaMeta() {
        assertEquals(RunOutcome.SUCCESS, run(listOf(">.....M"), "while not en_meta():\n    avanzar()"))
    }

    @Test
    fun unWhileQueNoAvanzaMareaAGlitch() {
        assertEquals(RunOutcome.TOO_MANY_STEPS, run(listOf(">.M"), "while not en_meta():\n    girar_izquierda()"))
    }

    @Test
    fun ifHayParedGiraEnLasEsquinas() {
        // Pasillo en forma de L: avanza y gira a la derecha al encontrar pared
        val board = listOf(
            ">..",
            "##.",
            "##M"
        )
        val program = """
            while not en_meta():
                if hay_pared():
                    girar_derecha()
                else:
                    avanzar()
        """
        assertEquals(RunOutcome.SUCCESS, run(board, program))
    }

    @Test
    fun ifNotHayParedFunciona() {
        assertEquals(
            RunOutcome.SUCCESS,
            run(listOf(">.M"), "while not en_meta():\n    if not hay_pared():\n        avanzar()")
        )
    }

    @Test
    fun losPasosGuardanLaLineaYElChoque() {
        val parsed = GlitchParser.parse(GlitchLine.parseProgram("girar_derecha()\navanzar()")) as ParseResult.Ok
        val result = GlitchRunner.run(GlitchBoard.parse(listOf(">M")), parsed.program)
        assertEquals(RunOutcome.CRASH, result.outcome)
        assertEquals(listOf(0, 1), result.steps.map { it.line })
        assertEquals(Direction.SOUTH, result.steps[0].direction)
        assertTrue(result.steps[1].bump)
    }

    // ---------- Editor ----------
    @Test
    fun alInsertarDebajoDeUnForLaLineaQuedaAdentro() {
        val editor = GlitchEditor(emptyList())
            .tapBlock(GlitchBlock.Repeat(2))
            .tapBlock(GlitchBlock.Forward)
            .tapBlock(GlitchBlock.TurnLeft)
        assertEquals(listOf(0, 1, 1), editor.lines.map { it.indent })
        assertEquals(2, editor.selected)
        val out = editor.outdent()
        assertEquals(0, out.lines[2].indent)
        assertEquals(1, out.indent().lines[2].indent)
    }

    @Test
    fun noSePuedeSangrarMasDeLoQuePermiteLaLineaDeArriba() {
        val editor = GlitchEditor(emptyList()).tapBlock(GlitchBlock.Forward).tapBlock(GlitchBlock.Forward)
        assertEquals(0, editor.indent().lines[1].indent)
    }

    @Test
    fun elLimiteDeLineasImpideAgregarMas() {
        val editor = GlitchEditor(emptyList(), maxLines = 1).tapBlock(GlitchBlock.Forward).tapBlock(GlitchBlock.Forward)
        assertEquals(1, editor.lines.size)
        assertFalse(editor.canAdd)
    }

    @Test
    fun borrarQuitaLaLineaElegida() {
        val editor = GlitchEditor(emptyList()).tapBlock(GlitchBlock.Forward).tapBlock(GlitchBlock.TurnLeft).delete()
        assertEquals(listOf<GlitchBlock?>(GlitchBlock.Forward), editor.lines.map { it.block })
        assertEquals(0, editor.selected)
    }

    @Test
    fun elNumeroDelForDaLaVuelta() {
        var editor = GlitchEditor(emptyList()).tapBlock(GlitchBlock.Repeat(8))
        editor = editor.cycleNumber(0)
        assertEquals(GlitchBlock.Repeat(9), editor.lines[0].block)
        assertEquals(GlitchBlock.Repeat(1), editor.cycleNumber(0).lines[0].block)
    }

    @Test
    fun enElModoCompletaSoloSeRellenanLosHuecos() {
        val lines = GlitchLine.parseProgram("avanzar()\n___\n___", lockFilled = true)
        var editor = GlitchEditor(lines, freeEditing = false)
        assertEquals(editor, editor.select(0))                       // Línea fija: no se elige
        editor = editor.tapBlock(GlitchBlock.TurnLeft)              // Rellena el primer hueco
        assertEquals(GlitchBlock.TurnLeft, editor.lines[1].block)
        assertEquals(2, editor.selected)                            // Salta al siguiente hueco
        editor = editor.tapBlock(GlitchBlock.Forward).delete().indent()
        assertEquals(3, editor.lines.size)                          // Sin borrar ni sangrar
        assertEquals(GlitchBlock.Forward, editor.lines[2].block)
        editor = editor.select(1).tapBlock(GlitchBlock.TurnRight)   // Un hueco ya lleno se puede cambiar
        assertEquals(GlitchBlock.TurnRight, editor.lines[1].block)
    }

    // ---------- Ejercicio ----------
    private val puzzle = GlitchPuzzle(
        prompt = "Lleva a Glitch a la meta",
        board = listOf(">..B.M"),
        mode = GlitchMode.BUILD,
        palette = listOf(GlitchBlock.Forward, GlitchBlock.Repeat(2)),
        solution = "for i in range(5):\n    avanzar()",
        maxLines = 2,
        correctFeedback = "¡Bien!",
        wrongFeedback = "Casi"
    )

    @Test
    fun laSolucionDelEjercicioEsCorrecta() {
        assertTrue(puzzle.isCorrect(puzzle.solutionLines))
    }

    @Test
    fun pasarseDelLimiteDeLineasNoValeAunqueLlegue() {
        val long = GlitchLine.parseProgram("avanzar()\navanzar()\navanzar()\navanzar()\navanzar()")
        assertTrue(puzzle.attempt(long) is GlitchAttempt.TooLong)
        assertFalse(puzzle.isCorrect(long))
    }

    @Test
    fun elModoArreglaEmpiezaConElProgramaConBug() {
        val fix = GlitchPuzzle(
            prompt = "Arregla",
            board = listOf(">.M"),
            mode = GlitchMode.FIX,
            palette = listOf(GlitchBlock.Forward),
            starter = "for i in range(1):\n    avanzar()",
            solution = "for i in range(2):\n    avanzar()",
            correctFeedback = "",
            wrongFeedback = ""
        )
        val editor = fix.newEditor()
        assertTrue(editor.freeEditing)
        assertFalse(fix.isCorrect(editor.lines))
        assertTrue(fix.isCorrect(fix.solutionLines))
    }
}
