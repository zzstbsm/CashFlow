package com.zhengzhou.cashflow

import android.util.Log
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
import com.zhengzhou.cashflow.ui.walletEdit.WalletEditScreen
import com.zhengzhou.cashflow.ui.walletOverview.WalletOverviewScreen
import java.util.UUID

const val TAG = "NavigationApp"
const val testing = false

@Composable
fun NavigationApp() {

    var currentScreen by remember {
        mutableStateOf(NavigationCurrentScreen.Balance)
    }
    // val startDestination = Screen.Balance.route

    val startDestination = if (testing) {
        Screen.WalletEdit.route
    } else {
        Screen.Balance.route
    }

    // Set the navigation controller
    val navController = rememberNavController()
    NavHost(navController, startDestination = startDestination) {
        composable(route = Screen.Balance.route) {
            BalanceScreen(
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
            var walletUUIDStr = navBackStackEntry.arguments?.getString("walletUUIDStr")

            if (testing && walletUUIDStr == null) {
                walletUUIDStr = UUID(0L,0L).toString()
            } else if (!testing)  {
                requireNotNull(walletUUIDStr) {
                    "Exception: passed walletUUIDStr not valid"
                }
            }
            WalletEditScreen(
                walletUUID = UUID.fromString(walletUUIDStr),
                navController = navController,
            )
        }
        composable(route = Screen.WalletOverview.route) { navBackStackEntry ->

            val walletUUID = UUID.fromString(
                (navBackStackEntry.savedStateHandle.get<String?>(
                    Screen.WalletOverview.keyWalletUUID()
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
        /*
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

enum class NavigationCurrentScreen(
    @DrawableRes
    val iconId: Int = 0,
    @StringRes
    val optionName: Int,
    @StringRes
    val accessibilityText: Int? = null,
    val route: String,
    val routeActive: Boolean = true,
    val navBarActive: Boolean = false,
    val bottomActive: Boolean = false,
) {
    /*
     * The order here is the same shown in the navigation bar if enabled
     */
    // TODO: update all images
    Balance(
        iconId = R.drawable.ic_home,
        optionName = R.string.nav_name_balance,
        accessibilityText = R.string.accessibility_menu_navbar_balance,
        route = "Balance",
        navBarActive = true,
        bottomActive = true,
    ),
    WalletOverview(
        iconId = R.drawable.ic_wallet,
        optionName = R.string.nav_name_wallet,
        accessibilityText = R.string.accessibility_menu_navbar_overview,
        route = "Wallet",
        navBarActive = true,
        bottomActive = true,
    ),
    Profile(
        iconId = R.drawable.ic_account,
        optionName = R.string.nav_name_profile,
        accessibilityText = R.string.accessibility_menu_navbar_profile,
        route = "Profile",
        navBarActive = true,
        bottomActive = true,
    ),

    /*
     * Not present in the navigation bar
     */

    WalletEdit(
        optionName = R.string.nav_name_wallet_edit,
        accessibilityText = null,
        route = "WalletEdit",
    ),
    TransactionEdit(
        optionName = R.string.nav_name_transaction_edit,
        accessibilityText = null,
        route = "TransactionEdit",
        routeActive = false,
    ),
    TransactionReport(
        optionName = R.string.nav_name_transaction_report,
        accessibilityText = null,
        route = "TransactionReport",
        routeActive = false
    )
    ;
    fun navigateTab(navController: NavController) {
        navController.navigate(this.route)
    }

    companion object {
        val elements: List<NavigationCurrentScreen> = enumValues<NavigationCurrentScreen>().toList()
    }
}

sealed class Screen(
    val route: String,
    val screenEnum: NavigationCurrentScreen,
) {

    private fun createRoute() = this.route
    fun navigate(navController: NavController) {
        navController.navigate(createRoute())
    }

    object Balance: Screen(
        route = NavigationCurrentScreen.Balance.route,
        screenEnum = NavigationCurrentScreen.Balance,
    )
    object Profile: Screen(
        route = NavigationCurrentScreen.Profile.route,
        screenEnum = NavigationCurrentScreen.Profile,
    )
    object WalletEdit: Screen(
        route = NavigationCurrentScreen.WalletEdit.route +
                "/{walletUUIDStr}",
        screenEnum = NavigationCurrentScreen.WalletEdit,
    ) {
        private fun createRoute(
            walletID: UUID,
        ) : String {
            return NavigationCurrentScreen.WalletEdit.route +
                    "/${walletID}"
        }
        fun navigate(
            walletID: UUID,
            navController: NavController,
        ) {
            navController.navigate(
                createRoute(
                    walletID = walletID
                )
            )
        }
    }
    object WalletOverview: Screen(
        route = NavigationCurrentScreen.WalletOverview.route,
        screenEnum = NavigationCurrentScreen.Balance,
    ) {
        private fun createRoute(
            walletID: UUID,
        ) : String {
            return NavigationCurrentScreen.WalletEdit.route +
                    "/${walletID}"
        }
        fun keyWalletUUID() : String = "walletUUIDStr"
        fun navigate(
            walletID: UUID,
            navController: NavController,
        ) {
            navController.navigate(
                createRoute(
                    walletID = walletID
                )
            )
        }
    }

    object TransactionEdit: Screen(
        route = NavigationCurrentScreen.TransactionEdit.route +
                "/{walletUUIDStr}" +
                "/{transactionType}" +
                "/{transactionUUIDStr}",
        screenEnum = NavigationCurrentScreen.Balance,
    ) {
        fun createRoute(
            walletUUIDStr: String,
            transactionType: Int,
            transactionUUIDStr: String,
        ) = "${screenEnum.route}/$walletUUIDStr/$transactionType/$transactionUUIDStr"
    }
    object TransactionReport: Screen(
        route = NavigationCurrentScreen.TransactionReport.route +
                "/{transactionUUIDStr}",
        screenEnum = NavigationCurrentScreen.Balance,
    ) {
        fun createRoute(
            transactionUUIDStr: String
        ) = "{${screenEnum.route}}/$transactionUUIDStr"
    }
}

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