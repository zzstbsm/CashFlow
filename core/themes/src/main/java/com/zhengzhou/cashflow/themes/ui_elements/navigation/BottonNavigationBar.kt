package com.zhengzhou.cashflow.themes.ui_elements.navigation

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.zhengzhou.cashflow.navigation.ApplicationScreensEnum
import com.zhengzhou.cashflow.navigation.NavigationAppTestTag
import com.zhengzhou.cashflow.navigation.functions.routeClick

@Composable
fun BottomNavigationBar(
    currentScreen: ApplicationScreensEnum,
    setCurrentScreen: (ApplicationScreensEnum) -> Unit,
    navController: NavController,
) {

    NavigationBar {
        ApplicationScreensEnum.elements
            .filter{ screen ->
                screen.bottomActive
            }.forEach { item ->
                NavigationBarItem(
                    icon = {
                        RouteIcon(applicationScreensEnum = item)
                    },
                    label = {
                        Text(stringResource(item.optionNameShort))
                    },
                    selected = (currentScreen == item),
                    onClick = {
                        routeClick(
                            setCurrentScreen = setCurrentScreen,
                            applicationScreensEnum = item,
                            navController = navController,
                        )
                    },
                    modifier = Modifier.testTag(
                        NavigationAppTestTag.bottomNavBar(item.name)
                    )
                )
            }
    }
}