package com.zhengzhou.cashflow.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.navigation.NavController

enum class ApplicationScreensEnum(
    @DrawableRes
    val iconId: Int = 0,
    @StringRes
    val optionNameShort: Int = 0,
    @StringRes
    val optionName: Int,
    @StringRes
    val accessibilityText: Int? = null,
    /**
     * Allow the route to be active from the navigation drawer or the navigation bottom bar
     *
     * ***Default*** [routeActive] = true
     */
    val routeActive: Boolean = true,
    /**
     * Show navigation path on the navigation drawer
     *
     * ***Default*** [navBarActive] = false
     */
    val navBarActive: Boolean = false,
    /**
     * Show navigation path on the navigation bottom bar
     *
     * ***Default*** [bottomActive] = false
     */
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
        navBarActive = true,
        bottomActive = true,
    ),
    WalletOverview (
        iconId = R.drawable.ic_wallet,
        optionName = R.string.nav_name_wallet,
        optionNameShort = R.string.nav_name_wallet,
        accessibilityText = R.string.accessibility_menu_navbar_overview,
        navBarActive = true,
        bottomActive = true,
    ),
    CommonTransactions (
        iconId = R.drawable.ic_send,
        optionName = R.string.nav_name_common_transactions,
        optionNameShort = R.string.nav_name_common_transactions_short,
        accessibilityText = R.string.accessibility_menu_navbar_common_transactions,
        navBarActive = true,
        bottomActive = true,
    ),
    ManageCategories (
        iconId = R.drawable.ic_category,
        optionName = R.string.nav_name_manage_categories,
        optionNameShort = R.string.nav_name_manage_categories,
        accessibilityText = R.string.nav_name_manage_categories,
        navBarActive = true,
    ),
    ServerUi(
        iconId = R.drawable.ic_web,
        optionName = R.string.nav_name_server,
        optionNameShort = R.string.nav_name_server,
        accessibilityText = R.string.nav_name_server,
        navBarActive = true,
    ),
    Profile (
        iconId = R.drawable.ic_account,
        optionName = R.string.nav_name_profile,
        optionNameShort = R.string.nav_name_profile,
        accessibilityText = R.string.accessibility_menu_navbar_profile,
        navBarActive = false,
        bottomActive = false,
    ),
    AboutMe (
        iconId = R.drawable.ic_info,
        optionName = R.string.nav_name_about_me,
        optionNameShort = R.string.nav_name_about_me,
        accessibilityText = R.string.nav_name_about_me,
        navBarActive = true,
        bottomActive = false,
    ),

    /*
     * Not present in the navigation bar
     */
    AllTransactions (
        optionName = R.string.nav_name_all_transactions,
        accessibilityText = R.string.nav_name_all_transactions,
    ),
    TransactionEdit (
        optionName = R.string.nav_name_transaction_edit,
        accessibilityText = null,
    ),
    TransactionReport (
        optionName = R.string.nav_name_transaction_report,
        accessibilityText = R.string.nav_name_transaction_report,
    ),
    WalletEdit (
        optionName = R.string.nav_name_wallet_edit,
        accessibilityText = R.string.nav_name_wallet_edit,
    )
    ;
    fun navigateTab(navController: NavController) {
        navController.navigate(this.name)
    }

    companion object {
        val elements: List<ApplicationScreensEnum> = enumValues<ApplicationScreensEnum>().toList()
    }
}