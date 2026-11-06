package me.pablorincon.proyecto_app.ui.navigation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class BottomTabTest {

    @Test
    fun cadaPestanaSeActivaEnSuPantallaPrincipal() {
        BottomTab.entries.forEach { tab ->
            assertEquals(tab, BottomTab.forRoute(tab.rootRoute))
        }
    }

    @Test
    fun actividadYLogrosMantienenActivaSuPestana() {
        assertEquals(BottomTab.Challenges, BottomTab.forRoute(AppRoutes.ACTIVITY))
        assertEquals(BottomTab.Profile, BottomTab.forRoute(AppRoutes.ACHIEVEMENTS))
    }

    @Test
    fun pantallasSinBarraNoTienenPestana() {
        listOf(
            AppRoutes.WELCOME, AppRoutes.REGISTER, AppRoutes.LOGIN, AppRoutes.FORGOT_PASSWORD,
            AppRoutes.LEVEL, AppRoutes.GOAL, AppRoutes.PATH_READY,
            AppRoutes.LESSON, AppRoutes.SETTINGS, null
        ).forEach { route ->
            assertNull("La ruta $route no debería mostrar la barra", BottomTab.forRoute(route))
        }
    }

    @Test
    fun ordenDeLasPestanasEsElDeLaGuia() {
        assertEquals(
            listOf(BottomTab.Home, BottomTab.Learn, BottomTab.Challenges, BottomTab.Tutor, BottomTab.Profile),
            BottomTab.entries.toList()
        )
    }
}
