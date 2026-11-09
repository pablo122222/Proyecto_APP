package me.pablorincon.proyecto_app.notifications

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import me.pablorincon.proyecto_app.R
import me.pablorincon.proyecto_app.domain.ReminderTime
import me.pablorincon.proyecto_app.domain.UserPreferences
import java.time.LocalDateTime
import java.time.ZoneId

// Recordatorio diario para practicar (Configuración → Notificaciones).
// Usa AlarmManager con una alarma diaria inexacta: no necesita librerías nuevas ni el permiso
// de alarmas exactas, y Android la agrupa con otras para cuidar la batería.
object ReminderScheduler {

    const val CHANNEL_ID = "recordatorios"
    private const val REQUEST_CODE = 41

    fun canNotify(context: Context): Boolean =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) ==
            PackageManager.PERMISSION_GRANTED

    // Programa o cancela la alarma según las preferencias.
    fun apply(context: Context, prefs: UserPreferences) {
        val alarms = context.getSystemService(AlarmManager::class.java) ?: return
        val intent = pendingIntent(context)
        alarms.cancel(intent)
        if (!prefs.reminders || !canNotify(context)) return
        val next = ReminderTime.next(LocalDateTime.now(), prefs.reminderHour, prefs.reminderMinute)
        val millis = next.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        alarms.setInexactRepeating(AlarmManager.RTC_WAKEUP, millis, AlarmManager.INTERVAL_DAY, intent)
    }

    fun createChannel(context: Context) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            context.getString(R.string.reminder_channel_name),
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply { description = context.getString(R.string.reminder_channel_description) }
        context.getSystemService(NotificationManager::class.java)?.createNotificationChannel(channel)
    }

    private fun pendingIntent(context: Context): PendingIntent = PendingIntent.getBroadcast(
        context,
        REQUEST_CODE,
        Intent(context, ReminderReceiver::class.java),
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
}
