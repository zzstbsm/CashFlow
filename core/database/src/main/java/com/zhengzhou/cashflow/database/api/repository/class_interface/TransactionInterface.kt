package com.zhengzhou.cashflow.database.api.repository.class_interface

import com.zhengzhou.cashflow.data.Transaction
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface TransactionInterface {
    suspend fun getTransaction(transactionId: UUID): Transaction?
    suspend fun addTransaction(transaction: Transaction)
    suspend fun updateTransaction(transaction: Transaction)
    suspend fun deleteTransaction(transaction: Transaction)

    /**
     * @param listOfUUID List of UUID associated to the list of wallet to process
     * @return List of transactions in the wallets indicated in [listOfUUID]
     */
    fun getTransactionListInListOfWallet(listOfUUID: List<UUID>): Flow<List<Transaction>>
    fun getTransactionListInWallet(walletUUID: UUID): Flow<List<Transaction>>
    fun getTransactionIsBlueprint(): Flow<List<Transaction>>

}