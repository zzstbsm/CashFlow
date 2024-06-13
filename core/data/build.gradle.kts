plugins {
    `android-library`
    `kotlin-android`
    kotlin("plugin.serialization")
}

apply<MainGradlePlugin>()

android {
    namespace = "com.zhengzhou.cashflow.data"
}

dependencies {

    implementation(Dependencies.serializationJson)

    compose()
    core()
    room()
    test()

    themes()

}