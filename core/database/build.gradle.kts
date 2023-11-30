plugins {
    `android-library`
    `kotlin-android`
}

apply<MainGradlePlugin>()

android {
    namespace = "com.zhengzhou.cashflow.database"
}

dependencies {

    core()
    room()
    test()

    implementation(project(":core:data"))
    implementation(project(":core:themes"))
    implementation(project(":tools"))
}