package com.zhengzhou.cashflow

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zhengzhou.cashflow.ui.balance.BalanceScreen
/*
import com.zhengzhou.cashflow.data.TransactionType
import com.zhengzhou.cashflow.ui.balanceOverviewScreen.BalanceOverviewScreen
import com.zhengzhou.cashflow.ui.operationScreen.OperationScreen
import com.zhengzhou.cashflow.ui.transactionReportScreen.TransactionReportScreen
import com.zhengzhou.cashflow.ui.walletOverviewScreen.WalletOverviewScreen
 */
import java.util.*

@Composable
fun NavigationApp() {

    // Set the navigation controller
    val navController = rememberNavController()
    NavHost(navController, startDestination = Screen.Balance.route) {
        composable(route = Screen.Balance.route) {
            BalanceScreen(navController = navController)
        }
        /*
        composable(route = Screen.WalletOverview.route) {
            WalletOverviewScreen(navController = navController)
        }
        composable(route = Screen.BalanceOverview.route) {
            BalanceOverviewScreen(navController = navController)
        }
        composable(route = Screen.Operation.route) {backStackEntry ->
            val walletUUIDStr = backStackEntry.arguments?.getString("walletUUIDStr")
            val transactionType = backStackEntry.arguments?.getString("transactionType")
            val transactionUUIDStr = backStackEntry.arguments?.getString("transactionUUIDStr")
            requireNotNull(walletUUIDStr) {
                "Exception: passed walletUUIDStr not valid"
            }
            requireNotNull(transactionType) {
                "Exception: passed transactionType not valid"
            }
            requireNotNull(transactionUUIDStr) {
                "Exception: passed updateTransaction not valid"
            }
            OperationScreen(
                walletUUIDStr = walletUUIDStr,
                transactionTypeId = transactionType.toInt(),
                transactionUUIDStr = transactionUUIDStr,
                navController = navController
            )
        }
        composable(route = Screen.TransactionReport.route) {backStackEntry ->
            val transactionUUIDStr = backStackEntry.arguments?.getString("transactionUUIDStr")
            requireNotNull(transactionUUIDStr) {
                "Exception: passed walletUUIDStr not valid"
            }
            TransactionReportScreen(
                transactionUUIDStr = transactionUUIDStr,
                navController = navController
            )
        }
        */
    }
}

sealed class Screen(val route: String) {
    object Balance: Screen("Balance")
    object TransactionEdit: Screen(
        "TransactionEdit" +
                "/{walletUUIDStr}" +
                "/{transactionType}" +
                "/{transactionUUIDStr}"
    ) {
        fun createRoute(
            walletUUIDStr: String,
            transactionType: Int,
            transactionUUIDStr: String,
        ) = "Operation/$walletUUIDStr/$transactionType/$transactionUUIDStr"
    }
    object TransactionReport: Screen(
        "TransactionReport" +
                "/{transactionUUIDStr}"
    ) {
        fun createRoute(
            transactionUUIDStr: String
        ) = "TransactionReport/$transactionUUIDStr"
    }
    object WalletEdit: Screen("WalletEdit")
}

/*
fun navToOperation(
    walletUUID: UUID,
    transactionType: TransactionType,
    transactionUUID: UUID,
    navController: NavController,
) {
    navController.navigate(
        Screen.Operation.createRoute(
            walletUUIDStr = walletUUID.toString(),
            transactionType = transactionType.id,
            transactionUUIDStr = transactionUUID.toString()
        )
    )
}

fun navToTransactionReport(
    transactionUUID: UUID,
    navController: NavController,
) {
    navController.navigate(
        Screen.TransactionReport.createRoute(
            transactionUUIDStr = transactionUUID.toString(),
        )
    )
}

 */

@Composable
fun BackHandler(
    enabled: Boolean = true,
    onBack: () -> Unit,
) {
    val currentOnBack by rememberUpdatedState(onBack)
    val backCallback = remember {
        object : OnBackPressedCallback(enabled = enabled) {
            override fun handleOnBackPressed() {
                currentOnBack()
            }
        }
    }
    SideEffect {
        backCallback.isEnabled = enabled
    }
    val backDispatcher = checkNotNull(
        LocalOnBackPressedDispatcherOwner.current
    ) {
        "No OnBackPressedDispatcherOwner was provided via LocalOnBackPressedDispatcherOwner"
    }.onBackPressedDispatcher
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, backDispatcher) {
        backDispatcher.addCallback(lifecycleOwner,backCallback)
        onDispose {
            backCallback.remove()
        }
    }
}