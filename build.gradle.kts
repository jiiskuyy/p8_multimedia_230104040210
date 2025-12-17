// build.gradle.kts (Project Level)
plugins {
    // Android Gradle Plugin (Sesuaikan dengan versi Android Studio Anda, misal 8.1.3 atau 8.3.0)
    id("com.android.application") version "8.3.0" apply false
    id("com.android.library") version "8.3.0" apply false

    // Kotlin Android Plugin
    id("org.jetbrains.kotlin.android") version "2.0.21" apply false

    // [PENTING] Plugin Compose Compiler (Wajib untuk Kotlin 2.0+)
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21" apply false
}