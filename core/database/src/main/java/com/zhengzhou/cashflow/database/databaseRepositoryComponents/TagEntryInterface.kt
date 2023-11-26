package com.zhengzhou.cashflow.database.databaseRepositoryComponents

import com.zhengzhou.cashflow.data.TagEntry
import com.zhengzhou.cashflow.database.DatabaseInstance
import kotlinx.coroutines.flow.Flow

internal interface TagEntryInterface {
    fun getTagEntryList(): Flow<List<TagEntry>> {

        val database = DatabaseInstance.get()

        return database.databaseDao().getTagEntryList()
    }
}