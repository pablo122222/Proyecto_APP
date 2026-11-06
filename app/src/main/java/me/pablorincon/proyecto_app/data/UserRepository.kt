package me.pablorincon.proyecto_app.data

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.CancellationException
import me.pablorincon.proyecto_app.domain.UserProfile

// Único lugar de la app que habla con Firestore para el perfil del usuario (users/{uid}).
// Igual que AuthRepository: las pantallas solo ven resultados simples.
// Firestore guarda una copia en el teléfono: las lecturas ya vistas funcionan sin red.
class UserRepository(private val db: FirebaseFirestore = FirebaseFirestore.getInstance()) {

    sealed interface ProfileResult {
        data class Success(val profile: UserProfile) : ProfileResult
        data object NoConnection : ProfileResult
        data object Failed : ProfileResult
    }

    enum class SaveResult { Saved, NoConnection, Failed }

    private fun document(uid: String) = db.collection(USERS).document(uid)

    // Crea el documento de un usuario nuevo con los valores iniciales (decisión E).
    suspend fun createInitialProfile(uid: String, name: String): SaveResult = try {
        document(uid).set(UserProfileMapper.toMap(UserProfile.initial(name))).awaitResult()
        SaveResult.Saved
    } catch (e: CancellationException) {
        throw e // La pantalla se cerró: no es un error que mostrar
    } catch (e: Exception) {
        if (e.isNetworkError()) SaveResult.NoConnection else SaveResult.Failed
    }

    // Lee el perfil. Si el documento no existe todavía (registro que no alcanzó a
    // guardarlo), lo crea: así el login siempre termina con un perfil válido.
    // Sin red y sin copia local da NoConnection: nunca se asume que el usuario es nuevo.
    suspend fun ensureProfile(uid: String, fallbackName: String): ProfileResult = try {
        val snapshot = document(uid).get().awaitResult()
        val data = snapshot.data
        if (snapshot.exists() && data != null) {
            ProfileResult.Success(UserProfileMapper.fromMap(data, fallbackName))
        } else {
            val profile = UserProfile.initial(fallbackName)
            document(uid).set(UserProfileMapper.toMap(profile)).awaitResult()
            ProfileResult.Success(profile)
        }
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        if (e.isNetworkError()) ProfileResult.NoConnection else ProfileResult.Failed
    }

    private fun Exception.isNetworkError(): Boolean =
        this is FirebaseNetworkException ||
            (this is FirebaseFirestoreException && code == FirebaseFirestoreException.Code.UNAVAILABLE)

    private companion object {
        const val USERS = "users"
    }
}
