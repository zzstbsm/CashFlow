package com.zhengzhou.cashflow.wallet_overview.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.data.Transaction
import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import com.zhengzhou.cashflow.database.api.use_case.categoryUseCases.implementations.CategoryUseCases
import com.zhengzhou.cashflow.database.api.use_case.transactionUseCases.implementations.TransactionUseCases
import com.zhengzhou.cashflow.database.api.use_case.walletUseCases.implementations.WalletUseCases
import com.zhengzhou.cashflow.wallet_overview.WalletOverviewReturnResults
import com.zhengzhou.cashflow.wallet_overview.data_structure.TransactionAndCategory
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Integer.min
import java.util.Date
import java.util.UUID


internal class WalletOverviewViewModel(
    repository: RepositoryInterface,
    walletUUID: UUID,
): ViewModel() {

    private var writingOnUiState: Boolean = false

    private var _uiState = MutableStateFlow(WalletOverviewUiState())
    val uiState: StateFlow<WalletOverviewUiState> = _uiState.asStateFlow()

    private val categoryUseCases = CategoryUseCases(repository)
    private val transactionUseCases = TransactionUseCases(repository)
    private val walletUseCases = WalletUseCases(repository)

    private var retrieveCurrentAmountInWalletJob: Job
    private var retrieveTransactionJob: Job
    private var retrieveShownWalletJob: Job
    private var retrieveWalletListJob: Job

    init {
        retrieveWalletListJob = jobUpdateWalletList()

        // Get wallet to show
        retrieveShownWalletJob = jobUpdateCurrentShownWallet(walletUUID = walletUUID)

        retrieveTransactionJob = jobUpdateTransaction()
        retrieveCurrentAmountInWalletJob = jobUpdateCurrentAmount()
    }

    fun reloadScreen() {
        viewModelScope.launch {
            setUiState(
                isLoadingWallet = true
            )
            loadLastAccessed()
        }
        retrieveCurrentAmountInWalletJob.cancel()
        retrieveCurrentAmountInWalletJob = jobUpdateCurrentAmount()
    }
    private fun setUiState(
        wallet: Wallet? = null,
        currentAmountInTheWallet: Float? = null,
        ifZeroWallet: Boolean? = null,
        walletList: List<Wallet>? = null,

        isLoadingWallet: Boolean? = null,
        isLoadingTransactions: Boolean? = null,

        transactionAndCategoryList: List<TransactionAndCategory>? = null,
        showSelectWallet: Boolean? = null,
    ) {

        viewModelScope.launch {
            while (writingOnUiState) delay(5)

            writingOnUiState = true
            _uiState.value = WalletOverviewUiState(
                wallet = wallet ?: uiState.value.wallet,
                currentAmountInTheWallet = currentAmountInTheWallet ?: uiState.value.currentAmountInTheWallet,

                ifZeroWallet = ifZeroWallet ?: uiState.value.ifZeroWallet,
                walletList = walletList ?: uiState.value.walletList,
                transactionAndCategoryList = transactionAndCategoryList ?: uiState.value.transactionAndCategoryList,

                isLoadingWallet = isLoadingWallet ?: uiState.value.isLoadingWallet,
                isLoadingTransactions = isLoadingTransactions ?: uiState.value.isLoadingTransactions,

                showSelectWallet = showSelectWallet ?: uiState.value.showSelectWallet
            )
            writingOnUiState = false
        }
    }

    private suspend fun loadLastAccessed() {
        setUiState(
            wallet = walletUseCases.getLastAccessedWallet() ?: Wallet.newEmpty(),
            isLoadingWallet = false,
        )
        retrieveCurrentAmountInWalletJob.cancel()
        retrieveCurrentAmountInWalletJob = jobUpdateCurrentAmount()
        retrieveTransactionJob.cancel()
        retrieveTransactionJob = jobUpdateTransaction()
    }

    fun deleteShownWallet(): WalletOverviewReturnResults {

        return if (uiState.value.transactionAndCategoryList.isEmpty()) {
            viewModelScope.launch {
                walletUseCases.deleteWallet(uiState.value.wallet)
                setUiState(
                    wallet = Wallet.newEmpty(),
                    currentAmountInTheWallet = 0f,
                    isLoadingWallet = true
                )
                loadLastAccessed()
                while (uiState.value.isLoadingWallet) delay(5)

                if (uiState.value.wallet.id == Wallet.newWalletId()) {
                    setUiState(
                        ifZeroWallet = true,
                        isLoadingWallet = false
                    )
                }
            }
            WalletOverviewReturnResults.CAN_DELETE_WALLET
        } else WalletOverviewReturnResults.CANNOT_DELETE_WALLET


    }


    fun selectWallet(wallet: Wallet) {

        val walletToReload = wallet.copy(
            lastAccess = Date()
        )
        viewModelScope.launch {
             walletUseCases.updateWallet(walletToReload)
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

            while (uiState.value.isLoadingTransactions) delay(5)

            setUiState(
                isLoadingTransactions = false,
            )

            val wallet = uiState.value.wallet

            if (wallet.id != Wallet.newWalletId()) {

                transactionUseCases.getTransactionListInWallet(
                    walletUUID = wallet.id,
                ).collect { list: List<Transaction> ->
                    val transactionAndCategoryList = mutableListOf<TransactionAndCategory>()

                    list.filter { !it.isBlueprint }.let { filteredList ->
                        filteredList.subList(0, min(filteredList.size, 3))
                            .forEach { transaction ->
                                val category = categoryUseCases.getCategory(transaction.categoryUUID)
                                    ?: Category.newEmpty()


                                transactionAndCategoryList.add(
                                    TransactionAndCategory(
                                        transaction = if (wallet.id == transaction.secondaryWalletUUID) {
                                            transaction.copy(
                                                amount = -transaction.amount
                                            )
                                        } else transaction,
                                        category = category,
                                    )
                                )
                            }
                    }

                    setUiState(
                        transactionAndCategoryList = transactionAndCategoryList,
                        isLoadingTransactions = false,
                    )
                }
            }
        }
    }
    private fun jobUpdateWalletList(): Job {
        return viewModelScope.launch {

            walletUseCases.getWalletList().collect { walletList: List<Wallet> ->
                setUiState(
                    ifZeroWallet = walletList.isEmpty(),
                    walletList = walletList.sortedBy { it.name }
                )
            }
        }
    }
    private fun jobUpdateCurrentAmount(): Job {
        return viewModelScope.launch {

            if (uiState.value.wallet.id != Wallet.newWalletId()) {
                transactionUseCases.getTransactionListInWallet(uiState.value.wallet.id)
                    .collect { transactionList: List<Transaction> ->
                        setUiState(
                            currentAmountInTheWallet = transactionList.filter {
                                !it.isBlueprint
                            }.map {
                                if (it.secondaryWalletUUID == uiState.value.wallet.id) -it.amount else it.amount
                            }.sum() + uiState.value.wallet.startAmount
                        )
                    }
            }
        }
    }
    private fun jobUpdateCurrentShownWallet(walletUUID: UUID): Job {
        return viewModelScope.launch {
            if (walletUUID == Wallet.newWalletId()) {
                while (uiState.value.isLoadingWallet) delay(5)
                if (!uiState.value.ifZeroWallet) {
                    loadLastAccessed()
                }
            } else {
                setUiState(
                    wallet = walletUseCases.getWallet(walletUUID) ?: Wallet.newEmpty(),
                    isLoadingWallet = false
                )
            }
        }
    }

}