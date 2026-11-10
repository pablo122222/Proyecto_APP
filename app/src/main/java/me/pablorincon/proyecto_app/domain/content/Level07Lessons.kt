package me.pablorincon.proyecto_app.domain.content

import me.pablorincon.proyecto_app.domain.BossContent
import me.pablorincon.proyecto_app.domain.FillBlanks
import me.pablorincon.proyecto_app.domain.FindBug
import me.pablorincon.proyecto_app.domain.GlitchMode
import me.pablorincon.proyecto_app.domain.GlitchPuzzle
import me.pablorincon.proyecto_app.domain.LessonContent
import me.pablorincon.proyecto_app.domain.MultipleChoice
import me.pablorincon.proyecto_app.domain.OrderLines
import me.pablorincon.proyecto_app.domain.PredictOutput
import me.pablorincon.proyecto_app.domain.TypeCode
import me.pablorincon.proyecto_app.domain.WalkStep
import me.pablorincon.proyecto_app.domain.glitch.GlitchBlock

// Nivel 7 · Bucles while (11 lecciones y el jefe «El Reloj Infinito»), con Glitch como hilo conductor.
// Cada ejemplo trae su "Ver paso a paso": línea, nota, variables y todo lo impreso hasta ahí.
internal object Level07Lessons {

    // Paso a paso más corto de escribir: variables como "nombre" to "valor".
    private fun step(line: Int, note: String, vars: List<Pair<String, String>>, out: List<String> = emptyList()) =
        WalkStep(line, note, vars, out)

    private val repiteMientras = LessonContent(
        lessonId = "n07_l01",
        title = "Repite mientras… 🔁",
        hook = "Glitch tiene que regar 3 plantas y se cansó de copiar el mismo print tres veces. Hay una forma mejor.",
        explanation = "while repite su bloque mientras la condición sea verdadera. Antes de cada vuelta revisa la condición; cuando es falsa, el bucle termina. La última línea del bloque cambia la variable para que algún día la condición deje de cumplirse.",
        code = "planta = 1\nwhile planta <= 3:\n    print(\"Regando planta\", planta)\n    planta = planta + 1",
        expectedOutput = "Regando planta 1\nRegando planta 2\nRegando planta 3",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "n = 1\nwhile n <= 2:\n    print(n)\n    n = n + 1",
                acceptedAnswers = listOf("1\n2"),
                correctFeedback = "¡Exacto! Da dos vueltas: con n = 1 y con n = 2. Con n = 3 la condición ya es falsa.",
                wrongFeedback = "Sigue el bucle: imprime n, suma 1 y vuelve a revisar n <= 2 antes de cada vuelta."
            ),
            FillBlanks(
                prompt = "Completa la palabra que repite mientras la condición se cumpla.",
                codeTemplate = "n = 1\n___ n <= 3:\n    print(n)\n    n = n + 1",
                answers = listOf("while"),
                chips = listOf("while", "if", "for", "repeat"),
                correctFeedback = "¡Sí! while repite; if solo lo haría una vez.",
                wrongFeedback = "if revisa una sola vez. Para repetir mientras algo sea cierto se usa otra palabra."
            ),
            OrderLines(
                prompt = "Ordena el programa que riega las plantas 1, 2 y 3.",
                lines = listOf(
                    "planta = 1",
                    "while planta <= 3:",
                    "    print(\"Regando planta\", planta)",
                    "    planta = planta + 1"
                ),
                correctFeedback = "¡Perfecto! Primero la variable, luego el while y su bloque con sangría.",
                wrongFeedback = "La variable se crea antes del while. Dentro del bloque, primero se imprime y al final se suma 1."
            )
        ),
        walkthrough = listOf(
            step(1, "Crea la variable planta con el valor 1.", listOf("planta" to "1")),
            step(2, "Revisa la condición: 1 <= 3 es verdadero, así que entra al bucle.", listOf("planta" to "1")),
            step(3, "Imprime el mensaje con planta = 1.", listOf("planta" to "1"), listOf("Regando planta 1")),
            step(4, "Suma 1: planta pasa a valer 2. Al terminar el bloque, vuelve arriba al while.", listOf("planta" to "2"), listOf("Regando planta 1")),
            step(2, "Revisa otra vez: 2 <= 3 es verdadero. Otra vuelta.", listOf("planta" to "2"), listOf("Regando planta 1")),
            step(3, "Imprime con planta = 2.", listOf("planta" to "2"), listOf("Regando planta 1", "Regando planta 2")),
            step(4, "planta pasa a valer 3 y vuelve al while.", listOf("planta" to "3"), listOf("Regando planta 1", "Regando planta 2")),
            step(2, "3 <= 3 es verdadero (3 es igual a 3). Otra vuelta.", listOf("planta" to "3"), listOf("Regando planta 1", "Regando planta 2")),
            step(3, "Imprime con planta = 3.", listOf("planta" to "3"), listOf("Regando planta 1", "Regando planta 2", "Regando planta 3")),
            step(4, "planta pasa a valer 4 y vuelve al while.", listOf("planta" to "4"), listOf("Regando planta 1", "Regando planta 2", "Regando planta 3")),
            step(2, "4 <= 3 es falso: el bucle termina. No hay más líneas, así que el programa acaba.", listOf("planta" to "4"), listOf("Regando planta 1", "Regando planta 2", "Regando planta 3"))
        )
    )

    private val atajos = LessonContent(
        lessonId = "n07_l02",
        title = "Atajos: += y -= ✍️",
        hook = "Glitch recoge monedas y escribe monedas = monedas + 1 una y otra vez. Hay un atajo más corto.",
        explanation = "monedas += 1 es lo mismo que monedas = monedas + 1. También existe -= para restar. Son muy comunes dentro de los bucles para cambiar la variable en cada vuelta.",
        code = "monedas = 0\nwhile monedas < 3:\n    monedas += 1\n    print(\"Monedas:\", monedas)",
        expectedOutput = "Monedas: 1\nMonedas: 2\nMonedas: 3",
        exercises = listOf(
            TypeCode(
                prompt = "Escribe la línea que suma 1 a la variable vidas usando +=.",
                acceptedAnswers = listOf("vidas += 1"),
                placeholder = "vidas ...",
                correctFeedback = "¡Eso! vidas += 1 suma 1 a lo que ya tenía.",
                wrongFeedback = "Escribe el nombre de la variable, luego += y el número que quieres sumar."
            ),
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "x = 10\nx -= 3\nx += 1\nprint(x)",
                acceptedAnswers = listOf("8"),
                correctFeedback = "¡Muy bien! 10 − 3 = 7, y 7 + 1 = 8.",
                wrongFeedback = "Hazlo en orden: primero resta 3 a 10 y luego suma 1."
            ),
            FillBlanks(
                prompt = "Completa para que la energía baje 2 puntos.",
                codeTemplate = "energia = 10\nenergia ___ 2\nprint(energia)",
                answers = listOf("-="),
                chips = listOf("-=", "+=", "=-", "=="),
                correctFeedback = "¡Sí! -= resta y guarda el resultado en la misma variable.",
                wrongFeedback = "Para restar el signo menos va antes del igual, todo junto."
            )
        ),
        walkthrough = listOf(
            step(1, "Glitch empieza sin monedas.", listOf("monedas" to "0")),
            step(2, "0 < 3 es verdadero: entra al bucle.", listOf("monedas" to "0")),
            step(3, "monedas += 1: ahora tiene 1.", listOf("monedas" to "1")),
            step(4, "Imprime cuántas lleva.", listOf("monedas" to "1"), listOf("Monedas: 1")),
            step(2, "1 < 3 es verdadero: otra vuelta.", listOf("monedas" to "1"), listOf("Monedas: 1")),
            step(3, "monedas += 1: ahora tiene 2.", listOf("monedas" to "2"), listOf("Monedas: 1")),
            step(4, "Imprime cuántas lleva.", listOf("monedas" to "2"), listOf("Monedas: 1", "Monedas: 2")),
            step(2, "2 < 3 es verdadero: otra vuelta.", listOf("monedas" to "2"), listOf("Monedas: 1", "Monedas: 2")),
            step(3, "monedas += 1: ahora tiene 3.", listOf("monedas" to "3"), listOf("Monedas: 1", "Monedas: 2")),
            step(4, "Imprime cuántas lleva.", listOf("monedas" to "3"), listOf("Monedas: 1", "Monedas: 2", "Monedas: 3")),
            step(2, "3 < 3 es falso: el bucle termina.", listOf("monedas" to "3"), listOf("Monedas: 1", "Monedas: 2", "Monedas: 3"))
        )
    )

    private val cuentaRegresiva = LessonContent(
        lessonId = "n07_l03",
        title = "Cuenta regresiva 🚀",
        hook = "El cohete de Glitch City despega en 3… 2… 1… Un while también sabe contar hacia atrás.",
        explanation = "Para contar hacia atrás, la variable empieza alta y baja con -= en cada vuelta. Cuando la condición deja de cumplirse, el programa sigue con la primera línea fuera del bucle (la que no tiene sangría).",
        code = "n = 3\nwhile n > 0:\n    print(n)\n    n -= 1\nprint(\"¡Despegue!\")",
        expectedOutput = "3\n2\n1\n¡Despegue!",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "n = 2\nwhile n > 0:\n    print(n)\n    n -= 1\nprint(\"Fin\")",
                acceptedAnswers = listOf("2\n1\nFin"),
                correctFeedback = "¡Exacto! Imprime 2 y 1; con n = 0 sale del bucle y muestra Fin.",
                wrongFeedback = "Revisa: ¿se imprime el 0? La condición es n > 0. Y no olvides la línea de después del bucle."
            ),
            OrderLines(
                prompt = "Ordena la cuenta regresiva del cohete.",
                lines = listOf(
                    "n = 3",
                    "while n > 0:",
                    "    print(n)",
                    "    n -= 1",
                    "print(\"¡Despegue!\")"
                ),
                correctFeedback = "¡Despegue! El print final va fuera del bucle, sin sangría.",
                wrongFeedback = "El «¡Despegue!» debe salir una sola vez, al final: va sin sangría, después del bloque."
            ),
            FillBlanks(
                prompt = "Completa para que n baje de 1 en 1.",
                codeTemplate = "n = 5\nwhile n > 0:\n    print(n)\n    n ___ 1",
                answers = listOf("-="),
                chips = listOf("-=", "+=", "*=", "=="),
                correctFeedback = "¡Sí! Restando 1 en cada vuelta, n llega a 0 y el bucle termina.",
                wrongFeedback = "Si n sube, n > 0 se cumpliría para siempre. Hay que restar."
            )
        ),
        walkthrough = listOf(
            step(1, "n empieza en 3.", listOf("n" to "3")),
            step(2, "3 > 0 es verdadero: entra al bucle.", listOf("n" to "3")),
            step(3, "Imprime 3.", listOf("n" to "3"), listOf("3")),
            step(4, "n -= 1: n baja a 2.", listOf("n" to "2"), listOf("3")),
            step(2, "2 > 0 es verdadero: otra vuelta.", listOf("n" to "2"), listOf("3")),
            step(3, "Imprime 2.", listOf("n" to "2"), listOf("3", "2")),
            step(4, "n baja a 1.", listOf("n" to "1"), listOf("3", "2")),
            step(2, "1 > 0 es verdadero: otra vuelta.", listOf("n" to "1"), listOf("3", "2")),
            step(3, "Imprime 1.", listOf("n" to "1"), listOf("3", "2", "1")),
            step(4, "n baja a 0.", listOf("n" to "0"), listOf("3", "2", "1")),
            step(2, "0 > 0 es falso: sale del bucle y sigue con la línea de abajo.", listOf("n" to "0"), listOf("3", "2", "1")),
            step(5, "Esta línea está fuera del bucle: se ejecuta una sola vez.", listOf("n" to "0"), listOf("3", "2", "1", "¡Despegue!"))
        )
    )

    private val nuncaSeCumple = LessonContent(
        lessonId = "n07_l04",
        title = "¿Y si nunca se cumple? 🚪",
        hook = "Glitch quiere correr, pero tiene la energía en 0. ¿Cuántas vueltas da el bucle?",
        explanation = "while revisa la condición antes de entrar. Si ya es falsa desde el principio, el bloque no se ejecuta ni una vez y el programa sigue después del bucle.",
        code = "energia = 0\nwhile energia > 0:\n    print(\"Corriendo\")\n    energia -= 1\nprint(\"Glitch descansa\")",
        expectedOutput = "Glitch descansa",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "x = 5\nwhile x < 3:\n    print(\"Hola\")\n    x += 1\nprint(\"Listo\")",
                acceptedAnswers = listOf("Listo"),
                correctFeedback = "¡Exacto! 5 < 3 es falso desde el principio: el bucle no se ejecuta.",
                wrongFeedback = "Revisa la condición con x = 5 antes de entrar. ¿Se cumple alguna vez?"
            ),
            MultipleChoice(
                prompt = "Si x empieza en 3, ¿cuántas veces se repite el bloque de while x < 3?",
                options = listOf("Ninguna", "Una", "Tres", "Para siempre"),
                correctIndex = 0,
                correctFeedback = "¡Sí! 3 < 3 es falso, así que no entra ni una vez.",
                wrongFeedback = "Revisa la condición antes de la primera vuelta: ¿3 es menor que 3?"
            ),
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "n = 1\nwhile n < 3:\n    print(n)\n    n += 1\nprint(\"Fin\")",
                acceptedAnswers = listOf("1\n2\nFin"),
                correctFeedback = "¡Muy bien! Dos vueltas y luego la línea de fuera del bucle.",
                wrongFeedback = "La condición es n < 3: ¿se imprime el 3? Y la última línea se ejecuta al salir."
            )
        ),
        walkthrough = listOf(
            step(1, "energia empieza en 0.", listOf("energia" to "0")),
            step(2, "0 > 0 es falso desde el inicio: Python se salta todo el bloque del while.", listOf("energia" to "0")),
            step(5, "Sigue con la primera línea fuera del bucle.", listOf("energia" to "0"), listOf("Glitch descansa"))
        )
    )

    private val bucleInfinito = LessonContent(
        lessonId = "n07_l05",
        title = "El bucle infinito 🌀",
        hook = "Un robot de Glitch City se quedó dando vueltas toda la noche. Alguien olvidó una línea.",
        explanation = "La línea n += 1 es la que hace avanzar el bucle. Si faltara, n valdría 1 para siempre, la condición nunca sería falsa y el programa no terminaría: eso es un bucle infinito. Siempre revisa que la variable de la condición cambie en cada vuelta y se acerque a la salida.",
        code = "n = 1\nwhile n <= 3:\n    print(n)\n    n += 1\nprint(\"Terminó\")",
        expectedOutput = "1\n2\n3\nTerminó",
        exercises = listOf(
            FindBug(
                prompt = "Este bucle nunca termina. Toca la línea del error.",
                codeLines = listOf(
                    "n = 1",
                    "while n <= 3:",
                    "    print(n)",
                    "    n = n - 1"
                ),
                bugLineIndex = 3,
                correctFeedback = "¡Bug cazado! n baja y se aleja de 3: n <= 3 se cumple para siempre.",
                wrongFeedback = "Fíjate hacia dónde va n en cada vuelta. ¿Se acerca a dejar de cumplir n <= 3?"
            ),
            FindBug(
                prompt = "n sube de 3 en 3 y Glitch espera que se detenga en 10, pero nunca para. Toca el error.",
                codeLines = listOf(
                    "n = 0",
                    "while n != 10:",
                    "    n += 3"
                ),
                bugLineIndex = 1,
                correctFeedback = "¡Bien visto! n vale 0, 3, 6, 9, 12… nunca exactamente 10. Con n < 10 sí terminaría.",
                wrongFeedback = "Escribe los valores que toma n: 0, 3, 6, 9… ¿Alguna vez vale exactamente 10?"
            ),
            TypeCode(
                prompt = "Arregla el primer bucle: escribe la línea que debe ir dentro para que n suba de 1 en 1.",
                acceptedAnswers = listOf("n += 1", "n = n + 1"),
                placeholder = "n ...",
                correctFeedback = "¡Arreglado! Ahora n se acerca a 3 en cada vuelta y el bucle termina.",
                wrongFeedback = "Hay que sumar 1 a n. Puedes usar el atajo += que viste antes."
            )
        ),
        walkthrough = listOf(
            step(1, "n empieza en 1.", listOf("n" to "1")),
            step(2, "1 <= 3: verdadero, entra.", listOf("n" to "1")),
            step(3, "Imprime 1.", listOf("n" to "1"), listOf("1")),
            step(4, "Esta es la línea clave: n sube a 2 y se acerca a la salida.", listOf("n" to "2"), listOf("1")),
            step(2, "2 <= 3: verdadero, otra vuelta.", listOf("n" to "2"), listOf("1")),
            step(3, "Imprime 2.", listOf("n" to "2"), listOf("1", "2")),
            step(4, "n sube a 3.", listOf("n" to "3"), listOf("1", "2")),
            step(2, "3 <= 3: verdadero, otra vuelta.", listOf("n" to "3"), listOf("1", "2")),
            step(3, "Imprime 3.", listOf("n" to "3"), listOf("1", "2", "3")),
            step(4, "n sube a 4.", listOf("n" to "4"), listOf("1", "2", "3")),
            step(2, "4 <= 3: falso. Gracias a la línea 4, el bucle termina.", listOf("n" to "4"), listOf("1", "2", "3")),
            step(5, "Fuera del bucle.", listOf("n" to "4"), listOf("1", "2", "3", "Terminó"))
        )
    )

    private val acumular = LessonContent(
        lessonId = "n07_l06",
        title = "Acumular un total 🧮",
        hook = "Glitch quiere sumar 1 + 2 + 3 + 4 sin hacerlo a mano. Un bucle puede ir juntando.",
        explanation = "Un acumulador es una variable que empieza en 0 y en cada vuelta suma algo con +=. Al final del bucle tiene el total. Ojo: el print va fuera del bucle para mostrar solo el resultado final.",
        code = "total = 0\nn = 1\nwhile n <= 4:\n    total += n\n    n += 1\nprint(total)",
        expectedOutput = "10",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa?",
                code = "total = 0\nn = 1\nwhile n <= 3:\n    total += n\n    n += 1\nprint(total)",
                acceptedAnswers = listOf("6"),
                correctFeedback = "¡Exacto! 1 + 2 + 3 = 6.",
                wrongFeedback = "Suma los valores que toma n dentro del bucle: empieza en 1 y para cuando n <= 3 es falso."
            ),
            FillBlanks(
                prompt = "Completa para que total vaya juntando los valores de n.",
                codeTemplate = "total = 0\nn = 1\nwhile n <= 5:\n    total ___ n\n    n += 1",
                answers = listOf("+="),
                chips = listOf("+=", "=", "-=", "=="),
                correctFeedback = "¡Sí! += suma n a lo que total ya tenía.",
                wrongFeedback = "Con = solo, total perdería lo anterior y quedaría con el último n. Hay que sumar."
            ),
            OrderLines(
                prompt = "Ordena el programa que suma del 1 al 4 y muestra el total.",
                lines = listOf(
                    "total = 0",
                    "n = 1",
                    "while n <= 4:",
                    "    total += n",
                    "    n += 1",
                    "print(total)"
                ),
                correctFeedback = "¡Perfecto! Variables antes, suma dentro del bucle, print al final.",
                wrongFeedback = "total y n se crean antes del bucle; el print va fuera, sin sangría, para mostrar solo el final."
            )
        ),
        walkthrough = listOf(
            step(1, "El acumulador empieza en 0.", listOf("total" to "0")),
            step(2, "n empieza en 1.", listOf("total" to "0", "n" to "1")),
            step(3, "1 <= 4: entra.", listOf("total" to "0", "n" to "1")),
            step(4, "total += n: 0 + 1 = 1.", listOf("total" to "1", "n" to "1")),
            step(5, "n sube a 2.", listOf("total" to "1", "n" to "2")),
            step(3, "2 <= 4: otra vuelta.", listOf("total" to "1", "n" to "2")),
            step(4, "1 + 2 = 3.", listOf("total" to "3", "n" to "2")),
            step(5, "n sube a 3.", listOf("total" to "3", "n" to "3")),
            step(3, "3 <= 4: otra vuelta.", listOf("total" to "3", "n" to "3")),
            step(4, "3 + 3 = 6.", listOf("total" to "6", "n" to "3")),
            step(5, "n sube a 4.", listOf("total" to "6", "n" to "4")),
            step(3, "4 <= 4: otra vuelta.", listOf("total" to "6", "n" to "4")),
            step(4, "6 + 4 = 10.", listOf("total" to "10", "n" to "4")),
            step(5, "n sube a 5.", listOf("total" to "10", "n" to "5")),
            step(3, "5 <= 4: falso, termina el bucle.", listOf("total" to "10", "n" to "5")),
            step(6, "Muestra el total una sola vez.", listOf("total" to "10", "n" to "5"), listOf("10"))
        )
    )

    private val deDosEnDos = LessonContent(
        lessonId = "n07_l07",
        title = "De dos en dos 🐸",
        hook = "La rana de Glitch City salta de dos en dos piedras. El bucle puede avanzar a saltos.",
        explanation = "La variable no tiene que subir de 1 en 1: con n += 2 avanza de dos en dos, con n += 5 de cinco en cinco. La condición decide hasta dónde llega.",
        code = "n = 2\nwhile n <= 8:\n    print(n)\n    n += 2",
        expectedOutput = "2\n4\n6\n8",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "n = 1\nwhile n <= 7:\n    print(n)\n    n += 3",
                acceptedAnswers = listOf("1\n4\n7"),
                correctFeedback = "¡Muy bien! 1, 4 y 7; con 10 ya no se cumple n <= 7.",
                wrongFeedback = "Empieza en 1 y suma 3 cada vez. Para cuando n pasa de 7."
            ),
            TypeCode(
                prompt = "Escribe la línea que suma 5 a n usando +=.",
                acceptedAnswers = listOf("n += 5"),
                placeholder = "n ...",
                correctFeedback = "¡Eso! Así n avanza de cinco en cinco.",
                wrongFeedback = "Igual que n += 1, pero con otro número."
            ),
            FillBlanks(
                prompt = "Completa para contar de 10 en 10 hasta 50.",
                codeTemplate = "n = 10\nwhile n <= ___:\n    print(n)\n    n += ___",
                answers = listOf("50", "10"),
                chips = listOf("50", "10", "5", "100"),
                correctFeedback = "¡Sí! 10, 20, 30, 40 y 50.",
                wrongFeedback = "El primer hueco es dónde termina y el segundo, de cuánto es cada salto."
            )
        ),
        walkthrough = listOf(
            step(1, "n empieza en 2.", listOf("n" to "2")),
            step(2, "2 <= 8: entra.", listOf("n" to "2")),
            step(3, "Imprime 2.", listOf("n" to "2"), listOf("2")),
            step(4, "n += 2: salta a 4.", listOf("n" to "4"), listOf("2")),
            step(2, "4 <= 8: otra vuelta.", listOf("n" to "4"), listOf("2")),
            step(3, "Imprime 4.", listOf("n" to "4"), listOf("2", "4")),
            step(4, "Salta a 6.", listOf("n" to "6"), listOf("2", "4")),
            step(2, "6 <= 8: otra vuelta.", listOf("n" to "6"), listOf("2", "4")),
            step(3, "Imprime 6.", listOf("n" to "6"), listOf("2", "4", "6")),
            step(4, "Salta a 8.", listOf("n" to "8"), listOf("2", "4", "6")),
            step(2, "8 <= 8: otra vuelta.", listOf("n" to "8"), listOf("2", "4", "6")),
            step(3, "Imprime 8.", listOf("n" to "8"), listOf("2", "4", "6", "8")),
            step(4, "Salta a 10.", listOf("n" to "10"), listOf("2", "4", "6", "8")),
            step(2, "10 <= 8: falso, termina.", listOf("n" to "10"), listOf("2", "4", "6", "8"))
        )
    )

    private val hastaUnaMeta = LessonContent(
        lessonId = "n07_l08",
        title = "Repetir hasta una meta 🎯",
        hook = "Glitch ahorra 30 monedas por semana para comprar una patineta de 100. ¿Cuántas semanas necesita?",
        explanation = "A veces no sabemos cuántas vueltas hacen falta: el bucle repite hasta alcanzar la meta y otra variable cuenta las vueltas. Así el programa calcula la respuesta por nosotros.",
        code = "ahorro = 0\nsemanas = 0\nwhile ahorro < 100:\n    ahorro += 30\n    semanas += 1\nprint(semanas)",
        expectedOutput = "4",
        exercises = listOf(
            PredictOutput(
                prompt = "Un robot tiene 10 de vida y cada golpe le quita 4. ¿Qué muestra este programa?",
                code = "vida = 10\ngolpes = 0\nwhile vida > 0:\n    vida -= 4\n    golpes += 1\nprint(golpes)",
                acceptedAnswers = listOf("3"),
                correctFeedback = "¡Exacto! 10 → 6 → 2 → −2: hacen falta 3 golpes.",
                wrongFeedback = "Anota la vida después de cada golpe y cuenta cuántos hacen falta para que ya no sea mayor que 0."
            ),
            FindBug(
                prompt = "Glitch quiere ahorrar hasta 100, pero el programa nunca termina. Toca el error.",
                codeLines = listOf(
                    "ahorro = 0",
                    "while ahorro < 100:",
                    "    ahorro -= 30",
                    "print(\"Listo\")"
                ),
                bugLineIndex = 2,
                correctFeedback = "¡Bug cazado! Restando, el ahorro baja y nunca llega a 100.",
                wrongFeedback = "¿El ahorro se acerca a 100 en cada vuelta? Mira cómo cambia."
            ),
            FillBlanks(
                prompt = "Completa la condición para repetir mientras falte para llegar a 100.",
                codeTemplate = "while ahorro ___ 100:\n    ahorro += 30",
                answers = listOf("<"),
                chips = listOf("<", ">", "==", ">="),
                correctFeedback = "¡Sí! Mientras el ahorro sea menor que 100, sigue ahorrando.",
                wrongFeedback = "Hay que repetir mientras todavía no llega a 100: ¿mayor o menor?"
            )
        ),
        walkthrough = listOf(
            step(1, "Empieza sin ahorro.", listOf("ahorro" to "0")),
            step(2, "Y sin semanas contadas.", listOf("ahorro" to "0", "semanas" to "0")),
            step(3, "0 < 100: todavía falta, entra.", listOf("ahorro" to "0", "semanas" to "0")),
            step(4, "Ahorra 30.", listOf("ahorro" to "30", "semanas" to "0")),
            step(5, "Cuenta una semana.", listOf("ahorro" to "30", "semanas" to "1")),
            step(3, "30 < 100: sigue.", listOf("ahorro" to "30", "semanas" to "1")),
            step(4, "Ahorra 30 más.", listOf("ahorro" to "60", "semanas" to "1")),
            step(5, "Segunda semana.", listOf("ahorro" to "60", "semanas" to "2")),
            step(3, "60 < 100: sigue.", listOf("ahorro" to "60", "semanas" to "2")),
            step(4, "Ahorra 30 más.", listOf("ahorro" to "90", "semanas" to "2")),
            step(5, "Tercera semana.", listOf("ahorro" to "90", "semanas" to "3")),
            step(3, "90 < 100: todavía falta un poco, sigue.", listOf("ahorro" to "90", "semanas" to "3")),
            step(4, "Ahorra 30 más: ya pasa de 100.", listOf("ahorro" to "120", "semanas" to "3")),
            step(5, "Cuarta semana.", listOf("ahorro" to "120", "semanas" to "4")),
            step(3, "120 < 100 es falso: meta alcanzada, termina.", listOf("ahorro" to "120", "semanas" to "4")),
            step(6, "Muestra cuántas semanas hicieron falta.", listOf("ahorro" to "120", "semanas" to "4"), listOf("4"))
        )
    )

    private val pideHastaCorrecto = LessonContent(
        lessonId = "n07_l09",
        title = "Pide hasta que sea correcto 🔐",
        hook = "La puerta secreta de Glitch City pide una clave y no se rinde hasta que la aciertes.",
        explanation = "while con input() vuelve a preguntar mientras la respuesta no sirva. Fíjate que se pregunta una vez antes del bucle y otra vez al final de cada vuelta. La salida de abajo es la que verías si escribes hola y luego glitch.",
        code = "clave = input(\"Clave: \")\nwhile clave != \"glitch\":\n    print(\"Incorrecta\")\n    clave = input(\"Clave: \")\nprint(\"¡Bienvenido!\")",
        expectedOutput = "Clave: hola\nIncorrecta\nClave: glitch\n¡Bienvenido!",
        exercises = listOf(
            MultipleChoice(
                prompt = "Si el usuario escribe «a», luego «b» y luego «glitch», ¿cuántas veces se pide la clave en total?",
                options = listOf("1", "2", "3", "4"),
                correctIndex = 2,
                correctFeedback = "¡Sí! Una antes del bucle y dos dentro: 3 en total.",
                wrongFeedback = "Cuenta la pregunta de antes del bucle y la de cada vuelta. Las vueltas terminan cuando escribe glitch."
            ),
            FillBlanks(
                prompt = "Completa para repetir mientras la clave sea distinta de «glitch».",
                codeTemplate = "while clave ___ \"glitch\":\n    clave = input(\"Clave: \")",
                answers = listOf("!="),
                chips = listOf("!=", "==", "=", "<>"),
                correctFeedback = "¡Eso! != significa «distinto de».",
                wrongFeedback = "Hay que repetir mientras NO sea igual. En Python «distinto» se escribe con ! e =."
            ),
            OrderLines(
                prompt = "Ordena el programa de la puerta secreta.",
                lines = listOf(
                    "clave = input(\"Clave: \")",
                    "while clave != \"glitch\":",
                    "    print(\"Incorrecta\")",
                    "    clave = input(\"Clave: \")",
                    "print(\"¡Bienvenido!\")"
                ),
                correctFeedback = "¡Puerta abierta! Pregunta antes, repite dentro y da la bienvenida al final.",
                wrongFeedback = "La primera pregunta va antes del while; dentro se avisa y se vuelve a preguntar; la bienvenida va fuera."
            )
        ),
        walkthrough = listOf(
            step(1, "Pregunta la clave. Supón que el usuario escribe hola.", listOf("clave" to "\"hola\""), listOf("Clave: hola")),
            step(2, "\"hola\" != \"glitch\" es verdadero: entra al bucle.", listOf("clave" to "\"hola\""), listOf("Clave: hola")),
            step(3, "Avisa que está mal.", listOf("clave" to "\"hola\""), listOf("Clave: hola", "Incorrecta")),
            step(4, "Vuelve a preguntar. Ahora escribe glitch.", listOf("clave" to "\"glitch\""), listOf("Clave: hola", "Incorrecta", "Clave: glitch")),
            step(2, "\"glitch\" != \"glitch\" es falso: sale del bucle.", listOf("clave" to "\"glitch\""), listOf("Clave: hola", "Incorrecta", "Clave: glitch")),
            step(5, "Da la bienvenida.", listOf("clave" to "\"glitch\""), listOf("Clave: hola", "Incorrecta", "Clave: glitch", "¡Bienvenido!"))
        )
    )

    private val decidirDentro = LessonContent(
        lessonId = "n07_l10",
        title = "Decidir dentro del bucle 🔀",
        hook = "Glitch revisa los números del 1 al 4 y solo cuenta los pares. Un if dentro del while decide en cada vuelta.",
        explanation = "Dentro de un bucle puede ir un if: en cada vuelta se revisa y solo a veces se ejecuta su bloque. Ojo con la sangría: n += 1 va al nivel del while, no dentro del if, para que se ejecute en todas las vueltas.",
        code = "n = 1\npares = 0\nwhile n <= 4:\n    if n % 2 == 0:\n        pares += 1\n    n += 1\nprint(pares)",
        expectedOutput = "2",
        exercises = listOf(
            PredictOutput(
                prompt = "¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "n = 1\nwhile n <= 5:\n    if n % 2 == 1:\n        print(n)\n    n += 1",
                acceptedAnswers = listOf("1\n3\n5"),
                correctFeedback = "¡Muy bien! Solo los impares (resto 1) se imprimen.",
                wrongFeedback = "n recorre del 1 al 5, pero el print está dentro del if: solo sale cuando el resto entre 2 es 1."
            ),
            FillBlanks(
                prompt = "Completa la condición que detecta si n es par.",
                codeTemplate = "if n % 2 ___ 0:\n    pares += 1",
                answers = listOf("=="),
                chips = listOf("==", "=", "!=", ">"),
                correctFeedback = "¡Sí! Par significa que el resto entre 2 es igual a 0.",
                wrongFeedback = "Para comparar si dos cosas son iguales se usan dos signos =."
            ),
            FindBug(
                prompt = "Este programa se queda pegado para siempre. Toca la línea mal ubicada.",
                codeLines = listOf(
                    "n = 1",
                    "pares = 0",
                    "while n <= 6:",
                    "    if n % 2 == 0:",
                    "        pares += 1",
                    "        n += 1"
                ),
                bugLineIndex = 5,
                correctFeedback = "¡Bug cazado! Con n = 1 (impar) no entra al if, n nunca sube y el bucle no termina.",
                wrongFeedback = "Mira la sangría: ¿qué pasa con n cuando es impar?"
            )
        ),
        walkthrough = listOf(
            step(1, "n empieza en 1.", listOf("n" to "1")),
            step(2, "El contador de pares empieza en 0.", listOf("n" to "1", "pares" to "0")),
            step(3, "1 <= 4: entra.", listOf("n" to "1", "pares" to "0")),
            step(4, "1 % 2 == 0 es falso (1 es impar): se salta la línea 5.", listOf("n" to "1", "pares" to "0")),
            step(6, "n sube a 2 (esta línea va en todas las vueltas).", listOf("n" to "2", "pares" to "0")),
            step(3, "2 <= 4: otra vuelta.", listOf("n" to "2", "pares" to "0")),
            step(4, "2 % 2 == 0 es verdadero: es par.", listOf("n" to "2", "pares" to "0")),
            step(5, "Cuenta un par.", listOf("n" to "2", "pares" to "1")),
            step(6, "n sube a 3.", listOf("n" to "3", "pares" to "1")),
            step(3, "3 <= 4: otra vuelta.", listOf("n" to "3", "pares" to "1")),
            step(4, "3 es impar: se salta la línea 5.", listOf("n" to "3", "pares" to "1")),
            step(6, "n sube a 4.", listOf("n" to "4", "pares" to "1")),
            step(3, "4 <= 4: otra vuelta.", listOf("n" to "4", "pares" to "1")),
            step(4, "4 es par.", listOf("n" to "4", "pares" to "1")),
            step(5, "Cuenta otro par.", listOf("n" to "4", "pares" to "2")),
            step(6, "n sube a 5.", listOf("n" to "5", "pares" to "2")),
            step(3, "5 <= 4: falso, termina.", listOf("n" to "5", "pares" to "2")),
            step(7, "Muestra cuántos pares encontró.", listOf("n" to "5", "pares" to "2"), listOf("2"))
        )
    )

    private val glitchHastaLaMeta = LessonContent(
        lessonId = "n07_l11",
        title = "Glitch hasta la meta 🤖",
        hook = "Glitch no sabe a cuántas casillas está la meta. Con while puede avanzar hasta llegar, sin contar antes.",
        explanation = "en_meta() responde True o False según si Glitch ya llegó. while not en_meta() significa «mientras no esté en la meta»: repite avanzar() y se detiene solo. Funciona igual si la meta está a 2 o a 20 casillas. En el ejemplo, supón que la meta está 3 casillas adelante.",
        code = "pasos = 0\nwhile not en_meta():\n    avanzar()\n    pasos += 1\nprint(\"Llegué en\", pasos, \"pasos\")",
        expectedOutput = "Llegué en 3 pasos",
        exercises = listOf(
            GlitchPuzzle(
                prompt = "Lleva a Glitch a la meta con un while, sin contar las casillas.",
                board = listOf(">....M"),
                mode = GlitchMode.BUILD,
                palette = listOf(GlitchBlock.WhileNotGoal, GlitchBlock.Forward),
                solution = "while not en_meta():\n    avanzar()",
                maxLines = 2,
                correctFeedback = "¡Llegó! Dos líneas sirven para cualquier distancia.",
                wrongFeedback = "Pon el while y, dentro con sangría, la orden que lo hace avanzar."
            ),
            GlitchPuzzle(
                prompt = "Ahora hay una pared en el camino. Dentro del while, si hay pared gira a la derecha; si no, avanza.",
                board = listOf(">..#", "....", "..M."),
                mode = GlitchMode.BUILD,
                palette = listOf(
                    GlitchBlock.WhileNotGoal,
                    GlitchBlock.IfWall(),
                    GlitchBlock.Else,
                    GlitchBlock.Forward,
                    GlitchBlock.TurnRight
                ),
                solution = "while not en_meta():\n    if hay_pared():\n        girar_derecha()\n    else:\n        avanzar()",
                maxLines = 5,
                correctFeedback = "¡Excelente! Un if dentro del while decide en cada paso.",
                wrongFeedback = "Dentro del while va el if hay_pared() con su giro, y en el else el avance."
            ),
            FillBlanks(
                prompt = "Completa para que Glitch repita mientras no esté en la meta.",
                codeTemplate = "___ not en_meta():\n    avanzar()",
                answers = listOf("while"),
                chips = listOf("while", "if", "for", "else"),
                correctFeedback = "¡Sí! while repite; if solo daría un paso.",
                wrongFeedback = "Necesitas la palabra que repite mientras la condición se cumpla."
            )
        ),
        walkthrough = listOf(
            step(1, "Todavía no ha dado pasos.", listOf("pasos" to "0")),
            step(2, "¿Está en la meta? No, así que not en_meta() es verdadero: entra.", listOf("pasos" to "0")),
            step(3, "Avanza una casilla.", listOf("pasos" to "0")),
            step(4, "Cuenta el paso.", listOf("pasos" to "1")),
            step(2, "Todavía no está en la meta: otra vuelta.", listOf("pasos" to "1")),
            step(3, "Avanza.", listOf("pasos" to "1")),
            step(4, "Cuenta el paso.", listOf("pasos" to "2")),
            step(2, "Aún no llega: otra vuelta.", listOf("pasos" to "2")),
            step(3, "Avanza: ahora está sobre la meta.", listOf("pasos" to "2")),
            step(4, "Cuenta el paso.", listOf("pasos" to "3")),
            step(2, "Ya está en la meta: not en_meta() es falso y sale.", listOf("pasos" to "3")),
            step(5, "Avisa cuántos pasos dio.", listOf("pasos" to "3"), listOf("Llegué en 3 pasos"))
        )
    )

    // Jefe del nivel: mezcla lo aprendido en las 11 lecciones.
    val boss = BossContent(
        bossId = "n07_boss",
        name = "El Reloj Infinito",
        emoji = "⏰",
        intro = "Un reloj gigante quedó atrapado en un bucle que nunca termina. Tic, tac, tic, tac… Solo lo detiene quien domina el while.",
        exercises = listOf(
            PredictOutput(
                prompt = "Primer tic. ¿Qué muestra este programa? Escríbelo línea por línea.",
                code = "n = 6\nwhile n > 0:\n    print(n)\n    n -= 2",
                acceptedAnswers = listOf("6\n4\n2"),
                correctFeedback = "¡Golpe! 6, 4 y 2; con 0 ya no se cumple n > 0.",
                wrongFeedback = "Resta 2 en cada vuelta y revisa si el 0 se imprime: la condición es n > 0."
            ),
            FindBug(
                prompt = "El reloj escondió el error que lo mantiene girando. ¡Tócalo!",
                codeLines = listOf(
                    "tic = 0",
                    "while tic < 5:",
                    "    print(\"tic\")",
                    "    tic = 0"
                ),
                bugLineIndex = 3,
                correctFeedback = "¡Bug golpeado! tic vuelve a 0 en cada vuelta y nunca llega a 5.",
                wrongFeedback = "¿La variable de la condición se acerca a 5? Mira la última línea del bloque."
            ),
            PredictOutput(
                prompt = "El reloj suma sus engranajes. ¿Qué muestra este programa?",
                code = "total = 0\nn = 2\nwhile n <= 6:\n    total += n\n    n += 2\nprint(total)",
                acceptedAnswers = listOf("12"),
                correctFeedback = "¡Muy bien! 2 + 4 + 6 = 12.",
                wrongFeedback = "n toma los valores 2, 4 y 6. Súmalos."
            ),
            FillBlanks(
                prompt = "Completa para que el reloj haga tac 3 veces y se detenga.",
                codeTemplate = "energia = 3\n___ energia > 0:\n    print(\"tac\")\n    energia ___ 1",
                answers = listOf("while", "-="),
                chips = listOf("while", "if", "-=", "+="),
                correctFeedback = "¡Casi lo tienes! Repite y baja la energía hasta 0.",
                wrongFeedback = "Hace falta la palabra que repite y algo que haga bajar la energía."
            ),
            TypeCode(
                prompt = "Último golpe: escribe la línea del while que repite mientras vidas sea mayor que 0.",
                acceptedAnswers = listOf("while vidas > 0:"),
                placeholder = "while ...",
                correctFeedback = "¡Reloj detenido! Dominas el while.",
                wrongFeedback = "Escribe while, la condición vidas > 0 y los dos puntos al final."
            )
        )
    )

    val all: List<LessonContent> = listOf(
        repiteMientras, atajos, cuentaRegresiva, nuncaSeCumple,
        bucleInfinito, acumular, deDosEnDos, hastaUnaMeta,
        pideHastaCorrecto, decidirDentro, glitchHastaLaMeta
    )
}
