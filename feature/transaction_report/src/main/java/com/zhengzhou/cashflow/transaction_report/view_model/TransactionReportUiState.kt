package com.zhengzhou.cashflow.transaction_report.view_model

import com.zhengzhou.cashflow.transaction_report.data_structure.TransactionFullForUI

internal data class TransactionReportUiState(
    val transactionFullForUI: TransactionFullForUI = TransactionFullForUI(),

    val isLoading: Boolean = true,
)