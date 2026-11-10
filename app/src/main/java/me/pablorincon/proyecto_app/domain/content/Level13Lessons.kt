package me.pablorincon.proyecto_app.domain.content

import me.pablorincon.proyecto_app.domain.BossContent
import me.pablorincon.proyecto_app.domain.FillBlanks
import me.pablorincon.proyecto_app.domain.FindBug
import me.pablorincon.proyecto_app.domain.LessonContent
import me.pablorincon.proyecto_app.domain.MultipleChoice
import me.pablorincon.proyecto_app.domain.PredictOutput
import me.pablorincon.proyecto_app.domain.TypeCode
import me.pablorincon.proyecto_app.domain.WalkStep

// Nivel 13 · Funciones II (11 lecciones y el jefe «La Fábrica de Bugs»): valores por defecto,
// argumentos por nombre, variables locales y globales, varios resultados, listas como
// parámetro, funciones que colaboran, probar funciones y sus errores típicos.
internal object Level13Lessons {

    private fun step(line: Int, note: String, vars: List<Pair<String, String>>, out: List<String> = emptyList()) =
        WalkStep(line, note, vars, out)

    private val porDefecto = LessonContent(
        lessonId = "n13_l01",
        title = "Valores por defecto 🎚️",
        hook = "Glitch casi siempre saluda con \"Hola\". ¿Tiene que escribirlo cada vez?",
        explanation = "Un parámetro puede tener un valor por defecto: def saludar(nombre, saludo=\"Hola\"):. Si al llamar no le pasas ese valor, usa el de por defecto; si se lo pasas, usa el tuyo. Regla: los parámetros con valor por defecto van al final.",
        code = "def saludar(nombre, saludo=\"Hola\"):\n    print(saludo + \",\", nombre)\nsaludar(\"Ana\")\nsaludar(\"Leo\", \"Buenas\")",
        expectedOutput = "Hola, Ana\nBuenas, Leo",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "def potencia(b, e=2):\n    return b ** e\nprint(potencia(3))\nprint(potencia(2, 3))",
                acceptedAnswers = listOf("9\n8"),
                correctFeedback = "¡Exacto! 3 al cuadrado (e por defecto) y 2 al cubo.",
                wrongFeedback = "En la primera llamada e vale 2 por defecto; en la segunda vale 3."
            ),
            FillBlanks(
                prompt = "Haz que veces valga 1 si no se pasa.",
                codeTemplate = "def repetir(texto, veces___1):\n    print(texto * veces)",
                answers = listOf("="),
                chips = listOf("=", "==", ":", "->"),
                correctFeedback = "¡Sí! veces=1 es su valor por defecto.",
                wrongFeedback = "Es el mismo signo que usas para guardar un valor en una variable."
            ),
            FindBug(
                prompt = "Este programa da SyntaxError. ¡Toca la línea con el error!",
                codeLines = listOf(
                    "def pedir(cantidad=1, producto):",
                    "    print(cantidad, producto)",
                    "pedir(2, \"pan\")"
                ),
                bugLineIndex = 0,
                correctFeedback = "¡Cazado! Los parámetros con valor por defecto van al final: def pedir(producto, cantidad=1).",
                wrongFeedback = "Revisa el orden de los parámetros en el def."
            )
        ),
        walkthrough = listOf(
            step(1, "saludo vale \"Hola\" si no se dice otra cosa.", emptyList()),
            step(3, "Solo se pasa el nombre: saludo usa su valor por defecto.", listOf("nombre" to "'Ana'", "saludo" to "'Hola'")),
            step(2, "Muestra el saludo.", listOf("nombre" to "'Ana'", "saludo" to "'Hola'"), listOf("Hola, Ana")),
            step(4, "Ahora se pasan los dos: saludo vale \"Buenas\".", listOf("nombre" to "'Leo'", "saludo" to "'Buenas'"), listOf("Hola, Ana")),
            step(2, "Muestra el saludo nuevo.", listOf("nombre" to "'Leo'", "saludo" to "'Buenas'"), listOf("Hola, Ana", "Buenas, Leo"))
        )
    )

    private val porNombre = LessonContent(
        lessonId = "n13_l02",
        title = "Argumentos por nombre 🏷️",
        hook = "Glitch nunca recuerda en qué orden van los parámetros. ¿Y si los llama por su nombre?",
        explanation = "Al llamar una función puedes escribir parametro=valor. Así no importa el orden, y el código se lee mejor: presentar(edad=12, nombre=\"Ana\") deja claro qué es cada cosa.",
        code = "def presentar(nombre, edad):\n    print(nombre, \"tiene\", edad)\npresentar(edad=12, nombre=\"Ana\")",
        expectedOutput = "Ana tiene 12",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "def resta(a, b):\n    return a - b\nprint(resta(b=1, a=10))",
                acceptedAnswers = listOf("9"),
                correctFeedback = "¡Bien! a vale 10 y b vale 1, aunque se escribieron al revés.",
                wrongFeedback = "Con nombres, cada valor va al parámetro que dice, no por el orden."
            ),
            TypeCode(
                prompt = "Llama a la función crear_robot pasando color=\"rojo\" por nombre.",
                acceptedAnswers = listOf("crear_robot(color=\"rojo\")"),
                placeholder = "crear_robot(...)",
                correctFeedback = "¡Perfecto!",
                wrongFeedback = "Dentro de los paréntesis: el nombre del parámetro, =, y el texto con comillas."
            ),
            FillBlanks(
                prompt = "Completa la llamada para que ancho valga 5.",
                codeTemplate = "def caja(ancho, alto):\n    print(ancho * alto)\ncaja(alto=2, ___=5)",
                answers = listOf("ancho"),
                chips = listOf("ancho", "alto", "5", "caja"),
                correctFeedback = "¡Eso! Muestra 10.",
                wrongFeedback = "Falta nombrar el otro parámetro de la función."
            )
        ),
        walkthrough = listOf(
            step(1, "Se guarda la función.", emptyList()),
            step(3, "Cada valor va a su parámetro por nombre, sin importar el orden.", listOf("nombre" to "'Ana'", "edad" to "12")),
            step(2, "Muestra la presentación.", listOf("nombre" to "'Ana'", "edad" to "12"), listOf("Ana tiene 12"))
        )
    )

    private val locales = LessonContent(
        lessonId = "n13_l03",
        title = "Lo que pasa en la función se queda en la función 🏠",
        hook = "Glitch cambió x dentro de una función, pero afuera x seguía igual. ¿Magia?",
        explanation = "Las variables que se crean dentro de una función son locales: solo existen ahí adentro y desaparecen al terminar. Si adentro usas el mismo nombre que una variable de afuera, es OTRA variable distinta: la de afuera no cambia.",
        code = "x = 5\ndef cambiar():\n    x = 100\n    print(\"Dentro:\", x)\ncambiar()\nprint(\"Fuera:\", x)",
        expectedOutput = "Dentro: 100\nFuera: 5",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "n = 1\ndef f():\n    n = 2\nf()\nprint(n)",
                acceptedAnswers = listOf("1"),
                correctFeedback = "¡Muy bien! La n de adentro es otra variable.",
                wrongFeedback = "La n que se cambia vive dentro de f. ¿Cuál se imprime?"
            ),
            FindBug(
                prompt = "Este programa da NameError. ¡Toca la línea que lo causa!",
                codeLines = listOf(
                    "def calcular():",
                    "    total = 10",
                    "calcular()",
                    "print(total)"
                ),
                bugLineIndex = 3,
                correctFeedback = "¡Cazado! total solo existía dentro de calcular. Lo correcto: return total y guardarlo afuera.",
                wrongFeedback = "¿Dónde se creó total? ¿Desde dónde se intenta usar?"
            ),
            MultipleChoice(
                prompt = "Una variable creada dentro de una función…",
                options = listOf(
                    "Solo existe dentro de esa función",
                    "Existe en todo el programa",
                    "Cambia la de afuera si se llama igual",
                    "Se guarda para siempre"
                ),
                correctIndex = 0,
                correctFeedback = "¡Correcto! Es local: nace y muere con la función.",
                wrongFeedback = "Piensa en el ejemplo: afuera x seguía valiendo 5."
            )
        ),
        walkthrough = listOf(
            step(1, "x de afuera vale 5.", listOf("x" to "5")),
            step(2, "Se guarda la función.", listOf("x" to "5")),
            step(5, "Se llama cambiar().", listOf("x" to "5")),
            step(3, "Se crea una x NUEVA, local, que vale 100.", listOf("x" to "5", "x (local)" to "100")),
            step(4, "Adentro se ve la local.", listOf("x" to "5", "x (local)" to "100"), listOf("Dentro: 100")),
            step(6, "La función terminó y su x desapareció. La de afuera sigue en 5.", listOf("x" to "5"), listOf("Dentro: 100", "Fuera: 5"))
        )
    )

    private val globales = LessonContent(
        lessonId = "n13_l04",
        title = "Variables globales y por qué evitarlas 🌍",
        hook = "Glitch quiere que una función sume puntos a su marcador. ¿Cómo lo hace sin enredarse?",
        explanation = "Una variable creada afuera de todas las funciones es global. Existe la palabra global para cambiarla desde adentro, pero hace que el programa sea difícil de seguir. Lo más claro: pasar el valor como parámetro, devolver el nuevo con return y guardarlo afuera.",
        code = "puntos = 0\ndef ganar(actual):\n    return actual + 10\npuntos = ganar(puntos)\npuntos = ganar(puntos)\nprint(puntos)",
        expectedOutput = "20",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "vidas = 3\ndef perder(v):\n    return v - 1\nvidas = perder(vidas)\nprint(vidas)",
                acceptedAnswers = listOf("2"),
                correctFeedback = "¡Exacto! Entra 3, sale 2 y se guarda en vidas.",
                wrongFeedback = "perder devuelve una vida menos, y ese valor se guarda en vidas."
            ),
            FillBlanks(
                prompt = "Completa para que monedas se actualice con lo que devuelve gastar.",
                codeTemplate = "monedas = 5\ndef gastar(m):\n    return m - 2\nmonedas = ___(monedas)\nprint(monedas)",
                answers = listOf("gastar"),
                chips = listOf("gastar", "monedas", "return", "def"),
                correctFeedback = "¡Bien! Muestra 3.",
                wrongFeedback = "Hay que llamar a la función y guardar lo que devuelve."
            ),
            FindBug(
                prompt = "El marcador se queda en 0. ¡Toca el error!",
                codeLines = listOf(
                    "puntos = 0",
                    "def ganar(actual):",
                    "    return actual + 10",
                    "ganar(puntos)",
                    "print(puntos)"
                ),
                bugLineIndex = 3,
                correctFeedback = "¡Cazado! ganar devuelve 10, pero nadie lo guarda: debe ser puntos = ganar(puntos).",
                wrongFeedback = "La función está bien. ¿Qué pasa con el valor que devuelve?"
            )
        ),
        walkthrough = listOf(
            step(1, "El marcador empieza en 0.", listOf("puntos" to "0")),
            step(2, "Se guarda la función.", listOf("puntos" to "0")),
            step(4, "ganar(0) devuelve 10 y se guarda en puntos.", listOf("puntos" to "10")),
            step(5, "ganar(10) devuelve 20.", listOf("puntos" to "20")),
            step(6, "Se muestra el marcador.", listOf("puntos" to "20"), listOf("20"))
        )
    )

    private val variosValores = LessonContent(
        lessonId = "n13_l05",
        title = "Devolver varios valores 📦📦",
        hook = "Glitch quiere saber el menor Y el mayor de una lista con una sola función.",
        explanation = "return a, b devuelve dos valores juntos (Python los empaqueta en una tupla, algo parecido a una lista que no se puede cambiar). Al recibirlos puedes separarlos en dos variables: menor, mayor = min_max(lista).",
        code = "def min_max(lista):\n    return min(lista), max(lista)\nmenor, mayor = min_max([4, 9, 1])\nprint(menor)\nprint(mayor)",
        expectedOutput = "1\n9",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "def dividir(a, b):\n    return a // b, a % b\nc, r = dividir(7, 2)\nprint(c, r)",
                acceptedAnswers = listOf("3 1"),
                correctFeedback = "¡Muy bien! 7 entre 2 da 3 y sobra 1.",
                wrongFeedback = "La función devuelve el cociente y el resto. c recibe el primero y r el segundo."
            ),
            FillBlanks(
                prompt = "Recibe los dos valores en dos variables.",
                codeTemplate = "def nombre_completo():\n    return \"Ana\", \"Ruiz\"\nnombre___ apellido = nombre_completo()",
                answers = listOf(","),
                chips = listOf(",", "+", "=", ";"),
                correctFeedback = "¡Eso! nombre recibe \"Ana\" y apellido \"Ruiz\".",
                wrongFeedback = "Las dos variables se separan igual que los valores del return."
            ),
            TypeCode(
                prompt = "Escribe la línea que devuelve x e y a la vez.",
                acceptedAnswers = listOf("return x, y"),
                placeholder = "return ...",
                correctFeedback = "¡Perfecto!",
                wrongFeedback = "Un solo return, con los dos valores separados por coma."
            )
        ),
        walkthrough = listOf(
            step(1, "Se guarda la función.", emptyList()),
            step(3, "Se llama con [4, 9, 1].", listOf("lista" to "[4, 9, 1]")),
            step(2, "Devuelve el menor y el mayor juntos: (1, 9).", listOf("lista" to "[4, 9, 1]")),
            step(3, "Se separan: menor recibe 1 y mayor recibe 9.", listOf("menor" to "1", "mayor" to "9")),
            step(4, "Se muestra el menor.", listOf("menor" to "1", "mayor" to "9"), listOf("1")),
            step(5, "Y el mayor.", listOf("menor" to "1", "mayor" to "9"), listOf("1", "9"))
        )
    )

    private val recibenListas = LessonContent(
        lessonId = "n13_l06",
        title = "Funciones que reciben listas 📋",
        hook = "Glitch tiene las notas de todo el curso y quiere el promedio con una sola llamada.",
        explanation = "Un parámetro puede ser una lista. Dentro de la función la usas como cualquier lista: len, sum, recorrerla con for… Así la misma función sirve para listas de cualquier tamaño. Cuidado con la lista vacía: len es 0 y no se puede dividir entre 0.",
        code = "def promedio(notas):\n    return sum(notas) // len(notas)\nprint(promedio([8, 6, 10]))",
        expectedOutput = "8",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "def primero(lista):\n    return lista[0]\nprint(primero([7, 3, 5]))",
                acceptedAnswers = listOf("7"),
                correctFeedback = "¡Bien! El elemento de la posición 0.",
                wrongFeedback = "La función devuelve lista[0]. ¿Cuál es el primero?"
            ),
            FindBug(
                prompt = "Este programa da ZeroDivisionError. ¡Toca la línea que lo provoca!",
                codeLines = listOf(
                    "def promedio(notas):",
                    "    return sum(notas) // len(notas)",
                    "print(promedio([]))"
                ),
                bugLineIndex = 2,
                correctFeedback = "¡Cazado! Con una lista vacía, len es 0 y no se puede dividir entre 0.",
                wrongFeedback = "La función sirve para listas con notas. ¿Qué lista se le pasa aquí?"
            ),
            FillBlanks(
                prompt = "Completa el for que recorre la lista que recibe la función.",
                codeTemplate = "def contar_pares(numeros):\n    n = 0\n    for x in ___:\n        if x % 2 == 0:\n            n += 1\n    return n",
                answers = listOf("numeros"),
                chips = listOf("numeros", "x", "n", "lista"),
                correctFeedback = "¡Eso! Se recorre el parámetro.",
                wrongFeedback = "¿Cómo se llama el parámetro que trae la lista?"
            )
        ),
        walkthrough = listOf(
            step(1, "Se guarda la función.", emptyList()),
            step(3, "Se llama con una lista de 3 notas.", listOf("notas" to "[8, 6, 10]")),
            step(2, "sum da 24, len da 3: 24 // 3 = 8.", listOf("notas" to "[8, 6, 10]")),
            step(3, "Se muestra el promedio.", emptyList(), listOf("8"))
        )
    )

    private val colaboran = LessonContent(
        lessonId = "n13_l07",
        title = "Funciones que llaman a otras 🤝",
        hook = "Glitch ya tiene la función doble. Para hacer \"doble más uno\" no la vuelve a escribir: la reutiliza.",
        explanation = "Dentro de una función puedes llamar a otra. Así reutilizas lo que ya funciona y cada función queda corta. Python sigue el orden de las llamadas: entra a la de afuera, de ahí a la de adentro, y vuelve con el resultado.",
        code = "def doble(n):\n    return n * 2\ndef doble_mas_uno(n):\n    return doble(n) + 1\nprint(doble_mas_uno(5))",
        expectedOutput = "11",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "def a():\n    print(\"A\")\ndef b():\n    print(\"B\")\n    a()\nb()",
                acceptedAnswers = listOf("B\nA"),
                correctFeedback = "¡Exacto! b muestra B y después llama a a.",
                wrongFeedback = "Solo se llama a b. Sigue lo que hace b línea por línea."
            ),
            FillBlanks(
                prompt = "Completa: suma_cuadrados usa la función cuadrado.",
                codeTemplate = "def cuadrado(n):\n    return n * n\ndef suma_cuadrados(a, b):\n    return ___(a) + cuadrado(b)",
                answers = listOf("cuadrado"),
                chips = listOf("cuadrado", "suma_cuadrados", "return", "print"),
                correctFeedback = "¡Muy bien! Se reutiliza cuadrado dos veces.",
                wrongFeedback = "Para el cuadrado de a, llama a la misma función que se usa con b."
            ),
            TypeCode(
                prompt = "Dentro de una función, escribe la línea que devuelve el doble de x usando la función doble.",
                acceptedAnswers = listOf("return doble(x)"),
                placeholder = "return ...",
                correctFeedback = "¡Perfecto! return y la llamada.",
                wrongFeedback = "Devuelve lo que da llamar a doble con x."
            )
        ),
        walkthrough = listOf(
            step(1, "Se guarda doble.", emptyList()),
            step(3, "Se guarda doble_mas_uno.", emptyList()),
            step(5, "Se llama doble_mas_uno(5).", listOf("n" to "5")),
            step(4, "Para calcular, llama a doble(5).", listOf("n" to "5")),
            step(2, "doble devuelve 10.", listOf("n" to "5")),
            step(4, "10 + 1 = 11, y eso se devuelve.", listOf("n" to "5")),
            step(5, "Se muestra.", emptyList(), listOf("11"))
        )
    )

    private val dividir = LessonContent(
        lessonId = "n13_l08",
        title = "Dividir un problema en funciones 🧱",
        hook = "Contar vocales parece una sola tarea, pero Glitch la divide en dos piezas pequeñas.",
        explanation = "Un problema grande se resuelve mejor en partes: una función para cada idea. es_vocal decide sobre UNA letra; contar_vocales recorre el texto y usa es_vocal. Cada pieza se entiende, se prueba y se arregla por separado.",
        code = "def es_vocal(letra):\n    return letra in \"aeiou\"\ndef contar_vocales(texto):\n    n = 0\n    for letra in texto:\n        if es_vocal(letra):\n            n += 1\n    return n\nprint(contar_vocales(\"glitch city\"))",
        expectedOutput = "2",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "def es_par(n):\n    return n % 2 == 0\ndef pares(lista):\n    r = []\n    for x in lista:\n        if es_par(x):\n            r.append(x)\n    return r\nprint(len(pares([1, 2, 3, 4])))",
                acceptedAnswers = listOf("2"),
                correctFeedback = "¡Bien! pares devuelve [2, 4], que tiene 2 elementos.",
                wrongFeedback = "pares arma una lista solo con los pares. ¿Cuántos hay entre 1 y 4?"
            ),
            MultipleChoice(
                prompt = "¿Por qué conviene dividir un programa en funciones pequeñas?",
                options = listOf(
                    "Cada parte se entiende y se prueba por separado",
                    "El programa siempre corre más rápido",
                    "Python lo exige",
                    "Así no hace falta usar variables"
                ),
                correctIndex = 0,
                correctFeedback = "¡Correcto! Piezas pequeñas = menos errores y más fáciles de encontrar.",
                wrongFeedback = "Piensa en qué es más fácil revisar: una pieza pequeña o todo junto."
            ),
            FillBlanks(
                prompt = "Prueba la función es_vocal con la letra a.",
                codeTemplate = "def es_vocal(letra):\n    return letra in \"aeiou\"\nprint(___(\"a\"))",
                answers = listOf("es_vocal"),
                chips = listOf("es_vocal", "letra", "contar_vocales", "\"aeiou\""),
                correctFeedback = "¡Eso! Muestra True.",
                wrongFeedback = "Hay que llamar a la función que se definió arriba."
            )
        ),
        walkthrough = listOf(
            step(1, "Se guarda es_vocal.", emptyList()),
            step(3, "Se guarda contar_vocales.", emptyList()),
            step(9, "Se llama con \"glitch city\".", listOf("texto" to "'glitch city'")),
            step(4, "El contador empieza en 0.", listOf("texto" to "'glitch city'", "n" to "0")),
            step(5, "Recorre g y l: es_vocal devuelve False para las dos.", listOf("texto" to "'glitch city'", "n" to "0", "letra" to "'l'")),
            step(6, "Llega la i: es_vocal(\"i\") devuelve True.", listOf("texto" to "'glitch city'", "n" to "0", "letra" to "'i'")),
            step(7, "Suma una vocal.", listOf("texto" to "'glitch city'", "n" to "1", "letra" to "'i'")),
            step(6, "Sigue: t, c, h, el espacio y la c no son vocales; la i de city sí.", listOf("texto" to "'glitch city'", "n" to "1", "letra" to "'i'")),
            step(7, "Segunda vocal. La t y la y no suman.", listOf("texto" to "'glitch city'", "n" to "2", "letra" to "'i'")),
            step(8, "Terminó el texto: devuelve 2.", listOf("texto" to "'glitch city'", "n" to "2", "letra" to "'y'")),
            step(9, "Se muestra.", emptyList(), listOf("2"))
        )
    )

    private val probar = LessonContent(
        lessonId = "n13_l09",
        title = "Probar una función con varios casos 🧪",
        hook = "La función de Glitch funcionó una vez. ¿Eso quiere decir que siempre funciona?",
        explanation = "Una función se prueba con varios casos: uno normal, uno al revés y los casos límite (números iguales, el 0, una lista vacía…). Muchos bugs solo aparecen en los casos límite.",
        code = "def mayor(a, b):\n    if a > b:\n        return a\n    return b\nprint(mayor(3, 8))\nprint(mayor(8, 3))\nprint(mayor(5, 5))",
        expectedOutput = "8\n8\n5",
        exercises = listOf(
            FindBug(
                prompt = "Esta función falla cuando los dos números son iguales (devuelve None). ¡Toca el error!",
                codeLines = listOf(
                    "def maximo(a, b):",
                    "    if a > b:",
                    "        return a",
                    "    if b > a:",
                    "        return b",
                    "print(maximo(4, 4))"
                ),
                bugLineIndex = 3,
                correctFeedback = "¡Cazado! Con 4 y 4 ninguna condición se cumple. Bastaba return b sin el segundo if.",
                wrongFeedback = "Prueba a mano con a = 4 y b = 4. ¿Qué condición deja ese caso sin respuesta?"
            ),
            PredictOutput(
                prompt = "Caso límite: ¿qué muestra este programa?",
                code = "def signo(n):\n    if n > 0:\n        return \"positivo\"\n    if n < 0:\n        return \"negativo\"\n    return \"cero\"\nprint(signo(0))",
                acceptedAnswers = listOf("cero"),
                correctFeedback = "¡Muy bien! El 0 no es mayor ni menor que 0.",
                wrongFeedback = "¿0 > 0? ¿0 < 0? Si ninguna se cumple, llega al último return."
            ),
            MultipleChoice(
                prompt = "Quieres probar una función que devuelve el valor positivo de un número. ¿Qué casos conviene revisar?",
                options = listOf(
                    "Un positivo, un negativo y el cero",
                    "Solo el 1",
                    "Solo números muy grandes",
                    "No hace falta probarla"
                ),
                correctIndex = 0,
                correctFeedback = "¡Correcto! Un caso de cada tipo, incluido el límite.",
                wrongFeedback = "Hay que cubrir cada tipo de número que la función puede recibir."
            )
        ),
        walkthrough = listOf(
            step(1, "Se guarda la función.", emptyList()),
            step(5, "Caso normal: 3 y 8. Como 3 > 8 no se cumple, devuelve b.", listOf("a" to "3", "b" to "8"), listOf("8")),
            step(6, "Al revés: 8 > 3, devuelve a.", listOf("a" to "8", "b" to "3"), listOf("8", "8")),
            step(7, "Caso límite: iguales. 5 > 5 no se cumple y devuelve b, que también es 5.", listOf("a" to "5", "b" to "5"), listOf("8", "8", "5"))
        )
    )

    private val errores = LessonContent(
        lessonId = "n13_l10",
        title = "Errores típicos con funciones 🐞",
        hook = "Glitch hizo una lista de los errores que más le pasan con funciones. ¡Aprende de sus golpes!",
        explanation = "Los más comunes: olvidar los paréntesis al llamar, olvidar el return (y recibir None), pasar menos valores de los que pide la función (TypeError), usar afuera una variable local (NameError) y poner el return dentro del for.",
        code = "def area(base, altura):\n    return base * altura\nprint(area(3, 4))",
        expectedOutput = "12",
        exercises = listOf(
            FindBug(
                prompt = "Este programa da TypeError. ¡Toca la línea que lo causa!",
                codeLines = listOf(
                    "def area(base, altura):",
                    "    return base * altura",
                    "print(area(5))"
                ),
                bugLineIndex = 2,
                correctFeedback = "¡Cazado! area pide dos valores y solo recibió uno.",
                wrongFeedback = "¿Cuántos valores pide la función y cuántos recibe?"
            ),
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "def f(n):\n    n * 2\nprint(f(4))",
                acceptedAnswers = listOf("None"),
                correctFeedback = "¡Bien! Calcula n * 2 pero no lo devuelve: sale None.",
                wrongFeedback = "¿La función tiene return?"
            ),
            TypeCode(
                prompt = "Escribe la línea que muestra el área con base 5 y altura 2.",
                acceptedAnswers = listOf("print(area(5, 2))"),
                placeholder = "print(...)",
                correctFeedback = "¡Perfecto! Los dos valores, en orden.",
                wrongFeedback = "La llamada lleva los dos valores separados por coma, dentro de print."
            )
        ),
        walkthrough = listOf(
            step(1, "Una función correcta: dos parámetros y un return.", emptyList()),
            step(3, "Se llama con los dos valores que pide.", listOf("base" to "3", "altura" to "4")),
            step(2, "Devuelve 3 * 4.", listOf("base" to "3", "altura" to "4")),
            step(3, "Se muestra el área.", emptyList(), listOf("12"))
        )
    )

    private val adivinanzas = LessonContent(
        lessonId = "n13_l11",
        title = "Mini programa: el juego de las adivinanzas 🎲",
        hook = "Glitch piensa un número y te da pistas: \"más alto\" o \"más bajo\". ¡A programar el juego!",
        explanation = "La función revisar compara un intento con el número secreto y devuelve una pista. En el juego real, cada intento vendría de input; aquí probamos la función con una lista de intentos para ver todas las respuestas.",
        code = "def revisar(intento, secreto):\n    if intento < secreto:\n        return \"Más alto\"\n    if intento > secreto:\n        return \"Más bajo\"\n    return \"¡Adivinaste!\"\nsecreto = 7\nfor intento in [3, 9, 7]:\n    print(intento, revisar(intento, secreto))",
        expectedOutput = "3 Más alto\n9 Más bajo\n7 ¡Adivinaste!",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "def revisar(intento, secreto):\n    if intento < secreto:\n        return \"Más alto\"\n    if intento > secreto:\n        return \"Más bajo\"\n    return \"¡Adivinaste!\"\nprint(revisar(10, 7))",
                acceptedAnswers = listOf("Más bajo"),
                correctFeedback = "¡Exacto! 10 es mayor que 7: hay que bajar.",
                wrongFeedback = "¿10 < 7? ¿10 > 7? Sigue los if en orden."
            ),
            FillBlanks(
                prompt = "Completa: el juego termina cuando se adivina el número.",
                codeTemplate = "adivinado = False\nwhile not adivinado:\n    intento = int(input(\"Número: \"))\n    if revisar(intento, 7) == \"¡Adivinaste!\":\n        adivinado = ___",
                answers = listOf("True"),
                chips = listOf("True", "False", "intento", "break"),
                correctFeedback = "¡Bien! Con la bandera en True, el while ya no da otra vuelta.",
                wrongFeedback = "El while sigue mientras not adivinado sea verdadero. ¿Qué valor lo detiene?"
            ),
            FindBug(
                prompt = "Cuando el intento es muy grande, el juego dice \"Más alto\". ¡Toca el error!",
                codeLines = listOf(
                    "def revisar(intento, secreto):",
                    "    if intento < secreto:",
                    "        return \"Más alto\"",
                    "    if intento > secreto:",
                    "        return \"Más alto\"",
                    "    return \"¡Adivinaste!\""
                ),
                bugLineIndex = 4,
                correctFeedback = "¡Cazado! Si el intento es mayor, la pista debe ser \"Más bajo\".",
                wrongFeedback = "Revisa qué pista devuelve cada if."
            )
        ),
        walkthrough = listOf(
            step(1, "Se guarda la función revisar.", emptyList()),
            step(7, "Glitch piensa el 7.", listOf("secreto" to "7")),
            step(8, "Primer intento: 3.", listOf("secreto" to "7", "intento" to "3")),
            step(9, "revisar(3, 7): 3 < 7, devuelve \"Más alto\".", listOf("secreto" to "7", "intento" to "3"), listOf("3 Más alto")),
            step(8, "Segundo intento: 9.", listOf("secreto" to "7", "intento" to "9"), listOf("3 Más alto")),
            step(9, "revisar(9, 7): 9 > 7, devuelve \"Más bajo\".", listOf("secreto" to "7", "intento" to "9"), listOf("3 Más alto", "9 Más bajo")),
            step(8, "Tercer intento: 7.", listOf("secreto" to "7", "intento" to "7"), listOf("3 Más alto", "9 Más bajo")),
            step(9, "Ni menor ni mayor: ¡adivinó!", listOf("secreto" to "7", "intento" to "7"), listOf("3 Más alto", "9 Más bajo", "7 ¡Adivinaste!"))
        )
    )

    // Jefe del nivel: mezcla lo aprendido en las 11 lecciones.
    val boss = BossContent(
        bossId = "n13_boss",
        name = "La Fábrica de Bugs",
        emoji = "🏭",
        intro = "Esta fábrica produce funciones con fallas escondidas: parámetros en desorden, variables que desaparecen y llamadas sin paréntesis. Apágala encontrando cada bug.",
        exercises = listOf(
            FindBug(
                prompt = "En vez del saludo sale algo como <function saludar…>. ¡Toca el error!",
                codeLines = listOf(
                    "def saludar(nombre=\"amigo\"):",
                    "    return \"Hola, \" + nombre",
                    "print(saludar)"
                ),
                bugLineIndex = 2,
                correctFeedback = "¡Golpe! Sin paréntesis no se llama: debe ser saludar().",
                wrongFeedback = "La función está bien. Mira cómo se usa en el print."
            ),
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "def f(a, b=10):\n    return a + b\nprint(f(1))\nprint(f(1, 1))",
                acceptedAnswers = listOf("11\n2"),
                correctFeedback = "¡Bien! b vale 10 por defecto, o 1 si se pasa.",
                wrongFeedback = "En la primera llamada b usa su valor por defecto."
            ),
            PredictOutput(
                prompt = "La fábrica intenta cambiar x. ¿Qué muestra? Escríbelo línea por línea.",
                code = "x = 1\ndef cambiar():\n    x = 50\n    return x\nprint(cambiar())\nprint(x)",
                acceptedAnswers = listOf("50\n1"),
                correctFeedback = "¡Eso! La x de adentro es local; la de afuera sigue en 1.",
                wrongFeedback = "La función devuelve su x local; la x de afuera no se toca."
            ),
            FillBlanks(
                prompt = "Recibe los dos valores que devuelve la función.",
                codeTemplate = "def extremos(lista):\n    return lista[0], lista[-1]\nprimero, ultimo = ___([4, 5, 6])\nprint(primero, ultimo)",
                answers = listOf("extremos"),
                chips = listOf("extremos", "lista", "return", "print"),
                correctFeedback = "¡Muy bien! Muestra 4 6.",
                wrongFeedback = "Hay que llamar a la función definida arriba."
            ),
            TypeCode(
                prompt = "Último golpe: llama a la función enviar pasando mensaje=\"listo\" por nombre.",
                acceptedAnswers = listOf("enviar(mensaje=\"listo\")"),
                placeholder = "enviar(...)",
                correctFeedback = "¡Fábrica apagada! Dominas las funciones.",
                wrongFeedback = "El nombre del parámetro, =, y el texto con comillas, dentro de los paréntesis."
            )
        )
    )

    val all: List<LessonContent> = listOf(
        porDefecto, porNombre, locales, globales,
        variosValores, recibenListas, colaboran, dividir,
        probar, errores, adivinanzas
    )
}
