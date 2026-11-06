package me.pablorincon.proyecto_app.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import me.pablorincon.proyecto_app.R
import me.pablorincon.proyecto_app.ui.components.AppBottomBar
import me.pablorincon.proyecto_app.ui.screens.forgotpassword.ForgotPasswordScreen
import me.pablorincon.proyecto_app.ui.screens.login.LoginScreen
import me.pablorincon.proyecto_app.ui.screens.placeholder.PlaceholderScreen
import me.pablorincon.proyecto_app.ui.screens.profile.ProfileScreen
import me.pablorincon.proyecto_app.ui.screens.register.RegisterScreen
import me.pablorincon.proyecto_app.ui.screens.welcome.WelcomeScreen
import me.pablorincon.proyecto_app.ui.theme.AppBackground

// Duración del fundido entre pantallas (la guía no define transiciones).
// En la Fase 18, "Reducir animaciones" podrá quitarlo.
private const val FADE_MILLIS = 200

// Grafo de navegación de toda la app y barra de pestañas inferior.
// La barra se dibuja encima del contenido (es semitransparente); cada pantalla con
// pestaña deja libres al final los 98 dp de AppDimens.ScreenBottom.
@Composable
fun AppNavHost(startDestination: String, modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val activeTab = BottomTab.forRoute(backStackEntry?.destination?.route)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.fillMaxSize(),
            enterTransition = { fadeIn(tween(FADE_MILLIS)) },
            exitTransition = { fadeOut(tween(FADE_MILLIS)) }
        ) {
            accountDestinations(navController)
            personalizationDestinations(navController)
            tabGraphs(navController)
        }

        if (activeTab != null) {
            AppBottomBar(
                activeTab = activeTab,
                onTabClick = { navController.openTab(it, activeTab) },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

// ---------- Cuenta: Bienvenida, Registro, Login y Recuperar contraseña ----------
private fun NavGraphBuilder.accountDestinations(navController: NavHostController) {
    composable(AppRoutes.WELCOME) {
        WelcomeScreen(
            onCreateAccount = { navController.openFromWelcome(AppRoutes.REGISTER) },
            onLogin = { navController.openFromWelcome(AppRoutes.LOGIN) }
        )
    }
    composable(AppRoutes.REGISTER) {
        RegisterScreen(
            onBack = navController::goBack,
            onLogin = { navController.openFromWelcome(AppRoutes.LOGIN) },
            // Tras registrarse va a Login; atrás desde ahí vuelve a Bienvenida
            onRegistered = { navController.openFromWelcome(AppRoutes.LOGIN) }
        )
    }
    composable(AppRoutes.LOGIN) {
        LoginScreen(
            onBack = navController::goBack,
            onCreateAccount = { navController.openFromWelcome(AppRoutes.REGISTER) },
            onForgotPassword = { navController.navigate(AppRoutes.FORGOT_PASSWORD) },
            // Se borra el historial de cuenta: atrás no debe volver al login
            onLoggedIn = { hasPersonalization ->
                navController.openClearingHistory(AppRoutes.afterLogin(hasPersonalization))
            }
        )
    }
    composable(AppRoutes.FORGOT_PASSWORD) {
        ForgotPasswordScreen(
            onBack = navController::goBack,
            onSent = navController::goBack
        )
    }
}

// ---------- Personalización (Fase 8): Nivel → Objetivo → Ruta lista ----------
private fun NavGraphBuilder.personalizationDestinations(navController: NavHostController) {
    placeholder(AppRoutes.LEVEL, R.string.title_level, navController)
    placeholder(AppRoutes.GOAL, R.string.title_goal, navController)
    placeholder(AppRoutes.PATH_READY, R.string.title_path_ready, navController)
}

// ---------- Pestañas: un grafo anidado por pestaña ----------
private fun NavGraphBuilder.tabGraphs(navController: NavHostController) {
    navigation(startDestination = AppRoutes.HOME, route = AppRoutes.TAB_HOME) {
        placeholder(AppRoutes.HOME, R.string.tab_home)
    }
    navigation(startDestination = AppRoutes.LEARNING_PATH, route = AppRoutes.TAB_LEARN) {
        placeholder(AppRoutes.LEARNING_PATH, R.string.title_learning_path)
        composable(
            route = AppRoutes.LESSON,
            arguments = listOf(navArgument(AppRoutes.LESSON_ARG) { type = NavType.StringType })
        ) {
            PlaceholderScreen(title = R.string.title_lesson, onBack = navController::goBack)
        }
    }
    navigation(startDestination = AppRoutes.GLITCH_CITY, route = AppRoutes.TAB_CHALLENGES) {
        placeholder(AppRoutes.GLITCH_CITY, R.string.title_glitch_city)
        composable(
            route = AppRoutes.ACTIVITY,
            arguments = listOf(navArgument(AppRoutes.ACTIVITY_ARG) { type = NavType.StringType })
        ) {
            PlaceholderScreen(title = R.string.title_activity, onBack = navController::goBack)
        }
    }
    navigation(startDestination = AppRoutes.TUTOR, route = AppRoutes.TAB_TUTOR) {
        placeholder(AppRoutes.TUTOR, R.string.tab_tutor)
    }
    navigation(startDestination = AppRoutes.PROFILE, route = AppRoutes.TAB_PROFILE) {
        composable(AppRoutes.PROFILE) {
            ProfileScreen(
                onAchievements = { navController.navigate(AppRoutes.ACHIEVEMENTS) },
                onSettings = { navController.navigate(AppRoutes.SETTINGS) },
                onSignedOut = { navController.openClearingHistory(AppRoutes.WELCOME) }
            )
        }
        placeholder(AppRoutes.ACHIEVEMENTS, R.string.title_achievements, navController)
        placeholder(AppRoutes.SETTINGS, R.string.title_settings, navController)
    }
}

// Destino provisional. Con navController lleva botón atrás; sin él es raíz de pestaña.
private fun NavGraphBuilder.placeholder(
    route: String,
    @StringRes title: Int,
    navController: NavHostController? = null
) {
    composable(route) {
        PlaceholderScreen(title = title, onBack = navController?.let { it::goBack })
    }
}

// ---------- Acciones de navegación ----------

// Botón atrás de la pantalla. Nunca deja la app sin pantalla (doble toque rápido).
private fun NavHostController.goBack() {
    if (previousBackStackEntry != null) popBackStack()
}

// Abre Registro o Login dejando solo la Bienvenida debajo, para que ir y volver
// entre ambas no acumule pantallas.
private fun NavHostController.openFromWelcome(route: String) = navigate(route) {
    popUpTo(AppRoutes.WELCOME)
    launchSingleTop = true
}

// Abre una pantalla borrando todo el historial (tras iniciar o cerrar sesión).
private fun NavHostController.openClearingHistory(route: String) = navigate(route) {
    popUpTo(graph.id) { inclusive = true }
    launchSingleTop = true
}

// Cambia de pestaña. Inicio queda siempre al fondo (atrás desde otra pestaña vuelve
// a Inicio) y cada pestaña recuerda dónde estaba. Tocar la pestaña activa vuelve
// a su pantalla principal.
private fun NavHostController.openTab(tab: BottomTab, current: BottomTab) {
    if (tab == current) {
        popBackStack(tab.rootRoute, inclusive = false)
        return
    }
    navigate(tab.rootRoute) {
        popUpTo(AppRoutes.HOME) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
