package com.zhengzhou.cashflow.profile

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.zhengzhou.cashflow.navigation.ApplicationScreensEnum
import com.zhengzhou.cashflow.navigation.Screen
import com.zhengzhou.cashflow.navigation.functions.ReloadPageAfterPopBackStack
import com.zhengzhou.cashflow.themes.ui_elements.navigation.BottomNavigationBar
import com.zhengzhou.cashflow.themes.ui_elements.navigation.SectionNavigationDrawerSheet
import com.zhengzhou.cashflow.themes.ui_elements.navigation.SectionTopAppBar

@Composable
fun ProfileScreen(
    currentScreen: ApplicationScreensEnum,
    setCurrentScreen: (ApplicationScreensEnum) -> Unit,
    navController: NavController,
) {
    val profileViewModel: ProfileViewModel = viewModel()
    val profileUiState by profileViewModel.uiState.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ReloadPageAfterPopBackStack(
        pageRoute = Screen.Profile.route,
        navController = navController,
    ) {
        setCurrentScreen(ApplicationScreensEnum.Profile)
    }

    ModalNavigationDrawer(
        drawerContent = {
            SectionNavigationDrawerSheet(
                drawerState = drawerState,
                currentScreen = currentScreen,
                setCurrentScreen = setCurrentScreen,
                navController = navController
            )
        },
        gesturesEnabled = drawerState.currentValue == DrawerValue.Open,
        drawerState = drawerState,
    ) {
        Scaffold (
            topBar = {
                SectionTopAppBar(
                    currentScreen = currentScreen,
                    drawerState = drawerState,
                )
            },
            content = { paddingValues ->
                ScreenProfileMainBody(
                    modifier = Modifier.padding(paddingValues)
                )
            },
            bottomBar = {
                BottomNavigationBar(
                    currentScreen = currentScreen,
                    setCurrentScreen = setCurrentScreen,
                    navController = navController,
                )
            }
        )
    }
}

@Composable
fun ScreenProfileMainBody(
    modifier: Modifier = Modifier,
) {

}