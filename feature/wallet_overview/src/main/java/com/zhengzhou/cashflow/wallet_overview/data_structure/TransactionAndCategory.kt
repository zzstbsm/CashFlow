package com.zhengzhou.cashflow.wallet_overview.data_structure

import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.data.Transaction

internal data class TransactionAndCategory(
    val transaction: Transaction,
    val category: Category,
)