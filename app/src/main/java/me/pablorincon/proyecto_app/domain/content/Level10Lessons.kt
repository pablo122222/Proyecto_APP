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

// Nivel 10 · Listas (11 lecciones y el jefe «El Coleccionista Glotón»): crear listas, índices,
// cambiar, agregar y quitar, len, recorrer con for, acumular y buscar con in.
// Quien elige "Intermedio" empieza aquí: la primera lección no da por sabido nada de listas.
internal object Level10Lessons {

    private fun step(line: Int, note: String, vars: List<Pair<String, String>>, out: List<String> = emptyList()) =
        WalkStep(line, note, vars, out)

    private val variasCosas = LessonContent(
        lessonId = "n10_l01",
        title = "Muchas cosas en una variable 🧺",
        hook = "Glitch quiere guardar sus 3 herramientas favoritas. ¿Tres variables? Mejor una sola caja con compartimentos: una lista.",
        explanation = "Una lista guarda varios valores, en orden, dentro de una sola variable. Se escribe entre corchetes [ ] y los elementos van separados por comas. Puede tener textos, números o ambos. Al imprimirla, Python muestra los corchetes y los textos con comillas simples.",
        code = "herramientas = [\"llave\", \"martillo\", \"cinta\"]\nnotas = [7, 9, 10]\nprint(herramientas)\nprint(notas)",
        expectedOutput = "['llave', 'martillo', 'cinta']\n[7, 9, 10]",
        exercises = listOf(
            TypeCode(
                prompt = "Crea una lista llamada colores con \"rojo\" y \"azul\", en ese orden.",
                acceptedAnswers = listOf("colores = [\"rojo\", \"azul\"]"),
                placeholder = "colores = ...",
                correctFeedback = "¡Perfecto! Corchetes, comas entre elementos y los textos con comillas.",
                wrongFeedback = "La lista va entre corchetes [ ] y cada texto entre comillas, separados por una coma."
            ),
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "numeros = [3, 1, 2]\nprint(numeros)",
                acceptedAnswers = listOf("[3, 1, 2]", "[3,1,2]"),
                correctFeedback = "¡Exacto! La lista se muestra tal cual, con corchetes y en el mismo orden.",
                wrongFeedback = "print muestra la lista completa, con sus corchetes. ¿Cambia el orden?"
            ),
            FindBug(
                prompt = "Glitch quería una lista de 3 frutas, pero le salen 2. ¡Toca la línea con el error!",
                codeLines = listOf(
                    "nombre = \"Glitch\"",
                    "frutas = [\"pera\", \"uva\" \"kiwi\"]",
                    "print(nombre)",
                    "print(frutas)"
                ),
                bugLineIndex = 1,
                correctFeedback = "¡Bien visto! Falta una coma: Python pega \"uva\" y \"kiwi\" en un solo texto, \"uvakiwi\".",
                wrongFeedback = "Revisa cómo se separan los elementos dentro de los corchetes."
            )
        ),
        walkthrough = listOf(
            step(1, "Se crea la lista con 3 textos, en orden.", listOf("herramientas" to "['llave', 'martillo', 'cinta']")),
            step(2, "Otra lista, esta vez con números.", listOf("herramientas" to "['llave', 'martillo', 'cinta']", "notas" to "[7, 9, 10]")),
            step(3, "print muestra la lista con corchetes y comillas simples.", listOf("herramientas" to "['llave', 'martillo', 'cinta']", "notas" to "[7, 9, 10]"), listOf("['llave', 'martillo', 'cinta']")),
            step(4, "Los números se muestran sin comillas.", listOf("herramientas" to "['llave', 'martillo', 'cinta']", "notas" to "[7, 9, 10]"), listOf("['llave', 'martillo', 'cinta']", "[7, 9, 10]"))
        )
    )

    private val posiciones = LessonContent(
        lessonId = "n10_l02",
        title = "Posiciones: empezar en 0 0️⃣",
        hook = "Glitch tiene una fila de robots y quiere llamar a uno solo. ¿Cómo lo señala?",
        explanation = "Cada elemento tiene una posición llamada índice. ¡La primera es 0, no 1! La segunda es 1, la tercera 2… Para leer un elemento se escribe el nombre de la lista y el índice entre corchetes: lista[0].",
        code = "robots = [\"Bip\", \"Bop\", \"Bup\"]\nprint(robots[0])\nprint(robots[2])",
        expectedOutput = "Bip\nBup",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "letras = [\"a\", \"b\", \"c\", \"d\"]\nprint(letras[1])",
                acceptedAnswers = listOf("b"),
                correctFeedback = "¡Sí! El índice 1 es el segundo elemento.",
                wrongFeedback = "Cuenta desde 0: a está en 0, b en 1…"
            ),
            FillBlanks(
                prompt = "Muestra el primer planeta de la lista.",
                codeTemplate = "planetas = [\"Marte\", \"Venus\"]\nprint(planetas[___])",
                answers = listOf("0"),
                chips = listOf("0", "1", "2", "primero"),
                correctFeedback = "¡Eso! El primero siempre está en la posición 0.",
                wrongFeedback = "En Python las posiciones empiezan a contar desde 0."
            ),
            MultipleChoice(
                prompt = "En la lista [10, 20, 30], ¿qué índice tiene el 30?",
                options = listOf("0", "1", "2", "3"),
                correctIndex = 2,
                correctFeedback = "¡Correcto! 10 está en 0, 20 en 1 y 30 en 2.",
                wrongFeedback = "Recuerda que el primero está en 0. Cuenta desde ahí."
            )
        ),
        walkthrough = listOf(
            step(1, "Se crea la lista: Bip en 0, Bop en 1, Bup en 2.", listOf("robots" to "['Bip', 'Bop', 'Bup']")),
            step(2, "robots[0] es el primero.", listOf("robots" to "['Bip', 'Bop', 'Bup']"), listOf("Bip")),
            step(3, "robots[2] es el tercero: se cuenta 0, 1, 2.", listOf("robots" to "['Bip', 'Bop', 'Bup']"), listOf("Bip", "Bup"))
        )
    )

    private val negativos = LessonContent(
        lessonId = "n10_l03",
        title = "Desde el final: índices negativos ⏪",
        hook = "Glitch quiere saber quién llegó de último a la fila, pero no sabe cuántos hay.",
        explanation = "También se puede contar desde el final con números negativos: -1 es el último, -2 el penúltimo, y así. Es muy útil cuando no sabes cuántos elementos tiene la lista.",
        code = "fila = [\"Ana\", \"Leo\", \"Sol\", \"Teo\"]\nprint(fila[-1])\nprint(fila[-2])",
        expectedOutput = "Teo\nSol",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "n = [5, 8, 13]\nprint(n[-1])",
                acceptedAnswers = listOf("13"),
                correctFeedback = "¡Muy bien! -1 siempre es el último.",
                wrongFeedback = "Los índices negativos cuentan desde el final. ¿Cuál es el último?"
            ),
            TypeCode(
                prompt = "Escribe la línea que muestra el último elemento de la lista tareas, usando un índice negativo.",
                acceptedAnswers = listOf("print(tareas[-1])"),
                placeholder = "print(...)",
                correctFeedback = "¡Exacto! tareas[-1] es la última tarea, sea cual sea el largo de la lista.",
                wrongFeedback = "Usa print con el nombre de la lista y el índice negativo del último entre corchetes."
            ),
            FindBug(
                prompt = "Glitch quería mostrar el primer color, pero sale el último. ¡Toca el error!",
                codeLines = listOf(
                    "colores = [\"rojo\", \"verde\", \"azul\"]",
                    "print(\"El primero es:\")",
                    "print(colores[-1])"
                ),
                bugLineIndex = 2,
                correctFeedback = "¡Bug encontrado! -1 es el último; el primero es colores[0].",
                wrongFeedback = "¿Qué línea elige el elemento? Revisa su índice."
            )
        ),
        walkthrough = listOf(
            step(1, "Se crea la fila de 4 personas.", listOf("fila" to "['Ana', 'Leo', 'Sol', 'Teo']")),
            step(2, "-1 es el último de la fila.", listOf("fila" to "['Ana', 'Leo', 'Sol', 'Teo']"), listOf("Teo")),
            step(3, "-2 es el penúltimo.", listOf("fila" to "['Ana', 'Leo', 'Sol', 'Teo']"), listOf("Teo", "Sol"))
        )
    )

    private val cambiar = LessonContent(
        lessonId = "n10_l04",
        title = "Cambiar un elemento ✏️",
        hook = "El profe revisó otra vez el examen de Glitch: la tercera nota no era 5, ¡era 9!",
        explanation = "Para reemplazar un elemento se usa su índice a la izquierda del =: lista[2] = 9. El valor que estaba en esa posición desaparece y los demás no cambian. Las listas se pueden modificar después de crearlas.",
        code = "notas = [6, 8, 5]\nnotas[2] = 9\nprint(notas)",
        expectedOutput = "[6, 8, 9]",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "x = [1, 2, 3]\nx[0] = 10\nprint(x)",
                acceptedAnswers = listOf("[10, 2, 3]", "[10,2,3]"),
                correctFeedback = "¡Eso! Solo cambió la posición 0.",
                wrongFeedback = "x[0] = 10 reemplaza el primer elemento; los demás quedan igual."
            ),
            FillBlanks(
                prompt = "Cambia el pez por un gato (el segundo elemento).",
                codeTemplate = "mascotas = [\"perro\", \"pez\", \"loro\"]\nmascotas[___] = \"gato\"",
                answers = listOf("1"),
                chips = listOf("0", "1", "2", "-1"),
                correctFeedback = "¡Bien! El segundo elemento está en la posición 1.",
                wrongFeedback = "¿En qué posición está \"pez\"? Recuerda que se cuenta desde 0."
            ),
            OrderLines(
                prompt = "Ordena el programa: crea la lista, cambia el primer puntaje y muestra la lista.",
                lines = listOf(
                    "puntos = [0, 0, 0]",
                    "puntos[0] = 5",
                    "print(puntos)"
                ),
                correctFeedback = "¡Perfecto! Muestra [5, 0, 0].",
                wrongFeedback = "No puedes cambiar una lista que todavía no existe, y el print va al final para ver el cambio."
            )
        ),
        walkthrough = listOf(
            step(1, "Se crea la lista de notas.", listOf("notas" to "[6, 8, 5]")),
            step(2, "La posición 2 pasa de 5 a 9. Las otras no se tocan.", listOf("notas" to "[6, 8, 9]")),
            step(3, "Se muestra la lista ya corregida.", listOf("notas" to "[6, 8, 9]"), listOf("[6, 8, 9]"))
        )
    )

    private val agregar = LessonContent(
        lessonId = "n10_l05",
        title = "Agregar con append ➕",
        hook = "Glitch prepara su mochila para una aventura. Empieza vacía y va metiendo cosas una por una.",
        explanation = "Una lista vacía se escribe []. Con lista.append(valor) se agrega un elemento al final. La lista crece: no hace falta saber de antemano cuántos elementos tendrá. Es perfecto para llenarla dentro de un bucle.",
        code = "mochila = []\nmochila.append(\"mapa\")\nmochila.append(\"linterna\")\nprint(mochila)",
        expectedOutput = "['mapa', 'linterna']",
        exercises = listOf(
            TypeCode(
                prompt = "Escribe la línea que agrega el número 7 al final de la lista numeros.",
                acceptedAnswers = listOf("numeros.append(7)"),
                placeholder = "numeros...",
                correctFeedback = "¡Exacto! El nombre de la lista, un punto, append y el valor entre paréntesis.",
                wrongFeedback = "Se escribe lista.append(valor): con punto y paréntesis."
            ),
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "pares = []\nfor i in range(3):\n    pares.append(i * 2)\nprint(pares)",
                acceptedAnswers = listOf("[0, 2, 4]", "[0,2,4]"),
                correctFeedback = "¡Muy bien! i vale 0, 1 y 2, y se agregan sus dobles.",
                wrongFeedback = "range(3) da 0, 1 y 2. ¿Cuál es el doble de cada uno?"
            ),
            FindBug(
                prompt = "Este programa no funciona. ¡Toca la línea con el error!",
                codeLines = listOf(
                    "lista = []",
                    "lista.append(\"a\")",
                    "lista.append \"b\"",
                    "print(lista)"
                ),
                bugLineIndex = 2,
                correctFeedback = "¡Bug cazado! append necesita paréntesis: lista.append(\"b\").",
                wrongFeedback = "Compara las dos líneas que usan append."
            )
        ),
        walkthrough = listOf(
            step(1, "La mochila empieza vacía.", listOf("mochila" to "[]")),
            step(2, "Se agrega el mapa al final.", listOf("mochila" to "['mapa']")),
            step(3, "La linterna va detrás del mapa.", listOf("mochila" to "['mapa', 'linterna']")),
            step(4, "Se muestra la mochila llena.", listOf("mochila" to "['mapa', 'linterna']"), listOf("['mapa', 'linterna']"))
        )
    )

    private val quitar = LessonContent(
        lessonId = "n10_l06",
        title = "Quitar con remove y pop ➖",
        hook = "En la fila del cine, Leo se cansó y se fue. Y el último ya entró a la sala.",
        explanation = "lista.remove(valor) quita el primer elemento igual a ese valor. lista.pop() quita el último y además lo devuelve, así que puedes guardarlo en una variable. Con pop(i) quitas el de la posición i.",
        code = "fila = [\"Ana\", \"Leo\", \"Sol\"]\nfila.remove(\"Leo\")\nultimo = fila.pop()\nprint(fila)\nprint(ultimo)",
        expectedOutput = "['Ana']\nSol",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "n = [4, 7, 4]\nn.remove(4)\nprint(n)",
                acceptedAnswers = listOf("[7, 4]", "[7,4]"),
                correctFeedback = "¡Exacto! remove solo quita el primer 4 que encuentra.",
                wrongFeedback = "remove no quita todos los iguales: solo el primero."
            ),
            FillBlanks(
                prompt = "Quita el último elemento de la pila y guárdalo en sacado.",
                codeTemplate = "pila = [1, 2, 3]\nsacado = pila.___()",
                answers = listOf("pop"),
                chips = listOf("pop", "remove", "append", "len"),
                correctFeedback = "¡Sí! pop() quita el último y lo devuelve: sacado vale 3.",
                wrongFeedback = "Necesitas la que quita el último Y te lo entrega para guardarlo."
            ),
            MultipleChoice(
                prompt = "La lista tiene dos veces \"x\". ¿Qué hace lista.remove(\"x\")?",
                options = listOf("Quita solo la primera", "Quita las dos", "Quita la última", "Da un error"),
                correctIndex = 0,
                correctFeedback = "¡Correcto! remove quita solo la primera coincidencia.",
                wrongFeedback = "remove busca desde el principio y se detiene en la primera que encuentra."
            )
        ),
        walkthrough = listOf(
            step(1, "La fila tiene 3 personas.", listOf("fila" to "['Ana', 'Leo', 'Sol']")),
            step(2, "Se quita a Leo: los demás se juntan.", listOf("fila" to "['Ana', 'Sol']")),
            step(3, "pop quita a Sol (el último) y lo guarda en ultimo.", listOf("fila" to "['Ana']", "ultimo" to "'Sol'")),
            step(4, "Solo queda Ana.", listOf("fila" to "['Ana']", "ultimo" to "'Sol'"), listOf("['Ana']")),
            step(5, "Y ultimo tiene a Sol.", listOf("fila" to "['Ana']", "ultimo" to "'Sol'"), listOf("['Ana']", "Sol"))
        )
    )

    private val largo = LessonContent(
        lessonId = "n10_l07",
        title = "len y el error de índice 📏",
        hook = "Glitch pidió la caja número 3 de un estante que solo tiene 3 cajas… y todo se vino abajo.",
        explanation = "len(lista) dice cuántos elementos tiene. Como se empieza en 0, los índices van de 0 a len - 1. Si pides una posición que no existe, Python se detiene con IndexError. El último siempre es lista[len(lista) - 1], o más corto, lista[-1].",
        code = "dias = [\"lun\", \"mar\", \"mié\"]\nprint(len(dias))\nprint(dias[len(dias) - 1])",
        expectedOutput = "3\nmié",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "x = [2, 4, 6, 8]\nprint(len(x))",
                acceptedAnswers = listOf("4"),
                correctFeedback = "¡Bien! len cuenta elementos, no muestra el último índice.",
                wrongFeedback = "len cuenta cuántos elementos hay en total."
            ),
            FindBug(
                prompt = "Este programa se detiene con IndexError. ¡Toca la línea que lo causa!",
                codeLines = listOf(
                    "frutas = [\"pera\", \"uva\"]",
                    "print(len(frutas))",
                    "print(frutas[2])"
                ),
                bugLineIndex = 2,
                correctFeedback = "¡Cazado! Con 2 frutas los índices son 0 y 1: la posición 2 no existe.",
                wrongFeedback = "Si la lista tiene 2 elementos, ¿qué índices existen?"
            ),
            TypeCode(
                prompt = "Escribe la línea que muestra cuántos elementos tiene la lista invitados.",
                acceptedAnswers = listOf("print(len(invitados))"),
                placeholder = "print(...)",
                correctFeedback = "¡Perfecto! len dentro de print.",
                wrongFeedback = "Usa len con la lista entre paréntesis, y todo eso dentro de print."
            )
        ),
        walkthrough = listOf(
            step(1, "Una lista de 3 días: índices 0, 1 y 2.", listOf("dias" to "['lun', 'mar', 'mié']")),
            step(2, "len cuenta 3 elementos.", listOf("dias" to "['lun', 'mar', 'mié']"), listOf("3")),
            step(3, "len(dias) - 1 es 2: el último índice.", listOf("dias" to "['lun', 'mar', 'mié']"), listOf("3", "mié"))
        )
    )

    private val recorrer = LessonContent(
        lessonId = "n10_l08",
        title = "Recorrer una lista con for 🔁",
        hook = "Glitch quiere saludar a cada uno de sus amigos, sin olvidar a ninguno.",
        explanation = "for elemento in lista: da una vuelta por cada elemento, en orden. En cada vuelta la variable vale el siguiente elemento. No necesitas índices ni saber cuántos hay. El nombre de la variable lo eliges tú.",
        code = "amigos = [\"Bip\", \"Bop\"]\nfor amigo in amigos:\n    print(\"Hola,\", amigo)",
        expectedOutput = "Hola, Bip\nHola, Bop",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "for n in [3, 1, 2]:\n    print(n * 10)",
                acceptedAnswers = listOf("30\n10\n20"),
                correctFeedback = "¡Exacto! Una vuelta por elemento, en el orden de la lista.",
                wrongFeedback = "n toma cada valor de la lista en orden: 3, luego 1, luego 2."
            ),
            OrderLines(
                prompt = "Ordena el programa para mostrar cada color de la lista.",
                lines = listOf(
                    "colores = [\"rojo\", \"azul\"]",
                    "for c in colores:",
                    "    print(c)"
                ),
                correctFeedback = "¡Perfecto! Primero la lista, luego el for y el print con sangría.",
                wrongFeedback = "La lista debe existir antes del for, y el print va dentro del for."
            ),
            FillBlanks(
                prompt = "Completa el for que recorre los planetas.",
                codeTemplate = "planetas = [\"Marte\", \"Saturno\"]\nfor p ___ planetas:\n    print(p)",
                answers = listOf("in"),
                chips = listOf("in", "of", "en", "="),
                correctFeedback = "¡Sí! for p in planetas.",
                wrongFeedback = "Es la misma palabra que usas con range: for i ___ range(3)."
            )
        ),
        walkthrough = listOf(
            step(1, "La lista tiene 2 amigos.", listOf("amigos" to "['Bip', 'Bop']")),
            step(2, "Primera vuelta: amigo vale Bip.", listOf("amigos" to "['Bip', 'Bop']", "amigo" to "'Bip'")),
            step(3, "Saluda a Bip.", listOf("amigos" to "['Bip', 'Bop']", "amigo" to "'Bip'"), listOf("Hola, Bip")),
            step(2, "Segunda vuelta: amigo vale Bop.", listOf("amigos" to "['Bip', 'Bop']", "amigo" to "'Bop'"), listOf("Hola, Bip")),
            step(3, "Saluda a Bop.", listOf("amigos" to "['Bip', 'Bop']", "amigo" to "'Bop'"), listOf("Hola, Bip", "Hola, Bop")),
            step(2, "No quedan más elementos: el for termina.", listOf("amigos" to "['Bip', 'Bop']", "amigo" to "'Bop'"), listOf("Hola, Bip", "Hola, Bop"))
        )
    )

    private val acumular = LessonContent(
        lessonId = "n10_l09",
        title = "Sumar, contar y buscar el mayor 🏆",
        hook = "Glitch jugó 3 partidas. ¿Cuál fue su mejor puntaje? ¿Y cuántos puntos hizo en total?",
        explanation = "Con un for y una variable que se va actualizando puedes sumar, contar o encontrar el mayor. Para el mayor, empieza con el primer elemento y cámbialo cada vez que encuentres uno más grande. Python además trae sum(lista) y max(lista) ya hechos.",
        code = "puntos = [4, 9, 2]\nmayor = puntos[0]\nfor p in puntos:\n    if p > mayor:\n        mayor = p\nprint(mayor)\nprint(sum(puntos))",
        expectedOutput = "9\n15",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "n = [5, 1, 4]\ntotal = 0\nfor x in n:\n    total += x\nprint(total)",
                acceptedAnswers = listOf("10"),
                correctFeedback = "¡Bien! 0 + 5 + 1 + 4 = 10.",
                wrongFeedback = "total empieza en 0 y en cada vuelta se le suma un elemento."
            ),
            FillBlanks(
                prompt = "Cuenta cuántas personas tienen 18 años o más.",
                codeTemplate = "edades = [12, 18, 7, 20]\nadultos = 0\nfor e in edades:\n    if e >= 18:\n        adultos ___ 1\nprint(adultos)",
                answers = listOf("+="),
                chips = listOf("+=", "=", "==", "-="),
                correctFeedback = "¡Eso! El contador sube de 1 en 1: muestra 2.",
                wrongFeedback = "El contador tiene que aumentar cada vez que encuentra un adulto, sin perder lo que llevaba."
            ),
            TypeCode(
                prompt = "Escribe la línea que muestra la suma de la lista ventas, usando sum.",
                acceptedAnswers = listOf("print(sum(ventas))"),
                placeholder = "print(...)",
                correctFeedback = "¡Perfecto! sum suma todos los elementos de una vez.",
                wrongFeedback = "Igual que con len: sum con la lista entre paréntesis, dentro de print."
            )
        ),
        walkthrough = listOf(
            step(1, "Los puntajes de las 3 partidas.", listOf("puntos" to "[4, 9, 2]")),
            step(2, "Por ahora el mayor es el primero.", listOf("puntos" to "[4, 9, 2]", "mayor" to "4")),
            step(3, "Primera vuelta: p vale 4.", listOf("puntos" to "[4, 9, 2]", "mayor" to "4", "p" to "4")),
            step(4, "¿4 > 4? No: mayor no cambia.", listOf("puntos" to "[4, 9, 2]", "mayor" to "4", "p" to "4")),
            step(3, "Segunda vuelta: p vale 9.", listOf("puntos" to "[4, 9, 2]", "mayor" to "4", "p" to "9")),
            step(4, "¿9 > 4? ¡Sí!", listOf("puntos" to "[4, 9, 2]", "mayor" to "4", "p" to "9")),
            step(5, "Hay un nuevo mayor.", listOf("puntos" to "[4, 9, 2]", "mayor" to "9", "p" to "9")),
            step(3, "Tercera vuelta: p vale 2.", listOf("puntos" to "[4, 9, 2]", "mayor" to "9", "p" to "2")),
            step(4, "¿2 > 9? No.", listOf("puntos" to "[4, 9, 2]", "mayor" to "9", "p" to "2")),
            step(6, "El for terminó: se muestra el mayor.", listOf("puntos" to "[4, 9, 2]", "mayor" to "9", "p" to "2"), listOf("9")),
            step(7, "sum suma todo: 4 + 9 + 2.", listOf("puntos" to "[4, 9, 2]", "mayor" to "9", "p" to "2"), listOf("9", "15"))
        )
    )

    private val estaDentro = LessonContent(
        lessonId = "n10_l10",
        title = "¿Está en la lista? in 🔍",
        hook = "Glitch es el portero de la fiesta. Solo pasan los que están en la lista de invitados.",
        explanation = "valor in lista da True si el valor está y False si no. Con un if decides qué hacer. Al revés, valor not in lista da True cuando NO está. Ojo: \"Ana\" y \"ana\" son textos distintos.",
        code = "invitados = [\"Ana\", \"Leo\"]\nnombre = \"Leo\"\nif nombre in invitados:\n    print(\"Pasa\")\nelse:\n    print(\"No estás en la lista\")",
        expectedOutput = "Pasa",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "print(3 in [1, 2, 3])\nprint(5 in [1, 2, 3])",
                acceptedAnswers = listOf("True\nFalse"),
                correctFeedback = "¡Exacto! El 3 está; el 5 no.",
                wrongFeedback = "in responde True o False según si el valor está en la lista."
            ),
            FillBlanks(
                prompt = "Agrega la leche al carrito solo si todavía no está.",
                codeTemplate = "carrito = [\"pan\"]\nif \"leche\" ___ carrito:\n    carrito.append(\"leche\")",
                answers = listOf("not in"),
                chips = listOf("not in", "in", "==", "!="),
                correctFeedback = "¡Bien! not in evita agregarla dos veces.",
                wrongFeedback = "Queremos agregarla cuando NO está en el carrito."
            ),
            FindBug(
                prompt = "Siempre dice que falta pan, aunque sí hay. ¡Toca el error!",
                codeLines = listOf(
                    "compras = [\"pan\", \"leche\"]",
                    "if \"Pan\" in compras:",
                    "    print(\"Hay pan\")",
                    "else:",
                    "    print(\"Falta pan\")"
                ),
                bugLineIndex = 1,
                correctFeedback = "¡Bug encontrado! \"Pan\" con mayúscula no es igual a \"pan\".",
                wrongFeedback = "Compara letra por letra lo que se busca con lo que hay en la lista."
            )
        ),
        walkthrough = listOf(
            step(1, "La lista de invitados.", listOf("invitados" to "['Ana', 'Leo']")),
            step(2, "Llega Leo.", listOf("invitados" to "['Ana', 'Leo']", "nombre" to "'Leo'")),
            step(3, "¿Leo está en invitados? True.", listOf("invitados" to "['Ana', 'Leo']", "nombre" to "'Leo'")),
            step(4, "Entra por el if.", listOf("invitados" to "['Ana', 'Leo']", "nombre" to "'Leo'"), listOf("Pasa"))
        )
    )

    private val listaCompras = LessonContent(
        lessonId = "n10_l11",
        title = "Mini programa: la lista de compras 🛒",
        hook = "Glitch arma su lista del mercado: anota, se arrepiente, borra y al final la revisa.",
        explanation = "Juntamos todo: una lista vacía que crece con append, un remove para corregir, un for para mostrarla bonita y len para contar. Fíjate: remove quita solo el primer \"pan\".",
        code = "compras = []\ncompras.append(\"pan\")\ncompras.append(\"leche\")\ncompras.append(\"pan\")\ncompras.remove(\"pan\")\nfor cosa in compras:\n    print(\"-\", cosa)\nprint(\"Total:\", len(compras))",
        expectedOutput = "- leche\n- pan\nTotal: 2",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "precios = [3, 5]\nprecios.append(2)\nprint(len(precios), sum(precios))",
                acceptedAnswers = listOf("3 10"),
                correctFeedback = "¡Muy bien! 3 precios que suman 10.",
                wrongFeedback = "Después del append hay un precio más. Cuenta cuántos son y súmalos."
            ),
            OrderLines(
                prompt = "Ordena el programa: lista vacía, agrega huevos y muestra cada cosa.",
                lines = listOf(
                    "compras = []",
                    "compras.append(\"huevos\")",
                    "for c in compras:",
                    "    print(c)"
                ),
                correctFeedback = "¡Perfecto! Crear, llenar y recorrer.",
                wrongFeedback = "Primero la lista tiene que existir, luego se llena y al final se recorre."
            ),
            FindBug(
                prompt = "La lista de compras no arranca. ¡Toca el error!",
                codeLines = listOf(
                    "compras = [\"pan\", \"queso\"]",
                    "compras.append(\"jugo\")",
                    "for c in compras",
                    "    print(c)"
                ),
                bugLineIndex = 2,
                correctFeedback = "¡Cazado! Al for le faltan los dos puntos.",
                wrongFeedback = "Revisa cómo termina la línea que abre el bucle."
            )
        ),
        walkthrough = listOf(
            step(1, "La lista empieza vacía.", listOf("compras" to "[]")),
            step(2, "Anota pan.", listOf("compras" to "['pan']")),
            step(3, "Anota leche.", listOf("compras" to "['pan', 'leche']")),
            step(4, "Anota pan otra vez (se le olvidó que ya estaba).", listOf("compras" to "['pan', 'leche', 'pan']")),
            step(5, "remove quita solo el primer pan.", listOf("compras" to "['leche', 'pan']")),
            step(6, "Primera vuelta: cosa vale leche.", listOf("compras" to "['leche', 'pan']", "cosa" to "'leche'")),
            step(7, "Muestra un guion y la cosa.", listOf("compras" to "['leche', 'pan']", "cosa" to "'leche'"), listOf("- leche")),
            step(6, "Segunda vuelta: cosa vale pan.", listOf("compras" to "['leche', 'pan']", "cosa" to "'pan'"), listOf("- leche")),
            step(7, "Muestra el pan.", listOf("compras" to "['leche', 'pan']", "cosa" to "'pan'"), listOf("- leche", "- pan")),
            step(8, "Cuenta lo que quedó.", listOf("compras" to "['leche', 'pan']", "cosa" to "'pan'"), listOf("- leche", "- pan", "Total: 2"))
        )
    )

    // Jefe del nivel: mezcla lo aprendido en las 11 lecciones.
    val boss = BossContent(
        bossId = "n10_boss",
        name = "El Coleccionista Glotón",
        emoji = "🧺",
        intro = "Guarda todo lo que encuentra en listas larguísimas y nunca sabe dónde dejó nada. Para vencerlo tendrás que encontrar, agregar, quitar y contar sin equivocarte de posición.",
        exercises = listOf(
            PredictOutput(
                prompt = "Primer golpe. ¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "tesoros = [\"moneda\", \"gema\", \"llave\"]\nprint(tesoros[1])\nprint(tesoros[-1])",
                acceptedAnswers = listOf("gema\nllave"),
                correctFeedback = "¡Golpe! El índice 1 es el segundo y -1 el último.",
                wrongFeedback = "Cuenta desde 0 para el primero; -1 es el último."
            ),
            PredictOutput(
                prompt = "El coleccionista mete y saca cosas. ¿Qué muestra?",
                code = "bolsa = [1, 2]\nbolsa.append(3)\nbolsa.pop(0)\nprint(bolsa)",
                acceptedAnswers = listOf("[2, 3]", "[2,3]"),
                correctFeedback = "¡Bien! append agrega el 3 al final y pop(0) saca el primero.",
                wrongFeedback = "pop(0) quita el elemento de la posición 0, no el último."
            ),
            FindBug(
                prompt = "El coleccionista cuenta sus gemas, pero el total sale mal. ¡Toca el error!",
                codeLines = listOf(
                    "gemas = [3, 5, 2]",
                    "total = 0",
                    "for g in gemas:",
                    "    total = g",
                    "print(total)"
                ),
                bugLineIndex = 3,
                correctFeedback = "¡Bug golpeado! total = g pisa el valor; debe ser total += g.",
                wrongFeedback = "¿Qué línea debería ir sumando y en cambio reemplaza?"
            ),
            FillBlanks(
                prompt = "Completa el for para mostrar cada cosa del botín.",
                codeTemplate = "botin = [\"oro\", \"plata\"]\nfor ___ in botin:\n    print(cosa)",
                answers = listOf("cosa"),
                chips = listOf("cosa", "botin", "in", "lista"),
                correctFeedback = "¡Eso! La variable del for es la que se usa dentro.",
                wrongFeedback = "Mira qué nombre usa el print: ese es el que toma cada elemento."
            ),
            TypeCode(
                prompt = "Último golpe: escribe la línea que muestra cuántas cosas tiene la lista botin.",
                acceptedAnswers = listOf("print(len(botin))"),
                placeholder = "print(...)",
                correctFeedback = "¡Coleccionista vencido! Ya dominas las listas.",
                wrongFeedback = "len cuenta los elementos; ponlo dentro de print."
            )
        )
    )

    val all: List<LessonContent> = listOf(
        variasCosas, posiciones, negativos, cambiar,
        agregar, quitar, largo, recorrer,
        acumular, estaDentro, listaCompras
    )
}
