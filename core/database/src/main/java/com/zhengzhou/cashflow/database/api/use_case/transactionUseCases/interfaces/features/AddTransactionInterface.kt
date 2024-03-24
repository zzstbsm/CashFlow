package com.zhengzhou.cashflow.database.api.use_case.transactionUseCases.interfaces.features

import com.zhengzhou.cashflow.data.Transaction

interface AddTransactionInterface {
    /**
     * Insert a new uninitialized Transaction in the data source and return the initialized transaction
     * @param transaction Transaction to insert
     * @return Initialized transaction
     */
    suspend fun addTransaction(transaction: Transaction) : Transaction
}