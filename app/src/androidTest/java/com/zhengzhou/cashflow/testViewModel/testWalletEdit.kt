package com.zhengzhou.cashflow.testViewModel

import android.annotation.SuppressLint
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.zhengzhou.cashflow.DatabaseRepositoryTest
import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.databaseRepositoryInitializerTest
import com.zhengzhou.cashflow.ui.walletEdit.WalletEditUiState
import com.zhengzhou.cashflow.ui.walletEdit.WalletEditViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.Date
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class TestWalletEdit() {

    private lateinit var repository: DatabaseRepositoryTest
    private lateinit var wallet1: Wallet
    private lateinit var wallet2: Wallet
    private lateinit var wallet3: Wallet
    private lateinit var wallet4: Wallet
    private lateinit var expectedUiState: WalletEditUiState

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
        expectedUiState = WalletEditUiState(
                wallet = wallet,
        )
    }
    private fun assertSameWallet(expected: Wallet, result: Wallet) {
        assertEquals(expected.name,result.name)
        assertEquals(expected.startAmount,result.startAmount)
        assertEquals(expected.currency,result.currency)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        repository.close()
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    @Test
    @Throws(Exception::class)
    fun initViewModelNoPassedWallet() {

        val coroutineScope = CoroutineScope(Dispatchers.Default)
        val newWallet = UUID(0L,0L)

        coroutineScope.launch {
            setExpectedUiState(wallet1)
            repository.addWallet(expectedUiState.wallet)

            val walletEditViewModel = WalletEditViewModel(newWallet)
            val walletOverviewUiState = walletEditViewModel.uiState.value

            assertSameWallet(Wallet(),walletOverviewUiState.wallet)

        }
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    @Test
    @Throws(Exception::class)
    fun initViewModelPassedWallet() {

        val coroutineScope = CoroutineScope(Dispatchers.Default)

        coroutineScope.launch {
            setExpectedUiState(wallet1)
            repository.addWallet(wallet1)

            val walletEditViewModel = WalletEditViewModel(wallet1.id)

            while (walletEditViewModel.uiState.value.isLoading) {
                delay(20)
            }

            val walletOverviewUiState = walletEditViewModel.uiState.value
            assertSameWallet(wallet1,walletOverviewUiState.wallet)
        }
    }
}
