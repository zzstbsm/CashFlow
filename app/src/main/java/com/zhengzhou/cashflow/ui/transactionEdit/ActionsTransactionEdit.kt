package com.zhengzhou.cashflow.ui.transactionEdit

import androidx.navigation.NavController

class TransactionEditActions {
    companion object {
        fun save(
            transactionEditViewModel: TransactionEditViewModel,
            navController: NavController,
        ) {
            val saveTransactionResult = transactionEditViewModel.saveTransaction()
            val saveTransactionSuccessful = saveTransactionResult == TransactionSaveResult.SUCCESS

            if (saveTransactionSuccessful) navController.popBackStack()

            com.zhengzhou.cashflow.tools.EventMessages.sendMessageId(saveTransactionResult.errorMessage)
        }
    }
}