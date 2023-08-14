package com.zhengzhou.cashflow.ui.walletOverview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.data.Currency
import com.zhengzhou.cashflow.data.Transaction
import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.dataForUi.TransactionAndCategory
import com.zhengzhou.cashflow.database.DatabaseRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Integer.min
import java.text.NumberFormat
import java.util.Date
import java.util.UUID

data class WalletOverviewUiState(
    val wallet: Wallet = Wallet.newEmpty(),
    val currentAmountInTheWallet: Float = 0f,
    val isLoading: Boolean = true,

    val ifZeroWallet: Boolean = false,
    val walletList: List<Wallet> = listOf(),

    val isLoadingWallet: Boolean = true,
    val transactionAndCategoryList: List<TransactionAndCategory> = listOf(),

    val showSelectWallet: Boolean = false,
)

class WalletOverviewViewModel(
    walletUUID: UUID,
): ViewModel() {

    private var writingOnUiState: Boolean = false

    private var _uiState = MutableStateFlow(WalletOverviewUiState())
    val uiState: StateFlow<WalletOverviewUiState> = _uiState.asStateFlow()

    private val repository = DatabaseRepository.get()

    var currencyFormatter: NumberFormat = Currency.setCurrencyFormatter(Currency.EUR.abbreviation)

    private var retrieveCurrentAmountInWalletJob: Job
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
        retrieveCurrentAmountInWalletJob = jobUpdateCurrentAmount()
    }

    private fun setUiState(
        wallet: Wallet? = null,
        currentAmountInTheWallet: Float? = null,
        isLoading: Boolean? = null,
        ifZeroWallet: Boolean? = null,
        walletList: List<Wallet>? = null,
        isLoadingWallet: Boolean? = null,
        transactionAndCategoryList: List<TransactionAndCategory>? = null,
        showSelectWallet: Boolean? = null,
    ) {

        viewModelScope.launch {
            while (writingOnUiState) delay(1)

            writingOnUiState = true
            _uiState.value = WalletOverviewUiState(
                wallet = wallet ?: uiState.value.wallet,
                currentAmountInTheWallet = currentAmountInTheWallet ?: uiState.value.currentAmountInTheWallet,
                isLoading = isLoading ?: uiState.value.isLoading,
                ifZeroWallet = ifZeroWallet ?: uiState.value.ifZeroWallet,
                walletList = walletList ?: uiState.value.walletList,
                isLoadingWallet = isLoadingWallet ?: uiState.value.isLoadingWallet,
                transactionAndCategoryList = transactionAndCategoryList ?: uiState.value.transactionAndCategoryList,
                showSelectWallet = showSelectWallet ?: uiState.value.showSelectWallet
            )
            if (wallet != null) {
                currencyFormatter = Currency.setCurrencyFormatter(wallet.currency.abbreviation)
            }
            writingOnUiState = false
        }
    }

    fun formatCurrency(amount: Float) : String {
        return Currency.formatCurrency(currencyFormatter, amount)
    }

    private suspend fun getWallet(walletUUID: UUID) {
        setUiState(
            wallet = repository.getWallet(walletUUID) ?: Wallet.newEmpty(),
            isLoading = false
        )
    }

    private suspend fun loadLastAccessed() {
        setUiState(
            wallet = repository.getWalletLastAccessed() ?: Wallet.newEmpty(),
            isLoading = false,
        )
        retrieveCurrentAmountInWalletJob.cancel()
        retrieveCurrentAmountInWalletJob = jobUpdateCurrentAmount()
        retrieveTransactionJob.cancel()
        retrieveTransactionJob = jobUpdateTransaction()
    }

    fun deleteShownWallet(): WalletOverviewReturnResults {

        return if (uiState.value.transactionAndCategoryList.isEmpty()) {
            viewModelScope.launch {
                repository.deleteWallet(uiState.value.wallet)
                setUiState(
                    wallet = Wallet.newEmpty(),
                    isLoading = true
                )
                loadLastAccessed()
                while (uiState.value.isLoadingWallet) delay(5)

                if (uiState.value.wallet.id == UUID(0L,0L)) {
                    setUiState(
                        ifZeroWallet = true,
                        isLoading = false
                    )
                }
            }
            WalletOverviewReturnResults.CAN_DELETE_WALLET
        } else WalletOverviewReturnResults.CANNOT_DELETE_WALLET


    }

    fun reloadScreen() {
        viewModelScope.launch {
            setUiState(
                isLoading = true
            )
            loadLastAccessed()
        }
        retrieveCurrentAmountInWalletJob.cancel()
        retrieveCurrentAmountInWalletJob = jobUpdateCurrentAmount()
    }

    fun selectWallet(wallet: Wallet) {

        val walletToReload = wallet.copy(
            lastAccess = Date()
        )
        viewModelScope.launch {
             repository.updateWallet(walletToReload)
        }

        setUiState(
            wallet = walletToReload
        )

        // Retrieve recent transactions
        retrieveTransactionJob.cancel()
        retrieveTransactionJob = jobUpdateTransaction()

        // Update wallet amount
        retrieveCurrentAmountInWalletJob.cancel()
        retrieveCurrentAmountInWalletJob = jobUpdateCurrentAmount()
    }
    fun showSelectWalletDialog(toShow: Boolean) {
        setUiState(
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
                delay(5)
            }

            val wallet = uiState.value.wallet

            repository.getTransactionListInWallet(
                walletUUID = wallet.id,
            ).collect { list: List<Transaction> ->
                val transactionAndCategoryList = mutableListOf<TransactionAndCategory>()

                list.filter { !it.isBlueprint }.subList(0,min(list.size,3)).forEach { transaction ->
                    val category = repository.getCategory(transaction.categoryId) ?: Category.newEmpty()


                    transactionAndCategoryList.add(
                        TransactionAndCategory(
                            transaction = if (wallet.id == transaction.secondaryWalletId) {
                                transaction.copy(
                                    amount = -transaction.amount
                                )
                            } else transaction,
                            category = category,
                        )
                    )
                }

                setUiState(
                    transactionAndCategoryList = transactionAndCategoryList
                )
            }
        }
    }
    private fun jobUpdateWalletList(): Job {
        return viewModelScope.launch {

            repository.getWalletList().collect { walletList: List<Wallet> ->
                //_walletList.value = walletList

                setUiState(
                    ifZeroWallet = walletList.isEmpty(),
                    walletList = walletList.sortedBy { it.name }
                )
            }
        }
    }
    private fun jobUpdateCurrentAmount(): Job {
        return viewModelScope.launch {
            while (writingOnUiState) delay(1)
            var tempAmount = uiState.value.wallet.startAmount

            repository.getTransactionListInWallet(uiState.value.wallet.id).collect { transactionList: List<Transaction> ->
                transactionList.forEach { transaction: Transaction ->
                    tempAmount += transaction.amount
                }

                setUiState(
                    currentAmountInTheWallet = tempAmount
                )
            }
        }
    }

}