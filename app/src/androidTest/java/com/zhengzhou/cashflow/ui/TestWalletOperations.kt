package com.zhengzhou.cashflow.ui

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.zhengzhou.cashflow.MainActivity
import com.zhengzhou.cashflow.navigation.ApplicationScreensEnum
import com.zhengzhou.cashflow.themes.IconsMappedForDB
import com.zhengzhou.cashflow.ui.walletEdit.WalletEditTestTag
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runners.MethodSorters

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class TestWalletOperations {

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val testActions = TestDefaultActions(composeTestRule)

    private val walletName1 = "MyWallet"
    private val walletAmount1 = "100.53"

    private val walletName2 = "Wallet 2"
    private val walletName2Edited = "Abracadabra"
    private val walletAmount2 = "56.53"
    private val walletAmount2Edited = "156.53"

    private val walletName3 = "Master"
    private val walletAmount3 = "0.53"
    private val walletAmount3Wrong = "3ad.53"


    @Test
    fun o01_addWalletWhenZeroWallets() {

        testActions.navigateBottomBar(
            route = ApplicationScreensEnum.WalletOverview.route
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
            route = ApplicationScreensEnum.WalletOverview.route
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
    fun o03_editWallet() {

        testActions.navigateBottomBar(
            route = ApplicationScreensEnum.WalletOverview.route
        )

        testActions.checkExistWallet()
        testActions.checkShownWallet(
            walletName = walletName2,
            walletAmount = walletAmount2,
        )
        testActions.openEditWallet()

        testActions.fillWalletAndSave(
            walletName = walletName2Edited,
            walletAmount = walletAmount2Edited,
            walletIcon = IconsMappedForDB.WALLET,
        )

        testActions.checkShownWallet(
            walletName = walletName2Edited,
            walletAmount = walletAmount2Edited,
        )
    }

    @Test
    fun o04_insertNonValidData() {
        testActions.navigateBottomBar(
            route = ApplicationScreensEnum.WalletOverview.route
        )
        testActions.checkShownWallet(
            walletName = walletName2Edited,
            walletAmount = walletAmount2Edited,
        )

        testActions.openAddWalletWhenWalletsExist()

        testActions.fillWalletExpectingError(
            walletName = " ",
            walletAmount = walletAmount3,
            walletIcon = IconsMappedForDB.WALLET,
        )
        testActions.checkError(
            testTag = WalletEditTestTag.TAG_TEST_FIELD_WALLET_NAME_SUPPORTING_TEXT,
            errorMessage = "Wallet name not valid"
        )

        testActions.fillWalletExpectingError(
            walletName = walletName1,
            walletAmount = walletAmount3,
            walletIcon = IconsMappedForDB.CARD,
        )
        testActions.checkError(
            testTag = WalletEditTestTag.TAG_TEST_FIELD_WALLET_NAME_SUPPORTING_TEXT,
            errorMessage = "Wallet name already in use"
        )

        testActions.fillWalletExpectingError(
            walletName = walletName3,
            walletAmount = walletAmount3Wrong,
            walletIcon = IconsMappedForDB.WALLET,
        )
        testActions.checkError(
            testTag = WalletEditTestTag.TAG_TEST_FIELD_WALLET_AMOUNT_SUPPORTING_TEXT,
            errorMessage = "Inserted amount not valid"
        )

        testActions.fillWalletAndSave(
            walletName = walletName3,
            walletAmount = walletAmount3,
            walletIcon = IconsMappedForDB.WALLET,
        )

        testActions.checkShownWallet(
            walletName = walletName3,
            walletAmount = walletAmount3
        )
    }

    @Test
    fun o05_deleteWalletWhenThreeWallets() {

        testActions.navigateBottomBar(
            route = ApplicationScreensEnum.WalletOverview.route
        )

        testActions.checkExistWallet()
        testActions.checkShownWallet(
            walletName = walletName3,
            walletAmount = walletAmount3,
        )
        testActions.openDeleteWalletWhenWalletsExist()
        testActions.checkShownWallet(
            walletName = walletName2Edited,
            walletAmount = walletAmount2Edited,
        )

        return
    }

    @Test
    fun o06_deleteWalletWhenTwoWallets() {

        testActions.navigateBottomBar(
            route = ApplicationScreensEnum.WalletOverview.route
        )

        testActions.checkExistWallet()
        testActions.checkShownWallet(
            walletName = walletName2Edited,
            walletAmount = walletAmount2Edited,
        )
        testActions.openDeleteWalletWhenWalletsExist()
        testActions.checkShownWallet(
            walletName = walletName1,
            walletAmount = walletAmount1,
        )

        return
    }

    @Test
    fun o07_deleteWalletWhenOneWallet() {

        testActions.navigateBottomBar(
            route = ApplicationScreensEnum.WalletOverview.route
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