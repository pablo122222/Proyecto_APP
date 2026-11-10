package me.pablorincon.proyecto_app.domain

import me.pablorincon.proyecto_app.domain.glitchcity.CashierLab
import me.pablorincon.proyecto_app.domain.glitchcity.ElevatorLab
import me.pablorincon.proyecto_app.domain.glitchcity.LabSimulation
import me.pablorincon.proyecto_app.domain.glitchcity.LabValues
import me.pablorincon.proyecto_app.domain.glitchcity.Labs
import me.pablorincon.proyecto_app.domain.glitchcity.LineNote
import me.pablorincon.proyecto_app.domain.glitchcity.LineStatus.False
import me.pablorincon.proyecto_app.domain.glitchcity.LineStatus.Ran
import me.pablorincon.proyecto_app.domain.glitchcity.LineStatus.Skipped
import me.pablorincon.proyecto_app.domain.glitchcity.NpcRulesLab
import me.pablorincon.proyecto_app.domain.glitchcity.OffersShopLab
import me.pablorincon.proyecto_app.domain.glitchcity.RobotLab
import me.pablorincon.proyecto_app.domain.glitchcity.TrafficLightLab
import org.junit.Assert.assertEquals
import org.junit.Test

class LabTraceTest {

    private fun LabSimulation.runWith(vararg numbers: Pair<String, Int>) =
        run(numbers.fold(LabValues.initial(params)) { values, (id, n) -> values.with(id, n) })

    @Test
    fun cadaLineaDelCodigoTieneSuTrazado() {
        Labs.all.forEach { lab ->
            val run = lab.run(LabValues.initial(lab.params))
            assertEquals(lab.activityId, run.code.size, run.trace.size)
        }
    }

    @Test
    fun semaforoEnRojoRevisaLasDosCondicionesYEjecutaElElse() {
        val trace = TrafficLightLab.runWith(TrafficLightLab.TIME to 3).trace
        assertEquals(listOf(Ran, False, False, Ran), trace.map { it.status })
        assertEquals(LineNote.Check("3 > 40", false), trace[1].note)
        assertEquals(LineNote.ElseRuns, trace[3].note)
    }

    @Test
    fun semaforoEnVerdeNiSiquieraRevisaElElif() {
        val trace = TrafficLightLab.runWith(TrafficLightLab.TIME to 50).trace
        assertEquals(listOf(Ran, Ran, Skipped, Skipped), trace.map { it.status })
        assertEquals(LineNote.NotReached, trace[2].note)
    }

    @Test
    fun cajeroSinDescuentoSaltaLaLineaDentroDelIf() {
        val few = CashierLab.runWith(CashierLab.QUANTITY to 2).trace
        assertEquals(listOf(Ran, False, Skipped), few.map { it.status })
        assertEquals(LineNote.InsideFalseIf, few[2].note)

        val many = CashierLab.runWith(CashierLab.PRICE to 10, CashierLab.QUANTITY to 5, CashierLab.DISCOUNT to 10).trace
        assertEquals(listOf(Ran, Ran, Ran), many.map { it.status })
        assertEquals(LineNote.Assign("total", "45"), many[2].note)
    }

    @Test
    fun elAscensorDiceCuantasVueltasDaElFor() {
        val trace = ElevatorLab.runWith(ElevatorLab.LAST_FLOOR to 7, ElevatorLab.STEP to 3).trace
        assertEquals(LineNote.Loop("piso", listOf(1, 4, 7)), trace[0].note)
        assertEquals(LineNote.LoopBody(3), trace[1].note)
    }

    @Test
    fun elRobotExplicaLaListaYLaLlamada() {
        val trace = RobotLab.run(LabValues.initial(RobotLab.params)).trace
        assertEquals(LineNote.ListValue("paquetes", 4), trace[0].note)
        assertEquals(LineNote.Call("mayor", "8"), trace[1].note)
        val empty = RobotLab.run(LabValues.initial(RobotLab.params).with(RobotLab.PACKAGES, "")).trace
        assertEquals(LineNote.Call("mayor", null), empty[1].note)
    }

    @Test
    fun laTiendaDiceConQueComprasSeUsaCadaReturn() {
        val trace = OffersShopLab.runWith(OffersShopLab.MIN_PURCHASE to 50, OffersShopLab.PERCENT to 30).trace
        assertEquals(LineNote.ShopCheck(50, yes = listOf(60, 90), no = listOf(30)), trace[1].note)
        assertEquals(LineNote.ReturnDiscount(listOf(60 to "42", 90 to "63")), trace[2].note)
        assertEquals(LineNote.ReturnPlain(listOf(30)), trace[3].note)

        // Con mínimo 100 ninguna compra tiene descuento: ese return no se usa
        val none = OffersShopLab.runWith(OffersShopLab.MIN_PURCHASE to 100).trace
        assertEquals(Skipped, none[2].status)
    }

    @Test
    fun elNpcUsaLaDistanciaDePrueba() {
        val trace = NpcRulesLab.runWith(NpcRulesLab.FLEE to 1, NpcRulesLab.GREET to 6, NpcRulesLab.TEST_DISTANCE to 4).trace
        assertEquals(listOf(False, Ran, Skipped), trace.map { it.status })
        assertEquals(LineNote.Check("4 <= 6", true), trace[1].note)
    }
}
