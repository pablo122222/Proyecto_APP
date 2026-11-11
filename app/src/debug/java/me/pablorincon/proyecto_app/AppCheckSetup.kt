package me.pablorincon.proyecto_app

import com.google.firebase.Firebase
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory

// App Check en debug: Firebase AI Logic solo responde a apps verificadas. El emulador no pasa
// Play Integrity, así que en debug se usa un token de depuración. La primera vez que se abre el
// tutor, Logcat (filtro "DebugAppCheckProvider") muestra:
//   Firebase App Check debug token: <token>
// Ese token se registra en la consola de Firebase: App Check > Apps > (la app) > ⋮ > Administrar
// tokens de depuración. Es privado: no se sube al repositorio. Se mantiene mientras no se
// desinstale la app ni se borren sus datos; si pasa, sale uno nuevo y hay que registrarlo. La versión release usa
// Play Integrity (src/release/.../AppCheckSetup.kt).
object AppCheckSetup {
    fun install() {
        Firebase.appCheck.installAppCheckProviderFactory(DebugAppCheckProviderFactory.getInstance())
    }
}
