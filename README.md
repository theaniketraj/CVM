# Custom Versioning Model (CVM)

The **Custom Versioning Model (CVM)** is a lightweight, Gradle-based tool that automates project version management. It reads version information from a dynamic `version.properties` file and automatically increments build numbers and updates version details during your build process.

## Features

- **Automated Version Management:** Automatically updates your build number and version information.
- **Simple Configuration:** Manage versioning through an easily editable `version.properties` file.
- **Gradle Integration:** Seamlessly integrates with any Gradle-based project.
- **Future Integration with CEIE:** CVM is designed to be integrated as a core component in CEIE, providing a robust versioning solution.

## Getting Started

### Prerequisites

- **Java Development Kit (JDK):** JDK 11 or later.
- **Gradle:** Ensure you're using a compatible Gradle version (e.g., Gradle 8.12.0).
- **Kotlin:** Familiarity with Kotlin is beneficial as CVM is written in Kotlin.

### Installation

1. **Clone the Repository:**
   
   ```bash
   git clone https://github.com/theaniketraj/CVM.git
   cd CVM
   ```
2. **Configure the gradle Wrapper:** Ensure that your ```gradle/wrapper/gradle-wrapper.properties``` file points to a valid Gradle version:
   
   ```properties
   distributionUrl=https\://services.gradle.org/distributions/gradle-8.12.0-all.zip
   ```
3. **Set Up Version Properties:** Create a ```version.properties``` file in the project root with initial values:
   
    ```properties
    VERSION_MAJOR=1
    VERSION_MINOR=0
    VERSION_PATCH=0
    BUILD_NUMBER=1
    ```

### Usage

**Incrementing the Version**
To automatically increment the build number and update version information, run the following Gradle task:
   ```bash
   ./gradlew incrementVersion
   ```
    
This task:

- **Reads the current version data from** ```version.properties```
- **Increments the** ```BUILD_NUMBER```
- **Optionally updates the major, minor, or patch numbers if a corresponding release trigger file** (e.g., ```RELEASE_MAJOR```, ```RELEASE_MINOR```, or ```RELEASE_PATCH```) exists
- **Writes the updated version information back to** ```version.properties```

**Viewing the Current Version**

A dedicated task is available to display the current version:
   ```bash
   ./gradle showVersion
   ```

**Integration with [CEIE](https://github.com/theaniketraj/ceie)**

CVM is planned to be integrated into CEIE as a core component. This integration will streamline version management across CEIE projects by automating version and build number updates seamlessly.

**Roadmap**

- **Enhanced Testing:** Continuous testing across multiple environments.
- **Expanded Documentation:** More detailed guides and configuration options.
- **CEIE Integration:** Full integration of CVM into the CEIE ecosystem.

**Contributing**

Contributions are welcome! If you'd like to contribute improvements or fixes:

- **Fork the repository**.
- **Create a new branch for your feature or bugfix.**
- **Submit a pull request with your changes.**

**License**

This project is licensed under the [MIT License](https://github.com/theaniketraj/CVM/blob/main/LICENSE)

**Note**

This project is being deprecated. Check [VISTA](https://github.com/theaniketraj/VISTA) for an updated release.
