package me.pablorincon.proyecto_app.data

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.CancellationException
import java.time.LocalDate

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

    sealed interface CompleteResult {
        data class Completed(val xpAwarded: Int) : CompleteResult
        data object AlreadyCompleted : CompleteResult // Sin XP: ya se había completado
        data object NoConnection : CompleteResult
        data object Failed : CompleteResult
    }

    // Marca la lección (o el jefe) como completada, suma su XP y registra la actividad del día
    // (racha y meta diaria), todo junto y una sola vez.
    // Usa una transacción: así el XP no se duplica si se reintenta, y sin red falla en lugar
    // de dejar una escritura en cola que se aplicaría dos veces al reintentar.
    suspend fun completeLesson(
        uid: String,
        lessonId: String,
        xp: Int,
        today: LocalDate = LocalDate.now()
    ): CompleteResult = try {
        val userRef = db.collection(USERS).document(uid)
        val progressRef = userRef.collection(PROGRESS).document(CURRICULUM)
        val awarded = db.runTransaction { tx ->
            // En una transacción todas las lecturas van antes de las escrituras
            val already = ProgressMapper.completedLessonIds(tx.get(progressRef).data)
            val user = UserProfileMapper.fromMap(tx.get(userRef).data.orEmpty())
            if (lessonId in already) {
                false
            } else {
                tx.set(
                    progressRef,
                    mapOf(ProgressMapper.COMPLETED_LESSONS to FieldValue.arrayUnion(lessonId)),
                    SetOptions.merge()
                )
                val activity = user.dailyActivity.record(today)
                tx.update(
                    userRef,
                    mapOf(
                        UserProfileMapper.XP to FieldValue.increment(xp.toLong()),
                        UserProfileMapper.STREAK to activity.streak,
                        UserProfileMapper.LAST_ACTIVE_DATE to activity.lastActiveDate.orEmpty(),
                        UserProfileMapper.ACTIVITIES_ON_LAST_DAY to activity.activitiesOnLastDay
                    )
                )
                true
            }
        }.awaitResult()
        if (awarded) CompleteResult.Completed(xp) else CompleteResult.AlreadyCompleted
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        if (e.isNetworkError()) CompleteResult.NoConnection else CompleteResult.Failed
    }

    private companion object {
        const val USERS = "users"
        const val PROGRESS = "progress"
        const val CURRICULUM = "curriculum"
    }
}
