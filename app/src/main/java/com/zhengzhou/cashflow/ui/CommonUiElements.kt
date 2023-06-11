package com.zhengzhou.cashflow.ui

import androidx.compose.foundation.layout.Column
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
import com.zhengzhou.cashflow.R
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
                        RouteIcon(navigationCurrentScreen = item)
                    },
                    label = {
                        Text(stringResource(item.optionName))
                    },
                    selected = (currentScreen == item),
                    onClick = {
                        routeClick(
                            setCurrentScreen = setCurrentScreen,
                            navigationCurrentScreen = item,
                            navController = navController,
                        )
                    }
                )
            }
    }
}

@Composable
fun SectionNavigationDrawerSheet(
    drawerState: DrawerState,
    currentScreen: NavigationCurrentScreen,
    setCurrentScreen: (NavigationCurrentScreen) -> Unit,
    navController: NavController,
) {

    val scope = rememberCoroutineScope()

    ModalDrawerSheet {
        Column() {
            Text(text = stringResource(id = R.string.app_name))
            NavigationCurrentScreen.elements
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
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SectionTopAppBar(
    currentScreen: NavigationCurrentScreen,
    drawerState: DrawerState,
    modifier: Modifier = Modifier,
    pageName: String? = null,
) {

    val scope = rememberCoroutineScope()

    TopAppBar(
        title = {
            Text(text = pageName ?: stringResource(id = currentScreen.optionName))
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    scope.launch {
                        drawerState.open()
                    }
                },
                modifier = modifier,
            ) {
                Icon (
                    painter  = painterResource(id = R.drawable.ic_menu),
                    contentDescription = stringResource(id = R.string.accessibility_menu_navbar),
                )
            }
        }
    )
}

@Composable
private fun RouteIcon(
    navigationCurrentScreen : NavigationCurrentScreen,
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
    setCurrentScreen: (NavigationCurrentScreen) -> Unit,
    navigationCurrentScreen: NavigationCurrentScreen,
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