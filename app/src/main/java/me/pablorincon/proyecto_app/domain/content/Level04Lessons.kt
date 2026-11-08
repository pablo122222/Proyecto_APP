package me.pablorincon.proyecto_app.domain.content

import me.pablorincon.proyecto_app.domain.BossContent
import me.pablorincon.proyecto_app.domain.FillBlanks
import me.pablorincon.proyecto_app.domain.FindBug
import me.pablorincon.proyecto_app.domain.LessonContent
import me.pablorincon.proyecto_app.domain.MultipleChoice
import me.pablorincon.proyecto_app.domain.OrderLines
import me.pablorincon.proyecto_app.domain.PredictOutput
import me.pablorincon.proyecto_app.domain.TypeCode

// Nivel 4 · Entrada y salida (11 lecciones y el jefe «El Eco Gigante»), con Glitch como hilo conductor.
internal object Level04Lessons {

    private val preguntarConInput = LessonContent(
        lessonId = "n04_l01",
        title = "Preguntar con input 🎤",
        hook = "Hasta ahora Glitch solo hablaba. Hoy aprende a escuchar: ¡tu programa va a conversar!",
        explanation = "input(\"pregunta\") muestra la pregunta, espera lo que escribe el usuario y lo devuelve. Se guarda en una variable para usarlo después. La salida de abajo es la que verías si escribes Ana.",
        code = "nombre = input(\"¿Cómo te llamas? \")\nprint(\"Hola,\", nombre)",
        expectedOutput = "¿Cómo te llamas? Ana\nHola, Ana",
        exercises = listOf(
            PredictOutput(
                prompt = "El usuario escribe azul. Escribe la salida completa (la pregunta con lo que escribió y la línea del print).",
                code = "color = input(\"Color favorito: \")\nprint(\"Tu color es\", color)",
                acceptedAnswers = listOf("Color favorito: azul\nTu color es azul"),
                correctFeedback = "¡Exacto! La pregunta queda con lo que escribe el usuario, y luego sale el print.",
                wrongFeedback = "Primero aparece la pregunta de input (seguida de lo que escribe el usuario) y después lo que muestra el print."
            ),
            FillBlanks(
                prompt = "Glitch pregunta tu nombre y lo guarda. ¿Qué función falta?",
                codeTemplate = "nombre = ___(\"¿Cómo te llamas? \")",
                answers = listOf("input"),
                chips = listOf("input", "print", "int", "ask"),
                correctFeedback = "¡Sí! input pregunta y devuelve lo que se escribe.",
                wrongFeedback = "Se necesita la función que pide un dato al usuario. No es print, que solo muestra."
            ),
            TypeCode(
                prompt = "Escribe la línea que pregunta ¿Cuál es tu ciudad? y guarda la respuesta en una variable llamada ciudad.",
                acceptedAnswers = listOf("ciudad = input(\"¿Cuál es tu ciudad? \")"),
                placeholder = "ciudad = ...",
                correctFeedback = "¡Eso! Variable, =, input y la pregunta entre comillas.",
                wrongFeedback = "Guarda con ciudad = y pide el dato con input(\"...\"), con la pregunta exacta entre comillas."
            )
        )
    )

    private val inputDevuelveTexto = LessonContent(
        lessonId = "n04_l02",
        title = "input siempre devuelve texto 🔤",
        hook = "Glitch pregunta la edad, el usuario escribe 20... y Glitch no puede sumarle 1. ¿Por qué?",
        explanation = "input() siempre devuelve texto, aunque el usuario escriba un número. Para usarlo como número hay que convertirlo con int() (entero) o float() (decimal).",
        code = "edad = input(\"Edad: \")\nprint(type(edad))",
        expectedOutput = "Edad: 20\n<class 'str'>",
        exercises = listOf(
            PredictOutput(
                prompt = "El usuario escribe 4. ¿Qué muestra la última línea?",
                code = "x = input(\"x: \")\nprint(x + x)",
                acceptedAnswers = listOf("44"),
                correctFeedback = "¡Exacto! x es el texto \"4\", y sumar textos los une: 44.",
                wrongFeedback = "input devuelve texto. Y con textos, + los pega uno detrás de otro."
            ),
            FillBlanks(
                prompt = "Quieres guardar la edad como número entero. ¿Qué función falta?",
                codeTemplate = "edad = ___(input(\"Edad: \"))",
                answers = listOf("int"),
                chips = listOf("int", "str", "float", "type"),
                correctFeedback = "¡Sí! int() convierte el texto escrito en un entero.",
                wrongFeedback = "Para una edad entera necesitas convertir el texto a número entero."
            ),
            FindBug(
                prompt = "Python dice TypeError en la segunda línea. ¿Qué línea hay que arreglar para que edad sea un número?",
                codeLines = listOf(
                    "edad = input(\"Edad: \")",
                    "siguiente = edad + 1",
                    "print(siguiente)"
                ),
                bugLineIndex = 0,
                correctFeedback = "¡Bug cazado! El problema nace en el input: edad es texto y hay que convertirlo con int().",
                wrongFeedback = "La línea 2 solo sufre el problema. El origen es dónde se crea edad: ahí es texto."
            )
        )
    )

    private val cuentasConEntrada = LessonContent(
        lessonId = "n04_l03",
        title = "Cuentas con lo que escribe el usuario ➗",
        hook = "Glitch quiere ser calculadora: que el usuario ponga los números y él haga el resto.",
        explanation = "Convierte cada dato con int() o float() y haz tus cuentas. Usa float() si pueden venir decimales.",
        code = "a = int(input(\"Primer número: \"))\nb = int(input(\"Segundo número: \"))\nprint(\"Suma:\", a + b)",
        expectedOutput = "Primer número: 4\nSegundo número: 6\nSuma: 10",
        exercises = listOf(
            PredictOutput(
                prompt = "El usuario escribe 7 y luego 3. Escribe solo lo que muestran los dos print (sin las preguntas).",
                code = "a = int(input(\"a: \"))\nb = int(input(\"b: \"))\nprint(a * b)\nprint(a - b)",
                acceptedAnswers = listOf("21\n4"),
                correctFeedback = "¡Perfecto! 7 * 3 = 21 y 7 - 3 = 4.",
                wrongFeedback = "a vale 7 y b vale 3. El primer print multiplica y el segundo resta."
            ),
            FillBlanks(
                prompt = "El precio puede traer decimales (como 2.5). ¿Qué función falta?",
                codeTemplate = "precio = ___(input(\"Precio: \"))",
                answers = listOf("float"),
                chips = listOf("float", "int", "str", "round"),
                correctFeedback = "¡Sí! float() acepta decimales; int() fallaría con 2.5.",
                wrongFeedback = "Si el dato puede tener decimales, necesitas la conversión a decimal."
            ),
            TypeCode(
                prompt = "Escribe la línea que pide un número entero con la pregunta Edad: y lo guarda en edad.",
                acceptedAnswers = listOf("edad = int(input(\"Edad: \"))"),
                placeholder = "edad = ...",
                correctFeedback = "¡Eso! input pide, int convierte y edad guarda.",
                wrongFeedback = "Envuelve el input con int(...) para convertir lo escrito en entero y guárdalo con edad =."
            )
        )
    )

    private val fStrings = LessonContent(
        lessonId = "n04_l04",
        title = "Frases con huecos: f-strings ✨",
        hook = "Glitch se cansó de pegar textos con comas y +. Descubre una forma mucho más cómoda.",
        explanation = "Una f antes de las comillas permite poner variables entre llaves dentro del texto: f\"Hola {nombre}\". No hace falta str() ni comas. Sin la f, las llaves se muestran tal cual.",
        code = "nombre = \"Glitch\"\nedad = 3\nprint(f\"Soy {nombre} y tengo {edad} años\")",
        expectedOutput = "Soy Glitch y tengo 3 años",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "pais = \"Chile\"\nn = 2\nprint(f\"{pais} tiene {n} robots\")\nprint(\"{pais}\")",
                acceptedAnswers = listOf("Chile tiene 2 robots\n{pais}"),
                correctFeedback = "¡Bien visto! Con la f las llaves se reemplazan; sin ella se muestran tal cual.",
                wrongFeedback = "Fíjate en la letra f: solo el primer print la tiene, y solo allí se reemplazan las llaves."
            ),
            FillBlanks(
                prompt = "¿Qué letra va antes de las comillas para poder usar llaves con variables?",
                codeTemplate = "nombre = \"Ana\"\nprint(___\"Hola {nombre}\")",
                answers = listOf("f"),
                chips = listOf("f", "s", "r", "x"),
                correctFeedback = "¡Sí! La f viene de formato: activa las llaves.",
                wrongFeedback = "Las frases con llaves se llaman f-strings, por la letra que llevan al principio."
            ),
            TypeCode(
                prompt = "Con las variables robot y energia ya creadas, escribe un print con f-string que muestre el robot, la palabra «tiene» y la energia.",
                acceptedAnswers = listOf("print(f\"{robot} tiene {energia}\")"),
                placeholder = "print(f\"...\")",
                correctFeedback = "¡Eso! Dos variables en llaves y la palabra en medio, todo en una frase.",
                wrongFeedback = "Empieza con print(f\" y usa {robot}, la palabra tiene y {energia} dentro del texto."
            )
        )
    )

    private val fStringsConCuentas = LessonContent(
        lessonId = "n04_l05",
        title = "f-strings con cuentas y formato 🧮",
        hook = "Dentro de las llaves también se puede calcular y hasta decidir cuántos decimales mostrar.",
        explanation = "Entre llaves puedes poner una cuenta: {a + b}. Con :.2f muestras un decimal con 2 cifras: {precio:.2f}.",
        code = "precio = 3.14159\nprint(f\"Precio: {precio:.2f}\")\nprint(f\"Doble: {precio * 2:.1f}\")",
        expectedOutput = "Precio: 3.14\nDoble: 6.3",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "a = 4\nb = 5\nprint(f\"{a} + {b} = {a + b}\")",
                acceptedAnswers = listOf("4 + 5 = 9"),
                correctFeedback = "¡Exacto! Las llaves muestran a, b y el resultado de la suma.",
                wrongFeedback = "Cada llave se reemplaza por su valor; la última calcula a + b."
            ),
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "x = 2.71828\nprint(f\"{x:.2f}\")\nprint(f\"{x:.1f}\")",
                acceptedAnswers = listOf("2.72\n2.7"),
                correctFeedback = "¡Muy bien! :.2f redondea a 2 decimales y :.1f a uno.",
                wrongFeedback = "El número después del punto indica cuántos decimales mostrar, y se redondea."
            ),
            FillBlanks(
                prompt = "Quieres mostrar el precio con 2 decimales. ¿Qué va después de los dos puntos?",
                codeTemplate = "print(f\"{precio:___}\")",
                answers = listOf(".2f"),
                chips = listOf(".2f", "2f", ".2", "f2"),
                correctFeedback = "¡Sí! .2f significa: 2 decimales, formato decimal.",
                wrongFeedback = "El formato lleva primero un punto, luego la cantidad de decimales y al final la letra f."
            )
        )
    )

    private val separadores = LessonContent(
        lessonId = "n04_l06",
        title = "Separadores con sep 🧩",
        hook = "Glitch quiere la fecha con guiones y no con espacios. print tiene un truco para eso.",
        explanation = "Cuando print muestra varias cosas separadas por comas, las separa con un espacio. Con sep=\"...\" eliges otro separador (o ninguno con sep=\"\").",
        code = "print(\"2024\", \"11\", \"08\", sep=\"-\")\nprint(\"Glitch\", \"Bot\", sep=\"\")\nprint(1, 2, 3, sep=\" + \")",
        expectedOutput = "2024-11-08\nGlitchBot\n1 + 2 + 3",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "print(\"a\", \"b\", \"c\", sep=\"*\")\nprint(\"x\", \"y\", sep=\"\")",
                acceptedAnswers = listOf("a*b*c\nxy"),
                correctFeedback = "¡Exacto! El sep va entre cada par de elementos, y sep=\"\" los junta sin nada.",
                wrongFeedback = "sep se coloca entre cada elemento. Con un texto vacío, los elementos quedan pegados."
            ),
            FillBlanks(
                prompt = "Quieres mostrar 10/05/2025 con barras. ¿Qué parámetro cambia el separador?",
                codeTemplate = "print(\"10\", \"05\", \"2025\", ___=\"/\")",
                answers = listOf("sep"),
                chips = listOf("sep", "end", "split", "join"),
                correctFeedback = "¡Sí! sep es el separador entre los elementos.",
                wrongFeedback = "Hay un parámetro de print cuyo nombre es la abreviatura de separador."
            ),
            TypeCode(
                prompt = "Escribe un print que muestre 1-2-3 (los números 1, 2 y 3 separados por guiones) usando sep.",
                acceptedAnswers = listOf("print(1, 2, 3, sep=\"-\")", "print(\"1\", \"2\", \"3\", sep=\"-\")"),
                placeholder = "print(...)",
                correctFeedback = "¡Eso! Tres valores y sep=\"-\" para separarlos con guiones.",
                wrongFeedback = "Pon los tres valores separados por comas y al final sep=\"-\"."
            )
        )
    )

    private val terminarConEnd = LessonContent(
        lessonId = "n04_l07",
        title = "Terminar sin salto con end 🚫",
        hook = "Normalmente print salta de línea al terminar. Glitch quiere seguir escribiendo en la misma.",
        explanation = "Con end=\"...\" eliges qué se escribe al final del print. Con end=\" \" o end=\"\" la siguiente salida continúa en la misma línea.",
        code = "print(\"Cargando\", end=\"...\")\nprint(\"listo\")\nprint(\"A\", end=\" \")\nprint(\"B\")",
        expectedOutput = "Cargando...listo\nA B",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "print(\"Uno\", end=\" \")\nprint(\"Dos\", end=\" \")\nprint(\"Tres\")",
                acceptedAnswers = listOf("Uno Dos Tres"),
                correctFeedback = "¡Correcto! Todo queda en una sola línea separado por espacios.",
                wrongFeedback = "Los dos primeros print terminan con un espacio y no con salto de línea, así que todo sigue en la misma línea."
            ),
            FillBlanks(
                prompt = "Quieres que el siguiente print siga en la misma línea. ¿Qué parámetro usas?",
                codeTemplate = "print(\"Hola\", ___=\" \")",
                answers = listOf("end"),
                chips = listOf("end", "sep", "stop", "next"),
                correctFeedback = "¡Sí! end controla cómo termina el print (por defecto, con un salto de línea).",
                wrongFeedback = "El parámetro se llama igual que la palabra en inglés para «final»."
            ),
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "print(\"a\", end=\"-\")\nprint(\"b\", end=\"-\")\nprint(\"c\")",
                acceptedAnswers = listOf("a-b-c"),
                correctFeedback = "¡Exacto! Cada print termina con un guion, excepto el último, que salta de línea.",
                wrongFeedback = "El end de cada print se escribe al final de su salida: los dos primeros terminan con un guion."
            )
        )
    )

    private val caracteresEspeciales = LessonContent(
        lessonId = "n04_l08",
        title = "Caracteres especiales 🎭",
        hook = "¿Cómo escribe Glitch comillas dentro de un texto, o un salto de línea? Con la barra invertida.",
        explanation = "La barra invertida (\\) crea caracteres especiales: \\n es un salto de línea, \\\" una comilla dentro del texto y \\\\ una barra.",
        code = "print(\"Línea 1\\nLínea 2\")\nprint(\"Dijo \\\"hola\\\"\")\nprint(\"Ruta: C:\\\\datos\")",
        expectedOutput = "Línea 1\nLínea 2\nDijo \"hola\"\nRuta: C:\\datos",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "print(\"A\\nB\\nC\")",
                acceptedAnswers = listOf("A\nB\nC"),
                correctFeedback = "¡Exacto! Cada \\n parte la salida en una nueva línea.",
                wrongFeedback = "Cada \\n dentro del texto es un salto de línea: A, B y C quedan en líneas separadas."
            ),
            FillBlanks(
                prompt = "¿Qué secuencia produce un salto de línea dentro de un texto?",
                codeTemplate = "print(\"Hola___Glitch\")",
                answers = listOf("\\n"),
                chips = listOf("\\n", "/n", "\\t", "\\\\"),
                correctFeedback = "¡Sí! \\n es la secuencia del salto de línea.",
                wrongFeedback = "Es una barra invertida (la que se inclina hacia la derecha arriba) seguida de una letra n."
            ),
            TypeCode(
                prompt = "Escribe un print que muestre: Dijo \"hola\" (con las comillas dobles). Puedes escapar las comillas o cambiar el tipo de comillas de afuera.",
                acceptedAnswers = listOf("print(\"Dijo \\\"hola\\\"\")", "print('Dijo \"hola\"')"),
                placeholder = "print(...)",
                correctFeedback = "¡Eso! Las comillas dentro del texto se escapan con \\ o se usan comillas distintas por fuera.",
                wrongFeedback = "Si usas comillas dobles por fuera, las de adentro necesitan \\\" delante. O usa comillas simples por fuera."
            )
        )
    )

    private val calculadoraDePropinas = LessonContent(
        lessonId = "n04_l09",
        title = "Calculadora de propinas 💸",
        hook = "Glitch trabaja de mesero y quiere calcular la propina de cada cuenta al instante.",
        explanation = "Reto práctico: pide un dato, conviértelo a número, calcula y muestra el resultado con formato. La salida de abajo es la que verías si escribes 50.",
        code = "cuenta = float(input(\"Total de la cuenta: \"))\npropina = cuenta * 0.10\nprint(f\"Propina: {propina:.2f}\")\nprint(f\"Total: {cuenta + propina:.2f}\")",
        expectedOutput = "Total de la cuenta: 50\nPropina: 5.00\nTotal: 55.00",
        exercises = listOf(
            PredictOutput(
                prompt = "El usuario escribe 20. Escribe solo lo que muestran los dos print.",
                code = "cuenta = float(input(\"Cuenta: \"))\npropina = cuenta * 0.10\nprint(f\"Propina: {propina:.2f}\")\nprint(f\"Total: {cuenta + propina:.2f}\")",
                acceptedAnswers = listOf("Propina: 2.00\nTotal: 22.00"),
                correctFeedback = "¡Perfecto! 10 % de 20 es 2.00 y el total es 22.00.",
                wrongFeedback = "Calcula 20 * 0.10 para la propina y súmala a la cuenta. Recuerda los 2 decimales."
            ),
            OrderLines(
                prompt = "Ordena el programa para que calcule y muestre la propina.",
                lines = listOf(
                    "cuenta = float(input(\"Total: \"))",
                    "propina = cuenta * 0.10",
                    "print(f\"Propina: {propina}\")"
                ),
                correctFeedback = "¡Programa listo! Pide, calcula y muestra, en ese orden.",
                wrongFeedback = "No puedes calcular la propina sin tener la cuenta, ni mostrarla antes de calcularla."
            ),
            FindBug(
                prompt = "Python dice TypeError al calcular la propina. ¿Qué línea hay que arreglar para que cuenta sea un número?",
                codeLines = listOf(
                    "cuenta = input(\"Total: \")",
                    "propina = cuenta * 0.10",
                    "print(propina)"
                ),
                bugLineIndex = 0,
                correctFeedback = "¡Bug cazado! cuenta es texto: hay que convertirla con float() al leerla.",
                wrongFeedback = "El error aparece al multiplicar, pero la causa está en la línea que crea cuenta."
            )
        )
    )

    private val cuandoEscribenCualquierCosa = LessonContent(
        lessonId = "n04_l10",
        title = "Cuando escriben cualquier cosa 🙈",
        hook = "¿Y si Glitch pide la edad y el usuario escribe «veinte»? Python se queja con un error muy particular.",
        explanation = "int() y float() solo convierten textos que parecen números. Si el texto no sirve, Python lanza un ValueError. int(\"3.5\") también falla, porque 3.5 no es un entero.",
        code = "edad = int(input(\"Edad: \"))",
        expectedOutput = "Edad: veinte\nValueError: invalid literal for int() with base 10: 'veinte'",
        exercises = listOf(
            PredictOutput(
                prompt = "El usuario escribe hola en int(input(\"N: \")). Escribe solo el nombre del error que aparece.",
                code = "n = int(input(\"N: \"))",
                acceptedAnswers = listOf("ValueError"),
                correctFeedback = "¡Exacto! ValueError: el valor no se puede convertir al tipo pedido.",
                wrongFeedback = "El texto «hola» no es un valor válido para un entero. El error se llama como eso: error de valor."
            ),
            MultipleChoice(
                prompt = "¿Cuál de estas entradas hace fallar a int(input())?",
                options = listOf("42", "-7", "3.5", "007"),
                correctIndex = 2,
                correctFeedback = "¡Muy bien! 3.5 tiene punto decimal y no es un entero válido para int().",
                wrongFeedback = "Los enteros pueden llevar signo o ceros delante. Busca la entrada que no es un número entero."
            ),
            FindBug(
                prompt = "Una de estas líneas siempre da ValueError. ¿Cuál?",
                codeLines = listOf(
                    "a = int(input(\"a: \"))",
                    "b = float(input(\"b: \"))",
                    "c = int(\"12.5\")"
                ),
                bugLineIndex = 2,
                correctFeedback = "¡Bug cazado! int(\"12.5\") falla porque 12.5 tiene decimales.",
                wrongFeedback = "Las dos primeras dependen de lo que escriba el usuario. Una tercera siempre falla: convierte a entero un texto con decimales."
            )
        )
    )

    private val tarjetaDePresentacion = LessonContent(
        lessonId = "n04_l11",
        title = "Tarjeta de presentación 🪪",
        hook = "Glitch quiere imprimir su tarjeta con marco incluido. ¡Es el momento de juntar todo el nivel!",
        explanation = "Mezcla input, int, f-strings y la repetición de textos para armar un programa completo. La salida de abajo es la que verías si escribes Ana y 30.",
        code = "nombre = input(\"Nombre: \")\nedad = int(input(\"Edad: \"))\nprint(\"=\" * 20)\nprint(f\"Nombre: {nombre}\")\nprint(f\"Edad: {edad}\")\nprint(\"=\" * 20)",
        expectedOutput = "Nombre: Ana\nEdad: 30\n====================\nNombre: Ana\nEdad: 30\n====================",
        exercises = listOf(
            PredictOutput(
                prompt = "El usuario escribe Ana y 30. Escribe solo lo que muestran los print (el marco tiene 20 signos =).",
                code = "nombre = input(\"Nombre: \")\nedad = int(input(\"Edad: \"))\nprint(\"=\" * 20)\nprint(f\"Nombre: {nombre}\")\nprint(f\"Edad: {edad}\")\nprint(\"=\" * 20)",
                acceptedAnswers = listOf("====================\nNombre: Ana\nEdad: 30\n===================="),
                correctFeedback = "¡Tarjeta perfecta! Marco, nombre, edad y marco otra vez.",
                wrongFeedback = "Las líneas de = se repiten arriba y abajo, y en medio van el nombre y la edad."
            ),
            TypeCode(
                prompt = "Escribe el print que dibuja una línea de 20 signos igual (=) usando la multiplicación.",
                acceptedAnswers = listOf("print(\"=\" * 20)"),
                placeholder = "print(...)",
                correctFeedback = "¡Eso! \"=\" * 20 son veinte signos iguales.",
                wrongFeedback = "Multiplica el texto \"=\" por 20 dentro del print."
            ),
            FindBug(
                prompt = "Python da error al calcular edad + 5. ¿Qué línea hay que arreglar?",
                codeLines = listOf(
                    "nombre = input(\"Nombre: \")",
                    "edad = input(\"Edad: \")",
                    "print(f\"En 5 años tendrás {edad + 5}\")"
                ),
                bugLineIndex = 1,
                correctFeedback = "¡Bug cazado! edad es texto: hay que convertirla con int() al leerla.",
                wrongFeedback = "El problema nace donde se crea edad: todo lo que devuelve input es texto."
            )
        )
    )

    // Jefe del nivel: mezcla lo aprendido en las 11 lecciones.
    val boss = BossContent(
        bossId = "n04_boss",
        name = "El Eco Gigante",
        emoji = "🔊",
        intro = "Repite todo lo que dices, pero distorsionado. ¡Domina input, print y las f-strings para dejarlo sin voz!",
        exercises = listOf(
            PredictOutput(
                prompt = "El usuario escribe 5 y luego 4. ¿Qué muestra el print?",
                code = "a = int(input(\"a: \"))\nb = int(input(\"b: \"))\nprint(f\"{a} x {b} = {a * b}\")",
                acceptedAnswers = listOf("5 x 4 = 20"),
                correctFeedback = "¡Eco apagado! 5 x 4 = 20.",
                wrongFeedback = "Cada llave se reemplaza por su valor, y la última calcula a * b."
            ),
            PredictOutput(
                prompt = "El Eco lanza este código. ¿Qué muestra? Escríbelo línea por línea.",
                code = "print(\"A\", \"B\", \"C\", sep=\"\")\nprint(\"D\", end=\"!\")\nprint(\"E\")",
                acceptedAnswers = listOf("ABC\nD!E"),
                correctFeedback = "¡Golpe certero! sep vacío junta las letras y end=\"!\" deja la E pegada.",
                wrongFeedback = "El primer print no pone separadores. El segundo termina con ! en vez de salto de línea."
            ),
            FindBug(
                prompt = "El Eco escondió un error: el saludo no usa la variable. ¡Tócalo!",
                codeLines = listOf(
                    "nombre = input(\"Nombre: \")",
                    "print(\"Hola, {nombre}\")",
                    "print(\"Fin\")"
                ),
                bugLineIndex = 1,
                correctFeedback = "¡Bug golpeado! Sin la f, las llaves se muestran tal cual.",
                wrongFeedback = "Fíjate en el print que tiene llaves: ¿qué le falta antes de las comillas?"
            ),
            FillBlanks(
                prompt = "Completa para mostrar el precio con 2 decimales.",
                codeTemplate = "precio = 9.876\nprint(f\"{precio:___}\")",
                answers = listOf(".2f"),
                chips = listOf(".2f", "2f", ".2", "f2"),
                correctFeedback = "¡Voz cortada! .2f muestra 9.88.",
                wrongFeedback = "El formato es: punto, cantidad de decimales y la letra f."
            ),
            TypeCode(
                prompt = "Último ataque: escribe la línea que pide un decimal con la pregunta Altura: y lo guarda en altura.",
                acceptedAnswers = listOf("altura = float(input(\"Altura: \"))"),
                placeholder = "altura = ...",
                correctFeedback = "¡Eco vencido! input pide, float convierte y altura guarda.",
                wrongFeedback = "Envuelve input(\"Altura: \") con float(...) y guárdalo en altura."
            )
        )
    )

    val all: List<LessonContent> = listOf(
        preguntarConInput, inputDevuelveTexto, cuentasConEntrada, fStrings,
        fStringsConCuentas, separadores, terminarConEnd, caracteresEspeciales,
        calculadoraDePropinas, cuandoEscribenCualquierCosa, tarjetaDePresentacion
    )
}
