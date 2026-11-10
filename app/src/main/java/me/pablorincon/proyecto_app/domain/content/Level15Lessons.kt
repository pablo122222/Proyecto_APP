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

// Nivel 15 · Proyecto final (11 lecciones y el jefe «El Glitch Supremo»): un solo proyecto,
// el inventario de la tienda de Glitch City, construido pieza por pieza con todo lo aprendido.
internal object Level15Lessons {

    private fun step(line: Int, note: String, vars: List<Pair<String, String>>, out: List<String> = emptyList()) =
        WalkStep(line, note, vars, out)

    private val planear = LessonContent(
        lessonId = "n15_l01",
        title = "Planear antes de programar 🗺️",
        hook = "La tienda de Glitch City es un caos: nadie sabe cuánto queda de cada cosa. Glitch va a programar su inventario… pero primero, un plan.",
        explanation = "Antes de escribir código, piensa: ¿qué DATOS hay? (cada producto con su cantidad → un diccionario). ¿Qué ACCIONES? (mostrar, agregar, vender, avisar poco stock, buscar → una función para cada una). ¿Cómo lo USA la gente? (un menú). Los comentarios con # sirven para anotar el plan: Python los ignora.",
        code = "# Inventario de la tienda de Glitch City\n# Datos: producto -> cantidad\n# Acciones: mostrar, agregar, vender, buscar\nprint(\"Plan listo\")",
        expectedOutput = "Plan listo",
        exercises = listOf(
            MultipleChoice(
                prompt = "¿Qué estructura conviene para guardar la cantidad de cada producto y buscarla por su nombre?",
                options = listOf("Un diccionario", "Una lista", "Un texto largo", "Un solo número"),
                correctIndex = 0,
                correctFeedback = "¡Correcto! producto → cantidad es un par clave: valor.",
                wrongFeedback = "Se busca por el nombre del producto, no por una posición."
            ),
            OrderLines(
                prompt = "Ordena los pasos del plan, del primero al último.",
                lines = listOf(
                    "# 1. Definir los datos",
                    "# 2. Escribir las funciones",
                    "# 3. Armar el menú"
                ),
                correctFeedback = "¡Perfecto! Datos, funciones y por último el menú que las usa.",
                wrongFeedback = "Las funciones necesitan los datos, y el menú necesita las funciones."
            ),
            FillBlanks(
                prompt = "Completa para que esta línea sea un comentario.",
                codeTemplate = "___ Este texto es un comentario",
                answers = listOf("#"),
                chips = listOf("#", "//", "--", "print"),
                correctFeedback = "¡Sí! En Python los comentarios empiezan con #.",
                wrongFeedback = "Mira cómo empiezan las líneas del plan en el ejemplo."
            )
        ),
        walkthrough = listOf(
            step(1, "Un comentario: Python lo salta.", emptyList()),
            step(2, "Otro comentario con el plan de los datos.", emptyList()),
            step(3, "Y el de las acciones.", emptyList()),
            step(4, "La única línea que hace algo.", emptyList(), listOf("Plan listo"))
        )
    )

    private val datos = LessonContent(
        lessonId = "n15_l02",
        title = "Los datos: un diccionario de productos 📦",
        hook = "Primer paso del proyecto: anotar qué hay en la tienda.",
        explanation = "El inventario es un diccionario: cada clave es un producto y su valor, la cantidad que queda. Un producto con 0 sigue en el inventario (existe, pero está agotado).",
        code = "inventario = {\"manzana\": 10, \"pan\": 4, \"jugo\": 0}\nprint(len(inventario))\nprint(inventario[\"pan\"])",
        expectedOutput = "3\n4",
        exercises = listOf(
            TypeCode(
                prompt = "Agrega al inventario la clave \"queso\" con 6 unidades.",
                acceptedAnswers = listOf("inventario[\"queso\"] = 6"),
                placeholder = "inventario...",
                correctFeedback = "¡Perfecto! Nuevo producto en la tienda.",
                wrongFeedback = "La clave entre corchetes y después = con la cantidad."
            ),
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "inventario = {\"manzana\": 10, \"pan\": 4}\ninventario[\"pan\"] = 9\nprint(inventario[\"pan\"] + inventario[\"manzana\"])",
                acceptedAnswers = listOf("19"),
                correctFeedback = "¡Bien! 9 panes + 10 manzanas.",
                wrongFeedback = "El pan cambió a 9 antes de sumar."
            ),
            FindBug(
                prompt = "El inventario no arranca: da SyntaxError. ¡Toca el error!",
                codeLines = listOf(
                    "inventario = {\"manzana\": 10, \"pan\" 4}",
                    "print(len(inventario))",
                    "print(inventario)"
                ),
                bugLineIndex = 0,
                correctFeedback = "¡Cazado! Entre \"pan\" y 4 faltan los dos puntos.",
                wrongFeedback = "Revisa cómo se separa cada clave de su valor."
            )
        ),
        walkthrough = listOf(
            step(1, "Tres productos; el jugo está agotado.", listOf("inventario" to "{'manzana': 10, 'pan': 4, 'jugo': 0}")),
            step(2, "Hay 3 productos distintos.", listOf("inventario" to "{'manzana': 10, 'pan': 4, 'jugo': 0}"), listOf("3")),
            step(3, "Quedan 4 panes.", listOf("inventario" to "{'manzana': 10, 'pan': 4, 'jugo': 0}"), listOf("3", "4"))
        )
    )

    private val mostrar = LessonContent(
        lessonId = "n15_l03",
        title = "Mostrar el inventario 📋",
        hook = "Glitch quiere ver todo lo que hay en la tienda, producto por producto.",
        explanation = "La primera función del proyecto: mostrar(inv) recibe el inventario y lo recorre con items() para mostrar cada producto con su cantidad. Al recibir el diccionario como parámetro, sirve para cualquier inventario.",
        code = "inventario = {\"manzana\": 10, \"pan\": 4}\ndef mostrar(inv):\n    for producto, cantidad in inv.items():\n        print(producto, \":\", cantidad)\nmostrar(inventario)",
        expectedOutput = "manzana : 10\npan : 4",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "inv = {\"jugo\": 2}\nfor p, c in inv.items():\n    print(f\"{p}: {c}\")",
                acceptedAnswers = listOf("jugo: 2"),
                correctFeedback = "¡Exacto! La f-string arma el texto con el producto y su cantidad.",
                wrongFeedback = "p es la clave y c el valor. ¿Cómo queda el texto con las llaves reemplazadas?"
            ),
            FillBlanks(
                prompt = "Completa para recorrer producto y cantidad a la vez.",
                codeTemplate = "def mostrar(inv):\n    for producto, cantidad in inv.___():\n        print(producto, cantidad)",
                answers = listOf("items"),
                chips = listOf("items", "get", "pop", "append"),
                correctFeedback = "¡Bien! items da los pares.",
                wrongFeedback = "¿Qué método da clave y valor juntos?"
            ),
            OrderLines(
                prompt = "Ordena el programa: una función que muestra el título del inventario, y su llamada.",
                lines = listOf(
                    "def titulo():",
                    "    print(\"=== INVENTARIO ===\")",
                    "titulo()"
                ),
                correctFeedback = "¡Perfecto! Definir y luego llamar.",
                wrongFeedback = "La función se define primero; la llamada va al final, sin sangría."
            )
        ),
        walkthrough = listOf(
            step(1, "El inventario.", listOf("inventario" to "{'manzana': 10, 'pan': 4}")),
            step(2, "Se guarda la función mostrar.", listOf("inventario" to "{'manzana': 10, 'pan': 4}")),
            step(5, "Se llama con el inventario.", listOf("inv" to "{'manzana': 10, 'pan': 4}")),
            step(3, "Primer par: manzana, 10.", listOf("inv" to "{'manzana': 10, 'pan': 4}", "producto" to "'manzana'", "cantidad" to "10")),
            step(4, "Se muestra.", listOf("inv" to "{'manzana': 10, 'pan': 4}", "producto" to "'manzana'", "cantidad" to "10"), listOf("manzana : 10")),
            step(3, "Segundo par: pan, 4.", listOf("inv" to "{'manzana': 10, 'pan': 4}", "producto" to "'pan'", "cantidad" to "4"), listOf("manzana : 10")),
            step(4, "Se muestra.", listOf("inv" to "{'manzana': 10, 'pan': 4}", "producto" to "'pan'", "cantidad" to "4"), listOf("manzana : 10", "pan : 4"))
        )
    )

    private val agregar = LessonContent(
        lessonId = "n15_l04",
        title = "Función para agregar productos ➕",
        hook = "Llegó el camión con mercancía: más pan, y un producto nuevo, queso.",
        explanation = "agregar suma unidades a un producto. Con inv.get(producto, 0) funciona tanto para productos que ya existen como para nuevos. Fíjate: el diccionario se modifica DENTRO de la función y el cambio se ve afuera, porque es el mismo diccionario (con los números no pasaba eso).",
        code = "inventario = {\"pan\": 4}\ndef agregar(inv, producto, cantidad):\n    inv[producto] = inv.get(producto, 0) + cantidad\nagregar(inventario, \"pan\", 2)\nagregar(inventario, \"queso\", 5)\nprint(inventario)",
        expectedOutput = "{'pan': 6, 'queso': 5}",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "def agregar(inv, producto, cantidad):\n    inv[producto] = inv.get(producto, 0) + cantidad\ninv = {}\nagregar(inv, \"uva\", 3)\nagregar(inv, \"uva\", 1)\nprint(inv[\"uva\"])",
                acceptedAnswers = listOf("4"),
                correctFeedback = "¡Muy bien! 0 + 3 la primera vez, y 3 + 1 la segunda.",
                wrongFeedback = "La primera vez no hay uvas (get da 0). La segunda ya hay 3."
            ),
            FindBug(
                prompt = "Glitch tenía 4 panes, agregó 2 y le quedan… 2. ¡Toca el error!",
                codeLines = listOf(
                    "def agregar(inv, producto, cantidad):",
                    "    inv[producto] = cantidad",
                    "inv = {\"pan\": 4}",
                    "agregar(inv, \"pan\", 2)",
                    "print(inv[\"pan\"])"
                ),
                bugLineIndex = 1,
                correctFeedback = "¡Cazado! Reemplaza en lugar de sumar. Debe ser inv.get(producto, 0) + cantidad.",
                wrongFeedback = "¿Qué línea pisa lo que ya había en vez de sumarle?"
            ),
            FillBlanks(
                prompt = "Completa: un producto nuevo empieza desde cero unidades.",
                codeTemplate = "def agregar(inv, producto, cantidad):\n    inv[producto] = inv.get(producto, ___) + cantidad",
                answers = listOf("0"),
                chips = listOf("0", "1", "cantidad", "producto"),
                correctFeedback = "¡Eso! Si no existía, parte de 0.",
                wrongFeedback = "Antes de llegar el camión, ¿cuántos había de un producto nuevo?"
            )
        ),
        walkthrough = listOf(
            step(1, "Hay 4 panes.", listOf("inventario" to "{'pan': 4}")),
            step(2, "Se guarda la función.", listOf("inventario" to "{'pan': 4}")),
            step(4, "Llegan 2 panes.", listOf("inventario" to "{'pan': 4}", "producto" to "'pan'", "cantidad" to "2")),
            step(3, "4 + 2 = 6.", listOf("inventario" to "{'pan': 6}", "producto" to "'pan'", "cantidad" to "2")),
            step(5, "Llega queso, que es nuevo.", listOf("inventario" to "{'pan': 6}", "producto" to "'queso'", "cantidad" to "5")),
            step(3, "get da 0: 0 + 5 = 5.", listOf("inventario" to "{'pan': 6, 'queso': 5}", "producto" to "'queso'", "cantidad" to "5")),
            step(6, "El inventario cambió fuera de la función.", listOf("inventario" to "{'pan': 6, 'queso': 5}"), listOf("{'pan': 6, 'queso': 5}"))
        )
    )

    private val vender = LessonContent(
        lessonId = "n15_l05",
        title = "Función para vender (con validaciones) 💰",
        hook = "Un cliente pide jugo que no existe, otro pide 9 panes cuando solo hay 4… Glitch tiene que revisar antes de vender.",
        explanation = "vender valida antes de restar: si el producto no existe, o si no hay suficiente, devuelve un mensaje y sale con return sin tocar nada. Solo si todo está bien resta la cantidad. Validar primero evita inventarios con números negativos.",
        code = "def vender(inv, producto, cantidad):\n    if producto not in inv:\n        return \"No existe\"\n    if inv[producto] < cantidad:\n        return \"No alcanza\"\n    inv[producto] -= cantidad\n    return \"Vendido\"\ninv = {\"pan\": 4}\nprint(vender(inv, \"jugo\", 1))\nprint(vender(inv, \"pan\", 9))\nprint(vender(inv, \"pan\", 3))\nprint(inv[\"pan\"])",
        expectedOutput = "No existe\nNo alcanza\nVendido\n1",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "inv = {\"pan\": 2}\nif inv[\"pan\"] >= 3:\n    print(\"Vendido\")\nelse:\n    print(\"No alcanza\")",
                acceptedAnswers = listOf("No alcanza"),
                correctFeedback = "¡Bien! Solo hay 2 y piden 3.",
                wrongFeedback = "¿2 >= 3?"
            ),
            FillBlanks(
                prompt = "Completa la validación: no se vende si hay menos de lo que piden.",
                codeTemplate = "def vender(inv, producto, cantidad):\n    if inv[producto] ___ cantidad:\n        return \"No alcanza\"\n    inv[producto] -= cantidad\n    return \"Vendido\"",
                answers = listOf("<"),
                chips = listOf("<", ">", "==", ">="),
                correctFeedback = "¡Eso! Si hay MENOS de lo que piden, no alcanza.",
                wrongFeedback = "¿Cuándo no alcanza? Cuando lo que hay es… que lo que piden."
            ),
            FindBug(
                prompt = "Con esta versión el pan puede quedar en negativo. ¡Toca el error!",
                codeLines = listOf(
                    "def vender(inv, producto, cantidad):",
                    "    inv[producto] -= cantidad",
                    "    if inv[producto] < 0:",
                    "        return \"No alcanza\"",
                    "    return \"Vendido\""
                ),
                bugLineIndex = 1,
                correctFeedback = "¡Cazado! Resta ANTES de validar: el daño ya está hecho. Primero se revisa y después se resta.",
                wrongFeedback = "El problema es el orden: ¿qué línea cambia el inventario demasiado pronto?"
            )
        ),
        walkthrough = listOf(
            step(1, "Se guarda la función vender.", emptyList()),
            step(8, "Hay 4 panes.", listOf("inv" to "{'pan': 4}")),
            step(9, "Piden jugo: no está en el inventario.", listOf("inv" to "{'pan': 4}", "producto" to "'jugo'")),
            step(3, "Sale con \"No existe\" sin tocar nada.", listOf("inv" to "{'pan': 4}", "producto" to "'jugo'")),
            step(9, "Se muestra.", listOf("inv" to "{'pan': 4}"), listOf("No existe")),
            step(10, "Piden 9 panes.", listOf("inv" to "{'pan': 4}", "producto" to "'pan'", "cantidad" to "9"), listOf("No existe")),
            step(5, "4 < 9: no alcanza.", listOf("inv" to "{'pan': 4}", "producto" to "'pan'", "cantidad" to "9"), listOf("No existe")),
            step(10, "Se muestra.", listOf("inv" to "{'pan': 4}"), listOf("No existe", "No alcanza")),
            step(11, "Piden 3 panes: el producto existe y alcanza.", listOf("inv" to "{'pan': 4}", "producto" to "'pan'", "cantidad" to "3"), listOf("No existe", "No alcanza")),
            step(6, "Ahora sí se resta.", listOf("inv" to "{'pan': 1}", "producto" to "'pan'", "cantidad" to "3"), listOf("No existe", "No alcanza")),
            step(11, "Se muestra.", listOf("inv" to "{'pan': 1}"), listOf("No existe", "No alcanza", "Vendido")),
            step(12, "Queda 1 pan.", listOf("inv" to "{'pan': 1}"), listOf("No existe", "No alcanza", "Vendido", "1"))
        )
    )

    private val pocoStock = LessonContent(
        lessonId = "n15_l06",
        title = "Avisar cuando queda poco stock ⚠️",
        hook = "Glitch no quiere quedarse sin pan otra vez. Necesita una alarma de productos por acabarse.",
        explanation = "poco_stock recorre el inventario y arma una lista con los productos que tienen menos del mínimo. El mínimo tiene valor por defecto 3, pero se puede cambiar al llamarla.",
        code = "def poco_stock(inv, minimo=3):\n    alerta = []\n    for producto, cantidad in inv.items():\n        if cantidad < minimo:\n            alerta.append(producto)\n    return alerta\ninv = {\"pan\": 1, \"jugo\": 8, \"queso\": 2}\nprint(poco_stock(inv))\nprint(poco_stock(inv, 10))",
        expectedOutput = "['pan', 'queso']\n['pan', 'jugo', 'queso']",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "inv = {\"a\": 0, \"b\": 5, \"c\": 1}\nbajos = 0\nfor p, c in inv.items():\n    if c < 3:\n        bajos += 1\nprint(bajos)",
                acceptedAnswers = listOf("2"),
                correctFeedback = "¡Exacto! a (0) y c (1) tienen menos de 3.",
                wrongFeedback = "Cuenta los productos con menos de 3 unidades."
            ),
            FillBlanks(
                prompt = "Completa: guarda en la alerta cada producto que tenga poco.",
                codeTemplate = "def poco_stock(inv, minimo=3):\n    alerta = []\n    for producto, cantidad in inv.items():\n        if cantidad < minimo:\n            alerta.___(producto)\n    return alerta",
                answers = listOf("append"),
                chips = listOf("append", "pop", "remove", "get"),
                correctFeedback = "¡Bien! La lista de alerta va creciendo.",
                wrongFeedback = "alerta es una lista. ¿Cómo se le agrega algo al final?"
            ),
            TypeCode(
                prompt = "Escribe la línea que llama a poco_stock con inv y un mínimo de 5, y muestra el resultado.",
                acceptedAnswers = listOf("print(poco_stock(inv, 5))", "print(poco_stock(inv, minimo=5))"),
                placeholder = "print(...)",
                correctFeedback = "¡Perfecto!",
                wrongFeedback = "La llamada lleva inv y el 5, y todo va dentro de print."
            )
        ),
        walkthrough = listOf(
            step(1, "Se guarda la función (mínimo 3 por defecto).", emptyList()),
            step(7, "El inventario de hoy.", listOf("inv" to "{'pan': 1, 'jugo': 8, 'queso': 2}")),
            step(8, "Se llama sin mínimo: usa 3.", listOf("inv" to "{'pan': 1, 'jugo': 8, 'queso': 2}", "minimo" to "3")),
            step(5, "pan (1) y queso (2) tienen menos de 3; jugo (8) no.", listOf("minimo" to "3", "alerta" to "['pan', 'queso']")),
            step(8, "Se muestra la alerta.", listOf("inv" to "{'pan': 1, 'jugo': 8, 'queso': 2}"), listOf("['pan', 'queso']")),
            step(9, "Con mínimo 10, los tres tienen poco.", listOf("inv" to "{'pan': 1, 'jugo': 8, 'queso': 2}", "minimo" to "10"), listOf("['pan', 'queso']", "['pan', 'jugo', 'queso']"))
        )
    )

    private val valorTotal = LessonContent(
        lessonId = "n15_l07",
        title = "Calcular el valor total 💵",
        hook = "¿Cuánto dinero hay en mercancía en la tienda? Glitch cruza las cantidades con los precios.",
        explanation = "Con dos diccionarios con las mismas claves (cantidades y precios) se puede calcular el valor: por cada producto, cantidad × precio, y se acumula en un total. Es una función que recibe dos diccionarios.",
        code = "cantidades = {\"pan\": 4, \"jugo\": 2}\nprecios = {\"pan\": 2, \"jugo\": 3}\ndef valor_total(cant, prec):\n    total = 0\n    for producto, c in cant.items():\n        total += c * prec[producto]\n    return total\nprint(valor_total(cantidades, precios))",
        expectedOutput = "14",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "c = {\"a\": 3}\np = {\"a\": 5}\nprint(c[\"a\"] * p[\"a\"])",
                acceptedAnswers = listOf("15"),
                correctFeedback = "¡Bien! 3 unidades a 5 cada una.",
                wrongFeedback = "Multiplica la cantidad por el precio de la misma clave."
            ),
            FindBug(
                prompt = "El total solo cuenta el último producto. ¡Toca el error!",
                codeLines = listOf(
                    "def valor_total(cant, prec):",
                    "    total = 0",
                    "    for producto, c in cant.items():",
                    "        total = c * prec[producto]",
                    "    return total"
                ),
                bugLineIndex = 3,
                correctFeedback = "¡Cazado! Debe acumular con +=, no reemplazar con =.",
                wrongFeedback = "¿Qué línea debería ir sumando y en cambio pisa el valor?"
            ),
            FillBlanks(
                prompt = "Completa: busca el precio del producto de esta vuelta.",
                codeTemplate = "def valor_total(cant, prec):\n    total = 0\n    for producto, c in cant.items():\n        total += c * prec[___]\n    return total",
                answers = listOf("producto"),
                chips = listOf("producto", "c", "total", "\"producto\""),
                correctFeedback = "¡Eso! La misma clave sirve en los dos diccionarios.",
                wrongFeedback = "El precio se busca con la variable que tiene el nombre del producto (sin comillas)."
            )
        ),
        walkthrough = listOf(
            step(1, "Cuántos hay de cada producto.", listOf("cantidades" to "{'pan': 4, 'jugo': 2}")),
            step(2, "Cuánto cuesta cada uno.", listOf("cantidades" to "{'pan': 4, 'jugo': 2}", "precios" to "{'pan': 2, 'jugo': 3}")),
            step(3, "Se guarda la función.", listOf("cantidades" to "{'pan': 4, 'jugo': 2}", "precios" to "{'pan': 2, 'jugo': 3}")),
            step(8, "Se llama con los dos diccionarios.", listOf("cant" to "{'pan': 4, 'jugo': 2}", "prec" to "{'pan': 2, 'jugo': 3}")),
            step(4, "El total empieza en 0.", listOf("total" to "0")),
            step(5, "Primer producto: pan, 4 unidades.", listOf("total" to "0", "producto" to "'pan'", "c" to "4")),
            step(6, "4 × 2 = 8.", listOf("total" to "8", "producto" to "'pan'", "c" to "4")),
            step(5, "Segundo producto: jugo, 2 unidades.", listOf("total" to "8", "producto" to "'jugo'", "c" to "2")),
            step(6, "2 × 3 = 6; total 14.", listOf("total" to "14", "producto" to "'jugo'", "c" to "2")),
            step(7, "Devuelve 14.", listOf("total" to "14")),
            step(8, "Se muestra.", emptyList(), listOf("14"))
        )
    )

    private val buscar = LessonContent(
        lessonId = "n15_l08",
        title = "Buscar un producto por nombre 🔍",
        hook = "Los clientes escriben \"  Pan \" con espacios y mayúsculas. Glitch quiere encontrarlo igual.",
        explanation = "Antes de buscar, se limpia lo que escribió el usuario: strip() quita los espacios y lower() pasa a minúsculas. Si el producto existe, se devuelve la cantidad; si no, -1 para indicar que no está.",
        code = "inventario = {\"manzana\": 10, \"pan\": 4}\ndef buscar(inv, texto):\n    texto = texto.strip().lower()\n    if texto in inv:\n        return inv[texto]\n    return -1\nprint(buscar(inventario, \"  Pan \"))\nprint(buscar(inventario, \"kiwi\"))",
        expectedOutput = "4\n-1",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Fíjate en las mayúsculas.",
                code = "print(\" JUGO \".strip().lower())",
                acceptedAnswers = listOf("jugo"),
                ignoreCase = false,
                correctFeedback = "¡Exacto! Sin espacios y en minúsculas.",
                wrongFeedback = "strip quita los espacios de los lados y lower pasa todo a minúsculas."
            ),
            FillBlanks(
                prompt = "Otra forma de buscar: devuelve -1 si el producto no está.",
                codeTemplate = "def buscar(inv, texto):\n    return inv.get(texto, ___)",
                answers = listOf("-1"),
                chips = listOf("-1", "0", "texto", "inv"),
                correctFeedback = "¡Bien! get con -1 como valor de reserva.",
                wrongFeedback = "¿Qué número usamos para indicar que no se encontró?"
            ),
            FindBug(
                prompt = "La búsqueda de \"Pan\" devuelve -1 aunque sí hay pan. ¡Toca el error!",
                codeLines = listOf(
                    "def buscar(inv, texto):",
                    "    texto.lower()",
                    "    if texto in inv:",
                    "        return inv[texto]",
                    "    return -1"
                ),
                bugLineIndex = 1,
                correctFeedback = "¡Cazado! lower() no cambia el texto: hay que guardarlo, texto = texto.lower().",
                wrongFeedback = "Recuerda que los textos no cambian solos."
            )
        ),
        walkthrough = listOf(
            step(1, "El inventario.", listOf("inventario" to "{'manzana': 10, 'pan': 4}")),
            step(2, "Se guarda la función.", listOf("inventario" to "{'manzana': 10, 'pan': 4}")),
            step(7, "Se busca \"  Pan \".", listOf("texto" to "'  Pan '")),
            step(3, "Se limpia: queda \"pan\".", listOf("texto" to "'pan'")),
            step(4, "¿\"pan\" está? Sí.", listOf("texto" to "'pan'")),
            step(5, "Devuelve la cantidad.", listOf("texto" to "'pan'")),
            step(7, "Se muestra.", emptyList(), listOf("4")),
            step(8, "Se busca kiwi.", listOf("texto" to "'kiwi'"), listOf("4")),
            step(6, "No está: devuelve -1.", listOf("texto" to "'kiwi'"), listOf("4")),
            step(8, "Se muestra.", emptyList(), listOf("4", "-1"))
        )
    )

    private val menu = LessonContent(
        lessonId = "n15_l09",
        title = "El menú con while True 📟",
        hook = "La tienda necesita un menú que siga funcionando hasta que el encargado elija salir.",
        explanation = "Un menú se arma con while True: en cada vuelta se lee una opción y se decide qué hacer; con 0 se sale con break. En el programa real la opción viene de input(\"Opción: \"). Aquí la simulamos con una lista de lo que escribiría el usuario, y pop(0) va sacando la siguiente.",
        code = "pedidos = [\"1\", \"2\", \"0\"]\nwhile True:\n    opcion = pedidos.pop(0)\n    if opcion == \"0\":\n        print(\"Adiós\")\n        break\n    print(\"Elegiste\", opcion)",
        expectedOutput = "Elegiste 1\nElegiste 2\nAdiós",
        exercises = listOf(
            OrderLines(
                prompt = "Ordena el menú: pide una opción en cada vuelta y sale con 0.",
                lines = listOf(
                    "while True:",
                    "    opcion = input(\"Opción: \")",
                    "    if opcion == \"0\":",
                    "        break",
                    "    print(\"Elegiste\", opcion)"
                ),
                correctFeedback = "¡Perfecto! Primero se pide, después se revisa si es 0 y si no, se atiende.",
                wrongFeedback = "La opción se pide al empezar cada vuelta, y se revisa si es 0 antes de atenderla."
            ),
            FillBlanks(
                prompt = "Completa para que el menú termine con la opción 0.",
                codeTemplate = "while True:\n    opcion = input(\"Opción: \")\n    if opcion == \"0\":\n        ___",
                answers = listOf("break"),
                chips = listOf("break", "continue", "return", "True"),
                correctFeedback = "¡Eso! break sale del while True.",
                wrongFeedback = "Necesitas la palabra que termina el bucle por completo."
            ),
            PredictOutput(
                prompt = "Con otros pedidos: ¿qué muestra? Escríbelo línea por línea.",
                code = "pedidos = [\"2\", \"0\", \"1\"]\nwhile True:\n    opcion = pedidos.pop(0)\n    if opcion == \"0\":\n        print(\"Adiós\")\n        break\n    print(\"Elegiste\", opcion)",
                acceptedAnswers = listOf("Elegiste 2\nAdiós"),
                correctFeedback = "¡Muy bien! Al llegar al 0 sale: el 1 nunca se atiende.",
                wrongFeedback = "Saca los pedidos en orden. ¿Qué pasa al llegar al 0?"
            )
        ),
        walkthrough = listOf(
            step(1, "Lo que \"escribiría\" el usuario, en orden.", listOf("pedidos" to "['1', '2', '0']")),
            step(2, "Empieza el menú.", listOf("pedidos" to "['1', '2', '0']")),
            step(3, "Saca la primera opción: \"1\".", listOf("pedidos" to "['2', '0']", "opcion" to "'1'")),
            step(4, "¿Es \"0\"? No.", listOf("pedidos" to "['2', '0']", "opcion" to "'1'")),
            step(7, "Atiende la opción.", listOf("pedidos" to "['2', '0']", "opcion" to "'1'"), listOf("Elegiste 1")),
            step(3, "Siguiente vuelta: \"2\".", listOf("pedidos" to "['0']", "opcion" to "'2'"), listOf("Elegiste 1")),
            step(4, "¿Es \"0\"? No.", listOf("pedidos" to "['0']", "opcion" to "'2'"), listOf("Elegiste 1")),
            step(7, "Atiende.", listOf("pedidos" to "['0']", "opcion" to "'2'"), listOf("Elegiste 1", "Elegiste 2")),
            step(3, "Siguiente vuelta: \"0\".", listOf("pedidos" to "[]", "opcion" to "'0'"), listOf("Elegiste 1", "Elegiste 2")),
            step(4, "¿Es \"0\"? ¡Sí!", listOf("pedidos" to "[]", "opcion" to "'0'"), listOf("Elegiste 1", "Elegiste 2")),
            step(5, "Se despide.", listOf("pedidos" to "[]", "opcion" to "'0'"), listOf("Elegiste 1", "Elegiste 2", "Adiós")),
            step(6, "break: el menú termina.", listOf("pedidos" to "[]", "opcion" to "'0'"), listOf("Elegiste 1", "Elegiste 2", "Adiós"))
        )
    )

    private val cazarBugs = LessonContent(
        lessonId = "n15_l10",
        title = "Cazar los bugs del proyecto 🐛",
        hook = "El inventario ya casi está listo. Antes de abrir la tienda, Glitch lo prueba con casos difíciles.",
        explanation = "Un proyecto se prueba con los casos que podrían romperlo: un producto agotado, vender justo todo lo que queda, un producto que no existe. Aquí el jugo está en 0 y se venden los 4 panes exactos: la validación tiene que aguantar los dos casos.",
        code = "inventario = {\"pan\": 4, \"jugo\": 0}\ndef vender(inv, producto, cantidad):\n    if inv.get(producto, 0) < cantidad:\n        return \"No alcanza\"\n    inv[producto] -= cantidad\n    return \"Vendido\"\nprint(vender(inventario, \"jugo\", 1))\nprint(vender(inventario, \"pan\", 4))\nprint(inventario)",
        expectedOutput = "No alcanza\nVendido\n{'pan': 0, 'jugo': 0}",
        exercises = listOf(
            FindBug(
                prompt = "Este programa da TypeError. ¡Toca la línea que lo causa!",
                codeLines = listOf(
                    "def agregar(inv, producto, cantidad):",
                    "    inv[producto] = inv.get(producto, 0) + cantidad",
                    "inv = {}",
                    "agregar(inv, \"pan\")",
                    "print(inv)"
                ),
                bugLineIndex = 3,
                correctFeedback = "¡Cazado! agregar pide 3 valores y solo recibe 2: falta la cantidad.",
                wrongFeedback = "Cuenta los parámetros de la función y los valores de la llamada."
            ),
            FindBug(
                prompt = "Este programa da IndentationError. ¡Toca el error!",
                codeLines = listOf(
                    "def mostrar(inv):",
                    "    for producto, cantidad in inv.items():",
                    "    print(producto, cantidad)",
                    "mostrar({\"pan\": 4})"
                ),
                bugLineIndex = 2,
                correctFeedback = "¡Cazado! Lo de adentro del for necesita más sangría.",
                wrongFeedback = "Revisa la sangría de cada línea dentro de la función."
            ),
            PredictOutput(
                prompt = "Sin validar, ¿qué pasa? ¿Qué muestra este programa?",
                code = "inv = {\"pan\": 4}\ninv[\"pan\"] -= 5\nprint(inv[\"pan\"] < 0)",
                acceptedAnswers = listOf("True"),
                correctFeedback = "¡Exacto! Quedó en -1: por eso vender valida antes de restar.",
                wrongFeedback = "4 - 5 es… ¿menor que 0?"
            )
        ),
        walkthrough = listOf(
            step(1, "Pan con 4 y jugo agotado.", listOf("inventario" to "{'pan': 4, 'jugo': 0}")),
            step(2, "Se guarda la función vender.", listOf("inventario" to "{'pan': 4, 'jugo': 0}")),
            step(7, "Caso difícil 1: piden jugo, que está en 0.", listOf("inventario" to "{'pan': 4, 'jugo': 0}", "producto" to "'jugo'", "cantidad" to "1")),
            step(3, "0 < 1: no alcanza.", listOf("inventario" to "{'pan': 4, 'jugo': 0}", "producto" to "'jugo'", "cantidad" to "1")),
            step(7, "Se muestra.", listOf("inventario" to "{'pan': 4, 'jugo': 0}"), listOf("No alcanza")),
            step(8, "Caso difícil 2: piden justo los 4 panes.", listOf("inventario" to "{'pan': 4, 'jugo': 0}", "producto" to "'pan'", "cantidad" to "4"), listOf("No alcanza")),
            step(3, "¿4 < 4? No: sí alcanza.", listOf("inventario" to "{'pan': 4, 'jugo': 0}", "producto" to "'pan'", "cantidad" to "4"), listOf("No alcanza")),
            step(5, "Se restan los 4.", listOf("inventario" to "{'pan': 0, 'jugo': 0}", "producto" to "'pan'", "cantidad" to "4"), listOf("No alcanza")),
            step(8, "Se muestra.", listOf("inventario" to "{'pan': 0, 'jugo': 0}"), listOf("No alcanza", "Vendido")),
            step(9, "Nada quedó en negativo.", listOf("inventario" to "{'pan': 0, 'jugo': 0}"), listOf("No alcanza", "Vendido", "{'pan': 0, 'jugo': 0}"))
        )
    )

    private val repaso = LessonContent(
        lessonId = "n15_l11",
        title = "Repaso: todo lo que aprendiste 🎓",
        hook = "Glitch mira atrás: variables, condicionales, bucles, listas, textos, funciones y diccionarios. ¡Todo cabe en un solo programa!",
        explanation = "Este último programa usa casi todo: un diccionario, un for con items(), un acumulador, un if y una f-string. Ya sabes leerlo línea por línea. Lo que sigue es practicar y crear tus propios programas.",
        code = "inventario = {\"pan\": 4, \"jugo\": 1, \"queso\": 6}\ntotal = 0\nfor producto, cantidad in inventario.items():\n    total += cantidad\n    if cantidad < 3:\n        print(f\"¡Pide más {producto}!\")\nprint(\"Unidades:\", total)",
        expectedOutput = "¡Pide más jugo!\nUnidades: 11",
        exercises = listOf(
            MultipleChoice(
                prompt = "¿Qué usarías para repetir una acción con cada producto del inventario?",
                options = listOf("Un for con items()", "Un if", "Un return", "Un input"),
                correctIndex = 0,
                correctFeedback = "¡Correcto! El for recorre, items() da producto y cantidad.",
                wrongFeedback = "Necesitas algo que repita, una vez por producto."
            ),
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "def f(lista):\n    return len(lista) * 2\nprint(f([\"a\", \"b\"]))",
                acceptedAnswers = listOf("4"),
                correctFeedback = "¡Muy bien! 2 elementos × 2.",
                wrongFeedback = "La función devuelve el largo de la lista por 2."
            ),
            TypeCode(
                prompt = "Escribe la primera línea de una función llamada reporte que recibe inv.",
                acceptedAnswers = listOf("def reporte(inv):"),
                placeholder = "def ...",
                correctFeedback = "¡Perfecto! Ya estás listo para crear tus propias funciones.",
                wrongFeedback = "def, el nombre, el parámetro entre paréntesis y los dos puntos."
            )
        ),
        walkthrough = listOf(
            step(1, "El inventario final.", listOf("inventario" to "{'pan': 4, 'jugo': 1, 'queso': 6}")),
            step(2, "El acumulador de unidades.", listOf("total" to "0")),
            step(3, "pan, 4.", listOf("total" to "0", "producto" to "'pan'", "cantidad" to "4")),
            step(4, "Suma 4.", listOf("total" to "4", "producto" to "'pan'", "cantidad" to "4")),
            step(5, "¿4 < 3? No.", listOf("total" to "4", "producto" to "'pan'", "cantidad" to "4")),
            step(3, "jugo, 1.", listOf("total" to "4", "producto" to "'jugo'", "cantidad" to "1")),
            step(4, "Suma 1.", listOf("total" to "5", "producto" to "'jugo'", "cantidad" to "1")),
            step(5, "¿1 < 3? Sí.", listOf("total" to "5", "producto" to "'jugo'", "cantidad" to "1")),
            step(6, "Aviso con f-string.", listOf("total" to "5", "producto" to "'jugo'", "cantidad" to "1"), listOf("¡Pide más jugo!")),
            step(3, "queso, 6.", listOf("total" to "5", "producto" to "'queso'", "cantidad" to "6"), listOf("¡Pide más jugo!")),
            step(4, "Suma 6.", listOf("total" to "11", "producto" to "'queso'", "cantidad" to "6"), listOf("¡Pide más jugo!")),
            step(5, "¿6 < 3? No.", listOf("total" to "11", "producto" to "'queso'", "cantidad" to "6"), listOf("¡Pide más jugo!")),
            step(7, "Total de unidades.", listOf("total" to "11"), listOf("¡Pide más jugo!", "Unidades: 11"))
        )
    )

    // Jefe final: mezcla lo aprendido en todo el recorrido.
    val boss = BossContent(
        bossId = "n15_boss",
        name = "El Glitch Supremo",
        emoji = "👑",
        intro = "El último desafío: una versión de Glitch hecha de todos los bugs que has cazado. Usa listas, textos, funciones y diccionarios para vencerlo de una vez por todas.",
        exercises = listOf(
            PredictOutput(
                prompt = "Primer golpe. ¿Qué muestra este programa?",
                code = "datos = {\"pan\": [2, 3]}\nprint(sum(datos[\"pan\"]))",
                acceptedAnswers = listOf("5"),
                correctFeedback = "¡Golpe! La lista [2, 3] suma 5.",
                wrongFeedback = "datos[\"pan\"] es una lista. ¿Cuánto suman sus elementos?"
            ),
            PredictOutput(
                prompt = "El Supremo cuenta palabras. ¿Qué muestra?",
                code = "def contar(texto):\n    return len(texto.split())\nprint(contar(\"hola glitch city\"))",
                acceptedAnswers = listOf("3"),
                correctFeedback = "¡Bien! split da 3 palabras.",
                wrongFeedback = "split corta por espacios. ¿Cuántas palabras quedan?"
            ),
            FindBug(
                prompt = "La suma sale 1 en vez de 6. ¡Toca el error!",
                codeLines = listOf(
                    "def total(precios):",
                    "    t = 0",
                    "    for p in precios:",
                    "        t += p",
                    "        return t",
                    "print(total([1, 2, 3]))"
                ),
                bugLineIndex = 4,
                correctFeedback = "¡Bug golpeado! El return dentro del for sale en la primera vuelta.",
                wrongFeedback = "Revisa la sangría: ¿qué línea termina la función demasiado pronto?"
            ),
            FillBlanks(
                prompt = "Muestra los productos con menos de 5 unidades.",
                codeTemplate = "inventario = {\"pan\": 4}\nfor producto, cantidad in inventario.___():\n    if cantidad ___ 5:\n        print(producto)",
                answers = listOf("items", "<"),
                chips = listOf("items", "<", "get", ">"),
                correctFeedback = "¡Eso! Muestra pan.",
                wrongFeedback = "Primero el método que da los pares; luego la comparación de \"menos de\"."
            ),
            TypeCode(
                prompt = "Golpe final: escribe la línea que agrega \"Glitch\" al final de la lista campeones.",
                acceptedAnswers = listOf("campeones.append(\"Glitch\")"),
                placeholder = "campeones...",
                correctFeedback = "¡EL GLITCH SUPREMO HA CAÍDO! Terminaste todo el recorrido. ¡Eres programador!",
                wrongFeedback = "La lista, un punto, append y el texto con comillas entre paréntesis."
            )
        )
    )

    val all: List<LessonContent> = listOf(
        planear, datos, mostrar, agregar,
        vender, pocoStock, valorTotal, buscar,
        menu, cazarBugs, repaso
    )
}
