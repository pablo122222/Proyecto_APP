package me.pablorincon.proyecto_app.domain

// Nivel elegido en Personalización (paso 1). El id es lo que se guarda en Firestore:
// no cambia aunque cambie el texto que ve el usuario.
enum class LearningLevel(val id: String) {
    Beginner("principiante"),
    Basic("basico"),
    Intermediate("intermedio");

    companion object {
        fun fromId(id: String?): LearningLevel? = entries.firstOrNull { it.id == id }
    }
}

// Objetivo elegido en Personalización (paso 2).
enum class LearningGoal(val id: String) {
    UnderstandProgramming("entender"),
    PassCourse("aprobar"),
    SolveProblems("resolver"),
    BuildProjects("crear");

    companion object {
        fun fromId(id: String?): LearningGoal? = entries.firstOrNull { it.id == id }
    }
}

// Tamaño del texto de Configuración (se aplica de verdad en la Fase 18).
enum class TextSize(val id: String) {
    Standard("estandar"),
    Large("grande"),
    ExtraLarge("muy_grande");

    companion object {
        fun fromId(id: String?): TextSize = entries.firstOrNull { it.id == id } ?: Standard
    }
}

// Preferencias de Configuración y accesibilidad. Subtítulos y recordatorios
// vienen activados por defecto, como en la guía de diseño.
data class UserPreferences(
    val textSize: TextSize = TextSize.Standard,
    val highContrast: Boolean = false,
    val reduceMotion: Boolean = false,
    val captions: Boolean = true,
    val soundCues: Boolean = false,
    val reminders: Boolean = true
)

// Perfil del usuario: todo lo que vive en el documento users/{uid}.
// Un usuario nuevo empieza desde cero (decisión E): nada de datos de ejemplo.
data class UserProfile(
    val name: String,
    val level: LearningLevel? = null,
    val goal: LearningGoal? = null,
    val dailyGoal: Int = DEFAULT_DAILY_GOAL,
    val xp: Int = 0,
    val gems: Int = 0,
    val streak: Int = 0,
    val lastActiveDate: String? = null,       // "yyyy-MM-dd"; la racha lo usa en la Fase 12
    val equippedTitleId: String = INITIAL_TITLE_ID,
    val preferences: UserPreferences = UserPreferences()
) {
    // Sin nivel y objetivo guardados, después del login toca Personalización (decisión A).
    val hasPersonalization: Boolean get() = level != null && goal != null

    companion object {
        const val DEFAULT_DAILY_GOAL = 2        // "Ritmo recomendado"
        const val INITIAL_TITLE_ID = "t0"       // "Aprendiz de Glitch"

        fun initial(name: String) = UserProfile(name = name)
    }
}
