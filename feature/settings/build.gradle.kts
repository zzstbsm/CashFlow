plugins {
    `android-library`
    `kotlin-android`
}

apply<MainGradlePlugin>()

android {
    namespace = "com.zhengzhou.cashflow.settings"
}

dependencies {

    core()
    compose()
    test()

    featuresDependencies()
}