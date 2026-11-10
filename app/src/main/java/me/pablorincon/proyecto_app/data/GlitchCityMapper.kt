package me.pablorincon.proyecto_app.data

import me.pablorincon.proyecto_app.domain.glitchcity.GlitchCityProgress

// Convierte el documento users/{uid}/progress/glitchCity al progreso y de vuelta.
// Código puro (sin Firebase) para poder probarlo sin red. Un documento ausente o dañado
// cuenta como "sin progreso": nunca debe hacer fallar la app.
internal object GlitchCityMapper {

    const val DISCOVERIES = "discoveries"
    const val PUBLISHED = "published"
    const val CREATION_VALUES = "creationValues"
    const val DRONE_RECORDS = "droneRecords"
    const val RECORDS_IMPROVED = "recordsImproved"
    const val GEMS_EARNED = "gemsEarned"

    fun toMap(progress: GlitchCityProgress): Map<String, Any> = mapOf(
        DISCOVERIES to progress.discoveries.toList(),
        PUBLISHED to progress.published.toList(),
        CREATION_VALUES to progress.creationValues,
        DRONE_RECORDS to progress.droneRecords,
        RECORDS_IMPROVED to progress.recordsImproved,
        GEMS_EARNED to progress.gemsEarned
    )

    fun fromMap(data: Map<String, Any?>?): GlitchCityProgress {
        val d = data.orEmpty()
        return GlitchCityProgress(
            discoveries = d.strings(DISCOVERIES),
            published = d.strings(PUBLISHED),
            creationValues = (d[CREATION_VALUES] as? Map<*, *>).orEmpty()
                .mapNotNull { (key, value) -> (key as? String)?.let { it to intMap(value) } }
                .toMap(),
            droneRecords = intMap(d[DRONE_RECORDS]).filterValues { it > 0 },
            recordsImproved = (d[RECORDS_IMPROVED] as? Number)?.toInt()?.coerceAtLeast(0) ?: 0,
            gemsEarned = (d[GEMS_EARNED] as? Number)?.toInt()?.coerceAtLeast(0) ?: 0
        )
    }

    private fun Map<String, Any?>.strings(key: String): Set<String> =
        (this[key] as? List<*>).orEmpty().filterIsInstance<String>().toSet()

    // Firestore devuelve los números como Long: se aceptan Int, Long o Double.
    private fun intMap(value: Any?): Map<String, Int> =
        (value as? Map<*, *>).orEmpty()
            .mapNotNull { (key, number) ->
                val n = (number as? Number)?.toInt()
                if (key is String && n != null) key to n else null
            }
            .toMap()
}
