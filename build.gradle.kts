plugins {
    id("com.android.application") version "8.5.2"
    id("org.jetbrains.kotlin.android") version "1.9.24"
}
android {
    namespace = "com.mohaa.launcher"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.mohaa.launcher"
        minSdk = 24
        targetSdk = 34
        versionCode = 3
        versionName = "1.3"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
}
