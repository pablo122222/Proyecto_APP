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

// Nivel 8 · Bucles for (11 lecciones y el jefe «La Hidra de los Ciclos»), con Glitch como hilo conductor.
// Cada ejemplo trae su "Ver paso a paso": línea, nota, variables y todo lo impreso hasta ahí.
internal object Level08Lessons {

    private fun step(line: Int, note: String, vars: List<Pair<String, String>>, out: List<String> = emptyList()) =
        WalkStep(line, note, vars, out)

    private val repetirFijo = LessonContent(
        lessonId = "n08_l01",
        title = "Repetir un número fijo de veces 🔂",
        hook = "Glitch quiere saludar 3 veces. Con while necesitaba un contador; con for basta una línea.",
        explanation = "for i in range(3) repite su bloque exactamente 3 veces. Es la forma corta cuando ya sabes cuántas vueltas quieres: no hace falta crear ni sumar un contador a mano, for lo hace solo.",
        code = "for i in range(3):\n    print(\"¡Hola!\")",
        expectedOutput = "¡Hola!\n¡Hola!\n¡Hola!",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "for i in range(2):\n    print(\"Glitch\")",
                acceptedAnswers = listOf("Glitch\nGlitch"),
                correctFeedback = "¡Exacto! range(2) da dos vueltas.",
                wrongFeedback = "range(2) repite el bloque una cantidad fija de veces. ¿Cuántas?"
            ),
            FillBlanks(
                prompt = "Completa para repetir el print 4 veces.",
                codeTemplate = "for i in ___(4):\n    print(\"Paso\")",
                answers = listOf("range"),
                chips = listOf("range", "len", "list", "while"),
                correctFeedback = "¡Sí! range(4) da las 4 vueltas.",
                wrongFeedback = "La función que da una serie de números para el for empieza por r."
            ),
            OrderLines(
                prompt = "Ordena: Glitch salta 3 veces y al final dice «¡Listo!» una sola vez.",
                lines = listOf(
                    "for i in range(3):",
                    "    print(\"Saltando\")",
                    "print(\"¡Listo!\")"
                ),
                correctFeedback = "¡Perfecto! El «¡Listo!» va sin sangría: fuera del bucle.",
                wrongFeedback = "Lo que se repite va dentro, con sangría. Lo que sale una vez va después, sin sangría."
            )
        ),
        walkthrough = listOf(
            step(1, "range(3) da los valores 0, 1 y 2. Primera vuelta: i vale 0.", listOf("i" to "0")),
            step(2, "Saluda.", listOf("i" to "0"), listOf("¡Hola!")),
            step(1, "Siguiente valor: i vale 1.", listOf("i" to "1"), listOf("¡Hola!")),
            step(2, "Saluda otra vez.", listOf("i" to "1"), listOf("¡Hola!", "¡Hola!")),
            step(1, "Siguiente valor: i vale 2.", listOf("i" to "2"), listOf("¡Hola!", "¡Hola!")),
            step(2, "Tercer saludo.", listOf("i" to "2"), listOf("¡Hola!", "¡Hola!", "¡Hola!")),
            step(1, "range(3) ya no tiene más valores: el bucle termina.", listOf("i" to "2"), listOf("¡Hola!", "¡Hola!", "¡Hola!"))
        )
    )

    private val valoresDeI = LessonContent(
        lessonId = "n08_l02",
        title = "¿Qué valores toma i? 🔢",
        hook = "Glitch imprimió i y se sorprendió: ¡empieza en 0!",
        explanation = "range(4) da los números 0, 1, 2 y 3: empieza en 0 y no incluye el 4. Son 4 valores, por eso da 4 vueltas. En cada vuelta, i toma el siguiente valor.",
        code = "for i in range(4):\n    print(i)",
        expectedOutput = "0\n1\n2\n3",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "for i in range(3):\n    print(i)",
                acceptedAnswers = listOf("0\n1\n2"),
                correctFeedback = "¡Muy bien! Empieza en 0 y se detiene antes del 3.",
                wrongFeedback = "range empieza en 0 y no incluye el número que le das."
            ),
            MultipleChoice(
                prompt = "¿Cuál es el último valor que toma i en for i in range(5)?",
                options = listOf("4", "5", "0", "6"),
                correctIndex = 0,
                correctFeedback = "¡Eso! 0, 1, 2, 3 y 4: el 5 no se incluye.",
                wrongFeedback = "range(5) da 5 valores empezando en 0. ¿Cuál es el último?"
            ),
            TypeCode(
                prompt = "Escribe la línea del for que da 5 vueltas usando i y range.",
                acceptedAnswers = listOf("for i in range(5):"),
                placeholder = "for ...",
                correctFeedback = "¡Perfecto! for, la variable, in, range y los dos puntos.",
                wrongFeedback = "Escribe for i in range(5): sin olvidar los dos puntos al final."
            )
        ),
        walkthrough = listOf(
            step(1, "Primera vuelta: i vale 0 (range empieza en 0).", listOf("i" to "0")),
            step(2, "Imprime 0.", listOf("i" to "0"), listOf("0")),
            step(1, "i vale 1.", listOf("i" to "1"), listOf("0")),
            step(2, "Imprime 1.", listOf("i" to "1"), listOf("0", "1")),
            step(1, "i vale 2.", listOf("i" to "2"), listOf("0", "1")),
            step(2, "Imprime 2.", listOf("i" to "2"), listOf("0", "1", "2")),
            step(1, "i vale 3, el último: el 4 no se incluye.", listOf("i" to "3"), listOf("0", "1", "2")),
            step(2, "Imprime 3.", listOf("i" to "3"), listOf("0", "1", "2", "3")),
            step(1, "No quedan valores: termina.", listOf("i" to "3"), listOf("0", "1", "2", "3"))
        )
    )

    private val desdeHasta = LessonContent(
        lessonId = "n08_l03",
        title = "Desde dónde hasta dónde 📏",
        hook = "Glitch quiere contar del 1 al 3, no del 0 al 2. range acepta un punto de partida.",
        explanation = "range(inicio, fin) empieza en inicio y llega hasta fin − 1: el final nunca se incluye. Para contar del 1 al 3 se escribe range(1, 4).",
        code = "for i in range(1, 4):\n    print(i)",
        expectedOutput = "1\n2\n3",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "for i in range(2, 5):\n    print(i)",
                acceptedAnswers = listOf("2\n3\n4"),
                correctFeedback = "¡Exacto! Empieza en 2 y se detiene antes del 5.",
                wrongFeedback = "Empieza en el primer número y para justo antes del segundo."
            ),
            FillBlanks(
                prompt = "Completa para que imprima del 1 al 5.",
                codeTemplate = "for i in range(1, ___):\n    print(i)",
                answers = listOf("6"),
                chips = listOf("6", "5", "4", "10"),
                correctFeedback = "¡Sí! Para incluir el 5, el final tiene que ser 6.",
                wrongFeedback = "El final no se incluye: ¿qué número va justo después del 5?"
            ),
            FindBug(
                prompt = "Glitch quiere contar del 1 al 10, pero el 10 no aparece. Toca el error.",
                codeLines = listOf(
                    "for i in range(1, 10):",
                    "    print(i)"
                ),
                bugLineIndex = 0,
                correctFeedback = "¡Bug cazado! range(1, 10) llega hasta 9; hace falta range(1, 11).",
                wrongFeedback = "El print está bien. Revisa hasta dónde llega el range."
            )
        ),
        walkthrough = listOf(
            step(1, "range(1, 4) da 1, 2 y 3. Primera vuelta: i vale 1.", listOf("i" to "1")),
            step(2, "Imprime 1.", listOf("i" to "1"), listOf("1")),
            step(1, "i vale 2.", listOf("i" to "2"), listOf("1")),
            step(2, "Imprime 2.", listOf("i" to "2"), listOf("1", "2")),
            step(1, "i vale 3.", listOf("i" to "3"), listOf("1", "2")),
            step(2, "Imprime 3.", listOf("i" to "3"), listOf("1", "2", "3")),
            step(1, "El 4 no se incluye: termina.", listOf("i" to "3"), listOf("1", "2", "3"))
        )
    )

    private val conSaltos = LessonContent(
        lessonId = "n08_l04",
        title = "Con saltos 🐸",
        hook = "La rana de Glitch City vuelve, ahora con for. Salta de 3 en 3.",
        explanation = "range(inicio, fin, salto) avanza de salto en salto. range(0, 10, 3) da 0, 3, 6 y 9: el siguiente sería 12, que ya pasa del 10.",
        code = "for i in range(0, 10, 3):\n    print(i)",
        expectedOutput = "0\n3\n6\n9",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "for i in range(1, 10, 4):\n    print(i)",
                acceptedAnswers = listOf("1\n5\n9"),
                correctFeedback = "¡Muy bien! 1, 5 y 9; el siguiente sería 13.",
                wrongFeedback = "Empieza en 1 y suma 4 cada vez, sin llegar a 10."
            ),
            FillBlanks(
                prompt = "Completa para imprimir los pares del 2 al 10.",
                codeTemplate = "for n in range(2, 11, ___):\n    print(n)",
                answers = listOf("2"),
                chips = listOf("2", "1", "10", "11"),
                correctFeedback = "¡Sí! De 2 en 2: 2, 4, 6, 8 y 10.",
                wrongFeedback = "El tercer número es el salto. ¿De cuánto en cuánto van los pares?"
            ),
            TypeCode(
                prompt = "Escribe el for que recorre 0, 5, 10 y 15 usando range(0, 20, …).",
                acceptedAnswers = listOf("for i in range(0, 20, 5):"),
                placeholder = "for i in range(...",
                correctFeedback = "¡Eso! Salto de 5, sin llegar al 20.",
                wrongFeedback = "Escribe for i in range(0, 20, 5): con el salto como tercer número."
            )
        ),
        walkthrough = listOf(
            step(1, "Empieza en 0.", listOf("i" to "0")),
            step(2, "Imprime 0.", listOf("i" to "0"), listOf("0")),
            step(1, "Salta 3: i vale 3.", listOf("i" to "3"), listOf("0")),
            step(2, "Imprime 3.", listOf("i" to "3"), listOf("0", "3")),
            step(1, "Salta 3: i vale 6.", listOf("i" to "6"), listOf("0", "3")),
            step(2, "Imprime 6.", listOf("i" to "6"), listOf("0", "3", "6")),
            step(1, "Salta 3: i vale 9.", listOf("i" to "9"), listOf("0", "3", "6")),
            step(2, "Imprime 9.", listOf("i" to "9"), listOf("0", "3", "6", "9")),
            step(1, "El siguiente sería 12, que pasa del 10: termina.", listOf("i" to "9"), listOf("0", "3", "6", "9"))
        )
    )

    private val haciaAtras = LessonContent(
        lessonId = "n08_l05",
        title = "Hacia atrás 🚀",
        hook = "El cohete de Glitch City despega otra vez, ahora con for.",
        explanation = "Con un salto negativo, range cuenta hacia atrás. range(3, 0, -1) da 3, 2 y 1: empieza en 3 y se detiene antes del 0. Sin el -1, range(3, 0) no da ningún valor.",
        code = "for n in range(3, 0, -1):\n    print(n)\nprint(\"¡Despegue!\")",
        expectedOutput = "3\n2\n1\n¡Despegue!",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "for n in range(5, 0, -2):\n    print(n)",
                acceptedAnswers = listOf("5\n3\n1"),
                correctFeedback = "¡Exacto! Baja de 2 en 2: 5, 3 y 1.",
                wrongFeedback = "Empieza en 5 y resta 2 cada vez, sin llegar a 0."
            ),
            FindBug(
                prompt = "Solo se ve «¡Despegue!»: la cuenta no aparece. Toca el error.",
                codeLines = listOf(
                    "for n in range(3, 0):",
                    "    print(n)",
                    "print(\"¡Despegue!\")"
                ),
                bugLineIndex = 0,
                correctFeedback = "¡Bug cazado! Sin -1, range intenta subir de 3 a 0 y no da ningún valor.",
                wrongFeedback = "Para bajar, range necesita saberlo. Revisa sus números."
            ),
            FillBlanks(
                prompt = "Completa para contar 10, 9, 8… hasta 1.",
                codeTemplate = "for n in range(10, 0, ___):\n    print(n)",
                answers = listOf("-1"),
                chips = listOf("-1", "1", "0", "10"),
                correctFeedback = "¡Sí! Un salto de -1 baja de uno en uno.",
                wrongFeedback = "Para bajar, el salto tiene que ser negativo."
            )
        ),
        walkthrough = listOf(
            step(1, "Empieza en 3.", listOf("n" to "3")),
            step(2, "Imprime 3.", listOf("n" to "3"), listOf("3")),
            step(1, "Salto de -1: n vale 2.", listOf("n" to "2"), listOf("3")),
            step(2, "Imprime 2.", listOf("n" to "2"), listOf("3", "2")),
            step(1, "n vale 1.", listOf("n" to "1"), listOf("3", "2")),
            step(2, "Imprime 1.", listOf("n" to "1"), listOf("3", "2", "1")),
            step(1, "El siguiente sería 0, que no se incluye: termina.", listOf("n" to "1"), listOf("3", "2", "1")),
            step(3, "Fuera del bucle: se ejecuta una vez.", listOf("n" to "1"), listOf("3", "2", "1", "¡Despegue!"))
        )
    )

    private val acumularConFor = LessonContent(
        lessonId = "n08_l06",
        title = "Acumular con for 🧮",
        hook = "Glitch vuelve a sumar del 1 al 5, pero esta vez con for. Ya no tiene que sumar 1 a n a mano.",
        explanation = "El acumulador funciona igual que con while: empieza en 0 y en cada vuelta suma con +=. La diferencia es que for se encarga de cambiar n; no hace falta n += 1.",
        code = "total = 0\nfor n in range(1, 6):\n    total += n\nprint(total)",
        expectedOutput = "15",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "total = 0\nfor n in range(1, 4):\n    total += n\nprint(total)",
                acceptedAnswers = listOf("6"),
                correctFeedback = "¡Muy bien! 1 + 2 + 3 = 6.",
                wrongFeedback = "n toma 1, 2 y 3 (el 4 no se incluye). Súmalos."
            ),
            FillBlanks(
                prompt = "Completa para que total vaya juntando los valores de n.",
                codeTemplate = "total = 0\nfor n in range(1, 11):\n    total ___ n",
                answers = listOf("+="),
                chips = listOf("+=", "=", "-=", "=="),
                correctFeedback = "¡Sí! += suma n a lo que total ya tenía.",
                wrongFeedback = "Con = solo, total perdería lo anterior. Hay que sumar."
            ),
            OrderLines(
                prompt = "Ordena el programa que suma del 1 al 5 y muestra el total.",
                lines = listOf(
                    "total = 0",
                    "for n in range(1, 6):",
                    "    total += n",
                    "print(total)"
                ),
                correctFeedback = "¡Perfecto! El total empieza antes del for y se muestra después.",
                wrongFeedback = "total = 0 va antes del bucle y el print va fuera, sin sangría."
            )
        ),
        walkthrough = listOf(
            step(1, "El acumulador empieza en 0.", listOf("total" to "0")),
            step(2, "Primera vuelta: n vale 1.", listOf("total" to "0", "n" to "1")),
            step(3, "0 + 1 = 1.", listOf("total" to "1", "n" to "1")),
            step(2, "n vale 2.", listOf("total" to "1", "n" to "2")),
            step(3, "1 + 2 = 3.", listOf("total" to "3", "n" to "2")),
            step(2, "n vale 3.", listOf("total" to "3", "n" to "3")),
            step(3, "3 + 3 = 6.", listOf("total" to "6", "n" to "3")),
            step(2, "n vale 4.", listOf("total" to "6", "n" to "4")),
            step(3, "6 + 4 = 10.", listOf("total" to "10", "n" to "4")),
            step(2, "n vale 5, el último.", listOf("total" to "10", "n" to "5")),
            step(3, "10 + 5 = 15.", listOf("total" to "15", "n" to "5")),
            step(2, "No quedan valores (el 6 no se incluye): termina.", listOf("total" to "15", "n" to "5")),
            step(4, "Muestra el total.", listOf("total" to "15", "n" to "5"), listOf("15"))
        )
    )

    private val forConIf = LessonContent(
        lessonId = "n08_l07",
        title = "for con if 🔀",
        hook = "Glitch busca los múltiplos de 3 entre el 1 y el 6. El for recorre y el if elige.",
        explanation = "El for recorre todos los números y el if decide en cada vuelta. El contador se crea antes del for: si estuviera dentro, se volvería a 0 en cada vuelta y perdería la cuenta.",
        code = "contador = 0\nfor n in range(1, 7):\n    if n % 3 == 0:\n        contador += 1\nprint(contador)",
        expectedOutput = "2",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "for n in range(1, 7):\n    if n % 2 == 0:\n        print(n)",
                acceptedAnswers = listOf("2\n4\n6"),
                correctFeedback = "¡Exacto! Solo los pares pasan el if.",
                wrongFeedback = "n recorre del 1 al 6, pero solo se imprime si el resto entre 2 es 0."
            ),
            FillBlanks(
                prompt = "Completa la condición que detecta los múltiplos de 3.",
                codeTemplate = "if n % 3 ___ 0:\n    contador += 1",
                answers = listOf("=="),
                chips = listOf("==", "=", "!=", ">"),
                correctFeedback = "¡Sí! Múltiplo de 3 significa resto 0 al dividir entre 3.",
                wrongFeedback = "Para comparar si dos cosas son iguales se usan dos signos =."
            ),
            FindBug(
                prompt = "Este programa siempre muestra 0 o 1, nunca la cuenta completa. Toca el error.",
                codeLines = listOf(
                    "for n in range(1, 10):",
                    "    contador = 0",
                    "    if n % 3 == 0:",
                    "        contador += 1",
                    "print(contador)"
                ),
                bugLineIndex = 1,
                correctFeedback = "¡Bug cazado! El contador se reinicia en cada vuelta. Debe ir antes del for.",
                wrongFeedback = "¿Qué le pasa al contador al empezar cada vuelta?"
            )
        ),
        walkthrough = listOf(
            step(1, "El contador empieza en 0, antes del bucle.", listOf("contador" to "0")),
            step(2, "n vale 1.", listOf("contador" to "0", "n" to "1")),
            step(3, "1 % 3 es 1, no 0: no es múltiplo. Se salta la línea 4.", listOf("contador" to "0", "n" to "1")),
            step(2, "n vale 2.", listOf("contador" to "0", "n" to "2")),
            step(3, "2 % 3 es 2: no es múltiplo.", listOf("contador" to "0", "n" to "2")),
            step(2, "n vale 3.", listOf("contador" to "0", "n" to "3")),
            step(3, "3 % 3 es 0: ¡es múltiplo!", listOf("contador" to "0", "n" to "3")),
            step(4, "Cuenta uno.", listOf("contador" to "1", "n" to "3")),
            step(2, "n vale 4.", listOf("contador" to "1", "n" to "4")),
            step(3, "4 % 3 es 1: no es múltiplo.", listOf("contador" to "1", "n" to "4")),
            step(2, "n vale 5.", listOf("contador" to "1", "n" to "5")),
            step(3, "5 % 3 es 2: no es múltiplo.", listOf("contador" to "1", "n" to "5")),
            step(2, "n vale 6, el último.", listOf("contador" to "1", "n" to "6")),
            step(3, "6 % 3 es 0: ¡múltiplo!", listOf("contador" to "1", "n" to "6")),
            step(4, "Cuenta otro.", listOf("contador" to "2", "n" to "6")),
            step(2, "No quedan valores: termina.", listOf("contador" to "2", "n" to "6")),
            step(5, "Muestra cuántos encontró.", listOf("contador" to "2", "n" to "6"), listOf("2"))
        )
    )

    private val tablas = LessonContent(
        lessonId = "n08_l08",
        title = "Tablas de multiplicar ✖️",
        hook = "Glitch tiene examen de la tabla del 3. ¿Y si el programa la escribe por él?",
        explanation = "La variable del for se puede usar en cálculos. Aquí i va de 1 a 3 y en cada vuelta se calcula 3 * i. Con print y comas se arma la frase completa.",
        code = "for i in range(1, 4):\n    print(3, \"x\", i, \"=\", 3 * i)",
        expectedOutput = "3 x 1 = 3\n3 x 2 = 6\n3 x 3 = 9",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "for i in range(1, 4):\n    print(2 * i)",
                acceptedAnswers = listOf("2\n4\n6"),
                correctFeedback = "¡Muy bien! 2 × 1, 2 × 2 y 2 × 3.",
                wrongFeedback = "i toma 1, 2 y 3. Multiplica cada uno por 2."
            ),
            FillBlanks(
                prompt = "Completa para mostrar la tabla del 7 completa, del 1 al 10.",
                codeTemplate = "for i in range(1, ___):\n    print(7, \"x\", i, \"=\", 7 * i)",
                answers = listOf("11"),
                chips = listOf("11", "10", "7", "12"),
                correctFeedback = "¡Sí! Para llegar al 10, el final tiene que ser 11.",
                wrongFeedback = "Recuerda que el final de range no se incluye."
            ),
            TypeCode(
                prompt = "Escribe la línea que muestra el resultado de 5 por i.",
                acceptedAnswers = listOf("print(5 * i)", "print(i * 5)"),
                placeholder = "print(...)",
                correctFeedback = "¡Eso! El asterisco multiplica.",
                wrongFeedback = "Usa print y dentro la multiplicación con *."
            )
        ),
        walkthrough = listOf(
            step(1, "i vale 1.", listOf("i" to "1")),
            step(2, "Calcula 3 * 1 = 3 y lo muestra con la frase.", listOf("i" to "1"), listOf("3 x 1 = 3")),
            step(1, "i vale 2.", listOf("i" to "2"), listOf("3 x 1 = 3")),
            step(2, "3 * 2 = 6.", listOf("i" to "2"), listOf("3 x 1 = 3", "3 x 2 = 6")),
            step(1, "i vale 3.", listOf("i" to "3"), listOf("3 x 1 = 3", "3 x 2 = 6")),
            step(2, "3 * 3 = 9.", listOf("i" to "3"), listOf("3 x 1 = 3", "3 x 2 = 6", "3 x 3 = 9")),
            step(1, "El 4 no se incluye: termina.", listOf("i" to "3"), listOf("3 x 1 = 3", "3 x 2 = 6", "3 x 3 = 9"))
        )
    )

    private val forOWhile = LessonContent(
        lessonId = "n08_l09",
        title = "¿for o while? ⚖️",
        hook = "Glitch conoce dos bucles y no sabe cuál elegir. Hay una regla sencilla.",
        explanation = "Si sabes cuántas vueltas quieres, usa for: este ejemplo hace lo mismo que i = 1, while i <= 3: print(i), i += 1, pero en dos líneas. Si dependes de algo que no sabes de antemano (como que el usuario acierte una clave), usa while.",
        code = "for i in range(1, 4):\n    print(i)",
        expectedOutput = "1\n2\n3",
        exercises = listOf(
            MultipleChoice(
                prompt = "Glitch debe pedir la clave hasta que el usuario acierte. ¿Qué bucle conviene?",
                options = listOf(
                    "while, porque no sabe cuántos intentos harán falta",
                    "for, porque siempre son 3 intentos",
                    "if, porque se pregunta una sola vez",
                    "Ninguno, basta con un print"
                ),
                correctIndex = 0,
                correctFeedback = "¡Exacto! No se sabe cuántas vueltas harán falta: eso es trabajo de while.",
                wrongFeedback = "¿Sabes de antemano cuántas veces fallará el usuario?"
            ),
            OrderLines(
                prompt = "Ordena la versión con while del ejemplo (imprime 1, 2 y 3).",
                lines = listOf(
                    "i = 1",
                    "while i <= 3:",
                    "    print(i)",
                    "    i += 1"
                ),
                correctFeedback = "¡Perfecto! Con while hay que crear y sumar la variable a mano.",
                wrongFeedback = "La variable se crea antes del while; dentro se imprime y luego se suma 1."
            ),
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "for i in range(2, 4):\n    print(i * 10)",
                acceptedAnswers = listOf("20\n30"),
                correctFeedback = "¡Muy bien! i vale 2 y 3; multiplicados por 10.",
                wrongFeedback = "range(2, 4) da 2 y 3. Multiplica cada uno por 10."
            )
        ),
        walkthrough = listOf(
            step(1, "for crea i y le da el primer valor: 1. (Con while, eso era i = 1.)", listOf("i" to "1")),
            step(2, "Imprime 1.", listOf("i" to "1"), listOf("1")),
            step(1, "for pasa solo al siguiente valor: 2. (Con while, eso era i += 1.)", listOf("i" to "2"), listOf("1")),
            step(2, "Imprime 2.", listOf("i" to "2"), listOf("1", "2")),
            step(1, "i vale 3.", listOf("i" to "3"), listOf("1", "2")),
            step(2, "Imprime 3.", listOf("i" to "3"), listOf("1", "2", "3")),
            step(1, "No quedan valores: termina. for se encargó de todo el conteo.", listOf("i" to "3"), listOf("1", "2", "3"))
        )
    )

    private val decideElUsuario = LessonContent(
        lessonId = "n08_l10",
        title = "Cuántas veces lo decide el usuario 🙋",
        hook = "Glitch pregunta cuántas veces gritar «¡Hurra!» y obedece.",
        explanation = "range acepta una variable, así que el número de vueltas puede venir del usuario. Como input() devuelve texto, hay que convertirlo con int() antes de dárselo a range. La salida de abajo es la que verías si escribes 2.",
        code = "veces = int(input(\"¿Cuántas veces? \"))\nfor i in range(veces):\n    print(\"¡Hurra!\")",
        expectedOutput = "¿Cuántas veces? 2\n¡Hurra!\n¡Hurra!",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "n = 3\nfor i in range(n):\n    print(\"*\")",
                acceptedAnswers = listOf("*\n*\n*"),
                correctFeedback = "¡Exacto! range(n) con n = 3 da 3 vueltas.",
                wrongFeedback = "range(n) usa el valor de n. ¿Cuánto vale?"
            ),
            FillBlanks(
                prompt = "Completa para repetir tantas veces como diga la variable veces.",
                codeTemplate = "for i in range(___):\n    print(\"¡Hurra!\")",
                answers = listOf("veces"),
                chips = listOf("veces", "\"veces\"", "input", "i"),
                correctFeedback = "¡Sí! range recibe la variable, sin comillas.",
                wrongFeedback = "Con comillas sería un texto, no la variable. Elige el nombre tal cual."
            ),
            FindBug(
                prompt = "Este programa falla al llegar al for. Toca la línea que causa el problema.",
                codeLines = listOf(
                    "veces = input(\"¿Cuántas veces? \")",
                    "for i in range(veces):",
                    "    print(\"¡Hurra!\")"
                ),
                bugLineIndex = 0,
                correctFeedback = "¡Bug cazado! input() devuelve texto; range necesita un número: falta int().",
                wrongFeedback = "range necesita un número. ¿Qué tipo de dato devuelve input()?"
            )
        ),
        walkthrough = listOf(
            step(1, "Pregunta y convierte a número. Supón que el usuario escribe 2.", listOf("veces" to "2"), listOf("¿Cuántas veces? 2")),
            step(2, "range(veces) es range(2). Primera vuelta: i vale 0.", listOf("veces" to "2", "i" to "0"), listOf("¿Cuántas veces? 2")),
            step(3, "¡Hurra!", listOf("veces" to "2", "i" to "0"), listOf("¿Cuántas veces? 2", "¡Hurra!")),
            step(2, "Segunda vuelta: i vale 1.", listOf("veces" to "2", "i" to "1"), listOf("¿Cuántas veces? 2", "¡Hurra!")),
            step(3, "¡Hurra!", listOf("veces" to "2", "i" to "1"), listOf("¿Cuántas veces? 2", "¡Hurra!", "¡Hurra!")),
            step(2, "Ya dio 2 vueltas: termina.", listOf("veces" to "2", "i" to "1"), listOf("¿Cuántas veces? 2", "¡Hurra!", "¡Hurra!"))
        )
    )

    private val glitchYElFor = LessonContent(
        lessonId = "n08_l11",
        title = "Glitch y el for 🤖",
        hook = "¿Te acuerdas del bloque →×4 del dron en Glitch City? Era un for todo el tiempo.",
        explanation = "for i in range(3) con avanzar() adentro hace avanzar a Glitch 3 casillas: es exactamente lo que hace un bloque ×3 del dron. Cuando veas «Tu programa en Python» en Competir, vas a reconocer este for.",
        code = "for i in range(3):\n    avanzar()\nprint(\"Glitch avanzó 3 casillas\")",
        expectedOutput = "Glitch avanzó 3 casillas",
        exercises = listOf(
            GlitchPuzzle(
                prompt = "Lleva a Glitch a la meta usando un for.",
                board = listOf(">...M"),
                mode = GlitchMode.BUILD,
                palette = listOf(GlitchBlock.Repeat(1), GlitchBlock.Forward),
                solution = "for i in range(4):\n    avanzar()",
                maxLines = 2,
                correctFeedback = "¡Llegó! Un for de 4 vueltas con avanzar() adentro.",
                wrongFeedback = "Cuenta las casillas hasta la meta y pon ese número en el range."
            ),
            GlitchPuzzle(
                prompt = "Este programa se queda corto. Arréglalo para que Glitch llegue a la meta.",
                board = listOf(">....M"),
                mode = GlitchMode.FIX,
                palette = listOf(GlitchBlock.Repeat(1), GlitchBlock.Forward),
                starter = "for i in range(2):\n    avanzar()",
                solution = "for i in range(5):\n    avanzar()",
                correctFeedback = "¡Arreglado! Ahora el range da las vueltas que hacen falta.",
                wrongFeedback = "Glitch avanza una casilla por vuelta. ¿Cuántas vueltas necesita?"
            ),
            PredictOutput(
                prompt = "En Glitch City, el bloque →×3 del dron es este for. ¿Qué muestra? Escríbelo línea por línea.",
                code = "for paso in range(3):\n    print(\"mover_derecha()\")",
                acceptedAnswers = listOf("mover_derecha()\nmover_derecha()\nmover_derecha()"),
                correctFeedback = "¡Exacto! Tres vueltas, tres movimientos: eso es el ×3.",
                wrongFeedback = "range(3) da 3 vueltas, y en cada una se imprime la misma línea."
            )
        ),
        walkthrough = listOf(
            step(1, "Primera vuelta: i vale 0.", listOf("i" to "0")),
            step(2, "Glitch avanza una casilla.", listOf("i" to "0")),
            step(1, "Segunda vuelta: i vale 1.", listOf("i" to "1")),
            step(2, "Avanza otra casilla.", listOf("i" to "1")),
            step(1, "Tercera vuelta: i vale 2.", listOf("i" to "2")),
            step(2, "Tercera casilla.", listOf("i" to "2")),
            step(1, "range(3) terminó: 3 vueltas, 3 casillas.", listOf("i" to "2")),
            step(3, "Fuera del bucle: avisa.", listOf("i" to "2"), listOf("Glitch avanzó 3 casillas"))
        )
    )

    // Jefe del nivel: mezcla lo aprendido en las 11 lecciones.
    val boss = BossContent(
        bossId = "n08_boss",
        name = "La Hidra de los Ciclos",
        emoji = "🐉",
        intro = "A esta hidra le crece una cabeza en cada vuelta de su bucle. Solo la vence quien sabe exactamente cuántas vueltas da un for.",
        exercises = listOf(
            PredictOutput(
                prompt = "Primera cabeza. ¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "for i in range(1, 8, 3):\n    print(i)",
                acceptedAnswers = listOf("1\n4\n7"),
                correctFeedback = "¡Golpe! 1, 4 y 7; el siguiente sería 10.",
                wrongFeedback = "Empieza en 1 y salta de 3 en 3 sin llegar al 8."
            ),
            FindBug(
                prompt = "La Hidra quiere mostrar del 1 al 5, pero se queda en el 4. ¡Toca el error!",
                codeLines = listOf(
                    "for cabeza in range(1, 5):",
                    "    print(cabeza)"
                ),
                bugLineIndex = 0,
                correctFeedback = "¡Bug golpeado! Para incluir el 5, el final tiene que ser 6.",
                wrongFeedback = "El final de range no se incluye. ¿Hasta dónde llega este?"
            ),
            PredictOutput(
                prompt = "La Hidra suma sus cabezas. ¿Qué muestra este programa?",
                code = "total = 0\nfor i in range(5):\n    total += i\nprint(total)",
                acceptedAnswers = listOf("10"),
                correctFeedback = "¡Muy bien! 0 + 1 + 2 + 3 + 4 = 10.",
                wrongFeedback = "range(5) empieza en 0 y llega hasta 4. Suma esos valores."
            ),
            FillBlanks(
                prompt = "Completa para contar hacia atrás de 2 en 2: 10, 8, 6, 4, 2.",
                codeTemplate = "for n in range(10, 0, ___):\n    print(n)",
                answers = listOf("-2"),
                chips = listOf("-2", "2", "-1", "0"),
                correctFeedback = "¡Cabeza cortada! Un salto de -2 baja de 2 en 2.",
                wrongFeedback = "Para bajar de 2 en 2, el salto tiene que ser negativo y de tamaño 2."
            ),
            TypeCode(
                prompt = "Último golpe: escribe el for que recorre del 1 al 10 con i.",
                acceptedAnswers = listOf("for i in range(1, 11):"),
                placeholder = "for ...",
                correctFeedback = "¡Hidra vencida! Dominas el for y el range.",
                wrongFeedback = "Escribe for i in range(1, 11): (el 11 no se incluye, así llega al 10)."
            )
        )
    )

    val all: List<LessonContent> = listOf(
        repetirFijo, valoresDeI, desdeHasta, conSaltos,
        haciaAtras, acumularConFor, forConIf, tablas,
        forOWhile, decideElUsuario, glitchYElFor
    )
}
