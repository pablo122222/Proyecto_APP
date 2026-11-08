package me.pablorincon.proyecto_app.domain.content

import me.pablorincon.proyecto_app.domain.BossContent
import me.pablorincon.proyecto_app.domain.FillBlanks
import me.pablorincon.proyecto_app.domain.FindBug
import me.pablorincon.proyecto_app.domain.LessonContent
import me.pablorincon.proyecto_app.domain.MultipleChoice
import me.pablorincon.proyecto_app.domain.OrderLines
import me.pablorincon.proyecto_app.domain.PredictOutput
import me.pablorincon.proyecto_app.domain.TypeCode

// Nivel 2 · Variables (11 lecciones y el jefe «El Guardián de las Cajas»), con Glitch como hilo conductor.
internal object Level02Lessons {

    private val laCajaConEtiqueta = LessonContent(
        lessonId = "n02_l01",
        title = "La caja con etiqueta 📦",
        hook = "Glitch tiene un problema: se le olvida todo. Le damos cajas con etiquetas para que guarde sus cosas.",
        explanation = "Una variable es una caja con nombre donde se guarda un dato. Se crea con =: el nombre va a la izquierda y el valor a la derecha.",
        code = "nombre = \"Glitch\"\nenergia = 100\nprint(nombre)\nprint(energia)",
        expectedOutput = "Glitch\n100",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "ciudad = \"Glitch City\"\nprint(\"ciudad\")\nprint(ciudad)",
                acceptedAnswers = listOf("ciudad\nGlitch City"),
                correctFeedback = "¡Bien visto! Con comillas es texto literal; sin comillas, Python busca la caja con ese nombre.",
                wrongFeedback = "Compara con lo que aprendiste: entre comillas es texto tal cual; sin comillas es el nombre de una caja."
            ),
            TypeCode(
                prompt = "Guarda el número 50 en una variable llamada bateria.",
                acceptedAnswers = listOf("bateria = 50"),
                placeholder = "nombre = valor",
                correctFeedback = "¡Sí! El nombre a la izquierda y el valor a la derecha.",
                wrongFeedback = "El = guarda lo de la derecha en la caja de la izquierda: primero el nombre, luego =, luego el valor."
            ),
            FillBlanks(
                prompt = "Crea una variable y luego muéstrala. ¿Qué nombre va en los dos huecos?",
                codeTemplate = "___ = \"Glitch\"\nprint(___)",
                answers = listOf("robot", "robot"),
                chips = listOf("robot", "robot", "Robot", "mi robot"),
                correctFeedback = "¡Correcto! El nombre tiene que ser exactamente el mismo en los dos lugares.",
                wrongFeedback = "Python distingue mayúsculas y los nombres no pueden llevar espacios. Debe ser idéntico al crear y al usar."
            )
        )
    )

    private val cambiandoLoQueHayDentro = LessonContent(
        lessonId = "n02_l02",
        title = "Cambiando lo que hay dentro 🔋",
        hook = "La batería de Glitch se gasta. Hay que actualizar la caja cada vez que baja.",
        explanation = "El valor de una variable puede cambiar. Python calcula primero lo de la derecha y después lo guarda.",
        code = "energia = 100\nenergia = energia - 30\nprint(energia)",
        expectedOutput = "70",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "energia = 100\nenergia = energia - 30\nenergia = energia - 30\nprint(energia)",
                acceptedAnswers = listOf("40"),
                correctFeedback = "¡Exacto! 100 → 70 → 40. Cada línea actualiza la caja.",
                wrongFeedback = "Sigue la caja paso a paso: empieza en 100, baja 30 y vuelve a bajar 30."
            ),
            OrderLines(
                prompt = "Ordena el programa para que muestre 65.",
                lines = listOf(
                    "energia = 100",
                    "energia = energia - 35",
                    "print(energia)"
                ),
                correctFeedback = "¡Perfecto! Primero se crea la caja, luego se actualiza y al final se muestra.",
                wrongFeedback = "Una variable debe existir antes de usarla, y hay que mostrarla después de actualizarla."
            ),
            TypeCode(
                prompt = "Glitch gasta 5 de energía. Escribe la línea que actualiza la variable energia.",
                acceptedAnswers = listOf("energia = energia - 5"),
                placeholder = "energia = ...",
                correctFeedback = "¡Sí! Calcula y guarda el resultado en la misma caja.",
                wrongFeedback = "Calcular no basta: hay que guardar el resultado con =. Usa energia en los dos lados."
            )
        )
    )

    private val nombresDeVariables = LessonContent(
        lessonId = "n02_l03",
        title = "Nombres de variables 🏷️",
        hook = "Glitch etiqueta sus cajas, pero no vale cualquier nombre. Hay reglas.",
        explanation = "Un nombre puede llevar letras, números y guion bajo (_), pero no empieza por número ni lleva espacios. Python distingue mayúsculas: Energia y energia son cajas distintas. Lo habitual es escribirlas en minúsculas con guion bajo (mi_robot).",
        code = "mi_robot = \"Glitch\"\nenergia_1 = 100\nprint(mi_robot, energia_1)",
        expectedOutput = "Glitch 100",
        exercises = listOf(
            FindBug(
                prompt = "Una de estas variables tiene un nombre que Python no acepta. ¡Cázala!",
                codeLines = listOf(
                    "robot_1 = \"Glitch\"",
                    "2robots = 5",
                    "mi_robot = \"Beep\""
                ),
                bugLineIndex = 1,
                correctFeedback = "¡Bug cazado! Un nombre no puede empezar por un número.",
                wrongFeedback = "Revisa cómo empieza cada nombre: hay una regla sobre el primer carácter."
            ),
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "Energia = 10\nenergia = 20\nprint(Energia, energia)",
                acceptedAnswers = listOf("10 20"),
                correctFeedback = "¡Exacto! Para Python, Energia y energia son dos cajas distintas.",
                wrongFeedback = "Mira la primera letra: una E y una e son letras distintas, así que son dos variables diferentes."
            ),
            MultipleChoice(
                prompt = "¿Cuál es el mejor nombre para guardar los puntos de un jugador?",
                options = listOf("puntos_jugador", "pj1x", "1puntos", "puntos jugador"),
                correctIndex = 0,
                correctFeedback = "¡Sí! Es válido y se entiende al leerlo.",
                wrongFeedback = "Un buen nombre es válido (sin espacios y sin empezar por número) y además dice qué guarda."
            )
        )
    )

    private val variablesEnPrint = LessonContent(
        lessonId = "n02_l04",
        title = "Variables dentro de print 🖨️",
        hook = "Glitch ya sabe guardar cosas. Ahora quiere mostrarlas, sin escribir el valor a mano.",
        explanation = "Dentro de print puedes poner variables sin comillas y separadas por comas: Python muestra el valor que guardan.",
        code = "nombre = \"Glitch\"\nedad = 3\nprint(\"Hola\", nombre)\nprint(nombre, \"tiene\", edad, \"años\")",
        expectedOutput = "Hola Glitch\nGlitch tiene 3 años",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "bateria = 80\nprint(\"Batería:\", bateria)\nprint(\"bateria\")",
                acceptedAnswers = listOf("Batería: 80\nbateria"),
                correctFeedback = "¡Bien visto! Sin comillas se muestra el valor; con comillas, la palabra.",
                wrongFeedback = "Fíjate en las comillas: bateria sin ellas es la caja (80) y con ellas es solo texto."
            ),
            FillBlanks(
                prompt = "Completa para mostrar: Hola, Ana",
                codeTemplate = "nombre = \"Ana\"\nprint(\"Hola,\", ___)",
                answers = listOf("nombre"),
                chips = listOf("nombre", "\"nombre\"", "Nombre", "nombres"),
                correctFeedback = "¡Correcto! La variable va sin comillas y con el nombre exacto.",
                wrongFeedback = "Hay que usar el nombre exacto de la variable, sin comillas ni cambios de mayúsculas o letras."
            ),
            TypeCode(
                prompt = "Hay una variable llamada puntos. Escribe un print que muestre: Puntos: y su valor, separados por coma.",
                acceptedAnswers = listOf("print(\"Puntos:\", puntos)"),
                placeholder = "print(...)",
                correctFeedback = "¡Eso! El texto entre comillas, una coma y la variable sin comillas.",
                wrongFeedback = "Primero el texto entre comillas, luego una coma y por último el nombre de la variable sin comillas."
            )
        )
    )

    private val copiarValores = LessonContent(
        lessonId = "n02_l05",
        title = "Copiar valores 🔁",
        hook = "Glitch copia lo que hay en una caja a otra. ¿Y si la primera cambia después?",
        explanation = "b = a copia el valor que tiene a en ese momento. Si después cambias a, b se queda como estaba.",
        code = "a = 10\nb = a\na = 99\nprint(a, b)",
        expectedOutput = "99 10",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "x = 5\ny = x\nx = x + 1\nprint(x, y)",
                acceptedAnswers = listOf("6 5"),
                correctFeedback = "¡Muy bien! y copió el 5 y no sigue a x, que subió a 6.",
                wrongFeedback = "y se queda con el valor que tenía x al copiar. Lo que pase con x después no lo afecta."
            ),
            OrderLines(
                prompt = "Ordena el programa para que muestre: 99 10",
                lines = listOf(
                    "bateria = 10",
                    "copia = bateria",
                    "bateria = 99",
                    "print(bateria, copia)"
                ),
                correctFeedback = "¡Perfecto! Primero se copia el 10 y luego bateria cambia a 99.",
                wrongFeedback = "La copia debe hacerse mientras bateria todavía vale 10, y antes de que cambie a 99."
            ),
            FillBlanks(
                prompt = "Glitch quiere que respaldo valga lo mismo que energia. ¿Qué símbolo falta?",
                codeTemplate = "energia = 70\nrespaldo ___ energia",
                answers = listOf("="),
                chips = listOf("=", "==", "->", "<-"),
                correctFeedback = "¡Sí! El = guarda el valor de la derecha en la variable de la izquierda.",
                wrongFeedback = "Para guardar un valor se usa un solo =. Con == se compara y -> o <- no existen en Python."
            )
        )
    )

    private val variasAlMismoTiempo = LessonContent(
        lessonId = "n02_l06",
        title = "Varias a la vez 🧺",
        hook = "Glitch es rápido: crea varias cajas en una sola línea.",
        explanation = "Puedes crear varias variables a la vez: x, y = 3, 4. Tiene que haber tantos valores como nombres, y se asignan en orden.",
        code = "x, y = 3, 4\nprint(x + y)\nprint(x * y)",
        expectedOutput = "7\n12",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "a, b = 6, 2\nprint(a - b)\nprint(a * b)",
                acceptedAnswers = listOf("4\n12"),
                correctFeedback = "¡Exacto! a vale 6 y b vale 2: 6 - 2 = 4 y 6 * 2 = 12.",
                wrongFeedback = "Los valores se asignan en orden: a recibe 6 y b recibe 2. Luego haz las dos cuentas."
            ),
            TypeCode(
                prompt = "Crea dos variables en una sola línea: ancho vale 5 y alto vale 3.",
                acceptedAnswers = listOf("ancho, alto = 5, 3"),
                placeholder = "nombre1, nombre2 = valor1, valor2",
                correctFeedback = "¡Sí! Dos nombres, un igual y dos valores en el mismo orden.",
                wrongFeedback = "Pon los dos nombres separados por coma, luego =, y después los dos valores en el mismo orden."
            ),
            FindBug(
                prompt = "Una de estas líneas da error porque no coincide la cantidad de valores. ¡Cázala!",
                codeLines = listOf(
                    "x, y = 1, 2",
                    "z, w = 5, 6, 7",
                    "print(x + y)"
                ),
                bugLineIndex = 1,
                correctFeedback = "¡Bug cazado! Dos nombres pero tres valores: tienen que ser la misma cantidad.",
                wrongFeedback = "Cuenta nombres y valores en cada línea: en una de ellas no coinciden."
            )
        )
    )

    private val variablesConCuentas = LessonContent(
        lessonId = "n02_l07",
        title = "Variables con cuentas 🧮",
        hook = "Glitch calcula el área de su habitación sin repetir números a mano.",
        explanation = "Las variables se pueden usar en cuentas, y el resultado se guarda en otra variable. Así el programa se adapta si cambias los datos.",
        code = "ancho = 4\nalto = 3\narea = ancho * alto\nprint(\"Área:\", area)",
        expectedOutput = "Área: 12",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "largo = 5\nancho = 2\nperimetro = 2 * (largo + ancho)\nprint(perimetro)",
                acceptedAnswers = listOf("14"),
                correctFeedback = "¡Correcto! Primero lo del paréntesis: 5 + 2 = 7, y luego 2 * 7 = 14.",
                wrongFeedback = "Resuelve primero lo que está entre paréntesis (largo + ancho) y luego multiplica por 2."
            ),
            FillBlanks(
                prompt = "Glitch compra 3 robots a 20 monedas cada uno. ¿Qué símbolo calcula el total?",
                codeTemplate = "precio = 20\ncantidad = 3\ntotal = precio ___ cantidad\nprint(total)",
                answers = listOf("*"),
                chips = listOf("*", "+", "-", "/"),
                correctFeedback = "¡Sí! 20 * 3 = 60: precio por cantidad.",
                wrongFeedback = "Piensa: varios robots al mismo precio se calculan multiplicando precio por cantidad."
            ),
            TypeCode(
                prompt = "Ya existen las variables ancho y alto. Escribe la línea que guarda su producto en una variable llamada area.",
                acceptedAnswers = listOf("area = ancho * alto", "area = alto * ancho"),
                placeholder = "area = ...",
                correctFeedback = "¡Eso! Una cuenta con variables y el resultado guardado en area.",
                wrongFeedback = "Usa las dos variables multiplicadas con * y guarda el resultado en area con =."
            )
        )
    )

    private val contadores = LessonContent(
        lessonId = "n02_l08",
        title = "Contadores 🔢",
        hook = "Glitch cuenta cada paso que da. Para eso necesita sumar de uno en uno a la misma caja.",
        explanation = "Sumar a una variable es tan común que tiene atajo: pasos += 1 equivale a pasos = pasos + 1. También existen -= y *=.",
        code = "pasos = 0\npasos += 1\npasos += 1\nprint(pasos)",
        expectedOutput = "2",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "puntos = 10\npuntos += 5\npuntos -= 3\nprint(puntos)",
                acceptedAnswers = listOf("12"),
                correctFeedback = "¡Exacto! 10 + 5 = 15 y 15 - 3 = 12.",
                wrongFeedback = "Ve línea por línea: += suma a la variable y -= le resta."
            ),
            FillBlanks(
                prompt = "Glitch encuentra 2 monedas más. ¿Qué operador falta para que muestre 7?",
                codeTemplate = "monedas = 5\nmonedas ___ 2\nprint(monedas)",
                answers = listOf("+="),
                chips = listOf("+=", "=+", "++", "=="),
                correctFeedback = "¡Sí! += suma 2 a lo que ya había en la caja.",
                wrongFeedback = "Python no tiene ++. Para sumar a lo que ya hay se usa += (el + va primero)."
            ),
            TypeCode(
                prompt = "Escribe la línea que resta 1 a la variable energia usando el atajo.",
                acceptedAnswers = listOf("energia -= 1"),
                placeholder = "energia ...",
                correctFeedback = "¡Eso! El atajo -= resta y guarda en un solo paso.",
                wrongFeedback = "El atajo para restar y guardar es -= (el - va primero), seguido del número."
            )
        )
    )

    private val intercambiar = LessonContent(
        lessonId = "n02_l09",
        title = "Intercambiar valores 🔀",
        hook = "Glitch lleva la gorra en una mano y la bota en la otra. ¿Cómo las intercambia sin soltar ninguna?",
        explanation = "Para intercambiar dos variables hace falta una caja de ayuda (temporal), o en Python una línea mágica: a, b = b, a.",
        code = "a = 1\nb = 2\na, b = b, a\nprint(a, b)",
        expectedOutput = "2 1",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "x = \"sol\"\ny = \"luna\"\nx, y = y, x\nprint(x, y)",
                acceptedAnswers = listOf("luna sol"),
                correctFeedback = "¡Exacto! Intercambiaron sus valores: x tiene luna e y tiene sol.",
                wrongFeedback = "x, y = y, x deja en x lo que tenía y, y en y lo que tenía x."
            ),
            OrderLines(
                prompt = "a y b ya tienen valor. Ordena estas 3 líneas para intercambiarlos usando una caja temporal.",
                lines = listOf(
                    "temp = a",
                    "a = b",
                    "b = temp"
                ),
                correctFeedback = "¡Perfecto! Guardas a, le das a el valor de b y luego b recibe lo guardado.",
                wrongFeedback = "Si pisas una variable antes de guardarla, pierdes su valor. Primero guarda en temp."
            ),
            TypeCode(
                prompt = "Intercambia los valores de x e y en una sola línea.",
                acceptedAnswers = listOf("x, y = y, x"),
                placeholder = "x, y = ...",
                correctFeedback = "¡Eso! Python intercambia sin necesitar la caja temporal.",
                wrongFeedback = "A la izquierda x, y; a la derecha los mismos nombres pero en orden contrario."
            )
        )
    )

    private val miniPrograma = LessonContent(
        lessonId = "n02_l10",
        title = "Mini programa: la tiendita 🛒",
        hook = "Glitch abre una tiendita de pilas. Con variables puede cambiar precios y cantidades sin reescribir todo.",
        explanation = "Un programa con variables es fácil de cambiar: modificas un dato al principio y todo lo demás se recalcula.",
        code = "producto = \"Pila\"\nprecio = 8\ncantidad = 3\ntotal = precio * cantidad\nprint(producto, \"x\", cantidad, \"=\", total)",
        expectedOutput = "Pila x 3 = 24",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "puntos = 10\nbonus = 5\npuntos = puntos + bonus\nbonus = 0\nprint(puntos, bonus)",
                acceptedAnswers = listOf("15 0"),
                correctFeedback = "¡Exacto! puntos sumó el bonus (15) y después bonus cambió a 0.",
                wrongFeedback = "Sigue las líneas en orden: primero puntos suma bonus, y solo después bonus se pone en 0."
            ),
            FindBug(
                prompt = "Este programa falla al mostrar el total. ¿En qué línea está el bug?",
                codeLines = listOf(
                    "precio = 8",
                    "cantidad = 3",
                    "total = precio * cantidad",
                    "print(\"Total:\", totl)"
                ),
                bugLineIndex = 3,
                correctFeedback = "¡Bug cazado! totl no existe: la variable se llama total.",
                wrongFeedback = "Compara los nombres con los que se crearon. En una línea hay uno escrito distinto."
            ),
            TypeCode(
                prompt = "Existen las variables producto y total. Escribe un print que muestre las dos separadas por coma.",
                acceptedAnswers = listOf("print(producto, total)"),
                placeholder = "print(...)",
                correctFeedback = "¡Eso! Dos variables en un print, separadas por coma y sin comillas.",
                wrongFeedback = "Pon las dos variables, sin comillas y separadas por una coma, dentro del print."
            )
        )
    )

    private val erroresConVariables = LessonContent(
        lessonId = "n02_l11",
        title = "Cuando una variable falla 🐞",
        hook = "Las variables también dan guerra: usarlas antes de crearlas o escribirlas distinto. Glitch aprende a cazarlas.",
        explanation = "Si usas un nombre que no existe, Python lanza un NameError. Suele pasar por usarla antes de crearla, por una errata o por cambiar mayúsculas.",
        code = "print(energia)\nenergia = 100",
        expectedOutput = "NameError: name 'energia' is not defined",
        exercises = listOf(
            FindBug(
                prompt = "Python dice NameError: name 'Robot' is not defined. ¿En qué línea?",
                codeLines = listOf(
                    "robot = \"Glitch\"",
                    "print(Robot)",
                    "print(robot)"
                ),
                bugLineIndex = 1,
                correctFeedback = "¡Bug cazado! Robot con mayúscula no es la misma caja que robot.",
                wrongFeedback = "El mensaje dice qué nombre no se encuentra: Robot. Busca dónde se usa escrito así."
            ),
            OrderLines(
                prompt = "Arregla el orden para que el programa funcione.",
                lines = listOf(
                    "energia = 100",
                    "print(energia)"
                ),
                correctFeedback = "¡Arreglado! La variable existe antes de usarla.",
                wrongFeedback = "Una variable solo se puede usar después de crearla. ¿Cuál de las dos líneas debe ir primero?"
            ),
            PredictOutput(
                prompt = "Python lanza un error con este código. Escribe solo el nombre del tipo de error.",
                code = "pila = 5\nprint(pilas)",
                acceptedAnswers = listOf("NameError"),
                correctFeedback = "¡Exacto! pilas no existe: se creó pila, sin la s final.",
                wrongFeedback = "Cuando un nombre no existe, el error se llama igual que su causa: el nombre (Name) que no se encuentra."
            )
        )
    )

    // Jefe del nivel: mezcla lo aprendido en las 11 lecciones.
    val boss = BossContent(
        bossId = "n02_boss",
        name = "El Guardián de las Cajas",
        emoji = "📦",
        intro = "Guarda todas las cajas de Glitch City y solo las abre a quien sepa darles nombre. ¡Demuéstrale que dominas las variables!",
        exercises = listOf(
            PredictOutput(
                prompt = "El Guardián te lanza este código. ¿Qué muestra?",
                code = "oro = 10\ngemas = oro\noro = oro + 5\nprint(oro, gemas)",
                acceptedAnswers = listOf("15 10"),
                correctFeedback = "¡Golpe certero! gemas copió el 10 y oro subió a 15.",
                wrongFeedback = "Recuerda que copiar guarda el valor de ese momento: gemas no sigue a oro."
            ),
            FindBug(
                prompt = "Una de estas cajas tiene un nombre inválido. ¡Tócala!",
                codeLines = listOf(
                    "mi_caja = 5",
                    "otra caja = 7",
                    "print(mi_caja)"
                ),
                bugLineIndex = 1,
                correctFeedback = "¡Bug golpeado! Los nombres no pueden llevar espacios.",
                wrongFeedback = "Revisa cada nombre: uno rompe una de las reglas de los nombres de variables."
            ),
            TypeCode(
                prompt = "Guarda el texto Glitch en una variable llamada nombre.",
                acceptedAnswers = listOf("nombre = \"Glitch\""),
                placeholder = "nombre = ...",
                correctFeedback = "¡Bien hecho! Nombre a la izquierda y el texto entre comillas a la derecha.",
                wrongFeedback = "El texto va entre comillas y se guarda con =: nombre, igual, \"Glitch\"."
            ),
            FillBlanks(
                prompt = "Intercambia a y b en una sola línea. ¿Qué va en los huecos?",
                codeTemplate = "a = 1\nb = 2\na, b = ___, ___\nprint(a, b)",
                answers = listOf("b", "a"),
                chips = listOf("a", "b", "c", "1"),
                correctFeedback = "¡Intercambio perfecto! Ahora a vale 2 y b vale 1.",
                wrongFeedback = "A la derecha van los mismos nombres de la izquierda, pero en orden contrario."
            ),
            PredictOutput(
                prompt = "Último ataque del Guardián. ¿Qué muestra este código?",
                code = "pasos = 0\npasos += 4\npasos -= 1\npasos = pasos * 2\nprint(pasos)",
                acceptedAnswers = listOf("6"),
                correctFeedback = "¡Guardián vencido! 0 → 4 → 3 → 6.",
                wrongFeedback = "Ve paso a paso: empieza en 0, suma 4, resta 1 y por último multiplica por 2."
            )
        )
    )

    val all: List<LessonContent> = listOf(
        laCajaConEtiqueta, cambiandoLoQueHayDentro, nombresDeVariables, variablesEnPrint,
        copiarValores, variasAlMismoTiempo, variablesConCuentas, contadores,
        intercambiar, miniPrograma, erroresConVariables
    )
}
