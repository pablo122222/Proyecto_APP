package me.pablorincon.proyecto_app.ui.screens.settings

import android.Manifest
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import me.pablorincon.proyecto_app.R
import me.pablorincon.proyecto_app.domain.TextSize
import me.pablorincon.proyecto_app.ui.components.ChoiceOption
import me.pablorincon.proyecto_app.ui.components.ConfirmSheet
import me.pablorincon.proyecto_app.ui.components.FormError
import me.pablorincon.proyecto_app.ui.components.PasswordTextField
import me.pablorincon.proyecto_app.ui.components.PrimaryButton
import me.pablorincon.proyecto_app.ui.components.SecondaryButton
import me.pablorincon.proyecto_app.ui.components.SettingsBandDivider
import me.pablorincon.proyecto_app.ui.components.SettingsHeader
import me.pablorincon.proyecto_app.ui.components.SettingsNote
import me.pablorincon.proyecto_app.ui.components.SettingsRow
import me.pablorincon.proyecto_app.ui.components.SettingsScaffold
import me.pablorincon.proyecto_app.ui.components.SettingsToggleRow
import me.pablorincon.proyecto_app.ui.theme.AppTextStyles
import me.pablorincon.proyecto_app.ui.theme.Ink
import me.pablorincon.proyecto_app.ui.theme.InterFamily
import me.pablorincon.proyecto_app.ui.theme.Muted
import me.pablorincon.proyecto_app.ui.theme.Paper
import me.pablorincon.proyecto_app.ui.theme.Primary
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

private val SheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
private val SheetScrim = Color(0x731F2A44) // #1F2A44 al 45 % (guía §0)

// Pantalla "Configuración" (sin barra de pestañas). Todo lo que se cambia aquí se aplica
// de verdad: texto, contraste, animaciones, sonido, vibración y el recordatorio diario.
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onLeftAccount: () -> Unit,
    viewModel: SettingsViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LifecycleResumeEffect(viewModel) {
        viewModel.refreshPermission()
        onPauseOrDispose { }
    }
    LaunchedEffect(state.leftAccount) {
        if (state.leftAccount) {
            viewModel.onLeftHandled()
            onLeftAccount()
        }
    }
    // Avisos cortos (toast de la guía §0); el de contraseña incorrecta va dentro de su hoja
    val toastText = when (state.message) {
        SettingsMessage.ProgressReset -> stringResource(R.string.settings_reset_done)
        SettingsMessage.NoConnection -> stringResource(R.string.settings_no_connection)
        SettingsMessage.Failed -> stringResource(R.string.settings_failed)
        SettingsMessage.NotificationsBlocked -> stringResource(R.string.settings_notifications_blocked_toast)
        SettingsMessage.WrongPassword, null -> null
    }
    LaunchedEffect(toastText) {
        if (toastText != null) {
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()
            viewModel.onMessageShown()
        }
    }

    val permission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) viewModel.setReminders(true) else viewModel.onNotificationsDenied()
    }
    val onRemindersChange: (Boolean) -> Unit = { on ->
        if (on && !state.canNotify && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permission.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            viewModel.setReminders(on)
        }
    }

    SettingsScaffold(title = stringResource(R.string.title_settings), onBack = onBack) {
        Text(
            text = stringResource(R.string.settings_intro),
            style = AppTextStyles.SettingsNote,
            color = Muted,
            modifier = Modifier.padding(horizontal = 22.dp, vertical = 4.dp)
        )

        // ---------- Accesibilidad ----------
        SettingsHeader(stringResource(R.string.settings_section_accessibility))
        SettingsRow(
            icon = R.drawable.ic_row_text,
            title = stringResource(R.string.settings_text_size),
            subtitle = stringResource(R.string.settings_text_size_subtitle),
            value = stringResource(textSizeLabel(state.prefs.textSize)),
            onClick = { viewModel.openSheet(SettingsSheet.TextSize) }
        )
        SettingsToggleRow(
            icon = R.drawable.ic_row_contrast,
            title = stringResource(R.string.settings_high_contrast),
            subtitle = stringResource(R.string.settings_high_contrast_subtitle),
            checked = state.prefs.highContrast,
            onCheckedChange = viewModel::setHighContrast
        )
        SettingsToggleRow(
            icon = R.drawable.ic_row_motion,
            title = stringResource(R.string.settings_reduce_motion),
            subtitle = stringResource(R.string.settings_reduce_motion_subtitle),
            checked = state.prefs.reduceMotion,
            onCheckedChange = viewModel::setReduceMotion
        )
        SettingsNote(stringResource(R.string.settings_dark_mode_soon))

        // ---------- Sonido y vibración ----------
        SettingsBandDivider()
        SettingsHeader(stringResource(R.string.settings_section_sound))
        SettingsToggleRow(
            icon = R.drawable.ic_row_volume,
            title = stringResource(R.string.settings_sound_cues),
            subtitle = stringResource(R.string.settings_sound_cues_subtitle),
            checked = state.prefs.soundCues,
            onCheckedChange = viewModel::setSoundCues
        )
        SettingsToggleRow(
            icon = R.drawable.ic_row_vibrate,
            title = stringResource(R.string.settings_vibration),
            subtitle = stringResource(R.string.settings_vibration_subtitle),
            checked = state.prefs.vibration,
            onCheckedChange = viewModel::setVibration
        )

        // ---------- Notificaciones ----------
        SettingsBandDivider()
        SettingsHeader(stringResource(R.string.settings_section_notifications))
        SettingsToggleRow(
            icon = R.drawable.ic_row_bell,
            title = stringResource(R.string.settings_reminders),
            subtitle = stringResource(R.string.settings_reminders_subtitle),
            checked = state.remindersOn,
            onCheckedChange = onRemindersChange
        )
        SettingsRow(
            icon = R.drawable.ic_row_clock,
            title = stringResource(R.string.settings_reminder_time),
            value = formatTime(state.prefs.reminderHour, state.prefs.reminderMinute),
            enabled = state.remindersOn,
            onClick = { viewModel.openSheet(SettingsSheet.ReminderTime) }
        )
        if (state.prefs.reminders && !state.canNotify) {
            SettingsNote(stringResource(R.string.settings_notifications_blocked))
            SettingsRow(
                icon = R.drawable.ic_row_settings,
                title = stringResource(R.string.settings_open_system_notifications),
                onClick = {
                    context.startActivity(
                        Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                            .putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                    )
                }
            )
        }

        // ---------- Datos y privacidad ----------
        SettingsBandDivider()
        SettingsHeader(stringResource(R.string.settings_section_data))
        SettingsNote(stringResource(R.string.settings_data_note))
        SettingsRow(
            icon = R.drawable.ic_row_reset,
            title = stringResource(R.string.settings_reset_progress),
            subtitle = stringResource(R.string.settings_reset_progress_subtitle),
            onClick = { viewModel.openSheet(SettingsSheet.ResetProgress) }
        )
        SettingsRow(
            icon = R.drawable.ic_row_trash,
            title = stringResource(R.string.settings_delete_account),
            subtitle = stringResource(R.string.settings_delete_account_subtitle),
            danger = true,
            onClick = { viewModel.openSheet(SettingsSheet.DeleteAccount) }
        )
        SettingsRow(
            icon = R.drawable.ic_row_logout,
            title = stringResource(R.string.profile_logout_title),
            danger = true,
            onClick = { viewModel.openSheet(SettingsSheet.SignOut) }
        )
    }

    when (state.sheet) {
        SettingsSheet.TextSize -> TextSizeSheet(state.prefs.textSize, viewModel::setTextSize, viewModel::closeSheet)
        SettingsSheet.ReminderTime -> ReminderTimeDialog(
            hour = state.prefs.reminderHour,
            minute = state.prefs.reminderMinute,
            onConfirm = viewModel::setReminderTime,
            onDismiss = viewModel::closeSheet
        )
        SettingsSheet.ResetProgress -> ConfirmSheet(
            title = stringResource(R.string.settings_reset_confirm_title),
            message = stringResource(R.string.settings_reset_confirm_message),
            confirmLabel = stringResource(
                if (state.busy) R.string.settings_working else R.string.settings_reset_confirm_yes
            ),
            cancelLabel = stringResource(R.string.settings_cancel),
            onConfirm = { if (!state.busy) viewModel.resetProgress() },
            onDismiss = viewModel::closeSheet
        )
        SettingsSheet.DeleteAccount -> DeleteAccountSheet(
            busy = state.busy,
            wrongPassword = state.message == SettingsMessage.WrongPassword,
            onConfirm = viewModel::deleteAccount,
            onDismiss = viewModel::closeSheet
        )
        SettingsSheet.SignOut -> ConfirmSheet(
            title = stringResource(R.string.logout_confirm_title),
            message = stringResource(R.string.logout_confirm_message),
            confirmLabel = stringResource(R.string.logout_confirm_yes),
            cancelLabel = stringResource(R.string.logout_confirm_cancel),
            onConfirm = viewModel::signOut,
            onDismiss = viewModel::closeSheet
        )
        null -> Unit
    }
}

internal fun textSizeLabel(size: TextSize): Int = when (size) {
    TextSize.Standard -> R.string.settings_text_standard
    TextSize.Large -> R.string.settings_text_large
    TextSize.ExtraLarge -> R.string.settings_text_extra_large
}

// Hora con el formato del teléfono (por ejemplo, "7:00 p. m.").
internal fun formatTime(hour: Int, minute: Int): String =
    LocalTime.of(hour, minute).format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

// Hoja para elegir el tamaño del texto, con una vista previa.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TextSizeSheet(current: TextSize, onSelect: (TextSize) -> Unit, onDismiss: () -> Unit) {
    ModalBottomSheet(onDismissRequest = onDismiss, containerColor = Paper, scrimColor = SheetScrim, shape = SheetShape) {
        Column(Modifier.padding(start = 22.dp, end = 22.dp, top = 4.dp, bottom = 30.dp)) {
            SheetTitle(stringResource(R.string.settings_text_size))
            Text(text = stringResource(R.string.settings_text_size_subtitle), style = AppTextStyles.SmallBody, color = Muted)
            Spacer(Modifier.height(16.dp))
            Column(Modifier.selectableGroup(), verticalArrangement = Arrangement.spacedBy(9.dp)) {
                TextSize.entries.forEach { size ->
                    ChoiceOption(
                        icon = when (size) {
                            TextSize.Standard -> "A"
                            TextSize.Large -> "A+"
                            TextSize.ExtraLarge -> "A++"
                        },
                        title = stringResource(textSizeLabel(size)),
                        subtitle = stringResource(
                            when (size) {
                                TextSize.Standard -> R.string.settings_text_standard_hint
                                TextSize.Large -> R.string.settings_text_large_hint
                                TextSize.ExtraLarge -> R.string.settings_text_extra_large_hint
                            }
                        ),
                        selected = size == current,
                        onClick = { onSelect(size) }
                    )
                }
            }
        }
    }
}

// Reloj para elegir la hora del recordatorio.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReminderTimeDialog(hour: Int, minute: Int, onConfirm: (Int, Int) -> Unit, onDismiss: () -> Unit) {
    val time = rememberTimePickerState(initialHour = hour, initialMinute = minute)
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Paper,
        title = { SheetTitle(stringResource(R.string.settings_reminder_time)) },
        text = {
            TimePicker(
                state = time,
                colors = TimePickerDefaults.colors(
                    selectorColor = Primary,
                    timeSelectorSelectedContainerColor = Primary.copy(alpha = 0.15f),
                    timeSelectorSelectedContentColor = Primary,
                    periodSelectorSelectedContainerColor = Primary.copy(alpha = 0.15f),
                    periodSelectorSelectedContentColor = Primary
                )
            )
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(time.hour, time.minute) }) {
                Text(stringResource(R.string.settings_save), color = Primary)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.settings_cancel), color = Muted) }
        }
    )
}

// Eliminar la cuenta pide la contraseña: Firebase exige confirmar la identidad.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DeleteAccountSheet(
    busy: Boolean,
    wrongPassword: Boolean,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var password by rememberSaveable { mutableStateOf("") }
    ModalBottomSheet(onDismissRequest = onDismiss, containerColor = Paper, scrimColor = SheetScrim, shape = SheetShape) {
        Column(Modifier.padding(start = 22.dp, end = 22.dp, top = 4.dp, bottom = 30.dp)) {
            SheetTitle(stringResource(R.string.settings_delete_confirm_title))
            Text(text = stringResource(R.string.settings_delete_confirm_message), style = AppTextStyles.SmallBody, color = Muted)
            Spacer(Modifier.height(14.dp))
            PasswordTextField(
                label = stringResource(R.string.settings_delete_password_label),
                value = password,
                onValueChange = { password = it },
                placeholder = stringResource(R.string.settings_delete_password_placeholder),
                modifier = Modifier.fillMaxWidth()
            )
            FormError(message = if (wrongPassword) stringResource(R.string.settings_wrong_password) else null)
            Spacer(Modifier.height(10.dp))
            PrimaryButton(
                text = stringResource(if (busy) R.string.settings_working else R.string.settings_delete_confirm_yes),
                enabled = password.isNotBlank() && !busy,
                onClick = { onConfirm(password) }
            )
            Spacer(Modifier.height(10.dp))
            SecondaryButton(text = stringResource(R.string.settings_cancel), onClick = onDismiss)
        }
    }
}

@Composable
private fun SheetTitle(text: String) {
    Text(
        text = text,
        style = TextStyle(fontFamily = InterFamily, fontWeight = FontWeight.Bold, fontSize = 18.sp),
        color = Ink
    )
    Spacer(Modifier.height(6.dp))
}
