package com.zhengzhou.cashflow.database.api.complex_data

import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.data.Transaction

data class TransactionAndCategory(
    val transaction: Transaction,
    val category: Category,
)