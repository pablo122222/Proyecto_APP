package me.pablorincon.proyecto_app.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExerciseTest {

    private fun order(vararg lines: String) = OrderLines("Ordena", lines.toList(), "bien", "mal")

    // ---------- Ordena el código ----------
    @Test
    fun ordenarAceptaSoloElOrdenCorrecto() {
        val ex = order("a", "b", "c")
        assertTrue(ex.isCorrect(listOf("a", "b", "c")))
        assertFalse(ex.isCorrect(listOf("b", "a", "c")))
    }

    @Test
    fun elOrdenInicialNuncaEsLaSolucion() {
        listOf(
            order("a", "b"),
            order("print(1)", "print(2)", "print(3)", "print(4)"),
            order("x", "x", "y") // Con líneas repetidas también
        ).forEach { ex ->
            (0..30).forEach { step ->
                val arranged = ex.initialOrder(step).map { ex.lines[it] }
                assertFalse("${ex.lines} paso $step", ex.isCorrect(arranged))
                assertEquals(ex.lines.sorted(), arranged.sorted()) // Usa todas las líneas, sin perder ninguna
            }
        }
    }

    @Test
    fun elOrdenInicialEsElMismoParaElMismoPaso() {
        val ex = order("a", "b", "c", "d")
        assertEquals(ex.initialOrder(3), ex.initialOrder(3))
    }

    // ---------- Completa el hueco ----------
    private val fill = FillBlanks(
        prompt = "Completa",
        codeTemplate = "___(\"Hola\")\nedad ___ 20",
        answers = listOf("print", "="),
        chips = listOf("print", "=", "==", "input"),
        correctFeedback = "bien",
        wrongFeedback = "mal"
    )

    @Test
    fun completarCuentaLosHuecosYValidaFichas() {
        assertEquals(2, fill.blankCount)
        assertTrue(fill.isCorrect(listOf("print", "=")))
        assertFalse(fill.isCorrect(listOf("print", "==")))
        assertFalse(fill.isCorrect(listOf("print", null)))
    }

    @Test
    fun completarUsaTodasLasFichasAlMezclar() {
        assertEquals(listOf(0, 1, 2, 3), fill.chipOrder(0).sorted())
    }

    // ---------- Caza el bug ----------
    @Test
    fun cazarElBugAceptaSoloLaLineaCorrecta() {
        val ex = FindBug("Bug", listOf("a", "b", "c"), bugLineIndex = 1, correctFeedback = "bien", wrongFeedback = "mal")
        assertTrue(ex.isCorrect(1))
        assertFalse(ex.isCorrect(0))
    }

    // ---------- Predice la salida ----------
    private fun predict(vararg answers: String, ignoreCase: Boolean = true) =
        PredictOutput("Predice", "print(1)", answers.toList(), ignoreCase, "bien", "mal")

    @Test
    fun predecirIgnoraEspaciosYSaltosDeLinea() {
        val ex = predict("A\nB\nC")
        assertTrue(ex.isCorrect("A B C"))
        assertTrue(ex.isCorrect("  A\n\nB   C \n"))
        assertFalse(ex.isCorrect("A C B"))
    }

    @Test
    fun predecirIgnoraMayusculasSoloSiSeIndica() {
        assertTrue(predict("True").isCorrect("true"))
        assertFalse(predict("True", ignoreCase = false).isCorrect("true"))
        assertTrue(predict("True", ignoreCase = false).isCorrect("True"))
    }

    @Test
    fun predecirNoAceptaVacioYAceptaVariasRespuestas() {
        val ex = predict("70", "setenta")
        assertFalse(ex.isCorrect(""))
        assertFalse(ex.isCorrect("   "))
        assertTrue(ex.isCorrect("setenta"))
    }

    // ---------- Escribe el código ----------
    private val type = TypeCode("Escribe", listOf("print(\"Hola\")"), correctFeedback = "bien", wrongFeedback = "mal")

    @Test
    fun escribirIgnoraEspaciosYTipoDeComillas() {
        assertTrue(type.isCorrect("print(\"Hola\")"))
        assertTrue(type.isCorrect("print('Hola')"))
        assertTrue(type.isCorrect("print( \"Hola\" )"))
        assertTrue(type.isCorrect("print(“Hola”)")) // Comillas tipográficas del teclado
    }

    @Test
    fun escribirDistingueMayusculasYRechazaVacio() {
        assertFalse(type.isCorrect("Print(\"Hola\")"))
        assertFalse(type.isCorrect("print(\"hola\")"))
        assertFalse(type.isCorrect(""))
        assertFalse(type.isCorrect("print(\"Hola\""))
    }

    // ---------- Opción múltiple ----------
    private val choice = MultipleChoice("Elige", listOf("a", "b", "c", "d"), 1, "bien", "mal")

    @Test
    fun opcionMultipleMezclaSinPerderOpciones() {
        (0..20).forEach { step ->
            assertEquals(listOf(0, 1, 2, 3), choice.displayOrder(step).sorted())
        }
    }

    @Test
    fun laCorrectaNoSeQuedaSiempreEnLaMismaPosicion() {
        val positions = (0..30).map { step -> choice.displayOrder(step).indexOf(choice.correctIndex) }.toSet()
        assertTrue("La respuesta correcta siempre en ${positions}", positions.size > 1)
        assertNotEquals(setOf(1), positions)
    }
}
