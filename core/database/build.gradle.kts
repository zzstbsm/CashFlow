import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
keystoreProperties.load(FileInputStream(keystorePropertiesFile))

android {

    signingConfigs {
        create("release") {
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
            storeFile = file(keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String

        }
    }

    namespace = "com.zhengzhou.cashflow.database"

    defaultConfig {
        minSdk = 26
        compileSdk = 34

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            isJniDebuggable = false
            isRenderscriptDebuggable = false
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
        }
        register("preprod") {
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            isJniDebuggable = false
            isRenderscriptDebuggable = false
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
        }
        register("stag") {
            isJniDebuggable = true
            isRenderscriptDebuggable = true
            signingConfig = signingConfigs.getByName("debug")
            isMinifyEnabled = false
        }
        register("dev") {
            isJniDebuggable = true
            isRenderscriptDebuggable = true
            signingConfig = signingConfigs.getByName("debug")
            isMinifyEnabled = false
        }
        getByName("debug") {
            isJniDebuggable = true
            isRenderscriptDebuggable = true
            signingConfig = signingConfigs.getByName("debug")
            isMinifyEnabled = false
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

dependencies {

    core()
    room()
    test()

    implementation(project(":core:data"))
    implementation(project(":core:themes"))
    implementation(project(":tools"))
}