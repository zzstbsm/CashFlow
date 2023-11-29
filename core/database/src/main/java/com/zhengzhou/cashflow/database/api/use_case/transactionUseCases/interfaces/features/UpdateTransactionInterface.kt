package com.zhengzhou.cashflow.database.api.use_case.transactionUseCases.interfaces.features

import com.zhengzhou.cashflow.data.Transaction

interface UpdateTransactionInterface {
    /**
     * Update a transaction already in the data source
     */
    suspend fun updateTransaction(transaction: Transaction)
}