package com.zhengzhou.cashflow.navigation.functions

import androidx.navigation.NavController
import com.zhengzhou.cashflow.navigation.ApplicationScreensEnum
import com.zhengzhou.cashflow.tools.EventMessages

fun routeClick(
    setCurrentScreen: (ApplicationScreensEnum) -> Unit,
    applicationScreensEnum: ApplicationScreensEnum,
    navController: NavController
) {
    if (applicationScreensEnum.routeActive) {
        setCurrentScreen(applicationScreensEnum)
        applicationScreensEnum.navigateTab(navController = navController)
    }
    else {
        EventMessages.sendMessage("Route not active")
    }
}