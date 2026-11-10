package me.pablorincon.proyecto_app.ui.screens.glitchcity

import me.pablorincon.proyecto_app.domain.glitchcity.GlitchCityCatalog
import me.pablorincon.proyecto_app.domain.glitchcity.GlitchTitles
import org.junit.Assert.assertEquals
import org.junit.Test

class GlitchCityTextTest {

    @Test
    fun cadaActividadDelCatalogoTieneNombreYTema() {
        val ids = GlitchCityCatalog.labs.map { it.id } + GlitchCityCatalog.creations.map { it.id }
        assertEquals(ids.toSet(), GlitchCityText.activityNames.keys)
    }

    @Test
    fun cadaNivelDelDronTieneNombre() {
        assertEquals(GlitchCityCatalog.droneLevels.map { it.id }.toSet(), GlitchCityText.droneLevelNames.keys)
    }

    @Test
    fun cadaTituloTieneNombreYDescripcion() {
        assertEquals(GlitchTitles.all.map { it.id }.toSet(), GlitchCityText.titleNames.keys)
        assertEquals(GlitchTitles.all.map { it.id }.toSet(), GlitchCityText.titleDescriptions.keys)
    }
}
