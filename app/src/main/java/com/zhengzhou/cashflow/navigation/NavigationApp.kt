package com.zhengzhou.cashflow.navigation

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zhengzhou.cashflow.R
import com.zhengzhou.cashflow.data.Currency
import com.zhengzhou.cashflow.data.TransactionType
import com.zhengzhou.cashflow.ui.aboutMe.AboutMeScreen
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
        mutableStateOf(NavigationCurrentScreen.Balance)
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
        composable(route = Screen.AllTransactions.route) {navBackStackEntry ->
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

enum class NavigationCurrentScreen(
    @DrawableRes
    val iconId: Int = 0,
    @StringRes
    val optionNameShort: Int = 0,
    @StringRes
    val optionName: Int,
    @StringRes
    val accessibilityText: Int? = null,
    val route: String,
    // Allow the route to be active from the navigation drawer or the navigation bottom bar
    val routeActive: Boolean = true,
    // Show navigation path on the navigation drawer
    val navBarActive: Boolean = false,
    // Show navigation path on the navigation bottom bar
    val bottomActive: Boolean = false,
) {
    /*
     * The order here is the same shown in the navigation bar if enabled
     */
    // TODO: update all images
    Balance(
        iconId = R.drawable.ic_home,
        optionName = R.string.nav_name_balance,
        optionNameShort = R.string.nav_name_balance,
        accessibilityText = R.string.accessibility_menu_navbar_balance,
        route = "Balance",
        navBarActive = true,
        bottomActive = true,
    ),
    WalletOverview(
        iconId = R.drawable.ic_wallet,
        optionName = R.string.nav_name_wallet,
        optionNameShort = R.string.nav_name_wallet,
        accessibilityText = R.string.accessibility_menu_navbar_overview,
        route = "Wallet",
        navBarActive = true,
        bottomActive = true,
    ),
    CommonTransactions(
        iconId = R.drawable.ic_send,
        optionName = R.string.nav_name_common_transactions,
        optionNameShort = R.string.nav_name_common_transactions_short,
        accessibilityText = R.string.accessibility_menu_navbar_common_transactions,
        route = "CommonTransactions",
        navBarActive = true,
        bottomActive = true,
    ),
    ManageCategories(
        iconId = R.drawable.ic_category,
        optionName = R.string.nav_name_manage_categories,
        optionNameShort = R.string.nav_name_manage_categories,
        accessibilityText = R.string.nav_name_manage_categories,
        route = "ManageCategories",
        navBarActive = true,
    ),
    Profile(
        iconId = R.drawable.ic_account,
        optionName = R.string.nav_name_profile,
        optionNameShort = R.string.nav_name_profile,
        accessibilityText = R.string.accessibility_menu_navbar_profile,
        route = "Profile",
        navBarActive = false,
        bottomActive = false,
    ),
    AboutMe(
        iconId = R.drawable.ic_info,
        optionName = R.string.nav_name_about_me,
        optionNameShort = R.string.nav_name_about_me,
        accessibilityText = R.string.nav_name_about_me,
        route = "AboutMe",
        navBarActive = true,
        bottomActive = false,
    ),

    /*
     * Not present in the navigation bar
     */
    AllTransactions(
        optionName = R.string.nav_name_all_transactions,
        accessibilityText = R.string.nav_name_all_transactions,
        route = "AllTransactions",
    ),
    TransactionEdit(
        optionName = R.string.nav_name_transaction_edit,
        accessibilityText = null,
        route = "TransactionEdit",
    ),
    TransactionReport(
        optionName = R.string.nav_name_transaction_report,
        accessibilityText = R.string.nav_name_transaction_report,
        route = "TransactionReport",
    ),
    WalletEdit(
        optionName = R.string.nav_name_wallet_edit,
        accessibilityText = R.string.nav_name_wallet_edit,
        route = "WalletEdit",
    ),
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
) {

    object AboutMe: Screen(
        route = NavigationCurrentScreen.AboutMe.route
    )
    object AllTransactions: Screen(
        route = NavigationCurrentScreen.AllTransactions.route +
                "/{${NavigationKeys.keyWalletUUID}}" +
                "/{${NavigationKeys.keyCategoryUUID}}" +
                "/{${NavigationKeys.keyCurrencyName}}"
    ) {
        private fun createRoute(
            walletUUID: UUID,
            categoryUUID: UUID,
            currency: Currency,
        ): String = NavigationCurrentScreen.AllTransactions.route +
                "/${walletUUID}/${categoryUUID}/${currency.name}"

        fun navigate(
            walletUUID: UUID = UUID(0L,0L),
            categoryUUID: UUID = UUID(0L,0L),
            currency: Currency,
            navController: NavController,
        ) {
            navController.navigate(
                createRoute(
                    walletUUID = walletUUID,
                    categoryUUID = categoryUUID,
                    currency = currency,
                )
            )
        }

    }
    object Balance: Screen(
        route = NavigationCurrentScreen.Balance.route,
    )
    object CommonTransactions: Screen(
        route = NavigationCurrentScreen.CommonTransactions.route,
    )
    object ManageCategories: Screen(
        route = NavigationCurrentScreen.ManageCategories.route,
    )
    object Profile: Screen(
        route = NavigationCurrentScreen.Profile.route,
    )

    object TransactionEdit: Screen(
        route = NavigationCurrentScreen.TransactionEdit.route +
                "/{${NavigationKeys.keyTransactionTypeID}}" +
                "/{${NavigationKeys.keyTransactionUUID}}" +
                "/{${NavigationKeys.keyCurrencyName}}" +
                "/{${NavigationKeys.keyIsBlueprint}}" +
                "/{${NavigationKeys.keyEditBlueprint}}",
    ) {
        private fun createRoute(
            transactionType: TransactionType,
            transactionUUID: UUID,
            currency: Currency,
            isBlueprint: Boolean,
            editBlueprint: Boolean,
        ) = NavigationCurrentScreen.TransactionEdit.route +
                "/${transactionType.id}/$transactionUUID/${currency.name}/$isBlueprint/$editBlueprint"

        fun navigate(
            transactionType: TransactionType,
            transactionUUID: UUID,
            currency: Currency,
            isBlueprint: Boolean,
            editBlueprint: Boolean,
            navController: NavController,
        )  {
            navController.navigate(
                createRoute(
                    transactionType = transactionType,
                    transactionUUID = transactionUUID,
                    currency = currency,
                    isBlueprint = isBlueprint,
                    editBlueprint = editBlueprint,
                )
            )
        }
    }
    object TransactionReport: Screen(
        route = NavigationCurrentScreen.TransactionReport.route +
                "/{${NavigationKeys.keyTransactionUUID}}",
    ) {
        private fun createRoute(
            transactionUUID: UUID,
        ) = NavigationCurrentScreen.TransactionReport.route +
                "/$transactionUUID"

        fun navigate(
            transactionUUID: UUID,
            navController: NavController,
        ) {
            navController.navigate(
                createRoute(
                    transactionUUID
                )
            )
        }
    }
    object WalletEdit: Screen(
        route = NavigationCurrentScreen.WalletEdit.route +
                "/{${NavigationKeys.keyWalletUUID}}",
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
    ) {
        const val handleKeyWalletUUID : String = "walletUUID"
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

@Composable
fun ReloadPageAfterPopBackStack(
    pageRoute:String,
    navController: NavController,
    onPopBackStack: () -> Unit,
) {
    val callback = NavController.OnDestinationChangedListener { _, destination, _ ->
        if (destination.route == pageRoute) {
            onPopBackStack()
        }
    }
    LaunchedEffect(navController) {
        navController.addOnDestinationChangedListener(callback)
    }
    DisposableEffect(Unit) {
        onDispose {
            navController.removeOnDestinationChangedListener(callback)
        }
    }
}