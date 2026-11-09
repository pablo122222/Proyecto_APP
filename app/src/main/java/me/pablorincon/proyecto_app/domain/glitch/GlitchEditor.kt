package me.pablorincon.proyecto_app.domain.glitch

// Estado del editor de programas: las líneas y la línea seleccionada (el "cursor").
// Inmutable y puro, como LessonQuiz: la pantalla solo guarda el estado y llama a estas funciones.
data class GlitchEditor(
    val lines: List<GlitchLine>,
    val selected: Int? = null,
    val freeEditing: Boolean = true,   // false en el modo Completa: solo se rellenan huecos
    val maxLines: Int? = null
) {
    val canAdd: Boolean get() = freeEditing && (maxLines == null || lines.size < maxLines)
    val selectedLine: GlitchLine? get() = selected?.let { lines.getOrNull(it) }
    val canEditSelected: Boolean get() = freeEditing && selectedLine != null

    fun select(index: Int): GlitchEditor {
        val line = lines.getOrNull(index) ?: return this
        if (line.locked) return this
        return copy(selected = if (selected == index) null else index)
    }

    // Tocar un bloque de la paleta. Con edición libre lo inserta debajo de la línea elegida
    // (o al final) con la sangría que tocaría en Python; en el modo Completa rellena el hueco.
    fun tapBlock(block: GlitchBlock): GlitchEditor = if (freeEditing) insert(block) else fill(block)

    private fun insert(block: GlitchBlock): GlitchEditor {
        if (!canAdd) return this
        val anchor = selected ?: lines.lastIndex
        val above = lines.getOrNull(anchor)
        val indent = when {
            above == null -> 0
            above.block?.opensBody == true -> minOf(above.indent + 1, MAX_INDENT)
            else -> above.indent
        }
        val at = anchor + 1
        val newLines = lines.toMutableList().apply { add(at, GlitchLine(block, indent)) }
        return copy(lines = newLines, selected = at)
    }

    private fun fill(block: GlitchBlock): GlitchEditor {
        val target = selected?.takeIf { lines[it].slot }
            ?: lines.indexOfFirst { it.slot && it.block == null }.takeIf { it >= 0 }
            ?: return this
        return replace(target) { it.copy(block = block) }.copy(selected = nextHoleAfter(target))
    }

    private fun nextHoleAfter(index: Int): Int? {
        val updated = lines.indices.filter { it != index && lines[it].block == null }
        return updated.firstOrNull { it > index } ?: updated.firstOrNull()
    }

    fun indent(): GlitchEditor {
        val index = selected ?: return this
        if (!freeEditing || index == 0) return this
        val above = lines[index - 1]
        val limit = if (above.block?.opensBody == true) above.indent + 1 else above.indent
        val target = minOf(lines[index].indent + 1, limit, MAX_INDENT)
        return replace(index) { it.copy(indent = target) }
    }

    fun outdent(): GlitchEditor {
        val index = selected ?: return this
        if (!freeEditing || lines[index].indent == 0) return this
        return replace(index) { it.copy(indent = it.indent - 1) }
    }

    fun delete(): GlitchEditor {
        val index = selected ?: return this
        if (!freeEditing) return this
        val newLines = lines.toMutableList().apply { removeAt(index) }
        return copy(lines = newLines, selected = (index - 1).takeIf { it >= 0 && newLines.isNotEmpty() })
    }

    // Tocar el número de un for: 1, 2, … 9 y vuelve a 1. Funciona también en el modo Completa.
    fun cycleNumber(index: Int): GlitchEditor {
        val line = lines.getOrNull(index) ?: return this
        val block = line.block as? GlitchBlock.Repeat ?: return this
        if (line.locked) return this
        return replace(index) { it.copy(block = GlitchBlock.Repeat(block.nextTimes())) }
    }

    private fun replace(index: Int, change: (GlitchLine) -> GlitchLine): GlitchEditor =
        copy(lines = lines.toMutableList().apply { this[index] = change(this[index]) })

    companion object {
        const val MAX_INDENT = 3
    }
}
