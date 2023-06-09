package com.zhengzhou.cashflow.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.zhengzhou.cashflow.NavigationCurrentScreen
import com.zhengzhou.cashflow.tools.EventMessages
import kotlinx.coroutines.launch

@Composable
fun BottomNavigationBar(
    currentScreen: NavigationCurrentScreen,
    setCurrentScreen: (NavigationCurrentScreen) -> Unit,
    navController: NavController,
) {

    NavigationBar() {
        NavigationCurrentScreen.elements
            .filter{ screen ->
            screen.bottomActive
            }.forEach { item ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = item.iconId),
                            contentDescription = stringResource(id = item.accessibilityText),
                        )
                    },
                    label = {
                        Text(stringResource(item.optionName))
                    },
                    selected = (currentScreen == item),
                    onClick = {
                        if (item.routeActive) {
                            setCurrentScreen(item)
                            item.navigate(navController = navController)
                        }
                        else {
                            EventMessages.sendMessage("Route not active")
                        }
                    }
                )
            }
    }
}

@Composable
fun CustomNavigationDrawerSheet(
    drawerState: DrawerState,
    currentScreen: NavigationCurrentScreen,
    setCurrentScreen: (NavigationCurrentScreen) -> Unit,
    navController: NavController,
) {

    val scope = rememberCoroutineScope()

    ModalDrawerSheet {
        NavigationCurrentScreen.elements
            .filter { item ->
                item.navBarActive
            }
            .forEach { item ->
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = item.iconId),
                            contentDescription = stringResource(id = item.accessibilityText),
                        )
                    },
                    label = { Text(stringResource(id = item.optionName)) },
                    selected = item == currentScreen,
                    onClick = {
                        scope.launch { drawerState.close() }
                        if (item.routeActive) {
                            setCurrentScreen(item)
                            item.navigate(navController = navController)
                        }
                        else {
                            EventMessages.sendMessage("Route not active")
                        }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
    }
}

