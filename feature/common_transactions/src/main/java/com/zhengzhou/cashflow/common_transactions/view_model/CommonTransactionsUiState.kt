package com.zhengzhou.cashflow.common_transactions.view_model

import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.database.api.complex_data.TransactionFullForUI

data class CommonTransactionsUiState(
    val isLoading: Boolean = true,
    val walletList: List<Wallet> = listOf(),

    val transactionFullForUIList: List<TransactionFullForUI> = listOf(),
)