package com.zhengzhou.cashflow.ui.transactionEdit

import androidx.lifecycle.ViewModel
import com.zhengzhou.cashflow.data.Transaction
import com.zhengzhou.cashflow.database.DatabaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

data class TransactionEditUiState(
    val transaction: Transaction = Transaction(),
)

class TransactionEditViewModel(
    transactionUUID: UUID
) : ViewModel() {

    private var _uiState = MutableStateFlow(TransactionEditUiState())
    val uiState: StateFlow<TransactionEditUiState> = _uiState.asStateFlow()

    private val repository = DatabaseRepository.get()

    init {

    }

}