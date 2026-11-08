package me.pablorincon.proyecto_app.domain.content

import me.pablorincon.proyecto_app.domain.BossContent
import me.pablorincon.proyecto_app.domain.FillBlanks
import me.pablorincon.proyecto_app.domain.FindBug
import me.pablorincon.proyecto_app.domain.LessonContent
import me.pablorincon.proyecto_app.domain.MultipleChoice
import me.pablorincon.proyecto_app.domain.PredictOutput
import me.pablorincon.proyecto_app.domain.TypeCode

// Nivel 3 · Tipos de datos (11 lecciones y el jefe «El Alquimista de Tipos»), con Glitch como hilo conductor.
internal object Level03Lessons {

    private val losTresFrascos = LessonContent(
        lessonId = "n03_l01",
        title = "Los tres frascos 🧪",
        hook = "En el laboratorio de Glitch hay frascos distintos: uno para palabras, otro para números enteros y otro para decimales. Si mezclas, ¡explota!",
        explanation = "Los datos tienen tipo: texto (str), entero (int) y decimal (float). type() te dice de cuál es un valor.",
        code = "nombre = \"Glitch\"\nedad = 3\naltura = 1.5\nprint(type(nombre))\nprint(type(edad))\nprint(type(altura))",
        expectedOutput = "<class 'str'>\n<class 'int'>\n<class 'float'>",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "print(type(7))\nprint(type(7.0))\nprint(type(\"7\"))",
                acceptedAnswers = listOf("<class 'int'>\n<class 'float'>\n<class 'str'>"),
                correctFeedback = "¡Exacto! 7 es entero, 7.0 es decimal y \"7\" con comillas es texto.",
                wrongFeedback = "Fíjate en cómo está escrito cada 7: sin punto es entero, con punto es decimal y entre comillas es texto."
            ),
            FillBlanks(
                prompt = "Glitch guarda el valor 4.5. ¿De qué tipo es?",
                codeTemplate = "4.5 es de tipo ___",
                answers = listOf("float"),
                chips = listOf("int", "float", "str", "bool"),
                correctFeedback = "¡Sí! Tiene punto decimal, así que es float.",
                wrongFeedback = "Los números con punto decimal pertenecen a un tipo propio, distinto del entero."
            ),
            MultipleChoice(
                prompt = "Glitch guarda a = \"10\", b = 10 y c = 10.5. ¿Cuántas de las tres guardan un número de verdad?",
                options = listOf("1", "2", "3", "0"),
                correctIndex = 1,
                correctFeedback = "¡Correcto! b y c son números; a es texto con apariencia de número.",
                wrongFeedback = "Cuenta solo las que no llevan comillas: a es texto aunque parezca número."
            )
        )
    )

    private val enterosYDecimales = LessonContent(
        lessonId = "n03_l02",
        title = "Enteros y decimales ⚖️",
        hook = "Glitch mide cosas: pasos completos y alturas con decimales. ¿Cómo sabe Python cuál es cuál?",
        explanation = "Un entero (int) no lleva punto; un decimal (float) sí, y se escribe con punto, no con coma. La división / siempre da un decimal, aunque el resultado sea exacto.",
        code = "print(7 / 2)\nprint(8 / 2)\nprint(type(8 / 2))",
        expectedOutput = "3.5\n4.0\n<class 'float'>",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "print(9 / 3)\nprint(9 / 2)",
                acceptedAnswers = listOf("3.0\n4.5"),
                correctFeedback = "¡Bien visto! La división siempre da decimal: 3.0 y 4.5.",
                wrongFeedback = "Recuerda: / siempre devuelve un decimal, incluso cuando la cuenta sale exacta."
            ),
            FindBug(
                prompt = "Glitch quería guardar el decimal 3.2, pero escribió mal una línea. ¿Cuál?",
                codeLines = listOf(
                    "altura = 1.5",
                    "peso = 3,2",
                    "print(altura, peso)"
                ),
                bugLineIndex = 1,
                correctFeedback = "¡Bug cazado! En Python los decimales llevan punto, no coma.",
                wrongFeedback = "Mira cómo están escritos los decimales: en programación se usa el punto para separar los decimales."
            ),
            TypeCode(
                prompt = "Escribe un print que muestre la división de 10 entre 4.",
                acceptedAnswers = listOf("print(10 / 4)"),
                placeholder = "print(...)",
                correctFeedback = "¡Eso! 10 / 4 da el decimal 2.5.",
                wrongFeedback = "Usa print con la división dentro, sin comillas: 10, el símbolo / y 4."
            )
        )
    )

    private val operaciones = LessonContent(
        lessonId = "n03_l03",
        title = "Cuentas y potencias ➕",
        hook = "Glitch ya sabe sumar y restar. Hoy aprende a multiplicar, dividir y elevar a una potencia.",
        explanation = "Operadores: + suma, - resta, * multiplica, / divide y ** eleva a una potencia (2 ** 3 es 2 por 2 por 2).",
        code = "print(6 + 2)\nprint(6 - 2)\nprint(6 * 2)\nprint(6 / 2)\nprint(6 ** 2)",
        expectedOutput = "8\n4\n12\n3.0\n36",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "print(2 ** 3)\nprint(5 * 4)\nprint(5 / 2)",
                acceptedAnswers = listOf("8\n20\n2.5"),
                correctFeedback = "¡Correcto! 2 ** 3 = 8, 5 * 4 = 20 y 5 / 2 = 2.5.",
                wrongFeedback = "** significa potencia: 2 ** 3 es 2 por 2 por 2. Luego la multiplicación y la división."
            ),
            FillBlanks(
                prompt = "Glitch quiere elevar 3 al cuadrado. ¿Qué operador falta?",
                codeTemplate = "print(3 ___ 2)",
                answers = listOf("**"),
                chips = listOf("**", "^", "*", "//"),
                correctFeedback = "¡Sí! ** es la potencia: 3 ** 2 = 9.",
                wrongFeedback = "En Python la potencia se escribe con dos asteriscos. El ^ existe, pero hace otra cosa."
            ),
            TypeCode(
                prompt = "Escribe un print que calcule 4 elevado a 3.",
                acceptedAnswers = listOf("print(4 ** 3)"),
                placeholder = "print(...)",
                correctFeedback = "¡Eso! 4 ** 3 = 64.",
                wrongFeedback = "Usa print y dentro 4, el operador de potencia (dos asteriscos) y 3."
            )
        )
    )

    private val divisionEnteraYResto = LessonContent(
        lessonId = "n03_l04",
        title = "Repartir y lo que sobra ✂️",
        hook = "Glitch reparte 17 galletas entre 5 amigos: ¿cuántas le tocan a cada uno y cuántas sobran?",
        explanation = "// da la división entera (sin decimales) y % da el resto. Con 17 entre 5: 17 // 5 es 3 y 17 % 5 es 2.",
        code = "print(17 // 5)\nprint(17 % 5)",
        expectedOutput = "3\n2",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "print(20 // 6)\nprint(20 % 6)",
                acceptedAnswers = listOf("3\n2"),
                correctFeedback = "¡Exacto! En 20 caben 3 veces el 6 (18) y sobran 2.",
                wrongFeedback = "Piensa en repartir: cuántas veces cabe 6 en 20 (//) y cuánto sobra (%)."
            ),
            FillBlanks(
                prompt = "Quieres saber cuántas galletas sobran al repartir 17 entre 5. ¿Qué operador usas?",
                codeTemplate = "print(17 ___ 5)",
                answers = listOf("%"),
                chips = listOf("%", "//", "/", "**"),
                correctFeedback = "¡Sí! % devuelve el resto de la división.",
                wrongFeedback = "Lo que sobra es el resto, y para eso Python tiene un operador que usa el signo de porcentaje."
            ),
            PredictOutput(
                prompt = "Glitch usa el resto para saber si un número es par (resto 0 al dividir entre 2). ¿Qué muestra?",
                code = "print(8 % 2)\nprint(9 % 2)\nprint(10 % 2)",
                acceptedAnswers = listOf("0\n1\n0"),
                correctFeedback = "¡Muy bien! Los pares dejan resto 0 y los impares dejan resto 1.",
                wrongFeedback = "Divide cada número entre 2 y quédate con lo que sobra: los pares no dejan nada."
            )
        )
    )

    private val ordenDeOperaciones = LessonContent(
        lessonId = "n03_l05",
        title = "El orden de las operaciones 🎯",
        hook = "¿Cuánto es 2 + 3 * 4? Glitch dudó... y Python tiene una regla clara.",
        explanation = "Python resuelve primero los paréntesis, luego las potencias, después * / // %, y al final + y -. Los paréntesis te dejan decidir el orden.",
        code = "print(2 + 3 * 4)\nprint((2 + 3) * 4)\nprint(2 ** 3 * 2)",
        expectedOutput = "14\n20\n16",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "print(10 - 2 * 3)\nprint((10 - 2) * 3)\nprint(2 + 12 / 4)",
                acceptedAnswers = listOf("4\n24\n5.0"),
                correctFeedback = "¡Excelente! 10 - 6 = 4, 8 * 3 = 24 y 2 + 3.0 = 5.0.",
                wrongFeedback = "Primero los paréntesis, luego multiplicar o dividir, y al final sumar o restar."
            ),
            FillBlanks(
                prompt = "Quieres que primero se sume 5 + 3 y luego se multiplique por 2 (resultado 16). ¿Qué símbolos faltan?",
                codeTemplate = "print(___5 + 3___ * 2)",
                answers = listOf("(", ")"),
                chips = listOf("(", ")", "[", "]"),
                correctFeedback = "¡Sí! Los paréntesis hacen que la suma se resuelva primero.",
                wrongFeedback = "Para cambiar el orden se usan paréntesis redondos: uno abre antes de la suma y otro cierra después."
            ),
            FindBug(
                prompt = "Glitch quería el promedio de 8 y 6 (que es 7), pero el resultado sale mal. ¿Qué línea tiene el bug?",
                codeLines = listOf(
                    "a = 8",
                    "b = 6",
                    "promedio = a + b / 2",
                    "print(promedio)"
                ),
                bugLineIndex = 2,
                correctFeedback = "¡Bug cazado! La división se hace antes que la suma: hacen falta paréntesis (a + b) / 2.",
                wrongFeedback = "Piensa qué operación se resuelve primero en a + b / 2 y qué debería pasar antes de dividir."
            )
        )
    )

    private val cambiarDeTipo = LessonContent(
        lessonId = "n03_l06",
        title = "Cambiar de frasco 🔄",
        hook = "Glitch necesita pasar un dato de un frasco a otro: de texto a número, o al revés.",
        explanation = "int() convierte a entero, float() a decimal y str() a texto. int(3.9) corta los decimales y da 3 (no redondea).",
        code = "print(int(\"5\") + 1)\nprint(float(\"2.5\") * 2)\nprint(str(10) + \"!\")",
        expectedOutput = "6\n5.0\n10!",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "print(int(\"7\") + int(\"3\"))\nprint(\"7\" + \"3\")",
                acceptedAnswers = listOf("10\n73"),
                correctFeedback = "¡Exacto! Convertidos a número se suman (10); como texto se unen (73).",
                wrongFeedback = "En la primera línea los textos se convierten a números y se suman; en la segunda siguen siendo texto y se unen."
            ),
            FillBlanks(
                prompt = "Quieres mostrar: Energía: 100 uniendo texto y número. ¿Qué convierte el número a texto?",
                codeTemplate = "print(\"Energía: \" + ___(100))",
                answers = listOf("str"),
                chips = listOf("str", "int", "float", "text"),
                correctFeedback = "¡Sí! str() convierte el número en texto para poder unirlo.",
                wrongFeedback = "Para unir con un texto, el número debe convertirse en texto. Esa función es la de string."
            ),
            PredictOutput(
                prompt = "¿Qué muestra este programa? (int corta los decimales, no redondea)",
                code = "print(int(3.9))\nprint(int(-2.5))\nprint(float(4))",
                acceptedAnswers = listOf("3\n-2\n4.0"),
                correctFeedback = "¡Muy bien! int corta la parte decimal hacia el cero: 3 y -2. float(4) da 4.0.",
                wrongFeedback = "int() no redondea: simplemente corta los decimales. Y float() agrega el .0."
            )
        )
    )

    private val unirTextos = LessonContent(
        lessonId = "n03_l07",
        title = "Unir textos 🧵",
        hook = "Glitch quiere armar frases como si fueran piezas de Lego.",
        explanation = "Con + los textos se pegan uno detrás de otro (concatenar). Si hay un número, primero conviértelo con str(). Los espacios hay que ponerlos tú.",
        code = "nombre = \"Glitch\"\nsaludo = \"Hola, \" + nombre + \"!\"\nprint(saludo)",
        expectedOutput = "Hola, Glitch!",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "a = \"Beep\"\nb = \"boop\"\nprint(a + b)\nprint(a + \" \" + b)",
                acceptedAnswers = listOf("Beepboop\nBeep boop"),
                correctFeedback = "¡Bien visto! + no agrega espacios solo: hay que ponerlos tú.",
                wrongFeedback = "+ pega los textos tal cual. Solo hay espacio donde tú pusiste un \" \"."
            ),
            FindBug(
                prompt = "Este programa falla porque une un texto con un número. ¿En qué línea?",
                codeLines = listOf(
                    "edad = 3",
                    "print(\"Tengo \" + edad + \" años\")",
                    "print(\"Fin\")"
                ),
                bugLineIndex = 1,
                correctFeedback = "¡Bug cazado! No se puede unir texto con un número sin convertirlo antes con str().",
                wrongFeedback = "Busca dónde se usa + entre un texto y una variable que guarda un número."
            ),
            TypeCode(
                prompt = "Con edad = 3 ya creada, escribe un print que muestre: Tengo 3 años (usando + y str).",
                acceptedAnswers = listOf("print(\"Tengo \" + str(edad) + \" años\")"),
                placeholder = "print(...)",
                correctFeedback = "¡Eso! str(edad) convierte el número en texto para poder unirlo.",
                wrongFeedback = "Une \"Tengo \", str(edad) y \" años\" con +, todo dentro de un print."
            )
        )
    )

    private val repetirTextos = LessonContent(
        lessonId = "n03_l08",
        title = "Repetir textos 🔁",
        hook = "Glitch se ríe: jajaja. Pero no quiere escribir ja tres veces a mano.",
        explanation = "Un texto multiplicado por un entero se repite esa cantidad de veces: \"ja\" * 3 da jajaja. Solo funciona con enteros.",
        code = "print(\"ja\" * 3)\nprint(\"-\" * 10)\nprint(\"Hola \" * 2)",
        expectedOutput = "jajaja\n----------\nHola Hola ",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "print(\"ab\" * 3)\nprint(\"=\" * 5)",
                acceptedAnswers = listOf("ababab\n====="),
                correctFeedback = "¡Correcto! El texto se repite tantas veces como indique el número.",
                wrongFeedback = "Multiplicar un texto por 3 lo repite 3 veces, pegado, sin espacios."
            ),
            TypeCode(
                prompt = "Escribe un print que dibuje una línea de 8 guiones (-) usando la multiplicación.",
                acceptedAnswers = listOf("print(\"-\" * 8)"),
                placeholder = "print(...)",
                correctFeedback = "¡Eso! \"-\" * 8 son ocho guiones.",
                wrongFeedback = "Multiplica el texto \"-\" (entre comillas) por 8 dentro del print."
            ),
            FindBug(
                prompt = "Una de estas líneas da error porque el texto se multiplica por un decimal. ¿Cuál?",
                codeLines = listOf(
                    "print(\"ha\" * 3)",
                    "print(\"ha\" * 2.5)",
                    "print(\"ha\" * 2)"
                ),
                bugLineIndex = 1,
                correctFeedback = "¡Bug cazado! Un texto solo se puede repetir un número entero de veces.",
                wrongFeedback = "Repetir 2.5 veces no tiene sentido. Busca el multiplicador que no es entero."
            )
        )
    )

    private val verdaderoOFalso = LessonContent(
        lessonId = "n03_l09",
        title = "Verdadero o falso ✅",
        hook = "Glitch responde preguntas con solo dos respuestas posibles. Son los booleanos, el cuarto frasco.",
        explanation = "Un booleano (bool) vale True o False, siempre con la primera letra en mayúscula. Las comparaciones dan booleanos.",
        code = "print(5 > 3)\nprint(2 == 3)\nprint(type(True))",
        expectedOutput = "True\nFalse\n<class 'bool'>",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "print(10 > 20)\nprint(7 == 7)\nprint(3 != 3)",
                acceptedAnswers = listOf("False\nTrue\nFalse"),
                correctFeedback = "¡Exacto! 10 no es mayor que 20, 7 sí es igual a 7 y 3 no es distinto de 3.",
                wrongFeedback = "Responde cada comparación: > es mayor que, == es igual que y != es distinto de."
            ),
            FillBlanks(
                prompt = "¿Cuál de estas es la forma correcta de escribir un booleano verdadero en Python?",
                codeTemplate = "El valor correcto es ___",
                answers = listOf("True"),
                chips = listOf("True", "true", "TRUE", "verdadero"),
                correctFeedback = "¡Sí! Siempre True, con la T mayúscula y el resto en minúsculas.",
                wrongFeedback = "Python distingue mayúsculas y no entiende español: el valor se escribe con inicial mayúscula."
            ),
            TypeCode(
                prompt = "Guarda en una variable llamada listo el valor verdadero.",
                acceptedAnswers = listOf("listo = True"),
                placeholder = "listo = ...",
                correctFeedback = "¡Eso! Una variable puede guardar un booleano como cualquier otro dato.",
                wrongFeedback = "Es como cualquier variable: nombre, =, y el valor True escrito exactamente así."
            )
        )
    )

    private val herramientasRapidas = LessonContent(
        lessonId = "n03_l10",
        title = "Herramientas rápidas 🧰",
        hook = "Glitch encuentra una caja de herramientas: funciones que ya vienen hechas en Python.",
        explanation = "abs() quita el signo, round(x, n) redondea a n decimales, max() da el mayor y min() el menor de varios valores.",
        code = "print(abs(-5))\nprint(round(3.14159, 2))\nprint(max(3, 9, 4))\nprint(min(3, 9, 4))",
        expectedOutput = "5\n3.14\n9\n3",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "print(abs(-12))\nprint(max(2, 8, 5))\nprint(min(2, 8, 5))\nprint(round(7.456, 1))",
                acceptedAnswers = listOf("12\n8\n2\n7.5"),
                correctFeedback = "¡Perfecto! abs quita el signo, max y min eligen, y round deja un decimal.",
                wrongFeedback = "Resuelve cada herramienta: abs quita el signo, max elige el mayor, min el menor y round redondea."
            ),
            FillBlanks(
                prompt = "Glitch quiere mostrar el valor sin signo de -7. ¿Qué función usa?",
                codeTemplate = "print(___(-7))",
                answers = listOf("abs"),
                chips = listOf("abs", "max", "round", "min"),
                correctFeedback = "¡Sí! abs devuelve el valor absoluto: 7.",
                wrongFeedback = "Una de las funciones sirve para quitarle el signo a un número. Su nombre es una abreviatura de absoluto."
            ),
            TypeCode(
                prompt = "Escribe un print que muestre el mayor entre 4, 17 y 9.",
                acceptedAnswers = listOf("print(max(4, 17, 9))"),
                placeholder = "print(...)",
                correctFeedback = "¡Eso! max devuelve el mayor de todos: 17.",
                wrongFeedback = "Usa la función que elige el mayor, con los tres números separados por comas."
            )
        )
    )

    private val mezclandoTipos = LessonContent(
        lessonId = "n03_l11",
        title = "Mezclando tipos 🧬",
        hook = "¿Qué pasa si Glitch mezcla un entero con un decimal? Python tiene un criterio claro.",
        explanation = "Entero con decimal da decimal (1 + 2.0 es 3.0). La división / siempre da decimal. La división entera // entre enteros da entero.",
        code = "print(2 + 1.5)\nprint(3 * 2.0)\nprint(type(4 / 2))",
        expectedOutput = "3.5\n6.0\n<class 'float'>",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "print(1 + 2)\nprint(1 + 2.0)\nprint(6 / 3)",
                acceptedAnswers = listOf("3\n3.0\n2.0"),
                correctFeedback = "¡Muy bien! Dos enteros dan entero, pero con un decimal el resultado es decimal.",
                wrongFeedback = "Si en la cuenta hay algún decimal el resultado es decimal, y la división / siempre da decimal."
            ),
            MultipleChoice(
                prompt = "¿Qué tipo tiene el resultado de 10 // 2?",
                options = listOf("int", "float", "str", "bool"),
                correctIndex = 0,
                correctFeedback = "¡Sí! La división entera entre enteros da un entero: 5.",
                wrongFeedback = "A diferencia de /, el operador // devuelve un entero cuando se usa con enteros."
            ),
            FindBug(
                prompt = "¿Qué línea guarda un texto en vez de un número?",
                codeLines = listOf(
                    "a = 5",
                    "b = \"3\"",
                    "c = 2.5"
                ),
                bugLineIndex = 1,
                correctFeedback = "¡Bug cazado! Las comillas convierten el 3 en texto.",
                wrongFeedback = "Busca el valor que lleva comillas: ese es un texto, aunque parezca número."
            )
        )
    )

    // Jefe del nivel: mezcla lo aprendido en las 11 lecciones.
    val boss = BossContent(
        bossId = "n03_boss",
        name = "El Alquimista de Tipos",
        emoji = "⚗️",
        intro = "Mezcla frascos sin avisar: enteros, decimales y textos. ¡Solo quien conoce cada tipo evita la explosión!",
        exercises = listOf(
            PredictOutput(
                prompt = "El Alquimista mezcla tres cuentas. ¿Qué muestra este código?",
                code = "print(7 // 2)\nprint(7 % 2)\nprint(7 / 2)",
                acceptedAnswers = listOf("3\n1\n3.5"),
                correctFeedback = "¡Frasco esquivado! Cociente 3, resto 1 y división decimal 3.5.",
                wrongFeedback = "// es la división entera, % es el resto y / es la división con decimales."
            ),
            PredictOutput(
                prompt = "Ahora mezcla textos y números. ¿Qué muestra?",
                code = "print(\"3\" + \"4\")\nprint(3 + 4)\nprint(\"3\" * 3)",
                acceptedAnswers = listOf("34\n7\n333"),
                correctFeedback = "¡Golpe certero! Textos se unen, números se suman y un texto por 3 se repite.",
                wrongFeedback = "Las comillas cambian todo: con textos + une y * repite; con números + suma."
            ),
            FindBug(
                prompt = "El Alquimista intentó unir un texto con un número. ¡Tócalo!",
                codeLines = listOf(
                    "puntos = 10",
                    "print(\"Puntos: \" + puntos)",
                    "print(\"Fin\")"
                ),
                bugLineIndex = 1,
                correctFeedback = "¡Bug golpeado! Sin str() no se puede unir un texto con un número.",
                wrongFeedback = "Busca dónde se usa + entre un texto y una variable numérica."
            ),
            FillBlanks(
                prompt = "Completa para que el código muestre 6 sumando texto convertido a número.",
                codeTemplate = "print(___(\"5\") + 1)",
                answers = listOf("int"),
                chips = listOf("int", "str", "float", "type"),
                correctFeedback = "¡Transmutación lograda! int(\"5\") es el entero 5, y 5 + 1 = 6.",
                wrongFeedback = "Para que el resultado sea exactamente 6 (sin .0), el texto debe convertirse a entero."
            ),
            TypeCode(
                prompt = "Último ataque: escribe un print que muestre el resto de dividir 29 entre 7.",
                acceptedAnswers = listOf("print(29 % 7)"),
                placeholder = "print(...)",
                correctFeedback = "¡Alquimista vencido! 29 % 7 = 1.",
                wrongFeedback = "El resto de una división se calcula con el operador de porcentaje (%)."
            )
        )
    )

    val all: List<LessonContent> = listOf(
        losTresFrascos, enterosYDecimales, operaciones, divisionEnteraYResto,
        ordenDeOperaciones, cambiarDeTipo, unirTextos, repetirTextos,
        verdaderoOFalso, herramientasRapidas, mezclandoTipos
    )
}
