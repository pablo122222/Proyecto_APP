package me.pablorincon.proyecto_app.ui.screens.glitchcity

import androidx.annotation.StringRes
import me.pablorincon.proyecto_app.R

// Textos de Glitch City por id del catálogo (el dominio solo guarda ids).
// Un test comprueba que cada id del catálogo tiene aquí su texto.
internal object GlitchCityText {

    // Laboratorio o creación → (nombre, tema)
    val activityNames: Map<String, Pair<Int, Int>> = mapOf(
        "traffic_light" to (R.string.city_lab_traffic_light to R.string.city_topic_variables_conditionals),
        "cashier" to (R.string.city_lab_cashier to R.string.city_topic_operators_conditionals),
        "elevator" to (R.string.city_lab_elevator to R.string.city_topic_loops),
        "robot" to (R.string.city_lab_robot to R.string.city_topic_lists_functions),
        "offers_shop" to (R.string.city_creation_offers_shop to R.string.city_topic_conditionals_functions),
        "npc_rules" to (R.string.city_creation_npc_rules to R.string.city_topic_conditionals)
    )

    val droneLevelNames: Map<String, Int> = mapOf(
        "d1" to R.string.city_drone_d1,
        "d2" to R.string.city_drone_d2,
        "d3" to R.string.city_drone_d3,
        "d4" to R.string.city_drone_d4,
        "d5" to R.string.city_drone_d5
    )

    val titleNames: Map<String, Int> = mapOf(
        "t0" to R.string.city_title_t0,
        "t1" to R.string.city_title_t1,
        "t2" to R.string.city_title_t2,
        "t3" to R.string.city_title_t3,
        "t4" to R.string.city_title_t4,
        "t5" to R.string.city_title_t5,
        "t6" to R.string.city_title_t6,
        "t7" to R.string.city_title_t7
    )

    @StringRes
    fun titleName(id: String): Int = titleNames[id] ?: R.string.city_title_t0
}
