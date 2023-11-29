package com.zhengzhou.cashflow.database.api.repository.class_interface

import com.zhengzhou.cashflow.data.TagTransaction
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface TagTransactionInterface {
    /**
     * Add a new TagTransaction
     */
    suspend fun addTagTransaction(tagTransaction: TagTransaction)
    /**
     * Delete a TagTransaction
     * @param tagTransaction TagTransaction to delete
     */
    suspend fun deleteTagTransaction(tagTransaction: TagTransaction)
    /**
     * Get list of TagTransaction in a transaction from Transaction.id
     */
    fun getTagTransactionListFromTransaction(transactionUUID: UUID): Flow<List<TagTransaction>>
    /**
     * Update a TagTransaction in the data source
     */
    suspend fun updateTagTransaction(tagTransaction: TagTransaction)
}