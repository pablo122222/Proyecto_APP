package me.pablorincon.proyecto_app.domain

import me.pablorincon.proyecto_app.domain.glitchcity.GlitchCityCatalog
import me.pablorincon.proyecto_app.domain.glitchcity.GlitchCityProgress
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class HomeAndStreakTest {

    private val today = LocalDate.of(2026, 11, 9)
    private val yesterday = today.minusDays(1)

    // ---------- Racha y actividad del día ----------
    @Test
    fun unaCuentaNuevaEmpiezaEnCero() {
        val activity = DailyActivity()
        assertEquals(0, activity.streakOn(today))
        assertEquals(0, activity.countOn(today))
    }

    @Test
    fun laPrimeraActividadDejaLaRachaEnUno() {
        val activity = DailyActivity().record(today)
        assertEquals(1, activity.streakOn(today))
        assertEquals(1, activity.countOn(today))
        assertEquals("2026-11-09", activity.lastActiveDate)
    }

    @Test
    fun variasActividadesElMismoDiaNoSubenLaRachaPeroSiElContador() {
        val activity = DailyActivity().record(today).record(today).record(today)
        assertEquals(1, activity.streakOn(today))
        assertEquals(3, activity.countOn(today))
    }

    @Test
    fun diasSeguidosSumanRacha() {
        val activity = DailyActivity(streak = 4, lastActiveDate = yesterday.toString(), activitiesOnLastDay = 2).record(today)
        assertEquals(5, activity.streakOn(today))
        assertEquals(1, activity.countOn(today)) // El contador es de hoy, no de ayer
    }

    @Test
    fun saltarseUnDiaReiniciaLaRacha() {
        val stale = DailyActivity(streak = 7, lastActiveDate = today.minusDays(2).toString(), activitiesOnLastDay = 3)
        assertEquals(0, stale.streakOn(today))  // Se muestra perdida
        assertEquals(0, stale.countOn(today))
        assertEquals(1, stale.record(today).streakOn(today))
    }

    @Test
    fun laRachaDeAyerSigueVivaHastaQueTermineHoy() {
        val activity = DailyActivity(streak = 3, lastActiveDate = yesterday.toString(), activitiesOnLastDay = 1)
        assertEquals(3, activity.streakOn(today))
        assertEquals(0, activity.countOn(today))
    }

    @Test
    fun unaFechaDanadaNoRompeNada() {
        val broken = DailyActivity(streak = 9, lastActiveDate = "ayer", activitiesOnLastDay = 4)
        assertEquals(0, broken.streakOn(today))
        assertEquals(1, broken.record(today).streakOn(today))
    }

    @Test
    fun laMetaDiaria() {
        assertTrue(DailyGoal.isMet(2, 2))
        assertFalse(DailyGoal.isMet(1, 2))
        assertEquals(0.5f, DailyGoal.fraction(1, 2))
        assertEquals(1f, DailyGoal.fraction(9, 2))
    }

    // ---------- Resumen de Inicio ----------
    private val curriculum = Curriculum.Base
    private val titles: (PathStep) -> String? = { step ->
        // Solo el nivel 1 tiene "contenido" en esta prueba
        if (step.id.startsWith("n01")) "Título ${step.id}" else null
    }

    private fun summary(completed: Set<String>, profile: UserProfile = UserProfile.initial("Juan David Bohorquez")) =
        HomeSummary.build(profile, CurriculumProgress(curriculum, completed), curriculum, today, titles)

    @Test
    fun unUsuarioNuevoVeSuPrimeraLeccionYUnaSugerenciaDelTutor() {
        val home = summary(emptySet())
        val next = home.next as NextUp.Step
        assertTrue(next.firstTime)
        assertEquals(curriculum.steps.first(), next.step)
        assertEquals(1, next.level)
        assertEquals(0, next.doneInLevel)
        assertEquals(12, next.stepsInLevel)
        assertNull(home.review)
        assertEquals(0, home.xp)
        assertEquals(0, home.streak)
        assertEquals("JD", home.initials)
    }

    @Test
    fun conLeccionesHechasSigueLaSiguienteYSeRepasaLaUltima() {
        val done = curriculum.modules[0].lessons.take(3).map { it.id }.toSet()
        val home = summary(done)
        val next = home.next as NextUp.Step
        assertFalse(next.firstTime)
        assertEquals(curriculum.modules[0].lessons[3], next.step)
        assertEquals(3, next.doneInLevel)
        assertEquals(curriculum.modules[0].lessons[2], home.review!!.lesson)
    }

    @Test
    fun elJefeSeMuestraComoJefe() {
        val done = curriculum.modules[0].lessons.map { it.id }.toSet()
        val next = summary(done).next as NextUp.Step
        assertTrue(next.isBoss)
        assertEquals(11, next.doneInLevel)
    }

    @Test
    fun siLoSiguienteNoTieneContenidoAvisaQueVienePronto() {
        val done = curriculum.modules[0].steps.map { it.id }.toSet()
        assertEquals(NextUp.ComingSoon(level = 2), summary(done).next)
    }

    // ---------- Reto recomendado ----------
    private fun cityHome(city: GlitchCityProgress?) =
        HomeSummary.build(UserProfile.initial("Ana"), CurriculumProgress(curriculum, emptySet()), curriculum, today, titles, city)

    @Test
    fun sinLeerGlitchCityNoSeRecomiendaReto() {
        assertNull(cityHome(null).challengeId)
    }

    @Test
    fun primeroSeRecomiendaElSiguienteNivelDelDron() {
        assertEquals("d1", cityHome(GlitchCityProgress()).challengeId)
        assertEquals("d3", cityHome(GlitchCityProgress(droneRecords = mapOf("d1" to 2, "d2" to 3))).challengeId)
    }

    @Test
    fun conElDronTerminadoSigueElPrimerLaboratorioPendiente() {
        val allDrone = GlitchCityCatalog.droneLevels.associate { it.id to 9 }
        val trafficDone = GlitchCityProgress(droneRecords = allDrone, discoveries = setOf("traffic_light:red", "traffic_light:green", "cashier:no_discount"))
        assertEquals("cashier", cityHome(trafficDone).challengeId) // Al cajero le falta un descubrimiento
    }

    @Test
    fun luegoLaCreacionSinPublicarYAlFinalNada() {
        val allDrone = GlitchCityCatalog.droneLevels.associate { it.id to 9 }
        val allLabs = GlitchCityCatalog.labs.flatMap { lab -> lab.discoveryIds.map(lab::key) }.toSet()
        val labsDone = GlitchCityProgress(droneRecords = allDrone, discoveries = allLabs, published = setOf(GlitchCityCatalog.OFFERS_SHOP))
        assertEquals(GlitchCityCatalog.NPC_RULES, cityHome(labsDone).challengeId)
        val everything = labsDone.copy(published = GlitchCityCatalog.creations.map { it.id }.toSet())
        assertNull(cityHome(everything).challengeId) // Se vuelve al repaso
    }

    @Test
    fun alTerminarLaRutaSeFelicita() {
        assertEquals(NextUp.Finished, summary(curriculum.steps.map { it.id }.toSet()).next)
    }

    @Test
    fun losNumerosDelDiaSalenDelPerfil() {
        val profile = UserProfile.initial("Ana").copy(
            xp = 140, streak = 2, lastActiveDate = today.toString(), activitiesOnLastDay = 2, dailyGoal = 2
        )
        val home = summary(emptySet(), profile)
        assertEquals(140, home.xp)
        assertEquals(2, home.streak)
        assertEquals(2, home.todayDone)
        assertTrue(home.goalMet)
    }

    @Test
    fun lasIniciales() {
        assertEquals("JD", HomeSummary.initialsOf("juan david bohorquez"))
        assertEquals("A", HomeSummary.initialsOf("  Ana "))
        assertEquals("?", HomeSummary.initialsOf(""))
        assertEquals("ÁL", HomeSummary.initialsOf("Álvaro López"))
    }
}
