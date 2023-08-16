package com.zhengzhou.cashflow.ui.balance

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.zhengzhou.cashflow.R

enum class BalanceTabOptions(
    @StringRes val text: Int,
    @DrawableRes val icon: Int,
) {
    CATEGORIES(
        text = R.string.Balance_category,
        icon = R.drawable.ic_category,
    ),
    TRANSACTIONS(
        text = R.string.Balance_transaction,
        icon = R.drawable.ic_transfer,
    ),
}