package com.zhengzhou.cashflow.ui.transactionReport

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zhengzhou.cashflow.R
import com.zhengzhou.cashflow.dataForUi.TransactionFullForUI
import com.zhengzhou.cashflow.database.DatabaseRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

data class TransactionReportUiState(
    val transactionFullForUI: TransactionFullForUI = TransactionFullForUI(),

    val isLoading: Boolean = true,

    )

class TransactionReportViewModel(
    transactionUUID: UUID,
): ViewModel() {

    private var _uiState = MutableStateFlow(TransactionReportUiState())
    val uiState: StateFlow<TransactionReportUiState> = _uiState.asStateFlow()

    private val repository = DatabaseRepository.get()

    private var writingOnUiState: Boolean = false

    private var jobLoadTransactionReport: Job


    init {
        jobLoadTransactionReport = loadTransactionReport(transactionUUID)
    }

    private fun setUiState(
        transactionFullForUI: TransactionFullForUI? = null,
        isLoading: Boolean? = null,
    ) {
        viewModelScope.launch {
            while (writingOnUiState) delay(5)

            writingOnUiState = true
            _uiState.value = uiState.value.copy(
                transactionFullForUI = transactionFullForUI ?: uiState.value.transactionFullForUI,
                isLoading = isLoading ?: uiState.value.isLoading,
            )
            writingOnUiState = false

        }
    }

    fun loadTransactionReport(transactionUUID: UUID): Job {
        return viewModelScope.launch {
            val (transactionFullForUI, isLoaded) = TransactionFullForUI.load(repository, transactionUUID)
            setUiState(
                transactionFullForUI = transactionFullForUI,
                isLoading = !isLoaded
            )
        }
    }

    fun deleteTransaction() {
        viewModelScope.launch {
            uiState.value.transactionFullForUI.copy(
                tagList = uiState.value.transactionFullForUI.tagList.map { tag ->
                    tag.copy(
                        count = tag.count - 1
                    )
                }
            ).delete(repository)
        }
        com.zhengzhou.cashflow.tools.EventMessages.sendMessageId(R.string.TransactionReport_transaction_deleted)
    }
}