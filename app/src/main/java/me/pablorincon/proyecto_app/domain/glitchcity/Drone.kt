package me.pablorincon.proyecto_app.domain.glitchcity

import me.pablorincon.proyecto_app.domain.glitch.Cell

// Dron de Competir (guía §5.3): un programa de bloques con flecha y repetición ×n.
// Es más simple que el Simulador de Glitch de las lecciones y por eso va aparte.

// Movimientos de la paleta. →↓ y ↓→ son bloques de dos pasos.
enum class DroneMove(val steps: List<Pair<Int, Int>>) {
    UP(listOf(0 to -1)),
    DOWN(listOf(0 to 1)),
    LEFT(listOf(-1 to 0)),
    RIGHT(listOf(1 to 0)),
    RIGHT_DOWN(listOf(1 to 0, 0 to 1)),
    DOWN_RIGHT(listOf(0 to 1, 1 to 0))
}

data class DroneBlock(val move: DroneMove, val times: Int = 1) {
    init {
        require(times in 1..MAX_REPEAT) { "Repetición fuera de rango: $times" }
    }

    companion object {
        const val MAX_REPEAT = 4   // Botones ×1 ×2 ×3 ×4
    }
}

// Mapa del dron, escrito como texto: 'S' salida, 'G' meta, '#' muro, '.' libre.
data class DroneMap(val width: Int, val height: Int, val walls: Set<Cell>, val start: Cell, val goal: Cell) {

    fun isInside(cell: Cell): Boolean = cell.x in 0 until width && cell.y in 0 until height

    companion object {
        fun parse(rows: List<String>): DroneMap {
            require(rows.isNotEmpty() && rows.all { it.length == rows.first().length }) { "Mapa mal formado: $rows" }
            val walls = mutableSetOf<Cell>()
            var start: Cell? = null
            var goal: Cell? = null
            rows.forEachIndexed { y, row ->
                row.forEachIndexed { x, c ->
                    when (c) {
                        '#' -> walls += Cell(x, y)
                        'S' -> start = Cell(x, y)
                        'G' -> goal = Cell(x, y)
                        '.' -> Unit
                        else -> error("Carácter desconocido '$c' en $rows")
                    }
                }
            }
            return DroneMap(rows.first().length, rows.size, walls, requireNotNull(start), requireNotNull(goal))
        }
    }
}

// Resultado de ejecutar un programa. Cada caso tiene su retroalimentación en la guía.
sealed interface DroneOutcome {
    data object Empty : DroneOutcome                                   // Programa vacío
    data object OutOfMap : DroneOutcome                                // Se sale del mapa
    data object HitWall : DroneOutcome                                 // Choca con un muro
    data object WrongCell : DroneOutcome                               // Termina en otra casilla
    data class TooManyBlocks(val blocks: Int, val max: Int) : DroneOutcome // Llegó, pero pasó el máximo
    data class Delivered(val blocks: Int, val stars: Int) : DroneOutcome
}

// path: casillas recorridas desde la salida; crash: el muro con el que chocó, si chocó.
data class DroneRun(val path: List<Cell>, val crash: Cell?, val outcome: DroneOutcome)

object Drone {

    // Ejecuta cada bloque n veces, paso a paso, y se detiene en cuanto llega a la meta.
    fun run(level: DroneLevel, program: List<DroneBlock>): DroneRun {
        val map = level.map
        if (program.isEmpty()) return DroneRun(listOf(map.start), null, DroneOutcome.Empty)
        val path = mutableListOf(map.start)
        var position = map.start
        for (block in program) {
            repeat(block.times) {
                for ((dx, dy) in block.move.steps) {
                    val next = Cell(position.x + dx, position.y + dy)
                    if (!map.isInside(next)) return DroneRun(path, null, DroneOutcome.OutOfMap)
                    if (next in map.walls) return DroneRun(path, next, DroneOutcome.HitWall)
                    position = next
                    path += next
                    if (position == map.goal) return DroneRun(path, null, delivered(level, program.size))
                }
            }
        }
        return DroneRun(path, null, DroneOutcome.WrongCell)
    }

    private fun delivered(level: DroneLevel, blocks: Int): DroneOutcome {
        val max = level.maxBlocks
        return if (max != null && blocks > max) DroneOutcome.TooManyBlocks(blocks, max)
        else DroneOutcome.Delivered(blocks, level.starsFor(blocks))
    }
}
