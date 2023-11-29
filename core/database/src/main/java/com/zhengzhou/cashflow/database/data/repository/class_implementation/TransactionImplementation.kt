package com.zhengzhou.cashflow.database.data.repository.class_implementation

import com.zhengzhou.cashflow.data.Transaction
import com.zhengzhou.cashflow.database.api.repository.class_interface.TransactionInterface
import com.zhengzhou.cashflow.database.data.data_source.dao.RepositoryDao
import kotlinx.coroutines.flow.Flow
import java.util.UUID

internal class TransactionImplementation(
    private val dao: RepositoryDao
) : TransactionInterface {
    override suspend fun getTransaction(transactionId: UUID): Transaction? {
        return dao.getTransaction(transactionId)
    }
    override suspend fun addTransaction(transaction: Transaction) {
        dao.addTransaction(transaction = transaction)
    }
    override suspend fun updateTransaction(transaction: Transaction) {
        dao.updateTransaction(transaction)
    }
    override suspend fun deleteTransaction(transaction: Transaction) {
        dao.deleteTransaction(transaction)
    }

    override fun getTransactionListInListOfWallet(listOfUUID: List<UUID>): Flow<List<Transaction>> {
        return dao.getTransactionListInListOfWallet(listOfUUID)
    }

    override fun getTransactionListInWallet(walletUUID: UUID): Flow<List<Transaction>> {
        return dao.getTransactionListInWallet(walletUUID)
    }
    override fun getTransactionIsBlueprint(): Flow<List<Transaction>> {
        return dao.getTransactionIsBlueprint()
    }
}