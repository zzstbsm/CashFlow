package com.zhengzhou.cashflow.ui.commonTransactions

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class CommonTransactionsUiState(
    val isLoading: Boolean = false,
)

class CommonTransactionsViewModel: ViewModel() {

    private var _uiState = MutableStateFlow(CommonTransactionsUiState())
    val uiState: StateFlow<CommonTransactionsUiState> = _uiState.asStateFlow()

    init {

    }

}