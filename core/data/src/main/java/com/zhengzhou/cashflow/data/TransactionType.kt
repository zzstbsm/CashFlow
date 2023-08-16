package com.zhengzhou.cashflow.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

enum class TransactionType (
    val id: Int,
    @StringRes val text: Int,
    @StringRes val newText: Int,
    @DrawableRes val iconId: Int,
    val signInDB: Float,
) {
    Loading(
        id = 0,
        text = R.string.loading,
        newText = R.string.loading,
        iconId = R.drawable.ic_error,
        signInDB = 0f,
    ),
    Move(
        id = 1,
        text = R.string.move,
        newText = R.string.new_move,
        iconId = R.drawable.ic_transfer,
        signInDB = -1f,
    ),
    Deposit(
        id = 2,
        text = R.string.deposit,
        newText = R.string.new_deposit,
        iconId = R.drawable.ic_add,
        signInDB = 1f,
    ),
    Expense(
        id = 3,
        text = R.string.expense,
        newText = R.string.new_expense,
        iconId = R.drawable.ic_remove,
        signInDB = -1f,
    );

    companion object {
        fun setTransaction(
            id: Int
        ): TransactionType? {

            // Return the type of transaction
            TransactionType.values().forEach { transactionType: TransactionType ->
                if (transactionType.id == id) {
                    return transactionType
                }
            }

            // The id is not valid
            return null
        }

        fun getAllActive(): List<TransactionType> {
            return listOf(
                Deposit,
                Expense,
                Move,
            )
        }
    }
}