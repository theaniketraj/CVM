// Test comment
#!/usr/bin/env kotlin

import java.io.File
import java.util.Properties

val versionFile = File("version.properties")
val backupFile = File("version_backup.properties")

// Check if version.properties exists before proceeding
if (!versionFile.exists()) {
    println("🚨 version.properties file is missing! Exiting...")
    System.exit(1)
}

// Create a backup before making any changes
versionFile.copyTo(backupFile, overwrite = true)

println("✅ Backup created: ${backupFile.absolutePath}")
val VERSION_FILE = "version.properties"
val BACKUP_FILE = "version_backup.properties"
val MAJOR_KEY = "VERSION_MAJOR"
val MINOR_KEY = "VERSION_MINOR"
val PATCH_KEY = "VERSION_PATCH"
val BUILD_KEY = "BUILD_NUMBER"

// Function to run shell commands and return output
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

// Backup the current version before updating
fun backupVersion() {
    val versionFile = File(VERSION_FILE)
    val backupFile = File(BACKUP_FILE)

    if (versionFile.exists()) {
        versionFile.copyTo(backupFile, overwrite = true)
        println("✅ Backup created: $BACKUP_FILE")
    } else {
        println("⚠️ No version.properties file found to backup.")
    }
}

// Restore the previous version in case of rollback
fun rollbackVersion() {
    val backupFile = File("version_backup.properties")
    val versionFile = File("version.properties")

    if (!backupFile.exists()) {
        println("⚠️ No backup file found. Cannot rollback!")
        return
    }

    backupFile.copyTo(versionFile, overwrite = true)
    println("🔄 Rollback successful: Restored previous version")
}

// Retrieve the latest Git tag or default to "v0.0.0"
val latestTag = runCommand("git describe --tags --abbrev=0") ?: "v0.0.0"

// Parse Git tag into version components
val tagVersion = latestTag.removePrefix("v").split(".")
val gitMajor = tagVersion.getOrNull(0)?.toIntOrNull() ?: 0
val gitMinor = tagVersion.getOrNull(1)?.toIntOrNull() ?: 0
val gitPatch = tagVersion.getOrNull(2)?.toIntOrNull() ?: 0

// Get short Git commit hash
val gitCommitHash = runCommand("git rev-parse --short HEAD") ?: "unknown"

// Load version.properties or create if missing
val versionFile = File(VERSION_FILE)
val properties = Properties().apply {
    if (versionFile.exists()) {
        load(versionFile.inputStream())
    } else {
        println("⚠️ version.properties not found. Creating a new one.")
        setProperty(MAJOR_KEY, gitMajor.toString())
        setProperty(MINOR_KEY, gitMinor.toString())
        setProperty(PATCH_KEY, gitPatch.toString())
        setProperty(BUILD_KEY, "0")
    }
}

// Perform backup before updating
backupVersion()

// Increment version numbers
val major = properties.getProperty(MAJOR_KEY, gitMajor.toString()).toInt()
val minor = properties.getProperty(MINOR_KEY, gitMinor.toString()).toInt()
val patch = properties.getProperty(PATCH_KEY, gitPatch.toString()).toInt()
val buildNumber = properties.getProperty(BUILD_KEY, "0").toInt() + 1

// Generate the new version string with Git hash
val newVersion = "$major.$minor.$patch+$buildNumber-$gitCommitHash"

// Update properties
properties[MAJOR_KEY] = major.toString()
properties[MINOR_KEY] = minor.toString()
properties[PATCH_KEY] = patch.toString()
properties[BUILD_KEY] = buildNumber.toString()

// Write updated values back to the version.properties file
versionFile.writer().use { properties.store(it, null) }

println("✅ Version updated to: $newVersion (Git hash: $gitCommitHash)")
