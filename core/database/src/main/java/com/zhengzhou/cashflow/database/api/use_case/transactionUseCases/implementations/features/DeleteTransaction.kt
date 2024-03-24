package com.zhengzhou.cashflow.database.api.use_case.transactionUseCases.implementations.features

import com.zhengzhou.cashflow.data.Transaction
import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import com.zhengzhou.cashflow.database.api.use_case.transactionUseCases.interfaces.features.DeleteTransactionInterface

class DeleteTransaction(
    val repository: RepositoryInterface
) : DeleteTransactionInterface {
    override suspend fun deleteTransaction(transaction: Transaction) {
        repository.deleteTransaction(transaction)
    }
}