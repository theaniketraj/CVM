package com.example.cvm

import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File
import java.util.Properties

class VersioningPlugin : Plugin<Project> {

    // Constants for property keys
    companion object {
        const val VERSION_FILE = "version.properties"
        const val MAJOR_KEY = "VERSION_MAJOR"
        const val MINOR_KEY = "VERSION_MINOR"
        const val PATCH_KEY = "VERSION_PATCH"
        const val BUILD_KEY = "BUILD_NUMBER"
    }

    override fun apply(target: Project) {
        // Register incrementVersion task
        target.tasks.register("incrementVersion") {
            doLast {
                // Ensure version.properties exists
                val versionFile = File(target.rootDir, VERSION_FILE)
                if (!versionFile.exists()) {
                    println("⚠️ $VERSION_FILE not found. Creating default version file.")
                    versionFile.writeText("$MAJOR_KEY=1\n$MINOR_KEY=0\n$PATCH_KEY=0\n$BUILD_KEY=0")
                }

                // Load version properties
                val properties = Properties().apply { load(versionFile.inputStream()) }

                // Read current version values
                val major = properties.getProperty(MAJOR_KEY, "1").toInt()
                val minor = properties.getProperty(MINOR_KEY, "0").toInt()
                val patch = properties.getProperty(PATCH_KEY, "0").toInt()
                val buildNumber = properties.getProperty(BUILD_KEY, "0").toInt()

                // Increment version logic (based on existence of release flags)
                val newVersion = when {
                    File("RELEASE_MAJOR").exists() -> "${major + 1}.0.0"
                    File("RELEASE_MINOR").exists() -> "$major.${minor + 1}.0"
                    File("RELEASE_PATCH").exists() -> "$major.$minor.${patch + 1}"
                    else -> "$major.$minor.$patch"
                }

                // Increment build number
                val newBuildNumber = buildNumber + 1

                // Update properties file with new version values
                properties[MAJOR_KEY] = newVersion.split(".")[0]
                properties[MINOR_KEY] = newVersion.split(".")[1]
                properties[PATCH_KEY] = newVersion.split(".")[2]
                properties[BUILD_KEY] = newBuildNumber.toString()

                // Write back to version.properties file
                versionFile.bufferedWriter().use { properties.store(it, null) }

                println("✅ Version updated to: $newVersion (Build: $newBuildNumber)")
            }
        }
    }
}
