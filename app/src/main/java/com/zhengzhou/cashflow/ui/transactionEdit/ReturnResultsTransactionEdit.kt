package com.zhengzhou.cashflow.ui.transactionEdit

import com.zhengzhou.cashflow.R
import com.zhengzhou.cashflow.data.TransactionType

enum class TransactionSaveResult(
    val errorMessage: Int,
    val transactionTypeActiveList: List<TransactionType>
) {

    SUCCESS(
        errorMessage = R.string.TransactionEdit_transaction_saved,
        transactionTypeActiveList = TransactionType.getAllActive(),
    ),
    NO_AMOUNT(
        errorMessage = R.string.TransactionEdit_no_amount,
        transactionTypeActiveList = TransactionType.getAllActive(),
    ),
    NO_CATEGORY(
        errorMessage = R.string.TransactionEdit_no_category,
        transactionTypeActiveList = listOf(
            TransactionType.Expense,
            TransactionType.Deposit,
        ),
    );

    fun throwError(
        checkIfThrow: Boolean,
        transactionType: TransactionType,
    ): Boolean {
        return checkIfThrow && transactionType in this.transactionTypeActiveList
    }
}