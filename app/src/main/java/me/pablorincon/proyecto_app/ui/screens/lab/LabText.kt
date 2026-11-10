package me.pablorincon.proyecto_app.ui.screens.lab

import me.pablorincon.proyecto_app.R
import me.pablorincon.proyecto_app.domain.glitchcity.CashierLab
import me.pablorincon.proyecto_app.domain.glitchcity.ElevatorLab
import me.pablorincon.proyecto_app.domain.glitchcity.NpcRulesLab
import me.pablorincon.proyecto_app.domain.glitchcity.OffersShopLab
import me.pablorincon.proyecto_app.domain.glitchcity.RobotLab
import me.pablorincon.proyecto_app.domain.glitchcity.TrafficLightLab

// Textos de los laboratorios por id (el dominio solo guarda ids).
// Un test comprueba que cada control y cada descubrimiento u objetivo tiene aquí su texto.
internal object LabText {

    val paramLabels: Map<String, Int> = mapOf(
        TrafficLightLab.TIME to R.string.lab_param_time,
        CashierLab.PRICE to R.string.lab_param_price,
        CashierLab.QUANTITY to R.string.lab_param_quantity,
        CashierLab.DISCOUNT to R.string.lab_param_discount,
        ElevatorLab.LAST_FLOOR to R.string.lab_param_last_floor,
        ElevatorLab.STEP to R.string.lab_param_step,
        RobotLab.PACKAGES to R.string.lab_param_packages,
        RobotLab.FUNCTION to R.string.lab_param_function,
        OffersShopLab.MIN_PURCHASE to R.string.lab_param_min_purchase,
        OffersShopLab.PERCENT to R.string.lab_param_percent,
        NpcRulesLab.FLEE to R.string.lab_param_flee,
        NpcRulesLab.GREET to R.string.lab_param_greet,
        NpcRulesLab.TEST_DISTANCE to R.string.lab_param_test_distance
    )

    // "actividad:objetivo" → texto
    val goals: Map<String, Int> = mapOf(
        "traffic_light:red" to R.string.lab_goal_traffic_light_red,
        "traffic_light:green" to R.string.lab_goal_traffic_light_green,
        "cashier:bulk_discount" to R.string.lab_goal_cashier_bulk_discount,
        "cashier:no_discount" to R.string.lab_goal_cashier_no_discount,
        "elevator:five_stops" to R.string.lab_goal_elevator_five_stops,
        "elevator:skip_three" to R.string.lab_goal_elevator_skip_three,
        "robot:all_functions" to R.string.lab_goal_robot_all_functions,
        "robot:six_packages" to R.string.lab_goal_robot_six_packages,
        "offers_shop:no_discount_30" to R.string.lab_goal_offers_shop_no_discount_30,
        "offers_shop:discount_60" to R.string.lab_goal_offers_shop_discount_60,
        "offers_shop:max_72_for_90" to R.string.lab_goal_offers_shop_max_72_for_90,
        "npc_rules:flee_1" to R.string.lab_goal_npc_rules_flee_1,
        "npc_rules:greet_4" to R.string.lab_goal_npc_rules_greet_4,
        "npc_rules:wander_8" to R.string.lab_goal_npc_rules_wander_8
    )

    fun goal(activityId: String, goalId: String): Int = goals.getValue("$activityId:$goalId")
}
