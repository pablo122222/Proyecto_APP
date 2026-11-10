package me.pablorincon.proyecto_app.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.withTimeoutOrNull
import me.pablorincon.proyecto_app.domain.UserProfile
import me.pablorincon.proyecto_app.domain.glitchcity.GlitchCityRules
import me.pablorincon.proyecto_app.domain.glitchcity.GlitchCityState

// Único lugar de la app que lee y guarda el progreso de Glitch City (users/{uid}/progress/glitchCity)
// y el título equipado. Igual que los otros repositorios: las pantallas solo ven resultados simples.
class GlitchCityRepository(private val db: FirebaseFirestore = FirebaseFirestore.getInstance()) {

    sealed interface LoadResult {
        data class Success(val city: GlitchCityState) : LoadResult
        data object NoConnection : LoadResult
        data object Failed : LoadResult
    }

    enum class Result { Done, NoConnection, Failed }

    private fun user(uid: String) = db.collection(USERS).document(uid)
    private fun city(uid: String) = user(uid).collection(PROGRESS).document(GLITCH_CITY)

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

    private companion object {
        const val USERS = "users"
        const val PROGRESS = "progress"
        const val GLITCH_CITY = "glitchCity"
        const val TIMEOUT_MILLIS = 8_000L
    }
}
