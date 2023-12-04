package com.zhengzhou.cashflow.transaction_edit

import androidx.navigation.NavController
import com.zhengzhou.cashflow.tools.EventMessages
import com.zhengzhou.cashflow.transaction_edit.return_results.TransactionSaveResult

internal class TransactionEditActions {
    companion object {
        fun save(
            transactionEditViewModel: TransactionEditViewModel,
            navController: NavController,
        ) {
            val saveTransactionResult = transactionEditViewModel.saveTransaction()
            val saveTransactionSuccessful = saveTransactionResult == TransactionSaveResult.SUCCESS

            if (saveTransactionSuccessful) navController.popBackStack()

            EventMessages.sendMessageId(saveTransactionResult.errorMessage)
        }
    }
}