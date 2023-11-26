package com.zhengzhou.cashflow.ui.commonTransactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zhengzhou.cashflow.R
import com.zhengzhou.cashflow.data.Transaction
import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.dataForUi.TransactionFullForUI
import com.zhengzhou.cashflow.database.DatabaseRepository
import com.zhengzhou.cashflow.tools.EventMessages
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CommonTransactionsUiState(
    val isLoading: Boolean = true,
    val walletList: List<Wallet> = listOf(),

    val transactionFullForUIList: List<TransactionFullForUI> = listOf(),
)

class CommonTransactionsViewModel(): ViewModel() {

    private var _uiState = MutableStateFlow(CommonTransactionsUiState())
    val uiState: StateFlow<CommonTransactionsUiState> = _uiState.asStateFlow()

    private var writingOnUiState = false

    private val repository = DatabaseRepository.get()
    private var jobLoadWalletList: Job
    private var jobLoadTransactionBlueprint: Job

    init {

        jobLoadWalletList = loadWalletList()
        jobLoadTransactionBlueprint = loadTransactionBlueprint()
    }

    private fun setUiState(
        isLoading: Boolean? = null,
        walletList: List<Wallet>? = null,

        transactionFullForUIList: List<TransactionFullForUI> ?= null,
    ) {
        viewModelScope.launch {
            while (writingOnUiState) delay(5)

            writingOnUiState = true

            _uiState.value = CommonTransactionsUiState(
                isLoading = isLoading ?: uiState.value.isLoading,
                walletList = walletList ?: uiState.value.walletList,
                transactionFullForUIList = transactionFullForUIList ?: uiState.value.transactionFullForUIList
            )

            writingOnUiState = false
        }
    }

    fun reloadScreen() {
        viewModelScope.launch {

            setUiState(
                isLoading = true
            )
            jobLoadWalletList.cancel()
            jobLoadTransactionBlueprint.cancel()
            jobLoadWalletList = loadWalletList()
            jobLoadTransactionBlueprint = loadTransactionBlueprint()
        }
    }
    private fun loadTransactionBlueprint(): Job {

        return viewModelScope.launch {

            val coroutineScope = CoroutineScope(Dispatchers.Default)

            var isLoading = true
            var transactionList: List<Transaction> = listOf()
            val transactionFullForUIList: MutableList<TransactionFullForUI> = mutableListOf()

            coroutineScope.launch {
                repository.getTransactionIsBlueprint().collect {
                    transactionList = it
                    isLoading = false
                }
            }

            while (isLoading) delay(5)

            transactionList.forEach { transaction ->
                val (transactionFullForUi, _) = TransactionFullForUI.load(repository, transaction.id)
                transactionFullForUIList.add(transactionFullForUi)
            }
            setUiState(
                isLoading = false,
                transactionFullForUIList = transactionFullForUIList
                    .sortedBy { it.transaction.description }
                    .sortedBy { it.transaction.transactionType }
            )

        }
    }

    private fun loadWalletList(): Job {
        return viewModelScope.launch {
            repository.getWalletList().collect { walletList ->
                setUiState(
                    walletList = walletList,
                )
            }
        }
    }

    fun changeWallet(indexInList: Int, newWallet: Wallet) {
        var element = uiState.value.transactionFullForUIList[indexInList]

        element = element.copy(
            wallet = newWallet,
            transaction = element.transaction.copy(
                walletUUID = newWallet.id
            )
        )
        val list = uiState.value.transactionFullForUIList.toMutableList()
        list[indexInList] = element
        setUiState(
            transactionFullForUIList = list.toList()
        )
    }

    fun addTransaction(transactionFullForUI: TransactionFullForUI) {
        viewModelScope.launch {
            transactionFullForUI.copy(
                transaction = transactionFullForUI.transaction.copy(
                    isBlueprint = false
                ),
                tagList = transactionFullForUI.tagList.map { tag ->
                    tag.copy(
                        count = tag.count + 1
                    )
                }
            ).save(
                repository = repository,
                newTransaction = true,
            )

            EventMessages.sendMessageId(R.string.TransactionEdit_transaction_saved)

        }
    }

    fun deleteTransaction(transactionFullForUI: TransactionFullForUI)  {
        viewModelScope.launch {
            transactionFullForUI.copy(
                tagList = transactionFullForUI.tagList.map { tag ->
                    tag.copy(
                        count = tag.count - 1
                    )
                }
            ).delete(repository)
            jobLoadTransactionBlueprint.cancel()
            jobLoadTransactionBlueprint = loadTransactionBlueprint()
        }
        EventMessages.sendMessageId(R.string.CommonTransactions_transaction_deleted)
    }
}