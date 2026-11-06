package me.pablorincon.proyecto_app.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import me.pablorincon.proyecto_app.R

// Las 5 pestañas de la barra inferior, en el orden de la guía de diseño.
// graphRoute: grafo anidado de la pestaña · rootRoute: su pantalla principal.
enum class BottomTab(
    val graphRoute: String,
    val rootRoute: String,
    @param:StringRes val label: Int,
    @param:DrawableRes val icon: Int
) {
    Home(AppRoutes.TAB_HOME, AppRoutes.HOME, R.string.tab_home, R.drawable.ic_tab_home),
    Learn(AppRoutes.TAB_LEARN, AppRoutes.LEARNING_PATH, R.string.tab_learn, R.drawable.ic_tab_learn),
    Challenges(AppRoutes.TAB_CHALLENGES, AppRoutes.GLITCH_CITY, R.string.tab_challenges, R.drawable.ic_tab_challenges),
    Tutor(AppRoutes.TAB_TUTOR, AppRoutes.TUTOR, R.string.tab_tutor, R.drawable.ic_tab_tutor),
    Profile(AppRoutes.TAB_PROFILE, AppRoutes.PROFILE, R.string.tab_profile, R.drawable.ic_tab_profile);

    companion object {
        // Pestaña que se ve activa en cada pantalla, o null si la pantalla no lleva barra
        // (cuenta, personalización, Lección y Configuración).
        // Actividad y Logros mantienen activas Retos y Perfil.
        fun forRoute(route: String?): BottomTab? = when (route) {
            AppRoutes.HOME -> Home
            AppRoutes.LEARNING_PATH -> Learn
            AppRoutes.GLITCH_CITY, AppRoutes.ACTIVITY -> Challenges
            AppRoutes.TUTOR -> Tutor
            AppRoutes.PROFILE, AppRoutes.ACHIEVEMENTS -> Profile
            else -> null
        }
    }
}
