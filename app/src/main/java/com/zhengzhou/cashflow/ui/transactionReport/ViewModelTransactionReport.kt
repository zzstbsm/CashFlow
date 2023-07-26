package com.zhengzhou.cashflow.ui.transactionReport

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.data.Tag
import com.zhengzhou.cashflow.data.TagLocation
import com.zhengzhou.cashflow.data.TagTransaction
import com.zhengzhou.cashflow.data.Transaction
import com.zhengzhou.cashflow.data.TransactionFullForUI
import com.zhengzhou.cashflow.data.TransactionType
import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.database.DatabaseRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

data class TransactionReportUiState(
    val wallet: Wallet = Wallet(),
    val transaction: Transaction = Transaction(),
    val category: Category = Category(),
    val tagTransactionList: List<TagTransaction> = listOf(),
    val tagList: List<Tag> = listOf(),
    val location: TagLocation = TagLocation(),

    val transactionType: TransactionType = TransactionType.Loading,

    val isLoading: Boolean = true,

)

class TransactionReportViewModel(
    transactionUUID: UUID,
): ViewModel() {

    private var _uiState = MutableStateFlow(TransactionReportUiState())
    val uiState: StateFlow<TransactionReportUiState> = _uiState.asStateFlow()

    private val repository = DatabaseRepository.get()

    private var jobLoadTransactionReport: Job


    init {
        jobLoadTransactionReport = loadTransactionReport(transactionUUID)
    }

    fun loadTransactionReport(transactionUUID: UUID): Job {
        return viewModelScope.launch {
            val (transactionFullForUI, isLoaded) = TransactionFullForUI.load(repository,transactionUUID)

            while (!isLoaded) {
                delay(20)
            }
            _uiState.value = uiState.value.copy(
                wallet = transactionFullForUI.wallet,
                transaction = transactionFullForUI.transaction,
                category = transactionFullForUI.category,
                tagList = transactionFullForUI.tagList,
                location = transactionFullForUI.location,

                transactionType = TransactionType.setTransaction(
                    transactionFullForUI.transaction.movementType
                ) ?: TransactionType.Loading,

                isLoading = false,
            )
        }
    }
}