import java.util.Properties
import java.io.File

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

// Load version properties dynamically
val versionProps = Properties().apply {
    val versionFile = File(rootProject.projectDir, "version.properties")
    if (versionFile.exists()) {
        versionFile.inputStream().use { load(it) }
    }
}

// Read version values
val versionCode = versionProps.getProperty("build", "1").toInt()
val versionName = versionProps.getProperty("version", "1.0.0")

android {
    namespace = "com.example.cvm"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.cvm"
        minSdk = 24
        targetSdk = 35
        this.versionCode = versionCode // Now Gradle correctly reads it
        this.versionName = versionName // Now Gradle correctly reads it

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

// Task to display the current version
tasks.register("showVersion") {
    doLast {
        println("Current Version: $versionName ($versionCode)")
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
