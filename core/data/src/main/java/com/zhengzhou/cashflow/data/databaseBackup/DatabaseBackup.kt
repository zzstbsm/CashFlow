package com.zhengzhou.cashflow.data.databaseBackup

import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.data.TagEntry
import com.zhengzhou.cashflow.data.TagTransaction
import com.zhengzhou.cashflow.data.Transaction
import com.zhengzhou.cashflow.data.Wallet
import kotlinx.serialization.Serializable

@Serializable
data class DatabaseBackup(
    val categoryList: List<Category>,
    val tagEntryList: List<TagEntry>,
    val tagTransactionList: List<TagTransaction>,
    val transactionList: List<Transaction>,
    val walletList: List<Wallet>
)