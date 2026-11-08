package me.pablorincon.proyecto_app.domain.content

import me.pablorincon.proyecto_app.domain.LessonContent
import me.pablorincon.proyecto_app.domain.QuizQuestion

// Módulo 1 · Fundamentos. Hilo conductor: Glitch, un robot muy literal que solo hace
// exactamente lo que se le dice. Las 3 preguntas piden pensar (~1 minuto cada una).
// Por ahora solo está la primera lección (punto (a) de la Fase 10); el resto llega en (b).
internal object FundamentosLessons {

    private val glitchNoLeeMentes = LessonContent(
        lessonId = "fundamentos_1",
        title = "Glitch no lee mentes 🤖",
        hook = "Glitch acaba de despertar en Glitch City. Tiene un cuerpo perfecto y cero ideas de qué hacer: solo obedece órdenes exactas. Si le dices «saluda», se queda mirando al vacío.",
        explanation = "Programar es escribir esas órdenes. En Python, print(...) le dice a Glitch que muestre un texto, y el texto va entre comillas.",
        code = "print(\"Beep boop\")\nprint(\"Soy Glitch\")\nprint(\"Mucho gusto\")",
        expectedOutput = "Beep boop\nSoy Glitch\nMucho gusto",
        questions = listOf(
            QuizQuestion(
                prompt = "Glitch ejecuta este código. ¿Cuántas líneas aparecen en pantalla y cuál es la segunda?",
                options = listOf(
                    "Una línea: «Beep boop Soy Glitch Mucho gusto».",
                    "Tres líneas; la segunda es «Soy Glitch».",
                    "Tres líneas; la segunda es print(\"Soy Glitch\").",
                    "Dos líneas; la segunda es «Mucho gusto»."
                ),
                correctIndex = 1,
                correctFeedback = "¡Exacto! Cada print escribe una línea nueva y las comillas no se ven.",
                wrongFeedback = "Ojo: cada print crea su propia línea, y solo se muestra lo de dentro de las comillas. La segunda línea es Soy Glitch."
            ),
            QuizQuestion(
                prompt = "Quieres que Glitch diga «¡Hola, Juan!». ¿Qué instrucción lo logra?",
                options = listOf(
                    "print(¡Hola, Juan!)",
                    "print(\"¡Hola, Juan!\")",
                    "print \"¡Hola, Juan!\"",
                    "imprime(\"¡Hola, Juan!\")"
                ),
                correctIndex = 1,
                correctFeedback = "¡Sí! Texto entre comillas, dentro de paréntesis y con print.",
                wrongFeedback = "Glitch es muy literal: faltan comillas, paréntesis o el nombre exacto print. Solo una opción tiene las tres cosas."
            ),
            QuizQuestion(
                prompt = "Glitch ejecuta print(\"2 + 2\"). Tu amigo dice que mostrará 4. ¿Quién tiene razón y por qué?",
                options = listOf(
                    "Tu amigo: Python siempre hace las cuentas.",
                    "Nadie: da error.",
                    "Tú: lo que va entre comillas es texto y se muestra tal cual: 2 + 2.",
                    "Tú: muestra \"2 + 2\" con comillas."
                ),
                correctIndex = 2,
                correctFeedback = "¡Bien visto! Entre comillas es texto, Glitch no calcula nada. (Más adelante le enseñaremos a sumar.)",
                wrongFeedback = "Las comillas convierten todo en texto, incluso los números. Glitch lo muestra tal cual, sin calcular y sin las comillas."
            )
        )
    )

    val all: List<LessonContent> = listOf(glitchNoLeeMentes)
}
