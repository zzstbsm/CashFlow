package com.zhengzhou.cashflow

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zhengzhou.cashflow.ui.balance.BalanceScreen
import com.zhengzhou.cashflow.ui.profile.ProfileScreen
import com.zhengzhou.cashflow.ui.walletEdit.WalletEditOption
import com.zhengzhou.cashflow.ui.walletEdit.WalletEditScreen
import com.zhengzhou.cashflow.ui.walletOverview.WalletOverviewScreen
import java.io.Serializable

const val TAG = "NavigationApp"

@Composable
fun NavigationApp() {

    var currentScreen by remember {
        mutableStateOf(NavigationCurrentScreen.Balance)
    }

    // Set the navigation controller
    val navController = rememberNavController()
    NavHost(navController, startDestination = Screen.Balance.route) {
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
        composable(route = Screen.WalletOverview.route) {
            WalletOverviewScreen(
                currentScreen = currentScreen,
                setCurrentScreen = { screen ->
                    currentScreen = screen
                },
                navController = navController,
            )
        }
        composable(route = Screen.WalletEdit.route) { navBackStackEntry ->
            Log.d(TAG,"Entering the navigation composable of WalletEdit")
            val walletEditOption = getSerializable(
                bundle = navBackStackEntry.arguments,
                key = "walletEditOption",
                m_class = WalletEditOption::class.java,
            )
            Log.d(TAG,"WalletEditOption retrieved")
            requireNotNull(walletEditOption) {
                "Exception: passed walletEditOption not valid"
            }
            Log.d(TAG,"WalletEditOption not null")
            WalletEditScreen(
                walletEditOption = walletEditOption,
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
    val iconId: Int,
    @StringRes
    val optionName: Int,
    @StringRes
    val accessibilityText: Int,
    val route: String,
    val routeActive: Boolean,
    val navBarActive: Boolean,
    val bottomActive: Boolean,
) {
    // TODO: update all images
    Balance(
        iconId = R.drawable.ic_home,
        optionName = R.string.nav_name_balance,
        accessibilityText = R.string.accessibility_menu_navbar_balance,
        route = "Balance",
        routeActive = true,
        navBarActive = true,
        bottomActive = true,
    ),
    WalletEdit(
        iconId = 0,
        optionName = R.string.wallet_edit,
        accessibilityText = 0,
        route = "WalletEdit",
        routeActive = true,
        navBarActive = false,
        bottomActive = false,
    ),
    WalletOverview(
        iconId = R.drawable.ic_wallet,
        optionName = R.string.nav_name_wallet,
        accessibilityText = R.string.accessibility_menu_navbar_overview,
        route = "Wallet",
        routeActive = true,
        navBarActive = true,
        bottomActive = true,
    ),
    Profile(
        iconId = R.drawable.ic_account,
        optionName = R.string.nav_name_profile,
        accessibilityText = R.string.accessibility_menu_navbar_profile,
        route = "Profile",
        routeActive = true,
        navBarActive = true,
        bottomActive = true,
    );

    companion object {
        val elements: List<NavigationCurrentScreen> = enumValues<NavigationCurrentScreen>().toList()
    }

    fun navigate(
        route: String = this.route,
        navController: NavController
    ) {
        navController.navigate(route)
    }
}

sealed class Screen(val route: String) {
    object Balance: Screen(
        route = NavigationCurrentScreen.Balance.route
    )
    object Profile: Screen(
        route = NavigationCurrentScreen.Profile.route
    )
    object WalletEdit: Screen(
        route = NavigationCurrentScreen.WalletEdit.route +
                "/{walletEditOption}"
    ) {
        fun createRoute(
            walletEditOption: WalletEditOption,
        ) = NavigationCurrentScreen.WalletEdit.route +
                "/${walletEditOption.name}"
    }
    object WalletOverview: Screen(
        route = NavigationCurrentScreen.WalletOverview.route
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

@Suppress("DEPRECATION")
fun <T : Serializable?> getSerializable(bundle: Bundle?, key: String, m_class: Class<T>): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        bundle?.getSerializable(key, m_class)!!
    else
        bundle?.getSerializable(key) as? T?
}
