package me.pablorincon.proyecto_app.domain.glitchcity

import me.pablorincon.proyecto_app.domain.UserProfile

// Lo que necesita la pantalla de Glitch City: el perfil (gemas, racha, XP, título) y el progreso.
data class GlitchCityState(val profile: UserProfile, val progress: GlitchCityProgress) {
    val shownTitleId: String get() = GlitchTitles.shown(profile.equippedTitleId, progress)
}

// Progreso de Glitch City (users/{uid}/progress/glitchCity). Los títulos no se guardan:
// se calculan de aquí. El título equipado vive en el perfil (equippedTitleId).
data class GlitchCityProgress(
    val discoveries: Set<String> = emptySet(),      // Lab.key(...) de cada descubrimiento hecho
    val published: Set<String> = emptySet(),        // Ids de creaciones publicadas
    val droneRecords: Map<String, Int> = emptyMap(), // Nivel del dron → menos bloques con que se completó
    val recordsImproved: Int = 0,                   // Veces que se mejoró un récord (título "Récord Personal")
    val gemsEarned: Int = 0                         // Gemas ganadas en los retos: lo que quita "Reiniciar"
) {
    fun discoveriesIn(lab: Lab): Int = lab.discoveryIds.count { lab.key(it) in discoveries }

    fun isPublished(creation: Creation): Boolean = creation.id in published

    fun record(level: DroneLevel): Int? = droneRecords[level.id]

    fun stars(level: DroneLevel): Int? = record(level)?.let(level::starsFor)

    // Desbloqueo secuencial: el primero siempre está abierto; los demás, al completar el anterior.
    fun status(index: Int): DroneLevelStatus {
        val levels = GlitchCityCatalog.droneLevels
        return when {
            record(levels[index]) != null -> DroneLevelStatus.Done
            index == 0 || record(levels[index - 1]) != null -> DroneLevelStatus.Current
            else -> DroneLevelStatus.Locked
        }
    }
}

enum class DroneLevelStatus { Done, Current, Locked }

// Títulos (guía §5.1): se ganan demostrando habilidades; t0 se tiene desde el inicio.
data class GlitchTitle(val id: String, val isEarned: (GlitchCityProgress) -> Boolean)

object GlitchTitles {

    val all: List<GlitchTitle> = listOf(
        GlitchTitle("t0") { true },
        GlitchTitle("t1") { it.discoveries.size >= 3 },
        GlitchTitle("t2") { GlitchCityCatalog.OFFERS_SHOP in it.published },
        GlitchTitle("t3") { GlitchCityCatalog.NPC_RULES in it.published },
        GlitchTitle("t4") { "d3" in it.droneRecords },  // Nivel 3: la escalera de bucles
        GlitchTitle("t5") { "d4" in it.droneRecords },  // Nivel especial 4: depurar el dron
        GlitchTitle("t6") { p -> GlitchCityCatalog.droneLevels.count { p.stars(it) == 3 } >= 3 },
        GlitchTitle("t7") { it.recordsImproved >= 1 }
    )

    fun earned(progress: GlitchCityProgress): Set<String> = all.filter { it.isEarned(progress) }.map { it.id }.toSet()

    fun canEquip(titleId: String, progress: GlitchCityProgress): Boolean = titleId in earned(progress)

    // El título que se muestra: el equipado si sigue ganado (tras reiniciar puede que no); si no, t0.
    fun shown(equippedId: String, progress: GlitchCityProgress): String =
        if (canEquip(equippedId, progress)) equippedId else all.first().id
}
