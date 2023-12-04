package com.zhengzhou.cashflow.transaction_edit.return_results

import com.zhengzhou.cashflow.data.TransactionType
import com.zhengzhou.cashflow.transaction_edit.R

enum class TransactionSaveResult(
    val errorMessage: Int,
    val transactionTypeActiveList: List<TransactionType>
) {

    SUCCESS(
        errorMessage = R.string.transaction_saved,
        transactionTypeActiveList = TransactionType.getAllActive(),
    ),
    NO_AMOUNT(
        errorMessage = R.string.no_amount,
        transactionTypeActiveList = TransactionType.getAllActive(),
    ),
    NO_CATEGORY(
        errorMessage = R.string.no_category,
        transactionTypeActiveList = listOf(
            TransactionType.Expense,
            TransactionType.Deposit,
        ),
    );

    fun checkIfThrowError(
        checkIfThrow: Boolean,
        transactionType: TransactionType,
    ): Boolean {
        return checkIfThrow && transactionType in this.transactionTypeActiveList
    }
}