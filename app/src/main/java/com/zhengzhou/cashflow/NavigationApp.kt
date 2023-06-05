package com.zhengzhou.cashflow

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zhengzhou.cashflow.ui.balance.BalanceScreen
import com.zhengzhou.cashflow.ui.profile.ProfileScreen

@Composable
fun NavigationApp() {

    var bottomOptionCurrentScreen by remember {
        mutableStateOf(BottomOptionCurrentScreen.Balance)
    }

    // Set the navigation controller
    val navController = rememberNavController()
    NavHost(navController, startDestination = Screen.Balance.route) {
        composable(route = Screen.Balance.route) {
            BalanceScreen(
                bottomOptionCurrentScreen = bottomOptionCurrentScreen,
                setBottomOptionCurrentScreen = { screen ->
                    bottomOptionCurrentScreen = screen
                },
                navController = navController,
            )
        }
        composable(route = Screen.Profile.route) {
            ProfileScreen(
                bottomOptionCurrentScreen = bottomOptionCurrentScreen,
                setBottomOptionCurrentScreen = { screen ->
                    bottomOptionCurrentScreen = screen
                },
                navController = navController,
            )
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

enum class BottomOptionCurrentScreen(
    @DrawableRes
    val iconId: Int,
    @StringRes
    val optionName: Int,
    @StringRes
    val accessibilityText: Int,
    val route: String,
) {
    // TODO: update all images
    Balance(
        iconId = R.drawable.ic_home,
        optionName = R.string.bottom_bar_balance,
        accessibilityText = R.string.accessibility_menu_navbar_balance,
        route = "Balance"
    ),
    Overview(
        iconId = R.drawable.ic_trending_up,
        optionName = R.string.bottom_bar_overview,
        accessibilityText = R.string.accessibility_menu_navbar_overview,
        route = "Overview"
    ),
    Profile(
        iconId = R.drawable.ic_account,
        optionName = R.string.bottom_bar_profile,
        accessibilityText = R.string.accessibility_menu_navbar_profile,
        route = "Profile"
    );

    companion object {
        val elements: List<BottomOptionCurrentScreen> = listOf(
            Balance,
            // Overview,
            Profile,
        )
    }

    fun navigate(
        navController: NavController
    ) {
        navController.navigate(route)
    }
}

sealed class Screen(val route: String) {
    object Balance: Screen(
        route = BottomOptionCurrentScreen.Balance.route
    )
    object Profile: Screen(
        route = BottomOptionCurrentScreen.Profile.route
    )

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