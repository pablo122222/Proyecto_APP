package me.pablorincon.proyecto_app.data

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.CancellationException
import me.pablorincon.proyecto_app.domain.AchievementUnlock
import me.pablorincon.proyecto_app.domain.CompletionOutcome
import me.pablorincon.proyecto_app.domain.CompletionStats
import me.pablorincon.proyecto_app.domain.Curriculum
import me.pablorincon.proyecto_app.domain.PathStep
import java.time.LocalDate

// Único lugar de la app que lee y guarda el progreso del currículo (users/{uid}/progress/curriculum)
// y lo que se gana al completar pasos (XP, racha, logros y gemas).
// Igual que los otros repositorios: las pantallas solo ven resultados simples.
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
        // unlocks: niveles de logro ganados con esta lección (para avisar al usuario)
        data class Completed(val xpAwarded: Int, val unlocks: List<AchievementUnlock> = emptyList()) : CompleteResult
        data object AlreadyCompleted : CompleteResult // Sin XP: ya se había completado
        data object NoConnection : CompleteResult
        data object Failed : CompleteResult
    }

    // Marca la lección (o el jefe) como completada, suma su XP, registra la actividad del día
    // (racha y meta diaria) y desbloquea los logros alcanzados con sus gemas, todo junto y una
    // sola vez. Usa una transacción: así nada se duplica si se reintenta, y sin red falla en
    // lugar de dejar una escritura en cola que se aplicaría dos veces al reintentar.
    suspend fun completeLesson(
        uid: String,
        lessonId: String,
        xp: Int,
        stats: CompletionStats = CompletionStats(),
        today: LocalDate = LocalDate.now()
    ): CompleteResult = try {
        val userRef = db.collection(USERS).document(uid)
        val progressRef = userRef.collection(PROGRESS).document(CURRICULUM)
        val outcome = db.runTransaction { tx ->
            // En una transacción todas las lecturas van antes de las escrituras
            val already = ProgressMapper.completedLessonIds(tx.get(progressRef).data)
            val user = UserProfileMapper.fromMap(tx.get(userRef).data.orEmpty())
            if (lessonId in already) {
                null
            } else {
                tx.set(
                    progressRef,
                    mapOf(ProgressMapper.COMPLETED_LESSONS to FieldValue.arrayUnion(lessonId)),
                    SetOptions.merge()
                )
                val result = CompletionOutcome.completing(user, stepsOf(already + lessonId), stats, today)
                tx.update(userRef, outcomeFields(result) + (UserProfileMapper.XP to FieldValue.increment(xp.toLong())))
                result
            }
        }.awaitResult()
        if (outcome != null) CompleteResult.Completed(xp, outcome.unlocks) else CompleteResult.AlreadyCompleted
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        if (e.isNetworkError()) CompleteResult.NoConnection else CompleteResult.Failed
    }

    // Desbloquea los logros que el progreso ya alcanza y aún no se habían entregado (por ejemplo,
    // lo hecho antes de que existieran los logros). Devuelve lo ganado para avisar; vacío si nada.
    suspend fun claimPendingAchievements(uid: String): List<AchievementUnlock> = try {
        val userRef = db.collection(USERS).document(uid)
        val progressRef = userRef.collection(PROGRESS).document(CURRICULUM)
        val cityRef = userRef.collection(PROGRESS).document(GLITCH_CITY)
        db.runTransaction { tx ->
            val completed = ProgressMapper.completedLessonIds(tx.get(progressRef).data)
            val city = GlitchCityMapper.fromMap(tx.get(cityRef).data)
            val userData = tx.get(userRef).data ?: return@runTransaction emptyList()
            val result = CompletionOutcome.pending(UserProfileMapper.fromMap(userData), stepsOf(completed), city)
            if (result.unlocks.isNotEmpty()) {
                tx.update(
                    userRef,
                    mapOf(
                        UserProfileMapper.BEST_STREAK to result.bestStreak,
                        UserProfileMapper.ACHIEVEMENT_TIERS to result.achievementTiers,
                        UserProfileMapper.GEMS to FieldValue.increment(result.gemsGained.toLong())
                    )
                )
            }
            result.unlocks
        }.awaitResult()
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        emptyList() // Se reintentará la próxima vez: no es un error que mostrar
    }

    private companion object {
        const val USERS = "users"
        const val PROGRESS = "progress"
        const val CURRICULUM = "curriculum"
        const val GLITCH_CITY = "glitchCity"
    }
}

// Pasos de la ruta con esos ids (para medir los logros).
internal fun stepsOf(ids: Set<String>): List<PathStep> = Curriculum.Base.steps.filter { it.id in ids }

// Campos del perfil que cambian al completar una actividad (racha, meta diaria, logros y sus
// gemas). También lo usa Glitch City.
internal fun outcomeFields(o: CompletionOutcome): Map<String, Any> = mapOf(
    UserProfileMapper.STREAK to o.activity.streak,
    UserProfileMapper.LAST_ACTIVE_DATE to o.activity.lastActiveDate.orEmpty(),
    UserProfileMapper.ACTIVITIES_ON_LAST_DAY to o.activity.activitiesOnLastDay,
    UserProfileMapper.BEST_STREAK to o.bestStreak,
    UserProfileMapper.BUGS_FOUND to o.bugsFound,
    UserProfileMapper.RECOVERED_EXERCISES to o.recovered,
    UserProfileMapper.ACHIEVEMENT_TIERS to o.achievementTiers,
    UserProfileMapper.GEMS to FieldValue.increment(o.gemsGained.toLong())
)
