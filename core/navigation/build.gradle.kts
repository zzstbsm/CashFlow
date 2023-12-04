plugins {
    `android-library`
    `kotlin-android`
}

apply<MainGradlePlugin>()

android {
    namespace = "com.zhengzhou.cashflow.navigation"
}

dependencies {
    implementation(project(mapOf("path" to ":tools")))
    core()
    compose()
    test()

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation(project(":core:data"))
    implementation(project(":core:database"))

}