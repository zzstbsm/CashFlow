package com.zhengzhou.cashflow.database.api.repository.class_interface

import com.zhengzhou.cashflow.data.Transaction
import com.zhengzhou.cashflow.data.Wallet
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface TransactionInterface {
    suspend fun getTransaction(transactionId: UUID): Transaction?
    suspend fun addTransaction(transaction: Transaction): Transaction
    suspend fun updateTransaction(transaction: Transaction)
    suspend fun deleteTransaction(transaction: Transaction)
    fun getTransactionListInListOfWallet(walletList: List<Wallet>): Flow<List<Transaction>>
    fun getTransactionListInWallet(walletUUID: UUID): Flow<List<Transaction>>
    fun getTransactionIsBlueprint(): Flow<List<Transaction>>

}