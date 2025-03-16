package com.example.cvm

import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File
import java.util.Properties

class VersioningPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.tasks.register("incrementVersion") {
            doLast {
                val versionFile = File(target.rootDir, "version.properties")
                
                if (!versionFile.exists()) {
                    println("❌ version.properties not found!")
                    return@doLast
                }

                val properties = Properties().apply { 
                    versionFile.inputStream().use { load(it) } 
                }

                // Read and increment build number
                val buildNumber = properties.getProperty("BUILD_NUMBER", "0").toInt() + 1
                properties["BUILD_NUMBER"] = buildNumber.toString()

                // Save back to file
                versionFile.writer().use { properties.store(it, null) }

                println("✅ Build number updated to: $buildNumber")
            }
        }
    }
}
