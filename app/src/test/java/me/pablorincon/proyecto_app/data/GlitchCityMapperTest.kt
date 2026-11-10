package me.pablorincon.proyecto_app.data

import me.pablorincon.proyecto_app.domain.glitchcity.GlitchCityProgress
import org.junit.Assert.assertEquals
import org.junit.Test

class GlitchCityMapperTest {

    @Test
    fun `sin documento no hay progreso`() {
        assertEquals(GlitchCityProgress(), GlitchCityMapper.fromMap(null))
    }

    @Test
    fun `lee todos los campos y acepta numeros Long`() {
        val data = mapOf<String, Any?>(
            GlitchCityMapper.DISCOVERIES to listOf("traffic_light:red", "robot:six_packages"),
            GlitchCityMapper.PUBLISHED to listOf("offers_shop"),
            GlitchCityMapper.CREATION_VALUES to mapOf("offers_shop" to mapOf("percent" to 20L)),
            GlitchCityMapper.DRONE_RECORDS to mapOf("d1" to 2L, "d2" to 5L),
            GlitchCityMapper.RECORDS_IMPROVED to 1L,
            GlitchCityMapper.GEMS_EARNED to 14L,
            GlitchCityMapper.CLEAN_SOLVES to listOf("d1"),
            GlitchCityMapper.PERFECT_FIRST_TRIES to listOf("d1", "d2")
        )
        val expected = GlitchCityProgress(
            discoveries = setOf("traffic_light:red", "robot:six_packages"),
            published = setOf("offers_shop"),
            creationValues = mapOf("offers_shop" to mapOf("percent" to 20)),
            droneRecords = mapOf("d1" to 2, "d2" to 5),
            recordsImproved = 1,
            gemsEarned = 14,
            cleanSolves = setOf("d1"),
            perfectFirstTries = setOf("d1", "d2")
        )
        assertEquals(expected, GlitchCityMapper.fromMap(data))
        assertEquals(expected, GlitchCityMapper.fromMap(GlitchCityMapper.toMap(expected))) // Ida y vuelta
    }

    @Test
    fun `los datos dañados se ignoran`() {
        val data = mapOf<String, Any?>(
            GlitchCityMapper.DISCOVERIES to listOf("cashier:no_discount", 3, null),
            GlitchCityMapper.PUBLISHED to "offers_shop",
            GlitchCityMapper.DRONE_RECORDS to mapOf("d1" to 0L, "d2" to "tres", 7 to 2L, "d3" to 4L),
            GlitchCityMapper.RECORDS_IMPROVED to -2L,
            GlitchCityMapper.GEMS_EARNED to "muchas"
        )
        val expected = GlitchCityProgress(
            discoveries = setOf("cashier:no_discount"),
            droneRecords = mapOf("d3" to 4)
        )
        assertEquals(expected, GlitchCityMapper.fromMap(data))
    }
}
