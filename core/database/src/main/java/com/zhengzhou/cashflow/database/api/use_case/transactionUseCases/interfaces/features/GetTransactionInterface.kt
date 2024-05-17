package com.zhengzhou.cashflow.database.api.use_case.transactionUseCases.interfaces.features

import com.zhengzhou.cashflow.data.Transaction
import com.zhengzhou.cashflow.data.Wallet
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface GetTransactionInterface {
    /**
     * Get transaction from transaction.id
     *  @param transactionUUID ID of the transaction to retrieve
     *  @return Transaction to retrieve
     */
    suspend fun getTransaction(transactionUUID: UUID): Transaction?

    /**
     * Get list of transactions that are blueprints/common transactions
     */
    fun getTransactionIsBlueprint(): Flow<List<Transaction>>
    /**
     * Given a list of wallet, retrieve all the transactions that are associated to the wallets in the list
     * @param walletList List of wallet to process
     * @return All the transactions in the wallets in input
     */
    fun getTransactionListInListOfWallet(walletList: List<Wallet>): Flow<List<Transaction>>
    /**
     * Get all the transactions in the wallet in input.
     * The wallet is both a primary or a secondary one for the transaction.
     */
    fun getTransactionListInWallet(walletUUID: UUID): Flow<List<Transaction>>
}