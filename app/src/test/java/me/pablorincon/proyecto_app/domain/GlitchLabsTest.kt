package me.pablorincon.proyecto_app.domain

import me.pablorincon.proyecto_app.domain.glitchcity.CashierLab
import me.pablorincon.proyecto_app.domain.glitchcity.ElevatorLab
import me.pablorincon.proyecto_app.domain.glitchcity.GlitchCityCatalog
import me.pablorincon.proyecto_app.domain.glitchcity.GlitchCityProgress
import me.pablorincon.proyecto_app.domain.glitchcity.GlitchCityRules
import me.pablorincon.proyecto_app.domain.glitchcity.LabResult
import me.pablorincon.proyecto_app.domain.glitchcity.LabSimulation
import me.pablorincon.proyecto_app.domain.glitchcity.LabValues
import me.pablorincon.proyecto_app.domain.glitchcity.Labs
import me.pablorincon.proyecto_app.domain.glitchcity.NpcAction
import me.pablorincon.proyecto_app.domain.glitchcity.NpcRulesLab
import me.pablorincon.proyecto_app.domain.glitchcity.OffersShopLab
import me.pablorincon.proyecto_app.domain.glitchcity.RobotLab
import me.pablorincon.proyecto_app.domain.glitchcity.TrafficLight
import me.pablorincon.proyecto_app.domain.glitchcity.TrafficLightLab
import me.pablorincon.proyecto_app.domain.glitchcity.WhyQuestions
import me.pablorincon.proyecto_app.domain.glitchcity.formatHundredths
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GlitchLabsTest {

    private fun LabSimulation.start() = LabValues.initial(params)
    private fun LabSimulation.runWith(vararg numbers: Pair<String, Int>) =
        run(numbers.fold(start()) { values, (id, n) -> values.with(id, n) })

    // ---------- Catálogo ----------
    @Test
    fun losLaboratoriosCoincidenConElCatalogo() {
        GlitchCityCatalog.labs.forEach { lab ->
            assertEquals(lab.discoveryIds, Labs.byId(lab.id)!!.goalIds)
        }
        GlitchCityCatalog.creations.forEach { creation ->
            assertEquals(creation.objectiveCount, Labs.byId(creation.id)!!.goalIds.size)
        }
    }

    @Test
    fun conLosValoresInicialesSoloElCajeroDescubreAlgo() {
        // Los valores de la guía (20 × 2 con 10 %) ya muestran que con menos de 5 no hay descuento.
        // Por eso la pantalla solo entrega descubrimientos después de que el usuario mueva un control.
        // (En Crear, los objetivos son una lista que se marca en vivo: pueden empezar cumplidos.)
        val explore = GlitchCityCatalog.labs.map { Labs.byId(it.id)!! }
        explore.filter { it != CashierLab }.forEach { lab ->
            assertEquals(lab.activityId, emptySet<String>(), lab.run(lab.start()).met)
        }
        assertEquals(setOf("no_discount"), CashierLab.run(CashierLab.start()).met)
    }

    @Test
    fun ningunaCreacionEmpiezaLista() {
        listOf(OffersShopLab, NpcRulesLab).forEach { lab ->
            assertFalse(lab.activityId, lab.run(lab.start()).met.containsAll(lab.goalIds))
        }
    }

    @Test
    fun cadaDescubrimientoYObjetivoSePuedeCumplir() {
        val reachable = mapOf(
            TrafficLightLab to listOf(TrafficLightLab.runWith(TrafficLightLab.TIME to 5), TrafficLightLab.runWith(TrafficLightLab.TIME to 50)),
            CashierLab to listOf(
                CashierLab.runWith(CashierLab.PRICE to 10, CashierLab.QUANTITY to 5, CashierLab.DISCOUNT to 50),
                CashierLab.runWith()
            ),
            ElevatorLab to listOf(
                ElevatorLab.runWith(ElevatorLab.LAST_FLOOR to 5),
                ElevatorLab.runWith(ElevatorLab.LAST_FLOOR to 12, ElevatorLab.STEP to 3)
            ),
            OffersShopLab to listOf(OffersShopLab.runWith(OffersShopLab.MIN_PURCHASE to 40, OffersShopLab.PERCENT to 20)),
            NpcRulesLab to listOf(NpcRulesLab.runWith(NpcRulesLab.FLEE to 2, NpcRulesLab.GREET to 6))
        )
        reachable.forEach { (lab, runs) ->
            assertEquals(lab.activityId, lab.goalIds.toSet(), runs.flatMap { it.met }.toSet())
        }
    }

    // ---------- Semáforo ----------
    @Test
    fun elSemaforoCambiaEnLosLimitesDeLaGuia() {
        fun light(t: Int) = (TrafficLightLab.runWith(TrafficLightLab.TIME to t).result as LabResult.Light).light
        assertEquals(TrafficLight.Red, light(10))
        assertEquals(TrafficLight.Yellow, light(11))
        assertEquals(TrafficLight.Yellow, light(40))
        assertEquals(TrafficLight.Green, light(41))
        assertEquals("tiempo = 41", TrafficLightLab.runWith(TrafficLightLab.TIME to 41).code.first())
    }

    // ---------- Cajero ----------
    @Test
    fun elCajeroSoloDescuentaDesdeCinco() {
        val four = CashierLab.runWith(CashierLab.PRICE to 20, CashierLab.QUANTITY to 4, CashierLab.DISCOUNT to 10)
        assertEquals(LabResult.Total("80"), four.result)
        assertEquals(setOf("no_discount"), four.met)

        val five = CashierLab.runWith(CashierLab.PRICE to 7, CashierLab.QUANTITY to 5, CashierLab.DISCOUNT to 10)
        assertEquals(LabResult.Total("31.5"), five.result) // 35 × 0.9
        assertEquals(setOf("bulk_discount"), five.met)
    }

    @Test
    fun sinDescuentoConfiguradoNoSeCompruebaNada() {
        assertFalse("no_discount" in CashierLab.runWith(CashierLab.DISCOUNT to 0).met)
    }

    @Test
    fun pagarExactamente40NoCuentaComoMenos() {
        val forty = CashierLab.runWith(CashierLab.PRICE to 10, CashierLab.QUANTITY to 5, CashierLab.DISCOUNT to 20)
        assertEquals(LabResult.Total("40"), forty.result)
        assertFalse("bulk_discount" in forty.met)
    }

    // ---------- Ascensor ----------
    @Test
    fun elAscensorRecorreLosPisosDeRange() {
        val run = ElevatorLab.runWith(ElevatorLab.LAST_FLOOR to 10, ElevatorLab.STEP to 3)
        assertEquals(LabResult.Floors(listOf(1, 4, 7, 10)), run.result)
        assertEquals("for piso in range(1, 11, 3):", run.code.first())
        assertEquals(setOf("skip_three"), run.met)
        assertEquals(setOf("five_stops"), ElevatorLab.runWith(ElevatorLab.LAST_FLOOR to 9, ElevatorLab.STEP to 2).met)
    }

    // ---------- Robot ----------
    @Test
    fun elRobotIgnoraLoQueNoEsNumeroYUsaMaximo12() {
        assertEquals(listOf(4, 8, 2), RobotLab.parse(" 4, ocho, 8,,2.5, 2 "))
        assertEquals(12, RobotLab.parse((1..20).joinToString(",")).size)
        assertTrue(RobotLab.parse("hola").isEmpty())
    }

    @Test
    fun elRobotAplicaCadaFuncion() {
        fun output(function: String) = RobotLab.run(RobotLab.start().with(RobotLab.FUNCTION, function)).result
        assertEquals(LabResult.RobotOutput("8"), output("mayor"))
        assertEquals(LabResult.RobotOutput("21"), output("suma"))
        assertEquals(LabResult.RobotOutput("[2, 4, 7, 8]"), output("ordenar"))
        assertEquals(LabResult.EmptyList, RobotLab.run(RobotLab.start().with(RobotLab.PACKAGES, "x")).result)
    }

    @Test
    fun probarLasTresFuncionesCuentaEnLaMismaVisita() {
        val values = RobotLab.start().with(RobotLab.FUNCTION, "ordenar")
        assertFalse("all_functions" in RobotLab.run(values, mapOf(RobotLab.FUNCTION to setOf("mayor"))).met)
        assertTrue("all_functions" in RobotLab.run(values, mapOf(RobotLab.FUNCTION to setOf("mayor", "suma"))).met)
        assertTrue("six_packages" in RobotLab.run(values.with(RobotLab.PACKAGES, "1,2,3,4,5,6")).met)
    }

    // ---------- Tienda de ofertas ----------
    @Test
    fun laTiendaCalculaElPrecioDeCadaCompra() {
        val run = OffersShopLab.runWith(OffersShopLab.MIN_PURCHASE to 50, OffersShopLab.PERCENT to 15)
        assertEquals(LabResult.Prices(listOf(30 to "30", 60 to "51", 90 to "76.5")), run.result)
        assertEquals(setOf("no_discount_30", "discount_60"), run.met)
    }

    @Test
    fun noventaCuesta72ConExactamenteUn20PorCiento() {
        val run = OffersShopLab.runWith(OffersShopLab.MIN_PURCHASE to 40, OffersShopLab.PERCENT to 20)
        assertEquals(setOf("no_discount_30", "discount_60", "max_72_for_90"), run.met)
    }

    // ---------- NPC ----------
    @Test
    fun elNpcRevisaLasReglasEnOrden() {
        assertEquals(NpcAction.Flee, NpcRulesLab.action(1, flee = 1, greet = 5))   // Cumple ambas: gana el if
        assertEquals(NpcAction.Greet, NpcRulesLab.action(4, flee = 1, greet = 5))
        assertEquals(NpcAction.Wander, NpcRulesLab.action(8, flee = 1, greet = 5))
    }

    @Test
    fun losObjetivosDelNpcNoDependenDeLaDistanciaDePrueba() {
        val a = NpcRulesLab.runWith(NpcRulesLab.FLEE to 1, NpcRulesLab.GREET to 7, NpcRulesLab.TEST_DISTANCE to 0)
        val b = NpcRulesLab.runWith(NpcRulesLab.FLEE to 1, NpcRulesLab.GREET to 7, NpcRulesLab.TEST_DISTANCE to 9)
        assertEquals(a.met, b.met)
        assertEquals(setOf("flee_1", "greet_4", "wander_8"), a.met)
        assertEquals(LabResult.Npc(9, NpcAction.Wander), b.result)
    }

    @Test
    fun saludarHasta8ImpidePasearA8() {
        assertFalse("wander_8" in NpcRulesLab.runWith(NpcRulesLab.FLEE to 1, NpcRulesLab.GREET to 8).met)
    }

    // ---------- Formato, publicación y preguntas ----------
    @Test
    fun losNumerosSeMuestranSinCerosDeMas() {
        assertEquals("90", formatHundredths(9000))
        assertEquals("87.5", formatHundredths(8750))
        assertEquals("31.05", formatHundredths(3105))
    }

    @Test
    fun actualizarUnaCreacionGuardaLosValoresSinRecompensa() {
        val shop = GlitchCityCatalog.creation(GlitchCityCatalog.OFFERS_SHOP)!!
        val first = GlitchCityRules.publish(GlitchCityProgress(), shop, mapOf("percent" to 20))
        val update = GlitchCityRules.publish(first.progress, shop, mapOf("percent" to 30))
        assertEquals(null, update.reward)
        assertEquals(mapOf("percent" to 30), update.progress.creationValues[shop.id])
        assertEquals(first.progress.gemsEarned, update.progress.gemsEarned)
    }

    @Test
    fun cadaCreacionTieneSuPreguntaConUnaRespuestaValida() {
        GlitchCityCatalog.creations.forEach { creation ->
            val question = WhyQuestions.forActivity(creation.id)!!
            assertTrue(question.correctIndex in 0 until question.optionCount)
        }
    }
}
