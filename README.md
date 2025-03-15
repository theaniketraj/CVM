# Custom Versioning Model (CVM)

The **Custom Versioning Model (CVM)** is a Gradle-based plugin designed to automate the management of project versions, build numbers, and versioning schemes based on a dynamic `version.properties` file.

## Features:
- Automatic version and build number increments.
- Customizable versioning based on project requirements.
- Seamless integration with your existing Gradle build system.

## Getting Started

### 1. Apply the Plugin
In your `build.gradle.kts` file:
```kotlin
```
plugins {
    id("com.example.cvm.versioning") version "1.0.0"
}
2. Configure version.properties
Create and configure a version.properties file in your project root directory to define your versioning parameters.

3. Sync Your Project
Run Gradle sync to apply the plugin and start version management.

Usage
Increment Build Number: Automatically increments the build number with each build.
Custom Versioning: Easily define custom versioning schemes via the version.properties file.
Roadmap
Full documentation to be published upon the release of the package to Gradle Plugin Portal.
Planned features: Version rollback, more flexible version schemes.
License
This project is licensed under the MIT License - see the LICENSE file for details.
