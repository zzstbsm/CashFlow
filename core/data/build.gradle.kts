plugins {
    `android-library`
    `kotlin-android`
}

apply<MainGradlePlugin>()

android {
    namespace = "com.zhengzhou.cashflow.data"
}

dependencies {

    core()
    room()
    test()

    implementation(project(":core:themes"))

}