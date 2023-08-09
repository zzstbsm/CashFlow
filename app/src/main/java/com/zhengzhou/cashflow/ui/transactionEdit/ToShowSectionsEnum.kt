package com.zhengzhou.cashflow.ui.transactionEdit

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.zhengzhou.cashflow.R
import com.zhengzhou.cashflow.data.TransactionType


enum class TransactionSectionToShow(
    @StringRes val text: Int,
    @DrawableRes val icon: Int,
    val transactionTypeList: List<TransactionType>,
) {
    AMOUNT(
        text = R.string.amount,
        icon = R.drawable.ic_euro,
        transactionTypeList = listOf(
            TransactionType.Move,
            TransactionType.Expense,
            TransactionType.Deposit
        )
    ),
    SECONDARY_WALLET(
        text = R.string.wallet,
        icon = R.drawable.ic_wallet,
        transactionTypeList = listOf(
            TransactionType.Move
        )
    ),
    CATEGORY(
        text = R.string.category,
        icon = R.drawable.ic_category,
        transactionTypeList = listOf(
            TransactionType.Deposit,
            TransactionType.Expense
        )
    ),
    TAG(
        text = R.string.tag,
        icon = R.drawable.ic_bookmark,
        transactionTypeList = listOf(
            TransactionType.Move,
            TransactionType.Expense,
            TransactionType.Deposit
        )
    ),
}

val TransactionEditScreenToChooseFunctionality = listOf(
    TransactionSectionToShow.SECONDARY_WALLET,
    TransactionSectionToShow.AMOUNT,
    TransactionSectionToShow.CATEGORY,
    TransactionSectionToShow.TAG,
)