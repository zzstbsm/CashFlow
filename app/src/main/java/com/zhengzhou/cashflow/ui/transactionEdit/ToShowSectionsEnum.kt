package com.zhengzhou.cashflow.ui.transactionEdit

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.zhengzhou.cashflow.R
import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.data.TransactionType
import java.util.*


enum class TransactionSectionToShow(
    @StringRes val text: Int,
    @DrawableRes val icon: Int,
) {
    AMOUNT(
        text = R.string.amount,
        icon = R.drawable.ic_euro
    ),
    DESCRIPTION(
        text = R.string.description,
        icon = R.drawable.ic_description
    ),
    TAG(
        text = R.string.tag,
        icon = R.drawable.ic_bookmark
    ),
    LOCATION(
        text = R.string.location,
        icon = R.drawable.ic_location
    ),
    CATEGORY(
        text = R.string.category,
        icon = R.drawable.ic_category
    ),
    MOVEMENT(
        text = R.string.movement_select_wallets,
        icon = R.drawable.ic_transfer
    )
}

val TransactionEditScreenToChooseFunctionality = listOf(
    TransactionSectionToShow.AMOUNT,
    TransactionSectionToShow.CATEGORY,
    TransactionSectionToShow.TAG,
    TransactionSectionToShow.LOCATION,
)