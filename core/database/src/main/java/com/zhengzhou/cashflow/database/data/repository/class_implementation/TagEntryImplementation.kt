package com.zhengzhou.cashflow.database.data.repository.class_implementation

import com.zhengzhou.cashflow.data.TagEntry
import com.zhengzhou.cashflow.database.api.repository.class_interface.TagEntryInterface
import com.zhengzhou.cashflow.database.data.data_source.dao.RepositoryDao
import kotlinx.coroutines.flow.Flow
import java.util.UUID

internal open class TagEntryImplementation(
    private val dao: RepositoryDao
) : TagEntryInterface {
    override suspend fun addTagEntry(tagEntry: TagEntry) {
        dao.addTagEntry(tag = tagEntry)
    }

    override suspend fun deleteTagEntry(tagEntry: TagEntry) {
        dao.deleteTagEntry(tag = tagEntry)
    }
    override suspend fun getTagEntry(tagEntryUUID: UUID): TagEntry? {
        return dao.getTagEntry(tagEntryUUID = tagEntryUUID)
    }


    override fun getTagEntryList(): Flow<List<TagEntry>> {
        return dao.getTagEntryList()
    }

}