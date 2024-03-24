package com.zhengzhou.cashflow.all_transactions

import java.util.Date

data class DateTabWithIndex(
    val date: Date,
    val indexOfTransaction: Int,
    val indexOfTab: Int,
)