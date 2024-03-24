plugins {
    `android-library`
    `kotlin-android`
}

apply<MainGradlePlugin>()

android {
    namespace = "com.zhengzhou.cashflow.feature.server_ui"
}

dependencies {

    core()
    compose()
    test()

    server()

}