package me.pablorincon.proyecto_app.domain

// Contenido del jefe de un nivel: una batalla de ejercicios mezclados del nivel. Cada acierto
// le baja vida al jefe; un fallo no resta nada (el ejercicio vuelve al final, como en las lecciones).
data class BossContent(
    val bossId: String,
    val name: String,       // Ej. "El Bug Gigante"
    val emoji: String,
    val intro: String,      // Presentación del jefe, con humor
    val exercises: List<Exercise>
) {
    init {
        require(exercises.size >= 3) { "El jefe $bossId necesita al menos 3 ejercicios" }
    }
}
