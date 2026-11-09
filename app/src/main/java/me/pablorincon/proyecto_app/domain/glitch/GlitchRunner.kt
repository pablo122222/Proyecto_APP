package me.pablorincon.proyecto_app.domain.glitch

enum class RunOutcome {
    SUCCESS,            // Llegó a la meta con todas las baterías
    CRASH,              // Chocó con una pared o con el borde
    NOT_AT_GOAL,        // El programa terminó lejos de la meta
    MISSING_BATTERIES,  // Terminó en la meta pero dejó baterías
    TOO_MANY_STEPS      // Bucle infinito (o casi): Glitch se mareó
}

// Una foto de Glitch después de una instrucción, para animar paso a paso.
data class GlitchStep(
    val line: Int,
    val cell: Cell,
    val direction: Direction,
    val batteriesLeft: Set<Cell>,
    val bump: Boolean = false    // Intentó avanzar contra una pared
)

data class GlitchRun(val steps: List<GlitchStep>, val outcome: RunOutcome)

// Ejecuta un programa sobre un tablero. Puro y determinista: se prueba sin pantalla.
// Si Glitch llega a la meta con todas las baterías, el programa se detiene ahí (éxito).
class GlitchRunner(private val board: GlitchBoard) {

    private var cell = board.start
    private var direction = board.startDirection
    private var batteries = board.batteries
    private var ticks = 0
    private val steps = mutableListOf<GlitchStep>()

    private class Stop(val outcome: RunOutcome) : Exception()

    fun run(program: List<Statement>): GlitchRun {
        val outcome = try {
            execute(program)
            when {
                cell != board.goal -> RunOutcome.NOT_AT_GOAL
                batteries.isNotEmpty() -> RunOutcome.MISSING_BATTERIES
                else -> RunOutcome.SUCCESS // Programa vacío sobre un tablero ya resuelto
            }
        } catch (stop: Stop) {
            stop.outcome
        }
        return GlitchRun(steps.toList(), outcome)
    }

    private fun execute(statements: List<Statement>) {
        statements.forEach { statement ->
            tick()
            when (statement) {
                is Statement.Action -> act(statement)
                is Statement.Loop -> repeat(statement.times) { execute(statement.body) }
                is Statement.While -> while (cell != board.goal) {
                    tick()
                    execute(statement.body)
                }
                is Statement.If -> {
                    val wall = board.isBlocked(cell.next(direction))
                    execute(if (wall != statement.negated) statement.then else statement.otherwise)
                }
            }
        }
    }

    private fun act(action: Statement.Action) {
        when (action.block) {
            GlitchBlock.Forward -> {
                val target = cell.next(direction)
                if (board.isBlocked(target)) {
                    steps += GlitchStep(action.line, cell, direction, batteries, bump = true)
                    throw Stop(RunOutcome.CRASH)
                }
                cell = target
                batteries = batteries - target
            }
            GlitchBlock.TurnLeft -> direction = direction.turnLeft()
            GlitchBlock.TurnRight -> direction = direction.turnRight()
            else -> error("No es una acción: ${action.block}")
        }
        steps += GlitchStep(action.line, cell, direction, batteries)
        if (steps.size > MAX_STEPS) throw Stop(RunOutcome.TOO_MANY_STEPS)
        if (cell == board.goal && batteries.isEmpty()) throw Stop(RunOutcome.SUCCESS)
    }

    // Cuenta también las vueltas sin movimiento (un while que solo gira para siempre).
    private fun tick() {
        ticks++
        if (ticks > MAX_TICKS) throw Stop(RunOutcome.TOO_MANY_STEPS)
    }

    companion object {
        const val MAX_STEPS = 200
        private const val MAX_TICKS = 2_000

        fun run(board: GlitchBoard, program: List<Statement>): GlitchRun = GlitchRunner(board).run(program)
    }
}
