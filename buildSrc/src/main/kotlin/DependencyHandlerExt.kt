import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.implementation(dependency: Any) {
    add("implementation",dependency)
}

fun DependencyHandler.ksp(dependency: String) {
    add("ksp",dependency)
}

fun DependencyHandler.androidTestImplementation(dependency: String) {
    add("androidTestImplementation",dependency)
}

fun DependencyHandler.testImplementation(dependency: String) {
    add("testImplementation",dependency)
}