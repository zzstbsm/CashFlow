package com.zhengzhou.cashflow.ui.transactionEdit

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.zhengzhou.cashflow.R


enum class TransactionSectionToShow(
    @StringRes val text: Int,
    @DrawableRes val icon: Int,
) {
    AMOUNT(
        text = R.string.amount,
        icon = R.drawable.ic_euro
    ),
    CATEGORY(
        text = R.string.category,
        icon = R.drawable.ic_category
    ),
    TAG(
        text = R.string.tag,
        icon = R.drawable.ic_bookmark
    ),
}

val TransactionEditScreenToChooseFunctionality = listOf(
    TransactionSectionToShow.AMOUNT,
    TransactionSectionToShow.CATEGORY,
    TransactionSectionToShow.TAG,
)