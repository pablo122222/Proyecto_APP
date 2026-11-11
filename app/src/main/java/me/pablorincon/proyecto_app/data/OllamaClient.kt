package me.pablorincon.proyecto_app.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import me.pablorincon.proyecto_app.domain.tutor.TutorTurn
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

// Llamada a la API de Ollama (/api/chat) con lo que Android ya trae: sin librerías extra.
// Con stream = true, Ollama manda una línea JSON por cada trozo de texto, hasta "done".
// Solo se usa en debug si local.properties tiene ollama.url (ver app/build.gradle.kts).
class OllamaClient(private val baseUrl: String, private val model: String) {

    suspend fun chat(turns: List<TutorTurn>, onPartial: (String) -> Unit): String = withContext(Dispatchers.IO) {
        val connection = URL(baseUrl.trimEnd('/') + "/api/chat").openConnection() as HttpURLConnection
        try {
            connection.requestMethod = "POST"
            connection.connectTimeout = CONNECT_TIMEOUT_MS
            connection.readTimeout = READ_TIMEOUT_MS
            connection.doOutput = true
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8")
            connection.outputStream.use { it.write(requestBody(turns).toByteArray(Charsets.UTF_8)) }

            val code = connection.responseCode
            check(code == HttpURLConnection.HTTP_OK) { "Ollama respondió HTTP $code" }

            val text = StringBuilder()
            connection.inputStream.bufferedReader(Charsets.UTF_8).useLines { lines ->
                for (line in lines) {
                    ensureActive() // si se cierra el chat, deja de leer
                    if (line.isBlank()) continue
                    val chunk = JSONObject(line)
                    val error = chunk.optString("error")
                    check(error.isEmpty()) { "Ollama: $error" }
                    val piece = chunk.optJSONObject("message")?.optString("content").orEmpty()
                    if (piece.isNotEmpty()) {
                        text.append(piece)
                        onPartial(text.toString())
                    }
                    if (chunk.optBoolean("done")) break
                }
            }
            text.toString()
        } finally {
            connection.disconnect()
        }
    }

    private fun requestBody(turns: List<TutorTurn>): String = JSONObject()
        .put("model", model)
        .put("stream", true)
        .put("keep_alive", KEEP_ALIVE) // el modelo sigue cargado entre preguntas
        .put("options", JSONObject()
            .put("temperature", TEMPERATURE)
            .put("num_predict", MAX_TOKENS))
        .put("messages", JSONArray().apply {
            turns.forEach { turn ->
                put(JSONObject().put("role", turn.role.name.lowercase()).put("content", turn.text))
            }
        })
        .toString()

    private companion object {
        const val CONNECT_TIMEOUT_MS = 3_000   // si el PC no está, pasar rápido a la demo
        const val READ_TIMEOUT_MS = 90_000     // la primera pregunta carga el modelo y tarda más
        const val KEEP_ALIVE = "15m"
        const val TEMPERATURE = 0.4            // explicaciones estables, no creativas
        const val MAX_TOKENS = 400             // tope de seguridad (~250 palabras)
    }
}
