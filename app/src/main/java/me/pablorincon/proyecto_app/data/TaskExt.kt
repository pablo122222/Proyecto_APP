package me.pablorincon.proyecto_app.data

import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.suspendCancellableCoroutine
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
