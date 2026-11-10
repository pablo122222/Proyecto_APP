package me.pablorincon.proyecto_app.domain.glitchcity

// Recompensas de Glitch City (guía §10, aprobadas). Cada evento que da recompensa cuenta
// también como actividad del día (racha y meta diaria); repetir algo ya hecho no da nada.
data class GlitchReward(val xp: Int, val gems: Int)

// reward == null: no hubo nada nuevo (y no cuenta como actividad).
data class GlitchEvent(val progress: GlitchCityProgress, val reward: GlitchReward?)

object GlitchCityRules {

    val DISCOVERY = GlitchReward(xp = 10, gems = 2)
    val FIRST_PUBLISH = GlitchReward(xp = 30, gems = 5)
    val RECORD_IMPROVED = GlitchReward(xp = 5, gems = 0)
    const val DRONE_BASE_XP = 20
    const val DRONE_XP_PER_LEVEL = 10
    const val DRONE_GEMS = 3

    fun discover(progress: GlitchCityProgress, lab: Lab, discoveryId: String): GlitchEvent {
        require(discoveryId in lab.discoveryIds) { "Descubrimiento desconocido: $discoveryId" }
        val key = lab.key(discoveryId)
        if (key in progress.discoveries) return GlitchEvent(progress, null)
        return rewarded(progress.copy(discoveries = progress.discoveries + key), DISCOVERY)
    }

    // Actualizar una creación ya publicada no da recompensa.
    fun publish(progress: GlitchCityProgress, creation: Creation): GlitchEvent {
        if (progress.isPublished(creation)) return GlitchEvent(progress, null)
        return rewarded(progress.copy(published = progress.published + creation.id), FIRST_PUBLISH)
    }

    // Primera vez: 20 + 10 × (nivel − 1) XP y 3 gemas. Después solo da algo si mejora el récord.
    fun completeDrone(progress: GlitchCityProgress, level: DroneLevel, blocks: Int): GlitchEvent {
        val record = progress.record(level)
        val records = progress.droneRecords + (level.id to blocks)
        return when {
            record == null -> rewarded(progress.copy(droneRecords = records), firstDroneReward(level))
            blocks < record -> rewarded(
                progress.copy(droneRecords = records, recordsImproved = progress.recordsImproved + 1),
                RECORD_IMPROVED
            )
            else -> GlitchEvent(progress, null)
        }
    }

    fun firstDroneReward(level: DroneLevel): GlitchReward {
        val index = GlitchCityCatalog.droneLevels.indexOf(level)
        require(index >= 0) { "Nivel desconocido: ${level.id}" }
        return GlitchReward(DRONE_BASE_XP + DRONE_XP_PER_LEVEL * index, DRONE_GEMS)
    }

    // Reiniciar quita solo las gemas ganadas en los retos (nunca deja el contador en negativo).
    fun gemsAfterReset(currentGems: Int, progress: GlitchCityProgress): Int =
        (currentGems - progress.gemsEarned).coerceAtLeast(0)

    private fun rewarded(progress: GlitchCityProgress, reward: GlitchReward) =
        GlitchEvent(progress.copy(gemsEarned = progress.gemsEarned + reward.gems), reward)
}
