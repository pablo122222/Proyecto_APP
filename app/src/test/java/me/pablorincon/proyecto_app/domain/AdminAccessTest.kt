package me.pablorincon.proyecto_app.domain

import me.pablorincon.proyecto_app.data.AdminAccess
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

// Cuenta de administrador: abre todo el currículo, solo en builds de depuración.
class AdminAccessTest {

    private val base = Curriculum.Base

    @Test
    fun soEsAdminElCorreoAdminEnDebug() {
        assertTrue(AdminAccess.isAdmin(AdminAccess.ADMIN_EMAIL, debugBuild = true))
        assertTrue(AdminAccess.isAdmin("ADMIN@CodeMentor.dev", debugBuild = true))
        assertFalse(AdminAccess.isAdmin("otra@cuenta.com", debugBuild = true))
        assertFalse(AdminAccess.isAdmin(null, debugBuild = true))
    }

    @Test
    fun enReleaseNadieEsAdminAunqueUseElCorreo() {
        assertFalse(AdminAccess.isAdmin(AdminAccess.ADMIN_EMAIL, debugBuild = false))
    }

    @Test
    fun conTodoAbiertoSePuedeEntrarACualquierPasoSinCompletarNada() {
        val p = CurriculumProgress(base, emptySet(), unlockAll = true)
        assertTrue(p.isStepUnlocked("n01_l11"))
        assertTrue(p.isStepUnlocked("n08_l05"))
        assertTrue(p.isStepUnlocked("n15_boss"))
        assertFalse(p.isStepUnlocked("no_existe")) // Un id desconocido sigue sin abrirse
    }

    @Test
    fun conTodoAbiertoElRestoDeNivelesQuedaDisponibleYNoBloqueado() {
        val p = CurriculumProgress(base, emptySet(), unlockAll = true)
        assertEquals(ModuleStatus.InProgress, p.modules.first().status)
        assertTrue(p.modules.drop(1).all { it.status == ModuleStatus.Optional })
        assertEquals(StepStatus.Current, p.stepStatus("n01_l01"))
        assertEquals(StepStatus.Available, p.stepStatus("n09_l03"))
    }

    @Test
    fun sinElModoAdminTodoSigueBloqueado() {
        val p = CurriculumProgress(base, emptySet())
        assertFalse(p.isStepUnlocked("n02_l01"))
        assertEquals(StepStatus.Locked, p.stepStatus("n09_l03"))
    }
}
