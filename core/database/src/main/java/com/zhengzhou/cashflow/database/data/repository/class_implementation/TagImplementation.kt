package com.zhengzhou.cashflow.database.data.repository.class_implementation

import com.zhengzhou.cashflow.data.Tag
import com.zhengzhou.cashflow.data.TagEntry
import com.zhengzhou.cashflow.database.api.repository.class_interface.TagInterface
import com.zhengzhou.cashflow.database.data.data_source.dao.RepositoryDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

internal open class TagImplementation(
    private val dao: RepositoryDao
) : TagInterface {

    override suspend fun addTag(tag: Tag): Tag {
        val initializedTag = tag.copy(
            id = UUID.randomUUID()
        )
        val (tagTransaction, tagEntry) = initializedTag.separate()
        when (initializedTag.count) {
            0 -> return initializedTag
            1 -> dao.addTagEntry(tagEntry)
            else -> dao.updateTagEntry(tagEntry)
        }
        dao.addTagTransaction(tagTransaction)
        return initializedTag
    }
    override suspend fun updateTag(tag: Tag) {

        val (tagTransaction, tagEntry) = tag.separate()

        if (tag.count == 0) {
            dao.deleteTagEntry(tagEntry)
            dao.deleteTagTransaction(tagTransaction)
        }

        dao.updateTagTransaction(tagTransaction)
        dao.updateTagEntry(tagEntry)
    }
    override suspend fun deleteTag(tag: Tag) {
    }
    override fun getTagListFromTransaction(transactionUUID: UUID): Flow<List<Tag>> {

        return dao.getTagTransactionFromTransaction(transactionUUID).map {
            it.mapNotNull {  tagTransaction ->
                val tagEntry: TagEntry? = dao.getTagEntry(tagTransaction.tagUUID)
                Tag.merge(tagTransaction, tagEntry)
            }
        }
    }

}