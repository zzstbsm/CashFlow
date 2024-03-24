package com.zhengzhou.cashflow.database.data.data_source.dao.class_dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.zhengzhou.cashflow.data.TagTransaction
import kotlinx.coroutines.flow.Flow
import java.util.UUID

internal interface TagTransactionDao {

    // Default implementations
    @Query("SELECT * FROM tag_transaction WHERE id=(:tagTransactionUUID)")
    suspend fun getTagTransaction(tagTransactionUUID: UUID): TagTransaction?
    @Insert(entity = TagTransaction::class, onConflict = OnConflictStrategy.ABORT)
    suspend fun addTagTransaction(tagTransaction: TagTransaction)
    @Update(entity = TagTransaction::class)
    suspend fun updateTagTransaction(tagTransaction: TagTransaction)
    @Delete(entity = TagTransaction::class)
    suspend fun deleteTagTransaction(tagTransaction: TagTransaction)

    // Custom query based on the application
    @Query("SELECT * FROM tag_transaction WHERE id_movement=(:transactionUUID)")
    fun getTagTransactionListFromTransaction(transactionUUID: UUID): Flow<List<TagTransaction>>
}