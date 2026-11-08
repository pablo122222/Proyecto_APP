package me.pablorincon.proyecto_app.domain.content

import me.pablorincon.proyecto_app.domain.BossContent
import me.pablorincon.proyecto_app.domain.FillBlanks
import me.pablorincon.proyecto_app.domain.FindBug
import me.pablorincon.proyecto_app.domain.LessonContent
import me.pablorincon.proyecto_app.domain.MultipleChoice
import me.pablorincon.proyecto_app.domain.OrderLines
import me.pablorincon.proyecto_app.domain.PredictOutput
import me.pablorincon.proyecto_app.domain.TypeCode

// Nivel 6 · Condicionales II (11 lecciones y el jefe «El Minotauro del Laberinto»), con Glitch como hilo conductor.
internal object Level06Lessons {

    private val masDeDosCaminos = LessonContent(
        lessonId = "n06_l01",
        title = "Más de dos caminos 🛤️",
        hook = "El clima de Glitch City cambia: frío, agradable o calor. Con un solo if/else no alcanza. Hace falta elif.",
        explanation = "elif añade caminos intermedios. Python revisa de arriba abajo y ejecuta solo el primero cuya condición sea verdadera. Aquí sale «Hace calor» porque 25 no es menor que 25.",
        code = "temperatura = 25\nif temperatura < 10:\n    print(\"Hace frío\")\nelif temperatura < 25:\n    print(\"Está agradable\")\nelse:\n    print(\"Hace calor\")",
        expectedOutput = "Hace calor",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "temperatura = 10\nif temperatura < 10:\n    print(\"Hace frío\")\nelif temperatura < 25:\n    print(\"Está agradable\")\nelse:\n    print(\"Hace calor\")",
                acceptedAnswers = listOf("Está agradable"),
                correctFeedback = "¡Eso! 10 < 10 es falso, y 10 < 25 es verdadero.",
                wrongFeedback = "Revisa en orden: ¿10 < 10? No. ¿10 < 25? Sí, y ahí se detiene."
            ),
            OrderLines(
                prompt = "Ordena el programa que muestra la letra de una nota (nota = 85).",
                lines = listOf(
                    "nota = 85",
                    "if nota >= 90:",
                    "    print(\"A\")",
                    "elif nota >= 80:",
                    "    print(\"B\")",
                    "else:",
                    "    print(\"C\")"
                ),
                correctFeedback = "¡Perfecto! if, luego elif y el else siempre al final.",
                wrongFeedback = "La cadena empieza con if, sigue con elif y termina con else. Cada condición va con su bloque debajo."
            ),
            FillBlanks(
                prompt = "Completa la palabra que añade un camino intermedio.",
                codeTemplate = "if t < 10:\n    print(\"Frío\")\n___ t < 25:\n    print(\"Agradable\")\nelse:\n    print(\"Calor\")",
                answers = listOf("elif"),
                chips = listOf("elif", "else if", "elseif", "if"),
                correctFeedback = "¡Sí! elif es la unión de else e if, escrita junta.",
                wrongFeedback = "En Python se escribe todo junto y sin espacio: una sola palabra de cuatro letras."
            )
        )
    )

    private val elPrimeroGana = LessonContent(
        lessonId = "n06_l02",
        title = "El primero que se cumple gana 🥇",
        hook = "Glitch repartió las medallas al revés: nadie recibió Oro. El orden de las condiciones importa.",
        explanation = "En una cadena if/elif, Python se detiene en la primera condición verdadera. Por eso lo más específico (como más de 90) va antes que lo más general (más de 50).",
        code = "puntos = 95\nif puntos > 50:\n    print(\"Plata\")\nelif puntos > 90:\n    print(\"Oro\")",
        expectedOutput = "Plata",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "x = 95\nif x > 50:\n    print(\"Plata\")\nelif x > 90:\n    print(\"Oro\")\nelse:\n    print(\"Nada\")",
                acceptedAnswers = listOf("Plata"),
                correctFeedback = "¡Exacto! 95 > 50 se cumple primero y Python ya no mira las demás.",
                wrongFeedback = "Revisa de arriba abajo: la primera condición ya es verdadera, así que no se evalúa el resto."
            ),
            FindBug(
                prompt = "Glitch quería dar Oro a los que pasen de 90, pero nunca lo da. Toca la condición demasiado general que tapa a la otra.",
                codeLines = listOf(
                    "if puntos > 50:",
                    "    print(\"Plata\")",
                    "elif puntos > 90:",
                    "    print(\"Oro\")"
                ),
                bugLineIndex = 0,
                correctFeedback = "¡Bug cazado! Todo el que pasa de 90 también pasa de 50: la condición más específica debe ir primero.",
                wrongFeedback = "Pregúntate: si puntos vale 95, ¿cuál condición se cumple primero y tapa a las demás?"
            ),
            TypeCode(
                prompt = "Escribe la línea elif que comprueba si puntos es mayor que 70.",
                acceptedAnswers = listOf("elif puntos > 70:"),
                placeholder = "elif ...",
                correctFeedback = "¡Eso! elif, la condición y los dos puntos.",
                wrongFeedback = "Empieza con elif, sigue la condición puntos > 70 y termina con dos puntos."
            )
        )
    )

    private val operadorAnd = LessonContent(
        lessonId = "n06_l03",
        title = "Dos condiciones a la vez: and 🤝",
        hook = "El portero necesita dos pistas: ser mayor de edad y traer entrada. Con and, hacen falta las dos.",
        explanation = "and es verdadero solo si las dos condiciones lo son. Si una falla, todo falla.",
        code = "edad = 20\ntiene_entrada = True\nif edad >= 18 and tiene_entrada:\n    print(\"Pasa\")\nelse:\n    print(\"No pasa\")",
        expectedOutput = "Pasa",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "a = 5\nb = 12\nprint(a > 3 and b > 10)\nprint(a > 3 and b > 20)",
                acceptedAnswers = listOf("True\nFalse"),
                correctFeedback = "¡Exacto! En la primera se cumplen las dos; en la segunda, b > 20 falla.",
                wrongFeedback = "Con and, las dos partes deben ser verdaderas. Revisa cada una por separado."
            ),
            FillBlanks(
                prompt = "Completa para que pasen solo los mayores de edad que además traen entrada.",
                codeTemplate = "if edad >= 18 ___ tiene_entrada:\n    print(\"Pasa\")",
                answers = listOf("and"),
                chips = listOf("and", "or", "not", "&&"),
                correctFeedback = "¡Sí! and exige las dos condiciones a la vez.",
                wrongFeedback = "«Y además» se escribe con una palabra en inglés de tres letras; && no existe en Python."
            ),
            TypeCode(
                prompt = "Escribe la línea del if que comprueba que x sea mayor que 0 y menor que 10.",
                acceptedAnswers = listOf("if x > 0 and x < 10:"),
                placeholder = "if ...",
                correctFeedback = "¡Eso! Dos comparaciones unidas con and.",
                wrongFeedback = "Escribe if x > 0 and x < 10: con las dos comparaciones completas, cada una con su x."
            )
        )
    )

    private val operadorOr = LessonContent(
        lessonId = "n06_l04",
        title = "Una u otra: or 🔀",
        hook = "El parque abre sábados y domingos. Basta con que se cumpla una de las dos opciones.",
        explanation = "or es verdadero si al menos una de las condiciones lo es. Solo es falso cuando todas fallan.",
        code = "dia = \"sábado\"\nif dia == \"sábado\" or dia == \"domingo\":\n    print(\"Fin de semana\")\nelse:\n    print(\"Día de trabajo\")",
        expectedOutput = "Fin de semana",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "print(5 > 10 or 3 > 1)\nprint(5 > 10 or 3 > 8)\nprint(1 == 1 or 1 == 2)",
                acceptedAnswers = listOf("True\nFalse\nTrue"),
                correctFeedback = "¡Muy bien! Con or basta que una se cumpla; solo falla si fallan las dos.",
                wrongFeedback = "Revisa cada línea: or es falso únicamente cuando las dos partes son falsas."
            ),
            FindBug(
                prompt = "Glitch quería comprobar si es lunes o martes, pero el if siempre se cumple. ¿Qué línea tiene el bug?",
                codeLines = listOf(
                    "dia = \"miércoles\"",
                    "if dia == \"lunes\" or \"martes\":",
                    "    print(\"Empieza la semana\")"
                ),
                bugLineIndex = 1,
                correctFeedback = "¡Bug cazado! Después de or hay que repetir la comparación completa: dia == \"martes\".",
                wrongFeedback = "A cada lado del or debe haber una condición completa. Un texto suelto siempre cuenta como verdadero."
            ),
            TypeCode(
                prompt = "Escribe la línea del if que comprueba si x es 1 o x es 2.",
                acceptedAnswers = listOf("if x == 1 or x == 2:"),
                placeholder = "if ...",
                correctFeedback = "¡Eso! Dos comparaciones completas unidas con or.",
                wrongFeedback = "Repite x en cada comparación: x == 1 or x == 2, y termina con dos puntos."
            )
        )
    )

    private val operadorNot = LessonContent(
        lessonId = "n06_l05",
        title = "Al revés: not 🔄",
        hook = "Si no llueve, Glitch sale al parque. A veces es más fácil preguntar por lo contrario.",
        explanation = "not invierte un valor: not True es False y not False es True.",
        code = "llueve = False\nif not llueve:\n    print(\"Salimos al parque\")\nelse:\n    print(\"Nos quedamos\")",
        expectedOutput = "Salimos al parque",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "print(not True)\nprint(not (3 > 5))\nprint(not False and True)",
                acceptedAnswers = listOf("False\nTrue\nTrue"),
                correctFeedback = "¡Exacto! not True = False, not (False) = True y not False and True = True.",
                wrongFeedback = "not da vuelta el valor que viene después. En la tercera, primero se aplica not False."
            ),
            FillBlanks(
                prompt = "Completa para que se cumpla cuando NO llueve.",
                codeTemplate = "if ___ llueve:\n    print(\"Salimos\")",
                answers = listOf("not"),
                chips = listOf("not", "no", "!", "except"),
                correctFeedback = "¡Sí! not invierte el valor de llueve.",
                wrongFeedback = "La negación en Python es una palabra en inglés de tres letras; el ! no se usa así."
            ),
            MultipleChoice(
                prompt = "¿Cuál condición es equivalente a not (x > 5)?",
                options = listOf("x <= 5", "x < 5", "x >= 5", "x != 5"),
                correctIndex = 0,
                correctFeedback = "¡Sí! Lo contrario de «mayor que 5» es «menor o igual que 5».",
                wrongFeedback = "Piensa en x = 5: not (5 > 5) es True, así que la condición equivalente también debe incluir al 5."
            )
        )
    )

    private val rangos = LessonContent(
        lessonId = "n06_l06",
        title = "Rangos y comparaciones encadenadas 📏",
        hook = "Para saber si un número está entre dos valores, Python tiene un atajo que se lee como en matemáticas.",
        explanation = "Puedes encadenar comparaciones: 1 <= x <= 10 equivale a x >= 1 and x <= 10. Ojo con incluir o no los extremos.",
        code = "x = 7\nif x >= 1 and x <= 10:\n    print(\"Entre 1 y 10\")\nprint(1 <= x <= 10)",
        expectedOutput = "Entre 1 y 10\nTrue",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "x = 15\nprint(1 <= x <= 10)\nprint(10 <= x <= 20)\nprint(0 < x < 15)",
                acceptedAnswers = listOf("False\nTrue\nFalse"),
                correctFeedback = "¡Exacto! 15 no está entre 1 y 10, sí entre 10 y 20, y no es menor que 15.",
                wrongFeedback = "Con x = 15 revisa cada rango. Fíjate en que 0 < x < 15 deja fuera al 15."
            ),
            TypeCode(
                prompt = "Escribe solo la condición (sin if) que sea verdadera cuando edad esté entre 12 y 17, ambos incluidos, en una comparación encadenada.",
                acceptedAnswers = listOf("12 <= edad <= 17"),
                placeholder = "... <= edad <= ...",
                correctFeedback = "¡Eso! Una sola línea que se lee como en matemáticas.",
                wrongFeedback = "Pon 12, el operador <=, la variable edad, otro <= y 17, en ese orden."
            ),
            MultipleChoice(
                prompt = "¿Cuál condición es verdadera solo si x está entre 1 y 10, ambos incluidos?",
                options = listOf(
                    "x >= 1 or x <= 10",
                    "x > 1 and x < 10",
                    "x >= 1 and x <= 10",
                    "x <= 1 and x >= 10"
                ),
                correctIndex = 2,
                correctFeedback = "¡Muy bien! Las dos condiciones a la vez, con los extremos incluidos.",
                wrongFeedback = "Piensa en 1 y en 10: deben entrar. Y las dos condiciones se tienen que cumplir juntas."
            )
        )
    )

    private val operadorIn = LessonContent(
        lessonId = "n06_l07",
        title = "¿Está dentro? in 📦",
        hook = "Glitch quiere saber si una letra es vocal, sin escribir cinco comparaciones seguidas.",
        explanation = "in comprueba si un texto está dentro de otro: \"o\" in \"hola\" es True. Distingue mayúsculas y minúsculas.",
        code = "letra = \"o\"\nif letra in \"aeiou\":\n    print(\"Es vocal\")\nprint(\"z\" in \"hola\")",
        expectedOutput = "Es vocal\nFalse",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "print(\"o\" in \"hola\")\nprint(\"z\" in \"hola\")\nprint(\"ol\" in \"hola\")\nprint(\"OL\" in \"hola\")",
                acceptedAnswers = listOf("True\nFalse\nTrue\nFalse"),
                correctFeedback = "¡Excelente! \"ol\" aparece seguido en hola, pero \"OL\" en mayúsculas no.",
                wrongFeedback = "Busca cada texto dentro de \"hola\", teniendo cuidado con las mayúsculas."
            ),
            FillBlanks(
                prompt = "Completa para comprobar si letra es una vocal.",
                codeTemplate = "if letra ___ \"aeiou\":\n    print(\"Vocal\")",
                answers = listOf("in"),
                chips = listOf("in", "is", "==", "has"),
                correctFeedback = "¡Sí! in pregunta si está dentro.",
                wrongFeedback = "Se necesita el operador de pertenencia: una palabra de dos letras en inglés."
            ),
            TypeCode(
                prompt = "Escribe la línea del if que comprueba si letra está dentro del texto \"abc\".",
                acceptedAnswers = listOf("if letra in \"abc\":"),
                placeholder = "if ...",
                correctFeedback = "¡Eso! if, letra in y el texto entre comillas.",
                wrongFeedback = "Usa if letra in \"abc\": con el texto entre comillas y los dos puntos al final."
            )
        )
    )

    private val precedencia = LessonContent(
        lessonId = "n06_l08",
        title = "Quién va primero: not, and, or 🥊",
        hook = "Cuando Glitch mezcla and y or sin paréntesis, Python sigue un orden preciso.",
        explanation = "Orden: primero not, luego and y al final or. Con paréntesis decides tú el orden y haces el código más claro.",
        code = "print(True or False and False)\nprint((True or False) and False)\nprint(not True or True)",
        expectedOutput = "True\nFalse\nTrue",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "print(False and True or True)\nprint(False and (True or True))\nprint(not False and not False)",
                acceptedAnswers = listOf("True\nFalse\nTrue"),
                correctFeedback = "¡Muy bien! Primero and y luego or, y los paréntesis cambian el orden.",
                wrongFeedback = "Resuelve and antes que or, lo de los paréntesis primero, y no antes que ambos."
            ),
            FillBlanks(
                prompt = "Quieres que primero se evalúe el or, antes del and. ¿Qué símbolos faltan?",
                codeTemplate = "print(___True or False___ and False)",
                answers = listOf("(", ")"),
                chips = listOf("(", ")", "[", "]"),
                correctFeedback = "¡Sí! Los paréntesis fuerzan que el or se resuelva primero.",
                wrongFeedback = "Se usan paréntesis redondos: uno antes de True y otro después de False."
            ),
            MultipleChoice(
                prompt = "En la expresión a or b and c, ¿qué se evalúa primero?",
                options = listOf("b and c", "a or b", "Todo a la vez", "De derecha a izquierda siempre"),
                correctIndex = 0,
                correctFeedback = "¡Sí! and tiene más prioridad que or.",
                wrongFeedback = "Recuerda el orden: not, luego and, luego or."
            )
        )
    )

    private val variablesBooleanas = LessonContent(
        lessonId = "n06_l09",
        title = "Variables verdadero/falso ✅",
        hook = "Glitch guarda respuestas de sí o no en variables. ¿Cómo las usa en un if sin escribir == True?",
        explanation = "Si una variable ya guarda True o False, se usa directamente: if listo:. Para lo contrario, if not listo:.",
        code = "listo = True\nllueve = False\nif listo:\n    print(\"Vamos\")\nif not llueve:\n    print(\"Sin paraguas\")",
        expectedOutput = "Vamos\nSin paraguas",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "a = True\nb = False\nif a and not b:\n    print(\"X\")\nif b or not a:\n    print(\"Y\")\nif a or b:\n    print(\"Z\")",
                acceptedAnswers = listOf("X\nZ"),
                correctFeedback = "¡Exacto! X y Z se cumplen; Y no, porque b es falso y not a también.",
                wrongFeedback = "Evalúa cada if por separado con a = True y b = False."
            ),
            FillBlanks(
                prompt = "Glitch guarda tiene_llave = True. ¿Cuál es la forma correcta de preguntar si la tiene?",
                codeTemplate = "if ___:\n    print(\"Abre la puerta\")",
                answers = listOf("tiene_llave"),
                chips = listOf("tiene_llave", "\"tiene_llave\"", "tiene_llave = True", "no tiene_llave"),
                correctFeedback = "¡Sí! Una variable booleana ya es una condición por sí sola.",
                wrongFeedback = "Con comillas sería un texto, con = sería una asignación y «no» no existe. Solo el nombre vale."
            ),
            TypeCode(
                prompt = "Escribe un if que se cumpla cuando la variable listo es falsa (usando not).",
                acceptedAnswers = listOf("if not listo:"),
                placeholder = "if ...",
                correctFeedback = "¡Eso! if not seguido de la variable y los dos puntos.",
                wrongFeedback = "Usa if, la palabra not, el nombre de la variable y termina con dos puntos."
            )
        )
    )

    private val menuDeOpciones = LessonContent(
        lessonId = "n06_l10",
        title = "Menú de opciones 🍽️",
        hook = "Glitch construye un menú: el usuario elige una opción y el programa responde distinto en cada caso.",
        explanation = "Un menú se arma con if/elif/else sobre lo que escribe el usuario. Recuerda que input() devuelve texto: compara con \"1\", no con 1. La salida de abajo es la que verías si escribes 2.",
        code = "opcion = input(\"1) Jugar  2) Salir: \")\nif opcion == \"1\":\n    print(\"¡A jugar!\")\nelif opcion == \"2\":\n    print(\"Adiós\")\nelse:\n    print(\"Opción no válida\")",
        expectedOutput = "1) Jugar  2) Salir: 2\nAdiós",
        exercises = listOf(
            PredictOutput(
                prompt = "El usuario escribe 3. ¿Qué muestra el programa (solo el print)?",
                code = "opcion = input(\"1) Jugar  2) Salir: \")\nif opcion == \"1\":\n    print(\"¡A jugar!\")\nelif opcion == \"2\":\n    print(\"Adiós\")\nelse:\n    print(\"Opción no válida\")",
                acceptedAnswers = listOf("Opción no válida"),
                correctFeedback = "¡Exacto! 3 no es ni 1 ni 2, así que va el else.",
                wrongFeedback = "Compara el 3 con las dos opciones: ninguna coincide, así que se ejecuta el else."
            ),
            FindBug(
                prompt = "Este menú nunca reconoce la opción 1 aunque el usuario la escriba. ¿Qué línea tiene el bug?",
                codeLines = listOf(
                    "opcion = input(\"1/2: \")",
                    "if opcion == 1:",
                    "    print(\"Uno\")"
                ),
                bugLineIndex = 1,
                correctFeedback = "¡Bug cazado! input devuelve texto: hay que comparar con \"1\" (entre comillas).",
                wrongFeedback = "input siempre devuelve texto, y un texto \"1\" nunca es igual al número 1."
            ),
            OrderLines(
                prompt = "Ordena el menú de opciones.",
                lines = listOf(
                    "opcion = input(\"Elige: \")",
                    "if opcion == \"a\":",
                    "    print(\"Opción A\")",
                    "elif opcion == \"b\":",
                    "    print(\"Opción B\")",
                    "else:",
                    "    print(\"Ninguna\")"
                ),
                correctFeedback = "¡Menú listo! Pregunta, if, elif y else.",
                wrongFeedback = "Primero se lee la opción; luego if, elif y por último else, cada uno con su print."
            )
        )
    )

    private val notasConLetras = LessonContent(
        lessonId = "n06_l11",
        title = "Notas con letras 🏅",
        hook = "Glitch es profesor y convierte cada nota numérica en una letra. ¡Es el programa del nivel!",
        explanation = "Usa una cadena if/elif/else de mayor a menor para clasificar en rangos. La salida de abajo es la que verías si escribes 85.",
        code = "nota = int(input(\"Nota: \"))\nif nota >= 90:\n    print(\"A\")\nelif nota >= 80:\n    print(\"B\")\nelif nota >= 70:\n    print(\"C\")\nelse:\n    print(\"D\")",
        expectedOutput = "Nota: 85\nB",
        exercises = listOf(
            PredictOutput(
                prompt = "El usuario escribe 70. ¿Qué letra muestra el programa?",
                code = "nota = int(input(\"Nota: \"))\nif nota >= 90:\n    print(\"A\")\nelif nota >= 80:\n    print(\"B\")\nelif nota >= 70:\n    print(\"C\")\nelse:\n    print(\"D\")",
                acceptedAnswers = listOf("C"),
                correctFeedback = "¡Exacto! 70 >= 70 se cumple en la tercera condición.",
                wrongFeedback = "Revisa en orden: 70 no es >= 90, no es >= 80, pero sí es >= 70."
            ),
            PredictOutput(
                prompt = "Ahora el usuario escribe 69. ¿Qué letra muestra el programa?",
                code = "nota = int(input(\"Nota: \"))\nif nota >= 90:\n    print(\"A\")\nelif nota >= 80:\n    print(\"B\")\nelif nota >= 70:\n    print(\"C\")\nelse:\n    print(\"D\")",
                acceptedAnswers = listOf("D"),
                correctFeedback = "¡Correcto! 69 no cumple ninguna condición, así que va el else.",
                wrongFeedback = "69 no llega a 70, 80 ni 90: se ejecuta el else."
            ),
            TypeCode(
                prompt = "Escribe la línea elif que comprueba si nota es al menos 80.",
                acceptedAnswers = listOf("elif nota >= 80:"),
                placeholder = "elif ...",
                correctFeedback = "¡Eso! «Al menos 80» se escribe con >=.",
                wrongFeedback = "«Al menos» significa mayor o igual: elif nota >= 80:"
            )
        )
    )

    // Jefe del nivel: mezcla lo aprendido en las 11 lecciones.
    val boss = BossContent(
        bossId = "n06_boss",
        name = "El Minotauro del Laberinto",
        emoji = "🐂",
        intro = "Guarda un laberinto con mil caminos y solo deja salir a quien elige bien con elif, and, or y not.",
        exercises = listOf(
            PredictOutput(
                prompt = "Primera bifurcación. ¿Qué muestra este programa?",
                code = "t = 10\nif t < 10:\n    print(\"Frío\")\nelif t < 25:\n    print(\"Templado\")\nelse:\n    print(\"Calor\")",
                acceptedAnswers = listOf("Templado"),
                correctFeedback = "¡Camino correcto! 10 < 10 es falso y 10 < 25 es verdadero.",
                wrongFeedback = "Revisa en orden y detente en la primera condición verdadera."
            ),
            PredictOutput(
                prompt = "Segunda bifurcación. ¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "print(not (2 > 3) and 5 == 5)\nprint(True and False or True)\nprint(1 < 5 < 3)",
                acceptedAnswers = listOf("True\nTrue\nFalse"),
                correctFeedback = "¡Muy bien! Resolviste not, and, or y una comparación encadenada.",
                wrongFeedback = "Recuerda: primero not, luego and, luego or. Y 1 < 5 < 3 exige que ambas comparaciones sean ciertas."
            ),
            FindBug(
                prompt = "El Minotauro escondió un error en este if. ¡Tócalo!",
                codeLines = listOf(
                    "dia = \"lunes\"",
                    "if dia == \"lunes\" or \"martes\":",
                    "    print(\"Empieza\")"
                ),
                bugLineIndex = 1,
                correctFeedback = "¡Bug golpeado! A los dos lados del or van comparaciones completas.",
                wrongFeedback = "Un texto suelto siempre cuenta como verdadero. Revisa la línea del if."
            ),
            FillBlanks(
                prompt = "Completa para que 17 también entre en el rango de adolescentes.",
                codeTemplate = "edad = 15\nif 12 <= edad ___ 17:\n    print(\"Adolescente\")",
                answers = listOf("<="),
                chips = listOf("<=", "<", ">=", "=="),
                correctFeedback = "¡Camino abierto! <= incluye el extremo 17.",
                wrongFeedback = "Para incluir el extremo hace falta «menor o igual»."
            ),
            TypeCode(
                prompt = "Última puerta: escribe la línea del if que comprueba que x es mayor que 0 y además par (resto 0 al dividir entre 2).",
                acceptedAnswers = listOf("if x > 0 and x % 2 == 0:"),
                placeholder = "if ...",
                correctFeedback = "¡Minotauro vencido! Dos condiciones unidas con and.",
                wrongFeedback = "Escribe if x > 0 and x % 2 == 0: con las dos condiciones completas."
            )
        )
    )

    val all: List<LessonContent> = listOf(
        masDeDosCaminos, elPrimeroGana, operadorAnd, operadorOr,
        operadorNot, rangos, operadorIn, precedencia,
        variablesBooleanas, menuDeOpciones, notasConLetras
    )
}
