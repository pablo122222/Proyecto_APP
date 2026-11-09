package me.pablorincon.proyecto_app.ui.navigation

import me.pablorincon.proyecto_app.domain.Boss
import me.pablorincon.proyecto_app.domain.PathStep

// Todas las rutas de la app en un solo lugar (rutas de texto de Navigation Compose).
object AppRoutes {

    // ---------- Cuenta ----------
    const val WELCOME = "bienvenida"
    const val REGISTER = "registro"
    const val LOGIN = "login"
    const val FORGOT_PASSWORD = "recuperar_contrasena"

    // ---------- Personalización ----------
    // Grafo que agrupa los 3 pasos: comparten el ViewModel con lo que se va eligiendo.
    const val PERSONALIZATION = "personalizacion"
    const val LEVEL = "nivel"
    const val GOAL = "objetivo"
    const val PATH_READY = "ruta_lista"

    // ---------- Grafos de cada pestaña ----------
    // Cada pestaña es un grafo anidado para que conserve su historial al cambiar de pestaña.
    const val TAB_HOME = "pestana_inicio"
    const val TAB_LEARN = "pestana_aprender"
    const val TAB_CHALLENGES = "pestana_retos"
    const val TAB_TUTOR = "pestana_tutor"
    const val TAB_PROFILE = "pestana_perfil"

    // ---------- Pantallas principales ----------
    const val HOME = "inicio"
    const val LEARNING_PATH = "ruta"
    const val LESSON_ARG = "leccionId"
    const val LESSON = "leccion/{$LESSON_ARG}"
    const val BOSS_ARG = "jefeId"
    const val BOSS = "jefe/{$BOSS_ARG}"
    const val GLITCH_CITY = "glitch_city"
    const val ACTIVITY_ARG = "actividadId"
    const val ACTIVITY = "actividad/{$ACTIVITY_ARG}"
    const val TUTOR = "tutor"
    const val PROFILE = "perfil"
    const val ACHIEVEMENTS = "logros"
    const val SETTINGS = "configuracion"

    // Pantallas internas de Perfil (sin barra de pestañas)
    const val PROFILE_EDIT = "perfil_editar"
    const val PROFILE_SECURITY = "perfil_seguridad"
    const val PROFILE_DAILY_GOAL = "perfil_meta_diaria"
    const val PROFILE_LEVEL_GOAL = "perfil_nivel_objetivo"
    const val PROFILE_STATS = "perfil_estadisticas"
    const val PROFILE_ABOUT = "perfil_acerca"

    // Los ids van dentro de la ruta: solo se aceptan caracteres seguros en una URL.
    private val SafeId = Regex("[a-z0-9_-]+")

    fun lesson(lessonId: String): String {
        require(SafeId.matches(lessonId)) { "Id de lección no válido: $lessonId" }
        return "leccion/$lessonId"
    }

    fun boss(bossId: String): String {
        require(SafeId.matches(bossId)) { "Id de jefe no válido: $bossId" }
        return "jefe/$bossId"
    }

    // Pantalla de un paso de la ruta: lección o jefe.
    fun step(step: PathStep): String = if (step is Boss) boss(step.id) else lesson(step.id)

    fun activity(activityId: String): String {
        require(SafeId.matches(activityId)) { "Id de actividad no válido: $activityId" }
        return "actividad/$activityId"
    }

    // Al abrir la app (decisión A): sin sesión, Bienvenida; con sesión, Inicio, salvo
    // que falte nivel y objetivo: entonces se retoma Personalización.
    fun startDestination(isLoggedIn: Boolean, hasPersonalization: Boolean = false): String = when {
        !isLoggedIn -> WELCOME
        hasPersonalization -> TAB_HOME
        else -> PERSONALIZATION // El grafo empieza en Nivel
    }

    // Destino después de cada inicio de sesión (decisión A).
    fun afterLogin(hasPersonalization: Boolean): String = if (hasPersonalization) HOME else LEVEL
}
