package me.pablorincon.proyecto_app

import com.google.firebase.Firebase
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory

// App Check en release: Play Integrity comprueba que la petición viene de esta app firmada en
// un dispositivo real. Requiere registrar en la consola de Firebase (App Check > Apps) la huella
// SHA-256 de la clave de firma de la release. Si la app se instala fuera de Google Play, en esa
// misma pantalla se marcan "PLAY_RECOGNIZED" y "LICENSED" como no obligatorios.
object AppCheckSetup {
    fun install() {
        Firebase.appCheck.installAppCheckProviderFactory(PlayIntegrityAppCheckProviderFactory.getInstance())
    }
}
