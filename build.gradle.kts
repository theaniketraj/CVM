// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    kotlin("jvm") version "1.8.22" apply false // Ensure proper application of Kotlin JVM
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register("incrementVersion") {
    doLast {
        val versionFile = file("version.properties")
        val properties = java.util.Properties().apply {
            load(versionFile.inputStream())
        }

        val oldBuild = properties["build"]?.toString()?.toInt() ?: 0
        val newBuild = oldBuild + 1
        properties["build"] = newBuild.toString()
        versionFile.writer().use { writer ->
            properties.store(writer, null)
        }

        println("Build incremented: $oldBuild -> $newBuild")
    }
} 

