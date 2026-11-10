package me.pablorincon.proyecto_app.domain.glitchcity

// Laboratorios de Explorar y Crear (guía §5.2): cada uno tiene controles, genera su código
// Python en vivo, calcula un resultado y dice qué descubrimientos u objetivos se cumplen.
// Las cuentas con dinero se hacen con enteros (centésimas) para comparar sin errores de redondeo.

// ---------- Controles ----------

sealed interface LabParam {
    val id: String
}

data class SliderParam(override val id: String, val min: Int, val max: Int, val initial: Int, val step: Int = 1) : LabParam
data class ChoiceParam(override val id: String, val options: List<String>, val initial: String) : LabParam
data class TextParam(override val id: String, val initial: String) : LabParam

// Valores actuales de los controles. Los sliders guardan números; el resto, texto.
data class LabValues(val numbers: Map<String, Int> = emptyMap(), val texts: Map<String, String> = emptyMap()) {
    fun number(id: String): Int = numbers.getValue(id)
    fun text(id: String): String = texts.getValue(id)

    fun with(id: String, value: Int) = copy(numbers = numbers + (id to value))
    fun with(id: String, value: String) = copy(texts = texts + (id to value))

    companion object {
        fun initial(params: List<LabParam>) = LabValues(
            numbers = params.filterIsInstance<SliderParam>().associate { it.id to it.initial },
            texts = params.filterIsInstance<ChoiceParam>().associate { it.id to it.initial } +
                params.filterIsInstance<TextParam>().associate { it.id to it.initial }
        )
    }
}

// ---------- Resultado de una ejecución ----------

enum class TrafficLight { Green, Yellow, Red }
enum class NpcAction { Flee, Greet, Wander }

sealed interface LabResult {
    data class Light(val light: TrafficLight) : LabResult
    data class Total(val total: String) : LabResult
    data class Floors(val floors: List<Int>) : LabResult
    data object EmptyList : LabResult                              // Robot sin números válidos
    data class RobotOutput(val output: String) : LabResult
    data class Prices(val lines: List<Pair<Int, String>>) : LabResult // compra → paga
    data class Npc(val distance: Int, val action: NpcAction) : LabResult
}

// code: líneas de Python; met: ids de los descubrimientos u objetivos que se cumplen ahora.
data class LabRun(val code: List<String>, val result: LabResult, val met: Set<String>)

// ---------- Laboratorios ----------

sealed interface LabSimulation {
    val activityId: String
    val goalIds: List<String>              // Descubrimientos (Explorar) u objetivos (Crear), en orden
    val params: List<LabParam>

    // tried: opciones que ya se probaron en esta visita, por control (para "prueba las tres funciones")
    fun run(values: LabValues, tried: Map<String, Set<String>> = emptyMap()): LabRun
}

object TrafficLightLab : LabSimulation {
    const val TIME = "time"
    override val activityId = "traffic_light"
    override val goalIds = listOf("red", "green")
    override val params = listOf(SliderParam(TIME, 0, 60, initial = 30))

    override fun run(values: LabValues, tried: Map<String, Set<String>>): LabRun {
        val t = values.number(TIME)
        val light = when {
            t > 40 -> TrafficLight.Green
            t > 10 -> TrafficLight.Yellow
            else -> TrafficLight.Red
        }
        val code = listOf(
            "tiempo = $t",
            "if tiempo > 40: luz = \"verde\"",
            "elif tiempo > 10: luz = \"amarilla\"",
            "else: luz = \"roja\""
        )
        val met = buildSet {
            if (light == TrafficLight.Red) add("red")
            if (light == TrafficLight.Green) add("green")
        }
        return LabRun(code, LabResult.Light(light), met)
    }
}

object CashierLab : LabSimulation {
    const val PRICE = "price"
    const val QUANTITY = "quantity"
    const val DISCOUNT = "discount"
    private const val BULK = 5             // Desde esta cantidad hay descuento
    override val activityId = "cashier"
    override val goalIds = listOf("bulk_discount", "no_discount")
    override val params = listOf(
        SliderParam(PRICE, 1, 50, initial = 20),
        SliderParam(QUANTITY, 1, 10, initial = 2),
        SliderParam(DISCOUNT, 0, 50, initial = 10)
    )

    override fun run(values: LabValues, tried: Map<String, Set<String>>): LabRun {
        val p = values.number(PRICE)
        val q = values.number(QUANTITY)
        val d = values.number(DISCOUNT)
        val discounted = q >= BULK
        val cents = p * q * (if (discounted) 100 - d else 100) // total × 100
        val code = listOf(
            "total = $p * $q",
            "if $q >= $BULK:",
            "    total = total * (1 - $d/100)"
        )
        val met = buildSet {
            if (discounted && cents < 40 * 100) add("bulk_discount")
            if (!discounted && d > 0) add("no_discount") // Sin descuento: total = precio × cantidad
        }
        return LabRun(code, LabResult.Total(formatHundredths(cents)), met)
    }
}

object ElevatorLab : LabSimulation {
    const val LAST_FLOOR = "last_floor"
    const val STEP = "step"
    override val activityId = "elevator"
    override val goalIds = listOf("five_stops", "skip_three")
    override val params = listOf(
        SliderParam(LAST_FLOOR, 1, 12, initial = 6),
        SliderParam(STEP, 1, 3, initial = 1)
    )

    override fun run(values: LabValues, tried: Map<String, Set<String>>): LabRun {
        val n = values.number(LAST_FLOOR)
        val s = values.number(STEP)
        val floors = (1..n step s).toList()
        val code = listOf(
            "for piso in range(1, ${n + 1}, $s):",
            "    visitar(piso)"
        )
        val met = buildSet {
            if (floors.size == 5) add("five_stops")
            if (s == 3 && n >= 10) add("skip_three")
        }
        return LabRun(code, LabResult.Floors(floors), met)
    }
}

object RobotLab : LabSimulation {
    const val PACKAGES = "packages"
    const val FUNCTION = "function"
    const val MAX_PACKAGES = 12
    val functions = listOf("mayor", "suma", "ordenar")
    override val activityId = "robot"
    override val goalIds = listOf("all_functions", "six_packages")
    override val params = listOf(
        TextParam(PACKAGES, initial = "4,8,2,7"),
        ChoiceParam(FUNCTION, functions, initial = "mayor")
    )

    // Solo cuentan los números enteros; lo demás se ignora. Máximo 12.
    fun parse(text: String): List<Int> =
        text.split(',').mapNotNull { it.trim().toIntOrNull() }.take(MAX_PACKAGES)

    override fun run(values: LabValues, tried: Map<String, Set<String>>): LabRun {
        val packages = parse(values.text(PACKAGES))
        val function = values.text(FUNCTION)
        val code = listOf(
            "paquetes = [${packages.joinToString(", ")}]",
            "resultado = $function(paquetes)"
        )
        val result = if (packages.isEmpty()) LabResult.EmptyList else LabResult.RobotOutput(
            when (function) {
                "suma" -> packages.sum().toString()
                "ordenar" -> packages.sorted().joinToString(", ", "[", "]")
                else -> packages.max().toString()
            }
        )
        val met = buildSet {
            if (packages.isNotEmpty() && (tried[FUNCTION].orEmpty() + function).containsAll(functions)) add("all_functions")
            if (packages.size >= 6) add("six_packages")
        }
        return LabRun(code, result, met)
    }
}

object OffersShopLab : LabSimulation {
    const val MIN_PURCHASE = "min_purchase"
    const val PERCENT = "percent"
    private val purchases = listOf(30, 60, 90)
    override val activityId = GlitchCityCatalog.OFFERS_SHOP
    override val goalIds = listOf("no_discount_30", "discount_60", "max_72_for_90")
    override val params = listOf(
        SliderParam(MIN_PURCHASE, 10, 100, initial = 100, step = 10),
        SliderParam(PERCENT, 5, 50, initial = 5, step = 5)
    )

    override fun run(values: LabValues, tried: Map<String, Set<String>>): LabRun {
        val u = values.number(MIN_PURCHASE)
        val d = values.number(PERCENT)
        // Lo que paga cada compra, en centésimas
        fun cents(purchase: Int) = purchase * (if (purchase >= u) 100 - d else 100)
        val code = listOf(
            "def precio_final(compra):",
            "    if compra >= $u:",
            "        return compra * (1 - $d/100)",
            "    return compra"
        )
        val met = buildSet {
            if (30 < u) add("no_discount_30")
            if (60 >= u) add("discount_60")
            if (cents(90) <= 72 * 100) add("max_72_for_90")
        }
        return LabRun(code, LabResult.Prices(purchases.map { it to formatHundredths(cents(it)) }), met)
    }
}

object NpcRulesLab : LabSimulation {
    const val FLEE = "flee"
    const val GREET = "greet"
    const val TEST_DISTANCE = "test_distance"
    const val MAX_DISTANCE = 10
    override val activityId = GlitchCityCatalog.NPC_RULES
    override val goalIds = listOf("flee_1", "greet_4", "wander_8")
    override val params = listOf(
        SliderParam(FLEE, 0, 5, initial = 0),
        SliderParam(GREET, 1, 9, initial = 1),
        SliderParam(TEST_DISTANCE, 0, MAX_DISTANCE, initial = 5)
    )

    fun action(distance: Int, flee: Int, greet: Int): NpcAction = when {
        distance <= flee -> NpcAction.Flee
        distance <= greet -> NpcAction.Greet
        else -> NpcAction.Wander
    }

    // Los objetivos se miden con las reglas, no con la distancia de prueba.
    override fun run(values: LabValues, tried: Map<String, Set<String>>): LabRun {
        val h = values.number(FLEE)
        val s = values.number(GREET)
        val x = values.number(TEST_DISTANCE)
        val code = listOf(
            "if distancia <= $h: accion = \"huir\"",
            "elif distancia <= $s: accion = \"saludar\"",
            "else: accion = \"pasear\""
        )
        val met = buildSet {
            if (action(1, h, s) == NpcAction.Flee) add("flee_1")
            if (action(4, h, s) == NpcAction.Greet) add("greet_4")
            if ((8..MAX_DISTANCE).all { action(it, h, s) == NpcAction.Wander }) add("wander_8")
        }
        return LabRun(code, LabResult.Npc(x, action(x, h, s)), met)
    }
}

object Labs {
    val all: List<LabSimulation> = listOf(TrafficLightLab, CashierLab, ElevatorLab, RobotLab, OffersShopLab, NpcRulesLab)

    fun byId(activityId: String): LabSimulation? = all.firstOrNull { it.activityId == activityId }
}

// Centésimas → texto con hasta 2 decimales y sin ceros de más: 9000 → "90", 8750 → "87.5".
fun formatHundredths(hundredths: Int): String {
    val whole = hundredths / 100
    val rest = hundredths % 100
    return when {
        rest == 0 -> whole.toString()
        rest % 10 == 0 -> "$whole.${rest / 10}"
        else -> "$whole.${rest.toString().padStart(2, '0')}"
    }
}

// Pregunta "¿Por qué funciona?" (decisión D) tras publicar por primera vez. Los textos viven
// en strings.xml; aquí solo cuántas opciones hay y cuál es la correcta.
data class WhyQuestion(val activityId: String, val optionCount: Int, val correctIndex: Int)

object WhyQuestions {
    val all: List<WhyQuestion> = listOf(
        WhyQuestion(GlitchCityCatalog.OFFERS_SHOP, optionCount = 3, correctIndex = 1),
        WhyQuestion(GlitchCityCatalog.NPC_RULES, optionCount = 3, correctIndex = 2)
    )

    fun forActivity(activityId: String): WhyQuestion? = all.firstOrNull { it.activityId == activityId }
}
