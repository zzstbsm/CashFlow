package com.zhengzhou.cashflow.ui.balance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.data.Transaction
import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.database.DatabaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

data class BalanceUiState(
    val wallet  : Wallet = Wallet.loadingWallet(),
    val isLoading: Boolean = true,
)

class BalanceViewModel() : ViewModel() {

    private val repository = DatabaseRepository.get()

    // UiState
    private var _uiState = MutableStateFlow(BalanceUiState())
    val uiState: StateFlow<BalanceUiState> = _uiState.asStateFlow()

    // List of all categories
    private val _categoriesList:
            MutableStateFlow<MutableList<Category>> = MutableStateFlow(mutableListOf())
    val categoriesList: StateFlow<MutableList<Category>> = _categoriesList.asStateFlow()

    // List of transactions to show
    private val _transactionList:
            MutableStateFlow<MutableList<Transaction>> = MutableStateFlow(mutableListOf())
    val transactionList: StateFlow<MutableList<Transaction>> = _transactionList.asStateFlow()

    // List of wallets
    private val _walletList:
            MutableStateFlow<List<Wallet>> = MutableStateFlow(emptyList())
    private val walletList: StateFlow<List<Wallet>>
        get() = _walletList.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getWalletList().collect {
                _walletList.value = it
                _uiState.value = uiState.value.copy(
                    isLoading = false
                )
                // Important to keep separate in order to load data
                _uiState.value = uiState.value.copy(
                    wallet = getWalletToShow()
                )
                loadTransactions()
            }
        }
    }

    private fun loadTransactions() {
        viewModelScope.launch {
            repository.getTransactionListInWallet(uiState.value.wallet.id).collect {
                _transactionList.value = it as MutableList<Transaction>
            }
        }
    }

    private suspend fun getWalletToShow(): Wallet {

        lateinit var walletToReturn: Wallet

        if (uiState.value.isLoading) {
            // Data still loading
            walletToReturn = Wallet.loadingWallet()
        } else {
            if (ifZeroWallets()) {
                // Create empty wallet and save it
                walletToReturn = Wallet.emptyWallet()
                viewModelScope.launch {
                    repository.addWallet(walletToReturn)
                }
            } else {
                // Retrieve wallet
                walletToReturn = repository.getWalletLastAccessed().copy(
                    lastAccess = Date()
                )
                // Update last access
                viewModelScope.launch {
                    repository.updateWallet(walletToReturn)
                }
            }
        }
        return walletToReturn
    }

    private fun ifZeroWallets(): Boolean {
        return walletList.value.isEmpty()
    }

}