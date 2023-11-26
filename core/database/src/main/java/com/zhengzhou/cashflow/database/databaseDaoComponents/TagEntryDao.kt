package com.zhengzhou.cashflow.database.databaseDaoComponents

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.zhengzhou.cashflow.data.TagEntry
import kotlinx.coroutines.flow.Flow
import java.util.UUID

internal interface TagEntryDao {

    // Default implementations
    @Query("SELECT * FROM tag_entry WHERE id=(:tagUUID)")
    suspend fun getTagEntry(tagUUID: UUID): TagEntry?
    @Insert(entity = TagEntry::class, onConflict = OnConflictStrategy.ABORT)
    suspend fun addTagEntry(tag: TagEntry)
    @Update(entity = TagEntry::class)
    suspend fun updateTagEntry(tag: TagEntry)
    @Delete(entity = TagEntry::class)
    suspend fun deleteTagEntry(tag: TagEntry)

    // Custom query based on the application
    @Query("SELECT * FROM tag_entry")
    fun getTagEntryList(): Flow<List<TagEntry>>
    @Query("UPDATE tag_entry SET count = count - 1 WHERE id=(:tagUUID)")
    suspend fun updateTagEntryDecreaseCounter(tagUUID: UUID)
    @Query("UPDATE tag_entry SET count = count + 1 WHERE id=(:tagUUID)")
    suspend fun updateTagEntryIncreaseCounter(tagUUID: UUID)
}
