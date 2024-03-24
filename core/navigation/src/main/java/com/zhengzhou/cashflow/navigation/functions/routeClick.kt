package com.zhengzhou.cashflow.navigation.functions

import android.util.Log
import androidx.navigation.NavController
import com.zhengzhou.cashflow.navigation.ApplicationScreensEnum
import com.zhengzhou.cashflow.navigation.BuildConfig

const val TAG = "Navigation_routeClick"

fun routeClick(
    setCurrentScreen: (ApplicationScreensEnum) -> Unit,
    applicationScreensEnum: ApplicationScreensEnum,
    navController: NavController
) {
    if (applicationScreensEnum.routeActive) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG,"Navigating to the page ${applicationScreensEnum.name}")
        }
        setCurrentScreen(applicationScreensEnum)
        applicationScreensEnum.navigateTab(navController = navController)
    }
    else if (BuildConfig.DEBUG) {
        Log.d(TAG, "Route not active")
    }
}