package me.pablorincon.proyecto_app.domain.glitch

// Tablero del Simulador de Glitch. Se escribe como texto, una fila por cadena:
//   '.' vacío   '#' pared   'B' batería   'M' meta
//   '>' '<' '^' 'v' Glitch y hacia dónde mira
// Ejemplo: listOf(">.B.M", "..#..")
enum class Direction(val dx: Int, val dy: Int) {
    NORTH(0, -1), EAST(1, 0), SOUTH(0, 1), WEST(-1, 0);

    fun turnLeft(): Direction = entries[(ordinal + 3) % 4]
    fun turnRight(): Direction = entries[(ordinal + 1) % 4]
}

data class Cell(val x: Int, val y: Int) {
    fun next(direction: Direction) = Cell(x + direction.dx, y + direction.dy)
}

data class GlitchBoard(
    val width: Int,
    val height: Int,
    val walls: Set<Cell>,
    val batteries: Set<Cell>,
    val goal: Cell,
    val start: Cell,
    val startDirection: Direction
) {
    fun isInside(cell: Cell): Boolean = cell.x in 0 until width && cell.y in 0 until height

    // Bloqueado = pared o borde del tablero (lo que consulta hay_pared()).
    fun isBlocked(cell: Cell): Boolean = !isInside(cell) || cell in walls

    companion object {
        fun parse(rows: List<String>): GlitchBoard {
            require(rows.isNotEmpty()) { "Tablero vacío" }
            val width = rows.first().length
            require(rows.all { it.length == width }) { "Todas las filas deben medir lo mismo: $rows" }
            val walls = mutableSetOf<Cell>()
            val batteries = mutableSetOf<Cell>()
            var goal: Cell? = null
            var start: Cell? = null
            var direction: Direction? = null
            rows.forEachIndexed { y, row ->
                row.forEachIndexed { x, char ->
                    val cell = Cell(x, y)
                    when (char) {
                        '.' -> Unit
                        '#' -> walls += cell
                        'B' -> batteries += cell
                        'M' -> {
                            require(goal == null) { "Hay dos metas: $rows" }
                            goal = cell
                        }
                        '>', '<', '^', 'v' -> {
                            require(start == null) { "Hay dos Glitch: $rows" }
                            start = cell
                            direction = when (char) {
                                '>' -> Direction.EAST
                                '<' -> Direction.WEST
                                '^' -> Direction.NORTH
                                else -> Direction.SOUTH
                            }
                        }
                        else -> throw IllegalArgumentException("Carácter desconocido '$char' en $rows")
                    }
                }
            }
            return GlitchBoard(
                width = width,
                height = rows.size,
                walls = walls,
                batteries = batteries,
                goal = requireNotNull(goal) { "Falta la meta: $rows" },
                start = requireNotNull(start) { "Falta Glitch: $rows" },
                startDirection = direction!!
            )
        }
    }
}
