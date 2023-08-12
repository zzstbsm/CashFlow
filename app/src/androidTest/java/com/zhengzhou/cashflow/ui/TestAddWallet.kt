package com.zhengzhou.cashflow.ui

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.zhengzhou.cashflow.MainActivity
import com.zhengzhou.cashflow.NavigationCurrentScreen
import com.zhengzhou.cashflow.tools.IconsMappedForDB
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runners.MethodSorters

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class TestAddWallet {

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val testActions = TestDefaultActions(composeTestRule)

    private val walletName1 = "MyWallet"
    private val walletAmount1 = "100.53"

    private val walletName2 = "Wallet 2"
    private val walletAmount2 = "56.53"

    @Test
    fun o01_addWalletWhenZeroWallets() {

        testActions.navigateBottomBar(
            route = NavigationCurrentScreen.WalletOverview.route
        )

        testActions.checkZeroWallets()

        testActions.openAddWalletWhenZeroWallets()

        testActions.fillWalletAndSave(
            walletName = walletName1,
            walletAmount = walletAmount1,
            walletIcon = IconsMappedForDB.CARD
        )

        testActions.checkShownWallet(
            walletName = walletName1,
            walletAmount = walletAmount1,
        )

        return
    }

    @Test
    fun o02_addWalletWhenWalletExists() {

        testActions.navigateBottomBar(
            route = NavigationCurrentScreen.WalletOverview.route
        )

        testActions.openAddWalletWhenWalletsExist()
        testActions.fillWalletAndSave(
            walletName = walletName2,
            walletAmount = walletAmount2,
            walletIcon = IconsMappedForDB.WALLET,
        )

        testActions.checkShownWallet(
            walletName = walletName2,
            walletAmount = walletAmount2,
        )

        return
    }

    @Test
    fun o03_deleteWalletWhenTwoWallets() {

        testActions.navigateBottomBar(
            route = NavigationCurrentScreen.WalletOverview.route
        )

        testActions.checkExistWallet()
        testActions.checkShownWallet(
            walletName = walletName2,
            walletAmount = walletAmount2,
        )
        testActions.openDeleteWalletWhenWalletsExist()
        testActions.checkShownWallet(
            walletName = walletName1,
            walletAmount = walletAmount1,
        )

        return
    }

    @Test
    fun o04_deleteWalletWhenOneWallet() {

        testActions.navigateBottomBar(
            route = NavigationCurrentScreen.WalletOverview.route
        )

        testActions.checkShownWallet(
            walletName = walletName1,
            walletAmount = walletAmount1,
        )
        testActions.openDeleteWalletWhenWalletsExist()
        testActions.checkZeroWallets()

        return
    }
}