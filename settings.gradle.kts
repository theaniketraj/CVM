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

rootProject.name = "CVM"
include(":app")
// Uncomment the following if you have a separate module for versioning (otherwise, the plugin is in buildSrc)
// include(":versioning")

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}
