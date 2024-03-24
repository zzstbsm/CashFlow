package com.zhengzhou.cashflow.transaction_edit.view_model

import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.data.Tag
import com.zhengzhou.cashflow.data.TagEntry
import com.zhengzhou.cashflow.data.Transaction
import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.transaction_edit.TransactionSectionToShow

internal data class TransactionEditUiState(
    val isLoading: Boolean = true,

    val wallet: Wallet = Wallet.newEmpty(),
    val transaction: Transaction = Transaction.newEmpty(),
    val currentTagList: List<Tag> = listOf(),
    val secondaryWallet: Wallet? = null,

    val walletList: List<Wallet> = listOf(),
    val categoryList: List<Category> = listOf(),
    val tagListInDB: List<TagEntry> = listOf(),

    val amountString: String = "0",
    val transactionSectionToShow: TransactionSectionToShow,
)