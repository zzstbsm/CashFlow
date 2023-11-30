import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
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

    namespace = "com.zhengzhou.cashflow.tools"

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
    compose()
    test()

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}