import org.gradle.api.artifacts.dsl.DependencyHandler

object Dependencies {
    const val core = "androidx.core:core-ktx:${Versions.core}"
    const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"

    const val compose = "androidx.compose.foundation:foundation:${Versions.compose}"
    const val composeRuntime = "androidx.compose.runtime:runtime:${Versions.compose}"
    const val composeUi = "androidx.compose.ui:ui:${Versions.compose}"
    const val composeTooling = "androidx.compose.ui:ui-tooling:${Versions.compose}"
    const val composeMaterial = "androidx.compose.material3:material3:${Versions.composeMaterial3}"
    const val composeMaterialWindowSizeClass = "androidx.compose.material3:material3-window-size-class:${Versions.composeMaterial3}"
    const val composeActivity = "androidx.activity:activity-compose:${Versions.composeActivity}"
    const val composeLifeycle = "androidx.lifecycle:lifecycle-view-model-compose:${Versions.composeLifecycle}"
    const val composeNavigation = "androidx.navigation:navigation-compose:${Versions.composeNavigation}"

    const val datastore = "androidx.datastore:datastore-preferences:${Versions.datastore}"

    const val room = "androidx.room:room-runtime:${Versions.room}"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
    const val roomKtx = "androidx.room:room-ktx:${Versions.room}"

    const val gson = "com.google.code.gson:gson:${Versions.gson}"


    const val junitKtx = "androidx.test.ext:junit-ktx:${Versions.junitKtx}"
    const val junit = "junit:junit:${Versions.junit}"
    const val junitUi = "androidx.compose.ui:ui-test-junit4:${Versions.junitUi}"
}

fun DependencyHandler.compose() {
    implementation(Dependencies.compose)
    implementation(Dependencies.composeRuntime)
    implementation(Dependencies.composeUi)
    implementation(Dependencies.composeTooling)
    implementation(Dependencies.composeMaterial)
    implementation(Dependencies.composeMaterialWindowSizeClass)
    implementation(Dependencies.composeActivity)
    // implementation(Dependencies.composeLifeycle)
    implementation(Dependencies.composeNavigation)

    androidTestImplementation(Dependencies.junitUi)
}

fun DependencyHandler.core() {
    implementation(Dependencies.appCompat)
    implementation(Dependencies.core)
}

fun DependencyHandler.datastore() {
    implementation(Dependencies.datastore)
}

fun DependencyHandler.room() {
    implementation(Dependencies.room)
    implementation(Dependencies.roomKtx)
    ksp(Dependencies.roomCompiler)
}

fun DependencyHandler.test() {
    implementation(Dependencies.junitKtx)
    testImplementation(Dependencies.junit)

}