package com.zhengzhou.cashflow.database.databaseRepositoryComponents

import com.zhengzhou.cashflow.data.Transaction
import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.database.DatabaseInstance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.util.UUID

internal interface TransactionInterface {
    suspend fun getTransaction(transactionId: UUID): Transaction? {

        val database = DatabaseInstance.get()

        return database.databaseDao().getTransaction(transactionId)
    }
    suspend fun addTransaction(transaction: Transaction): Transaction {

        val database = DatabaseInstance.get()

        val initializedTransaction = transaction.copy(
            id = UUID.randomUUID()
        )
        database.databaseDao().addTransaction(initializedTransaction)
        return initializedTransaction
    }
    suspend fun updateTransaction(transaction: Transaction) {

        val database = DatabaseInstance.get()

        database.databaseDao().updateTransaction(transaction)
    }
    suspend fun deleteTransaction(transaction: Transaction) {

        val database = DatabaseInstance.get()

        database.databaseDao().deleteTransaction(transaction)
    }
    fun getTransactionListInListOfWallet(walletList: List<Wallet>): Flow<List<Transaction>> {

        val database = DatabaseInstance.get()

        val idList: List<UUID> = walletList.map { it.id }
        return when(idList.size) {
            0 -> flowOf(listOf())
            else -> database.databaseDao().getTransactionListInListOfWallet(idList)
        }
    }

    fun getTransactionListInWallet(walletUUID: UUID): Flow<List<Transaction>> {

        val database = DatabaseInstance.get()

        return database.databaseDao().getTransactionListInWallet(walletUUID)
    }
    fun getTransactionIsBlueprint(): Flow<List<Transaction>> {

        val database = DatabaseInstance.get()

        return database.databaseDao().getTransactionIsBlueprint()
    }
}