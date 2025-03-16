import java.io.File
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.example.cvm.versioning") // Apply custom versioning plugin
}

// Load version properties dynamically
val versionProps = Properties().apply {
    val versionFile = File(rootProject.projectDir, "version.properties")
    if (versionFile.exists()) {
        versionFile.inputStream().use { load(it) }
    } else {
        println("⚠️ Warning: version.properties file is missing. Using default values.")
        setProperty("BUILD_NUMBER", "1")
        setProperty("VERSION_MAJOR", "1")
        setProperty("VERSION_MINOR", "0")
        setProperty("VERSION_PATCH", "0")
    }
}

// Read version values safely
val versionCode = versionProps.getProperty("BUILD_NUMBER", "1").toInt()
val versionName = "${versionProps.getProperty("VERSION_MAJOR", "1")}.${versionProps.getProperty("VERSION_MINOR", "0")}.${versionProps.getProperty("VERSION_PATCH", "0")}"

android {
    namespace = "com.example.cvm"
    compileSdk = 34 // Use the latest stable version

    defaultConfig {
        applicationId = "com.example.cvm"
        minSdk = 24
        targetSdk = 34 // Match compileSdk for best compatibility
        this.versionCode = versionCode
        this.versionName = versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

// Task to display the current version
tasks.register("showVersion") {
    doLast {
        println("Current Version: $versionName (Build: $versionCode)")
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    testImplementation(libs.junit)
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testImplementation(libs.junit.jupiter.params)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
