package com.zhengzhou.cashflow.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zhengzhou.cashflow.about_me.AboutMeScreen
import com.zhengzhou.cashflow.data.Currency
import com.zhengzhou.cashflow.data.TransactionType
import com.zhengzhou.cashflow.ui.allTransactions.AllTransactionsScreen
import com.zhengzhou.cashflow.ui.balance.BalanceScreen
import com.zhengzhou.cashflow.ui.commonTransactions.CommonTransactionsScreen
import com.zhengzhou.cashflow.ui.manageCategories.ManageCategoriesScreen
import com.zhengzhou.cashflow.ui.profile.ProfileScreen
import com.zhengzhou.cashflow.ui.transactionEdit.TransactionEditScreen
import com.zhengzhou.cashflow.ui.transactionReport.TransactionReportScreen
import com.zhengzhou.cashflow.ui.walletEdit.WalletEditScreen
import com.zhengzhou.cashflow.ui.walletOverview.WalletOverviewScreen
import java.util.UUID

@Composable
fun NavigationApp() {

    var currentScreen by remember {
        mutableStateOf(ApplicationScreensEnum.Balance)
    }
    val startDestination = Screen.Balance.route

    // Set the navigation controller
    val navController = rememberNavController()
    NavHost(navController, startDestination = startDestination) {
        composable(route = Screen.AboutMe.route) {
            AboutMeScreen(
                currentScreen = currentScreen,
                setCurrentScreen = { screen ->
                    currentScreen = screen
                },
                navController = navController,
            )
        }
        composable(route = Screen.AllTransactions.route) { navBackStackEntry ->
            val walletUUID = navBackStackEntry.arguments?.getString(NavigationKeys.keyWalletUUID)
            val categoryUUID = navBackStackEntry.arguments?.getString(NavigationKeys.keyCategoryUUID)
            val currency = Currency.setCurrency(
                name = navBackStackEntry.arguments?.getString(NavigationKeys.keyCurrencyName).toString()
            )
            requireNotNull(walletUUID) {
                "Exception: passed transactionType not valid"
            }
            requireNotNull(categoryUUID) {
                "Exception: passed updateTransaction not valid"
            }
            requireNotNull(currency) {
                "Exception: passed currency not valid"
            }

            AllTransactionsScreen(
                walletUUID = UUID.fromString(walletUUID),
                categoryUUID = UUID.fromString(categoryUUID),
                currency = currency,
                navController = navController,
            )
        }
        composable(route = Screen.Balance.route) {
            BalanceScreen(
                currentScreen = currentScreen,
                setCurrentScreen = { screen ->
                    currentScreen = screen
                },
                navController = navController,
            )
        }
        composable(route = Screen.CommonTransactions.route) {
            CommonTransactionsScreen(
                currentScreen = currentScreen,
                setCurrentScreen = { screen ->
                    currentScreen = screen
                },
                navController = navController,
            )
        }
        composable(route = Screen.ManageCategories.route) {
            ManageCategoriesScreen(
                currentScreen = currentScreen,
                setCurrentScreen = { screen ->
                    currentScreen = screen
                },
                navController = navController,
            )
        }
        composable(route = Screen.Profile.route) {
            ProfileScreen(
                currentScreen = currentScreen,
                setCurrentScreen = { screen ->
                    currentScreen = screen
                },
                navController = navController,
            )
        }
        composable(route = Screen.WalletEdit.route) { navBackStackEntry ->
            val walletUUID = navBackStackEntry.arguments?.getString(NavigationKeys.keyWalletUUID)

            WalletEditScreen(
                walletUUID = UUID.fromString(walletUUID),
                navController = navController,
            )
        }
        composable(route = Screen.WalletOverview.route) { navBackStackEntry ->

            val walletUUID = UUID.fromString(
                (navBackStackEntry.savedStateHandle.get<String?>(
                    NavigationKeys.keyWalletUUID
                ) ?: UUID(0L,0L)).toString()
            )

            WalletOverviewScreen(
                walletUUID = walletUUID,
                currentScreen = currentScreen,
                setCurrentScreen = { screen ->
                    currentScreen = screen
                },
                navController = navController,
            )
        }
        composable(route = Screen.TransactionEdit.route) { backStackEntry ->
            val transactionType = TransactionType.setTransaction(
                id = backStackEntry.arguments?.getString(NavigationKeys.keyTransactionTypeID)?.toInt()
            )
            val transactionUUID = backStackEntry.arguments?.getString(NavigationKeys.keyTransactionUUID)
            val currency = Currency.setCurrency(
                name = backStackEntry.arguments?.getString(NavigationKeys.keyCurrencyName).toString()
            )
            val isBlueprint = backStackEntry.arguments?.getString(NavigationKeys.keyIsBlueprint).toBoolean()
            val editBlueprint = backStackEntry.arguments?.getString(NavigationKeys.keyEditBlueprint).toBoolean()
            requireNotNull(transactionType) {
                "Exception: passed transactionType not valid"
            }
            requireNotNull(transactionUUID) {
                "Exception: passed updateTransaction not valid"
            }
            requireNotNull(currency) {
                "Exception: passed currency not valid"
            }
            TransactionEditScreen(
                transactionType = transactionType,
                transactionUUID = UUID.fromString(transactionUUID),
                currency = currency,
                isBlueprint = isBlueprint,
                editBlueprint = editBlueprint,
                navController = navController,
            )
        }
        composable(route = Screen.TransactionReport.route) { backStackEntry ->
            val transactionUUID = backStackEntry.arguments?.getString(NavigationKeys.keyTransactionUUID)
            requireNotNull(transactionUUID) {
                "Exception: passed walletUUIDStr not valid"
            }
            TransactionReportScreen(
                transactionUUID = UUID.fromString(transactionUUID),
                navController = navController
            )
        }
    }
}