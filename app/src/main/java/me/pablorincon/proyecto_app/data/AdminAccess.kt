package me.pablorincon.proyecto_app.data

import me.pablorincon.proyecto_app.BuildConfig

// Cuenta de administrador para revisar todos los niveles sin hacer el recorrido: abre todos
// los pasos de la ruta. Solo existe en builds de depuración (BuildConfig.DEBUG): en la versión
// release no hace nada, aunque alguien cree una cuenta con este correo. Es una comodidad de
// desarrollo, no un permiso real (en la Fase 20 se revisa junto con la cuenta de ejemplo).
object AdminAccess {

    const val ADMIN_EMAIL = "admin@codementor.dev"

    fun isAdmin(email: String?, debugBuild: Boolean = BuildConfig.DEBUG): Boolean =
        debugBuild && email.equals(ADMIN_EMAIL, ignoreCase = true)
}
