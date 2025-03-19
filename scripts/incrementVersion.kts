#!/usr/bin/env kotlin

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

    // Function to run shell commands
    fun runCommand(command: String): String? {
        return try {
            val process = ProcessBuilder(*command.split(" ").toTypedArray())
                .directory(File("."))
                .start()
            process.inputStream.bufferedReader().readText().trim()
        } catch (e: Exception) {
            null
        }
    }

// Retrieve the latest Git tag
    val latestTag = runCommand("git describe --tags --abbrev=0") ?: "v0.0.0"

// Parse Git tag into version components
    val tagVersion = latestTag.removePrefix("v").split(".")
    val gitMajor = tagVersion.getOrNull(0)?.toIntOrNull() ?: 0
    val gitMinor = tagVersion.getOrNull(1)?.toIntOrNull() ?: 0
    val gitPatch = tagVersion.getOrNull(2)?.toIntOrNull() ?: 0

// Get short Git commit hash
    val gitCommitHash = runCommand("git rev-parse --short HEAD") ?: "unknown"

// Load version.properties file
    val versionFile = File("version.properties")
    val properties = Properties().apply { load(versionFile.inputStream()) }

// Fallback to local versioning if no Git tags exist
    val major = properties["VERSION_MAJOR"].toString().toIntOrNull() ?: gitMajor
    val minor = properties["VERSION_MINOR"].toString().toIntOrNull() ?: gitMinor
    val patch = properties["VERSION_PATCH"].toString().toIntOrNull() ?: gitPatch
    val buildNumber = properties["BUILD_NUMBER"].toString().toInt() + 1

// Generate the new version with Git hash
    val newVersion = "$major.$minor.$patch+$buildNumber-$gitCommitHash"

// Update version.properties
    properties["VERSION_MAJOR"] = major.toString()
    properties["VERSION_MINOR"] = minor.toString()
    properties["VERSION_PATCH"] = patch.toString()
    properties["BUILD_NUMBER"] = buildNumber.toString()

// Write updated values back to the file
    versionFile.writer().use { properties.store(it, null) }

    println("🔄 Version updated to: $newVersion (Git hash: $gitCommitHash)")

    // override fun apply(target: Project) {
    //     // Register incrementVersion task
    //     target.tasks.register("incrementVersion") {
    //         doLast {
    //             // Ensure version.properties exists
    //             val versionFile = File(target.rootDir, VERSION_FILE)
    //             if (!versionFile.exists()) {
    //                 println("⚠️ $VERSION_FILE not found. Creating default version file.")
    //                 versionFile.writeText("$MAJOR_KEY=1\n$MINOR_KEY=0\n$PATCH_KEY=0\n$BUILD_KEY=0")
    //             }

    //             // Load version properties
    //             val properties = Properties().apply { load(versionFile.inputStream()) }

    //             // Read current version values
    //             val major = properties.getProperty(MAJOR_KEY, "1").toInt()
    //             val minor = properties.getProperty(MINOR_KEY, "0").toInt()
    //             val patch = properties.getProperty(PATCH_KEY, "0").toInt()
    //             val buildNumber = properties.getProperty(BUILD_KEY, "0").toInt()

    //             // Increment version logic (based on existence of release flags)
    //             val newVersion = when {
    //                 File("RELEASE_MAJOR").exists() -> "${major + 1}.0.0"
    //                 File("RELEASE_MINOR").exists() -> "$major.${minor + 1}.0"
    //                 File("RELEASE_PATCH").exists() -> "$major.$minor.${patch + 1}"
    //                 else -> "$major.$minor.$patch"
    //             }

    //             // Increment build number
    //             val newBuildNumber = buildNumber + 1

    //             // Update properties file with new version values
    //             properties[MAJOR_KEY] = newVersion.split(".")[0]
    //             properties[MINOR_KEY] = newVersion.split(".")[1]
    //             properties[PATCH_KEY] = newVersion.split(".")[2]
    //             properties[BUILD_KEY] = newBuildNumber.toString()

    //             // Write back to version.properties file
    //             versionFile.bufferedWriter().use { properties.store(it, null) }

    //             println("✅ Version updated to: $newVersion (Build: $newBuildNumber)")
            }
        
    

