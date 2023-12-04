package com.zhengzhou.cashflow.transaction_report.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zhengzhou.cashflow.database.api.complex_data.TransactionFullForUI
import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import com.zhengzhou.cashflow.tools.EventMessages
import com.zhengzhou.cashflow.transaction_report.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID


internal class TransactionReportViewModel(
    val repository: RepositoryInterface,
    transactionUUID: UUID,
): ViewModel() {

    private var _uiState = MutableStateFlow(TransactionReportUiState())
    val uiState: StateFlow<TransactionReportUiState> = _uiState.asStateFlow()


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
        EventMessages.sendMessageId(R.string.transaction_deleted)
    }
}