package me.pablorincon.proyecto_app.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.pablorincon.proyecto_app.BuildConfig
import me.pablorincon.proyecto_app.domain.tutor.TutorMessage
import me.pablorincon.proyecto_app.domain.tutor.TutorPrompt
import me.pablorincon.proyecto_app.domain.tutor.TutorReply
import me.pablorincon.proyecto_app.domain.tutor.TutorRepository
import me.pablorincon.proyecto_app.domain.tutor.TutorTurn
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

// Tutor con un modelo de IA local (Ollama corriendo en el PC). Si Ollama no responde
// (PC apagado, otra red, modelo sin descargar), contesta la demo: el chat nunca se queda sin respuesta.
class OllamaTutorRepository(
    private val chat: suspend (List<TutorTurn>) -> String,
    private val fallback: TutorRepository = DemoTutorRepository(),
    private val onError: (Exception) -> Unit = {}
) : TutorRepository {

    override suspend fun reply(question: String, history: List<TutorMessage>): TutorReply {
        val text = try {
            chat(TutorPrompt.turns(question, history)).trim()
        } catch (e: Exception) {
            onError(e)
            ""
        }
        return if (text.isNotEmpty()) TutorReply.Free(text) else fallback.reply(question, history)
    }

    companion object {
        private const val TAG = "OllamaTutor"

        // Dirección y modelo vienen de local.properties (solo en debug). En release están vacíos.
        fun fromBuildConfig(): OllamaTutorRepository? {
            val url = BuildConfig.OLLAMA_URL
            if (url.isBlank()) return null
            val client = OllamaClient(url, BuildConfig.OLLAMA_MODEL)
            return OllamaTutorRepository(
                chat = client::chat,
                onError = { Log.w(TAG, "Ollama no respondió en $url; contesta la demo", it) }
            )
        }
    }
}

// Llamada a la API de Ollama (/api/chat) con lo que Android ya trae: sin librerías extra.
class OllamaClient(private val baseUrl: String, private val model: String) {

    suspend fun chat(turns: List<TutorTurn>): String = withContext(Dispatchers.IO) {
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
            val body = connection.inputStream.bufferedReader(Charsets.UTF_8).use { it.readText() }
            JSONObject(body).optJSONObject("message")?.optString("content").orEmpty()
        } finally {
            connection.disconnect()
        }
    }

    private fun requestBody(turns: List<TutorTurn>): String = JSONObject()
        .put("model", model)
        .put("stream", false)
        .put("keep_alive", KEEP_ALIVE) // el modelo sigue cargado entre preguntas
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
    }
}
