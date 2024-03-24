package com.zhengzhou.cashflow.total_balance

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

enum class BalanceTabOptions(
    @StringRes val text: Int,
    @DrawableRes val icon: Int,
) {
    CATEGORIES(
        text = R.string.category,
        icon = R.drawable.ic_category,
    ),
    TRANSACTIONS(
        text = R.string.transaction,
        icon = R.drawable.ic_transfer,
    ),
}