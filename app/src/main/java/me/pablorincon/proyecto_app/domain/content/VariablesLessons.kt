package me.pablorincon.proyecto_app.domain.content

import me.pablorincon.proyecto_app.domain.LessonContent
import me.pablorincon.proyecto_app.domain.MultipleChoice

// Módulo 2 · Variables y tipos de datos. Sigue con Glitch como hilo conductor.
// Las 5 lecciones fueron aprobadas por el dueño del proyecto (decisión C).
internal object VariablesLessons {

    private val laCajaConEtiqueta = LessonContent(
        lessonId = "variables_1",
        title = "La caja con etiqueta 📦",
        hook = "Glitch tiene un problema: se le olvida todo. Le damos cajas con etiquetas para que guarde sus cosas.",
        explanation = "Una variable es una caja con nombre donde se guarda un dato. Se crea con =: el nombre va a la izquierda y el valor a la derecha.",
        code = "nombre = \"Glitch\"\nenergia = 100\nprint(nombre)\nprint(energia)",
        expectedOutput = "Glitch\n100",
        exercises = listOf(
            MultipleChoice(
                prompt = "Glitch ejecuta ciudad = \"Glitch City\", luego print(\"ciudad\") y luego print(ciudad). ¿Qué se muestra?",
                options = listOf(
                    "ciudad y luego Glitch City",
                    "Glitch City dos veces",
                    "ciudad dos veces",
                    "Un error"
                ),
                correctIndex = 0,
                correctFeedback = "¡Bien visto! Con comillas es texto literal; sin comillas, Python busca la caja con ese nombre.",
                wrongFeedback = "Compara con lo que aprendiste: entre comillas es texto tal cual; sin comillas es el nombre de una caja."
            ),
            MultipleChoice(
                prompt = "¿Qué línea guarda el número 50 en una variable llamada bateria?",
                options = listOf(
                    "50 = bateria",
                    "bateria = 50",
                    "bateria == 50",
                    "bateria -> 50"
                ),
                correctIndex = 1,
                correctFeedback = "¡Sí! El nombre a la izquierda y el valor a la derecha.",
                wrongFeedback = "El = guarda lo de la derecha en la caja de la izquierda. Solo una opción respeta ese orden con un solo =."
            ),
            MultipleChoice(
                prompt = "¿Cuál es un nombre de variable válido en Python?",
                options = listOf(
                    "2robots",
                    "mi robot",
                    "mi_robot",
                    "mi-robot"
                ),
                correctIndex = 2,
                correctFeedback = "¡Correcto! Con letras, números y guion bajo, y sin empezar por número.",
                wrongFeedback = "Los nombres no pueden empezar por número ni llevar espacios o guiones medios. El guion bajo sí vale."
            )
        )
    )

    private val cambiandoLoQueHayDentro = LessonContent(
        lessonId = "variables_2",
        title = "Cambiando lo que hay dentro 🔋",
        hook = "La batería de Glitch se gasta. Hay que actualizar la caja cada vez que baja.",
        explanation = "El valor de una variable puede cambiar. Python calcula primero lo de la derecha y después lo guarda.",
        code = "energia = 100\nenergia = energia - 30\nprint(energia)",
        expectedOutput = "70",
        exercises = listOf(
            MultipleChoice(
                prompt = "energia = 100, luego energia = energia - 30 dos veces, y por último print(energia). ¿Qué muestra?",
                options = listOf("30", "40", "70", "-60"),
                correctIndex = 1,
                correctFeedback = "¡Exacto! 100 → 70 → 40. Cada línea actualiza la caja.",
                wrongFeedback = "Sigue la caja paso a paso: empieza en 100, baja 30 y vuelve a bajar 30."
            ),
            MultipleChoice(
                prompt = "bateria = 10, luego copia = bateria, luego bateria = 99 y por último print(copia). ¿Qué se muestra?",
                options = listOf("99", "10", "109", "Un error"),
                correctIndex = 1,
                correctFeedback = "¡Muy bien! copia guardó el valor en ese momento y no sigue a bateria.",
                wrongFeedback = "Al copiar, copia se queda con el valor de ese instante (10). Cambiar bateria después no la afecta."
            ),
            MultipleChoice(
                prompt = "Glitch gasta 5 de energía y la variable debe quedar actualizada. ¿Qué línea lo hace?",
                options = listOf(
                    "energia - 5",
                    "energia = energia - 5",
                    "5 = energia - energia",
                    "energia == energia - 5"
                ),
                correctIndex = 1,
                correctFeedback = "¡Sí! Calcula y guarda el resultado en la misma caja.",
                wrongFeedback = "Calcular no basta: hay que guardar el resultado con =. Si no, la caja sigue igual."
            )
        )
    )

    private val cadaDatoTieneSuTipo = LessonContent(
        lessonId = "variables_3",
        title = "Cada dato tiene su tipo 🧪",
        hook = "En el laboratorio de Glitch hay frascos distintos: uno para palabras, otro para números enteros y otro para decimales. Si mezclas, ¡explota!",
        explanation = "Los datos tienen tipo: texto (str), entero (int) y decimal (float). type() te dice cuál es.",
        code = "nombre = \"Glitch\"\nedad = 3\naltura = 1.5\nprint(type(nombre))\nprint(type(edad))\nprint(type(altura))",
        expectedOutput = "<class 'str'>\n<class 'int'>\n<class 'float'>",
        exercises = listOf(
            MultipleChoice(
                prompt = "¿De qué tipo es el valor 7.0?",
                options = listOf("int", "float", "str", "No es un tipo válido"),
                correctIndex = 1,
                correctFeedback = "¡Eso! Si tiene punto decimal es float, aunque sea .0.",
                wrongFeedback = "Mira el punto decimal: los números con punto son float, aunque terminen en .0."
            ),
            MultipleChoice(
                prompt = "¿Qué tipo tiene \"42\" escrito con comillas?",
                options = listOf("int", "float", "str", "Depende del número"),
                correctIndex = 2,
                correctFeedback = "¡Exacto! Las comillas lo convierten en texto, aunque parezca un número.",
                wrongFeedback = "Las comillas mandan: todo lo que va entre comillas es texto (str)."
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

    private val sumarPerasConManzanas = LessonContent(
        lessonId = "variables_4",
        title = "Sumar peras con manzanas 🍐",
        hook = "Glitch intenta sumar «5» + «5» y le sale 55. ¿Se le rompió la calculadora? Para nada: hay un truco.",
        explanation = "Con números, + suma. Con textos, + los une. Pero no puedes sumar un texto y un número directamente.",
        code = "print(5 + 5)\nprint(\"5\" + \"5\")\nprint(\"Hola, \" + \"Glitch\")",
        expectedOutput = "10\n55\nHola, Glitch",
        exercises = listOf(
            MultipleChoice(
                prompt = "¿Qué muestra print(\"3\" + \"4\")?",
                options = listOf("7", "34", "\"34\" con comillas", "Un error"),
                correctIndex = 1,
                correctFeedback = "¡Sí! Con textos, + los une uno detrás del otro.",
                wrongFeedback = "Las comillas indican texto, y con texto + junta. No suma."
            ),
            MultipleChoice(
                prompt = "¿Qué pasa al ejecutar print(\"Robots: \" + 5)?",
                options = listOf(
                    "Muestra Robots: 5",
                    "Muestra Robots: 55",
                    "Da error: no se puede unir texto y número directamente",
                    "Muestra Robots: y se ignora el 5"
                ),
                correctIndex = 2,
                correctFeedback = "¡Bien visto! Python no mezcla texto y número sin que se lo pidas.",
                wrongFeedback = "Python es estricto: texto y número no se unen solos. Da un TypeError."
            ),
            MultipleChoice(
                prompt = "¿Cuál de estas líneas arregla el problema?",
                options = listOf(
                    "print(\"Robots: \" - 5)",
                    "print(\"Robots: \" + str(5))",
                    "print(Robots: + 5)",
                    "print(\"Robots: \" 5)"
                ),
                correctIndex = 1,
                correctFeedback = "¡Eso! str(5) convierte el número en texto, y así sí se pueden unir.",
                wrongFeedback = "Hay que convertir el 5 en texto con str() antes de unirlo. Las demás opciones están mal escritas o restan texto."
            )
        )
    )

    private val preguntaleAlUsuario = LessonContent(
        lessonId = "variables_5",
        title = "Pregúntale al usuario 🎤",
        hook = "Glitch quiere conocer a su nuevo amigo. Por primera vez va a esperar una respuesta y a usarla.",
        explanation = "input() pide un dato al usuario. Siempre devuelve texto; si lo quieres como número, conviértelo con int(). La salida de abajo es la que verías si escribes Ana y 20.",
        code = "nombre = input(\"¿Cómo te llamas? \")\nedad = int(input(\"¿Cuántos años tienes? \"))\nprint(\"Hola, \" + nombre)\nprint(\"El año que viene tendrás \" + str(edad + 1))",
        expectedOutput = "¿Cómo te llamas? Ana\n¿Cuántos años tienes? 20\nHola, Ana\nEl año que viene tendrás 21",
        exercises = listOf(
            MultipleChoice(
                prompt = "El usuario escribe 20 en un input(). ¿De qué tipo es lo que devuelve?",
                options = listOf("int", "str", "float", "Depende de lo que escriba"),
                correctIndex = 1,
                correctFeedback = "¡Exacto! input() siempre devuelve texto, aunque escribas un número.",
                wrongFeedback = "input() no adivina: todo lo que se escribe llega como texto."
            ),
            MultipleChoice(
                prompt = "edad = input(\"Edad: \"), el usuario escribe 20 y luego se ejecuta print(edad + 1). ¿Qué pasa?",
                options = listOf(
                    "Muestra 21",
                    "Muestra 201",
                    "Da error: no se puede sumar texto y número",
                    "Muestra 20 + 1"
                ),
                correctIndex = 2,
                correctFeedback = "¡Bien! edad es texto, y texto + número da error.",
                wrongFeedback = "edad es texto (\"20\"), y sumar texto con un número da TypeError. Hace falta int()."
            ),
            MultipleChoice(
                prompt = "¿Cuál convierte lo que escribe el usuario en un número entero?",
                options = listOf(
                    "edad = int(input(\"Edad: \"))",
                    "edad = input(int(\"Edad: \"))",
                    "edad = str(input(\"Edad: \"))",
                    "edad = input(\"Edad: \") + 0"
                ),
                correctIndex = 0,
                correctFeedback = "¡Sí! Primero se pide con input y el resultado se convierte con int.",
                wrongFeedback = "La conversión envuelve al input desde afuera: int( + input(...) + ). str hace lo contrario."
            )
        )
    )

    val all: List<LessonContent> = listOf(
        laCajaConEtiqueta,
        cambiandoLoQueHayDentro,
        cadaDatoTieneSuTipo,
        sumarPerasConManzanas,
        preguntaleAlUsuario
    )
}
