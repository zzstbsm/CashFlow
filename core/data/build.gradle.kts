plugins {
    `android-library`
    `kotlin-android`
}

apply<MainGradlePlugin>()

android {
    namespace = "com.zhengzhou.cashflow.data"
}

dependencies {

    compose()
    core()
    room()
    test()

    themes()

}