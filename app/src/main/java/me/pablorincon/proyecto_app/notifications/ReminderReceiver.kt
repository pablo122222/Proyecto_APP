package me.pablorincon.proyecto_app.notifications

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import me.pablorincon.proyecto_app.MainActivity
import me.pablorincon.proyecto_app.R
import me.pablorincon.proyecto_app.data.LocalPreferences

// Muestra la notificación del recordatorio diario. Tocarla abre la app.
class ReminderReceiver : BroadcastReceiver() {

    @SuppressLint("MissingPermission") // canNotify() revisa el permiso justo antes
    override fun onReceive(context: Context, intent: Intent) {
        if (!LocalPreferences(context).load().reminders || !ReminderScheduler.canNotify(context)) return
        val open = PendingIntent.getActivity(
            context,
            0,
            Intent(context, MainActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(context, ReminderScheduler.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(context.getString(R.string.reminder_title))
            .setContentText(context.getString(R.string.reminder_text))
            .setContentIntent(open)
            .setAutoCancel(true)
            .build()
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
    }

    private companion object {
        const val NOTIFICATION_ID = 7
    }
}

// Android borra las alarmas al reiniciar el teléfono: aquí se vuelven a programar.
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            ReminderScheduler.apply(context, LocalPreferences(context).load())
        }
    }
}
