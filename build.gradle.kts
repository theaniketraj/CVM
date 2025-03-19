// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // id("com.android.application") apply false
    // id("org.jetbrains.kotlin.android") apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    // id("com.example.cvm.versioning") apply true
    // We no longer need the Kotlin JVM plugin here
    // id("com.example.cvm.versioning") version "1.0.0" apply false // Custom plugin is now in buildSrc
}
// apply(plugin = "com.example.cvm.versioning")

// allprojects {
//     repositories {
//         google()
//         mavenCentral()
//     }
// }

// Increment version task to update version.properties
tasks.register("incrementVersion") {
    doLast {
        val versionFile = file("version.properties")
        if (!versionFile.exists()) {
            println("⚠️ version.properties not found! Skipping version increment.")
            return@doLast
        }
        val properties = java.util.Properties().apply { load(versionFile.inputStream()) }
        val oldBuild = properties.getProperty("BUILD_NUMBER", "0").toIntOrNull() ?: 0
        val newBuild = oldBuild + 1
        properties.setProperty("BUILD_NUMBER", newBuild.toString())
        versionFile.bufferedWriter().use { properties.store(it, null) }
        println("✅ Build incremented: $oldBuild → $newBuild")
    }
}

// Optionally, if you want this to run for each subproject, you could do:
subprojects {
    tasks.matching { it.name == "assemble" }.configureEach {
        dependsOn(rootProject.tasks.named("incrementVersion"))
    }
}