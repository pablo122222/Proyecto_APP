package me.pablorincon.proyecto_app.domain

// Normalización del texto que escribe el usuario, para no castigar detalles que no son el error.
internal object ExerciseText {

    // Salida de un programa: se ignoran espacios de más y saltos de línea
    // («A B C» y tres líneas A, B, C valen lo mismo). Mayúsculas opcionales.
    fun normalizeOutput(text: String, ignoreCase: Boolean): String {
        val tokens = text.trim().split(Regex("\\s+")).filter { it.isNotEmpty() }.joinToString(" ")
        return if (ignoreCase) tokens.lowercase() else tokens
    }

    // Código: sin espacios (print( "a" ) vale igual que print("a")), comillas simples = dobles
    // y comillas tipográficas = rectas. Las mayúsculas sí cuentan.
    fun normalizeCode(text: String): String = text
        .replace('\'', '"')
        .replace('“', '"').replace('”', '"').replace('‘', '"').replace('’', '"')
        .filterNot { it.isWhitespace() }
}
