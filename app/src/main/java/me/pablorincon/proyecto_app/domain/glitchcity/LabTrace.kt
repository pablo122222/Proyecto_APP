package me.pablorincon.proyecto_app.domain.glitchcity

// Trazado del código de un laboratorio (código visible): para cada línea, qué pasó al ejecutarla
// con los valores actuales y una nota para explicarla ("¿Qué hace esta línea?").
// Las notas solo guardan datos; los textos viven en strings.xml.

enum class LineStatus {
    Ran,      // Se ejecutó (o su condición fue verdadera)
    False,    // Se revisó su condición y fue falsa
    Skipped   // No se llegó a ejecutar
}

sealed interface LineNote {
    data class Assign(val name: String, val value: String) : LineNote          // Guarda un valor
    data class Check(val evaluated: String, val holds: Boolean) : LineNote     // if / elif: "3 > 40"
    data object NotReached : LineNote                                          // Una condición anterior ya fue verdadera
    data object ElseRuns : LineNote                                            // Ninguna condición fue verdadera
    data object InsideFalseIf : LineNote                                       // Está dentro de un if que fue falso
    data class Loop(val variable: String, val values: List<Int>) : LineNote    // for: valores que toma la variable
    data class LoopBody(val times: Int) : LineNote                             // Cuerpo del for
    data class ListValue(val name: String, val count: Int) : LineNote          // Lista con count elementos
    data class Call(val function: String, val result: String?) : LineNote     // null: lista vacía
    data class Define(val function: String, val uses: List<Int>) : LineNote    // def: con qué valores se usa
    data class ShopCheck(val minimum: Int, val yes: List<Int>, val no: List<Int>) : LineNote
    data class ReturnDiscount(val results: List<Pair<Int, String>>) : LineNote // compra → paga
    data class ReturnPlain(val purchases: List<Int>) : LineNote
}

data class LineTrace(val status: LineStatus, val note: LineNote)

// Cadena if / elif / else: la primera condición verdadera se ejecuta; las siguientes ni se
// revisan; el else solo corre si ninguna fue verdadera.
internal fun branchTrace(evaluated: List<String>, holds: List<Boolean>, hasElse: Boolean): List<LineTrace> {
    val taken = holds.indexOfFirst { it }
    val branches = evaluated.indices.map { i ->
        when {
            taken in 0 until i -> LineTrace(LineStatus.Skipped, LineNote.NotReached)
            holds[i] -> LineTrace(LineStatus.Ran, LineNote.Check(evaluated[i], true))
            else -> LineTrace(LineStatus.False, LineNote.Check(evaluated[i], false))
        }
    }
    val elseLine = when {
        !hasElse -> emptyList()
        taken >= 0 -> listOf(LineTrace(LineStatus.Skipped, LineNote.NotReached))
        else -> listOf(LineTrace(LineStatus.Ran, LineNote.ElseRuns))
    }
    return branches + elseLine
}
