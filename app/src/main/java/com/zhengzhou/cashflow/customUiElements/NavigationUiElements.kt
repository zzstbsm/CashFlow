package com.zhengzhou.cashflow.customUiElements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.zhengzhou.cashflow.R
import com.zhengzhou.cashflow.tools.EventMessages
import kotlinx.coroutines.launch

@Composable
fun BottomNavigationBar(
    currentScreen: com.zhengzhou.cashflow.navigation.NavigationCurrentScreen,
    setCurrentScreen: (com.zhengzhou.cashflow.navigation.NavigationCurrentScreen) -> Unit,
    navController: NavController,
) {

    NavigationBar {
        com.zhengzhou.cashflow.navigation.NavigationCurrentScreen.elements
            .filter{ screen ->
            screen.bottomActive
            }.forEach { item ->
                NavigationBarItem(
                    icon = {
                        RouteIcon(navigationCurrentScreen = item)
                    },
                    label = {
                        Text(stringResource(item.optionNameShort))
                    },
                    selected = (currentScreen == item),
                    onClick = {
                        routeClick(
                            setCurrentScreen = setCurrentScreen,
                            navigationCurrentScreen = item,
                            navController = navController,
                        )
                    },
                    modifier = Modifier.testTag(
                        com.zhengzhou.cashflow.navigation.NavigationAppTestTag.bottomNavBar(item.route)
                    )
                )
            }
    }
}

@Composable
fun SectionNavigationDrawerSheet(
    drawerState: DrawerState,
    currentScreen: com.zhengzhou.cashflow.navigation.NavigationCurrentScreen,
    setCurrentScreen: (com.zhengzhou.cashflow.navigation.NavigationCurrentScreen) -> Unit,
    navController: NavController,
) {

    val scope = rememberCoroutineScope()

    ModalDrawerSheet {
        Column {
            Text(
                text = stringResource(id = R.string.app_name),
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                maxLines = 1,
                modifier = Modifier.padding(horizontal = 28.dp, vertical = 16.dp),
            )
            com.zhengzhou.cashflow.navigation.NavigationCurrentScreen.elements
                .filter { item ->
                    item.navBarActive
                }
                .forEach { item ->
                    NavigationDrawerItem(
                        icon = {
                            RouteIcon(navigationCurrentScreen = item)
                        },
                        label = { Text(stringResource(id = item.optionName)) },
                        selected = item == currentScreen,
                        onClick = {
                            scope.launch { drawerState.close() }
                            routeClick(
                                setCurrentScreen = setCurrentScreen,
                                navigationCurrentScreen = item,
                                navController = navController,
                            )
                        },
                        modifier = Modifier
                            .testTag(
                                com.zhengzhou.cashflow.navigation.NavigationAppTestTag.drawerNavBar(item.route)
                            )
                            .padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }

        }
    }
}

@Composable
private fun RouteIcon(
    navigationCurrentScreen : com.zhengzhou.cashflow.navigation.NavigationCurrentScreen,
) {
    Icon(
        painter = painterResource(id = navigationCurrentScreen.iconId),
        contentDescription = if (navigationCurrentScreen.accessibilityText == null) {
            null
        } else {
            stringResource(id = navigationCurrentScreen.accessibilityText)
        }
    )
}

private fun routeClick(
    setCurrentScreen: (com.zhengzhou.cashflow.navigation.NavigationCurrentScreen) -> Unit,
    navigationCurrentScreen: com.zhengzhou.cashflow.navigation.NavigationCurrentScreen,
    navController: NavController
) {
    if (navigationCurrentScreen.routeActive) {
        setCurrentScreen(navigationCurrentScreen)
        navigationCurrentScreen.navigateTab(navController = navController)
    }
    else {
        EventMessages.sendMessage("Route not active")
    }
}

