package com.zhengzhou.cashflow.database.api.use_case.transactionUseCases.implementations.features

import com.zhengzhou.cashflow.data.Transaction
import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import com.zhengzhou.cashflow.database.api.use_case.transactionUseCases.interfaces.features.AddTransactionInterface
import java.util.UUID

class AddTransaction(
    val repository: RepositoryInterface
) : AddTransactionInterface {
    override suspend fun addTransaction(transaction: Transaction): Transaction {
        val initializedTransaction = transaction.copy(
            id = UUID.randomUUID()
        )
        repository.addTransaction(initializedTransaction)
        return initializedTransaction
    }
}