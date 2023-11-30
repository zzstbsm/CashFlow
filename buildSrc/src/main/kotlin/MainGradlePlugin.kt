import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register
import java.io.FileInputStream
import java.util.Properties

class MainGradlePlugin: Plugin<Project> {

    override fun apply(project: Project) {

        val keystorePropertiesFile = project.rootProject.file("keystore.properties")
        val keystoreProperties = Properties()
        keystoreProperties.load(FileInputStream(keystorePropertiesFile))

        applyPlugins(project)
        setProjectConfig(project,keystoreProperties)
    }

    private fun applyPlugins(project: Project) {
        project.apply {
            plugin("android-library")
            plugin("kotlin-android")
            plugin("com.google.devtools.ksp")
        }
    }

    private fun setProjectConfig(project: Project, keystoreProperties: Properties) {
        project.android().apply {

            signingConfigs {
                create(ProjectConfig.release) {
                    keyAlias = keystoreProperties["keyAlias"] as String
                    keyPassword = keystoreProperties["keyPassword"] as String
                    storeFile = project.file(keystoreProperties["storeFile"] as String)
                    storePassword = keystoreProperties["storePassword"] as String

                }
            }

            compileSdk = ProjectConfig.compileSdk

            defaultConfig {
                minSdk = ProjectConfig.minSdk
                testInstrumentationRunner = ProjectConfig.testInstrumentationRunner
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
            }

            buildTypes {
                getByName(ProjectConfig.release) {
                    proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
                    isJniDebuggable = false
                    isRenderscriptDebuggable = false
                    signingConfig = signingConfigs.getByName(ProjectConfig.release)
                    isMinifyEnabled = true
                }
                register(ProjectConfig.preprod) {
                    proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
                    isJniDebuggable = false
                    isRenderscriptDebuggable = false
                    signingConfig = signingConfigs.getByName(ProjectConfig.release)
                    isMinifyEnabled = true
                }
                register(ProjectConfig.staging) {
                    isJniDebuggable = true
                    isRenderscriptDebuggable = true
                    signingConfig = signingConfigs.getByName(ProjectConfig.debug)
                    isMinifyEnabled = false
                }
                register(ProjectConfig.developement) {
                    isJniDebuggable = true
                    isRenderscriptDebuggable = true
                    signingConfig = signingConfigs.getByName(ProjectConfig.debug)
                    isMinifyEnabled = false
                }
                getByName(ProjectConfig.debug) {
                    isJniDebuggable = true
                    isRenderscriptDebuggable = true
                    signingConfig = signingConfigs.getByName(ProjectConfig.debug)
                    isMinifyEnabled = false
                }
            }

            buildFeatures {
                compose = true
            }
        }
    }

    private fun Project.android(): LibraryExtension {
        return extensions.getByType(LibraryExtension::class.java)
    }

}