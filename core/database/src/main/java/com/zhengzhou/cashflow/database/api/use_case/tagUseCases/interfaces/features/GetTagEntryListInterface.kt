package com.zhengzhou.cashflow.database.api.use_case.tagUseCases.interfaces.features

import com.zhengzhou.cashflow.data.TagEntry
import kotlinx.coroutines.flow.Flow

interface GetTagEntryListInterface {
    fun getTagEntryList(): Flow<List<TagEntry>>
}