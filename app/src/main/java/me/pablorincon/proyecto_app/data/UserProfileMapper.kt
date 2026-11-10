package me.pablorincon.proyecto_app.data

import me.pablorincon.proyecto_app.domain.LearningGoal
import me.pablorincon.proyecto_app.domain.LearningLevel
import me.pablorincon.proyecto_app.domain.TextSize
import me.pablorincon.proyecto_app.domain.UserPreferences
import me.pablorincon.proyecto_app.domain.UserProfile

// Convierte el perfil al mapa que se guarda en Firestore y de vuelta.
// Código puro (sin Firebase) para poder probarlo sin red.
// Al leer, un campo ausente o con tipo equivocado toma su valor por defecto:
// un documento incompleto nunca debe hacer fallar la app.
internal object UserProfileMapper {

    const val NAME = "name"
    const val LEVEL = "level"
    const val GOAL = "goal"
    const val DAILY_GOAL = "dailyGoal"
    const val XP = "xp"
    const val GEMS = "gems"
    const val STREAK = "streak"
    const val LAST_ACTIVE_DATE = "lastActiveDate"
    const val ACTIVITIES_ON_LAST_DAY = "activitiesOnLastDay"
    const val BEST_STREAK = "bestStreak"
    const val BUGS_FOUND = "bugsFound"
    const val RECOVERED_EXERCISES = "recoveredExercises"
    const val EXPLAINED_WHY = "explainedWhy"
    const val ACHIEVEMENT_TIERS = "achievementTiers"
    const val EQUIPPED_TITLE = "equippedTitleId"
    const val PREFERENCES = "preferences"

    private const val TEXT_SIZE = "textSize"
    private const val HIGH_CONTRAST = "highContrast"
    private const val REDUCE_MOTION = "reduceMotion"
    private const val SOUND_CUES = "soundCues"
    private const val VIBRATION = "vibration"
    private const val REMINDERS = "reminders"
    private const val REMINDER_HOUR = "reminderHour"
    private const val REMINDER_MINUTE = "reminderMinute"

    fun toMap(profile: UserProfile): Map<String, Any?> = mapOf(
        NAME to profile.name,
        LEVEL to profile.level?.id,
        GOAL to profile.goal?.id,
        DAILY_GOAL to profile.dailyGoal,
        XP to profile.xp,
        GEMS to profile.gems,
        STREAK to profile.streak,
        LAST_ACTIVE_DATE to profile.lastActiveDate,
        ACTIVITIES_ON_LAST_DAY to profile.activitiesOnLastDay,
        BEST_STREAK to profile.bestStreak,
        BUGS_FOUND to profile.bugsFound,
        RECOVERED_EXERCISES to profile.recoveredExercises,
        EXPLAINED_WHY to profile.explainedWhy.toList(),
        ACHIEVEMENT_TIERS to profile.achievementTiers,
        EQUIPPED_TITLE to profile.equippedTitleId,
        PREFERENCES to preferencesToMap(profile.preferences)
    )

    fun preferencesToMap(prefs: UserPreferences): Map<String, Any> = mapOf(
        TEXT_SIZE to prefs.textSize.id,
        HIGH_CONTRAST to prefs.highContrast,
        REDUCE_MOTION to prefs.reduceMotion,
        SOUND_CUES to prefs.soundCues,
        VIBRATION to prefs.vibration,
        REMINDERS to prefs.reminders,
        REMINDER_HOUR to prefs.reminderHour,
        REMINDER_MINUTE to prefs.reminderMinute
    )

    fun fromMap(data: Map<String, Any?>, fallbackName: String = ""): UserProfile {
        val defaults = UserProfile.initial(fallbackName)
        return UserProfile(
            name = (data[NAME] as? String) ?: fallbackName,
            level = LearningLevel.fromId(data[LEVEL] as? String),
            goal = LearningGoal.fromId(data[GOAL] as? String),
            dailyGoal = data.int(DAILY_GOAL) ?: defaults.dailyGoal,
            xp = data.int(XP) ?: defaults.xp,
            gems = data.int(GEMS) ?: defaults.gems,
            streak = data.int(STREAK) ?: defaults.streak,
            lastActiveDate = data[LAST_ACTIVE_DATE] as? String,
            activitiesOnLastDay = data.int(ACTIVITIES_ON_LAST_DAY) ?: defaults.activitiesOnLastDay,
            bestStreak = data.int(BEST_STREAK) ?: defaults.bestStreak,
            bugsFound = data.int(BUGS_FOUND) ?: defaults.bugsFound,
            recoveredExercises = data.int(RECOVERED_EXERCISES) ?: defaults.recoveredExercises,
            explainedWhy = (data[EXPLAINED_WHY] as? List<*>).orEmpty().filterIsInstance<String>().toSet(),
            achievementTiers = (data[ACHIEVEMENT_TIERS] as? Map<*, *>).orEmpty()
                .mapNotNull { (key, value) -> (key as? String)?.let { k -> (value as? Number)?.let { k to it.toInt() } } }
                .toMap(),
            equippedTitleId = (data[EQUIPPED_TITLE] as? String) ?: defaults.equippedTitleId,
            preferences = preferencesFromMap(data[PREFERENCES] as? Map<*, *>)
        )
    }

    fun preferencesFromMap(prefs: Map<*, *>?): UserPreferences {
        val p = prefs.orEmpty()
        val d = UserPreferences()
        return UserPreferences(
            textSize = TextSize.fromId(p[TEXT_SIZE] as? String),
            highContrast = p[HIGH_CONTRAST] as? Boolean ?: d.highContrast,
            reduceMotion = p[REDUCE_MOTION] as? Boolean ?: d.reduceMotion,
            soundCues = p[SOUND_CUES] as? Boolean ?: d.soundCues,
            vibration = p[VIBRATION] as? Boolean ?: d.vibration,
            reminders = p[REMINDERS] as? Boolean ?: d.reminders,
            reminderHour = (p[REMINDER_HOUR] as? Number)?.toInt()?.takeIf { it in 0..23 } ?: d.reminderHour,
            reminderMinute = (p[REMINDER_MINUTE] as? Number)?.toInt()?.takeIf { it in 0..59 } ?: d.reminderMinute
        )
    }

    // Firestore devuelve los números como Long: se aceptan Int, Long o Double.
    private fun Map<String, Any?>.int(key: String): Int? = (this[key] as? Number)?.toInt()
}
