package me.pablorincon.proyecto_app.data

// Convierte el documento users/{uid}/progress/curriculum a los ids de lecciones completadas.
// Código puro (sin Firebase) para poder probarlo sin red. Un documento ausente o dañado
// cuenta como "ninguna completada": nunca debe hacer fallar la app.
internal object ProgressMapper {

    const val COMPLETED_LESSONS = "completedLessonIds"

    fun completedLessonIds(data: Map<String, Any?>?): Set<String> =
        (data?.get(COMPLETED_LESSONS) as? List<*>)
            .orEmpty()
            .filterIsInstance<String>()
            .toSet()
}
