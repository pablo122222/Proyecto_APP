package me.pablorincon.proyecto_app.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.pablorincon.proyecto_app.CodeMentorApp
import me.pablorincon.proyecto_app.data.AccountRepository
import me.pablorincon.proyecto_app.data.AuthRepository
import me.pablorincon.proyecto_app.data.PreferencesRepository
import me.pablorincon.proyecto_app.domain.TextSize
import me.pablorincon.proyecto_app.domain.UserPreferences
import me.pablorincon.proyecto_app.notifications.ReminderScheduler

// Mensajes cortos que la pantalla muestra tras una acción.
enum class SettingsMessage {
    ProgressReset, NoConnection, WrongPassword, Failed, NotificationsBlocked
}

// Hoja abierta en la pantalla (solo una a la vez).
enum class SettingsSheet { TextSize, ReminderTime, ResetProgress, DeleteAccount, SignOut }

data class SettingsUiState(
    val prefs: UserPreferences,
    val canNotify: Boolean,
    val sheet: SettingsSheet? = null,
    val busy: Boolean = false,             // Restableciendo o eliminando la cuenta
    val message: SettingsMessage? = null,
    val leftAccount: Boolean = false       // Cerró sesión o eliminó la cuenta: volver a Bienvenida
) {
    // El recordatorio solo cuenta como activo si Android deja mostrar notificaciones.
    val remindersOn: Boolean get() = prefs.reminders && canNotify
}

// Configuración y accesibilidad. Cada cambio se aplica al instante en toda la app,
// se guarda en el teléfono y se sube a la cuenta (PreferencesRepository).
class SettingsViewModel(
    private val auth: AuthRepository = AuthRepository(),
    private val preferences: PreferencesRepository = PreferencesRepository(),
    private val account: AccountRepository = AccountRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        SettingsUiState(prefs = preferences.current(), canNotify = ReminderScheduler.canNotify(CodeMentorApp.context))
    )
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    private fun change(transform: (UserPreferences) -> UserPreferences) {
        val prefs = transform(_uiState.value.prefs)
        preferences.save(auth.currentUid(), prefs)
        _uiState.update { it.copy(prefs = prefs) }
    }

    fun setTextSize(size: TextSize) {
        change { it.copy(textSize = size) }
        closeSheet()
    }
    fun setHighContrast(on: Boolean) = change { it.copy(highContrast = on) }
    fun setReduceMotion(on: Boolean) = change { it.copy(reduceMotion = on) }
    fun setSoundCues(on: Boolean) = change { it.copy(soundCues = on) }
    fun setVibration(on: Boolean) = change { it.copy(vibration = on) }

    // La pantalla pide el permiso de notificaciones antes de llamar con on = true.
    fun setReminders(on: Boolean) {
        refreshPermission()
        change { it.copy(reminders = on) }
    }

    fun setReminderTime(hour: Int, minute: Int) {
        change { it.copy(reminderHour = hour, reminderMinute = minute) }
        closeSheet()
    }

    fun onNotificationsDenied() = _uiState.update {
        it.copy(canNotify = false, message = SettingsMessage.NotificationsBlocked)
    }

    // Al volver de los ajustes del teléfono el permiso pudo cambiar: se reprograma la alarma.
    fun refreshPermission() {
        val canNotify = ReminderScheduler.canNotify(CodeMentorApp.context)
        if (canNotify != _uiState.value.canNotify) {
            _uiState.update { it.copy(canNotify = canNotify) }
            preferences.applyOnDevice(_uiState.value.prefs)
        }
    }

    fun openSheet(sheet: SettingsSheet) = _uiState.update { it.copy(sheet = sheet, message = null) }
    fun closeSheet() = _uiState.update { it.copy(sheet = null) }
    fun onMessageShown() = _uiState.update { it.copy(message = null) }

    fun resetProgress() {
        val uid = auth.currentUid() ?: return
        _uiState.update { it.copy(busy = true) }
        viewModelScope.launch {
            val result = account.resetProgress(uid)
            _uiState.update {
                it.copy(
                    busy = false,
                    sheet = null,
                    message = when (result) {
                        AccountRepository.Result.Done -> SettingsMessage.ProgressReset
                        AccountRepository.Result.NoConnection -> SettingsMessage.NoConnection
                        else -> SettingsMessage.Failed
                    }
                )
            }
        }
    }

    fun deleteAccount(password: String) {
        if (password.isBlank()) return
        _uiState.update { it.copy(busy = true, message = null) }
        viewModelScope.launch {
            when (account.deleteAccount(password)) {
                AccountRepository.Result.Done -> leave()
                AccountRepository.Result.WrongPassword -> fail(SettingsMessage.WrongPassword)
                AccountRepository.Result.NoConnection -> fail(SettingsMessage.NoConnection)
                AccountRepository.Result.Failed -> fail(SettingsMessage.Failed)
            }
        }
    }

    fun signOut() {
        auth.signOut()
        leave()
    }

    // El teléfono vuelve a los valores por defecto (otra persona podría iniciar sesión).
    private fun leave() {
        preferences.resetDevice()
        _uiState.update { it.copy(busy = false, sheet = null, leftAccount = true) }
    }

    private fun fail(message: SettingsMessage) = _uiState.update { it.copy(busy = false, message = message) }

    fun onLeftHandled() = _uiState.update { it.copy(leftAccount = false) }
}
