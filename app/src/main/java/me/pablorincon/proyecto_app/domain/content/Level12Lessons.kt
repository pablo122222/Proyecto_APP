package me.pablorincon.proyecto_app.domain.content

import me.pablorincon.proyecto_app.domain.BossContent
import me.pablorincon.proyecto_app.domain.FillBlanks
import me.pablorincon.proyecto_app.domain.FindBug
import me.pablorincon.proyecto_app.domain.LessonContent
import me.pablorincon.proyecto_app.domain.MultipleChoice
import me.pablorincon.proyecto_app.domain.OrderLines
import me.pablorincon.proyecto_app.domain.PredictOutput
import me.pablorincon.proyecto_app.domain.TypeCode
import me.pablorincon.proyecto_app.domain.WalkStep

// Nivel 12 · Funciones I (11 lecciones y el jefe «El Titán de las Funciones»): def, llamar,
// parámetros, return y la diferencia con print, y funciones que deciden o repiten.
internal object Level12Lessons {

    private fun step(line: Int, note: String, vars: List<Pair<String, String>>, out: List<String> = emptyList()) =
        WalkStep(line, note, vars, out)

    private val def = LessonContent(
        lessonId = "n12_l01",
        title = "Ponerle nombre a un bloque: def 🏷️",
        hook = "Glitch saluda igual todas las mañanas. ¿Y si le enseña a Python un comando nuevo llamado saludar?",
        explanation = "Una función es un bloque de código con nombre. Se crea con def nombre(): y las líneas de adentro van con sangría. Después, para usarla, se escribe su nombre con paréntesis: saludar(). Eso se llama \"llamar\" a la función.",
        code = "def saludar():\n    print(\"Hola\")\n    print(\"Soy Glitch\")\nsaludar()",
        expectedOutput = "Hola\nSoy Glitch",
        exercises = listOf(
            OrderLines(
                prompt = "Ordena el programa: define la función despedir y luego llámala.",
                lines = listOf(
                    "def despedir():",
                    "    print(\"Chao\")",
                    "despedir()"
                ),
                correctFeedback = "¡Perfecto! Primero se define, después se llama.",
                wrongFeedback = "La función debe existir antes de llamarla, y su contenido va con sangría justo debajo del def."
            ),
            TypeCode(
                prompt = "Escribe la primera línea de una función llamada bailar, sin parámetros.",
                acceptedAnswers = listOf("def bailar():"),
                placeholder = "def ...",
                correctFeedback = "¡Exacto! def, el nombre, paréntesis y dos puntos.",
                wrongFeedback = "No olvides los paréntesis y los dos puntos al final."
            ),
            FillBlanks(
                prompt = "Completa para crear la función aplaudir.",
                codeTemplate = "___ aplaudir():\n    print(\"clap\")",
                answers = listOf("def"),
                chips = listOf("def", "function", "fun", "for"),
                correctFeedback = "¡Sí! En Python las funciones se crean con def.",
                wrongFeedback = "Es una palabra corta, de \"define\"."
            )
        ),
        walkthrough = listOf(
            step(1, "Se define la función: Python la guarda, pero todavía no ejecuta nada de adentro.", emptyList()),
            step(4, "Aquí se llama a saludar: el programa salta adentro de la función.", emptyList()),
            step(2, "Primera línea de la función.", emptyList(), listOf("Hola")),
            step(3, "Segunda línea. Al terminar, vuelve a donde la llamaron.", emptyList(), listOf("Hola", "Soy Glitch"))
        )
    )

    private val llamar = LessonContent(
        lessonId = "n12_l02",
        title = "Definir no es llamar 📞",
        hook = "Glitch escribió su función alarma, pero nunca sonó… hasta que la llamó.",
        explanation = "Definir una función es como guardar una receta: no cocina nada. Lo de adentro solo se ejecuta cuando la llamas con nombre(). Puedes llamarla todas las veces que quieras, y cada vez se ejecuta completa.",
        code = "def alarma():\n    print(\"¡Despierta!\")\nprint(\"Inicio\")\nalarma()\nalarma()",
        expectedOutput = "Inicio\n¡Despierta!\n¡Despierta!",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "def hola():\n    print(\"hola\")\nprint(\"fin\")",
                acceptedAnswers = listOf("fin"),
                correctFeedback = "¡Muy bien! La función se definió, pero nadie la llamó.",
                wrongFeedback = "¿En qué parte del programa se llama a hola()?"
            ),
            MultipleChoice(
                prompt = "Una función tiene un print adentro y se llama 3 veces. ¿Cuántas veces se ejecuta ese print?",
                options = listOf("0", "1", "3", "Depende del print"),
                correctIndex = 2,
                correctFeedback = "¡Correcto! Cada llamada ejecuta la función completa.",
                wrongFeedback = "Cada vez que llamas a la función, se ejecuta todo lo de adentro."
            ),
            FindBug(
                prompt = "El menú nunca aparece. ¡Toca el error!",
                codeLines = listOf(
                    "def menu():",
                    "    print(\"1. Jugar\")",
                    "menu"
                ),
                bugLineIndex = 2,
                correctFeedback = "¡Cazado! Sin paréntesis no se llama a la función: debe ser menu().",
                wrongFeedback = "¿Cómo se llama a una función? Mira la última línea."
            )
        ),
        walkthrough = listOf(
            step(1, "Se guarda la función alarma, sin ejecutarla.", emptyList()),
            step(3, "La primera línea que realmente se ejecuta.", emptyList(), listOf("Inicio")),
            step(4, "Primera llamada: salta a la función.", emptyList(), listOf("Inicio")),
            step(2, "Suena la alarma.", emptyList(), listOf("Inicio", "¡Despierta!")),
            step(5, "Segunda llamada.", emptyList(), listOf("Inicio", "¡Despierta!")),
            step(2, "Suena otra vez.", emptyList(), listOf("Inicio", "¡Despierta!", "¡Despierta!"))
        )
    )

    private val parametros = LessonContent(
        lessonId = "n12_l03",
        title = "Parámetros 🎁",
        hook = "Glitch quiere saludar a cada amigo por su nombre, con la misma función.",
        explanation = "Un parámetro es una variable que la función recibe entre los paréntesis: def saludar(nombre):. Al llamarla le pasas un valor, saludar(\"Ana\"), y dentro de la función nombre vale \"Ana\". Así una función sirve para muchos casos.",
        code = "def saludar(nombre):\n    print(\"Hola,\", nombre)\nsaludar(\"Ana\")\nsaludar(\"Leo\")",
        expectedOutput = "Hola, Ana\nHola, Leo",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "def doble(n):\n    print(n * 2)\ndoble(5)",
                acceptedAnswers = listOf("10"),
                correctFeedback = "¡Exacto! n vale 5 dentro de la función.",
                wrongFeedback = "Al llamar doble(5), el parámetro n recibe el 5."
            ),
            FillBlanks(
                prompt = "Completa el parámetro que usa la función.",
                codeTemplate = "def ladrar(___):\n    print(\"Guau\" * veces)\nladrar(2)",
                answers = listOf("veces"),
                chips = listOf("veces", "2", "n", "ladrar"),
                correctFeedback = "¡Bien! El parámetro se llama igual que la variable que se usa adentro.",
                wrongFeedback = "Mira qué nombre usa el print dentro de la función."
            ),
            TypeCode(
                prompt = "Llama a la función saludar pasándole el texto \"Glitch\".",
                acceptedAnswers = listOf("saludar(\"Glitch\")"),
                placeholder = "saludar...",
                correctFeedback = "¡Perfecto! El valor va entre los paréntesis.",
                wrongFeedback = "Escribe el nombre de la función y, entre paréntesis, el texto con comillas."
            )
        ),
        walkthrough = listOf(
            step(1, "Se guarda la función, que espera un nombre.", emptyList()),
            step(3, "Llamada con \"Ana\": dentro, nombre vale Ana.", listOf("nombre" to "'Ana'")),
            step(2, "Saluda a Ana.", listOf("nombre" to "'Ana'"), listOf("Hola, Ana")),
            step(4, "Nueva llamada: ahora nombre vale Leo.", listOf("nombre" to "'Leo'"), listOf("Hola, Ana")),
            step(2, "Saluda a Leo.", listOf("nombre" to "'Leo'"), listOf("Hola, Ana", "Hola, Leo"))
        )
    )

    private val variosParametros = LessonContent(
        lessonId = "n12_l04",
        title = "Varios parámetros y su orden 🔢",
        hook = "Glitch presentó a Ana diciendo \"12 tiene Ana años\". Algo salió muy mal…",
        explanation = "Una función puede recibir varios parámetros separados por comas. Al llamarla, los valores se asignan en el MISMO ORDEN: el primero al primer parámetro, el segundo al segundo. Si los cambias de orden, la función recibe cosas equivocadas.",
        code = "def presentar(nombre, edad):\n    print(nombre, \"tiene\", edad, \"años\")\npresentar(\"Ana\", 12)\npresentar(12, \"Ana\")",
        expectedOutput = "Ana tiene 12 años\n12 tiene Ana años",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "def resta(a, b):\n    print(a - b)\nresta(10, 3)\nresta(3, 10)",
                acceptedAnswers = listOf("7\n-7"),
                correctFeedback = "¡Muy bien! El orden cambia el resultado.",
                wrongFeedback = "En la segunda llamada a vale 3 y b vale 10."
            ),
            FindBug(
                prompt = "Glitch quería \"Bip mide 30\", pero sale \"30 mide Bip\". ¡Toca el error!",
                codeLines = listOf(
                    "def medir(robot, cm):",
                    "    print(robot, \"mide\", cm)",
                    "medir(30, \"Bip\")"
                ),
                bugLineIndex = 2,
                correctFeedback = "¡Cazado! Los valores van en el orden de los parámetros: medir(\"Bip\", 30).",
                wrongFeedback = "La función está bien. Revisa el orden de los valores en la llamada."
            ),
            FillBlanks(
                prompt = "Completa la llamada con los dos valores.",
                codeTemplate = "def area(base, altura):\n    print(base * altura)\narea(4___ 5)",
                answers = listOf(","),
                chips = listOf(",", ";", "+", "y"),
                correctFeedback = "¡Eso! Los valores se separan con comas.",
                wrongFeedback = "Igual que los elementos de una lista, los valores se separan con…"
            )
        ),
        walkthrough = listOf(
            step(1, "La función espera nombre y edad, en ese orden.", emptyList()),
            step(3, "Primera llamada: nombre = Ana y edad = 12.", listOf("nombre" to "'Ana'", "edad" to "12")),
            step(2, "Todo bien.", listOf("nombre" to "'Ana'", "edad" to "12"), listOf("Ana tiene 12 años")),
            step(4, "Valores al revés: nombre = 12 y edad = Ana.", listOf("nombre" to "12", "edad" to "'Ana'"), listOf("Ana tiene 12 años")),
            step(2, "Python no se da cuenta: hace lo que le dijiste.", listOf("nombre" to "12", "edad" to "'Ana'"), listOf("Ana tiene 12 años", "12 tiene Ana años"))
        )
    )

    private val devolver = LessonContent(
        lessonId = "n12_l05",
        title = "return: devolver un resultado ↩️",
        hook = "Glitch no quiere que su función muestre el doble: quiere que se lo ENTREGUE para usarlo después.",
        explanation = "return valor hace que la función devuelva un resultado a quien la llamó. Ese resultado se puede guardar en una variable, imprimir o usar en una cuenta: resultado = doble(4). Es como una máquina: entra algo y sale otra cosa.",
        code = "def doble(n):\n    return n * 2\nresultado = doble(4)\nprint(resultado)",
        expectedOutput = "8",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "def suma(a, b):\n    return a + b\nprint(suma(2, 3))",
                acceptedAnswers = listOf("5"),
                correctFeedback = "¡Exacto! suma devuelve 5 y print lo muestra.",
                wrongFeedback = "La función devuelve a + b. ¿Cuánto es con 2 y 3?"
            ),
            FillBlanks(
                prompt = "Completa para que la función devuelva el cuadrado.",
                codeTemplate = "def cuadrado(x):\n    ___ x * x",
                answers = listOf("return"),
                chips = listOf("return", "print", "def", "="),
                correctFeedback = "¡Sí! return entrega el resultado.",
                wrongFeedback = "Necesitas la palabra que DEVUELVE el valor, no la que lo muestra."
            ),
            TypeCode(
                prompt = "Escribe la línea que devuelve el triple de n.",
                acceptedAnswers = listOf("return n * 3", "return 3 * n"),
                placeholder = "return ...",
                correctFeedback = "¡Perfecto!",
                wrongFeedback = "Empieza con return y luego la cuenta del triple."
            )
        ),
        walkthrough = listOf(
            step(1, "Se guarda la función doble.", emptyList()),
            step(3, "Se llama doble(4): n vale 4 dentro de la función.", listOf("n" to "4")),
            step(2, "Devuelve 4 * 2 = 8 a quien la llamó.", listOf("n" to "4")),
            step(3, "El 8 devuelto se guarda en resultado.", listOf("resultado" to "8")),
            step(4, "Se muestra.", listOf("resultado" to "8"), listOf("8"))
        )
    )

    private val printNoEsReturn = LessonContent(
        lessonId = "n12_l06",
        title = "print no es return 🆚",
        hook = "Dos funciones parecen hacer lo mismo… hasta que Glitch intenta guardar su resultado.",
        explanation = "print MUESTRA algo en pantalla, pero no entrega nada. return ENTREGA un valor, pero no muestra nada. Si una función no tiene return, devuelve None (\"nada\"). Para usar el resultado en otra parte, necesitas return.",
        code = "def con_print(n):\n    print(n + 1)\ndef con_return(n):\n    return n + 1\na = con_print(1)\nb = con_return(1)\nprint(a, b)",
        expectedOutput = "2\nNone 2",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "def f():\n    print(\"hola\")\nx = f()\nprint(x)",
                acceptedAnswers = listOf("hola\nNone"),
                correctFeedback = "¡Muy bien! f muestra hola, pero no devuelve nada: x es None.",
                wrongFeedback = "La función imprime algo, pero ¿tiene return? Si no, ¿qué vale x?"
            ),
            MultipleChoice(
                prompt = "Quieres usar el resultado de una función dentro de una cuenta. ¿Qué tiene que usar la función?",
                options = listOf("return", "print", "input", "Da igual cuál"),
                correctIndex = 0,
                correctFeedback = "¡Correcto! Solo return entrega un valor que se puede usar.",
                wrongFeedback = "print solo muestra en pantalla; no entrega nada para la cuenta."
            ),
            FindBug(
                prompt = "Sale \"Total: None\" en vez de \"Total: 5\". ¡Toca el error!",
                codeLines = listOf(
                    "def sumar(a, b):",
                    "    print(a + b)",
                    "total = sumar(2, 3)",
                    "print(\"Total:\", total)"
                ),
                bugLineIndex = 1,
                correctFeedback = "¡Cazado! La función debe devolver: return a + b.",
                wrongFeedback = "¿Por qué total queda en None? Mira lo que hace la función con el resultado."
            )
        ),
        walkthrough = listOf(
            step(1, "Se guarda con_print.", emptyList()),
            step(3, "Se guarda con_return.", emptyList()),
            step(5, "Se llama con_print(1).", listOf("n" to "1")),
            step(2, "Muestra 2… pero no devuelve nada.", listOf("n" to "1"), listOf("2")),
            step(5, "Como no hubo return, a vale None.", listOf("a" to "None"), listOf("2")),
            step(6, "Se llama con_return(1).", listOf("a" to "None", "n" to "1"), listOf("2")),
            step(4, "Devuelve 2 sin mostrar nada.", listOf("a" to "None", "n" to "1"), listOf("2")),
            step(6, "b guarda el 2.", listOf("a" to "None", "b" to "2"), listOf("2")),
            step(7, "Se ve la diferencia.", listOf("a" to "None", "b" to "2"), listOf("2", "None 2"))
        )
    )

    private val usarResultado = LessonContent(
        lessonId = "n12_l07",
        title = "Usar lo que devuelve una función 🧩",
        hook = "Glitch descubre que el resultado de una función se puede usar como cualquier número… ¡incluso dentro de otra llamada!",
        explanation = "Lo que devuelve una función se puede sumar, comparar, guardar o pasar a otra función. doble(3) + 1 primero calcula doble(3) y después le suma 1. En doble(doble(2)) se resuelve primero la llamada de adentro.",
        code = "def doble(n):\n    return n * 2\nx = doble(3) + 1\nprint(x)\nprint(doble(doble(2)))",
        expectedOutput = "7\n8",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "def mitad(n):\n    return n // 2\nprint(mitad(10) + mitad(4))",
                acceptedAnswers = listOf("7"),
                correctFeedback = "¡Exacto! 5 + 2 = 7.",
                wrongFeedback = "Calcula cada llamada por separado y después súmalas."
            ),
            TypeCode(
                prompt = "Guarda en total el resultado de llamar a sumar con 4 y 6.",
                acceptedAnswers = listOf("total = sumar(4, 6)"),
                placeholder = "total = ...",
                correctFeedback = "¡Perfecto! total recibe lo que devuelve sumar.",
                wrongFeedback = "A la izquierda la variable, después = y la llamada con sus dos valores."
            ),
            OrderLines(
                prompt = "Ordena el programa: define cubo, guarda cubo(2) en r y muéstralo.",
                lines = listOf(
                    "def cubo(n):",
                    "    return n * n * n",
                    "r = cubo(2)",
                    "print(r)"
                ),
                correctFeedback = "¡Muy bien! Muestra 8.",
                wrongFeedback = "Primero la función completa, después la llamada y al final el print."
            )
        ),
        walkthrough = listOf(
            step(1, "Se guarda la función doble.", emptyList()),
            step(3, "doble(3) devuelve 6; más 1 da 7.", listOf("x" to "7")),
            step(4, "Se muestra x.", listOf("x" to "7"), listOf("7")),
            step(5, "Primero doble(2) devuelve 4; luego doble(4) devuelve 8.", listOf("x" to "7"), listOf("7", "8"))
        )
    )

    private val returnCorta = LessonContent(
        lessonId = "n12_l08",
        title = "return corta la función ✋",
        hook = "Glitch puso un print después del return… y nunca se vio. ¿Por qué?",
        explanation = "Cuando la función llega a un return, devuelve el valor y termina en ese instante: lo que viene después no se ejecuta. Por eso se puede usar return dentro de un if para salir antes cuando algo no está bien.",
        code = "def revisar(edad):\n    if edad < 0:\n        return \"Edad imposible\"\n    return \"Edad válida\"\n    print(\"Esto nunca se ve\")\nprint(revisar(-3))\nprint(revisar(15))",
        expectedOutput = "Edad imposible\nEdad válida",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "def f():\n    return 1\n    return 2\nprint(f())",
                acceptedAnswers = listOf("1"),
                correctFeedback = "¡Bien! El primer return termina la función.",
                wrongFeedback = "¿Se llega a ejecutar la segunda línea con return?"
            ),
            FindBug(
                prompt = "Esta función siempre dice \"par\", incluso con el 7. ¡Toca el error!",
                codeLines = listOf(
                    "def tipo(n):",
                    "    return \"par\"",
                    "    if n % 2 != 0:",
                    "        return \"impar\""
                ),
                bugLineIndex = 1,
                correctFeedback = "¡Cazado! Ese return va primero y corta la función antes de revisar.",
                wrongFeedback = "Recuerda que return termina la función. ¿Cuál se ejecuta siempre primero?"
            ),
            FillBlanks(
                prompt = "Completa la función: si n es 0 o más, devuelve \"positivo\".",
                codeTemplate = "def signo(n):\n    if n >= 0:\n        ___ \"positivo\"\n    return \"negativo\"",
                answers = listOf("return"),
                chips = listOf("return", "print", "break", "else"),
                correctFeedback = "¡Eso! Si se cumple, devuelve y no llega a la última línea.",
                wrongFeedback = "Necesitas devolver el texto y terminar la función ahí mismo."
            )
        ),
        walkthrough = listOf(
            step(1, "Se guarda la función.", emptyList()),
            step(6, "Se llama con -3.", listOf("edad" to "-3")),
            step(2, "¿-3 < 0? Sí.", listOf("edad" to "-3")),
            step(3, "Devuelve y la función termina aquí.", listOf("edad" to "-3")),
            step(6, "Se muestra lo devuelto.", emptyList(), listOf("Edad imposible")),
            step(7, "Se llama con 15.", listOf("edad" to "15"), listOf("Edad imposible")),
            step(2, "¿15 < 0? No.", listOf("edad" to "15"), listOf("Edad imposible")),
            step(4, "Devuelve \"Edad válida\". La línea 5 nunca se ejecuta.", listOf("edad" to "15"), listOf("Edad imposible")),
            step(7, "Se muestra.", emptyList(), listOf("Edad imposible", "Edad válida"))
        )
    )

    private val decidir = LessonContent(
        lessonId = "n12_l09",
        title = "Funciones que deciden 🚦",
        hook = "Glitch quiere una función que convierta puntos en una nota con letra.",
        explanation = "Dentro de una función puedes usar if, elif y else, y devolver un resultado distinto en cada camino. Así la decisión queda guardada con un nombre y la puedes usar cuantas veces quieras.",
        code = "def nota(puntos):\n    if puntos >= 90:\n        return \"A\"\n    elif puntos >= 70:\n        return \"B\"\n    else:\n        return \"C\"\nprint(nota(95))\nprint(nota(70))",
        expectedOutput = "A\nB",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "def es_mayor(edad):\n    return edad >= 18\nprint(es_mayor(20))\nprint(es_mayor(9))",
                acceptedAnswers = listOf("True\nFalse"),
                correctFeedback = "¡Exacto! Una función también puede devolver True o False.",
                wrongFeedback = "La función devuelve el resultado de la comparación: True o False."
            ),
            FillBlanks(
                prompt = "Completa: si n es negativo, devuelve su valor positivo.",
                codeTemplate = "def positivo(n):\n    if n < 0:\n        return ___\n    return n",
                answers = listOf("-n"),
                chips = listOf("-n", "n", "0", "n - 1"),
                correctFeedback = "¡Muy bien! Con n = -5, -n es 5.",
                wrongFeedback = "Si n es -5, ¿qué hay que hacerle para que dé 5?"
            ),
            OrderLines(
                prompt = "Ordena la función que dice si un número es par o impar.",
                lines = listOf(
                    "def par(n):",
                    "    if n % 2 == 0:",
                    "        return \"par\"",
                    "    return \"impar\""
                ),
                correctFeedback = "¡Perfecto! Si no es par, llega al último return.",
                wrongFeedback = "El def va primero; el if revisa; su return va adentro; el otro return queda para cuando no se cumple."
            )
        ),
        walkthrough = listOf(
            step(1, "Se guarda la función nota.", emptyList()),
            step(8, "Se llama con 95.", listOf("puntos" to "95")),
            step(2, "¿95 >= 90? Sí.", listOf("puntos" to "95")),
            step(3, "Devuelve \"A\".", listOf("puntos" to "95")),
            step(8, "Se muestra.", emptyList(), listOf("A")),
            step(9, "Se llama con 70.", listOf("puntos" to "70"), listOf("A")),
            step(2, "¿70 >= 90? No.", listOf("puntos" to "70"), listOf("A")),
            step(4, "¿70 >= 70? Sí.", listOf("puntos" to "70"), listOf("A")),
            step(5, "Devuelve \"B\".", listOf("puntos" to "70"), listOf("A")),
            step(9, "Se muestra.", emptyList(), listOf("A", "B"))
        )
    )

    private val conBucles = LessonContent(
        lessonId = "n12_l10",
        title = "Funciones con bucles 🔁",
        hook = "Glitch quiere sumar todos los números del 1 hasta el que le digan, sin hacerlo a mano.",
        explanation = "Una función puede tener un bucle adentro. Lo importante: el return va FUERA del bucle (con menos sangría), para devolver el resultado solo cuando el bucle terminó todas sus vueltas.",
        code = "def sumar_hasta(n):\n    total = 0\n    for i in range(1, n + 1):\n        total += i\n    return total\nprint(sumar_hasta(3))",
        expectedOutput = "6",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "def contar_a(texto):\n    n = 0\n    for c in texto:\n        if c == \"a\":\n            n += 1\n    return n\nprint(contar_a(\"casa\"))",
                acceptedAnswers = listOf("2"),
                correctFeedback = "¡Bien! casa tiene dos a.",
                wrongFeedback = "La función cuenta las letras a del texto que recibe."
            ),
            FindBug(
                prompt = "Esta función solo suma el primer número de la lista. ¡Toca el error!",
                codeLines = listOf(
                    "def suma_lista(lista):",
                    "    total = 0",
                    "    for x in lista:",
                    "        total += x",
                    "        return total"
                ),
                bugLineIndex = 4,
                correctFeedback = "¡Cazado! El return está dentro del for: devuelve en la primera vuelta. Debe ir con menos sangría.",
                wrongFeedback = "Revisa la sangría. ¿Qué línea termina la función demasiado pronto?"
            ),
            TypeCode(
                prompt = "Escribe la línea que llama a sumar_hasta con 10 y muestra el resultado.",
                acceptedAnswers = listOf("print(sumar_hasta(10))"),
                placeholder = "print(...)",
                correctFeedback = "¡Perfecto! Mostraría 55.",
                wrongFeedback = "Pon la llamada sumar_hasta(10) dentro de print."
            )
        ),
        walkthrough = listOf(
            step(1, "Se guarda la función.", emptyList()),
            step(6, "Se llama con 3.", listOf("n" to "3")),
            step(2, "El total empieza en 0.", listOf("n" to "3", "total" to "0")),
            step(3, "i vale 1.", listOf("n" to "3", "total" to "0", "i" to "1")),
            step(4, "total = 1.", listOf("n" to "3", "total" to "1", "i" to "1")),
            step(3, "i vale 2.", listOf("n" to "3", "total" to "1", "i" to "2")),
            step(4, "total = 3.", listOf("n" to "3", "total" to "3", "i" to "2")),
            step(3, "i vale 3.", listOf("n" to "3", "total" to "3", "i" to "3")),
            step(4, "total = 6.", listOf("n" to "3", "total" to "6", "i" to "3")),
            step(5, "El for terminó: ahora sí se devuelve el total.", listOf("n" to "3", "total" to "6", "i" to "3")),
            step(6, "Se muestra el 6.", emptyList(), listOf("6"))
        )
    )

    private val descuentos = LessonContent(
        lessonId = "n12_l11",
        title = "Mini programa: calculadora de descuentos 🏷️",
        hook = "La tienda de Glitch City tiene ofertas. Glitch arma dos funciones que trabajan en equipo.",
        explanation = "Una función puede usar a otra: precio_final llama a descuento para saber cuánto restar. Cada función hace una sola cosa y tiene un nombre claro. Usamos // para que el resultado quede en números enteros.",
        code = "def descuento(precio, porcentaje):\n    return precio * porcentaje // 100\ndef precio_final(precio, porcentaje):\n    return precio - descuento(precio, porcentaje)\nprint(precio_final(200, 10))\nprint(precio_final(50, 0))",
        expectedOutput = "180\n50",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "def total(precio, cantidad):\n    return precio * cantidad\nprint(total(3, 4) - 2)",
                acceptedAnswers = listOf("10"),
                correctFeedback = "¡Exacto! 3 × 4 = 12, menos 2 = 10.",
                wrongFeedback = "Primero calcula lo que devuelve total(3, 4) y después réstale 2."
            ),
            FillBlanks(
                prompt = "Las compras de 100 o más tienen envío gratis; las demás pagan 10 de envío.",
                codeTemplate = "def con_envio(precio):\n    if precio >= 100:\n        return precio\n    return precio ___ 10",
                answers = listOf("+"),
                chips = listOf("+", "-", "*", "//"),
                correctFeedback = "¡Bien! El envío se suma al precio.",
                wrongFeedback = "El envío es un costo extra: ¿se suma o se resta?"
            ),
            OrderLines(
                prompt = "Ordena el programa: define mitad_precio y muestra el precio a mitad de 80.",
                lines = listOf(
                    "def mitad_precio(p):",
                    "    return p // 2",
                    "print(mitad_precio(80))"
                ),
                correctFeedback = "¡Perfecto! Muestra 40.",
                wrongFeedback = "La función completa primero; después se usa."
            )
        ),
        walkthrough = listOf(
            step(1, "Se guarda descuento.", emptyList()),
            step(3, "Se guarda precio_final.", emptyList()),
            step(5, "Se llama precio_final(200, 10).", listOf("precio" to "200", "porcentaje" to "10")),
            step(4, "Para saber cuánto restar, llama a descuento(200, 10).", listOf("precio" to "200", "porcentaje" to "10")),
            step(2, "200 * 10 // 100 = 20.", listOf("precio" to "200", "porcentaje" to "10")),
            step(4, "200 - 20 = 180.", listOf("precio" to "200", "porcentaje" to "10")),
            step(5, "Se muestra.", emptyList(), listOf("180")),
            step(6, "Con 0 % el descuento es 0: queda igual.", listOf("precio" to "50", "porcentaje" to "0"), listOf("180", "50"))
        )
    )

    // Jefe del nivel: mezcla lo aprendido en las 11 lecciones.
    val boss = BossContent(
        bossId = "n12_boss",
        name = "El Titán de las Funciones",
        emoji = "🗿",
        intro = "Es enorme y está hecho de funciones que se llaman unas a otras. Para derribarlo, sigue cada llamada, cada parámetro y cada return sin perderte.",
        exercises = listOf(
            PredictOutput(
                prompt = "Primer golpe. ¿Qué muestra este programa?",
                code = "def eco(t):\n    return t + t\nprint(eco(\"ja\"))",
                acceptedAnswers = listOf("jaja"),
                correctFeedback = "¡Golpe! \"ja\" + \"ja\" = \"jaja\".",
                wrongFeedback = "t vale \"ja\". ¿Qué pasa al sumar un texto consigo mismo?"
            ),
            PredictOutput(
                prompt = "El Titán imprime Y devuelve. ¿Qué muestra? Escríbelo línea por línea.",
                code = "def f(x):\n    print(x)\n    return x * 2\ny = f(3)\nprint(y)",
                acceptedAnswers = listOf("3\n6"),
                correctFeedback = "¡Bien! Primero el print de adentro, luego el valor devuelto.",
                wrongFeedback = "La función muestra x y además devuelve el doble, que se guarda en y."
            ),
            FindBug(
                prompt = "Sale None en vez de 6. ¡Toca el error!",
                codeLines = listOf(
                    "def triple(n):",
                    "    resultado = n * 3",
                    "print(triple(2))"
                ),
                bugLineIndex = 1,
                correctFeedback = "¡Bug golpeado! Calcula, pero no devuelve nada. Debe ser return n * 3.",
                wrongFeedback = "¿Qué le falta a la función para entregar su resultado?"
            ),
            FillBlanks(
                prompt = "Completa el parámetro de la función.",
                codeTemplate = "def saludo(___):\n    return \"Hola, \" + nombre\nprint(saludo(\"Titán\"))",
                answers = listOf("nombre"),
                chips = listOf("nombre", "\"Titán\"", "saludo", "return"),
                correctFeedback = "¡Eso! El parámetro es el que se usa adentro.",
                wrongFeedback = "Mira qué variable usa el return dentro de la función."
            ),
            TypeCode(
                prompt = "Último golpe: escribe la primera línea de una función llamada area que recibe base y altura.",
                acceptedAnswers = listOf("def area(base, altura):"),
                placeholder = "def ...",
                correctFeedback = "¡Titán derribado! Ya sabes crear y usar funciones.",
                wrongFeedback = "def, el nombre y los dos parámetros separados por coma entre paréntesis, y los dos puntos."
            )
        )
    )

    val all: List<LessonContent> = listOf(
        def, llamar, parametros, variosParametros,
        devolver, printNoEsReturn, usarResultado, returnCorta,
        decidir, conBucles, descuentos
    )
}
