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

// TODO: to remove in production
fun setDefaultExpenseCategories(): MutableList<Category>{
    return mutableListOf(
        Category(
            id = UUID(0L,1L),
            name = R.string.food,
            idIcon = R.drawable.ic_food,
            transactionTypeId = TransactionType.Expense.id,
        ),
        Category(
            id = UUID(0L,2L),
            name = R.string.health,
            idIcon = R.drawable.ic_health,
            transactionTypeId = TransactionType.Expense.id,
        ),
        Category(
            id = UUID(0L,3L),
            name = R.string.transportation,
            idIcon = R.drawable.ic_transportation,
            transactionTypeId = TransactionType.Expense.id,
        ),
        Category(
            id = UUID(0L,4L),
            name = R.string.travel,
            idIcon = R.drawable.ic_travel,
            transactionTypeId = TransactionType.Expense.id,
        ),
        Category(
            id = UUID(0L,5L),
            name = R.string.home,
            idIcon = R.drawable.ic_home,
            transactionTypeId = TransactionType.Expense.id,
        ),
    )
}
fun setDefaultDepositCategories(): MutableList<Category>{
    return mutableListOf(
        Category(
            id = UUID(1L,1L),
            name = R.string.salary,
            idIcon = R.drawable.ic_arrow_double_up,
            transactionTypeId = TransactionType.Deposit.id
        ),
    )
}

fun setDefaultMovementCategories(): MutableList<Category>{
    return mutableListOf(
        Category(
            id = UUID(2L,1L),
            name = R.string.move,
            idIcon = R.drawable.ic_transfer,
            transactionTypeId = TransactionType.Deposit.id
        ),
    )
}