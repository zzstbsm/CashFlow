package com.zhengzhou.cashflow.ui

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.zhengzhou.cashflow.MainActivity
import com.zhengzhou.cashflow.data.Currency
import com.zhengzhou.cashflow.navigation.NavigationAppTestTag
import com.zhengzhou.cashflow.themes.IconsMappedForDB
import com.zhengzhou.cashflow.ui.walletEdit.WalletEditTestTag
import com.zhengzhou.cashflow.ui.walletOverview.WalletOverviewTestTag
import java.text.NumberFormat

class TestDefaultActions(
    private val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>
) {

    private val currencyFormatter: NumberFormat = Currency.setCurrencyFormatter(Currency.EUR.name)

    fun fillWalletAndSave(
        walletName: String,
        walletAmount: String,
        walletIcon: IconsMappedForDB,
    ) {

        composeTestRule.onNodeWithTag(
            testTag = WalletEditTestTag.TAG_ICON_FIELD_ICON
        ).performClick()

        composeTestRule.onNodeWithTag(
            testTag = walletIcon.name
        ).performClick()

        composeTestRule.onNodeWithTag(
            testTag = WalletEditTestTag.TAG_TEST_FIELD_WALLET_NAME
        ).performTextClearance()

        composeTestRule.onNodeWithTag(
            testTag = WalletEditTestTag.TAG_TEST_FIELD_WALLET_NAME
        ).performTextInput(
            walletName
        )

        composeTestRule.onNodeWithTag(
            testTag = WalletEditTestTag.TAG_TEST_FIELD_WALLET_AMOUNT
        ).performTextClearance()


        composeTestRule.onNodeWithTag(
            testTag = WalletEditTestTag.TAG_TEST_FIELD_WALLET_AMOUNT
        ).performTextInput(
            walletAmount
        )

        composeTestRule.onNodeWithTag(
            testTag = WalletEditTestTag.TAG_FLOATING_ACTION_BUTTON
        ).performClick()
    }
    fun fillWalletExpectingError(
            walletName: String,
            walletAmount: String,
            walletIcon: IconsMappedForDB,
        ) {

        composeTestRule.onNodeWithTag(
            testTag = WalletEditTestTag.TAG_ICON_FIELD_ICON
        ).performClick()

        composeTestRule.onNodeWithTag(
            testTag = walletIcon.name
        ).performClick()

        composeTestRule.onNodeWithTag(
            testTag = WalletEditTestTag.TAG_TEST_FIELD_WALLET_NAME
        ).performTextClearance()

        composeTestRule.onNodeWithTag(
            testTag = WalletEditTestTag.TAG_TEST_FIELD_WALLET_NAME
        ).performTextInput(
            walletName
        )

        composeTestRule.onNodeWithTag(
            testTag = WalletEditTestTag.TAG_TEST_FIELD_WALLET_AMOUNT
        ).performTextClearance()


        composeTestRule.onNodeWithTag(
            testTag = WalletEditTestTag.TAG_TEST_FIELD_WALLET_AMOUNT
        ).performTextInput(
            walletAmount
        )

        composeTestRule.onNodeWithTag(
            testTag = WalletEditTestTag.TAG_FLOATING_ACTION_BUTTON
        ).performClick()
    }

    fun checkExistWallet() {
        composeTestRule.onNodeWithTag(
            testTag = WalletOverviewTestTag.TAG_FLOATING_ACTION_BUTTON
        ).assertTextEquals("Select wallet")
    }

    fun checkError(
        testTag: String,
        errorMessage: String,
    ) {
        composeTestRule.onNodeWithTag(
            testTag = testTag,
            useUnmergedTree = true,
        ).assertTextEquals(errorMessage)
    }

    fun checkShownWallet(
        walletName: String,
        walletAmount: String,
    ) {

        composeTestRule.onNodeWithTag(
            testTag = WalletOverviewTestTag.TAG_TEXT_WALLET_NAME
        ).assertTextEquals(walletName)
        composeTestRule.onNodeWithTag(
            testTag = WalletOverviewTestTag.TAG_TEXT_WALLET_AMOUNT
        ).assertTextEquals(
            Currency.formatCurrency(currencyFormatter, walletAmount.toFloat())
        )
    }

    fun checkZeroWallets() {

        composeTestRule.onNodeWithTag(
            testTag = WalletOverviewTestTag.TAG_FLOATING_ACTION_BUTTON
        ).assertTextEquals("Add wallet")

        composeTestRule.onNodeWithTag(
            WalletOverviewTestTag.TAG_TEXT_WALLET_NAME
        ).assertTextEquals("No wallet")
        composeTestRule.onNodeWithTag(
            WalletOverviewTestTag.TAG_TEXT_WALLET_AMOUNT
        ).assertTextEquals(Currency.formatCurrency(currencyFormatter, 0f))

    }

    fun openAddWalletWhenZeroWallets() {
        composeTestRule.onNodeWithTag(
            testTag = WalletOverviewTestTag.TAG_FLOATING_ACTION_BUTTON
        ).assertTextEquals("Add wallet")
        composeTestRule.onNodeWithTag(
            testTag = WalletOverviewTestTag.TAG_FLOATING_ACTION_BUTTON
        ).performClick()
    }

    fun openAddWalletWhenWalletsExist() {
        composeTestRule.onNodeWithTag(
            testTag = WalletOverviewTestTag.TAG_TOP_APP_BAR_OPEN_MENU
        ).performClick()
        composeTestRule.onNodeWithTag(
            testTag = WalletOverviewTestTag.TAG_DROP_DOWN_MENU_ADD_WALLET
        ).performClick()
    }

    fun openDeleteWalletWhenWalletsExist() {
        composeTestRule.onNodeWithTag(
            testTag = WalletOverviewTestTag.TAG_TOP_APP_BAR_OPEN_MENU
        ).performClick()
        composeTestRule.onNodeWithTag(
            testTag = WalletOverviewTestTag.TAG_DROP_DOWN_MENU_DELETE_WALLET
        ).performClick()
    }

    fun openEditWallet() {
        composeTestRule.onNodeWithTag(
            testTag = WalletOverviewTestTag.TAG_TOP_APP_BAR_ACTION_EDIT_WALLET
        ).performClick()
    }

    fun navigateBottomBar(
        route: String,
    ) {
        composeTestRule.onNodeWithTag(
            NavigationAppTestTag.bottomNavBar(route)
        ).performClick()
    }

    fun navigateDrawer(
        route: String,
    ) {
        composeTestRule.onNodeWithTag(
            testTag = NavigationAppTestTag.TAG_OPEN_NAV_DRAWER
        ).performClick()
        composeTestRule.onNodeWithTag(
            NavigationAppTestTag.drawerNavBar(route)
        ).performClick()
    }

}