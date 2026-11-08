package me.pablorincon.proyecto_app.domain.content

import me.pablorincon.proyecto_app.domain.LessonContent

// Contenido de todas las lecciones, por id. Se llena módulo por módulo (decisión C):
// una lección sin contenido aprobado aún se muestra como "llegará pronto".
object LessonCatalog {

    private val byId: Map<String, LessonContent> =
        (FundamentosLessons.all).associateBy { it.lessonId }

    fun contentFor(lessonId: String): LessonContent? = byId[lessonId]
}
