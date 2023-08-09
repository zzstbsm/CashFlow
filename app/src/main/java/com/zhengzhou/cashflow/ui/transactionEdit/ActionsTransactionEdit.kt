package com.zhengzhou.cashflow.ui.transactionEdit

import androidx.navigation.NavController
import com.zhengzhou.cashflow.tools.EventMessages

class TransactionEditActions {
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