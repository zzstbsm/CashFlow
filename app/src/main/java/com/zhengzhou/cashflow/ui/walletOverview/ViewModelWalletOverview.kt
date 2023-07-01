package com.zhengzhou.cashflow.ui.walletOverview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.data.Currency
import com.zhengzhou.cashflow.data.TransactionAndCategory
import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.database.DatabaseRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*

data class WalletOverviewUiState(
    val wallet: Wallet = Wallet(),
    val currentAmountInTheWallet: Float = 0f,
    val isLoading: Boolean = true,
    val ifZeroWallet: Boolean = false,
    val isLoadingWallet: Boolean = true,
    val transactionAndCategoryList: List<TransactionAndCategory> = listOf(),

    val showSelectWallet: Boolean = false,
)

class WalletOverviewViewModel(
    walletUUID: UUID,
): ViewModel() {

    private var _uiState = MutableStateFlow(WalletOverviewUiState())
    val uiState: StateFlow<WalletOverviewUiState> = _uiState.asStateFlow()

    private val _walletList = MutableStateFlow(listOf<Wallet>())
    val walletList: StateFlow<List<Wallet>> = _walletList.asStateFlow()

    private val repository = DatabaseRepository.get()

    var currencyFormatter: NumberFormat = Currency.setCurrencyFormatter(Currency.EUR.abbreviation)

    private var retrieveCurrentAmountInWallet: Job
    private var retrieveTransactionJob: Job
    private var retrieveWalletListJob: Job

    init {
        retrieveWalletListJob = jobUpdateWalletList()

        // Get wallet to show
        viewModelScope.launch {
            if (walletUUID == UUID(0L,0L)) {
                while (uiState.value.isLoading) {
                    delay(20)
                }
                if (!uiState.value.ifZeroWallet) {
                    loadLastAccessed()
                }
            } else {
                getWallet(walletUUID)
            }
            currencyFormatter = Currency.setCurrencyFormatter(uiState.value.wallet.currency.abbreviation)
        }

        retrieveTransactionJob = jobUpdateTransaction()
        retrieveCurrentAmountInWallet = jobUpdateCurrentAmount()
    }

    fun formatCurrency(amount: Float) : String {
        return Currency.formatCurrency(currencyFormatter,amount)
    }

    private suspend fun getWallet(walletUUID: UUID) {
        _uiState.value = uiState.value.copy(
            wallet = repository.getWallet(walletUUID) ?: Wallet.emptyWallet(),
            isLoading = false
        )
    }

    private suspend fun loadLastAccessed() {
        _uiState.value = uiState.value.copy(
            wallet = repository.getWalletLastAccessed() ?: Wallet.emptyWallet(),
            isLoading = false,
        )
    }

    fun deleteShownWallet() {
        viewModelScope.launch {
            repository.deleteWallet(uiState.value.wallet)
            _uiState.value = uiState.value.copy(
                wallet = Wallet()
            )
            loadLastAccessed()
            if (uiState.value.wallet.id == UUID(0L,0L)) {
                _uiState.value = uiState.value.copy(
                    ifZeroWallet = true
                )
            }
        }
    }

    fun reloadScreen() {
        viewModelScope.launch {
            _uiState.value = uiState.value.copy(
                isLoading = true
            )
            loadLastAccessed()
        }
    }

    fun selectWallet(wallet: Wallet) {

        val walletToReload = wallet.copy(
            lastAccess = Date()
        )
        viewModelScope.launch {
             repository.updateWallet(walletToReload)
        }
        _uiState.value = uiState.value.copy(
            wallet = walletToReload
        )

        // Retrieve recent transactions
        retrieveTransactionJob.cancel()
        retrieveTransactionJob = jobUpdateTransaction()

        // Update wallet amount
        retrieveCurrentAmountInWallet.cancel()
        retrieveCurrentAmountInWallet = jobUpdateCurrentAmount()
    }
    fun showSelectWalletDialog(toShow: Boolean) {
        _uiState.value = uiState.value.copy(
            showSelectWallet = toShow
        )
    }
    fun updateWalletList() {
        retrieveWalletListJob.cancel()
        retrieveWalletListJob = jobUpdateWalletList()
    }


    private fun jobUpdateTransaction(): Job {
        return viewModelScope.launch {
            while (uiState.value.isLoading) {
                delay(20)
            }
            repository.getTransactionShortListInWallet(
                uiState.value.wallet.id,
                3
            ).collect { list ->
                val transactionAndCategoryList = mutableListOf<TransactionAndCategory>()

                list.forEach { transaction ->
                    val category = repository.getCategory(transaction.idCategory) ?: Category()
                    transactionAndCategoryList.add(
                        TransactionAndCategory(
                            transaction = transaction,
                            category = category,
                        )
                    )
                }

                _uiState.value = uiState.value.copy(
                    transactionAndCategoryList = transactionAndCategoryList
                )
            }
        }
    }
    private fun jobUpdateWalletList(): Job {
        return viewModelScope.launch {
            repository.getWalletList().collect {
                _walletList.value = it
                _uiState.value = uiState.value.copy(
                    ifZeroWallet = it.isEmpty()
                )
            }
        }
    }
    private fun jobUpdateCurrentAmount(): Job {
        return viewModelScope.launch {
            while (uiState.value.isLoading) {
                delay(20)
            }
            var tempAmount = uiState.value.wallet.startAmount
            repository.getTransactionListInWallet(uiState.value.wallet.id).collect {
                it.forEach { transaction ->
                    tempAmount += transaction.amount
                }
                _uiState.value = uiState.value.copy(
                    currentAmountInTheWallet = tempAmount
                )
            }
        }
    }

}