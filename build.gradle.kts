// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}

// Repository Configuration
allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

// Version Increment Task
tasks.register("incrementVersion") {
    doLast {
        val versionFile = file("version.properties")

        if (!versionFile.exists()) {
            println("❌ version.properties file not found! Skipping version increment.")
            return@doLast
        }

        val properties = java.util.Properties().apply {
            versionFile.inputStream().use { load(it) }
        }

        val oldBuild = properties["BUILD_NUMBER"]?.toString()?.toIntOrNull() ?: 0
        val newBuild = oldBuild + 1

        properties["BUILD_NUMBER"] = newBuild.toString()
        versionFile.writer().use { properties.store(it, null) }

        println("✅ Build number incremented: $oldBuild → $newBuild")
    }
}

// Ensure version increment happens before build
tasks.named("assemble") {
    dependsOn("incrementVersion")
}
