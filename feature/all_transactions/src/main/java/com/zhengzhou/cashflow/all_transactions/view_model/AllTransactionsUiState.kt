package com.zhengzhou.cashflow.all_transactions.view_model

import com.zhengzhou.cashflow.all_transactions.DateTabWithIndex
import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.database.api.complex_data.TransactionAndCategory

data class AllTransactionsUiState(
    val isLoading: Boolean = true,

    val wallet: Wallet = Wallet.newEmpty(),

    val transactionListToShow: List<TransactionAndCategory> = listOf(),

    val dateTabWithIndexList: List<DateTabWithIndex> = listOf(),
    val shownTab: Int = -1
)