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
    const val EQUIPPED_TITLE = "equippedTitleId"
    const val PREFERENCES = "preferences"

    private const val TEXT_SIZE = "textSize"
    private const val HIGH_CONTRAST = "highContrast"
    private const val REDUCE_MOTION = "reduceMotion"
    private const val CAPTIONS = "captions"
    private const val SOUND_CUES = "soundCues"
    private const val REMINDERS = "reminders"

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
        EQUIPPED_TITLE to profile.equippedTitleId,
        PREFERENCES to mapOf(
            TEXT_SIZE to profile.preferences.textSize.id,
            HIGH_CONTRAST to profile.preferences.highContrast,
            REDUCE_MOTION to profile.preferences.reduceMotion,
            CAPTIONS to profile.preferences.captions,
            SOUND_CUES to profile.preferences.soundCues,
            REMINDERS to profile.preferences.reminders
        )
    )

    fun fromMap(data: Map<String, Any?>, fallbackName: String = ""): UserProfile {
        val defaults = UserProfile.initial(fallbackName)
        val prefs = (data[PREFERENCES] as? Map<*, *>).orEmpty()
        val defaultPrefs = UserPreferences()
        return UserProfile(
            name = (data[NAME] as? String) ?: fallbackName,
            level = LearningLevel.fromId(data[LEVEL] as? String),
            goal = LearningGoal.fromId(data[GOAL] as? String),
            dailyGoal = (data[DAILY_GOAL] as? Number)?.toInt() ?: defaults.dailyGoal,
            xp = (data[XP] as? Number)?.toInt() ?: defaults.xp,
            gems = (data[GEMS] as? Number)?.toInt() ?: defaults.gems,
            streak = (data[STREAK] as? Number)?.toInt() ?: defaults.streak,
            lastActiveDate = data[LAST_ACTIVE_DATE] as? String,
            activitiesOnLastDay = (data[ACTIVITIES_ON_LAST_DAY] as? Number)?.toInt() ?: defaults.activitiesOnLastDay,
            equippedTitleId = (data[EQUIPPED_TITLE] as? String) ?: defaults.equippedTitleId,
            preferences = UserPreferences(
                textSize = TextSize.fromId(prefs[TEXT_SIZE] as? String),
                highContrast = prefs[HIGH_CONTRAST] as? Boolean ?: defaultPrefs.highContrast,
                reduceMotion = prefs[REDUCE_MOTION] as? Boolean ?: defaultPrefs.reduceMotion,
                captions = prefs[CAPTIONS] as? Boolean ?: defaultPrefs.captions,
                soundCues = prefs[SOUND_CUES] as? Boolean ?: defaultPrefs.soundCues,
                reminders = prefs[REMINDERS] as? Boolean ?: defaultPrefs.reminders
            )
        )
    }
}
