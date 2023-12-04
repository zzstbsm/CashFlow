package com.zhengzhou.cashflow.transaction_edit

import androidx.navigation.NavController
import com.zhengzhou.cashflow.database.api.complex_data.TransactionSaveResult
import com.zhengzhou.cashflow.tools.EventMessages
import com.zhengzhou.cashflow.transaction_edit.view_model.TransactionEditViewModel

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