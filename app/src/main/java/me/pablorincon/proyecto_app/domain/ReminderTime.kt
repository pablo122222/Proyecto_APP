package me.pablorincon.proyecto_app.domain

import java.time.LocalDateTime
import java.time.LocalTime

// Cuándo sonará el próximo recordatorio diario: hoy a esa hora si aún no pasó; si no, mañana.
object ReminderTime {
    fun next(now: LocalDateTime, hour: Int, minute: Int): LocalDateTime {
        val today = now.toLocalDate().atTime(LocalTime.of(hour, minute))
        return if (today.isAfter(now)) today else today.plusDays(1)
    }
}
