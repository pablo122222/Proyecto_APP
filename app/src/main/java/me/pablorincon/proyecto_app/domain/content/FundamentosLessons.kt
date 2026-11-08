package me.pablorincon.proyecto_app.domain.content

import me.pablorincon.proyecto_app.domain.FillBlanks
import me.pablorincon.proyecto_app.domain.FindBug
import me.pablorincon.proyecto_app.domain.LessonContent
import me.pablorincon.proyecto_app.domain.MultipleChoice
import me.pablorincon.proyecto_app.domain.OrderLines
import me.pablorincon.proyecto_app.domain.PredictOutput
import me.pablorincon.proyecto_app.domain.TypeCode

// Módulo 1 · Fundamentos. Hilo conductor: Glitch, un robot muy literal que solo hace
// exactamente lo que se le dice. Cada lección tiene 3 ejercicios dinámicos (~1 minuto cada uno).
// Contenido aprobado por el dueño del proyecto (decisión C), versión con ejercicios dinámicos.
internal object FundamentosLessons {

    private val glitchNoLeeMentes = LessonContent(
        lessonId = "fundamentos_1",
        title = "Glitch no lee mentes 🤖",
        hook = "Glitch acaba de despertar en Glitch City. Tiene un cuerpo perfecto y cero ideas de qué hacer: solo obedece órdenes exactas. Si le dices «saluda», se queda mirando al vacío.",
        explanation = "Programar es escribir esas órdenes. En Python, print(...) le dice a Glitch que muestre un texto, y el texto va entre comillas.",
        code = "print(\"Beep boop\")\nprint(\"Soy Glitch\")\nprint(\"Mucho gusto\")",
        expectedOutput = "Beep boop\nSoy Glitch\nMucho gusto",
        exercises = listOf(
            TypeCode(
                prompt = "Glitch no adivina nada: dile exactamente qué decir. Escribe la instrucción para que diga ¡Hola, Juan!",
                acceptedAnswers = listOf("print(\"¡Hola, Juan!\")"),
                placeholder = "print(...)",
                correctFeedback = "¡Eso! Texto entre comillas, dentro de paréntesis y con print: Glitch lo dice tal cual.",
                wrongFeedback = "Glitch es muy literal: necesita la palabra print, los paréntesis y el texto entre comillas, escrito tal cual (con la ¡ y la coma)."
            ),
            FillBlanks(
                prompt = "Glitch debe decir dos frases. Completa las órdenes.",
                codeTemplate = "___(\"Beep boop\")\n___(\"Soy Glitch\")",
                answers = listOf("print", "print"),
                chips = listOf("print", "print", "imprime", "Print"),
                correctFeedback = "¡Bien! La orden se llama print, en minúsculas, y se usa una vez por cada frase.",
                wrongFeedback = "Glitch solo entiende la palabra exacta. Python distingue mayúsculas y no sabe español: no vale imprime ni Print."
            ),
            PredictOutput(
                prompt = "¿Qué muestra este programa en pantalla? Escríbelo línea por línea.",
                code = "print(\"Beep boop\")\nprint(\"2 + 2\")\nprint(\"Soy Glitch\")",
                acceptedAnswers = listOf("Beep boop\n2 + 2\nSoy Glitch"),
                correctFeedback = "¡Bien visto! Entre comillas es texto: Glitch no calcula 2 + 2, lo muestra tal cual y sin las comillas.",
                wrongFeedback = "Ojo: cada print escribe una línea nueva y las comillas no se ven. Lo que va dentro, incluso un 2 + 2, se muestra tal cual."
            )
        )
    )

    private val elOrdenImporta = LessonContent(
        lessonId = "fundamentos_2",
        title = "El orden importa 🚀",
        hook = "Glitch va a despegar con su nave. Pero primero hay que hacerlo todo en orden: ¡nadie despega con los motores apagados!",
        explanation = "Python lee de arriba hacia abajo, una línea tras otra. El orden decide el resultado.",
        code = "print(\"Prender motores\")\nprint(\"Cuenta regresiva: 3, 2, 1\")\nprint(\"¡Despegue!\")\nprint(\"Aterrizar en Glitch City\")",
        expectedOutput = "Prender motores\nCuenta regresiva: 3, 2, 1\n¡Despegue!\nAterrizar en Glitch City",
        exercises = listOf(
            OrderLines(
                prompt = "La nave de Glitch quedó desordenada. Ordena las instrucciones para un vuelo perfecto.",
                lines = listOf(
                    "print(\"Prender motores\")",
                    "print(\"Cuenta regresiva: 3, 2, 1\")",
                    "print(\"¡Despegue!\")",
                    "print(\"Aterrizar en Glitch City\")"
                ),
                correctFeedback = "¡Vuelo perfecto! Python ejecuta las líneas de arriba hacia abajo, justo en el orden que escribiste.",
                wrongFeedback = "Imagina el vuelo de Glitch: ¿qué tiene que pasar primero para poder despegar, y qué solo puede ocurrir al final?"
            ),
            PredictOutput(
                prompt = "Glitch ejecuta este programa. ¿Qué números muestra y en qué orden?",
                code = "print(\"1\")\nprint(\"3\")\nprint(\"2\")\nprint(\"3\")",
                acceptedAnswers = listOf("1\n3\n2\n3"),
                correctFeedback = "¡Correcto! Cada línea se ejecuta cuando le toca, aunque repitas el contenido.",
                wrongFeedback = "Ve línea por línea, de arriba hacia abajo: Python no reordena nada, ni siquiera para que los números queden bonitos."
            ),
            FindBug(
                prompt = "¡Glitch despegó sin casco! ¿Qué línea hay que mover antes del despegue?",
                codeLines = listOf(
                    "print(\"Abrir la puerta\")",
                    "print(\"Entrar a la nave\")",
                    "print(\"Cerrar la puerta\")",
                    "print(\"Prender motores\")",
                    "print(\"Despegar\")",
                    "print(\"Ponerse el casco\")"
                ),
                bugLineIndex = 5,
                correctFeedback = "¡Exacto! El casco va antes de despegar: el orden de las líneas decide cuándo pasa cada cosa.",
                wrongFeedback = "Piensa qué debería haber pasado antes de «Despegar». Busca la línea que ocurre demasiado tarde."
            )
        )
    )

    private val cazadoresDeBugs = LessonContent(
        lessonId = "fundamentos_3",
        title = "Cazadores de bugs 🐛",
        hook = "Un bug se coló en la nave de Glitch (¡un bichito de verdad, dice la leyenda!). Hoy te toca cazarlo.",
        explanation = "Un error en el código se llama bug, y todos los programadores los tienen. Python te dice dónde falló con un mensaje: léelo como una pista. También puedes dejar notas con #: Python las ignora.",
        code = "# Saludo de la nave\nprint(\"Hola, tripulación)\nprint(\"Bienvenidos a bordo\")",
        expectedOutput = "SyntaxError: unterminated string literal (detected at line 2)",
        exercises = listOf(
            FindBug(
                prompt = "Python gritó: SyntaxError: unterminated string literal (detected at line 2). Toca la línea con el bug.",
                codeLines = listOf(
                    "# Saludo de la nave",
                    "print(\"Hola, tripulación)",
                    "print(\"Bienvenidos a bordo\")"
                ),
                bugLineIndex = 1,
                correctFeedback = "¡Bug cazado! El texto empezó con comillas pero nunca las cerró.",
                wrongFeedback = "El mensaje dice «unterminated» (sin terminar) y la línea 2. Busca algo que se abre y no se cierra."
            ),
            FillBlanks(
                prompt = "Glitch quiere dejar una nota para humanos que Python ignore. Completa la primera línea.",
                codeTemplate = "___ Esta línea es solo una nota\nprint(\"Hola\")",
                answers = listOf("#"),
                chips = listOf("#", "//", "--", "/*"),
                correctFeedback = "¡Sí! Todo lo que va después de # es solo una nota para humanos.",
                wrongFeedback = "Python usa un símbolo concreto para los comentarios. Los otros pertenecen a otros lenguajes de programación."
            ),
            TypeCode(
                prompt = "Este código tiene un bug: prin(\"Hola\"). Escríbelo bien.",
                acceptedAnswers = listOf("print(\"Hola\")"),
                placeholder = "print(...)",
                correctFeedback = "¡Bug cazado! prin no existe: el comando se llama print.",
                wrongFeedback = "Mira la palabra antes del paréntesis: le falta una letra. Escribe la línea completa y corregida."
            )
        )
    )

    private val divideYVenceras = LessonContent(
        lessonId = "fundamentos_4",
        title = "Divide y vencerás 🍋",
        hook = "Glitch tiene sed. Si le dices «haz limonada», se queda en blanco. Hay que dividir el problema en pasos pequeños.",
        explanation = "Una lista de pasos ordenados para resolver algo se llama algoritmo. Dividir un problema grande en pasos chicos lo hace más fácil de resolver y de arreglar.",
        code = "print(\"Exprimir los limones\")\nprint(\"Echar agua y azúcar\")\nprint(\"Mezclar\")\nprint(\"Servir\")",
        expectedOutput = "Exprimir los limones\nEchar agua y azúcar\nMezclar\nServir",
        exercises = listOf(
            OrderLines(
                prompt = "Glitch tiene sed. Ordena los pasos de la limonada para que le salga perfecta.",
                lines = listOf(
                    "print(\"Exprimir los limones\")",
                    "print(\"Echar agua y azúcar\")",
                    "print(\"Mezclar\")",
                    "print(\"Servir\")"
                ),
                correctFeedback = "¡Eso! Un algoritmo es una lista de pasos en el orden correcto.",
                wrongFeedback = "Piensa en qué necesita cada paso: no puedes mezclar lo que aún no echaste, ni servir lo que aún no preparaste."
            ),
            FillBlanks(
                prompt = "Quieres la limonada fría. Completa el paso que falta entre «Mezclar» y «Servir».",
                codeTemplate = "print(\"Exprimir los limones\")\nprint(\"Echar agua y azúcar\")\nprint(\"Mezclar\")\n___\nprint(\"Servir\")",
                answers = listOf("print(\"Poner hielo\")"),
                chips = listOf(
                    "print(\"Poner hielo\")",
                    "print(\"Exprimir los limones\")",
                    "print(\"Beber\")",
                    "print(\"Tirar la limonada\")"
                ),
                correctFeedback = "¡Muy bien! Hielo después de mezclar y antes de servir: fría y lista.",
                wrongFeedback = "El paso que falta debe ayudar a que la limonada quede fría, y tiene que ocurrir antes de servir."
            ),
            MultipleChoice(
                prompt = "Tienes que «preparar una pizza» y el problema se ve enorme. ¿Cuál es el mejor primer paso como programador?",
                options = listOf(
                    "Escribir todo el código de una vez.",
                    "Dividirla en pasos pequeños: masa, salsa, queso, hornear.",
                    "Esperar a que Python lo resuelva solo.",
                    "Empezar por hornear."
                ),
                correctIndex = 1,
                correctFeedback = "¡Así se piensa! Primero divides, luego resuelves cada trocito.",
                wrongFeedback = "Un problema grande se resuelve en partes: lista primero los pasos pequeños."
            )
        )
    )

    val all: List<LessonContent> = listOf(glitchNoLeeMentes, elOrdenImporta, cazadoresDeBugs, divideYVenceras)
}
