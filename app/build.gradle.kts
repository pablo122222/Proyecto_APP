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

// Tutor IA: por defecto responde Gemini en la nube (Firebase AI Logic, ver GeminiClient).
// Opcional, solo en debug: usar Ollama en el PC (sin internet ni cuota). En local.properties
// (fuera de git):
//   ollama.url=http://127.0.0.1:11434    (con: adb reverse tcp:11434 tcp:11434; emulador o USB)
//   ollama.url=http://<IP-del-PC>:11434  (teléfono por wifi)
//   ollama.model=qwen2.5:3b
// Sin ollama.url, debug también usa Gemini.
val localProps = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) file.inputStream().use { load(it) }
}
val ollamaUrl: String = localProps.getProperty("ollama.url", "")
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
            // La release nunca usa Ollama: responde Gemini en la nube
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
    implementation(libs.firebase.ai)                     // Tutor IA con Gemini (Firebase AI Logic)
    implementation(libs.firebase.appcheck.playintegrity) // App Check en release
    debugImplementation(libs.firebase.appcheck.debug)    // App Check en debug (token de depuración)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}