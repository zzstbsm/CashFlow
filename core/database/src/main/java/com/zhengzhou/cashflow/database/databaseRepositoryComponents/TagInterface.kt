package com.zhengzhou.cashflow.database.databaseRepositoryComponents

import com.zhengzhou.cashflow.data.Tag
import com.zhengzhou.cashflow.data.TagEntry
import com.zhengzhou.cashflow.database.DatabaseInstance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

internal interface TagInterface {

    suspend fun addTag(tag: Tag): Tag {

        val database = DatabaseInstance.get()

        val initializedTag = tag.copy(
            id = UUID.randomUUID()
        )
        val (tagTransaction, tagEntry) = initializedTag.separate()
        when (initializedTag.count) {
            0 -> return initializedTag
            1 -> database.databaseDao().addTagEntry(tagEntry)
            else -> database.databaseDao().updateTagEntry(tagEntry)
        }
        database.databaseDao().addTagTransaction(tagTransaction)
        return initializedTag
    }
    suspend fun updateTag(tag: Tag) {

        val database = DatabaseInstance.get()

        val (tagTransaction, tagEntry) = tag.separate()

        if (tag.count == 0) {
            database.databaseDao().deleteTagEntry(tagEntry)
            database.databaseDao().deleteTagTransaction(tagTransaction)
        }

        database.databaseDao().updateTagTransaction(tagTransaction)
        database.databaseDao().updateTagEntry(tagEntry)
    }
    suspend fun deleteTag(tag: Tag) {

        val database = DatabaseInstance.get()

        val (tagTransaction, tagEntry) = tag.separate()
        database.databaseDao().deleteTagTransaction(tagTransaction)
        if (tagEntry.count > 0) {
            database.databaseDao().updateTagEntry(tagEntry)
        } else {
            database.databaseDao().deleteTagEntry(tagEntry)
        }
    }
    fun getTagListFromTransaction(transactionUUID: UUID): Flow<List<Tag>> {

        val database = DatabaseInstance.get()

        return database.databaseDao().getTagTransactionFromTransaction(transactionUUID).map {
            it.mapNotNull {  tagTransaction ->
                val tagEntry: TagEntry? = database.databaseDao().getTagEntry(tagTransaction.tagUUID)
                Tag.merge(tagTransaction, tagEntry)
            }
        }
    }
}