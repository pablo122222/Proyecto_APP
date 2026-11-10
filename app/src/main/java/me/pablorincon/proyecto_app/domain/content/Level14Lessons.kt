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

// Nivel 14 · Diccionarios (11 lecciones y el jefe «El Bibliotecario Desordenado»): clave → valor,
// leer, agregar, cambiar y quitar, in y get, recorrerlos, contar y combinarlos con listas.
internal object Level14Lessons {

    private fun step(line: Int, note: String, vars: List<Pair<String, String>>, out: List<String> = emptyList()) =
        WalkStep(line, note, vars, out)

    private val porNombre = LessonContent(
        lessonId = "n14_l01",
        title = "Buscar por nombre, no por posición 📖",
        hook = "Glitch quiere saber la edad de Ana. Con una lista tendría que recordar en qué posición está… ¿y si la busca por su nombre?",
        explanation = "Un diccionario guarda pares clave: valor entre llaves { }. La clave es lo que usas para buscar (como el nombre) y el valor es lo que encuentras (como la edad). Se lee con diccionario[clave], sin índices.",
        code = "edades = {\"Ana\": 12, \"Leo\": 10}\nprint(edades)\nprint(edades[\"Ana\"])",
        expectedOutput = "{'Ana': 12, 'Leo': 10}\n12",
        exercises = listOf(
            TypeCode(
                prompt = "Crea un diccionario llamado precios con la clave \"pan\" y el valor 2.",
                acceptedAnswers = listOf("precios = {\"pan\": 2}"),
                placeholder = "precios = ...",
                correctFeedback = "¡Perfecto! Llaves, la clave, dos puntos y el valor.",
                wrongFeedback = "Se escribe entre llaves { }, con la clave, : y el valor."
            ),
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "d = {\"a\": 1, \"b\": 2}\nprint(d[\"b\"])",
                acceptedAnswers = listOf("2"),
                correctFeedback = "¡Exacto! La clave \"b\" tiene el valor 2.",
                wrongFeedback = "Busca la clave \"b\" y mira qué valor tiene."
            ),
            MultipleChoice(
                prompt = "En {\"Ana\": 12}, ¿qué es \"Ana\"?",
                options = listOf("La clave", "El valor", "El índice", "Una lista"),
                correctIndex = 0,
                correctFeedback = "¡Correcto! \"Ana\" es la clave y 12 su valor.",
                wrongFeedback = "Lo que va antes de los dos puntos es lo que usas para buscar."
            )
        ),
        walkthrough = listOf(
            step(1, "Dos pares: Ana → 12 y Leo → 10.", listOf("edades" to "{'Ana': 12, 'Leo': 10}")),
            step(2, "print muestra el diccionario con sus llaves.", listOf("edades" to "{'Ana': 12, 'Leo': 10}"), listOf("{'Ana': 12, 'Leo': 10}")),
            step(3, "Se busca por la clave \"Ana\".", listOf("edades" to "{'Ana': 12, 'Leo': 10}"), listOf("{'Ana': 12, 'Leo': 10}", "12"))
        )
    )

    private val leer = LessonContent(
        lessonId = "n14_l02",
        title = "Leer un valor por su clave 🔑",
        hook = "La ficha de un robot tiene su nombre y su nivel. Glitch lee cada dato por su etiqueta.",
        explanation = "Las claves suelen ser textos que describen el dato: \"nombre\", \"nivel\". diccionario[\"nombre\"] da el valor. Si la clave no existe, Python se detiene con KeyError. Las mayúsculas importan: \"Nombre\" y \"nombre\" son claves distintas.",
        code = "robot = {\"nombre\": \"Bip\", \"nivel\": 3}\nprint(robot[\"nombre\"])\nprint(\"Nivel:\", robot[\"nivel\"])",
        expectedOutput = "Bip\nNivel: 3",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "capitales = {\"Perú\": \"Lima\", \"Chile\": \"Santiago\"}\nprint(capitales[\"Chile\"])",
                acceptedAnswers = listOf("Santiago"),
                correctFeedback = "¡Bien! El valor de la clave \"Chile\".",
                wrongFeedback = "Busca la clave \"Chile\" en el diccionario."
            ),
            FindBug(
                prompt = "Este programa da KeyError. ¡Toca la línea que lo causa!",
                codeLines = listOf(
                    "notas = {\"mate\": 9, \"arte\": 7}",
                    "print(notas[\"mate\"])",
                    "print(notas[\"música\"])"
                ),
                bugLineIndex = 2,
                correctFeedback = "¡Cazado! No hay ninguna clave \"música\".",
                wrongFeedback = "¿Qué clave se pide que no está en el diccionario?"
            ),
            FillBlanks(
                prompt = "Muestra de qué color es el cielo.",
                codeTemplate = "colores = {\"cielo\": \"azul\"}\nprint(colores[___])",
                answers = listOf("\"cielo\""),
                chips = listOf("\"cielo\"", "cielo", "0", "\"azul\""),
                correctFeedback = "¡Eso! La clave es el texto \"cielo\", con comillas.",
                wrongFeedback = "Se busca por la clave, no por el valor, y la clave es un texto: necesita comillas."
            )
        ),
        walkthrough = listOf(
            step(1, "La ficha del robot.", listOf("robot" to "{'nombre': 'Bip', 'nivel': 3}")),
            step(2, "El valor de \"nombre\".", listOf("robot" to "{'nombre': 'Bip', 'nivel': 3}"), listOf("Bip")),
            step(3, "El valor de \"nivel\".", listOf("robot" to "{'nombre': 'Bip', 'nivel': 3}"), listOf("Bip", "Nivel: 3"))
        )
    )

    private val agregarCambiar = LessonContent(
        lessonId = "n14_l03",
        title = "Agregar y cambiar ✍️",
        hook = "Llegaron peras a la tienda de Glitch, y además contó mal las manzanas.",
        explanation = "diccionario[clave] = valor hace dos cosas: si la clave NO existe, la agrega; si YA existe, cambia su valor. Un diccionario vacío se escribe {}.",
        code = "inventario = {\"manzanas\": 3}\ninventario[\"peras\"] = 5\ninventario[\"manzanas\"] = 10\nprint(inventario)",
        expectedOutput = "{'manzanas': 10, 'peras': 5}",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "d = {\"x\": 1}\nd[\"x\"] = 7\nprint(d[\"x\"])",
                acceptedAnswers = listOf("7"),
                correctFeedback = "¡Exacto! La clave ya existía: se cambió su valor.",
                wrongFeedback = "Si la clave existe, el = reemplaza su valor."
            ),
            TypeCode(
                prompt = "Agrega al diccionario stock la clave \"uvas\" con el valor 4.",
                acceptedAnswers = listOf("stock[\"uvas\"] = 4"),
                placeholder = "stock...",
                correctFeedback = "¡Perfecto!",
                wrongFeedback = "La clave va entre corchetes, y después = con el valor."
            ),
            OrderLines(
                prompt = "Ordena el programa: crea un diccionario vacío, guarda los puntos de Ana y muéstralos.",
                lines = listOf(
                    "puntos = {}",
                    "puntos[\"Ana\"] = 10",
                    "print(puntos[\"Ana\"])"
                ),
                correctFeedback = "¡Muy bien! Muestra 10.",
                wrongFeedback = "El diccionario tiene que existir antes de guardarle algo, y solo después se puede leer."
            )
        ),
        walkthrough = listOf(
            step(1, "Hay 3 manzanas.", listOf("inventario" to "{'manzanas': 3}")),
            step(2, "\"peras\" no existía: se agrega.", listOf("inventario" to "{'manzanas': 3, 'peras': 5}")),
            step(3, "\"manzanas\" ya existía: se cambia su valor.", listOf("inventario" to "{'manzanas': 10, 'peras': 5}")),
            step(4, "Se muestra el inventario actualizado.", listOf("inventario" to "{'manzanas': 10, 'peras': 5}"), listOf("{'manzanas': 10, 'peras': 5}"))
        )
    )

    private val quitar = LessonContent(
        lessonId = "n14_l04",
        title = "Quitar con del y pop 🗑️",
        hook = "Leo se cambió de ciudad y Sol cambió de número. Glitch limpia su agenda.",
        explanation = "del diccionario[clave] borra ese par. diccionario.pop(clave) también lo borra, pero además devuelve el valor para que puedas guardarlo. Ojo: remove es de las listas, no de los diccionarios.",
        code = "agenda = {\"Ana\": 111, \"Leo\": 222, \"Sol\": 333}\ndel agenda[\"Leo\"]\nnumero = agenda.pop(\"Sol\")\nprint(agenda)\nprint(numero)",
        expectedOutput = "{'Ana': 111}\n333",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "d = {\"a\": 1, \"b\": 2, \"c\": 3}\ndel d[\"a\"]\nprint(len(d))",
                acceptedAnswers = listOf("2"),
                correctFeedback = "¡Bien! len también cuenta los pares de un diccionario.",
                wrongFeedback = "Había 3 pares y se borró uno."
            ),
            FillBlanks(
                prompt = "Quita el jugo y guarda su precio en quitado.",
                codeTemplate = "precios = {\"pan\": 2, \"jugo\": 3}\nquitado = precios.___(\"jugo\")\nprint(quitado)",
                answers = listOf("pop"),
                chips = listOf("pop", "del", "remove", "append"),
                correctFeedback = "¡Eso! pop borra y devuelve: muestra 3.",
                wrongFeedback = "Necesitas el método que borra Y devuelve el valor."
            ),
            FindBug(
                prompt = "Este programa da AttributeError. ¡Toca el error!",
                codeLines = listOf(
                    "colores = {\"rojo\": 1, \"azul\": 2}",
                    "colores.remove(\"rojo\")",
                    "print(colores)"
                ),
                bugLineIndex = 1,
                correctFeedback = "¡Cazado! Los diccionarios no tienen remove: usa del colores[\"rojo\"] o pop.",
                wrongFeedback = "¿Qué método es de listas y no de diccionarios?"
            )
        ),
        walkthrough = listOf(
            step(1, "Tres contactos.", listOf("agenda" to "{'Ana': 111, 'Leo': 222, 'Sol': 333}")),
            step(2, "del borra a Leo.", listOf("agenda" to "{'Ana': 111, 'Sol': 333}")),
            step(3, "pop borra a Sol y devuelve su número.", listOf("agenda" to "{'Ana': 111}", "numero" to "333")),
            step(4, "Solo queda Ana.", listOf("agenda" to "{'Ana': 111}", "numero" to "333"), listOf("{'Ana': 111}")),
            step(5, "Y el número de Sol quedó guardado.", listOf("agenda" to "{'Ana': 111}", "numero" to "333"), listOf("{'Ana': 111}", "333"))
        )
    )

    private val existe = LessonContent(
        lessonId = "n14_l05",
        title = "¿Existe la clave? in y get 🛡️",
        hook = "Glitch no quiere que su programa explote con KeyError cada vez que alguien pide algo que no hay.",
        explanation = "clave in diccionario dice si la clave existe (True o False). diccionario.get(clave) devuelve el valor, o None si no existe, sin error. Con get(clave, otro) eliges qué devolver cuando no está.",
        code = "precios = {\"pan\": 2, \"leche\": 3}\nprint(\"pan\" in precios)\nprint(precios.get(\"queso\"))\nprint(precios.get(\"queso\", 0))",
        expectedOutput = "True\nNone\n0",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "d = {\"sol\": 1}\nprint(\"luna\" in d)\nprint(d.get(\"luna\", \"no hay\"))",
                acceptedAnswers = listOf("False\nno hay"),
                correctFeedback = "¡Exacto! No existe, así que get devuelve el valor de reserva.",
                wrongFeedback = "¿Está la clave \"luna\"? Si no, get devuelve su segundo valor."
            ),
            FillBlanks(
                prompt = "Revisa si hay borradores antes de leerlos.",
                codeTemplate = "stock = {\"lápiz\": 5}\nif \"borrador\" ___ stock:\n    print(stock[\"borrador\"])\nelse:\n    print(\"Agotado\")",
                answers = listOf("in"),
                chips = listOf("in", "not", "get", "=="),
                correctFeedback = "¡Bien! Así nunca se pide una clave que no existe. Muestra Agotado.",
                wrongFeedback = "Es la misma palabra que usas con listas para saber si algo está."
            ),
            MultipleChoice(
                prompt = "¿Qué devuelve precios.get(\"queso\") si no existe la clave \"queso\"?",
                options = listOf("None", "0", "Un error KeyError", "\"queso\""),
                correctIndex = 0,
                correctFeedback = "¡Correcto! get nunca da KeyError: devuelve None.",
                wrongFeedback = "La gracia de get es que no se detiene con error."
            )
        ),
        walkthrough = listOf(
            step(1, "Dos productos con precio.", listOf("precios" to "{'pan': 2, 'leche': 3}")),
            step(2, "La clave \"pan\" sí existe.", listOf("precios" to "{'pan': 2, 'leche': 3}"), listOf("True")),
            step(3, "No hay queso: get devuelve None, sin error.", listOf("precios" to "{'pan': 2, 'leche': 3}"), listOf("True", "None")),
            step(4, "Con un valor de reserva, devuelve 0.", listOf("precios" to "{'pan': 2, 'leche': 3}"), listOf("True", "None", "0"))
        )
    )

    private val recorrerClaves = LessonContent(
        lessonId = "n14_l06",
        title = "Recorrer claves 🔁",
        hook = "Glitch quiere mostrar la edad de cada uno de sus amigos, sin escribirlos uno por uno.",
        explanation = "for clave in diccionario: recorre las claves, en el orden en que se agregaron. Dentro del for, diccionario[clave] te da el valor de cada una.",
        code = "edades = {\"Ana\": 12, \"Leo\": 10}\nfor nombre in edades:\n    print(nombre, edades[nombre])",
        expectedOutput = "Ana 12\nLeo 10",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "d = {\"x\": 1, \"y\": 2}\nfor k in d:\n    print(k)",
                acceptedAnswers = listOf("x\ny"),
                correctFeedback = "¡Muy bien! Recorrer un diccionario da sus claves.",
                wrongFeedback = "El for sobre un diccionario recorre las claves, no los valores."
            ),
            FillBlanks(
                prompt = "Completa el for que recorre los productos.",
                codeTemplate = "stock = {\"pan\": 2, \"jugo\": 0}\nfor producto ___ stock:\n    print(producto)",
                answers = listOf("in"),
                chips = listOf("in", "of", "=", "en"),
                correctFeedback = "¡Eso! Igual que con listas y textos.",
                wrongFeedback = "Es la palabra de siempre en un for."
            ),
            FindBug(
                prompt = "Este programa da KeyError. ¡Toca el error!",
                codeLines = listOf(
                    "notas = {\"mate\": 9, \"arte\": 7}",
                    "for materia in notas:",
                    "    print(materia, notas[\"materia\"])"
                ),
                bugLineIndex = 2,
                correctFeedback = "¡Cazado! Con comillas busca la clave \"materia\", que no existe. Debe ser notas[materia].",
                wrongFeedback = "¿Se busca el valor de la variable materia o del texto \"materia\"?"
            )
        ),
        walkthrough = listOf(
            step(1, "Dos amigos con su edad.", listOf("edades" to "{'Ana': 12, 'Leo': 10}")),
            step(2, "Primera clave: Ana.", listOf("edades" to "{'Ana': 12, 'Leo': 10}", "nombre" to "'Ana'")),
            step(3, "edades[\"Ana\"] es 12.", listOf("edades" to "{'Ana': 12, 'Leo': 10}", "nombre" to "'Ana'"), listOf("Ana 12")),
            step(2, "Segunda clave: Leo.", listOf("edades" to "{'Ana': 12, 'Leo': 10}", "nombre" to "'Leo'"), listOf("Ana 12")),
            step(3, "edades[\"Leo\"] es 10.", listOf("edades" to "{'Ana': 12, 'Leo': 10}", "nombre" to "'Leo'"), listOf("Ana 12", "Leo 10"))
        )
    )

    private val items = LessonContent(
        lessonId = "n14_l07",
        title = "Recorrer con items() 🗂️",
        hook = "Glitch quiere la clave y el valor al mismo tiempo, sin buscarlos dos veces.",
        explanation = "diccionario.items() da los pares (clave, valor). En el for se reciben en dos variables: for producto, precio in precios.items():. Es la forma más clara de recorrer un diccionario.",
        code = "precios = {\"pan\": 2, \"jugo\": 3}\nfor producto, precio in precios.items():\n    print(producto, \"cuesta\", precio)",
        expectedOutput = "pan cuesta 2\njugo cuesta 3",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "d = {\"a\": 1, \"b\": 2}\ntotal = 0\nfor k, v in d.items():\n    total += v\nprint(total)",
                acceptedAnswers = listOf("3"),
                correctFeedback = "¡Exacto! Se suman los valores: 1 + 2.",
                wrongFeedback = "v toma cada valor del diccionario. ¿Cuánto suman?"
            ),
            TypeCode(
                prompt = "Escribe la primera línea de un for que recorre notas con items(), usando las variables materia y nota.",
                acceptedAnswers = listOf("for materia, nota in notas.items():"),
                placeholder = "for ...",
                correctFeedback = "¡Perfecto!",
                wrongFeedback = "Dos variables separadas por coma, in, y el diccionario con .items() y los dos puntos."
            ),
            OrderLines(
                prompt = "Ordena el programa para mostrar cada nombre con su edad.",
                lines = listOf(
                    "edades = {\"Ana\": 12}",
                    "for nombre, edad in edades.items():",
                    "    print(nombre, edad)"
                ),
                correctFeedback = "¡Muy bien!",
                wrongFeedback = "Primero el diccionario, luego el for y el print con sangría."
            )
        ),
        walkthrough = listOf(
            step(1, "Dos productos con su precio.", listOf("precios" to "{'pan': 2, 'jugo': 3}")),
            step(2, "Primer par: pan y 2.", listOf("precios" to "{'pan': 2, 'jugo': 3}", "producto" to "'pan'", "precio" to "2")),
            step(3, "Se muestra.", listOf("precios" to "{'pan': 2, 'jugo': 3}", "producto" to "'pan'", "precio" to "2"), listOf("pan cuesta 2")),
            step(2, "Segundo par: jugo y 3.", listOf("precios" to "{'pan': 2, 'jugo': 3}", "producto" to "'jugo'", "precio" to "3"), listOf("pan cuesta 2")),
            step(3, "Se muestra.", listOf("precios" to "{'pan': 2, 'jugo': 3}", "producto" to "'jugo'", "precio" to "3"), listOf("pan cuesta 2", "jugo cuesta 3"))
        )
    )

    private val contar = LessonContent(
        lessonId = "n14_l08",
        title = "Contar con un diccionario 🧮",
        hook = "En Glitch City votaron por la mascota oficial. Glitch cuenta los votos de cada una.",
        explanation = "Un diccionario es perfecto para contar: la clave es lo que cuentas y el valor, cuántas veces apareció. El truco: conteo.get(v, 0) + 1 suma uno, y si es la primera vez parte de 0 en lugar de dar KeyError.",
        code = "votos = [\"gato\", \"perro\", \"gato\"]\nconteo = {}\nfor v in votos:\n    conteo[v] = conteo.get(v, 0) + 1\nprint(conteo)",
        expectedOutput = "{'gato': 2, 'perro': 1}",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "letras = \"aba\"\nc = {}\nfor l in letras:\n    c[l] = c.get(l, 0) + 1\nprint(c[\"a\"])",
                acceptedAnswers = listOf("2"),
                correctFeedback = "¡Bien! La a aparece dos veces.",
                wrongFeedback = "Se cuenta cada letra de \"aba\". ¿Cuántas a hay?"
            ),
            FindBug(
                prompt = "Este programa da KeyError en la primera vuelta. ¡Toca el error!",
                codeLines = listOf(
                    "votos = [\"sí\", \"no\", \"sí\"]",
                    "conteo = {}",
                    "for v in votos:",
                    "    conteo[v] = conteo[v] + 1"
                ),
                bugLineIndex = 3,
                correctFeedback = "¡Cazado! La primera vez la clave no existe. Usa conteo.get(v, 0) + 1.",
                wrongFeedback = "Al principio el diccionario está vacío. ¿Qué línea pide una clave que todavía no hay?"
            ),
            FillBlanks(
                prompt = "Completa: si es la primera vez, el conteo debe partir de cero.",
                codeTemplate = "conteo = {}\nfor v in [\"a\", \"a\"]:\n    conteo[v] = conteo.get(v, ___) + 1",
                answers = listOf("0"),
                chips = listOf("0", "1", "v", "None"),
                correctFeedback = "¡Eso! 0 + 1 = 1 la primera vez.",
                wrongFeedback = "Antes del primer voto, ¿cuántos votos tenía?"
            )
        ),
        walkthrough = listOf(
            step(1, "Los votos.", listOf("votos" to "['gato', 'perro', 'gato']")),
            step(2, "El conteo empieza vacío.", listOf("votos" to "['gato', 'perro', 'gato']", "conteo" to "{}")),
            step(3, "Primer voto: gato.", listOf("conteo" to "{}", "v" to "'gato'")),
            step(4, "No estaba: get da 0, más 1.", listOf("conteo" to "{'gato': 1}", "v" to "'gato'")),
            step(3, "Segundo voto: perro.", listOf("conteo" to "{'gato': 1}", "v" to "'perro'")),
            step(4, "Primera vez de perro.", listOf("conteo" to "{'gato': 1, 'perro': 1}", "v" to "'perro'")),
            step(3, "Tercer voto: gato.", listOf("conteo" to "{'gato': 1, 'perro': 1}", "v" to "'gato'")),
            step(4, "Gato ya tenía 1: ahora 2.", listOf("conteo" to "{'gato': 2, 'perro': 1}", "v" to "'gato'")),
            step(5, "Se muestra el conteo.", listOf("conteo" to "{'gato': 2, 'perro': 1}", "v" to "'gato'"), listOf("{'gato': 2, 'perro': 1}"))
        )
    )

    private val listasDentro = LessonContent(
        lessonId = "n14_l09",
        title = "Listas dentro de diccionarios 🎒",
        hook = "Cada amigo de Glitch tiene una mochila con varias cosas. ¿Cómo guardarlas todas?",
        explanation = "El valor de una clave puede ser una lista. mochilas[\"Leo\"] es una lista, así que puedes usar append, len o índices sobre ella: mochilas[\"Leo\"].append(\"mapa\") o mochilas[\"Ana\"][0].",
        code = "mochilas = {\"Ana\": [\"lápiz\", \"regla\"], \"Leo\": [\"libro\"]}\nmochilas[\"Leo\"].append(\"mapa\")\nprint(mochilas[\"Leo\"])\nprint(len(mochilas[\"Ana\"]))",
        expectedOutput = "['libro', 'mapa']\n2",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "d = {\"notas\": [7, 9]}\nprint(d[\"notas\"][1])",
                acceptedAnswers = listOf("9"),
                correctFeedback = "¡Exacto! Primero la clave, luego el índice de la lista.",
                wrongFeedback = "d[\"notas\"] es la lista [7, 9]. ¿Qué hay en su posición 1?"
            ),
            FillBlanks(
                prompt = "Agrega a Bop a la lista de jugadores.",
                codeTemplate = "equipo = {\"jugadores\": [\"Bip\"]}\nequipo[\"jugadores\"].___(\"Bop\")",
                answers = listOf("append"),
                chips = listOf("append", "pop", "get", "items"),
                correctFeedback = "¡Bien! La lista queda ['Bip', 'Bop'].",
                wrongFeedback = "equipo[\"jugadores\"] es una lista. ¿Cómo se agrega algo al final de una lista?"
            ),
            TypeCode(
                prompt = "Escribe la línea que muestra el primer elemento de la lista guardada en tareas[\"lunes\"].",
                acceptedAnswers = listOf("print(tareas[\"lunes\"][0])"),
                placeholder = "print(...)",
                correctFeedback = "¡Perfecto! La clave y después el índice.",
                wrongFeedback = "Primero tareas[\"lunes\"] (la lista) y después [0], todo dentro de print."
            )
        ),
        walkthrough = listOf(
            step(1, "Cada clave tiene una lista como valor.", listOf("mochilas" to "{'Ana': ['lápiz', 'regla'], 'Leo': ['libro']}")),
            step(2, "Se agrega un mapa a la lista de Leo.", listOf("mochilas" to "{'Ana': ['lápiz', 'regla'], 'Leo': ['libro', 'mapa']}")),
            step(3, "La lista de Leo.", listOf("mochilas" to "{'Ana': ['lápiz', 'regla'], 'Leo': ['libro', 'mapa']}"), listOf("['libro', 'mapa']")),
            step(4, "Ana tiene 2 cosas.", listOf("mochilas" to "{'Ana': ['lápiz', 'regla'], 'Leo': ['libro', 'mapa']}"), listOf("['libro', 'mapa']", "2"))
        )
    )

    private val elegir = LessonContent(
        lessonId = "n14_l10",
        title = "Elegir entre lista y diccionario ⚖️",
        hook = "Glitch tiene dos tareas: guardar el orden de llegada a la fila y guardar el teléfono de cada amigo. ¿Qué usa para cada una?",
        explanation = "Usa una lista cuando importa el ORDEN o la posición (la fila, los turnos, un ranking). Usa un diccionario cuando quieres BUSCAR por un nombre o etiqueta (teléfono de cada persona, precio de cada producto).",
        code = "fila = [\"Ana\", \"Leo\", \"Sol\"]\ntelefonos = {\"Ana\": 111, \"Leo\": 222}\nprint(fila[0])\nprint(telefonos[\"Leo\"])",
        expectedOutput = "Ana\n222",
        exercises = listOf(
            MultipleChoice(
                prompt = "Quieres guardar el puntaje de cada jugador y buscarlo por su nombre. ¿Qué usas?",
                options = listOf("Un diccionario", "Una lista", "Un texto", "Una variable por jugador"),
                correctIndex = 0,
                correctFeedback = "¡Correcto! nombre → puntaje es justo un par clave: valor.",
                wrongFeedback = "Buscas por nombre, no por posición."
            ),
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "orden = [\"primero\", \"segundo\"]\npesos = {\"Bip\": 5}\nprint(orden[1], pesos[\"Bip\"])",
                acceptedAnswers = listOf("segundo 5"),
                correctFeedback = "¡Bien! Índice para la lista, clave para el diccionario.",
                wrongFeedback = "orden[1] es el de la posición 1; pesos[\"Bip\"] es el valor de esa clave."
            ),
            FillBlanks(
                prompt = "Guarda el orden de llegada: ¿qué estructura vacía hay que crear?",
                codeTemplate = "llegadas = ___\nllegadas.append(\"Ana\")\nllegadas.append(\"Leo\")",
                answers = listOf("[]"),
                chips = listOf("[]", "{}", "\"\"", "0"),
                correctFeedback = "¡Eso! Para un orden, una lista.",
                wrongFeedback = "append es de las listas. ¿Cómo se escribe una lista vacía?"
            )
        ),
        walkthrough = listOf(
            step(1, "Una lista: importa el orden.", listOf("fila" to "['Ana', 'Leo', 'Sol']")),
            step(2, "Un diccionario: se busca por nombre.", listOf("fila" to "['Ana', 'Leo', 'Sol']", "telefonos" to "{'Ana': 111, 'Leo': 222}")),
            step(3, "Quién va primero en la fila.", listOf("fila" to "['Ana', 'Leo', 'Sol']", "telefonos" to "{'Ana': 111, 'Leo': 222}"), listOf("Ana")),
            step(4, "El teléfono de Leo.", listOf("fila" to "['Ana', 'Leo', 'Sol']", "telefonos" to "{'Ana': 111, 'Leo': 222}"), listOf("Ana", "222"))
        )
    )

    private val agenda = LessonContent(
        lessonId = "n14_l11",
        title = "Mini programa: la agenda 📒",
        hook = "Glitch arma su agenda de contactos: agrega amigos y busca un número sin que nada explote.",
        explanation = "Juntamos todo: un diccionario vacío que se llena con agenda[nombre] = teléfono, un in para revisar antes de buscar y len para contar los contactos.",
        code = "agenda = {}\nagenda[\"Ana\"] = \"300 111\"\nagenda[\"Leo\"] = \"300 222\"\nnombre = \"Leo\"\nif nombre in agenda:\n    print(nombre, \"->\", agenda[nombre])\nelse:\n    print(\"No está en la agenda\")\nprint(\"Contactos:\", len(agenda))",
        expectedOutput = "Leo -> 300 222\nContactos: 2",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "a = {\"Ana\": 1}\na[\"Bo\"] = 2\na[\"Ana\"] = 3\nprint(a[\"Ana\"], len(a))",
                acceptedAnswers = listOf("3 2"),
                correctFeedback = "¡Muy bien! Ana cambió a 3 y hay 2 contactos.",
                wrongFeedback = "Bo es nuevo (se agrega); Ana ya existía (se cambia)."
            ),
            FindBug(
                prompt = "La agenda explota con KeyError al buscar a Sol. ¡Toca el error!",
                codeLines = listOf(
                    "agenda = {\"Ana\": \"111\"}",
                    "nombre = \"Sol\"",
                    "print(agenda[nombre])"
                ),
                bugLineIndex = 2,
                correctFeedback = "¡Cazado! Sol no está. Revisa con in, o usa agenda.get(nombre, \"No está\").",
                wrongFeedback = "¿Qué línea pide una clave que no existe?"
            ),
            OrderLines(
                prompt = "Ordena el programa: agenda vacía, agrega a Sol y muestra todos los contactos.",
                lines = listOf(
                    "agenda = {}",
                    "agenda[\"Sol\"] = \"333\"",
                    "for nombre, tel in agenda.items():",
                    "    print(nombre, tel)"
                ),
                correctFeedback = "¡Perfecto! Crear, llenar y recorrer.",
                wrongFeedback = "Primero el diccionario, luego se llena y al final se recorre."
            )
        ),
        walkthrough = listOf(
            step(1, "La agenda empieza vacía.", listOf("agenda" to "{}")),
            step(2, "Se agrega Ana.", listOf("agenda" to "{'Ana': '300 111'}")),
            step(3, "Se agrega Leo.", listOf("agenda" to "{'Ana': '300 111', 'Leo': '300 222'}")),
            step(4, "Vamos a buscar a Leo.", listOf("agenda" to "{'Ana': '300 111', 'Leo': '300 222'}", "nombre" to "'Leo'")),
            step(5, "¿Leo está en la agenda? Sí.", listOf("agenda" to "{'Ana': '300 111', 'Leo': '300 222'}", "nombre" to "'Leo'")),
            step(6, "Se muestra su número, sin riesgo de KeyError.", listOf("agenda" to "{'Ana': '300 111', 'Leo': '300 222'}", "nombre" to "'Leo'"), listOf("Leo -> 300 222")),
            step(9, "Se cuentan los contactos.", listOf("agenda" to "{'Ana': '300 111', 'Leo': '300 222'}", "nombre" to "'Leo'"), listOf("Leo -> 300 222", "Contactos: 2"))
        )
    )

    // Jefe del nivel: mezcla lo aprendido en las 11 lecciones.
    val boss = BossContent(
        bossId = "n14_boss",
        name = "El Bibliotecario Desordenado",
        emoji = "📚",
        intro = "Tiene miles de libros y nunca encuentra ninguno. Para vencerlo, ordena su biblioteca con diccionarios: busca, presta, cuenta y devuelve sin un solo KeyError.",
        exercises = listOf(
            PredictOutput(
                prompt = "Primer golpe: alguien devolvió un libro. ¿Qué muestra este programa?",
                code = "libros = {\"Dune\": 3, \"Momo\": 1}\nlibros[\"Momo\"] += 1\nprint(libros[\"Momo\"])",
                acceptedAnswers = listOf("2"),
                correctFeedback = "¡Golpe! Momo tenía 1 y ahora 2.",
                wrongFeedback = "+= 1 suma uno al valor de esa clave."
            ),
            PredictOutput(
                prompt = "El bibliotecario ordena sus estantes. ¿Qué muestra?",
                code = "estantes = {\"A\": [\"Dune\"], \"B\": []}\nestantes[\"B\"].append(\"Momo\")\nprint(len(estantes[\"A\"]) + len(estantes[\"B\"]))",
                acceptedAnswers = listOf("2"),
                correctFeedback = "¡Bien! Un libro en cada estante.",
                wrongFeedback = "Cuenta los libros de la lista de A y los de la lista de B."
            ),
            FindBug(
                prompt = "Este programa da error al recorrer los préstamos. ¡Toca el error!",
                codeLines = listOf(
                    "prestados = {\"Ana\": \"Dune\"}",
                    "for persona, libro in prestados:",
                    "    print(persona, libro)"
                ),
                bugLineIndex = 1,
                correctFeedback = "¡Bug golpeado! Para recibir clave y valor hace falta prestados.items().",
                wrongFeedback = "¿Qué método da los pares clave y valor juntos?"
            ),
            FillBlanks(
                prompt = "Busca cuántos Momo hay, sin error si no existe.",
                codeTemplate = "libros = {\"Dune\": 3}\nprint(libros.___(\"Momo\", 0))",
                answers = listOf("get"),
                chips = listOf("get", "pop", "items", "in"),
                correctFeedback = "¡Eso! get devuelve 0 en lugar de dar KeyError.",
                wrongFeedback = "Necesitas el método que devuelve un valor de reserva si la clave no está."
            ),
            TypeCode(
                prompt = "Último golpe: borra del diccionario libros la clave \"Dune\" usando del.",
                acceptedAnswers = listOf("del libros[\"Dune\"]"),
                placeholder = "del ...",
                correctFeedback = "¡Bibliotecario vencido! Ya dominas los diccionarios.",
                wrongFeedback = "del, el diccionario y la clave entre corchetes."
            )
        )
    )

    val all: List<LessonContent> = listOf(
        porNombre, leer, agregarCambiar, quitar,
        existe, recorrerClaves, items, contar,
        listasDentro, elegir, agenda
    )
}
