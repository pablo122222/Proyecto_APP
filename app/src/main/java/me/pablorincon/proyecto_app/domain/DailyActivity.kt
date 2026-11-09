package me.pablorincon.proyecto_app.domain

import java.time.LocalDate

// Racha y actividad del día (reglas aprobadas, guía §10):
// - Un día cuenta cuando se completa al menos una actividad (lección, jefe y, más adelante,
//   actividades de Retos). Solo cuentan las que dan XP por primera vez.
// - Días seguidos suman; si se salta un día, la racha vuelve a empezar en 1.
// - "Hoy" es el día del teléfono (zona horaria del dispositivo).
// La fecha se guarda como texto "yyyy-MM-dd" (ISO), igual que lastActiveDate del perfil.
data class DailyActivity(
    val streak: Int = 0,
    val lastActiveDate: String? = null,
    val activitiesOnLastDay: Int = 0
) {
    private val lastDay: LocalDate? = lastActiveDate?.let { runCatching { LocalDate.parse(it) }.getOrNull() }

    // Racha que se muestra hoy: sigue viva si hubo actividad hoy o ayer; si no, se perdió.
    fun streakOn(today: LocalDate): Int =
        if (lastDay == today || lastDay == today.minusDays(1)) streak else 0

    // Actividades completadas hoy (se reinicia sola al cambiar de día).
    fun countOn(today: LocalDate): Int = if (lastDay == today) activitiesOnLastDay else 0

    // Registra una actividad completada hoy.
    fun record(today: LocalDate): DailyActivity = when (lastDay) {
        today -> copy(activitiesOnLastDay = activitiesOnLastDay + 1, streak = maxOf(streak, 1))
        today.minusDays(1) -> DailyActivity(streak + 1, today.toString(), 1)
        else -> DailyActivity(1, today.toString(), 1)
    }
}

// Meta diaria: cuántas actividades quiere completar al día (Perfil la cambia; por defecto 2).
object DailyGoal {
    val options = listOf(1, 2, 3, 5)

    fun isMet(done: Int, goal: Int): Boolean = done >= goal

    // Progreso para la barra, de 0 a 1.
    fun fraction(done: Int, goal: Int): Float = if (goal <= 0) 1f else (done.toFloat() / goal).coerceIn(0f, 1f)
}
