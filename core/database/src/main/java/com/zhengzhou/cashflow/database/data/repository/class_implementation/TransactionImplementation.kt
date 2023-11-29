package com.zhengzhou.cashflow.database.data.repository.class_implementation

import com.zhengzhou.cashflow.data.Transaction
import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.database.api.repository.class_interface.TransactionInterface
import com.zhengzhou.cashflow.database.data.data_source.dao.RepositoryDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.util.UUID

internal open class TransactionImplementation(
    private val dao: RepositoryDao
) : TransactionInterface {
    override suspend fun getTransaction(transactionId: UUID): Transaction? {
        return dao.getTransaction(transactionId)
    }
    override suspend fun addTransaction(transaction: Transaction): Transaction {
        val initializedTransaction = transaction.copy(
            id = UUID.randomUUID()
        )
        dao.addTransaction(initializedTransaction)
        return initializedTransaction
    }
    override suspend fun updateTransaction(transaction: Transaction) {
        dao.updateTransaction(transaction)
    }
    override suspend fun deleteTransaction(transaction: Transaction) {
        dao.deleteTransaction(transaction)
    }
    override fun getTransactionListInListOfWallet(walletList: List<Wallet>): Flow<List<Transaction>> {
        val idList: List<UUID> = walletList.map { it.id }
        return when(idList.size) {
            0 -> flowOf(listOf())
            else -> dao.getTransactionListInListOfWallet(idList)
        }
    }

    override fun getTransactionListInWallet(walletUUID: UUID): Flow<List<Transaction>> {
        return dao.getTransactionListInWallet(walletUUID)
    }
    override fun getTransactionIsBlueprint(): Flow<List<Transaction>> {
        return dao.getTransactionIsBlueprint()
    }
}