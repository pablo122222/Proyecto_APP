package me.pablorincon.proyecto_app.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.pablorincon.proyecto_app.data.AccountRepository
import me.pablorincon.proyecto_app.data.AdminAccess
import me.pablorincon.proyecto_app.data.AuthRepository
import me.pablorincon.proyecto_app.data.CurriculumProgressLoader
import me.pablorincon.proyecto_app.data.CurriculumProgressLoader.LoadResult
import me.pablorincon.proyecto_app.data.PreferencesRepository
import me.pablorincon.proyecto_app.data.UserRepository
import me.pablorincon.proyecto_app.domain.Curriculum
import me.pablorincon.proyecto_app.domain.LearningGoal
import me.pablorincon.proyecto_app.domain.LearningLevel
import me.pablorincon.proyecto_app.domain.ProfileSummary
import java.time.LocalDate

// Resultado de una acción en las pantallas internas de Perfil (para mostrar un aviso).
enum class ProfileMessage { Saved, ResetEmailSent, NoConnection, Failed, InvalidName }

data class ProfileUiState(
    val summary: ProfileSummary? = null,
    val loading: Boolean = true,
    val loadFailed: Boolean = false,
    val saving: Boolean = false,
    val message: ProfileMessage? = null,
    val signedOut: Boolean = false // Se cerró la sesión: la app debe volver a Bienvenida
)

// Perfil y sus pantallas internas (Editar perfil, Seguridad, Meta diaria, Nivel y objetivo,
// Estadísticas, Acerca de) comparten este ViewModel: viven en el grafo de la pestaña Perfil.
class ProfileViewModel(
    private val auth: AuthRepository = AuthRepository(),
    private val loader: CurriculumProgressLoader = CurriculumProgressLoader(),
    private val account: AccountRepository = AccountRepository(),
    private val users: UserRepository = UserRepository(),
    private val curriculum: Curriculum = Curriculum.Base
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    // silent: refresca sin mostrar la carga (al volver a la pestaña o tras guardar).
    fun load(silent: Boolean = false) {
        val uid = auth.currentUid() ?: return _uiState.update { it.copy(loading = false, loadFailed = true) }
        if (!silent) _uiState.update { it.copy(loading = it.summary == null, loadFailed = false) }
        viewModelScope.launch {
            when (val result = loader.load(uid, auth.currentName().orEmpty(), AdminAccess.isAdmin(auth.currentEmail()))) {
                is LoadResult.Loaded -> _uiState.update {
                    it.copy(
                        summary = ProfileSummary.build(
                            result.profile, auth.currentEmail().orEmpty(), result.progress, curriculum, LocalDate.now()
                        ),
                        loading = false,
                        loadFailed = false
                    )
                }
                LoadResult.Failed -> _uiState.update { it.copy(loading = false, loadFailed = it.summary == null) }
            }
        }
    }

    fun saveName(name: String, onDone: () -> Unit) {
        val clean = name.trim().replace(Regex("\\s+"), " ")
        if (clean.length !in NAME_LENGTH) return _uiState.update { it.copy(message = ProfileMessage.InvalidName) }
        save(onDone) { uid -> account.updateName(uid, clean) }
    }

    fun saveDailyGoal(goal: Int, onDone: () -> Unit) = save(onDone) { uid -> account.updateDailyGoal(uid, goal) }

    fun saveLevelAndGoal(level: LearningLevel, goal: LearningGoal, onDone: () -> Unit) = save(onDone) { uid ->
        when (users.savePersonalization(uid, level, goal)) {
            UserRepository.SaveResult.Saved -> AccountRepository.Result.Done
            UserRepository.SaveResult.NoConnection -> AccountRepository.Result.NoConnection
            UserRepository.SaveResult.Failed -> AccountRepository.Result.Failed
        }
    }

    // Firebase envía un correo con el enlace para crear una contraseña nueva.
    fun sendPasswordReset() {
        val email = auth.currentEmail() ?: return
        _uiState.update { it.copy(saving = true, message = null) }
        viewModelScope.launch {
            val message = when (auth.sendPasswordReset(email)) {
                AuthRepository.ResetResult.Sent -> ProfileMessage.ResetEmailSent
                AuthRepository.ResetResult.NoConnection -> ProfileMessage.NoConnection
                else -> ProfileMessage.Failed
            }
            _uiState.update { it.copy(saving = false, message = message) }
        }
    }

    private fun save(onDone: () -> Unit, action: suspend (String) -> AccountRepository.Result) {
        val uid = auth.currentUid() ?: return
        _uiState.update { it.copy(saving = true, message = null) }
        viewModelScope.launch {
            val result = action(uid)
            _uiState.update {
                it.copy(
                    saving = false,
                    message = when (result) {
                        AccountRepository.Result.Done -> ProfileMessage.Saved
                        AccountRepository.Result.NoConnection -> ProfileMessage.NoConnection
                        else -> ProfileMessage.Failed
                    }
                )
            }
            if (result == AccountRepository.Result.Done) {
                load(silent = true)
                onDone()
            }
        }
    }

    fun onMessageShown() = _uiState.update { it.copy(message = null) }

    // Cerrar sesión en Firebase es local e inmediato; el teléfono vuelve a las preferencias por defecto.
    fun signOut() {
        auth.signOut()
        PreferencesRepository().resetDevice()
        _uiState.update { it.copy(signedOut = true) }
    }

    // La pantalla avisa que ya navegó, para no repetirlo al girar el teléfono.
    fun onSignedOutHandled() = _uiState.update { it.copy(signedOut = false) }

    companion object {
        val NAME_LENGTH = 2..40 // Nombre visible: ni vacío ni enorme
    }
}
