package com.zhengzhou.cashflow.database.api.repository.class_interface

import com.zhengzhou.cashflow.data.Tag
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface TagInterface {

    suspend fun addTag(tag: Tag): Tag
    suspend fun updateTag(tag: Tag)
    suspend fun deleteTag(tag: Tag)
    fun getTagListFromTransaction(transactionUUID: UUID): Flow<List<Tag>>

}