package com.zhengzhou.cashflow.themes.ui_elements.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.zhengzhou.cashflow.navigation.ApplicationScreensEnum
import com.zhengzhou.cashflow.navigation.NavigationAppTestTag
import com.zhengzhou.cashflow.navigation.R
import com.zhengzhou.cashflow.navigation.functions.routeClick
import kotlinx.coroutines.launch

@Composable
fun SectionNavigationDrawerSheet(
    drawerState: DrawerState,
    currentScreen: ApplicationScreensEnum,
    setCurrentScreen: (ApplicationScreensEnum) -> Unit,
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
            ApplicationScreensEnum.elements
                .filter { item ->
                    item.navBarActive
                }
                .forEach { item ->
                    NavigationDrawerItem(
                        icon = {
                            RouteIcon(applicationScreensEnum = item)
                        },
                        label = { Text(stringResource(id = item.optionName)) },
                        selected = item == currentScreen,
                        onClick = {
                            scope.launch { drawerState.close() }
                            routeClick(
                                setCurrentScreen = setCurrentScreen,
                                applicationScreensEnum = item,
                                navController = navController,
                            )
                        },
                        modifier = Modifier
                            .testTag(
                                NavigationAppTestTag.drawerNavBar(item.name)
                            )
                            .padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }

        }
    }
}