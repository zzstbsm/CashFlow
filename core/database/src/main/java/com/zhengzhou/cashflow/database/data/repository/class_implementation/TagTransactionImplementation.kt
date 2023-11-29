package com.zhengzhou.cashflow.database.data.repository.class_implementation

import com.zhengzhou.cashflow.data.TagTransaction
import com.zhengzhou.cashflow.database.api.repository.class_interface.TagTransactionInterface
import com.zhengzhou.cashflow.database.data.data_source.dao.RepositoryDao
import kotlinx.coroutines.flow.Flow
import java.util.UUID

internal class TagTransactionImplementation(
    val dao: RepositoryDao
) : TagTransactionInterface {
    override suspend fun addTagTransaction(tagTransaction: TagTransaction) {
        dao.addTagTransaction(tagTransaction = tagTransaction)
    }

    override suspend fun deleteTagTransaction(tagTransaction: TagTransaction) {
        dao.deleteTagTransaction(tagTransaction = tagTransaction)
    }

    override fun getTagTransactionListFromTransaction(transactionUUID: UUID): Flow<List<TagTransaction>> {
        return dao.getTagTransactionListFromTransaction(transactionUUID = transactionUUID)
    }

    override suspend fun updateTagTransaction(tagTransaction: TagTransaction) {
        dao.updateTagTransaction(tagTransaction = tagTransaction)
    }
}