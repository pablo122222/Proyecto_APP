package me.pablorincon.proyecto_app.domain.glitchcity

// Pregunta "¿Por qué funciona?" (decisión D): aparece la primera vez que se publica una creación
// o se completa un nivel del dron. Los textos viven en strings.xml; aquí solo cuántas opciones
// hay y cuál es la correcta (no siempre la misma posición).
data class WhyQuestion(val activityId: String, val optionCount: Int, val correctIndex: Int)

object WhyQuestions {
    val all: List<WhyQuestion> = listOf(
        WhyQuestion(GlitchCityCatalog.OFFERS_SHOP, optionCount = 3, correctIndex = 1),
        WhyQuestion(GlitchCityCatalog.NPC_RULES, optionCount = 3, correctIndex = 2),
        WhyQuestion("d1", optionCount = 3, correctIndex = 0),
        WhyQuestion("d2", optionCount = 3, correctIndex = 1),
        WhyQuestion("d3", optionCount = 3, correctIndex = 2),
        WhyQuestion("d4", optionCount = 3, correctIndex = 1),
        WhyQuestion("d5", optionCount = 3, correctIndex = 0)
    )

    fun forActivity(activityId: String): WhyQuestion? = all.firstOrNull { it.activityId == activityId }
}
