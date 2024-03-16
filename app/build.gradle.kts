import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
keystoreProperties.load(FileInputStream(keystorePropertiesFile))

android {

    signingConfigs {
        create(ProjectConfig.release) {
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
            storeFile = file(keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String

        }
    }

    namespace = "com.zhengzhou.cashflow"

    defaultConfig {

        applicationId = ProjectConfig.applicationId

        minSdk = ProjectConfig.minSdk
        targetSdk = ProjectConfig.targetSdk
        compileSdk = ProjectConfig.compileSdk
        versionCode = ProjectConfig.versionCode
        versionName = ProjectConfig.versionName

        testInstrumentationRunner = ProjectConfig.testInstrumentationRunner
    }

    buildTypes {
        getByName(ProjectConfig.release) {
            applicationIdSuffix = ".${ProjectConfig.production}"
            versionNameSuffix = ".${ProjectConfig.production}"
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            isDebuggable = false
            isJniDebuggable = false
            isRenderscriptDebuggable = false
            signingConfig = signingConfigs.getByName(ProjectConfig.release)
            isMinifyEnabled = true
        }
        register(ProjectConfig.preprod) {
            applicationIdSuffix = ".${ProjectConfig.preprod}"
            versionNameSuffix = ".${ProjectConfig.preprod}"
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            isDebuggable = false
            isJniDebuggable = false
            isRenderscriptDebuggable = false
            signingConfig = signingConfigs.getByName(ProjectConfig.release)
            isMinifyEnabled = true
        }
        register(ProjectConfig.staging) {
            applicationIdSuffix = ".${ProjectConfig.staging}"
            versionNameSuffix = ".${ProjectConfig.staging}"
            isDebuggable = true
            isJniDebuggable = true
            isRenderscriptDebuggable = true
            signingConfig = signingConfigs.getByName(ProjectConfig.debug)
            isMinifyEnabled = false
        }
        register(ProjectConfig.developement) {
            applicationIdSuffix = ".${ProjectConfig.developement}"
            versionNameSuffix = ".${ProjectConfig.developement}"
            isDebuggable = true
            isJniDebuggable = true
            isRenderscriptDebuggable = true
            signingConfig = signingConfigs.getByName(ProjectConfig.debug)
            isMinifyEnabled = false
        }
        getByName(ProjectConfig.debug) {
            applicationIdSuffix = ".${ProjectConfig.debug}"
            versionNameSuffix = ".${ProjectConfig.debug}"
            isDebuggable = true
            isJniDebuggable = true
            isRenderscriptDebuggable = true
            signingConfig = signingConfigs.getByName(ProjectConfig.debug)
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = ProjectConfig.kotlinCompilerExtensionVersion
    }
}

dependencies {

    core()
    compose()
    room()
    datastore()

    test()

    featuresDependencies()
    tools()

    allFeatures()

}