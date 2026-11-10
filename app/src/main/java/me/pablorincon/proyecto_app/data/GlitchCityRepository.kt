package me.pablorincon.proyecto_app.data

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.withTimeoutOrNull
import me.pablorincon.proyecto_app.domain.AchievementUnlock
import me.pablorincon.proyecto_app.domain.CompletionOutcome
import me.pablorincon.proyecto_app.domain.CompletionStats
import me.pablorincon.proyecto_app.domain.UserProfile
import me.pablorincon.proyecto_app.domain.glitchcity.Creation
import me.pablorincon.proyecto_app.domain.glitchcity.DroneLevel
import me.pablorincon.proyecto_app.domain.glitchcity.GlitchCityProgress
import me.pablorincon.proyecto_app.domain.glitchcity.GlitchCityRules
import me.pablorincon.proyecto_app.domain.glitchcity.GlitchCityState
import me.pablorincon.proyecto_app.domain.glitchcity.GlitchEvent
import me.pablorincon.proyecto_app.domain.glitchcity.GlitchReward
import me.pablorincon.proyecto_app.domain.glitchcity.Lab
import java.time.LocalDate

// Único lugar de la app que lee y guarda el progreso de Glitch City (users/{uid}/progress/glitchCity)
// y el título equipado. Igual que los otros repositorios: las pantallas solo ven resultados simples.
class GlitchCityRepository(private val db: FirebaseFirestore = FirebaseFirestore.getInstance()) {

    sealed interface LoadResult {
        data class Success(val city: GlitchCityState) : LoadResult
        data object NoConnection : LoadResult
        data object Failed : LoadResult
    }

    enum class Result { Done, NoConnection, Failed }

    sealed interface SaveResult {
        // reward: lo ganado (null si no había nada nuevo); unlocks: niveles de logro ganados
        data class Saved(val reward: GlitchReward?, val unlocks: List<AchievementUnlock> = emptyList()) : SaveResult
        data object NoConnection : SaveResult
        data object Failed : SaveResult
    }

    private fun user(uid: String) = db.collection(USERS).document(uid)
    private fun city(uid: String) = user(uid).collection(PROGRESS).document(GLITCH_CITY)
    private fun curriculum(uid: String) = user(uid).collection(PROGRESS).document(CURRICULUM)

    // Un usuario que aún no entra a los retos no tiene el documento: eso es "sin progreso".
    suspend fun load(uid: String): LoadResult = try {
        val profile = UserProfileMapper.fromMap(user(uid).get().awaitResult().data.orEmpty())
        val progress = GlitchCityMapper.fromMap(city(uid).get().awaitResult().data)
        LoadResult.Success(GlitchCityState(profile, progress))
    } catch (e: CancellationException) {
        throw e // La pantalla se cerró: no es un error que mostrar
    } catch (e: Exception) {
        if (e.isNetworkError()) LoadResult.NoConnection else LoadResult.Failed
    }

    // Solo el progreso de los retos (para Logros e Inicio). null si no se pudo leer.
    suspend fun loadProgress(uid: String): GlitchCityProgress? = try {
        GlitchCityMapper.fromMap(city(uid).get().awaitResult().data)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        null
    }

    suspend fun discover(uid: String, lab: Lab, discoveryId: String, today: LocalDate = LocalDate.now()): SaveResult =
        applyEvent(uid, today) { GlitchCityRules.discover(it, lab, discoveryId) }

    // Publicar por primera vez o actualizar (guarda los valores; solo la primera vez da recompensa).
    suspend fun publish(uid: String, creation: Creation, values: Map<String, Int>, today: LocalDate = LocalDate.now()): SaveResult =
        applyEvent(uid, today) { GlitchCityRules.publish(it, creation, values) }

    // Nivel del dron entregado con `blocks` bloques: la primera vez lo completa; después solo
    // cuenta si mejora el récord. clean: sin ejecuciones fallidas ni idea abierta antes de entregar.
    suspend fun completeDrone(
        uid: String,
        level: DroneLevel,
        blocks: Int,
        clean: Boolean,
        today: LocalDate = LocalDate.now()
    ): SaveResult = applyEvent(uid, today) { GlitchCityRules.completeDrone(it, level, blocks, clean) }

    // Aplica un evento de Glitch City y, si da recompensa, suma XP y gemas y registra la actividad
    // del día (racha, meta diaria y logros), todo junto y una sola vez. Usa una transacción: sin red
    // falla en lugar de dejar en cola una escritura que se aplicaría dos veces al reintentar.
    private suspend fun applyEvent(uid: String, today: LocalDate, event: (GlitchCityProgress) -> GlitchEvent): SaveResult =
        save {
            db.runTransaction { tx ->
                // En una transacción todas las lecturas van antes de las escrituras
                val progress = GlitchCityMapper.fromMap(tx.get(city(uid)).data)
                val profile = UserProfileMapper.fromMap(tx.get(user(uid)).data.orEmpty())
                val completed = ProgressMapper.completedLessonIds(tx.get(curriculum(uid)).data)
                val result = event(progress)
                if (result.progress != progress) tx.set(city(uid), GlitchCityMapper.toMap(result.progress))
                val reward = result.reward ?: return@runTransaction SaveResult.Saved(null)
                val outcome = CompletionOutcome.completing(profile, stepsOf(completed), CompletionStats(), today, result.progress)
                tx.update(
                    user(uid),
                    outcomeFields(outcome) + mapOf(
                        UserProfileMapper.XP to FieldValue.increment(reward.xp.toLong()),
                        UserProfileMapper.GEMS to FieldValue.increment((reward.gems + outcome.gemsGained).toLong())
                    )
                )
                SaveResult.Saved(reward, outcome.unlocks)
            }.awaitResult()
        }

    // Respuesta correcta a "¿Por qué funciona?" (decisión D): suma una vez por actividad al
    // logro "Explica el porqué". No cuenta como actividad del día.
    suspend fun explainWhy(uid: String, activityId: String): SaveResult = save {
        db.runTransaction { tx ->
            val profile = UserProfileMapper.fromMap(tx.get(user(uid)).data.orEmpty())
            val completed = ProgressMapper.completedLessonIds(tx.get(curriculum(uid)).data)
            val progress = GlitchCityMapper.fromMap(tx.get(city(uid)).data)
            if (activityId in profile.explainedWhy) return@runTransaction SaveResult.Saved(null)
            val outcome = CompletionOutcome.pending(
                profile.copy(explainedWhy = profile.explainedWhy + activityId), stepsOf(completed), progress
            )
            tx.update(
                user(uid),
                mapOf(
                    UserProfileMapper.EXPLAINED_WHY to FieldValue.arrayUnion(activityId),
                    UserProfileMapper.ACHIEVEMENT_TIERS to outcome.achievementTiers,
                    UserProfileMapper.GEMS to FieldValue.increment(outcome.gemsGained.toLong())
                )
            )
            SaveResult.Saved(null, outcome.unlocks)
        }.awaitResult()
    }

    // Quien llama comprueba antes con GlitchTitles.canEquip que el título está ganado.
    suspend fun equipTitle(uid: String, titleId: String): Result = attempt {
        withTimeoutOrNull(TIMEOUT_MILLIS) {
            user(uid).set(mapOf(UserProfileMapper.EQUIPPED_TITLE to titleId), SetOptions.merge()).awaitResult()
            Result.Done
        } ?: Result.NoConnection
    }

    // Borra descubrimientos, creaciones, récords y títulos, y quita las gemas ganadas en los
    // retos. El XP se conserva. Usa una transacción: sin red falla en lugar de dejar en cola
    // una resta de gemas que se aplicaría dos veces al reintentar.
    suspend fun reset(uid: String): Result = attempt {
        db.runTransaction { tx ->
            val progress = GlitchCityMapper.fromMap(tx.get(city(uid)).data)
            val profile = UserProfileMapper.fromMap(tx.get(user(uid)).data.orEmpty())
            tx.delete(city(uid))
            tx.set(
                user(uid),
                mapOf(
                    UserProfileMapper.GEMS to GlitchCityRules.gemsAfterReset(profile.gems, progress),
                    UserProfileMapper.EQUIPPED_TITLE to UserProfile.INITIAL_TITLE_ID
                ),
                SetOptions.merge()
            )
        }.awaitResult()
        Result.Done
    }

    private suspend fun attempt(block: suspend () -> Result): Result = try {
        block()
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        if (e.isNetworkError()) Result.NoConnection else Result.Failed
    }

    private suspend fun save(block: suspend () -> SaveResult): SaveResult = try {
        block()
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        if (e.isNetworkError()) SaveResult.NoConnection else SaveResult.Failed
    }

    private companion object {
        const val USERS = "users"
        const val PROGRESS = "progress"
        const val GLITCH_CITY = "glitchCity"
        const val CURRICULUM = "curriculum"
        const val TIMEOUT_MILLIS = 8_000L
    }
}
