package com.zhengzhou.cashflow.ui.allTransactions

import java.util.Date

data class DateTabWithIndex(
    val date: Date,
    val indexOfTransaction: Int,
    val indexOfTab: Int,
)