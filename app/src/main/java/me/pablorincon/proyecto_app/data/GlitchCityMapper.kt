package me.pablorincon.proyecto_app.data

import me.pablorincon.proyecto_app.domain.glitchcity.GlitchCityProgress

// Convierte el documento users/{uid}/progress/glitchCity al progreso. Las escrituras llegan con
// las actividades (Fases 14 y 15). Código puro (sin Firebase) para poder probarlo sin red.
// Un documento ausente o dañado cuenta como "sin progreso": nunca debe hacer fallar la app.
internal object GlitchCityMapper {

    const val DISCOVERIES = "discoveries"
    const val PUBLISHED = "published"
    const val DRONE_RECORDS = "droneRecords"
    const val RECORDS_IMPROVED = "recordsImproved"
    const val GEMS_EARNED = "gemsEarned"

    fun fromMap(data: Map<String, Any?>?): GlitchCityProgress {
        val d = data.orEmpty()
        return GlitchCityProgress(
            discoveries = d.strings(DISCOVERIES),
            published = d.strings(PUBLISHED),
            droneRecords = (d[DRONE_RECORDS] as? Map<*, *>).orEmpty()
                .mapNotNull { (key, value) ->
                    val blocks = (value as? Number)?.toInt()
                    if (key is String && blocks != null && blocks > 0) key to blocks else null
                }
                .toMap(),
            recordsImproved = (d[RECORDS_IMPROVED] as? Number)?.toInt()?.coerceAtLeast(0) ?: 0,
            gemsEarned = (d[GEMS_EARNED] as? Number)?.toInt()?.coerceAtLeast(0) ?: 0
        )
    }

    private fun Map<String, Any?>.strings(key: String): Set<String> =
        (this[key] as? List<*>).orEmpty().filterIsInstance<String>().toSet()
}
