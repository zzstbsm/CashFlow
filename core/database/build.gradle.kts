plugins {
    `android-library`
    `kotlin-android`
    id("com.google.devtools.ksp")
}

apply<MainGradlePlugin>()

android {
    namespace = "com.zhengzhou.cashflow.database"

    defaultConfig {

        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }

    }
}

dependencies {

    compose()
    core()
    room()
    test()

    data()
    themes()
}