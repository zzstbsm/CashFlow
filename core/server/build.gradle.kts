plugins {
    `android-library`
    `kotlin-android`
}

apply<MainGradlePlugin>()

android {
    namespace = "com.zhengzhou.cashflow.core.server"
}

dependencies {

    core()
    compose()
    test()

    ktor()

    data()
    database()

}