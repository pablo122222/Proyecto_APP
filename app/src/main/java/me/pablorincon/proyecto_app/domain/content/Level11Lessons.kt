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

// Nivel 11 · Textos (11 lecciones y el jefe «El Descifrador de Mensajes»): los textos como
// filas de letras, índices y rebanadas, inmutabilidad, métodos útiles, split/join y recorrerlos.
internal object Level11Lessons {

    private fun step(line: Int, note: String, vars: List<Pair<String, String>>, out: List<String> = emptyList()) =
        WalkStep(line, note, vars, out)

    private val filaDeLetras = LessonContent(
        lessonId = "n11_l01",
        title = "Un texto es una fila de letras 🔤",
        hook = "Glitch mira un texto con lupa y descubre que es como una lista: una fila de letras, una detrás de otra.",
        explanation = "Un texto (str) es una secuencia de caracteres. Por eso len también funciona con textos: cuenta cuántos caracteres tiene. ¡Ojo! Los espacios y los signos también cuentan. Un texto vacío \"\" tiene largo 0.",
        code = "saludo = \"Hola Glitch\"\nprint(len(saludo))\nprint(len(\"\"))",
        expectedOutput = "11\n0",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "print(len(\"robot\"))",
                acceptedAnswers = listOf("5"),
                correctFeedback = "¡Bien! r-o-b-o-t: 5 caracteres.",
                wrongFeedback = "Cuenta letra por letra los caracteres del texto."
            ),
            MultipleChoice(
                prompt = "¿Cuánto da len(\"a b\")?",
                options = listOf("2", "3", "1", "Da un error"),
                correctIndex = 1,
                correctFeedback = "¡Correcto! La a, el espacio y la b: 3 caracteres.",
                wrongFeedback = "El espacio también es un carácter."
            ),
            TypeCode(
                prompt = "Escribe la línea que muestra cuántos caracteres tiene la variable nombre.",
                acceptedAnswers = listOf("print(len(nombre))"),
                placeholder = "print(...)",
                correctFeedback = "¡Perfecto! Igual que con las listas.",
                wrongFeedback = "Usa len con la variable entre paréntesis, dentro de print."
            )
        ),
        walkthrough = listOf(
            step(1, "Un texto de 11 caracteres.", listOf("saludo" to "'Hola Glitch'")),
            step(2, "4 letras + 1 espacio + 6 letras = 11.", listOf("saludo" to "'Hola Glitch'"), listOf("11")),
            step(3, "El texto vacío no tiene caracteres.", listOf("saludo" to "'Hola Glitch'"), listOf("11", "0"))
        )
    )

    private val indices = LessonContent(
        lessonId = "n11_l02",
        title = "Índices en textos 🎯",
        hook = "Glitch quiere la inicial de cada palabra para hacer un código secreto.",
        explanation = "Como en las listas, cada carácter tiene un índice que empieza en 0, y los negativos cuentan desde el final. texto[0] es el primer carácter y texto[-1] el último. Si pides una posición que no existe, sale IndexError.",
        code = "palabra = \"Python\"\nprint(palabra[0])\nprint(palabra[-1])\nprint(palabra[2])",
        expectedOutput = "P\nn\nt",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "c = \"robot\"\nprint(c[1])",
                acceptedAnswers = listOf("o"),
                correctFeedback = "¡Eso! r está en 0 y o en 1.",
                wrongFeedback = "Cuenta desde 0: ¿qué letra está en la posición 1?"
            ),
            FillBlanks(
                prompt = "Muestra la última letra del animal.",
                codeTemplate = "animal = \"gato\"\nprint(animal[___])",
                answers = listOf("-1"),
                chips = listOf("-1", "4", "0", "1"),
                correctFeedback = "¡Muy bien! -1 es siempre el último.",
                wrongFeedback = "gato tiene 4 letras: sus índices van de 0 a 3. Hay un índice que siempre da la última."
            ),
            FindBug(
                prompt = "Este programa se detiene con IndexError. ¡Toca la línea que lo causa!",
                codeLines = listOf(
                    "clave = \"abc\"",
                    "print(clave[0])",
                    "print(clave[3])"
                ),
                bugLineIndex = 2,
                correctFeedback = "¡Cazado! \"abc\" tiene índices 0, 1 y 2: el 3 no existe.",
                wrongFeedback = "Si el texto tiene 3 caracteres, ¿qué índices existen?"
            )
        ),
        walkthrough = listOf(
            step(1, "P en 0, y en 1, t en 2, h en 3, o en 4, n en 5.", listOf("palabra" to "'Python'")),
            step(2, "El primer carácter.", listOf("palabra" to "'Python'"), listOf("P")),
            step(3, "El último, contando desde el final.", listOf("palabra" to "'Python'"), listOf("P", "n")),
            step(4, "El de la posición 2.", listOf("palabra" to "'Python'"), listOf("P", "n", "t"))
        )
    )

    private val rebanadas = LessonContent(
        lessonId = "n11_l03",
        title = "Rebanadas [a:b] 🍰",
        hook = "Glitch quiere cortar un trozo de su nombre largo, como una rebanada de pastel.",
        explanation = "texto[a:b] saca los caracteres desde la posición a hasta ANTES de b. Si no pones a, empieza desde el principio; si no pones b, llega hasta el final. Las rebanadas también funcionan con listas.",
        code = "palabra = \"Glitchito\"\nprint(palabra[0:6])\nprint(palabra[6:])\nprint(palabra[:3])",
        expectedOutput = "Glitch\nito\nGli",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "t = \"computadora\"\nprint(t[0:4])",
                acceptedAnswers = listOf("comp"),
                correctFeedback = "¡Exacto! Las posiciones 0, 1, 2 y 3: la 4 ya no entra.",
                wrongFeedback = "La rebanada llega hasta ANTES del segundo número."
            ),
            FillBlanks(
                prompt = "Saca \"mundo\" de \"hola mundo\".",
                codeTemplate = "frase = \"hola mundo\"\nprint(frase[___:])",
                answers = listOf("5"),
                chips = listOf("5", "4", "6", "10"),
                correctFeedback = "¡Sí! \"hola \" ocupa las posiciones 0 a 4, así que la m está en la 5.",
                wrongFeedback = "Cuenta también el espacio. ¿En qué posición está la m?"
            ),
            MultipleChoice(
                prompt = "¿Qué da \"abcdef\"[1:3]?",
                options = listOf("bc", "abc", "bcd", "ab"),
                correctIndex = 0,
                correctFeedback = "¡Correcto! Las posiciones 1 y 2: b y c.",
                wrongFeedback = "Empieza en la posición 1 y se detiene antes de la 3."
            )
        ),
        walkthrough = listOf(
            step(1, "G l i t c h i t o: posiciones 0 a 8.", listOf("palabra" to "'Glitchito'")),
            step(2, "De 0 hasta antes de 6.", listOf("palabra" to "'Glitchito'"), listOf("Glitch")),
            step(3, "De 6 hasta el final.", listOf("palabra" to "'Glitchito'"), listOf("Glitch", "ito")),
            step(4, "Desde el principio hasta antes de 3.", listOf("palabra" to "'Glitchito'"), listOf("Glitch", "ito", "Gli"))
        )
    )

    private val noSeCambian = LessonContent(
        lessonId = "n11_l04",
        title = "Los textos no se cambian 🧊",
        hook = "Glitch escribió su nombre en minúscula e intentó cambiar solo la primera letra… y Python se quejó.",
        explanation = "A diferencia de las listas, los textos no se pueden modificar: texto[0] = \"X\" da TypeError. Para \"cambiarlo\" se crea un texto nuevo (por ejemplo, uniendo trozos con +) y se guarda en la misma variable.",
        code = "nombre = \"glitch\"\nnombre = \"G\" + nombre[1:]\nprint(nombre)",
        expectedOutput = "Glitch",
        exercises = listOf(
            FindBug(
                prompt = "Este programa se detiene con TypeError. ¡Toca el error!",
                codeLines = listOf(
                    "palabra = \"pato\"",
                    "palabra[0] = \"g\"",
                    "print(palabra)"
                ),
                bugLineIndex = 1,
                correctFeedback = "¡Bien! Un texto no se puede cambiar por posición. Se hace: palabra = \"g\" + palabra[1:].",
                wrongFeedback = "¿Qué línea intenta modificar el texto por dentro?"
            ),
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "t = \"mapa\"\nt = t + \"s\"\nprint(t)",
                acceptedAnswers = listOf("mapas"),
                correctFeedback = "¡Exacto! Se creó un texto nuevo y se guardó en t.",
                wrongFeedback = "t + \"s\" forma un texto nuevo, y se guarda de nuevo en t."
            ),
            FillBlanks(
                prompt = "Convierte \"casa\" en \"masa\".",
                codeTemplate = "p = \"casa\"\np = \"m\" + p[___:]\nprint(p)",
                answers = listOf("1"),
                chips = listOf("1", "0", "2", "-1"),
                correctFeedback = "¡Perfecto! p[1:] es \"asa\", y con la m adelante queda \"masa\".",
                wrongFeedback = "Necesitas todo menos la primera letra. ¿Desde qué posición empieza eso?"
            )
        ),
        walkthrough = listOf(
            step(1, "El nombre en minúscula.", listOf("nombre" to "'glitch'")),
            step(2, "nombre[1:] es 'litch'. Con la G adelante se crea un texto nuevo.", listOf("nombre" to "'Glitch'")),
            step(3, "Se muestra el texto nuevo.", listOf("nombre" to "'Glitch'"), listOf("Glitch"))
        )
    )

    private val metodos = LessonContent(
        lessonId = "n11_l05",
        title = "upper, lower y strip 🧽",
        hook = "Alguien escribió \"  hola  \" con espacios de más. Glitch lo limpia y lo grita.",
        explanation = "Los textos traen herramientas (métodos): upper() pasa todo a mayúsculas, lower() a minúsculas y strip() quita los espacios del principio y del final. Devuelven un texto NUEVO: el original no cambia, así que guarda el resultado si lo vas a usar.",
        code = "grito = \"  hola  \"\nlimpio = grito.strip()\nprint(limpio.upper())\nprint(len(grito), len(limpio))",
        expectedOutput = "HOLA\n8 4",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Fíjate en las mayúsculas.",
                code = "print(\"GaTo\".lower())",
                acceptedAnswers = listOf("gato"),
                ignoreCase = false,
                correctFeedback = "¡Bien! Todo en minúsculas.",
                wrongFeedback = "lower() convierte TODAS las letras a minúsculas."
            ),
            TypeCode(
                prompt = "Guarda en limpio el texto entrada sin los espacios de los lados.",
                acceptedAnswers = listOf("limpio = entrada.strip()"),
                placeholder = "limpio = ...",
                correctFeedback = "¡Perfecto! strip devuelve el texto limpio y lo guardas.",
                wrongFeedback = "Se escribe texto.strip(), con punto y paréntesis, y se guarda con =."
            ),
            FindBug(
                prompt = "Glitch quería aceptar \"si\", \"SI\" o \"Si\", pero nunca acepta nada. ¡Toca el error!",
                codeLines = listOf(
                    "respuesta = \"SI\"",
                    "if respuesta.lower() == \"SI\":",
                    "    print(\"Aceptado\")"
                ),
                bugLineIndex = 1,
                correctFeedback = "¡Cazado! Después de lower() el texto es \"si\": hay que compararlo con \"si\" en minúscula.",
                wrongFeedback = "Si pasas el texto a minúsculas, ¿con qué deberías compararlo?"
            )
        ),
        walkthrough = listOf(
            step(1, "Un texto con 2 espacios a cada lado.", listOf("grito" to "'  hola  '")),
            step(2, "strip crea un texto nuevo sin esos espacios.", listOf("grito" to "'  hola  '", "limpio" to "'hola'")),
            step(3, "upper lo pasa a mayúsculas para mostrarlo.", listOf("grito" to "'  hola  '", "limpio" to "'hola'"), listOf("HOLA")),
            step(4, "grito no cambió: sigue con 8 caracteres.", listOf("grito" to "'  hola  '", "limpio" to "'hola'"), listOf("HOLA", "8 4"))
        )
    )

    private val reemplazar = LessonContent(
        lessonId = "n11_l06",
        title = "replace y count 🔁",
        hook = "Glitch quiere cambiar palabras en una frase y contar cuántas veces aparece una letra.",
        explanation = "texto.replace(viejo, nuevo) devuelve un texto nuevo donde cada aparición de viejo se cambia por nuevo. texto.count(algo) cuenta cuántas veces aparece. Como siempre con textos, replace no modifica el original: hay que guardar el resultado.",
        code = "frase = \"la papa y la pera\"\nprint(frase.replace(\"la\", \"una\"))\nprint(frase.count(\"a\"))",
        expectedOutput = "una papa y una pera\n5",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "print(\"banana\".count(\"a\"))",
                acceptedAnswers = listOf("3"),
                correctFeedback = "¡Exacto! b-a-n-a-n-a: tres a.",
                wrongFeedback = "Cuenta cuántas veces aparece la letra a."
            ),
            FillBlanks(
                prompt = "Cambia los guiones de la fecha por espacios.",
                codeTemplate = "fecha = \"10-11-2026\"\nprint(fecha.___(\"-\", \" \"))",
                answers = listOf("replace"),
                chips = listOf("replace", "count", "strip", "split"),
                correctFeedback = "¡Sí! Muestra 10 11 2026.",
                wrongFeedback = "Necesitas el método que cambia una cosa por otra."
            ),
            PredictOutput(
                prompt = "¡Cuidado con este! ¿Qué muestra?",
                code = "t = \"gato\"\nt.replace(\"g\", \"p\")\nprint(t)",
                acceptedAnswers = listOf("gato"),
                correctFeedback = "¡Muy bien! replace creó \"pato\", pero nadie lo guardó: t sigue siendo \"gato\".",
                wrongFeedback = "replace devuelve un texto nuevo. ¿Se guardó en alguna parte?"
            )
        ),
        walkthrough = listOf(
            step(1, "La frase original.", listOf("frase" to "'la papa y la pera'")),
            step(2, "Cada \"la\" se cambia por \"una\" en un texto nuevo.", listOf("frase" to "'la papa y la pera'"), listOf("una papa y una pera")),
            step(3, "La frase original no cambió: tiene 5 letras a.", listOf("frase" to "'la papa y la pera'"), listOf("una papa y una pera", "5"))
        )
    )

    private val buscar = LessonContent(
        lessonId = "n11_l07",
        title = "find y in 🔎",
        hook = "Glitch busca la palabra clave dentro de un mensaje misterioso.",
        explanation = "\"algo\" in texto da True si ese trozo aparece en el texto. texto.find(\"algo\") va más allá: devuelve la posición donde empieza, o -1 si no lo encuentra.",
        code = "mensaje = \"clave secreta\"\nprint(\"secreta\" in mensaje)\nprint(mensaje.find(\"secreta\"))\nprint(mensaje.find(\"x\"))",
        expectedOutput = "True\n6\n-1",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "print(\"hola\".find(\"l\"))",
                acceptedAnswers = listOf("2"),
                correctFeedback = "¡Bien! h en 0, o en 1, l en 2.",
                wrongFeedback = "find devuelve la posición, contando desde 0."
            ),
            MultipleChoice(
                prompt = "¿Qué devuelve find cuando no encuentra lo que buscas?",
                options = listOf("-1", "0", "None", "Da un error"),
                correctIndex = 0,
                correctFeedback = "¡Correcto! -1 significa \"no está\".",
                wrongFeedback = "0 sería una posición real (la primera). Necesita un número que nunca sea posición."
            ),
            FillBlanks(
                prompt = "Revisa si el correo tiene una arroba.",
                codeTemplate = "correo = \"glitch@city.com\"\nif \"@\" ___ correo:\n    print(\"Parece un correo\")",
                answers = listOf("in"),
                chips = listOf("in", "find", "==", "not"),
                correctFeedback = "¡Eso! in funciona con textos igual que con listas.",
                wrongFeedback = "Queremos saber si la arroba está dentro del texto: True o False."
            )
        ),
        walkthrough = listOf(
            step(1, "\"clave \" ocupa las posiciones 0 a 5.", listOf("mensaje" to "'clave secreta'")),
            step(2, "\"secreta\" sí aparece.", listOf("mensaje" to "'clave secreta'"), listOf("True")),
            step(3, "Empieza en la posición 6.", listOf("mensaje" to "'clave secreta'"), listOf("True", "6")),
            step(4, "No hay ninguna x: -1.", listOf("mensaje" to "'clave secreta'"), listOf("True", "6", "-1"))
        )
    )

    private val separar = LessonContent(
        lessonId = "n11_l08",
        title = "split: de texto a lista ✂️",
        hook = "Glitch recibió su lista de compras en una sola línea. ¡Hay que separarla!",
        explanation = "texto.split() corta el texto por los espacios y devuelve una lista de palabras. Con split(\",\") corta por comas, o por el separador que le pases. Así puedes contar palabras o tomar cada dato por separado.",
        code = "frase = \"pan leche huevos\"\ncosas = frase.split()\nprint(cosas)\nprint(len(cosas))\ndatos = \"Ana,12,Bogotá\"\nprint(datos.split(\",\"))",
        expectedOutput = "['pan', 'leche', 'huevos']\n3\n['Ana', '12', 'Bogotá']",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "print(len(\"uno dos tres cuatro\".split()))",
                acceptedAnswers = listOf("4"),
                correctFeedback = "¡Exacto! 4 palabras: split + len sirve para contar palabras.",
                wrongFeedback = "split arma una lista con cada palabra. ¿Cuántas palabras hay?"
            ),
            TypeCode(
                prompt = "Separa el texto linea por comas y guarda el resultado en partes.",
                acceptedAnswers = listOf("partes = linea.split(\",\")"),
                placeholder = "partes = ...",
                correctFeedback = "¡Perfecto! split con la coma como separador.",
                wrongFeedback = "Usa split y pásale la coma entre comillas como separador."
            ),
            FindBug(
                prompt = "Glitch quería el día de la fecha, pero sale la fecha completa. ¡Toca el error!",
                codeLines = listOf(
                    "fecha = \"10-11-2026\"",
                    "partes = fecha.split()",
                    "print(partes[0])"
                ),
                bugLineIndex = 1,
                correctFeedback = "¡Cazado! split() sin nada corta por espacios. Para la fecha es split(\"-\").",
                wrongFeedback = "¿Por qué carácter hay que cortar esta fecha?"
            )
        ),
        walkthrough = listOf(
            step(1, "Un solo texto con 3 cosas.", listOf("frase" to "'pan leche huevos'")),
            step(2, "split corta por los espacios.", listOf("frase" to "'pan leche huevos'", "cosas" to "['pan', 'leche', 'huevos']")),
            step(3, "Ahora es una lista.", listOf("frase" to "'pan leche huevos'", "cosas" to "['pan', 'leche', 'huevos']"), listOf("['pan', 'leche', 'huevos']")),
            step(4, "Tiene 3 elementos.", listOf("frase" to "'pan leche huevos'", "cosas" to "['pan', 'leche', 'huevos']"), listOf("['pan', 'leche', 'huevos']", "3")),
            step(5, "Otro texto, separado por comas.", listOf("cosas" to "['pan', 'leche', 'huevos']", "datos" to "'Ana,12,Bogotá'"), listOf("['pan', 'leche', 'huevos']", "3")),
            step(6, "split(\",\") corta por las comas. ¡El 12 sigue siendo texto!", listOf("cosas" to "['pan', 'leche', 'huevos']", "datos" to "'Ana,12,Bogotá'"), listOf("['pan', 'leche', 'huevos']", "3", "['Ana', '12', 'Bogotá']"))
        )
    )

    private val unir = LessonContent(
        lessonId = "n11_l09",
        title = "join: de lista a texto 🧵",
        hook = "Glitch tiene sus letras sueltas en una lista y quiere armar la palabra.",
        explanation = "separador.join(lista) une todos los textos de la lista en uno solo, poniendo el separador entre cada par. Con \"\" quedan pegados; con \" \" separados por espacios. Es lo contrario de split. Todos los elementos deben ser textos.",
        code = "letras = [\"G\", \"l\", \"i\", \"t\", \"c\", \"h\"]\nprint(\"\".join(letras))\nprint(\"-\".join(letras))",
        expectedOutput = "Glitch\nG-l-i-t-c-h",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "print(\", \".join([\"sol\", \"luna\"]))",
                acceptedAnswers = listOf("sol, luna"),
                correctFeedback = "¡Bien! La coma y el espacio van entre los dos.",
                wrongFeedback = "El separador se pone ENTRE los elementos, no al final."
            ),
            FillBlanks(
                prompt = "Une las palabras con un espacio.",
                codeTemplate = "palabras = [\"hola\", \"mundo\"]\nprint(\" \".___(palabras))",
                answers = listOf("join"),
                chips = listOf("join", "split", "append", "replace"),
                correctFeedback = "¡Sí! Muestra hola mundo.",
                wrongFeedback = "Necesitas el método que une, lo contrario de split."
            ),
            OrderLines(
                prompt = "Ordena el programa: separa el texto y vuelve a unirlo con signos +.",
                lines = listOf(
                    "texto = \"a b c\"",
                    "partes = texto.split()",
                    "print(\"+\".join(partes))"
                ),
                correctFeedback = "¡Perfecto! Muestra a+b+c.",
                wrongFeedback = "Primero el texto, luego se separa, y al final se une con el nuevo separador."
            )
        ),
        walkthrough = listOf(
            step(1, "Seis letras sueltas en una lista.", listOf("letras" to "['G', 'l', 'i', 't', 'c', 'h']")),
            step(2, "Con separador vacío quedan pegadas.", listOf("letras" to "['G', 'l', 'i', 't', 'c', 'h']"), listOf("Glitch")),
            step(3, "Con \"-\" se pone un guion entre cada par.", listOf("letras" to "['G', 'l', 'i', 't', 'c', 'h']"), listOf("Glitch", "G-l-i-t-c-h"))
        )
    )

    private val letraPorLetra = LessonContent(
        lessonId = "n11_l10",
        title = "Recorrer letra por letra 🔡",
        hook = "Glitch quiere contar las vocales de una palabra, una por una.",
        explanation = "for letra in texto: da una vuelta por cada carácter, en orden. Combinado con if e in puedes contar, filtrar o revisar cada letra. Truco: letra in \"aeiou\" dice si la letra es una vocal.",
        code = "palabra = \"casa\"\nvocales = 0\nfor letra in palabra:\n    if letra in \"aeiou\":\n        vocales += 1\nprint(vocales)",
        expectedOutput = "2",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "for c in \"abc\":\n    print(c * 2)",
                acceptedAnswers = listOf("aa\nbb\ncc"),
                correctFeedback = "¡Exacto! Cada letra, repetida dos veces.",
                wrongFeedback = "c toma cada letra: a, b y c. ¿Qué hace * 2 con un texto?"
            ),
            FillBlanks(
                prompt = "Cuenta cuántas letras o hay.",
                codeTemplate = "texto = \"robot loco\"\nn = 0\nfor letra in texto:\n    if letra == ___:\n        n += 1\nprint(n)",
                answers = listOf("\"o\""),
                chips = listOf("\"o\"", "o", "letra", "texto"),
                correctFeedback = "¡Muy bien! Muestra 4.",
                wrongFeedback = "Hay que comparar con el TEXTO o: necesita comillas."
            ),
            FindBug(
                prompt = "Glitch quería ver cada letra, pero sale la palabra entera 6 veces. ¡Toca el error!",
                codeLines = listOf(
                    "nombre = \"Glitch\"",
                    "for letra in nombre:",
                    "    print(nombre)"
                ),
                bugLineIndex = 2,
                correctFeedback = "¡Cazado! Dentro del for hay que usar letra, no nombre.",
                wrongFeedback = "¿Qué variable cambia en cada vuelta? ¿Cuál se está imprimiendo?"
            )
        ),
        walkthrough = listOf(
            step(1, "La palabra a revisar.", listOf("palabra" to "'casa'")),
            step(2, "El contador empieza en 0.", listOf("palabra" to "'casa'", "vocales" to "0")),
            step(3, "Primera letra: c.", listOf("palabra" to "'casa'", "vocales" to "0", "letra" to "'c'")),
            step(4, "¿c es vocal? No.", listOf("palabra" to "'casa'", "vocales" to "0", "letra" to "'c'")),
            step(3, "Segunda letra: a.", listOf("palabra" to "'casa'", "vocales" to "0", "letra" to "'a'")),
            step(4, "¿a es vocal? Sí.", listOf("palabra" to "'casa'", "vocales" to "0", "letra" to "'a'")),
            step(5, "El contador sube.", listOf("palabra" to "'casa'", "vocales" to "1", "letra" to "'a'")),
            step(3, "Tercera letra: s.", listOf("palabra" to "'casa'", "vocales" to "1", "letra" to "'s'")),
            step(4, "¿s es vocal? No.", listOf("palabra" to "'casa'", "vocales" to "1", "letra" to "'s'")),
            step(3, "Cuarta letra: a.", listOf("palabra" to "'casa'", "vocales" to "1", "letra" to "'a'")),
            step(4, "¿a es vocal? Sí.", listOf("palabra" to "'casa'", "vocales" to "1", "letra" to "'a'")),
            step(5, "El contador sube otra vez.", listOf("palabra" to "'casa'", "vocales" to "2", "letra" to "'a'")),
            step(6, "No quedan letras: se muestra el total.", listOf("palabra" to "'casa'", "vocales" to "2", "letra" to "'a'"), listOf("2"))
        )
    )

    private val contrasena = LessonContent(
        lessonId = "n11_l11",
        title = "Mini programa: contraseña segura 🔐",
        hook = "Glitch City pide claves seguras: al menos 8 caracteres y algún número. Glitch arma el revisor.",
        explanation = "Juntamos todo: strip limpia los espacios, un for revisa carácter por carácter si hay algún número, y un if con and junta las dos condiciones. Una variable True/False (bandera) recuerda si ya se encontró un número.",
        code = "clave = \" Glitch2026 \"\nclave = clave.strip()\ntiene_numero = False\nfor c in clave:\n    if c in \"0123456789\":\n        tiene_numero = True\nif len(clave) >= 8 and tiene_numero:\n    print(\"Clave segura\")\nelse:\n    print(\"Clave débil\")",
        expectedOutput = "Clave segura",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "clave = \"abc\"\nif len(clave) >= 8:\n    print(\"Segura\")\nelse:\n    print(\"Muy corta\")",
                acceptedAnswers = listOf("Muy corta"),
                correctFeedback = "¡Bien! 3 caracteres no llegan a 8.",
                wrongFeedback = "¿Cuántos caracteres tiene \"abc\"? ¿Es 8 o más?"
            ),
            FillBlanks(
                prompt = "La clave es segura solo si cumple las DOS condiciones.",
                codeTemplate = "clave = \"gato12345\"\ntiene_numero = True\nif len(clave) >= 8 ___ tiene_numero:\n    print(\"Clave segura\")",
                answers = listOf("and"),
                chips = listOf("and", "or", "not", "in"),
                correctFeedback = "¡Eso! and exige que las dos sean verdaderas.",
                wrongFeedback = "Con or bastaría una sola. Necesitas que se cumplan ambas."
            ),
            OrderLines(
                prompt = "Ordena el programa: pide la clave, límpiala y muestra su largo.",
                lines = listOf(
                    "clave = input(\"Clave: \")",
                    "clave = clave.strip()",
                    "print(len(clave))"
                ),
                correctFeedback = "¡Perfecto! Pedir, limpiar y medir.",
                wrongFeedback = "No puedes limpiar una clave que todavía no te dieron."
            )
        ),
        walkthrough = listOf(
            step(1, "La clave llega con espacios.", listOf("clave" to "' Glitch2026 '")),
            step(2, "strip quita los espacios: quedan 10 caracteres.", listOf("clave" to "'Glitch2026'")),
            step(3, "Todavía no hemos visto ningún número.", listOf("clave" to "'Glitch2026'", "tiene_numero" to "False")),
            step(4, "El for revisa G, l, i, t, c y h: ninguna es número.", listOf("clave" to "'Glitch2026'", "tiene_numero" to "False", "c" to "'h'")),
            step(5, "Llega al 2: ¡sí está en \"0123456789\"!", listOf("clave" to "'Glitch2026'", "tiene_numero" to "False", "c" to "'2'")),
            step(6, "La bandera queda en True (y así sigue hasta el final).", listOf("clave" to "'Glitch2026'", "tiene_numero" to "True", "c" to "'2'")),
            step(7, "10 >= 8 y hay número: las dos se cumplen.", listOf("clave" to "'Glitch2026'", "tiene_numero" to "True", "c" to "'6'")),
            step(8, "La clave es segura.", listOf("clave" to "'Glitch2026'", "tiene_numero" to "True", "c" to "'6'"), listOf("Clave segura"))
        )
    )

    // Jefe del nivel: mezcla lo aprendido en las 11 lecciones.
    val boss = BossContent(
        bossId = "n11_boss",
        name = "El Descifrador de Mensajes",
        emoji = "🔐",
        intro = "Esconde sus mensajes cortándolos, mezclándolos y cambiándoles las letras. Si entiendes cómo trabajan los textos, ningún mensaje se te resistirá.",
        exercises = listOf(
            PredictOutput(
                prompt = "Primer mensaje. ¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "m = \"secreto\"\nprint(m[0:3])\nprint(m[-1])",
                acceptedAnswers = listOf("sec\no"),
                correctFeedback = "¡Golpe! Las posiciones 0 a 2, y la última letra.",
                wrongFeedback = "La rebanada se detiene antes del 3; -1 es la última letra."
            ),
            PredictOutput(
                prompt = "El descifrador corta y vuelve a pegar. ¿Qué muestra?",
                code = "print(\" \".join(\"a-b-c\".split(\"-\")))",
                acceptedAnswers = listOf("a b c"),
                correctFeedback = "¡Bien! split separa por guiones y join une con espacios.",
                wrongFeedback = "Primero se hace lo de adentro: split(\"-\") da una lista. Luego join la une con espacios."
            ),
            FindBug(
                prompt = "El descifrador nunca dice \"Descifrado\". ¡Toca el error!",
                codeLines = listOf(
                    "msg = \"HOLA\"",
                    "msg.lower()",
                    "if msg == \"hola\":",
                    "    print(\"Descifrado\")"
                ),
                bugLineIndex = 1,
                correctFeedback = "¡Bug golpeado! lower() crea un texto nuevo que nadie guardó: debe ser msg = msg.lower().",
                wrongFeedback = "Recuerda: los textos no cambian solos. ¿Dónde se pierde un resultado?"
            ),
            FillBlanks(
                prompt = "Quédate solo con las letras del código (sin números).",
                codeTemplate = "codigo = \"x1y2z3\"\nletras = \"\"\nfor c in codigo:\n    if c not in \"0123456789\":\n        letras ___ c\nprint(letras)",
                answers = listOf("+="),
                chips = listOf("+=", "=", "-=", "=="),
                correctFeedback = "¡Eso! Se van pegando: muestra xyz.",
                wrongFeedback = "Hay que ir AGREGANDO cada letra al texto, sin perder las anteriores."
            ),
            TypeCode(
                prompt = "Último golpe: escribe la línea que muestra el mensaje m todo en mayúsculas.",
                acceptedAnswers = listOf("print(m.upper())"),
                placeholder = "print(...)",
                correctFeedback = "¡Descifrador vencido! Ya dominas los textos.",
                wrongFeedback = "Usa el método de mayúsculas sobre m, dentro de print."
            )
        )
    )

    val all: List<LessonContent> = listOf(
        filaDeLetras, indices, rebanadas, noSeCambian,
        metodos, reemplazar, buscar, separar,
        unir, letraPorLetra, contrasena
    )
}
