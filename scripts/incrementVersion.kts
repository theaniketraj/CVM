#!/usr/bin/env kotlin

import java.io.File
import java.util.Properties

// Constants for property keys and filenames
val VERSION_FILE = "version.properties"
val BACKUP_FILE = "version_backup.properties"
val MAJOR_KEY = "VERSION_MAJOR"
val MINOR_KEY = "VERSION_MINOR"
val PATCH_KEY = "VERSION_PATCH"
val BUILD_KEY = "BUILD_NUMBER"

// Create File objects once
val versionFile = File(VERSION_FILE)
val backupFile = File(BACKUP_FILE)

// Ensure version.properties exists; if not, exit.
if (!versionFile.exists()) {
    println("🚨 ERROR: $VERSION_FILE not found. Exiting...")
    System.exit(1)
}

// Create a backup before making changes
try {
    versionFile.copyTo(backupFile, overwrite = true)
    println("✅ Backup created at: ${backupFile.absolutePath}")
} catch (e: Exception) {
    println("❌ ERROR: Failed to create backup. Exiting...")
    System.exit(1)
}

// Load properties from version.properties
val properties = Properties()
versionFile.inputStream().use { properties.load(it) }

// Retrieve current version values; use defaults if missing
val currentMajor = properties.getProperty(MAJOR_KEY, "0").toIntOrNull() ?: 0
val currentMinor = properties.getProperty(MINOR_KEY, "0").toIntOrNull() ?: 0
val currentPatch = properties.getProperty(PATCH_KEY, "0").toIntOrNull() ?: 0
val currentBuild = properties.getProperty(BUILD_KEY, "0").toIntOrNull() ?: 0
val newBuild = currentBuild + 1

// Helper function to run shell commands
fun runCommand(command: String): String? {
    return try {
        val process = ProcessBuilder(*command.split(" ").toTypedArray())
            .directory(File("."))
            .start()
        process.inputStream.bufferedReader().readText().trim()
    } catch (e: Exception) {
        println("⚠️ Failed to execute command: $command")
        null
    }
}

// Retrieve latest Git tag and commit hash
val latestTag = runCommand("git describe --tags --abbrev=0") ?: "v0.0.0"
val tagComponents = latestTag.removePrefix("v").split(".")
val gitMajor = tagComponents.getOrNull(0)?.toIntOrNull() ?: 0
val gitMinor = tagComponents.getOrNull(1)?.toIntOrNull() ?: 0
val gitPatch = tagComponents.getOrNull(2)?.toIntOrNull() ?: 0
val gitCommitHash = runCommand("git rev-parse --short HEAD") ?: "unknown"

// Decide final version numbers (fallback to Git values if current ones are 0)
val finalMajor = if (currentMajor == 0) gitMajor else currentMajor
val finalMinor = if (currentMinor == 0) gitMinor else currentMinor
val finalPatch = if (currentPatch == 0) gitPatch else currentPatch

// Generate new version string: MAJOR.MINOR.PATCH+build-GitHash
val newVersion = "$finalMajor.$finalMinor.$finalPatch+$newBuild-$gitCommitHash"

// Update properties with new values
properties.setProperty(MAJOR_KEY, finalMajor.toString())
properties.setProperty(MINOR_KEY, finalMinor.toString())
properties.setProperty(PATCH_KEY, finalPatch.toString())
properties.setProperty(BUILD_KEY, newBuild.toString())

// Write updated properties back to version.properties
versionFile.writer().use { writer ->
    properties.store(writer, null)
}

println("✅ Version updated to: $newVersion (Git hash: $gitCommitHash)")
