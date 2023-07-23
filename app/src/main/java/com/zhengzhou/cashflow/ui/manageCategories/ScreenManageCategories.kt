package com.zhengzhou.cashflow.ui.manageCategories

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.zhengzhou.cashflow.NavigationCurrentScreen
import com.zhengzhou.cashflow.ui.BottomNavigationBar
import com.zhengzhou.cashflow.ui.SectionNavigationDrawerSheet
import com.zhengzhou.cashflow.ui.SectionTopAppBar

@Composable
fun ManageCategoriesScreen(
    currentScreen: NavigationCurrentScreen,
    setCurrentScreen: (NavigationCurrentScreen) -> Unit,
    navController: NavController
) {

    val manageCategoriesViewModel: ManageCategoriesViewModel = viewModel {
        ManageCategoriesViewModel()
    }
    val manageCategoriesUiState: ManageCategoriesUiState by manageCategoriesViewModel.uiState.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerContent = {
            SectionNavigationDrawerSheet(
                drawerState = drawerState,
                currentScreen = currentScreen,
                setCurrentScreen = setCurrentScreen,
                navController = navController,
            )
        },
        gesturesEnabled = drawerState.currentValue == DrawerValue.Open,
        drawerState = drawerState,
    ) {
        Scaffold(
            topBar = {
                SectionTopAppBar(
                    currentScreen = currentScreen,
                    drawerState = drawerState,
                    actions = {
                        SectionTopAppBar(
                            currentScreen = currentScreen,
                            drawerState = drawerState,
                        )
                    }
                )
            },
            content = { innerPadding ->
                ManageCategoriesMainBody(
                    manageCategoriesUiState = manageCategoriesUiState,
                    manageCategoriesViewModel = manageCategoriesViewModel,
                    innerPadding = innerPadding,
                    navController = navController,
                )
            },
            bottomBar = {
                BottomNavigationBar(
                    currentScreen = currentScreen,
                    setCurrentScreen = setCurrentScreen,
                    navController = navController
                )
            },
        )
    }
}

@Composable
fun ManageCategoriesMainBody(
    manageCategoriesUiState: ManageCategoriesUiState,
    manageCategoriesViewModel: ManageCategoriesViewModel,
    innerPadding: PaddingValues,
    navController: NavController,
) {

}