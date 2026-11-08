package me.pablorincon.proyecto_app.domain.content

import me.pablorincon.proyecto_app.domain.BossContent
import me.pablorincon.proyecto_app.domain.FillBlanks
import me.pablorincon.proyecto_app.domain.FindBug
import me.pablorincon.proyecto_app.domain.LessonContent
import me.pablorincon.proyecto_app.domain.MultipleChoice
import me.pablorincon.proyecto_app.domain.OrderLines
import me.pablorincon.proyecto_app.domain.PredictOutput
import me.pablorincon.proyecto_app.domain.TypeCode

// Nivel 5 · Condicionales I (11 lecciones y el jefe «La Esfinge de las Decisiones»), con Glitch como hilo conductor.
internal object Level05Lessons {

    private val elPorteroDeGlitchCity = LessonContent(
        lessonId = "n05_l01",
        title = "El portero de Glitch City 🚦",
        hook = "Glitch ahora trabaja de portero. Mira a cada visitante y decide: ¿entra o no entra? Eso es un condicional.",
        explanation = "Un condicional permite que un programa tome decisiones según si una condición es verdadera o falsa. Lo que va indentado bajo if o else es lo que se ejecuta en cada caso.",
        code = "edad = 20\nif edad >= 18:\n    print(\"Eres mayor de edad\")\nelse:\n    print(\"Eres menor de edad\")",
        expectedOutput = "Eres mayor de edad",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "edad = 18\nif edad >= 18:\n    print(\"Mayor\")\nelse:\n    print(\"Menor\")\nprint(\"Fin\")",
                acceptedAnswers = listOf("Mayor\nFin"),
                correctFeedback = "¡Exacto! >= incluye el 18, así que entra al if. El print de Fin siempre se ejecuta.",
                wrongFeedback = "Fíjate en el >=: incluye el caso de ser igual. Y la última línea no está indentada, así que no depende del if."
            ),
            OrderLines(
                prompt = "Ordena el programa del portero para una persona de 16 años.",
                lines = listOf(
                    "edad = 16",
                    "if edad >= 18:",
                    "    print(\"Entra\")",
                    "else:",
                    "    print(\"No entra\")"
                ),
                correctFeedback = "¡Perfecto! Primero el dato, luego la condición y después los dos caminos.",
                wrongFeedback = "La variable debe existir antes del if, y el else va después de lo que se ejecuta cuando la condición se cumple."
            ),
            FindBug(
                prompt = "Este programa da error. ¿En qué línea está el bug?",
                codeLines = listOf(
                    "edad = 20",
                    "if edad >= 18",
                    "    print(\"Entra\")"
                ),
                bugLineIndex = 1,
                correctFeedback = "¡Bug cazado! Faltan los dos puntos al final de la línea del if.",
                wrongFeedback = "La línea del if necesita algo al final para decir «aquí empieza el bloque»."
            )
        )
    )

    private val comparandoCosas = LessonContent(
        lessonId = "n05_l02",
        title = "Comparando cosas ⚖️",
        hook = "Para decidir, Glitch compara. ¿Es más grande? ¿Es igual? ¿Es distinto? Cada comparación responde con True o False.",
        explanation = "Comparar da True o False. Operadores: == igual, != distinto, <, >, <= y >=. Ojo: = guarda un valor, == compara.",
        code = "print(5 > 3)\nprint(5 == 3)\nprint(5 != 3)",
        expectedOutput = "True\nFalse\nTrue",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "print(10 - 4 >= 7)\nprint(3 + 4 == 7)\nprint(2 * 5 != 10)",
                acceptedAnswers = listOf("False\nTrue\nFalse"),
                correctFeedback = "¡Exacto! Primero se calcula cada lado y luego se compara: 6 >= 7 es falso, 7 == 7 verdadero, 10 != 10 falso.",
                wrongFeedback = "Resuelve primero las cuentas de cada lado y luego aplica la comparación."
            ),
            FillBlanks(
                prompt = "¿Qué operador comprueba si energia vale exactamente 100?",
                codeTemplate = "print(energia ___ 100)",
                answers = listOf("=="),
                chips = listOf("==", "=", "=>", "==="),
                correctFeedback = "¡Correcto! Dos signos iguales comparan; uno solo guarda.",
                wrongFeedback = "Para comparar se usan dos =. Un solo = guarda un valor y las otras opciones no existen en Python."
            ),
            TypeCode(
                prompt = "Escribe un print que muestre si 8 es mayor o igual que 8.",
                acceptedAnswers = listOf("print(8 >= 8)"),
                placeholder = "print(...)",
                correctFeedback = "¡Eso! 8 >= 8 es True porque 8 es igual a 8.",
                wrongFeedback = "Usa print con la comparación dentro: 8, el operador mayor o igual (>=) y 8."
            )
        )
    )

    private val ifSinElse = LessonContent(
        lessonId = "n05_l03",
        title = "El if solito 🎯",
        hook = "A veces Glitch solo quiere hacer algo cuando se cumple una condición, y si no, no hacer nada.",
        explanation = "El else es opcional: un if solo ejecuta su bloque si la condición es verdadera, y si no, el programa sigue con lo que viene después.",
        code = "puntos = 120\nif puntos > 100:\n    print(\"¡Nuevo récord!\")\nprint(\"Fin del juego\")",
        expectedOutput = "¡Nuevo récord!\nFin del juego",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "puntos = 80\nif puntos > 100:\n    print(\"Récord\")\nprint(\"Fin\")",
                acceptedAnswers = listOf("Fin"),
                correctFeedback = "¡Exacto! 80 no supera 100, así que el bloque del if se salta.",
                wrongFeedback = "Revisa la condición: 80 > 100 es falso, así que solo se ejecuta lo que está fuera del if."
            ),
            TypeCode(
                prompt = "Escribe la línea del if que comprueba si edad es menor que 12 (sin olvidar los dos puntos).",
                acceptedAnswers = listOf("if edad < 12:"),
                placeholder = "if ...",
                correctFeedback = "¡Eso! if, la condición y los dos puntos.",
                wrongFeedback = "La línea empieza con if, sigue la condición edad < 12 y termina con dos puntos."
            ),
            FillBlanks(
                prompt = "¿Qué símbolo falta al final de la línea del if?",
                codeTemplate = "if puntos > 100___\n    print(\"Récord\")",
                answers = listOf(":"),
                chips = listOf(":", ";", ",", "."),
                correctFeedback = "¡Sí! Los dos puntos abren el bloque del if.",
                wrongFeedback = "Es un signo de puntuación que usas en una lista cuando enumeras, formado por dos puntos uno sobre otro."
            )
        )
    )

    private val sangria = LessonContent(
        lessonId = "n05_l04",
        title = "Los espacios mandan 📏",
        hook = "En Python los espacios al inicio de la línea no son decoración: indican qué pertenece a cada decisión.",
        explanation = "Lo que va indentado (con 4 espacios) bajo un if forma su bloque. Una línea sin indentar ya no pertenece al if y se ejecuta siempre.",
        code = "if True:\n    print(\"Dentro\")\n    print(\"También dentro\")\nprint(\"Fuera\")",
        expectedOutput = "Dentro\nTambién dentro\nFuera",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "energia = 10\nif energia > 50:\n    print(\"A\")\n    print(\"B\")\nprint(\"C\")",
                acceptedAnswers = listOf("C"),
                correctFeedback = "¡Exacto! La condición es falsa, así que A y B se saltan. C no está indentada: siempre sale.",
                wrongFeedback = "Las dos líneas indentadas dependen del if (que es falso). Solo la última está fuera de él."
            ),
            FindBug(
                prompt = "Python dice IndentationError: expected an indented block. ¿Qué línea está mal?",
                codeLines = listOf(
                    "if 5 > 3:",
                    "print(\"Hola\")",
                    "print(\"Fin\")"
                ),
                bugLineIndex = 1,
                correctFeedback = "¡Bug cazado! Después de un if hay que indentar el bloque.",
                wrongFeedback = "Después de una línea que termina en : se espera un bloque indentado. Busca la línea que debería tener espacios."
            ),
            MultipleChoice(
                prompt = "¿Qué marca dónde empieza el bloque de un if en Python?",
                options = listOf(
                    "Los dos puntos y la indentación de las líneas siguientes",
                    "Las llaves { }",
                    "La palabra begin",
                    "Un punto y coma al final"
                ),
                correctIndex = 0,
                correctFeedback = "¡Sí! Dos puntos al final del if y las líneas siguientes con sangría.",
                wrongFeedback = "Python no usa llaves ni begin: usa dos puntos y la sangría."
            )
        )
    )

    private val compararTextos = LessonContent(
        lessonId = "n05_l05",
        title = "Comparar textos 🔤",
        hook = "El portero ahora pide la palabra secreta. ¿Cómo sabe Glitch si la dijeron bien?",
        explanation = "También se comparan textos con == y !=. Para Python, las mayúsculas cuentan: \"Glitch\" no es igual a \"glitch\". Con < y > se compara el orden alfabético.",
        code = "palabra = \"Glitch\"\nprint(palabra == \"Glitch\")\nprint(palabra == \"glitch\")\nprint(palabra != \"Bot\")",
        expectedOutput = "True\nFalse\nTrue",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "color = \"azul\"\nprint(color == \"azul\")\nprint(color == \"Azul\")\nprint(\"a\" < \"b\")",
                acceptedAnswers = listOf("True\nFalse\nTrue"),
                correctFeedback = "¡Muy bien! azul es igual a azul, pero no a Azul, y la a va antes que la b.",
                wrongFeedback = "Mira las mayúsculas: Azul y azul son textos distintos. Y en el orden alfabético, a va antes que b."
            ),
            FindBug(
                prompt = "Este programa debía comparar la respuesta, pero falla. ¿En qué línea está el bug?",
                codeLines = listOf(
                    "respuesta = \"Sí\"",
                    "if respuesta = \"Sí\":",
                    "    print(\"Gracias\")"
                ),
                bugLineIndex = 1,
                correctFeedback = "¡Bug cazado! Dentro de un if se compara con ==, no con =.",
                wrongFeedback = "Mira el signo dentro del if: = guarda valores, y aquí hace falta comparar."
            ),
            FillBlanks(
                prompt = "Completa para que entre solo quien diga la clave exacta.",
                codeTemplate = "clave = \"abracadabra\"\nif clave ___ \"abracadabra\":\n    print(\"Pasa\")",
                answers = listOf("=="),
                chips = listOf("==", "=", "!=", "=>"),
                correctFeedback = "¡Sí! == comprueba si son exactamente iguales.",
                wrongFeedback = "Para comprobar igualdad se usan dos signos =."
            )
        )
    )

    private val decisionesConInput = LessonContent(
        lessonId = "n05_l06",
        title = "Decidir con lo que escribe el usuario 💬",
        hook = "Glitch pregunta la edad y decide si deja pasar. ¡Ya es un portero interactivo!",
        explanation = "Combina input, int() y un if/else para que el programa reaccione a lo que escribe el usuario. La salida de abajo es la que verías si escribes 20.",
        code = "edad = int(input(\"¿Cuántos años tienes? \"))\nif edad >= 18:\n    print(\"Puedes entrar\")\nelse:\n    print(\"Todavía no\")",
        expectedOutput = "¿Cuántos años tienes? 20\nPuedes entrar",
        exercises = listOf(
            PredictOutput(
                prompt = "El usuario escribe 15. ¿Qué muestra el print?",
                code = "n = int(input(\"N: \"))\nif n >= 18:\n    print(\"Adulto\")\nelse:\n    print(\"Menor\")",
                acceptedAnswers = listOf("Menor"),
                correctFeedback = "¡Correcto! 15 no es mayor o igual que 18, así que va el else.",
                wrongFeedback = "Compara el 15 con 18: la condición es falsa, así que se ejecuta el bloque del else."
            ),
            OrderLines(
                prompt = "Ordena el programa del portero interactivo.",
                lines = listOf(
                    "edad = int(input(\"Edad: \"))",
                    "if edad >= 18:",
                    "    print(\"Pasa\")",
                    "else:",
                    "    print(\"No pasa\")"
                ),
                correctFeedback = "¡Programa listo! Pregunta, decide y responde.",
                wrongFeedback = "Primero hay que pedir el dato; luego decidir con if y else."
            ),
            FindBug(
                prompt = "Python dice TypeError al comparar edad >= 18. ¿Qué línea hay que arreglar?",
                codeLines = listOf(
                    "edad = input(\"Edad: \")",
                    "if edad >= 18:",
                    "    print(\"Pasa\")"
                ),
                bugLineIndex = 0,
                correctFeedback = "¡Bug cazado! edad es texto: hay que convertirla con int() al leerla.",
                wrongFeedback = "El error aparece al comparar, pero el origen está en cómo se creó edad: input devuelve texto."
            )
        )
    )

    private val paresEImpares = LessonContent(
        lessonId = "n05_l07",
        title = "Pares e impares 🎲",
        hook = "Glitch quiere saber si un número es par. Con el resto de la división puede averiguarlo sin esfuerzo.",
        explanation = "Un número es par si al dividirlo entre 2 el resto es 0: n % 2 == 0. Dentro de un if combinas % y == para preguntar cosas sobre el resto.",
        code = "n = 7\nif n % 2 == 0:\n    print(\"Par\")\nelse:\n    print(\"Impar\")",
        expectedOutput = "Impar",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "n = 10\nif n % 2 == 0:\n    print(\"Par\")\nelse:\n    print(\"Impar\")\nn = n + 1\nif n % 2 == 0:\n    print(\"Par\")\nelse:\n    print(\"Impar\")",
                acceptedAnswers = listOf("Par\nImpar"),
                correctFeedback = "¡Exacto! 10 es par y 11 es impar.",
                wrongFeedback = "Revisa cada vez el valor de n: primero 10 y luego 11 (después de sumar 1)."
            ),
            FillBlanks(
                prompt = "¿Qué operador permite saber el resto al dividir entre 2?",
                codeTemplate = "if n ___ 2 == 0:\n    print(\"Par\")",
                answers = listOf("%"),
                chips = listOf("%", "//", "/", "*"),
                correctFeedback = "¡Sí! % devuelve el resto de la división.",
                wrongFeedback = "Para saber el resto necesitas el operador del porcentaje (%)."
            ),
            TypeCode(
                prompt = "Escribe la línea del if que comprueba si numero es múltiplo de 5 (resto 0 al dividir entre 5).",
                acceptedAnswers = listOf("if numero % 5 == 0:"),
                placeholder = "if ...",
                correctFeedback = "¡Eso! Un múltiplo de 5 deja resto 0 al dividirlo entre 5.",
                wrongFeedback = "Usa if, luego numero % 5 == 0 y termina con dos puntos."
            )
        )
    )

    private val variosIf = LessonContent(
        lessonId = "n05_l08",
        title = "Varios if seguidos 🧱",
        hook = "Glitch revisa varias cosas, una tras otra, con if separados. ¿Pueden cumplirse varios a la vez?",
        explanation = "Cada if suelto se evalúa por su cuenta. Si varias condiciones son verdaderas, se ejecutan varios bloques.",
        code = "puntos = 95\nif puntos > 90:\n    print(\"Oro\")\nif puntos > 50:\n    print(\"Plata\")",
        expectedOutput = "Oro\nPlata",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "x = 8\nif x > 5:\n    print(\"A\")\nif x > 7:\n    print(\"B\")\nif x > 9:\n    print(\"C\")",
                acceptedAnswers = listOf("A\nB"),
                correctFeedback = "¡Exacto! 8 > 5 y 8 > 7 se cumplen; 8 > 9 no.",
                wrongFeedback = "Revisa cada condición por separado: cada if suelto se evalúa de forma independiente."
            ),
            MultipleChoice(
                prompt = "En dos if separados (sin else), ¿cuántos bloques pueden ejecutarse?",
                options = listOf(
                    "Los dos, si se cumplen ambas condiciones",
                    "Solo uno siempre",
                    "Ninguno",
                    "Depende del orden alfabético"
                ),
                correctIndex = 0,
                correctFeedback = "¡Sí! Cada if decide por su cuenta, sin importar lo que pase con el otro.",
                wrongFeedback = "Un if suelto no sabe nada de los otros: si su condición se cumple, se ejecuta."
            ),
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "n = 4\nif n > 3:\n    print(\"mayor que 3\")\nif n < 3:\n    print(\"menor que 3\")\nif n == 4:\n    print(\"igual a 4\")",
                acceptedAnswers = listOf("mayor que 3\nigual a 4"),
                correctFeedback = "¡Muy bien! Se cumplen el primero y el tercero; el segundo no.",
                wrongFeedback = "Responde cada pregunta con n = 4: ¿mayor que 3? ¿menor que 3? ¿igual a 4?"
            )
        )
    )

    private val ifDentroDeIf = LessonContent(
        lessonId = "n05_l09",
        title = "Decisiones dentro de decisiones 🪆",
        hook = "Glitch revisa dos cosas seguidas: primero la edad y, si pasa, la altura. Como muñecas rusas.",
        explanation = "Un if puede estar dentro de otro. Cada nivel se indenta más, y el bloque interior solo se evalúa si se cumple la condición exterior.",
        code = "edad = 17\nif edad >= 12:\n    if edad >= 18:\n        print(\"Adulto\")\n    else:\n        print(\"Joven\")\nelse:\n    print(\"Niño\")",
        expectedOutput = "Joven",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "x = 7\nif x > 5:\n    if x > 10:\n        print(\"Enorme\")\n    else:\n        print(\"Mediano\")\nelse:\n    print(\"Pequeño\")",
                acceptedAnswers = listOf("Mediano"),
                correctFeedback = "¡Exacto! 7 > 5 entra al primer bloque, y ahí 7 > 10 es falso, así que va el else interno.",
                wrongFeedback = "Primero la condición de afuera (¿7 > 5?) y luego la de adentro (¿7 > 10?)."
            ),
            PredictOutput(
                prompt = "Ahora con x = 3. ¿Qué muestra este programa?",
                code = "x = 3\nif x > 5:\n    if x > 10:\n        print(\"Enorme\")\n    else:\n        print(\"Mediano\")\nelse:\n    print(\"Pequeño\")",
                acceptedAnswers = listOf("Pequeño"),
                correctFeedback = "¡Correcto! 3 no supera 5, así que va el else exterior y el bloque interior ni se evalúa.",
                wrongFeedback = "Si la condición de afuera es falsa, no se mira nada de lo que hay dentro."
            ),
            FindBug(
                prompt = "Este código da error de indentación. ¿Qué línea está mal?",
                codeLines = listOf(
                    "x = 7",
                    "if x > 5:",
                    "    if x > 10:",
                    "    print(\"Enorme\")"
                ),
                bugLineIndex = 3,
                correctFeedback = "¡Bug cazado! El print debe ir más hacia adentro que el if interior.",
                wrongFeedback = "Después de un if interior hay que indentar un nivel más. Busca la línea que no lo hace."
            )
        )
    )

    private val erroresTipicos = LessonContent(
        lessonId = "n05_l10",
        title = "Errores típicos de los if 🐞",
        hook = "Todos los que aprenden Python se tropiezan con las mismas piedras. Glitch las conoce todas.",
        explanation = "Los tres errores clásicos: usar = en vez de ==, olvidar los dos puntos y no indentar el bloque. Python te los señala con SyntaxError o IndentationError.",
        code = "edad = 20\nif edad = 18:\n    print(\"Justo 18\")",
        expectedOutput = "SyntaxError: invalid syntax. Maybe you meant '==' or ':=' instead of '='?",
        exercises = listOf(
            FindBug(
                prompt = "Python dice: invalid syntax. Maybe you meant '==' instead of '='. ¿En qué línea?",
                codeLines = listOf(
                    "n = 5",
                    "if n = 5:",
                    "    print(\"Cinco\")"
                ),
                bugLineIndex = 1,
                correctFeedback = "¡Bug cazado! En un if se compara con ==.",
                wrongFeedback = "El mensaje te da la pista: dentro del if se confundió = con ==."
            ),
            FindBug(
                prompt = "Python dice: expected ':'. ¿En qué línea?",
                codeLines = listOf(
                    "n = 5",
                    "if n == 5",
                    "    print(\"Cinco\")"
                ),
                bugLineIndex = 1,
                correctFeedback = "¡Bug cazado! Faltan los dos puntos al final del if.",
                wrongFeedback = "El mensaje dice qué se esperaba: un signo al final de la línea del if."
            ),
            TypeCode(
                prompt = "Corrige esta línea y escríbela bien: if n = 5",
                acceptedAnswers = listOf("if n == 5:"),
                placeholder = "if ...",
                correctFeedback = "¡Arreglado! Doble igual para comparar y dos puntos al final.",
                wrongFeedback = "Hay dos errores en esa línea: el = debe ser == y falta algo al final."
            )
        )
    )

    private val accesoAlParque = LessonContent(
        lessonId = "n05_l11",
        title = "Acceso al parque 🎢",
        hook = "Glitch vigila la montaña rusa: hay que tener edad y altura suficientes. Es tu primer programa de decisiones completo.",
        explanation = "Mezcla input, int() e if anidados para tomar decisiones en cadena. La salida de abajo es la que verías si escribes 15 y 150.",
        code = "edad = int(input(\"Edad: \"))\naltura = int(input(\"Altura en cm: \"))\nif edad >= 12:\n    if altura >= 140:\n        print(\"Puedes subir\")\n    else:\n        print(\"Eres muy bajito\")\nelse:\n    print(\"Eres muy pequeño\")",
        expectedOutput = "Edad: 15\nAltura en cm: 150\nPuedes subir",
        exercises = listOf(
            PredictOutput(
                prompt = "El usuario escribe 15 y 150. ¿Qué muestra el programa (solo el print final)?",
                code = "edad = int(input(\"Edad: \"))\naltura = int(input(\"Altura: \"))\nif edad >= 12:\n    if altura >= 140:\n        print(\"Puedes subir\")\n    else:\n        print(\"Eres muy bajito\")\nelse:\n    print(\"Eres muy pequeño\")",
                acceptedAnswers = listOf("Puedes subir"),
                correctFeedback = "¡Perfecto! 15 >= 12 y 150 >= 140: puede subir.",
                wrongFeedback = "Comprueba las dos condiciones: la edad (15 >= 12) y después la altura (150 >= 140)."
            ),
            PredictOutput(
                prompt = "Ahora el usuario escribe 15 y 120. ¿Qué muestra el programa (solo el print final)?",
                code = "edad = int(input(\"Edad: \"))\naltura = int(input(\"Altura: \"))\nif edad >= 12:\n    if altura >= 140:\n        print(\"Puedes subir\")\n    else:\n        print(\"Eres muy bajito\")\nelse:\n    print(\"Eres muy pequeño\")",
                acceptedAnswers = listOf("Eres muy bajito"),
                correctFeedback = "¡Exacto! La edad alcanza, pero 120 no llega a 140.",
                wrongFeedback = "Pasa la primera condición (edad), pero falla la de la altura."
            ),
            TypeCode(
                prompt = "Escribe la línea del if que comprueba si altura es al menos 140.",
                acceptedAnswers = listOf("if altura >= 140:"),
                placeholder = "if ...",
                correctFeedback = "¡Eso! «Al menos 140» se escribe con >=.",
                wrongFeedback = "«Al menos» significa mayor o igual: usa >= y termina con dos puntos."
            )
        )
    )

    // Jefe del nivel: mezcla lo aprendido en las 11 lecciones.
    val boss = BossContent(
        bossId = "n05_boss",
        name = "La Esfinge de las Decisiones",
        emoji = "🦁",
        intro = "Plantea acertijos y solo deja pasar a quien sepa decidir con if y else. ¡Responde bien para cruzar!",
        exercises = listOf(
            PredictOutput(
                prompt = "El primer acertijo de la Esfinge. ¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "n = 12\nif n % 2 == 0:\n    print(\"Par\")\nelse:\n    print(\"Impar\")\nprint(\"Fin\")",
                acceptedAnswers = listOf("Par\nFin"),
                correctFeedback = "¡Acertijo resuelto! 12 es par, y Fin siempre sale.",
                wrongFeedback = "Calcula 12 % 2: si da 0 es par. La última línea no depende del if."
            ),
            FindBug(
                prompt = "La Esfinge escondió un error. ¡Tócalo!",
                codeLines = listOf(
                    "edad = 20",
                    "if edad >= 18",
                    "    print(\"Entra\")"
                ),
                bugLineIndex = 1,
                correctFeedback = "¡Bug golpeado! Faltaban los dos puntos del if.",
                wrongFeedback = "La línea del if necesita algo al final para abrir su bloque."
            ),
            FillBlanks(
                prompt = "Quieres mostrar Récord solo cuando puntos supera 100. ¿Qué operador falta?",
                codeTemplate = "puntos = 90\nif puntos ___ 100:\n    print(\"Récord\")",
                answers = listOf(">"),
                chips = listOf(">", "<", "==", "!="),
                correctFeedback = "¡Sí! > significa «mayor que»: con 90 no se muestra nada.",
                wrongFeedback = "«Supera» significa que es mayor. Elige el operador que lo expresa."
            ),
            PredictOutput(
                prompt = "Otro acertijo. ¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "x = 8\nif x > 5:\n    print(\"A\")\nif x > 7:\n    print(\"B\")\nelse:\n    print(\"C\")",
                acceptedAnswers = listOf("A\nB"),
                correctFeedback = "¡Resuelto! El primer if muestra A; el segundo if/else muestra B porque 8 > 7.",
                wrongFeedback = "Son dos decisiones independientes: el primer if y, aparte, el par if/else."
            ),
            TypeCode(
                prompt = "Último acertijo: escribe la línea del if que comprueba si nombre es igual a \"Glitch\".",
                acceptedAnswers = listOf("if nombre == \"Glitch\":"),
                placeholder = "if ...",
                correctFeedback = "¡La Esfinge te deja pasar! Comparación con == y dos puntos al final.",
                wrongFeedback = "Usa if, luego nombre == \"Glitch\" y termina con dos puntos."
            )
        )
    )

    val all: List<LessonContent> = listOf(
        elPorteroDeGlitchCity, comparandoCosas, ifSinElse, sangria,
        compararTextos, decisionesConInput, paresEImpares, variosIf,
        ifDentroDeIf, erroresTipicos, accesoAlParque
    )
}
