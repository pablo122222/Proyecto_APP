import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.services)
}

// Firma de la versión release: se lee de keystore.properties (en la raíz del proyecto, fuera de
// git) para que la clave y sus contraseñas nunca se suban al repositorio.
val keystoreFile: File = rootProject.file("keystore.properties")
val keystore = Properties().apply {
    if (keystoreFile.exists()) keystoreFile.inputStream().use { load(it) }
}

// Tutor IA con Ollama en el PC (solo debug). Por defecto la app busca Ollama en su propio
// localhost, que se conecta con el del PC con:  adb reverse tcp:11434 tcp:11434
// (sirve para el emulador y para un teléfono por USB; en este emulador 10.0.2.2 no llega desde
// la app). Para un teléfono por wifi, en local.properties (fuera de git):
//   ollama.url=http://<IP-del-PC>:11434
//   ollama.model=qwen2.5:3b
val localProps = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) file.inputStream().use { load(it) }
}
val ollamaUrl: String = localProps.getProperty("ollama.url", "http://127.0.0.1:11434")
val ollamaModel: String = localProps.getProperty("ollama.model", "qwen2.5:3b")

android {
    namespace = "me.pablorincon.proyecto_app"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "me.pablorincon.proyecto_app"
        minSdk = 28
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        if (keystoreFile.exists()) {
            create("release") {
                storeFile = rootProject.file(keystore.getProperty("storeFile"))
                storePassword = keystore.getProperty("storePassword")
                keyAlias = keystore.getProperty("keyAlias")
                keyPassword = keystore.getProperty("keyPassword")
            }
        }
    }

    buildTypes {
        debug {
            buildConfigField("String", "OLLAMA_URL", "\"$ollamaUrl\"")
            buildConfigField("String", "OLLAMA_MODEL", "\"$ollamaModel\"")
        }
        release {
            // Sin servidor propio todavía: la release usa siempre la demo del tutor
            buildConfigField("String", "OLLAMA_URL", "\"\"")
            buildConfigField("String", "OLLAMA_MODEL", "\"\"")
            // R8: reduce el instalable y quita el código que no se usa en release
            // (por ejemplo, la cuenta de ejemplo del login, que solo existe en debug)
            optimization {
                enable = true
            }
            // Sin keystore.properties se firma con la clave de depuración, solo para poder
            // instalar y probar la release en el emulador. Play Store rechaza esa firma.
            signingConfig = if (keystoreFile.exists()) signingConfigs.getByName("release")
            else signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true // AdminAccess usa BuildConfig.DEBUG
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}