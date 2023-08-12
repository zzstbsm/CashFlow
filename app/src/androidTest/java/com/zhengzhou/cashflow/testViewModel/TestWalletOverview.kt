package com.zhengzhou.cashflow.testViewModel

import android.annotation.SuppressLint
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.database.DatabaseRepository
import com.zhengzhou.cashflow.database.shared.databaseRepositoryInitializer
import com.zhengzhou.cashflow.ui.walletOverview.WalletOverviewUiState
import com.zhengzhou.cashflow.ui.walletOverview.WalletOverviewViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class TestWalletOverview {

    private lateinit var repository: DatabaseRepository
    private lateinit var wallet1: Wallet
    private lateinit var wallet2: Wallet
    private lateinit var wallet3: Wallet
    private lateinit var wallet4: Wallet

     @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        repository = DatabaseRepository(
            databaseRepositoryInitializer(context)
        )
    }

    @Before
    fun initializeData() {

        val date = Date()

        wallet1 = Wallet.newEmpty().copy(
            id = UUID(0L,1L),
            name = "myWallet1",
            startAmount = 100f,
            lastAccess = date,
        )
        wallet2 = Wallet.newEmpty().copy(
            id = UUID(0L,2L),
            name = "myWallet2",
            startAmount = 10f,
            lastAccess = Date(date.time + 20),
        )
        wallet3 = Wallet.newEmpty().copy(
            id = UUID(0L,2L),
            name = "myWallet3",
            startAmount = 5f,
            lastAccess = Date(date.time + 30),
        )
        wallet4 = Wallet.newEmpty().copy(
            id = UUID(0L,2L),
            name = "myWallet4",
            startAmount = 0f,
            lastAccess = Date(date.time + 40),
        )
    }
    private fun assertSameWallet(expected: Wallet, result: Wallet) {
        assertEquals(expected.name,result.name)
        assertEquals(expected.startAmount,result.startAmount)
        assertEquals(expected.currency,result.currency)
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    @Test
    @Throws(Exception::class)
    fun initViewModelPassedWallet() {

        val coroutineScope = CoroutineScope(Dispatchers.Default)

        coroutineScope.launch {
            repository.addWallet(wallet1)

            val walletOverviewViewModel = WalletOverviewViewModel(wallet1.id)
            val walletOverviewUiState = walletOverviewViewModel.uiState.value

            assertEquals(wallet1,walletOverviewUiState.wallet)

        }
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    @Test
    @Throws(Exception::class)
    fun initViewModelNoPassedWalletWithOneWalletInDB() {

        val coroutineScope = CoroutineScope(Dispatchers.Default)
        val noWalletUUID = UUID(0L,0L)

        coroutineScope.launch {
            repository.addWallet(wallet1)

            val walletOverviewViewModel = WalletOverviewViewModel(noWalletUUID)

            while (walletOverviewViewModel.uiState.value.isLoadingWallet) {
                delay(20)
            }

            val walletOverviewUiState = walletOverviewViewModel.uiState.value
            assertSameWallet(wallet1, walletOverviewUiState.wallet)

        }
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    @Test
    @Throws(Exception::class)
    fun initViewModelNoPassedWalletWithTwoWalletsInDB() {

        val coroutineScope = CoroutineScope(Dispatchers.Default)
        val noWalletUUID = UUID(0L,0L)

        coroutineScope.launch {
            delay(20)
            repository.addWallet(wallet1)
            repository.addWallet(wallet2)

            val walletOverviewViewModel = WalletOverviewViewModel(noWalletUUID)
            while (walletOverviewViewModel.uiState.value.isLoadingWallet) delay(5)

            val walletOverviewUiState = walletOverviewViewModel.uiState.value
            assertSameWallet(wallet2,walletOverviewUiState.wallet)

        }
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    @Test
    @Throws(Exception::class)
    fun initViewModelNoPassedWalletWithThreeWalletsInDB() {

        val coroutineScope = CoroutineScope(Dispatchers.Default)
        val noWalletUUID = UUID(0L,0L)

        coroutineScope.launch {
            repository.addWallet(wallet1)
            repository.addWallet(wallet2)
            repository.addWallet(wallet3)

            val walletOverviewViewModel = WalletOverviewViewModel(noWalletUUID)
            val walletOverviewUiState = walletOverviewViewModel.uiState.value

            assertSameWallet(wallet3,walletOverviewUiState.wallet)

        }
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    @Test
    @Throws(Exception::class)
    fun initViewModelDeleteShownWalletWithOneWalletInDB() {

        val coroutineScope = CoroutineScope(Dispatchers.Default)

        coroutineScope.launch {
            repository.addWallet(wallet1)

            val walletOverviewViewModel = WalletOverviewViewModel(wallet1.id)
            while (walletOverviewViewModel.uiState.value.isLoadingWallet) delay(5)
            val walletOverviewUiState = walletOverviewViewModel.uiState.value
            assertSameWallet(Wallet.newEmpty(), walletOverviewUiState.wallet)

        }
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    @Test
    @Throws(Exception::class)
    fun initViewModelDeleteShownWalletWithTwoWalletsInDB() {

        val coroutineScope = CoroutineScope(Dispatchers.Default)

        coroutineScope.launch {
            repository.addWallet(wallet1)
            repository.addWallet(wallet2)

            val walletOverviewViewModel = WalletOverviewViewModel(wallet2.id)
            while (walletOverviewViewModel.uiState.value.isLoadingWallet) delay(5)

            walletOverviewViewModel.deleteShownWallet()
            var walletOverviewUiState: WalletOverviewUiState = walletOverviewViewModel.uiState.value
            assertSameWallet(wallet1,walletOverviewUiState.wallet)

            walletOverviewViewModel.deleteShownWallet()
            walletOverviewUiState = walletOverviewViewModel.uiState.value
            assertSameWallet(Wallet.newEmpty(), walletOverviewUiState.wallet)

        }
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    @Test
    @Throws(Exception::class)
    fun initViewModelDeleteShownWalletWithThreeWalletsInDB() {

        val coroutineScope = CoroutineScope(Dispatchers.Default)

        coroutineScope.launch {
            repository.addWallet(wallet1)
            repository.addWallet(wallet2)
            repository.addWallet(wallet3)

            val walletOverviewViewModel = WalletOverviewViewModel(wallet3.id)
            while (walletOverviewViewModel.uiState.value.isLoadingWallet) delay(5)

            walletOverviewViewModel.deleteShownWallet()
            var walletOverviewUiState: WalletOverviewUiState = walletOverviewViewModel.uiState.value
            assertSameWallet(wallet2,walletOverviewUiState.wallet)

            walletOverviewViewModel.deleteShownWallet()
            walletOverviewUiState = walletOverviewViewModel.uiState.value
            assertSameWallet(wallet1,walletOverviewUiState.wallet)

            walletOverviewViewModel.deleteShownWallet()
            walletOverviewUiState = walletOverviewViewModel.uiState.value
            assertSameWallet(Wallet.newEmpty(), walletOverviewUiState.wallet)

        }
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    @Test
    @Throws(Exception::class)
    fun initViewModelSelectWallet() {

        val coroutineScope = CoroutineScope(Dispatchers.Default)

        coroutineScope.launch {
            repository.addWallet(wallet1)
            repository.addWallet(wallet2)
            repository.addWallet(wallet3)

            val walletOverviewViewModel = WalletOverviewViewModel(wallet3.id)
            while (walletOverviewViewModel.uiState.value.isLoadingWallet) delay(5)

            var walletOverviewUiState = walletOverviewViewModel.uiState.value
            assertSameWallet(wallet3,walletOverviewUiState.wallet)

            walletOverviewViewModel.selectWallet(wallet1)
            walletOverviewUiState = walletOverviewViewModel.uiState.value
            assertSameWallet(wallet1,walletOverviewUiState.wallet)

            walletOverviewViewModel.selectWallet(wallet2)
            walletOverviewUiState = walletOverviewViewModel.uiState.value
            assertSameWallet(wallet2,walletOverviewUiState.wallet)

            walletOverviewViewModel.selectWallet(wallet1)
            walletOverviewUiState = walletOverviewViewModel.uiState.value
            assertSameWallet(wallet1,walletOverviewUiState.wallet)

            walletOverviewViewModel.selectWallet(wallet3)
            walletOverviewUiState = walletOverviewViewModel.uiState.value
            assertSameWallet(wallet3,walletOverviewUiState.wallet)

        }
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    @Test
    @Throws(Exception::class)
    fun initViewModelDeleteWalletsInDifferentOrder() {

        val coroutineScope = CoroutineScope(Dispatchers.Default)

        coroutineScope.launch {
            repository.addWallet(wallet1)
            repository.addWallet(wallet2)
            repository.addWallet(wallet3)

            val walletOverviewViewModel = WalletOverviewViewModel(wallet3.id)
            while (walletOverviewViewModel.uiState.value.isLoadingWallet) delay(5)

            var walletOverviewUiState = walletOverviewViewModel.uiState.value
            assertSameWallet(wallet3,walletOverviewUiState.wallet)

            walletOverviewViewModel.selectWallet(wallet1)
            walletOverviewUiState = walletOverviewViewModel.uiState.value
            assertSameWallet(wallet1,walletOverviewUiState.wallet)

            walletOverviewViewModel.deleteShownWallet()
            walletOverviewUiState = walletOverviewViewModel.uiState.value
            assertSameWallet(wallet3,walletOverviewUiState.wallet)

            walletOverviewViewModel.deleteShownWallet()
            walletOverviewUiState = walletOverviewViewModel.uiState.value
            assertSameWallet(wallet2,walletOverviewUiState.wallet)

        }
    }
}
