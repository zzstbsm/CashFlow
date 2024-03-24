package com.zhengzhou.cashflow.database.api.use_case.transactionUseCases.implementations.features

import com.zhengzhou.cashflow.data.Transaction
import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import com.zhengzhou.cashflow.database.api.use_case.transactionUseCases.interfaces.features.UpdateTransactionInterface

class UpdateTransaction(
    val repository: RepositoryInterface
) : UpdateTransactionInterface {
    override suspend fun updateTransaction(transaction: Transaction) {
        repository.updateTransaction(transaction)
    }
}