package me.pablorincon.proyecto_app.domain.content

import me.pablorincon.proyecto_app.domain.BossContent
import me.pablorincon.proyecto_app.domain.FillBlanks
import me.pablorincon.proyecto_app.domain.FindBug
import me.pablorincon.proyecto_app.domain.LessonContent
import me.pablorincon.proyecto_app.domain.MultipleChoice
import me.pablorincon.proyecto_app.domain.OrderLines
import me.pablorincon.proyecto_app.domain.PredictOutput
import me.pablorincon.proyecto_app.domain.TypeCode

// Nivel 1 · Primeros pasos (11 lecciones y el jefe «El Bug Gigante»). Hilo conductor: Glitch, un robot muy literal que solo hace
// exactamente lo que se le dice. Cada lección tiene 3 ejercicios dinámicos (~1 minuto cada uno).
// Contenido aprobado por el dueño del proyecto (decisión C), versión con ejercicios dinámicos.
internal object Level01Lessons {

    private val glitchNoLeeMentes = LessonContent(
        lessonId = "n01_l01",
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
        lessonId = "n01_l02",
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
        lessonId = "n01_l03",
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
        lessonId = "n01_l04",
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

    private val comillas = LessonContent(
        lessonId = "n01_l05",
        title = "Comillas dobles y simples ✂️",
        hook = "Glitch descubre que Python acepta dos tipos de comillas. Eso sí: hay que cerrar con las mismas que abriste, o se arma un lío.",
        explanation = "El texto puede ir entre comillas dobles \"...\" o simples '...'. Deben abrir y cerrar con el mismo tipo. Si el texto lleva comillas de un tipo, usa el otro tipo por fuera.",
        code = "print(\"Hola\")\nprint('Hola')\nprint(\"Dijo 'beep'\")",
        expectedOutput = "Hola\nHola\nDijo 'beep'",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "print('Soy Glitch')\nprint(\"Dijo 'hola'\")",
                acceptedAnswers = listOf("Soy Glitch\nDijo 'hola'"),
                correctFeedback = "¡Bien! Las comillas de afuera no se ven; las de adentro, que son de otro tipo, sí.",
                wrongFeedback = "Las comillas que abren y cierran el texto no se muestran. Las que van dentro del texto, sí."
            ),
            FindBug(
                prompt = "Una de estas líneas mezcla las comillas. ¡Cázala!",
                codeLines = listOf(
                    "print(\"Hola\")",
                    "print('Adiós\")",
                    "print(\"Chao\")"
                ),
                bugLineIndex = 1,
                correctFeedback = "¡Bug cazado! Abrió con comilla simple y cerró con doble: Python no sabe dónde termina el texto.",
                wrongFeedback = "Mira con qué comilla abre y con cuál cierra cada línea. Deben ser del mismo tipo."
            ),
            FillBlanks(
                prompt = "Glitch quiere mostrar: Ella dijo \"hola\" (con las comillas dobles). ¿Con qué comillas va el texto por fuera?",
                codeTemplate = "print(___Ella dijo \"hola\"___)",
                answers = listOf("'", "'"),
                chips = listOf("'", "'", "\"", "\""),
                correctFeedback = "¡Sí! Si el texto lleva comillas dobles, las de afuera deben ser simples (y al revés).",
                wrongFeedback = "Si por fuera usas el mismo tipo que lleva el texto por dentro, Python cree que el texto termina antes de tiempo."
            )
        )
    )

    private val lineasEnBlanco = LessonContent(
        lessonId = "n01_l06",
        title = "Líneas en blanco 📜",
        hook = "Glitch quiere que sus mensajes respiren. Para eso aprende a dejar líneas vacías.",
        explanation = "print() sin nada dentro deja una línea en blanco. Dentro de un texto, \\n es un salto de línea: print(\"Hola\\nAdiós\") escribe dos líneas.",
        code = "print(\"Hola\")\nprint()\nprint(\"Adiós\")",
        expectedOutput = "Hola\n\nAdiós",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Cuántas líneas ocupa la salida en pantalla, contando la línea vacía? Escribe solo el número.",
                code = "print(\"Uno\\nDos\")\nprint()\nprint(\"Tres\")",
                acceptedAnswers = listOf("4"),
                correctFeedback = "¡Exacto! Uno, Dos, una línea vacía y Tres: son 4 líneas.",
                wrongFeedback = "Cuenta bien: el \\n parte el primer texto en dos líneas, print() agrega una vacía y luego sale Tres."
            ),
            TypeCode(
                prompt = "Escribe la instrucción que deja una línea en blanco.",
                acceptedAnswers = listOf("print()"),
                placeholder = "print(...)",
                correctFeedback = "¡Eso! Un print sin nada dentro escribe solo el salto de línea.",
                wrongFeedback = "Es la misma orden de siempre, pero sin ningún texto entre los paréntesis."
            ),
            OrderLines(
                prompt = "Ordena la carta de Glitch: saludo, línea vacía, mensaje, línea vacía y firma.",
                lines = listOf(
                    "print(\"Querida Ana,\")",
                    "print()",
                    "print(\"Te espero en Glitch City.\")",
                    "print()",
                    "print(\"Glitch\")"
                ),
                correctFeedback = "¡Qué carta tan elegante! Las líneas vacías separan cada parte.",
                wrongFeedback = "Una carta empieza con el saludo y termina con la firma. Las líneas vacías van entre una parte y otra."
            )
        )
    )

    private val glitchHaceCuentas = LessonContent(
        lessonId = "n01_l07",
        title = "Glitch hace cuentas ➕",
        hook = "Glitch descubre que también sabe calcular. Pero atento: sin comillas calcula, con comillas solo copia.",
        explanation = "Sin comillas, Python calcula: + suma, - resta, * multiplica y / divide. Con comillas es solo texto y se muestra tal cual.",
        code = "print(2 + 3)\nprint(\"2 + 3\")\nprint(10 - 4)",
        expectedOutput = "5\n2 + 3\n6",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "print(4 + 4)\nprint(\"4 + 4\")\nprint(4 * 2)",
                acceptedAnswers = listOf("8\n4 + 4\n8"),
                correctFeedback = "¡Bien visto! Sin comillas se calcula (8); con comillas se copia (4 + 4).",
                wrongFeedback = "Fíjate en las comillas: la segunda línea es texto y se copia tal cual; las otras dos se calculan."
            ),
            FillBlanks(
                prompt = "Glitch debe calcular y mostrar 7 por 3. ¿Qué símbolo falta?",
                codeTemplate = "print(7 ___ 3)",
                answers = listOf("*"),
                chips = listOf("*", "x", "+", "×"),
                correctFeedback = "¡Correcto! En Python la multiplicación se escribe con un asterisco (*).",
                wrongFeedback = "Python no entiende la x ni el signo ×. El símbolo de multiplicar en el teclado de programación es el asterisco."
            ),
            TypeCode(
                prompt = "Escribe la instrucción para que Glitch muestre el resultado de 15 menos 6.",
                acceptedAnswers = listOf("print(15 - 6)"),
                placeholder = "print(...)",
                correctFeedback = "¡Eso! Sin comillas, Python hace la resta y muestra 9.",
                wrongFeedback = "Usa print con la cuenta dentro, sin comillas, para que Python la calcule en vez de copiarla."
            )
        )
    )

    private val leerLosErrores = LessonContent(
        lessonId = "n01_l08",
        title = "Leer los errores 🚨",
        hook = "Cuando algo sale mal, Python grita. Glitch aprende a entender sus gritos en vez de asustarse.",
        explanation = "SyntaxError: algo está mal escrito. NameError: usaste un nombre que Python no conoce. IndentationError: hay espacios de más al inicio de una línea.",
        code = "print(\"Hola\")\n  print(\"Glitch\")",
        expectedOutput = "IndentationError: unexpected indent",
        exercises = listOf(
            FindBug(
                prompt = "Python dice: NameError: name 'pritn' is not defined. Toca la línea culpable.",
                codeLines = listOf(
                    "print(\"Hola\")",
                    "pritn(\"Glitch\")",
                    "print(\"Fin\")"
                ),
                bugLineIndex = 1,
                correctFeedback = "¡Bug cazado! pritn no existe: el nombre correcto es print.",
                wrongFeedback = "El mensaje te da el nombre que Python no conoce: pritn. Busca la línea donde aparece."
            ),
            FillBlanks(
                prompt = "Python dice IndentationError: unexpected indent. ¿Qué había de más al inicio de la línea?",
                codeTemplate = "Había ___ de más antes de print",
                answers = listOf("espacios"),
                chips = listOf("espacios", "comillas", "paréntesis", "números"),
                correctFeedback = "¡Exacto! Indent significa sangría: espacios al principio de la línea.",
                wrongFeedback = "«Indent» viene de indentación, la sangría que se forma al dejar huecos al principio de la línea."
            ),
            MultipleChoice(
                prompt = "Python dice NameError: name 'Hola' is not defined al ejecutar print(Hola). ¿Qué se olvidó?",
                options = listOf(
                    "Las comillas del texto",
                    "El paréntesis de cierre",
                    "Una coma",
                    "La palabra print"
                ),
                correctIndex = 0,
                correctFeedback = "¡Sí! Sin comillas, Python cree que Hola es un nombre y no lo encuentra.",
                wrongFeedback = "Fíjate en que print(Hola) sí lleva print y paréntesis. Lo que falta es lo que convierte Hola en texto."
            )
        )
    )

    private val dosCosasEnUnPrint = LessonContent(
        lessonId = "n01_l09",
        title = "Dos cosas en un print 🎁",
        hook = "Glitch quiere decir texto y números en la misma frase. Para eso, un print puede llevar varias cosas separadas por comas.",
        explanation = "Con comas, print muestra cada cosa separada por un espacio. Sirve para mezclar texto y números sin problemas.",
        code = "print(\"Hola\", \"Glitch\")\nprint(\"Tengo\", 3, \"años\")\nprint(\"2 + 2 =\", 2 + 2)",
        expectedOutput = "Hola Glitch\nTengo 3 años\n2 + 2 = 4",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "print(\"Glitch\", \"tiene\", 2, \"brazos\")\nprint(\"1 + 1 =\", 1 + 1)",
                acceptedAnswers = listOf("Glitch tiene 2 brazos\n1 + 1 = 2"),
                correctFeedback = "¡Correcto! Cada coma agrega un espacio, y 1 + 1 sin comillas se calcula.",
                wrongFeedback = "Cada coma separa dos cosas con un espacio. Y recuerda: lo que no lleva comillas se calcula."
            ),
            FillBlanks(
                prompt = "Glitch quiere mostrar: Tengo 5 robots. Completa para separar cada parte.",
                codeTemplate = "print(\"Tengo\"___ 5 ___ \"robots\")",
                answers = listOf(",", ","),
                chips = listOf(",", ",", "+", "+"),
                correctFeedback = "¡Muy bien! La coma separa las partes y deja mezclar texto con números.",
                wrongFeedback = "Con + Python intenta sumar, y no se pueden sumar un texto y un número. La coma sí las separa."
            ),
            TypeCode(
                prompt = "Escribe un print que muestre: Glitch 100 (el nombre y el número, separados por coma).",
                acceptedAnswers = listOf("print(\"Glitch\", 100)", "print(\"Glitch\", \"100\")"),
                placeholder = "print(...)",
                correctFeedback = "¡Eso! Dos cosas en un solo print, separadas por una coma.",
                wrongFeedback = "Pon el texto entre comillas, una coma y el número, todo dentro del mismo print."
            )
        )
    )

    private val miPrimerPrograma = LessonContent(
        lessonId = "n01_l10",
        title = "Mi primer programa 🏁",
        hook = "Glitch va a presentarse por primera vez, con nombre, edad y una cuentita. Es tu primer programa completo.",
        explanation = "Un programa es una receta completa: comentarios, textos, números y cálculos juntos, ejecutados de arriba abajo.",
        code = "# Presentación de Glitch\nprint(\"Hola, soy Glitch\")\nprint(\"Tengo\", 3, \"años\")\nprint(\"Mi energía es\", 50 + 50)",
        expectedOutput = "Hola, soy Glitch\nTengo 3 años\nMi energía es 100",
        exercises = listOf(
            OrderLines(
                prompt = "Ordena el programa de presentación de Glitch (el comentario va primero).",
                lines = listOf(
                    "# Presentación de Glitch",
                    "print(\"Hola, soy Glitch\")",
                    "print(\"Tengo\", 3, \"años\")",
                    "print(\"Mi energía es\", 50 + 50)"
                ),
                correctFeedback = "¡Programa listo! Comentario, saludo, edad y energía, en orden.",
                wrongFeedback = "Piensa cómo se presenta alguien: primero saluda, luego da sus datos. El comentario sirve de título."
            ),
            FindBug(
                prompt = "Este programa falla. ¿En qué línea está el bug?",
                codeLines = listOf(
                    "print(\"Hola, soy Glitch\")",
                    "print(\"Tengo\", 3, \"años\"",
                    "print(\"Adiós\")"
                ),
                bugLineIndex = 1,
                correctFeedback = "¡Bug cazado! Falta el paréntesis que cierra el print.",
                wrongFeedback = "Revisa que cada paréntesis que se abre también se cierre. Una línea se quedó abierta."
            ),
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "print(\"Robots:\", 2 + 3)\nprint(\"Total\", 4 * 2)",
                acceptedAnswers = listOf("Robots: 5\nTotal 8"),
                correctFeedback = "¡Perfecto! Python calcula 2 + 3 y 4 * 2, y muestra todo junto con espacios.",
                wrongFeedback = "Calcula primero lo que no lleva comillas: 2 + 3 y 4 * 2. Luego júntalo con el texto."
            )
        )
    )

    private val pensarAntesDeEscribir = LessonContent(
        lessonId = "n01_l11",
        title = "Pensar antes de escribir 🧠",
        hook = "Los mejores programadores no empiezan tecleando: primero piensan los pasos. Glitch también lo va a hacer.",
        explanation = "Antes de programar, escribe el plan con palabras. Luego tradúcelo a Python. Los comentarios con # te sirven para dejar el plan dentro del programa sin que se ejecute.",
        code = "# Paso 1: saludar\n# Paso 2: decir mi nombre\n# Paso 3: despedirme\nprint(\"Hola\")\nprint(\"Soy Glitch\")\nprint(\"Adiós\")",
        expectedOutput = "Hola\nSoy Glitch\nAdiós",
        exercises = listOf(
            OrderLines(
                prompt = "Glitch quiere cruzar la calle. Ordena el plan paso a paso.",
                lines = listOf(
                    "print(\"Mirar a los dos lados\")",
                    "print(\"Esperar a que no vengan autos\")",
                    "print(\"Cruzar caminando\")",
                    "print(\"Llegar al otro lado\")"
                ),
                correctFeedback = "¡Plan seguro! Primero se mira, luego se espera y al final se cruza.",
                wrongFeedback = "Piensa qué conviene hacer primero para cruzar sin peligro, y qué solo puede pasar al final."
            ),
            TypeCode(
                prompt = "Traduce este paso a Python: «Mostrar el mensaje Buenos días».",
                acceptedAnswers = listOf("print(\"Buenos días\")"),
                placeholder = "print(...)",
                correctFeedback = "¡Bien traducido! De la idea en palabras al código con print.",
                wrongFeedback = "Mostrar un mensaje es la tarea de print, con el texto exacto entre comillas."
            ),
            PredictOutput(
                prompt = "Glitch dejó partes del plan comentadas. ¿Qué muestra este programa?",
                code = "# print(\"Hola\")\nprint(\"Pienso\")\n# print(\"Escribo\")\nprint(\"Ejecuto\")",
                acceptedAnswers = listOf("Pienso\nEjecuto"),
                correctFeedback = "¡Exacto! Las líneas con # son notas: Python se las salta.",
                wrongFeedback = "Las líneas que empiezan con # no se ejecutan. Solo cuentan las otras dos."
            )
        )
    )

    // Jefe del nivel: mezcla lo aprendido en las 11 lecciones.
    val boss = BossContent(
        bossId = "n01_boss",
        name = "El Bug Gigante",
        emoji = "🐛",
        intro = "Se comió media pantalla y no piensa parar. ¡Solo el código bien escrito puede detenerlo!",
        exercises = listOf(
            TypeCode(
                prompt = "El Bug Gigante bloqueó el saludo de Glitch. Escribe la orden para mostrar: ¡Ataque de Glitch!",
                acceptedAnswers = listOf("print(\"¡Ataque de Glitch!\")"),
                placeholder = "print(...)",
                correctFeedback = "¡Golpe directo! Texto entre comillas dentro de un print.",
                wrongFeedback = "Necesitas print, paréntesis y el texto exacto entre comillas, con sus signos de exclamación."
            ),
            OrderLines(
                prompt = "Ordena el plan de ataque de Glitch.",
                lines = listOf(
                    "print(\"Cargar energía\")",
                    "print(\"Apuntar\")",
                    "print(\"¡Disparar!\")",
                    "print(\"Celebrar\")"
                ),
                correctFeedback = "¡Ataque perfecto! Cada paso en su lugar.",
                wrongFeedback = "Piensa qué hay que hacer antes de disparar y qué solo se hace después."
            ),
            FindBug(
                prompt = "El Bug Gigante escondió un error en este programa. ¡Tócalo para golpearlo!",
                codeLines = listOf(
                    "# Plan de batalla",
                    "print(\"Plan listo\")",
                    "print(\"Ataco\"",
                    "print(\"Gané\")"
                ),
                bugLineIndex = 2,
                correctFeedback = "¡Bug golpeado! Faltaba el paréntesis que cierra el print.",
                wrongFeedback = "Revisa que cada paréntesis abierto tenga su cierre. Una línea se quedó incompleta."
            ),
            PredictOutput(
                prompt = "El Bug Gigante lanza este código. ¿Qué muestra? Escríbelo línea por línea.",
                code = "print(\"2 + 3\")\nprint(2 + 3)\nprint(\"Total:\", 2 + 3)",
                acceptedAnswers = listOf("2 + 3\n5\nTotal: 5"),
                correctFeedback = "¡Esquivado! Con comillas se copia, sin comillas se calcula.",
                wrongFeedback = "Revisa las comillas de cada línea: con comillas se copia el texto y sin ellas Python calcula."
            ),
            FillBlanks(
                prompt = "Completa el último plan: una nota que Python ignore y la orden de victoria.",
                codeTemplate = "___ Última nota del plan\n___(\"Victoria\")",
                answers = listOf("#", "print"),
                chips = listOf("#", "print", "//", "imprime"),
                correctFeedback = "¡Bug vencido! Una nota con # y la orden con print.",
                wrongFeedback = "Un comentario empieza con un símbolo concreto y la orden para mostrar texto se llama print, en minúsculas."
            )
        )
    )

    val all: List<LessonContent> = listOf(
        glitchNoLeeMentes, elOrdenImporta, cazadoresDeBugs, divideYVenceras,
        comillas, lineasEnBlanco, glitchHaceCuentas, leerLosErrores,
        dosCosasEnUnPrint, miPrimerPrograma, pensarAntesDeEscribir
    )
}
