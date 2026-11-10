package me.pablorincon.proyecto_app.domain.content

import me.pablorincon.proyecto_app.domain.BossContent
import me.pablorincon.proyecto_app.domain.FillBlanks
import me.pablorincon.proyecto_app.domain.FindBug
import me.pablorincon.proyecto_app.domain.GlitchMode
import me.pablorincon.proyecto_app.domain.GlitchPuzzle
import me.pablorincon.proyecto_app.domain.LessonContent
import me.pablorincon.proyecto_app.domain.MultipleChoice
import me.pablorincon.proyecto_app.domain.OrderLines
import me.pablorincon.proyecto_app.domain.PredictOutput
import me.pablorincon.proyecto_app.domain.TypeCode
import me.pablorincon.proyecto_app.domain.WalkStep
import me.pablorincon.proyecto_app.domain.glitch.GlitchBlock

// Nivel 9 · Bucles avanzados (11 lecciones y el jefe «La Matrioska Loca»): bucles anidados,
// break y continue, con Glitch como hilo conductor. Cada ejemplo trae su "Ver paso a paso".
internal object Level09Lessons {

    private fun step(line: Int, note: String, vars: List<Pair<String, String>>, out: List<String> = emptyList()) =
        WalkStep(line, note, vars, out)

    private val bucleDentro = LessonContent(
        lessonId = "n09_l01",
        title = "Un bucle dentro de otro 🪆",
        hook = "Glitch revisa un estante de 2 filas con 2 cajas cada una. Para cada fila, recorre todas sus cajas.",
        explanation = "Un for puede ir dentro de otro. Por cada vuelta del de afuera, el de adentro da todas sus vueltas desde el principio. Mira la sangría: lo que está más a la derecha pertenece al bucle de adentro.",
        code = "for i in range(2):\n    for j in range(2):\n        print(i, j)",
        expectedOutput = "0 0\n0 1\n1 0\n1 1",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "for i in range(2):\n    for j in range(3):\n        print(i, j)",
                acceptedAnswers = listOf("0 0\n0 1\n0 2\n1 0\n1 1\n1 2"),
                correctFeedback = "¡Exacto! Para i = 0, j recorre 0, 1 y 2; luego lo mismo para i = 1.",
                wrongFeedback = "Fija i y recorre todos los valores de j antes de pasar al siguiente i."
            ),
            OrderLines(
                prompt = "Ordena el programa: un for de afuera, uno de adentro y el print dentro de ambos.",
                lines = listOf(
                    "for i in range(2):",
                    "    for j in range(2):",
                    "        print(i, j)"
                ),
                correctFeedback = "¡Perfecto! Cada nivel de sangría va dentro del anterior.",
                wrongFeedback = "Primero el for de afuera, luego el de adentro con más sangría y al final el print."
            ),
            FillBlanks(
                prompt = "Completa el bucle de adentro.",
                codeTemplate = "for i in range(3):\n    ___ j in range(2):\n        print(i, j)",
                answers = listOf("for"),
                chips = listOf("for", "while", "if", "in"),
                correctFeedback = "¡Sí! Un for dentro de otro for.",
                wrongFeedback = "La línea tiene la forma de un bucle que recorre un range."
            )
        ),
        walkthrough = listOf(
            step(1, "Bucle de afuera: i vale 0.", listOf("i" to "0")),
            step(2, "Bucle de adentro: j vale 0.", listOf("i" to "0", "j" to "0")),
            step(3, "Imprime i y j.", listOf("i" to "0", "j" to "0"), listOf("0 0")),
            step(2, "El de adentro sigue: j vale 1 (i no cambia).", listOf("i" to "0", "j" to "1"), listOf("0 0")),
            step(3, "Imprime.", listOf("i" to "0", "j" to "1"), listOf("0 0", "0 1")),
            step(2, "El de adentro terminó sus vueltas: vuelve al de afuera.", listOf("i" to "0", "j" to "1"), listOf("0 0", "0 1")),
            step(1, "Bucle de afuera: i vale 1.", listOf("i" to "1", "j" to "1"), listOf("0 0", "0 1")),
            step(2, "El de adentro empieza de nuevo: j vale 0.", listOf("i" to "1", "j" to "0"), listOf("0 0", "0 1")),
            step(3, "Imprime.", listOf("i" to "1", "j" to "0"), listOf("0 0", "0 1", "1 0")),
            step(2, "j vale 1.", listOf("i" to "1", "j" to "1"), listOf("0 0", "0 1", "1 0")),
            step(3, "Imprime.", listOf("i" to "1", "j" to "1"), listOf("0 0", "0 1", "1 0", "1 1")),
            step(2, "El de adentro terminó otra vez.", listOf("i" to "1", "j" to "1"), listOf("0 0", "0 1", "1 0", "1 1")),
            step(1, "El de afuera tampoco tiene más valores: todo termina.", listOf("i" to "1", "j" to "1"), listOf("0 0", "0 1", "1 0", "1 1"))
        )
    )

    private val cuantasVueltas = LessonContent(
        lessonId = "n09_l02",
        title = "¿Cuántas vueltas en total? ✖️",
        hook = "Glitch ordena 2 cajas con 3 robots cada una. ¿Cuántos robots revisa en total?",
        explanation = "Si el de afuera da 2 vueltas y el de adentro 3, lo de adentro se ejecuta 2 × 3 = 6 veces. Las vueltas se multiplican. El contador va fuera de los dos bucles para no reiniciarse.",
        code = "vueltas = 0\nfor i in range(2):\n    for j in range(3):\n        vueltas += 1\nprint(vueltas)",
        expectedOutput = "6",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "vueltas = 0\nfor i in range(3):\n    for j in range(4):\n        vueltas += 1\nprint(vueltas)",
                acceptedAnswers = listOf("12"),
                correctFeedback = "¡Muy bien! 3 × 4 = 12.",
                wrongFeedback = "Multiplica las vueltas del de afuera por las del de adentro."
            ),
            MultipleChoice(
                prompt = "En for i in range(5): con for j in range(2): print(\"hola\") adentro, ¿cuántas veces se imprime «hola»?",
                options = listOf("10", "7", "5", "2"),
                correctIndex = 0,
                correctFeedback = "¡Sí! 5 × 2 = 10.",
                wrongFeedback = "Las vueltas no se suman: se multiplican."
            ),
            FindBug(
                prompt = "Este programa debería mostrar 12, pero muestra 4. Toca el error.",
                codeLines = listOf(
                    "for i in range(3):",
                    "    vueltas = 0",
                    "    for j in range(4):",
                    "        vueltas += 1",
                    "print(vueltas)"
                ),
                bugLineIndex = 1,
                correctFeedback = "¡Bug cazado! El contador se reinicia en cada vuelta de afuera; debe ir antes del primer for.",
                wrongFeedback = "¿Qué le pasa a vueltas cada vez que empieza una vuelta del bucle de afuera?"
            )
        ),
        walkthrough = listOf(
            step(1, "El contador empieza en 0, fuera de los dos bucles.", listOf("vueltas" to "0")),
            step(2, "Afuera: i vale 0.", listOf("vueltas" to "0", "i" to "0")),
            step(3, "Adentro: j vale 0.", listOf("vueltas" to "0", "i" to "0", "j" to "0")),
            step(4, "Cuenta: 1.", listOf("vueltas" to "1", "i" to "0", "j" to "0")),
            step(3, "j vale 1.", listOf("vueltas" to "1", "i" to "0", "j" to "1")),
            step(4, "Cuenta: 2.", listOf("vueltas" to "2", "i" to "0", "j" to "1")),
            step(3, "j vale 2.", listOf("vueltas" to "2", "i" to "0", "j" to "2")),
            step(4, "Cuenta: 3.", listOf("vueltas" to "3", "i" to "0", "j" to "2")),
            step(3, "El de adentro terminó: 3 vueltas.", listOf("vueltas" to "3", "i" to "0", "j" to "2")),
            step(2, "Afuera: i vale 1.", listOf("vueltas" to "3", "i" to "1", "j" to "2")),
            step(3, "Adentro empieza de nuevo: j vale 0.", listOf("vueltas" to "3", "i" to "1", "j" to "0")),
            step(4, "Cuenta: 4.", listOf("vueltas" to "4", "i" to "1", "j" to "0")),
            step(3, "j vale 1.", listOf("vueltas" to "4", "i" to "1", "j" to "1")),
            step(4, "Cuenta: 5.", listOf("vueltas" to "5", "i" to "1", "j" to "1")),
            step(3, "j vale 2.", listOf("vueltas" to "5", "i" to "1", "j" to "2")),
            step(4, "Cuenta: 6.", listOf("vueltas" to "6", "i" to "1", "j" to "2")),
            step(3, "El de adentro terminó.", listOf("vueltas" to "6", "i" to "1", "j" to "2")),
            step(2, "El de afuera también: 2 × 3 = 6 vueltas en total.", listOf("vueltas" to "6", "i" to "1", "j" to "2")),
            step(5, "Muestra el total.", listOf("vueltas" to "6", "i" to "1", "j" to "2"), listOf("6"))
        )
    )

    private val dibujar = LessonContent(
        lessonId = "n09_l03",
        title = "Dibujar con bucles ⭐",
        hook = "Glitch quiere dibujar una escalera de estrellas en la pantalla.",
        explanation = "Un texto multiplicado por un número se repite: \"*\" * 3 es \"***\". Con un for que cambia ese número en cada vuelta, cada fila sale más larga que la anterior.",
        code = "for fila in range(1, 4):\n    print(\"*\" * fila)",
        expectedOutput = "*\n**\n***",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "for fila in range(1, 4):\n    print(\"#\" * fila)",
                acceptedAnswers = listOf("#\n##\n###"),
                correctFeedback = "¡Exacto! Una, dos y tres almohadillas.",
                wrongFeedback = "En cada vuelta se repite # tantas veces como vale fila."
            ),
            FillBlanks(
                prompt = "Completa para dibujar 5 filas (la última con 5 estrellas).",
                codeTemplate = "for fila in range(1, ___):\n    print(\"*\" * fila)",
                answers = listOf("6"),
                chips = listOf("6", "5", "4", "10"),
                correctFeedback = "¡Sí! Para que fila llegue a 5, el final tiene que ser 6.",
                wrongFeedback = "El final de range no se incluye."
            ),
            TypeCode(
                prompt = "Escribe la línea que muestra cuatro asteriscos usando el *.",
                acceptedAnswers = listOf("print(\"*\" * 4)"),
                placeholder = "print(...)",
                correctFeedback = "¡Eso! \"*\" * 4 es \"****\".",
                wrongFeedback = "Multiplica el texto \"*\" por 4 dentro del print."
            )
        ),
        walkthrough = listOf(
            step(1, "fila vale 1.", listOf("fila" to "1")),
            step(2, "\"*\" * 1 es una estrella.", listOf("fila" to "1"), listOf("*")),
            step(1, "fila vale 2.", listOf("fila" to "2"), listOf("*")),
            step(2, "\"*\" * 2 son dos estrellas.", listOf("fila" to "2"), listOf("*", "**")),
            step(1, "fila vale 3.", listOf("fila" to "3"), listOf("*", "**")),
            step(2, "Tres estrellas.", listOf("fila" to "3"), listOf("*", "**", "***")),
            step(1, "El 4 no se incluye: la escalera está lista.", listOf("fila" to "3"), listOf("*", "**", "***"))
        )
    )

    private val tablasCruzadas = LessonContent(
        lessonId = "n09_l04",
        title = "Tablas cruzadas 🔢",
        hook = "Glitch arma una mini tabla de multiplicar, combinando cada número con cada número.",
        explanation = "Con dos bucles anidados se combinan todos los valores de a con todos los de b. Así se arman tablas completas sin escribir cada línea a mano.",
        code = "for a in range(1, 3):\n    for b in range(1, 3):\n        print(a, \"x\", b, \"=\", a * b)",
        expectedOutput = "1 x 1 = 1\n1 x 2 = 2\n2 x 1 = 2\n2 x 2 = 4",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "for a in range(1, 3):\n    for b in range(1, 3):\n        print(a + b)",
                acceptedAnswers = listOf("2\n3\n3\n4"),
                correctFeedback = "¡Muy bien! 1+1, 1+2, 2+1 y 2+2.",
                wrongFeedback = "Combina a = 1 con b = 1 y 2; luego a = 2 con b = 1 y 2."
            ),
            FillBlanks(
                prompt = "Completa el cálculo del resultado.",
                codeTemplate = "for a in range(1, 4):\n    for b in range(1, 4):\n        print(a, \"x\", b, \"=\", ___)",
                answers = listOf("a * b"),
                chips = listOf("a * b", "a + b", "a x b", "b"),
                correctFeedback = "¡Sí! En Python se multiplica con *.",
                wrongFeedback = "La x es solo texto; para multiplicar se usa otro símbolo."
            ),
            OrderLines(
                prompt = "Ordena la mini tabla de multiplicar.",
                lines = listOf(
                    "for a in range(1, 3):",
                    "    for b in range(1, 3):",
                    "        print(a, \"x\", b, \"=\", a * b)"
                ),
                correctFeedback = "¡Perfecto! El print va dentro de los dos bucles.",
                wrongFeedback = "Primero el bucle de a, luego el de b con más sangría y el print adentro de ambos."
            )
        ),
        walkthrough = listOf(
            step(1, "a vale 1.", listOf("a" to "1")),
            step(2, "b vale 1.", listOf("a" to "1", "b" to "1")),
            step(3, "1 * 1 = 1.", listOf("a" to "1", "b" to "1"), listOf("1 x 1 = 1")),
            step(2, "b vale 2.", listOf("a" to "1", "b" to "2"), listOf("1 x 1 = 1")),
            step(3, "1 * 2 = 2.", listOf("a" to "1", "b" to "2"), listOf("1 x 1 = 1", "1 x 2 = 2")),
            step(2, "Terminó el bucle de b.", listOf("a" to "1", "b" to "2"), listOf("1 x 1 = 1", "1 x 2 = 2")),
            step(1, "a vale 2.", listOf("a" to "2", "b" to "2"), listOf("1 x 1 = 1", "1 x 2 = 2")),
            step(2, "b empieza de nuevo en 1.", listOf("a" to "2", "b" to "1"), listOf("1 x 1 = 1", "1 x 2 = 2")),
            step(3, "2 * 1 = 2.", listOf("a" to "2", "b" to "1"), listOf("1 x 1 = 1", "1 x 2 = 2", "2 x 1 = 2")),
            step(2, "b vale 2.", listOf("a" to "2", "b" to "2"), listOf("1 x 1 = 1", "1 x 2 = 2", "2 x 1 = 2")),
            step(3, "2 * 2 = 4.", listOf("a" to "2", "b" to "2"), listOf("1 x 1 = 1", "1 x 2 = 2", "2 x 1 = 2", "2 x 2 = 4")),
            step(2, "Terminó el bucle de b.", listOf("a" to "2", "b" to "2"), listOf("1 x 1 = 1", "1 x 2 = 2", "2 x 1 = 2", "2 x 2 = 4")),
            step(1, "a no tiene más valores: la tabla está completa.", listOf("a" to "2", "b" to "2"), listOf("1 x 1 = 1", "1 x 2 = 2", "2 x 1 = 2", "2 x 2 = 4"))
        )
    )

    private val salirConBreak = LessonContent(
        lessonId = "n09_l05",
        title = "break: salir antes ⏹️",
        hook = "Glitch cuenta cajas, pero al llegar a la 4 suena la alarma y tiene que salir corriendo.",
        explanation = "break termina el bucle en ese mismo momento, aunque queden vueltas. El programa sigue con la primera línea fuera del bucle. Aquí el 4 no se imprime porque el break va antes del print.",
        code = "for n in range(1, 10):\n    if n == 4:\n        break\n    print(n)",
        expectedOutput = "1\n2\n3",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "for n in range(1, 8):\n    if n == 3:\n        break\n    print(n)\nprint(\"Fin\")",
                acceptedAnswers = listOf("1\n2\nFin"),
                correctFeedback = "¡Exacto! Al llegar a 3 sale del bucle y sigue con «Fin».",
                wrongFeedback = "El break corta el bucle cuando n vale 3. ¿Se llega a imprimir el 3? ¿Y la última línea?"
            ),
            FindBug(
                prompt = "Glitch quería mostrar números hasta que algo lo detuviera, pero no muestra ninguno. Toca el error.",
                codeLines = listOf(
                    "for n in range(1, 10):",
                    "    break",
                    "    print(n)"
                ),
                bugLineIndex = 1,
                correctFeedback = "¡Bug cazado! Un break sin condición corta el bucle en la primera vuelta.",
                wrongFeedback = "¿Qué pasa en la primera vuelta antes de llegar al print?"
            ),
            FillBlanks(
                prompt = "Completa para que el bucle termine al llegar a 5.",
                codeTemplate = "for n in range(1, 10):\n    if n == 5:\n        ___\n    print(n)",
                answers = listOf("break"),
                chips = listOf("break", "continue", "stop", "exit"),
                correctFeedback = "¡Sí! break sale del bucle.",
                wrongFeedback = "La palabra de Python para salir del bucle significa «romper» en inglés."
            )
        ),
        walkthrough = listOf(
            step(1, "n vale 1.", listOf("n" to "1")),
            step(2, "1 == 4 es falso: sigue.", listOf("n" to "1")),
            step(4, "Imprime 1.", listOf("n" to "1"), listOf("1")),
            step(1, "n vale 2.", listOf("n" to "2"), listOf("1")),
            step(2, "2 == 4 es falso.", listOf("n" to "2"), listOf("1")),
            step(4, "Imprime 2.", listOf("n" to "2"), listOf("1", "2")),
            step(1, "n vale 3.", listOf("n" to "3"), listOf("1", "2")),
            step(2, "3 == 4 es falso.", listOf("n" to "3"), listOf("1", "2")),
            step(4, "Imprime 3.", listOf("n" to "3"), listOf("1", "2", "3")),
            step(1, "n vale 4.", listOf("n" to "4"), listOf("1", "2", "3")),
            step(2, "4 == 4 es verdadero.", listOf("n" to "4"), listOf("1", "2", "3")),
            step(3, "break: el bucle termina ya. El 4 no se imprime y del 5 al 9 ni se revisan.", listOf("n" to "4"), listOf("1", "2", "3"))
        )
    )

    private val whileTrue = LessonContent(
        lessonId = "n09_l06",
        title = "while True con break 🔁",
        hook = "Glitch busca el primer múltiplo de 4 desde el 9, pero no sabe cuántos números tendrá que revisar.",
        explanation = "while True repite «para siempre», porque True nunca deja de ser verdadero. La única salida es un break dentro. Es útil cuando la condición para parar es más fácil de revisar adentro del bucle.",
        code = "n = 9\nwhile True:\n    if n % 4 == 0:\n        break\n    n += 1\nprint(n)",
        expectedOutput = "12",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "n = 20\nwhile True:\n    if n % 6 == 0:\n        break\n    n += 1\nprint(n)",
                acceptedAnswers = listOf("24"),
                correctFeedback = "¡Muy bien! 20, 21, 22, 23… y 24 es el primer múltiplo de 6.",
                wrongFeedback = "Revisa 20, 21, 22… y detente en el primero cuyo resto entre 6 sea 0."
            ),
            MultipleChoice(
                prompt = "¿Qué pasaría con el ejemplo si quitamos el break?",
                options = listOf(
                    "Nunca terminaría",
                    "Imprimiría 12",
                    "Daría un error de inmediato",
                    "Imprimiría 9"
                ),
                correctIndex = 0,
                correctFeedback = "¡Sí! while True no se detiene solo: sin break es un bucle infinito.",
                wrongFeedback = "¿Qué otra cosa podría hacer que while True deje de repetirse?"
            ),
            FillBlanks(
                prompt = "Completa: la puerta pide la clave una y otra vez hasta que sea «glitch».",
                codeTemplate = "___ True:\n    clave = input(\"Clave: \")\n    if clave == \"glitch\":\n        break",
                answers = listOf("while"),
                chips = listOf("while", "for", "if", "break"),
                correctFeedback = "¡Eso! while True con un break adentro.",
                wrongFeedback = "Hace falta el bucle que repite mientras la condición sea verdadera."
            )
        ),
        walkthrough = listOf(
            step(1, "n empieza en 9.", listOf("n" to "9")),
            step(2, "while True: siempre entra.", listOf("n" to "9")),
            step(3, "9 % 4 es 1: no es múltiplo.", listOf("n" to "9")),
            step(5, "Prueba con el siguiente.", listOf("n" to "10")),
            step(2, "Otra vuelta.", listOf("n" to "10")),
            step(3, "10 % 4 es 2: no.", listOf("n" to "10")),
            step(5, "Siguiente.", listOf("n" to "11")),
            step(2, "Otra vuelta.", listOf("n" to "11")),
            step(3, "11 % 4 es 3: no.", listOf("n" to "11")),
            step(5, "Siguiente.", listOf("n" to "12")),
            step(2, "Otra vuelta.", listOf("n" to "12")),
            step(3, "12 % 4 es 0: ¡lo encontró!", listOf("n" to "12")),
            step(4, "break: sale del while True.", listOf("n" to "12")),
            step(6, "Muestra el número encontrado.", listOf("n" to "12"), listOf("12"))
        )
    )

    private val saltarConContinue = LessonContent(
        lessonId = "n09_l07",
        title = "continue: saltarse una vuelta ⏭️",
        hook = "Glitch reparte paquetes a las casas del 1 al 5, pero la casa 3 está de vacaciones.",
        explanation = "continue salta el resto de esa vuelta y pasa directo a la siguiente. A diferencia de break, el bucle no termina: solo se omite lo que queda de esa vuelta.",
        code = "for n in range(1, 6):\n    if n == 3:\n        continue\n    print(n)",
        expectedOutput = "1\n2\n4\n5",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "for n in range(1, 5):\n    if n == 2:\n        continue\n    print(n)",
                acceptedAnswers = listOf("1\n3\n4"),
                correctFeedback = "¡Exacto! Solo se salta el 2; el bucle sigue.",
                wrongFeedback = "continue omite el print solo en la vuelta del 2. ¿Qué pasa con el 3 y el 4?"
            ),
            FillBlanks(
                prompt = "Completa para imprimir solo los impares, saltándose los pares.",
                codeTemplate = "for n in range(1, 6):\n    if n % 2 == 0:\n        ___\n    print(n)",
                answers = listOf("continue"),
                chips = listOf("continue", "break", "pass", "skip"),
                correctFeedback = "¡Sí! continue se salta el print de los pares y sigue con el siguiente número.",
                wrongFeedback = "break terminaría todo en el primer par. Hace falta saltarse solo esa vuelta."
            ),
            FindBug(
                prompt = "Glitch quería saltarse solo la casa 3, pero deja de repartir al llegar a ella. Toca el error.",
                codeLines = listOf(
                    "for n in range(1, 6):",
                    "    if n == 3:",
                    "        break",
                    "    print(n)"
                ),
                bugLineIndex = 2,
                correctFeedback = "¡Bug cazado! break termina todo; para saltarse una sola vuelta va continue.",
                wrongFeedback = "La condición está bien. ¿Qué hace la palabra que va adentro del if?"
            )
        ),
        walkthrough = listOf(
            step(1, "n vale 1.", listOf("n" to "1")),
            step(2, "1 == 3 es falso.", listOf("n" to "1")),
            step(4, "Entrega en la casa 1.", listOf("n" to "1"), listOf("1")),
            step(1, "n vale 2.", listOf("n" to "2"), listOf("1")),
            step(2, "2 == 3 es falso.", listOf("n" to "2"), listOf("1")),
            step(4, "Entrega en la casa 2.", listOf("n" to "2"), listOf("1", "2")),
            step(1, "n vale 3.", listOf("n" to "3"), listOf("1", "2")),
            step(2, "3 == 3 es verdadero.", listOf("n" to "3"), listOf("1", "2")),
            step(3, "continue: se salta el print y pasa a la siguiente vuelta.", listOf("n" to "3"), listOf("1", "2")),
            step(1, "n vale 4: el bucle sigue.", listOf("n" to "4"), listOf("1", "2")),
            step(2, "4 == 3 es falso.", listOf("n" to "4"), listOf("1", "2")),
            step(4, "Entrega en la casa 4.", listOf("n" to "4"), listOf("1", "2", "4")),
            step(1, "n vale 5.", listOf("n" to "5"), listOf("1", "2", "4")),
            step(2, "5 == 3 es falso.", listOf("n" to "5"), listOf("1", "2", "4")),
            step(4, "Entrega en la casa 5.", listOf("n" to "5"), listOf("1", "2", "4", "5")),
            step(1, "No quedan casas: termina.", listOf("n" to "5"), listOf("1", "2", "4", "5"))
        )
    )

    private val filtrar = LessonContent(
        lessonId = "n09_l08",
        title = "Filtrar con continue 🧹",
        hook = "Glitch quiere sumar solo los números impares del 1 al 5 y dejar fuera los pares.",
        explanation = "continue sirve para filtrar: si un valor no interesa, se salta y el resto de la vuelta (aquí, la suma) no se ejecuta. Es otra forma de escribir un if que deja pasar solo algunos valores.",
        code = "total = 0\nfor n in range(1, 6):\n    if n % 2 == 0:\n        continue\n    total += n\nprint(total)",
        expectedOutput = "9",
        exercises = listOf(
            PredictOutput(
                prompt = "Ahora se saltan los impares. ¿Qué muestra este programa?",
                code = "total = 0\nfor n in range(1, 7):\n    if n % 2 == 1:\n        continue\n    total += n\nprint(total)",
                acceptedAnswers = listOf("12"),
                correctFeedback = "¡Muy bien! Solo suma los pares: 2 + 4 + 6 = 12.",
                wrongFeedback = "Los impares se saltan antes de sumar. ¿Qué números sí llegan al total?"
            ),
            OrderLines(
                prompt = "Ordena el programa que suma solo los impares del 1 al 5.",
                lines = listOf(
                    "total = 0",
                    "for n in range(1, 6):",
                    "    if n % 2 == 0:",
                    "        continue",
                    "    total += n",
                    "print(total)"
                ),
                correctFeedback = "¡Perfecto! El filtro va antes de la suma.",
                wrongFeedback = "El continue tiene que estar antes de total += n para evitar que se sume."
            ),
            FillBlanks(
                prompt = "Completa para saltarse los pares e imprimir solo los impares.",
                codeTemplate = "for n in range(1, 6):\n    if n % 2 ___ 0:\n        continue\n    print(n)",
                answers = listOf("=="),
                chips = listOf("==", "!=", "=", ">"),
                correctFeedback = "¡Sí! Si el resto es igual a 0 es par, y se salta.",
                wrongFeedback = "Hay que saltarse los números cuyo resto entre 2 es igual a 0."
            )
        ),
        walkthrough = listOf(
            step(1, "El total empieza en 0.", listOf("total" to "0")),
            step(2, "n vale 1.", listOf("total" to "0", "n" to "1")),
            step(3, "1 es impar: no se salta.", listOf("total" to "0", "n" to "1")),
            step(5, "Suma 1.", listOf("total" to "1", "n" to "1")),
            step(2, "n vale 2.", listOf("total" to "1", "n" to "2")),
            step(3, "2 es par.", listOf("total" to "1", "n" to "2")),
            step(4, "continue: se salta la suma.", listOf("total" to "1", "n" to "2")),
            step(2, "n vale 3.", listOf("total" to "1", "n" to "3")),
            step(3, "3 es impar.", listOf("total" to "1", "n" to "3")),
            step(5, "1 + 3 = 4.", listOf("total" to "4", "n" to "3")),
            step(2, "n vale 4.", listOf("total" to "4", "n" to "4")),
            step(3, "4 es par.", listOf("total" to "4", "n" to "4")),
            step(4, "Se salta.", listOf("total" to "4", "n" to "4")),
            step(2, "n vale 5.", listOf("total" to "4", "n" to "5")),
            step(3, "5 es impar.", listOf("total" to "4", "n" to "5")),
            step(5, "4 + 5 = 9.", listOf("total" to "9", "n" to "5")),
            step(2, "No quedan números: termina.", listOf("total" to "9", "n" to "5")),
            step(6, "Muestra la suma de los impares.", listOf("total" to "9", "n" to "5"), listOf("9"))
        )
    )

    private val breakOContinue = LessonContent(
        lessonId = "n09_l09",
        title = "¿break o continue? ⚖️",
        hook = "Glitch confunde las dos palabras. Veamos el mismo bucle con cada una.",
        explanation = "break termina todo el bucle; continue solo se salta la vuelta actual. Este ejemplo usa break y muestra 1 y 2. Si cambias break por continue, mostraría 1, 2, 4 y 5: solo faltaría el 3.",
        code = "for n in range(1, 6):\n    if n == 3:\n        break\n    print(n)",
        expectedOutput = "1\n2",
        exercises = listOf(
            MultipleChoice(
                prompt = "Con for n in range(1, 6): e if n == 3: adentro, ¿cuál de estos bucles muestra 1, 2, 4, 5?",
                options = listOf(
                    "El que tiene continue dentro del if",
                    "El que tiene break dentro del if",
                    "Los dos",
                    "Ninguno"
                ),
                correctIndex = 0,
                correctFeedback = "¡Exacto! continue solo se salta el 3; el bucle sigue con 4 y 5.",
                wrongFeedback = "Uno termina el bucle al llegar al 3 y el otro solo se salta esa vuelta."
            ),
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "for n in range(1, 6):\n    if n == 4:\n        continue\n    print(n)",
                acceptedAnswers = listOf("1\n2\n3\n5"),
                correctFeedback = "¡Muy bien! Solo falta el 4.",
                wrongFeedback = "continue se salta una sola vuelta. ¿Cuál?"
            ),
            TypeCode(
                prompt = "Escribe la palabra que termina el bucle por completo.",
                acceptedAnswers = listOf("break"),
                placeholder = "...",
                correctFeedback = "¡Eso! break sale del bucle.",
                wrongFeedback = "No es continue: esa solo se salta una vuelta."
            )
        ),
        walkthrough = listOf(
            step(1, "n vale 1.", listOf("n" to "1")),
            step(2, "1 == 3 es falso.", listOf("n" to "1")),
            step(4, "Imprime 1.", listOf("n" to "1"), listOf("1")),
            step(1, "n vale 2.", listOf("n" to "2"), listOf("1")),
            step(2, "2 == 3 es falso.", listOf("n" to "2"), listOf("1")),
            step(4, "Imprime 2.", listOf("n" to "2"), listOf("1", "2")),
            step(1, "n vale 3.", listOf("n" to "3"), listOf("1", "2")),
            step(2, "3 == 3 es verdadero.", listOf("n" to "3"), listOf("1", "2")),
            step(3, "break: todo termina aquí. Con continue, en cambio, seguiría con el 4.", listOf("n" to "3"), listOf("1", "2"))
        )
    )

    private val bandera = LessonContent(
        lessonId = "n09_l10",
        title = "Buscar con una bandera 🚩",
        hook = "Glitch quiere saber si entre el 1 y el 10 hay algún múltiplo de 7. Solo necesita un sí o un no.",
        explanation = "Una bandera es una variable que empieza en False y pasa a True cuando encuentras lo que buscas. Con break se deja de buscar en cuanto aparece. Al final, la bandera dice si se encontró.",
        code = "encontrado = False\nfor n in range(1, 11):\n    if n % 7 == 0:\n        encontrado = True\n        break\nprint(encontrado)",
        expectedOutput = "True",
        exercises = listOf(
            PredictOutput(
                prompt = "Ahora busca del 1 al 5. ¿Qué muestra este programa?",
                code = "encontrado = False\nfor n in range(1, 6):\n    if n % 7 == 0:\n        encontrado = True\n        break\nprint(encontrado)",
                acceptedAnswers = listOf("False"),
                correctFeedback = "¡Exacto! Del 1 al 5 no hay múltiplos de 7: la bandera nunca se levanta.",
                wrongFeedback = "¿Hay algún múltiplo de 7 entre 1 y 5? Si no, la bandera queda como empezó."
            ),
            FillBlanks(
                prompt = "Completa para levantar la bandera cuando se encuentra el múltiplo.",
                codeTemplate = "encontrado = False\nfor n in range(1, 11):\n    if n % 7 == 0:\n        encontrado = ___\n        break",
                answers = listOf("True"),
                chips = listOf("True", "False", "\"True\"", "7"),
                correctFeedback = "¡Sí! True, sin comillas: es un valor verdadero, no un texto.",
                wrongFeedback = "La bandera empieza en False. Al encontrarlo, ¿a qué valor cambia?"
            ),
            FindBug(
                prompt = "Este programa muestra False aunque el 7 sí está entre el 1 y el 10. Toca el error.",
                codeLines = listOf(
                    "for n in range(1, 11):",
                    "    encontrado = False",
                    "    if n % 7 == 0:",
                    "        encontrado = True",
                    "print(encontrado)"
                ),
                bugLineIndex = 1,
                correctFeedback = "¡Bug cazado! La bandera se baja en cada vuelta; con n = 8, 9 y 10 vuelve a False. Va antes del for.",
                wrongFeedback = "¿Qué le pasa a la bandera en las vueltas que vienen después del 7?"
            )
        ),
        walkthrough = listOf(
            step(1, "La bandera empieza abajo: False.", listOf("encontrado" to "False")),
            step(2, "n vale 1.", listOf("encontrado" to "False", "n" to "1")),
            step(3, "1 no es múltiplo de 7.", listOf("encontrado" to "False", "n" to "1")),
            step(2, "n vale 2.", listOf("encontrado" to "False", "n" to "2")),
            step(3, "No es múltiplo.", listOf("encontrado" to "False", "n" to "2")),
            step(2, "n vale 3.", listOf("encontrado" to "False", "n" to "3")),
            step(3, "No es múltiplo.", listOf("encontrado" to "False", "n" to "3")),
            step(2, "n vale 4.", listOf("encontrado" to "False", "n" to "4")),
            step(3, "No es múltiplo.", listOf("encontrado" to "False", "n" to "4")),
            step(2, "n vale 5.", listOf("encontrado" to "False", "n" to "5")),
            step(3, "No es múltiplo.", listOf("encontrado" to "False", "n" to "5")),
            step(2, "n vale 6.", listOf("encontrado" to "False", "n" to "6")),
            step(3, "No es múltiplo.", listOf("encontrado" to "False", "n" to "6")),
            step(2, "n vale 7.", listOf("encontrado" to "False", "n" to "7")),
            step(3, "7 % 7 es 0: ¡es múltiplo!", listOf("encontrado" to "False", "n" to "7")),
            step(4, "Levanta la bandera.", listOf("encontrado" to "True", "n" to "7")),
            step(5, "break: ya no hace falta revisar 8, 9 ni 10.", listOf("encontrado" to "True", "n" to "7")),
            step(6, "Muestra la bandera.", listOf("encontrado" to "True", "n" to "7"), listOf("True"))
        )
    )

    private val glitchAnidado = LessonContent(
        lessonId = "n09_l11",
        title = "Glitch y los bucles anidados 🤖",
        hook = "Glitch quiere dibujar una L caminando: avanza, gira, avanza, gira. Hay un patrón que se repite.",
        explanation = "Cada «lado» es igual: avanzar 2 veces y girar. Eso es un for (los pasos) dentro de otro for (los lados). Fíjate que girar_derecha() va con la sangría del for de afuera: se ejecuta una vez por lado, no por paso.",
        code = "for lado in range(2):\n    for paso in range(2):\n        avanzar()\n    girar_derecha()\nprint(\"Glitch dibujó una L\")",
        expectedOutput = "Glitch dibujó una L",
        exercises = listOf(
            GlitchPuzzle(
                prompt = "Glitch tiene que recorrer 3 casillas, girar y recorrer otras 3. Usa un for dentro de otro.",
                board = listOf(">...", "....", "....", "...M"),
                mode = GlitchMode.BUILD,
                palette = listOf(GlitchBlock.Repeat(1), GlitchBlock.Forward, GlitchBlock.TurnRight),
                solution = "for i in range(2):\n    for i in range(3):\n        avanzar()\n    girar_derecha()",
                maxLines = 4,
                correctFeedback = "¡Llegó! Dos lados de 3 pasos con 4 líneas.",
                wrongFeedback = "Por fuera: 2 lados. Por dentro: 3 avances. El giro va después del for de adentro."
            ),
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "for i in range(2):\n    for j in range(3):\n        print(\"paso\")\n    print(\"giro\")",
                acceptedAnswers = listOf("paso\npaso\npaso\ngiro\npaso\npaso\npaso\ngiro"),
                correctFeedback = "¡Muy bien! Tres pasos y un giro, dos veces.",
                wrongFeedback = "«giro» está con la sangría del for de afuera: sale una vez por cada vuelta de afuera."
            ),
            FillBlanks(
                prompt = "Glitch recorre un cuadrado: en cada lado avanza 2 y después gira. Completa la línea que falta.",
                codeTemplate = "for lado in range(4):\n    for paso in range(2):\n        avanzar()\n    ___",
                answers = listOf("girar_derecha()"),
                chips = listOf("girar_derecha()", "avanzar()", "break", "continue"),
                correctFeedback = "¡Sí! Un giro al terminar cada lado.",
                wrongFeedback = "Después de avanzar en un lado, ¿qué tiene que hacer para empezar el siguiente?"
            )
        ),
        walkthrough = listOf(
            step(1, "Primer lado: lado vale 0.", listOf("lado" to "0")),
            step(2, "Primer paso.", listOf("lado" to "0", "paso" to "0")),
            step(3, "Avanza.", listOf("lado" to "0", "paso" to "0")),
            step(2, "Segundo paso.", listOf("lado" to "0", "paso" to "1")),
            step(3, "Avanza.", listOf("lado" to "0", "paso" to "1")),
            step(2, "El for de los pasos terminó.", listOf("lado" to "0", "paso" to "1")),
            step(4, "Gira: está en el for de afuera, así que pasa una vez por lado.", listOf("lado" to "0", "paso" to "1")),
            step(1, "Segundo lado: lado vale 1.", listOf("lado" to "1", "paso" to "1")),
            step(2, "Los pasos empiezan de nuevo.", listOf("lado" to "1", "paso" to "0")),
            step(3, "Avanza.", listOf("lado" to "1", "paso" to "0")),
            step(2, "Segundo paso.", listOf("lado" to "1", "paso" to "1")),
            step(3, "Avanza.", listOf("lado" to "1", "paso" to "1")),
            step(2, "Terminaron los pasos.", listOf("lado" to "1", "paso" to "1")),
            step(4, "Gira otra vez.", listOf("lado" to "1", "paso" to "1")),
            step(1, "Ya hizo los 2 lados: termina.", listOf("lado" to "1", "paso" to "1")),
            step(5, "Fuera de los bucles.", listOf("lado" to "1", "paso" to "1"), listOf("Glitch dibujó una L"))
        )
    )

    // Jefe del nivel: mezcla lo aprendido en las 11 lecciones.
    val boss = BossContent(
        bossId = "n09_boss",
        name = "La Matrioska Loca",
        emoji = "🪆",
        intro = "Dentro de esta muñeca hay otra, y dentro otra… Sus bucles están metidos unos en otros. Solo la vence quien sigue cada vuelta sin perderse.",
        exercises = listOf(
            PredictOutput(
                prompt = "Primera muñeca. ¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "for i in range(2):\n    for j in range(3):\n        print(\"muñeca\")",
                acceptedAnswers = listOf("muñeca\nmuñeca\nmuñeca\nmuñeca\nmuñeca\nmuñeca"),
                correctFeedback = "¡Golpe! 2 × 3 = 6 muñecas.",
                wrongFeedback = "Las vueltas se multiplican: 2 de afuera por 3 de adentro."
            ),
            PredictOutput(
                prompt = "La muñeca cuenta hacia atrás y se detiene. ¿Qué muestra? Escríbelo línea por línea.",
                code = "for n in range(10, 0, -1):\n    if n == 7:\n        break\n    print(n)",
                acceptedAnswers = listOf("10\n9\n8"),
                correctFeedback = "¡Muy bien! Al llegar al 7 el break corta todo.",
                wrongFeedback = "Cuenta 10, 9, 8… ¿qué pasa al llegar al 7, antes del print?"
            ),
            PredictOutput(
                prompt = "Ahora se salta algunas. ¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "for n in range(1, 7):\n    if n % 3 == 0:\n        continue\n    print(n)",
                acceptedAnswers = listOf("1\n2\n4\n5"),
                correctFeedback = "¡Eso! Se salta el 3 y el 6, los múltiplos de 3.",
                wrongFeedback = "continue se salta las vueltas de los múltiplos de 3. ¿Cuáles son entre 1 y 6?"
            ),
            FindBug(
                prompt = "La Matrioska quería escribir «fila» una vez por fila, pero sale 3 veces por fila. ¡Toca el error!",
                codeLines = listOf(
                    "for fila in range(2):",
                    "    for col in range(3):",
                    "        print(\"*\")",
                    "        print(\"fila\")"
                ),
                bugLineIndex = 3,
                correctFeedback = "¡Bug golpeado! Esa línea tiene la sangría del for de adentro; debe ir al nivel del de afuera.",
                wrongFeedback = "Revisa la sangría: ¿cuál línea se repite más veces de lo que debería?"
            ),
            TypeCode(
                prompt = "Último golpe: escribe la palabra que se salta el resto de la vuelta y sigue con la siguiente.",
                acceptedAnswers = listOf("continue"),
                placeholder = "...",
                correctFeedback = "¡Matrioska vencida! Dominas los bucles avanzados.",
                wrongFeedback = "No es break: esa termina todo el bucle."
            )
        )
    )

    val all: List<LessonContent> = listOf(
        bucleDentro, cuantasVueltas, dibujar, tablasCruzadas,
        salirConBreak, whileTrue, saltarConContinue, filtrar,
        breakOContinue, bandera, glitchAnidado
    )
}
