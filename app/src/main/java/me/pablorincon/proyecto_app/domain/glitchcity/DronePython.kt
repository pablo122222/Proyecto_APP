package me.pablorincon.proyecto_app.domain.glitchcity

// El programa del dron escrito en Python, para que se vea qué hace cada bloque: un bloque ×1 es
// una llamada; un bloque ×n es un for con range(n). Cada paso del vuelo sabe en qué línea va,
// así la pantalla resalta la línea que se ejecuta mientras el dron se mueve.

// Un paso del vuelo: la línea que lo ejecuta, la dirección y, si está en un for, la vuelta.
data class DroneStep(val line: Int, val direction: DroneDirection, val iteration: Int, val times: Int)

// Direcciones de un paso (los bloques →↓ y ↓→ dan dos pasos).
enum class DroneDirection(val function: String, val dx: Int, val dy: Int) {
    Up("mover_arriba", 0, -1),
    Down("mover_abajo", 0, 1),
    Left("mover_izquierda", -1, 0),
    Right("mover_derecha", 1, 0);

    companion object {
        fun of(step: Pair<Int, Int>): DroneDirection = entries.first { it.dx == step.first && it.dy == step.second }
    }
}

// lines: el código; steps: todos los pasos del programa completo, en orden.
data class DroneCode(val lines: List<String>, val steps: List<DroneStep>)

object DronePython {

    fun of(program: List<DroneBlock>): DroneCode {
        val lines = mutableListOf<String>()
        val steps = mutableListOf<DroneStep>()
        program.forEach { block ->
            val directions = block.move.steps.map(DroneDirection::of)
            if (block.times == 1) {
                directions.forEach { direction ->
                    steps += DroneStep(lines.size, direction, iteration = 1, times = 1)
                    lines += "${direction.function}()"
                }
            } else {
                lines += "for paso in range(${block.times}):"
                val firstCall = lines.size
                directions.forEach { lines += "    ${it.function}()" }
                repeat(block.times) { turn ->
                    directions.forEachIndexed { i, direction ->
                        steps += DroneStep(firstCall + i, direction, iteration = turn + 1, times = block.times)
                    }
                }
            }
        }
        return DroneCode(lines, steps)
    }

    // Paso que se está mostrando cuando la animación lleva `visibleCells` casillas (la primera es
    // la salida). Al terminar con choque o salida del mapa, el paso es el que falló.
    fun stepAt(code: DroneCode, run: DroneRun, visibleCells: Int, finished: Boolean): DroneStep? {
        val failed = run.outcome == DroneOutcome.OutOfMap || run.outcome == DroneOutcome.HitWall
        val index = if (finished && failed) run.path.size - 1 else visibleCells - 2
        return code.steps.getOrNull(index)
    }
}
