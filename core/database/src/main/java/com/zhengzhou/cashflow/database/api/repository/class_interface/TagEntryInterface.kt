package com.zhengzhou.cashflow.database.api.repository.class_interface

import com.zhengzhou.cashflow.data.TagEntry
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface TagEntryInterface {
    suspend fun addTagEntry(tagEntry: TagEntry)
    suspend fun deleteTagEntry(tagEntry: TagEntry)
    suspend fun getTagEntry(tagEntryUUID: UUID): TagEntry?
    fun getTagEntryList(): Flow<List<TagEntry>>


}