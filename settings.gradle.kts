pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        mavenLocal()
        gradlePluginPortal()
    }
}

// Include custom module only if it exists as a separate module
include(":app")
include(":versioning") // If `VersioningPlugin` is separate, otherwise remove this.
rootProject.name = "CVM"

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}
