package com.zhengzhou.cashflow.themes.ui_elements.navigation

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.zhengzhou.cashflow.navigation.ApplicationScreensEnum

@Composable
internal fun RouteIcon(
    applicationScreensEnum : ApplicationScreensEnum,
) {
    Icon(
        painter = painterResource(id = applicationScreensEnum.iconId),
        contentDescription = when (applicationScreensEnum.accessibilityText) {
            null -> null
            else -> stringResource(id = applicationScreensEnum.accessibilityText!!)
        }
    )
}