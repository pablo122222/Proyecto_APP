package me.pablorincon.proyecto_app.domain.content

import me.pablorincon.proyecto_app.domain.LessonContent
import me.pablorincon.proyecto_app.domain.QuizQuestion

// Módulo 1 · Fundamentos. Hilo conductor: Glitch, un robot muy literal que solo hace
// exactamente lo que se le dice. Las 3 preguntas piden pensar (~1 minuto cada una).
// Las 4 lecciones fueron aprobadas por el dueño del proyecto (decisión C).
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

    private val elOrdenImporta = LessonContent(
        lessonId = "fundamentos_2",
        title = "El orden importa 🚀",
        hook = "Glitch va a despegar con su nave. Pero primero hay que hacerlo todo en orden: ¡nadie despega con los motores apagados!",
        explanation = "Python lee de arriba hacia abajo, una línea tras otra. El orden decide el resultado.",
        code = "print(\"Prender motores\")\nprint(\"Cuenta regresiva: 3, 2, 1\")\nprint(\"¡Despegue!\")\nprint(\"Aterrizar en Glitch City\")",
        expectedOutput = "Prender motores\nCuenta regresiva: 3, 2, 1\n¡Despegue!\nAterrizar en Glitch City",
        questions = listOf(
            QuizQuestion(
                prompt = "Un compañero cambió de lugar las líneas 2 y 3. ¿Qué verá el usuario?",
                options = listOf(
                    "Despegue → cuenta regresiva → motores → aterrizar.",
                    "Motores → despegue → cuenta regresiva → aterrizar.",
                    "Un error, porque el orden no se puede cambiar.",
                    "Exactamente lo mismo que antes."
                ),
                correctIndex = 1,
                correctFeedback = "¡Eso! No hay error, pero Glitch despegó y recién después hizo la cuenta regresiva. ¡Qué desastre!",
                wrongFeedback = "Python no se queja, simplemente obedece el nuevo orden: línea 1, luego la 3, luego la 2 y por último la 4."
            ),
            QuizQuestion(
                prompt = "Quieres que «Aterrizar en Glitch City» salga primero, sin borrar nada. ¿Qué haces?",
                options = listOf(
                    "Moverla a la primera línea del código.",
                    "Escribirla en mayúsculas.",
                    "Ponerle un 1 al principio del texto.",
                    "No se puede: el orden es fijo."
                ),
                correctIndex = 0,
                correctFeedback = "¡Sí! Para cambiar cuándo sucede algo, cambias su posición.",
                wrongFeedback = "Python ordena solo por posición: lo de arriba sale antes. Mayúsculas o números dentro del texto no cambian cuándo se ejecuta."
            ),
            QuizQuestion(
                prompt = "Glitch ejecuta: print(\"A\"), print(\"B\"), print(\"C\") y otra vez print(\"B\"), en ese orden. ¿Cuál es la salida completa?",
                options = listOf(
                    "A B C",
                    "A B C B",
                    "A C B",
                    "B A C B"
                ),
                correctIndex = 1,
                correctFeedback = "¡Correcto! Cada línea se ejecuta cuando le toca, aunque repitas el contenido.",
                wrongFeedback = "Ve línea por línea: A, luego B, luego C, y la última línea vuelve a mostrar B."
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
        questions = listOf(
            QuizQuestion(
                prompt = "Python dice: SyntaxError: unterminated string literal (detected at line 2). ¿Qué falló?",
                options = listOf(
                    "Falta cerrar las comillas en la línea 2.",
                    "La línea 3 está mal escrita.",
                    "El comentario de arriba.",
                    "print no existe."
                ),
                correctIndex = 0,
                correctFeedback = "¡Bug cazado! El texto empezó con comillas pero nunca las cerró.",
                wrongFeedback = "El mensaje dice «unterminated» (sin terminar) y la línea 2: busca algo que se abre y no se cierra."
            ),
            QuizQuestion(
                prompt = "Escribes la línea # print(\"Hola\") en tu programa. ¿Qué pasa al ejecutarlo?",
                options = listOf(
                    "Muestra Hola.",
                    "Muestra # print(\"Hola\").",
                    "No pasa nada visible: Python la ignora.",
                    "Da error."
                ),
                correctIndex = 2,
                correctFeedback = "¡Sí! Todo lo que va después de # es solo una nota para humanos.",
                wrongFeedback = "El # convierte la línea en comentario: Python se la salta, sin mostrar nada ni quejarse."
            ),
            QuizQuestion(
                prompt = "Glitch muestra «Bienvenidos a bord» en vez de «Bienvenidos a bordo». ¿Qué tipo de problema es?",
                options = listOf(
                    "Python mostró mal el texto.",
                    "Un error de escritura nuestro dentro de las comillas: Python muestra justo lo que le escribimos.",
                    "Un bug de la computadora.",
                    "Falta un # al principio."
                ),
                correctIndex = 1,
                correctFeedback = "¡Exacto! Python no corrige tu ortografía: muestra justo lo que escribiste.",
                wrongFeedback = "Python es literal como Glitch: si dentro de las comillas falta una letra, lo muestra así. Revisa lo que escribiste."
            )
        )
    )

    private val divideYVenceras = LessonContent(
        lessonId = "fundamentos_4",
        title = "Divide y vencerás 🍋",
        hook = "Glitch tiene sed. Si le dices «haz limonada», se queda en blanco. Hay que dividir el problema en pasos pequeños.",
        explanation = "Una lista de pasos ordenados para resolver algo se llama algoritmo. Dividir un problema grande en pasos chicos lo hace más fácil de resolver y de arreglar.",
        code = "print(\"1. Exprimir los limones\")\nprint(\"2. Echar agua y azúcar\")\nprint(\"3. Mezclar\")\nprint(\"4. Servir\")",
        expectedOutput = "1. Exprimir los limones\n2. Echar agua y azúcar\n3. Mezclar\n4. Servir",
        questions = listOf(
            QuizQuestion(
                prompt = "Glitch hace los pasos en este orden: 1, 3, 2, 4. ¿Cuál es el problema?",
                options = listOf(
                    "Mezcló antes de echar los ingredientes, así que no había nada que mezclar.",
                    "Ninguno: el resultado es igual.",
                    "Sirvió antes de tiempo.",
                    "Exprimió los limones dos veces."
                ),
                correctIndex = 0,
                correctFeedback = "¡Eso! Los pasos dependen unos de otros: no puedes mezclar lo que aún no echaste.",
                wrongFeedback = "Piensa qué hay en el vaso en el paso 3 si el paso 2 aún no ocurrió."
            ),
            QuizQuestion(
                prompt = "Quieres añadir «Poner hielo» para que la limonada quede fría. ¿Dónde debe ir?",
                options = listOf(
                    "Al final, después de servir.",
                    "Entre «Mezclar» y «Servir».",
                    "Antes de exprimir los limones.",
                    "En cualquier lugar, da igual."
                ),
                correctIndex = 1,
                correctFeedback = "¡Muy bien! Hielo después de mezclar y antes de servir: fría y lista.",
                wrongFeedback = "El hielo debe estar antes de servir, pero después de tener la mezcla lista."
            ),
            QuizQuestion(
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

    val all: List<LessonContent> =
        listOf(glitchNoLeeMentes, elOrdenImporta, cazadoresDeBugs, divideYVenceras)
}
