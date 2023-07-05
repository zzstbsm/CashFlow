package com.zhengzhou.cashflow.testViewModel

import android.annotation.SuppressLint
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.zhengzhou.cashflow.DatabaseRepositoryTest
import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.databaseRepositoryInitializerTest
import com.zhengzhou.cashflow.ui.walletOverview.WalletOverviewUiState
import com.zhengzhou.cashflow.ui.walletOverview.WalletOverviewViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.Date
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class TestWalletOverview {

    private lateinit var repository: DatabaseRepositoryTest
    private lateinit var wallet1: Wallet
    private lateinit var wallet2: Wallet
    private lateinit var wallet3: Wallet
    private lateinit var wallet4: Wallet
    private lateinit var expectedUiState: WalletOverviewUiState

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        repository = databaseRepositoryInitializerTest(context)
    }

    @Before
    fun initializeData() {

        val date = Date()

        wallet1 = Wallet(
            id = UUID(0L,1L),
            name = "myWallet1",
            startAmount = 100f,
            lastAccess = date,
        )
        wallet2 = Wallet(
            id = UUID(0L,2L),
            name = "myWallet2",
            startAmount = 10f,
            lastAccess = Date(date.time + 20),
        )
        wallet3 = Wallet(
            id = UUID(0L,2L),
            name = "myWallet3",
            startAmount = 5f,
            lastAccess = Date(date.time + 30),
        )
        wallet4 = Wallet(
            id = UUID(0L,2L),
            name = "myWallet4",
            startAmount = 0f,
            lastAccess = Date(date.time + 40),
        )
        setExpectedUiState(wallet1)
    }

    private fun setExpectedUiState(wallet: Wallet) {
        expectedUiState = WalletOverviewUiState(
            wallet = wallet,
            currentAmountInTheWallet = wallet.startAmount,
            ifZeroWallet = false,
            isLoadingWallet = false
        )
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        repository.close()
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    @Test
    @Throws(Exception::class)
    fun initViewModelPassedWallet() {

        val coroutineScope = CoroutineScope(Dispatchers.Default)

        coroutineScope.launch {
            setExpectedUiState(wallet1)
            repository.addWallet(expectedUiState.wallet)

            val walletOverviewViewModel = WalletOverviewViewModel(expectedUiState.wallet.id)
            val walletOverviewUiState = walletOverviewViewModel.uiState.value

            assertTrue(expectedUiState.wallet == walletOverviewUiState.wallet)

        }
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    @Test
    @Throws(Exception::class)
    fun initViewModelNoPassedWalletWithOneWalletInDB() {

        val coroutineScope = CoroutineScope(Dispatchers.Default)
        val noWalletUUID = UUID(0L,0L)

        coroutineScope.launch {
            setExpectedUiState(wallet1)
            repository.addWallet(wallet1)

            val walletOverviewViewModel = WalletOverviewViewModel(noWalletUUID)

            while (walletOverviewViewModel.uiState.value.isLoadingWallet) {
                delay(20)
            }

            val walletOverviewUiState = walletOverviewViewModel.uiState.value
            assertTrue(wallet1 == walletOverviewUiState.wallet)

        }
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    @Test
    @Throws(Exception::class)
    fun initViewModelNoPassedWalletWithTwoWalletsInDB() {

        val coroutineScope = CoroutineScope(Dispatchers.Default)
        val noWalletUUID = UUID(0L,0L)

        coroutineScope.launch {
            repository.addWallet(wallet1)
            repository.addWallet(wallet2)
            setExpectedUiState(wallet2)

            val walletOverviewViewModel = WalletOverviewViewModel(noWalletUUID)
            while (walletOverviewViewModel.uiState.value.isLoadingWallet) {
                delay(20)
            }

            val walletOverviewUiState = walletOverviewViewModel.uiState.value
            assertTrue(wallet2 == walletOverviewUiState.wallet)

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
            setExpectedUiState(wallet3)

            val walletOverviewViewModel = WalletOverviewViewModel(noWalletUUID)
            val walletOverviewUiState = walletOverviewViewModel.uiState.value

            assertTrue(expectedUiState.wallet == walletOverviewUiState.wallet)

        }
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    @Test
    @Throws(Exception::class)
    fun initViewModelDeleteShownWalletWithOneWalletInDB() {

        val coroutineScope = CoroutineScope(Dispatchers.Default)

        coroutineScope.launch {
            repository.addWallet(wallet1)
            setExpectedUiState(Wallet.emptyWallet())

            val walletOverviewViewModel = WalletOverviewViewModel(wallet1.id)
            val walletOverviewUiState = walletOverviewViewModel.uiState.value

            walletOverviewViewModel.deleteShownWallet()
            assertTrue(Wallet.emptyWallet() == walletOverviewUiState.wallet)

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
            setExpectedUiState(wallet2)

            val walletOverviewViewModel = WalletOverviewViewModel(expectedUiState.wallet.id)
            val walletOverviewUiState = walletOverviewViewModel.uiState.value

            walletOverviewViewModel.deleteShownWallet()
            assertTrue(wallet1 == walletOverviewUiState.wallet)
            walletOverviewViewModel.deleteShownWallet()
            assertTrue(Wallet.emptyWallet() == walletOverviewUiState.wallet)

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
            setExpectedUiState(wallet3)

            val walletOverviewViewModel = WalletOverviewViewModel(expectedUiState.wallet.id)

            while (walletOverviewViewModel.uiState.value.isLoadingWallet) {
                delay(20)
            }

            var walletOverviewUiState: WalletOverviewUiState = walletOverviewViewModel.uiState.value
            walletOverviewViewModel.deleteShownWallet()
            assertTrue(wallet2 == walletOverviewUiState.wallet)

            walletOverviewUiState = walletOverviewViewModel.uiState.value
            walletOverviewViewModel.deleteShownWallet()
            assertTrue(wallet1 == walletOverviewUiState.wallet)

            walletOverviewUiState = walletOverviewViewModel.uiState.value
            walletOverviewViewModel.deleteShownWallet()
            assertTrue(Wallet.emptyWallet() == walletOverviewUiState.wallet)

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
            setExpectedUiState(wallet3)

            val walletOverviewViewModel = WalletOverviewViewModel(expectedUiState.wallet.id)
            lateinit var walletOverviewUiState: WalletOverviewUiState

            while (walletOverviewUiState.isLoadingWallet) {
                delay(20)
            }

            walletOverviewUiState = walletOverviewViewModel.uiState.value
            assertTrue(wallet3 == walletOverviewUiState.wallet)
            walletOverviewViewModel.selectWallet(wallet1)
            walletOverviewUiState = walletOverviewViewModel.uiState.value
            assertTrue(wallet1 == walletOverviewUiState.wallet)
            walletOverviewViewModel.selectWallet(wallet2)
            walletOverviewUiState = walletOverviewViewModel.uiState.value
            assertTrue(wallet2 == walletOverviewUiState.wallet)
            walletOverviewViewModel.selectWallet(wallet1)
            walletOverviewUiState = walletOverviewViewModel.uiState.value
            assertTrue(wallet1 == walletOverviewUiState.wallet)
            walletOverviewViewModel.selectWallet(wallet3)
            walletOverviewUiState = walletOverviewViewModel.uiState.value
            assertTrue(wallet3 == walletOverviewUiState.wallet)

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

            while (walletOverviewViewModel.uiState.value.isLoadingWallet) {
                delay(20)
            }

            var walletOverviewUiState = walletOverviewViewModel.uiState.value
            assertTrue(wallet3 == walletOverviewUiState.wallet)

            walletOverviewViewModel.selectWallet(wallet1)
            walletOverviewUiState = walletOverviewViewModel.uiState.value
            assertTrue(wallet1 == walletOverviewUiState.wallet)

            walletOverviewViewModel.deleteShownWallet()
            walletOverviewUiState = walletOverviewViewModel.uiState.value
            assertTrue(wallet3 == walletOverviewUiState.wallet)

            walletOverviewViewModel.deleteShownWallet()
            walletOverviewUiState = walletOverviewViewModel.uiState.value
            assertTrue(wallet2 == walletOverviewUiState.wallet)

        }
    }
}
