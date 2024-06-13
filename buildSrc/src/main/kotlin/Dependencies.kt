import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.project

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
    const val serializationJson = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.serializationJson}"

    const val ktorGson = "io.ktor:ktor-gson:${Versions.ktorGson}"
    const val ktorHTTP = "io.ktor:ktor-server-html-builder:${Versions.ktor}"
    const val ktorCSS = "org.jetbrains:kotlin-css-jvm:${Versions.ktorCSS}"
    const val ktorServerCore = "io.ktor:ktor-server-core:${Versions.ktor}"
    const val ktorServerNetty = "io.ktor:ktor-server-netty:${Versions.ktor}"

    const val junitKtx = "androidx.test.ext:junit-ktx:${Versions.junitKtx}"
    const val junitAndroid = "androidx.test.ext:junit:${Versions.junitKtx}"
    const val junit = "junit:junit:${Versions.junit}"
    const val junitUi = "androidx.compose.ui:ui-test-junit4:${Versions.junitUi}"
    const val testEspresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
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

fun DependencyHandler.allFeatures() {

    implementation(project(":feature:about_me"))
    implementation(project(":feature:all_transactions"))
    implementation(project(":feature:common_transactions"))
    implementation(project(":feature:manage_categories"))
    implementation(project(":feature:profile"))
    implementation(project(":feature:server_ui"))
    implementation(project(":feature:settings"))
    implementation(project(":feature:total_balance"))
    implementation(project(":feature:transaction_edit"))
    implementation(project(":feature:transaction_report"))
    implementation(project(":feature:wallet_edit"))
    implementation(project(":feature:wallet_overview"))
}

fun DependencyHandler.core() {
    implementation(Dependencies.appCompat)
    implementation(Dependencies.core)
}

fun DependencyHandler.data() {
    implementation(project(":core:data"))
    implementation(Dependencies.serializationJson)
}

fun DependencyHandler.database() {
    implementation(project(":core:database"))
}

fun DependencyHandler.datastore() {
    implementation(Dependencies.datastore)
}

fun DependencyHandler.featuresDependencies() {
    data()
    database()
    navigation()
    themes()
    tools()
}

fun DependencyHandler.ktor() {
    implementation(Dependencies.ktorGson)
    implementation(Dependencies.ktorHTTP)
    //implementation(Dependencies.ktorCSS)
    implementation(Dependencies.ktorServerCore)
    implementation(Dependencies.ktorServerNetty)
}

fun DependencyHandler.navigation() {
    implementation(project(":core:navigation"))
}

fun DependencyHandler.room() {
    implementation(Dependencies.room)
    implementation(Dependencies.roomKtx)
    ksp(Dependencies.roomCompiler)
}

fun DependencyHandler.server() {
    implementation(project(":core:server"))
}

fun DependencyHandler.test() {
    implementation(Dependencies.junitKtx)
    testImplementation(Dependencies.junit)
    androidTestImplementation(Dependencies.junitAndroid)
    androidTestImplementation(Dependencies.testEspresso)
}

fun DependencyHandler.themes() {
    implementation(project(":core:themes"))
}

fun DependencyHandler.tools() {
    implementation(project(":core:tools"))
}