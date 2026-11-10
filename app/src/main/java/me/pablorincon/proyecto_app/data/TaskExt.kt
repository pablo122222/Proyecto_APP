package me.pablorincon.proyecto_app.data

import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Source
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

// Falta de red: Firebase la avisa con estas dos excepciones (Firestore usa UNAVAILABLE).
internal fun Exception.isNetworkError(): Boolean =
    this is FirebaseNetworkException ||
        (this is FirebaseFirestoreException && code == FirebaseFirestoreException.Code.UNAVAILABLE)

// Convierte una tarea de Firebase (callbacks) en una función suspend,
// así no hace falta agregar la librería kotlinx-coroutines-play-services.
internal suspend fun <T> Task<T>.awaitResult(): T = suspendCancellableCoroutine { continuation ->
    addOnCompleteListener { task ->
        val error = task.exception
        when {
            error != null -> continuation.resumeWithException(error)
            task.isCanceled -> continuation.cancel()
            else -> continuation.resume(task.result)
        }
    }
}

// Cuando la red se acaba de caer o es muy mala, Firestore todavía no sabe que está sin conexión
// y una lectura o una transacción pueden tardar más de un minuto en fallar (probado en el
// emulador con modo avión). Pasado este límite se trata como falta de conexión.
internal const val FIRESTORE_TIMEOUT_MILLIS = 10_000L

private fun noResponse() = FirebaseFirestoreException(
    "Firestore no respondió a tiempo",
    FirebaseFirestoreException.Code.UNAVAILABLE
)

// Para transacciones: sin respuesta a tiempo falla como falta de conexión. Es seguro aunque
// la transacción llegue a aplicarse después: todas comprueban lo ya hecho y no duplican nada.
internal suspend fun <T> Task<T>.awaitOrNoConnection(): T = try {
    withTimeout(FIRESTORE_TIMEOUT_MILLIS) { awaitResult() }
} catch (e: TimeoutCancellationException) {
    throw noResponse()
}

// Lee un documento. Si el servidor no responde a tiempo usa la copia guardada en el teléfono;
// si tampoco la hay, falla como falta de conexión (Firestore lanza UNAVAILABLE).
internal suspend fun DocumentReference.read(): DocumentSnapshot = try {
    withTimeout(FIRESTORE_TIMEOUT_MILLIS) { get().awaitResult() }
} catch (e: TimeoutCancellationException) {
    get(Source.CACHE).awaitResult()
}
