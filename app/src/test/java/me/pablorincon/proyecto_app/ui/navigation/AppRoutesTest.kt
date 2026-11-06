package me.pablorincon.proyecto_app.ui.navigation

import org.junit.Assert.assertEquals
import org.junit.Test

class AppRoutesTest {

    @Test
    fun rutasConArgumentoSeArmanConElId() {
        assertEquals("leccion/condicionales_1", AppRoutes.lesson("condicionales_1"))
        assertEquals("actividad/semaforo", AppRoutes.activity("semaforo"))
    }

    @Test(expected = IllegalArgumentException::class)
    fun idDeLeccionConCaracteresNoSegurosSeRechaza() {
        AppRoutes.lesson("lección 1/2")
    }

    @Test(expected = IllegalArgumentException::class)
    fun idDeActividadVacioSeRechaza() {
        AppRoutes.activity("")
    }

    @Test
    fun alAbrirLaAppSeEligeElDestinoSegunLaSesion() {
        assertEquals(AppRoutes.TAB_HOME, AppRoutes.startDestination(isLoggedIn = true))
        assertEquals(AppRoutes.WELCOME, AppRoutes.startDestination(isLoggedIn = false))
    }

    @Test
    fun trasIniciarSesionSeVaAInicio() {
        assertEquals(AppRoutes.HOME, AppRoutes.afterLogin())
    }
}
