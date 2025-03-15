package com.example.cvm

import org.gradle.api.Plugin
import org.gradle.api.Project

class VersioningPlugin:Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.register("bumpVersion") {
            doLast {
                val type = project.findProperty("type") as String? ?: "patch"
                VersionManager.bumpVersion(type)
                println("Version bumped to ${VersionManager.getVersion()}")
            }
        }

        project.tasks.register("incrementBuild") {
            doLast {
                VersionManager.incrementBuild()
                println("Build number incremented to ${VersionManager.getBuildNumber()}")
            }
        }
    }
}