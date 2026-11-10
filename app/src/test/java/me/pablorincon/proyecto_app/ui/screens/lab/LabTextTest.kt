package me.pablorincon.proyecto_app.ui.screens.lab

import me.pablorincon.proyecto_app.domain.glitchcity.Labs
import org.junit.Assert.assertEquals
import org.junit.Test

class LabTextTest {

    @Test
    fun cadaControlTieneSuEtiqueta() {
        assertEquals(Labs.all.flatMap { lab -> lab.params.map { it.id } }.toSet(), LabText.paramLabels.keys)
    }

    @Test
    fun cadaDescubrimientoYObjetivoTieneSuTexto() {
        val keys = Labs.all.flatMap { lab -> lab.goalIds.map { "${lab.activityId}:$it" } }.toSet()
        assertEquals(keys, LabText.goals.keys)
    }
}
