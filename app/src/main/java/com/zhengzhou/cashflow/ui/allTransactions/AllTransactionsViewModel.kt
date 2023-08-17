package com.zhengzhou.cashflow.ui.allTransactions

import androidx.lifecycle.ViewModel
import com.zhengzhou.cashflow.data.Currency
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

data class AllTransactionsUiState(
    val isLoading: Boolean = true,
)

class AllTransactionsViewModel(
    walletUUID: UUID,
    categoryUUID: UUID,
    currency: Currency,
): ViewModel() {

    private var _uiState = MutableStateFlow(AllTransactionsUiState())
    val uiState: StateFlow<AllTransactionsUiState> = _uiState.asStateFlow()

    init {

    }

}