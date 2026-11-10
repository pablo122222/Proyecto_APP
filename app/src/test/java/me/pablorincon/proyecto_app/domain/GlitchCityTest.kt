package me.pablorincon.proyecto_app.domain

import me.pablorincon.proyecto_app.domain.glitch.Cell
import me.pablorincon.proyecto_app.domain.glitchcity.Drone
import me.pablorincon.proyecto_app.domain.glitchcity.DroneBlock
import me.pablorincon.proyecto_app.domain.glitchcity.DroneLevelStatus
import me.pablorincon.proyecto_app.domain.glitchcity.DroneMove
import me.pablorincon.proyecto_app.domain.glitchcity.DroneMove.DOWN
import me.pablorincon.proyecto_app.domain.glitchcity.DroneMove.RIGHT
import me.pablorincon.proyecto_app.domain.glitchcity.DroneMove.RIGHT_DOWN
import me.pablorincon.proyecto_app.domain.glitchcity.DroneMove.UP
import me.pablorincon.proyecto_app.domain.glitchcity.DroneOutcome
import me.pablorincon.proyecto_app.domain.glitchcity.GlitchCityCatalog
import me.pablorincon.proyecto_app.domain.glitchcity.GlitchCityProgress
import me.pablorincon.proyecto_app.domain.glitchcity.GlitchCityRules
import me.pablorincon.proyecto_app.domain.glitchcity.GlitchCityState
import me.pablorincon.proyecto_app.domain.glitchcity.GlitchReward
import me.pablorincon.proyecto_app.domain.glitchcity.GlitchTitles
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GlitchCityTest {

    private val levels = GlitchCityCatalog.droneLevels
    private fun level(n: Int) = levels[n - 1]
    private fun b(move: DroneMove, times: Int = 1) = DroneBlock(move, times)

    // Solución con la menor cantidad de bloques de cada nivel (deben dar 3 estrellas).
    private val solutions = mapOf(
        1 to listOf(b(RIGHT, 4), b(DOWN, 4)),
        2 to listOf(b(DOWN, 2), b(RIGHT, 4), b(DOWN, 2)),
        3 to listOf(b(RIGHT_DOWN, 4)),
        4 to listOf(b(RIGHT, 4), b(DOWN, 4)),
        5 to listOf(b(DOWN, 3), b(RIGHT, 2), b(DOWN), b(RIGHT, 2))
    )

    // ---------- Catálogo ----------
    @Test
    fun laCiudadTieneLoDeLaGuia() {
        assertEquals(listOf("traffic_light", "cashier", "elevator", "robot"), GlitchCityCatalog.labs.map { it.id })
        assertTrue(GlitchCityCatalog.labs.all { it.discoveryIds.size == 2 })
        assertEquals(2, GlitchCityCatalog.creations.size)
        assertEquals(5, levels.size)
        assertEquals(listOf(false, false, false, true, true), levels.map { it.special })
        assertTrue(levels.all { it.map.width == 5 && it.map.height == 5 })
    }

    // ---------- Dron ----------
    @Test
    fun cadaNivelTieneSolucionConTresEstrellas() {
        solutions.forEach { (n, program) ->
            assertEquals("nivel $n", DroneOutcome.Delivered(program.size, 3), Drone.run(level(n), program).outcome)
        }
    }

    @Test
    fun unProgramaVacioNoSeEjecuta() {
        val run = Drone.run(level(1), emptyList())
        assertEquals(DroneOutcome.Empty, run.outcome)
        assertEquals(listOf(Cell(0, 0)), run.path)
    }

    @Test
    fun salirseDelMapaEsUnErrorAparte() {
        val run = Drone.run(level(1), listOf(b(UP)))
        assertEquals(DroneOutcome.OutOfMap, run.outcome)
        assertNull(run.crash)
    }

    @Test
    fun chocarConUnMuroMarcaLaCasilla() {
        val run = Drone.run(level(2), listOf(b(RIGHT)))
        assertEquals(DroneOutcome.HitWall, run.outcome)
        assertEquals(Cell(1, 0), run.crash)
        assertEquals(listOf(Cell(0, 0)), run.path)
    }

    @Test
    fun terminarEnOtraCasillaNoEsChoque() {
        val run = Drone.run(level(1), listOf(b(RIGHT, 2)))
        assertEquals(DroneOutcome.WrongCell, run.outcome)
        assertEquals(listOf(Cell(0, 0), Cell(1, 0), Cell(2, 0)), run.path)
    }

    @Test
    fun elDronSeDetieneAlLlegarALaMeta() {
        // Sobra un bloque, pero el dron ya entregó el paquete: no llega a salirse.
        val run = Drone.run(level(1), listOf(b(RIGHT, 4), b(DOWN, 4), b(RIGHT)))
        assertEquals(DroneOutcome.Delivered(3, 2), run.outcome)
        assertEquals(Cell(4, 4), run.path.last())
    }

    @Test
    fun elProgramaPrecargadoDelNivel4SeSaleDelMapa() {
        assertEquals(DroneOutcome.OutOfMap, Drone.run(level(4), level(4).starter).outcome)
    }

    @Test
    fun pasarElMaximoDeBloquesNoCuentaComoEntrega() {
        val seven = listOf(b(DOWN), b(DOWN), b(DOWN), b(RIGHT, 2), b(DOWN), b(RIGHT), b(RIGHT))
        assertEquals(DroneOutcome.TooManyBlocks(7, 6), Drone.run(level(5), seven).outcome)
    }

    @Test
    fun lasEstrellasDependenDelPar() {
        val first = level(1) // par [2, 4]
        assertEquals(3, first.starsFor(2))
        assertEquals(2, first.starsFor(3))
        assertEquals(2, first.starsFor(4))
        assertEquals(1, first.starsFor(5))
    }

    // ---------- Desbloqueo de niveles ----------
    @Test
    fun unUsuarioNuevoSoloTieneElPrimerNivelAbierto() {
        val progress = GlitchCityProgress()
        assertEquals(DroneLevelStatus.Current, progress.status(0))
        assertTrue((1 until levels.size).all { progress.status(it) == DroneLevelStatus.Locked })
    }

    @Test
    fun completarUnNivelAbreElSiguiente() {
        val progress = GlitchCityProgress(droneRecords = mapOf("d1" to 3))
        assertEquals(DroneLevelStatus.Done, progress.status(0))
        assertEquals(DroneLevelStatus.Current, progress.status(1))
        assertEquals(DroneLevelStatus.Locked, progress.status(2))
        assertEquals(2, progress.stars(level(1)))
    }

    // ---------- Recompensas ----------
    @Test
    fun unDescubrimientoNuevoDaRecompensaUnaSolaVez() {
        val lab = GlitchCityCatalog.labs.first()
        val first = GlitchCityRules.discover(GlitchCityProgress(), lab, "red")
        assertEquals(GlitchReward(10, 2), first.reward)
        assertEquals(1, first.progress.discoveriesIn(lab))
        assertEquals(2, first.progress.gemsEarned)

        val again = GlitchCityRules.discover(first.progress, lab, "red")
        assertNull(again.reward)
        assertEquals(first.progress, again.progress)
    }

    @Test
    fun publicarDaRecompensaSoloLaPrimeraVez() {
        val shop = GlitchCityCatalog.creation(GlitchCityCatalog.OFFERS_SHOP)!!
        val first = GlitchCityRules.publish(GlitchCityProgress(), shop)
        assertEquals(GlitchReward(30, 5), first.reward)
        assertTrue(first.progress.isPublished(shop))
        assertNull(GlitchCityRules.publish(first.progress, shop).reward) // Actualizar no da nada
    }

    @Test
    fun completarUnNivelDelDronDaXpSegunSuPosicion() {
        assertEquals(listOf(20, 30, 40, 50, 60), levels.map { GlitchCityRules.firstDroneReward(it).xp })
        val event = GlitchCityRules.completeDrone(GlitchCityProgress(), level(2), 4)
        assertEquals(GlitchReward(30, 3), event.reward)
        assertEquals(4, event.progress.record(level(2)))
    }

    @Test
    fun laPrimeraEntregaLimpiaCuentaParaSolucionAutonoma() {
        val clean = GlitchCityRules.completeDrone(GlitchCityProgress(), level(2), 4, clean = true).progress
        assertEquals(setOf("d2"), clean.cleanSolves)
        val notClean = GlitchCityRules.completeDrone(GlitchCityProgress(), level(2), 4, clean = false).progress
        assertTrue(notClean.cleanSolves.isEmpty())
        // Mejorar el récord después ya no cuenta, aunque sea limpio
        val improved = GlitchCityRules.completeDrone(notClean, level(2), 3, clean = true).progress
        assertTrue(improved.cleanSolves.isEmpty())
    }

    @Test
    fun soloElPrimerProgramaConTresEstrellasCuentaParaEstratega() {
        val perfect = GlitchCityRules.completeDrone(GlitchCityProgress(), level(1), 2).progress
        assertEquals(setOf("d1"), perfect.perfectFirstTries)
        val twoStars = GlitchCityRules.completeDrone(GlitchCityProgress(), level(1), 4).progress
        assertTrue(twoStars.perfectFirstTries.isEmpty())
        // Llegar a 3 estrellas mejorando el récord no cuenta: no fue al primer intento
        val later = GlitchCityRules.completeDrone(twoStars, level(1), 2).progress
        assertTrue(later.perfectFirstTries.isEmpty())
    }

    @Test
    fun soloLosRetosDeCasoLimiteCuentan() {
        assertEquals(0, GlitchCityProgress(discoveries = setOf("cashier:bulk_discount"), published = setOf(GlitchCityCatalog.NPC_RULES)).edgeCasesSolved)
        assertEquals(2, GlitchCityProgress(discoveries = setOf("cashier:no_discount"), published = setOf(GlitchCityCatalog.OFFERS_SHOP)).edgeCasesSolved)
    }

    @Test
    fun soloMejorarElRecordDaRecompensa() {
        val done = GlitchCityRules.completeDrone(GlitchCityProgress(), level(1), 4).progress

        val same = GlitchCityRules.completeDrone(done, level(1), 4)
        assertNull(same.reward)
        val worse = GlitchCityRules.completeDrone(done, level(1), 6)
        assertNull(worse.reward)
        assertEquals(4, worse.progress.record(level(1))) // El récord no empeora

        val better = GlitchCityRules.completeDrone(done, level(1), 2)
        assertEquals(GlitchReward(5, 0), better.reward)
        assertEquals(2, better.progress.record(level(1)))
        assertEquals(1, better.progress.recordsImproved)
    }

    @Test
    fun reiniciarQuitaSoloLasGemasDeLosRetos() {
        val progress = GlitchCityProgress(gemsEarned = 12)
        assertEquals(20, GlitchCityRules.gemsAfterReset(32, progress)) // Las de logros se quedan
        assertEquals(0, GlitchCityRules.gemsAfterReset(5, progress))   // Nunca en negativo
    }

    // ---------- Títulos ----------
    @Test
    fun unUsuarioNuevoSoloTieneElTituloInicial() {
        assertEquals(setOf("t0"), GlitchTitles.earned(GlitchCityProgress()))
        assertEquals(8, GlitchTitles.all.size)
    }

    @Test
    fun cadaTituloSeGanaConSuCondicion() {
        val progress = GlitchCityProgress(
            discoveries = setOf("traffic_light:red", "traffic_light:green", "cashier:no_discount"),
            published = setOf(GlitchCityCatalog.OFFERS_SHOP, GlitchCityCatalog.NPC_RULES),
            droneRecords = mapOf("d1" to 2, "d2" to 3, "d3" to 1, "d4" to 2),
            recordsImproved = 1
        )
        assertEquals(setOf("t0", "t1", "t2", "t3", "t4", "t5", "t6", "t7"), GlitchTitles.earned(progress))
    }

    @Test
    fun tresEstrellasEnSoloDosNivelesNoDaEficienciaTotal() {
        val progress = GlitchCityProgress(droneRecords = mapOf("d1" to 2, "d2" to 3, "d3" to 4))
        assertFalse("t6" in GlitchTitles.earned(progress))
    }

    @Test
    fun unTituloQueYaNoSeTieneSeMuestraComoElInicial() {
        val profile = UserProfile.initial("Ana").copy(equippedTitleId = "t4")
        assertEquals("t0", GlitchCityState(profile, GlitchCityProgress()).shownTitleId)
        assertEquals("t4", GlitchCityState(profile, GlitchCityProgress(droneRecords = mapOf("d3" to 2))).shownTitleId)
        assertFalse(GlitchTitles.canEquip("t9", GlitchCityProgress()))
    }
}
