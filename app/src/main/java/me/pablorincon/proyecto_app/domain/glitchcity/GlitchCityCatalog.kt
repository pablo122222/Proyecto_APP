package me.pablorincon.proyecto_app.domain.glitchcity

import me.pablorincon.proyecto_app.domain.glitchcity.DroneMove.DOWN
import me.pablorincon.proyecto_app.domain.glitchcity.DroneMove.RIGHT

// Lo que hay en Glitch City (guía §5): laboratorios de Explorar, creaciones de Crear y niveles
// del dron. Aquí solo van ids y datos de juego; los textos visibles viven en strings.xml, por id.

// Laboratorio de Explorar: cada descubrimiento nuevo da recompensa.
data class Lab(val id: String, val emoji: String, val discoveryIds: List<String>) {
    // Así se guarda un descubrimiento: "laboratorio:descubrimiento" (único en toda la ciudad)
    fun key(discoveryId: String) = "$id:$discoveryId"
}

// Creación de Crear: se publica al cumplir todos sus objetivos.
data class Creation(val id: String, val emoji: String, val objectiveCount: Int)

// Nivel del dron. par = bloques máximos para [3 estrellas, 2 estrellas]; más bloques = 1 estrella.
// starter: programa precargado (nivel de depuración); maxBlocks: límite del reto, si lo tiene.
data class DroneLevel(
    val id: String,
    val rows: List<String>,
    val par: List<Int>,
    val maxBlocks: Int? = null,
    val starter: List<DroneBlock> = emptyList(),
    val special: Boolean = false
) {
    init {
        require(par.size == 2 && par[0] < par[1]) { "par mal definido: $id" }
    }

    val map: DroneMap = DroneMap.parse(rows)

    fun starsFor(blocks: Int): Int = when {
        blocks <= par[0] -> 3
        blocks <= par[1] -> 2
        else -> 1
    }
}

object GlitchCityCatalog {

    val labs: List<Lab> = listOf(
        Lab("traffic_light", "🚦", listOf("red", "green")),
        Lab("cashier", "🛒", listOf("bulk_discount", "no_discount")),
        Lab("elevator", "🏢", listOf("five_stops", "skip_three")),
        Lab("robot", "🤖", listOf("all_functions", "six_packages"))
    )

    val creations: List<Creation> = listOf(
        Creation(OFFERS_SHOP, "🏷️", objectiveCount = 3),
        Creation(NPC_RULES, "🧙", objectiveCount = 3)
    )

    val droneLevels: List<DroneLevel> = listOf(
        DroneLevel("d1", listOf("S....", ".....", ".....", ".....", "....G"), par = listOf(2, 4)),
        DroneLevel("d2", listOf("S#...", ".#.#.", ".....", ".###.", "#...G"), par = listOf(3, 5)),
        DroneLevel("d3", listOf("S.###", "#..##", "##..#", "###..", "####G"), par = listOf(2, 4)),
        DroneLevel(
            "d4", listOf("S....", ".###.", ".#...", ".#.#.", "...#G"), par = listOf(2, 4),
            starter = listOf(DroneBlock(RIGHT, 4), DroneBlock(DOWN, 2), DroneBlock(RIGHT, 2), DroneBlock(DOWN, 2)),
            special = true
        ),
        DroneLevel("d5", listOf("S.#..", "..#.#", ".##..", "...#.", "#...G"), par = listOf(4, 6), maxBlocks = 6, special = true)
    )

    const val OFFERS_SHOP = "offers_shop"
    const val NPC_RULES = "npc_rules"

    fun lab(id: String): Lab? = labs.firstOrNull { it.id == id }
    fun creation(id: String): Creation? = creations.firstOrNull { it.id == id }
    fun droneLevel(id: String): DroneLevel? = droneLevels.firstOrNull { it.id == id }
}
