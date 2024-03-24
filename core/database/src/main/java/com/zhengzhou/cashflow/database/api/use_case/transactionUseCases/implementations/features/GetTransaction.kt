package com.zhengzhou.cashflow.database.api.use_case.transactionUseCases.implementations.features

import com.zhengzhou.cashflow.data.Transaction
import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import com.zhengzhou.cashflow.database.api.use_case.transactionUseCases.interfaces.features.GetTransactionInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.util.UUID

class GetTransaction(
    val repository: RepositoryInterface
) : GetTransactionInterface {
    override suspend fun getTransaction(transactionUUID: UUID): Transaction? {
        return repository.getTransaction(transactionUUID)
    }

    override fun getTransactionIsBlueprint(): Flow<List<Transaction>> {
        return repository.getTransactionIsBlueprint()
    }

    override fun getTransactionListInListOfWallet(walletList: List<Wallet>): Flow<List<Transaction>> {
        val idList: List<UUID> = walletList.map { it.id }
        return when(idList.size) {
            0 -> flowOf(listOf())
            else -> repository.getTransactionListInListOfWallet(idList)
        }
    }

    override fun getTransactionListInWallet(walletUUID: UUID): Flow<List<Transaction>> {
        return repository.getTransactionListInWallet(walletUUID)
    }
}