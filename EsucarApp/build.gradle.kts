// Archivo de build del PROYECTO (raíz)
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    // KSP: necesario para que Room genere el código del DAO (guía de persistencia)
    alias(libs.plugins.ksp) apply false
}
