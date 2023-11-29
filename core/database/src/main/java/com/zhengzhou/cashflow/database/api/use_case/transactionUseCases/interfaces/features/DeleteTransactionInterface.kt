package com.zhengzhou.cashflow.database.api.use_case.transactionUseCases.interfaces.features

import com.zhengzhou.cashflow.data.Transaction

interface DeleteTransactionInterface {
    /**
     * Delete a transaction from the data source
     */
    suspend fun deleteTransaction(transaction: Transaction)
}