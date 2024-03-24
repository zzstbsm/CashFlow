package com.zhengzhou.cashflow.total_balance

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.zhengzhou.cashflow.data.Transaction
import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import com.zhengzhou.cashflow.navigation.ApplicationScreensEnum
import com.zhengzhou.cashflow.navigation.Screen
import com.zhengzhou.cashflow.navigation.functions.ReloadPageAfterPopBackStack
import com.zhengzhou.cashflow.tools.ui_elements.navigation.BottomNavigationBar
import com.zhengzhou.cashflow.tools.ui_elements.navigation.SectionNavigationDrawerSheet
import com.zhengzhou.cashflow.tools.ui_elements.navigation.SectionTopAppBar
import com.zhengzhou.cashflow.total_balance.view_model.BalanceViewModel

@Composable
fun BalanceScreen(
    repository: RepositoryInterface,
    currentScreen: ApplicationScreensEnum,
    setCurrentScreen: (ApplicationScreensEnum) -> Unit,
    navController: NavController
) {

    val balanceViewModel: BalanceViewModel = viewModel {
        BalanceViewModel(repository = repository)
    }
    val balanceUiState by balanceViewModel.uiState.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ReloadPageAfterPopBackStack(
        pageRoute = Screen.Balance.route,
        navController = navController,
    ) {
        setCurrentScreen(ApplicationScreensEnum.Balance)
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
        Scaffold(
            topBar = {
                SectionTopAppBar(
                    currentScreen = currentScreen,
                    drawerState = drawerState,
                )
            },
            content = { innerPadding ->
                BalanceMainBody(
                    balanceUiState = balanceUiState,
                    balanceViewModel = balanceViewModel,
                    innerPaddingValues = innerPadding,
                    navController = navController,
                )
            },
            bottomBar = {
                BottomNavigationBar(
                    currentScreen = currentScreen,
                    setCurrentScreen = setCurrentScreen,
                    navController = navController,
                )
            },
            floatingActionButton = {
                if (!balanceUiState.isLoading) {
                    BalanceFloatingActionButtons(
                        wallet = balanceUiState.getLastWallet(),
                        transaction = Transaction.newEmpty(),
                        navController = navController,
                    )
                }
            },
        )
    }
}
