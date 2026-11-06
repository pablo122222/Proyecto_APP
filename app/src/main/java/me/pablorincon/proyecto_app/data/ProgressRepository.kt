package me.pablorincon.proyecto_app.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CancellationException

// Único lugar de la app que lee el progreso del currículo (users/{uid}/progress/curriculum).
// Igual que los otros repositorios: las pantallas solo ven resultados simples.
// Guardar lecciones completadas llega en la Fase 10, junto con el XP.
class ProgressRepository(private val db: FirebaseFirestore = FirebaseFirestore.getInstance()) {

    sealed interface ProgressResult {
        data class Success(val completedLessonIds: Set<String>) : ProgressResult
        data object NoConnection : ProgressResult
        data object Failed : ProgressResult
    }

    // Un usuario nuevo aún no tiene el documento: eso es "ninguna lección completada"
    // y no se crea hasta que complete la primera.
    suspend fun loadCompletedLessons(uid: String): ProgressResult = try {
        val snapshot = db.collection(USERS).document(uid)
            .collection(PROGRESS).document(CURRICULUM)
            .get().awaitResult()
        ProgressResult.Success(ProgressMapper.completedLessonIds(snapshot.data))
    } catch (e: CancellationException) {
        throw e // La pantalla se cerró: no es un error que mostrar
    } catch (e: Exception) {
        if (e.isNetworkError()) ProgressResult.NoConnection else ProgressResult.Failed
    }

    private companion object {
        const val USERS = "users"
        const val PROGRESS = "progress"
        const val CURRICULUM = "curriculum"
    }
}
