package me.pablorincon.proyecto_app.domain.content

import me.pablorincon.proyecto_app.domain.BossContent

// Contenido de los jefes, por id. Se llena nivel por nivel, junto con las lecciones del nivel.
object BossCatalog {

    private val byId: Map<String, BossContent> = listOf(Level01Lessons.boss, Level02Lessons.boss, Level03Lessons.boss, Level04Lessons.boss, Level05Lessons.boss, Level06Lessons.boss, Level07Lessons.boss, Level08Lessons.boss).associateBy { it.bossId }

    fun contentFor(bossId: String): BossContent? = byId[bossId]
}
