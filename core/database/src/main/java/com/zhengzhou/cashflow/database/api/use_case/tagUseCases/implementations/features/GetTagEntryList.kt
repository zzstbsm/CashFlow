package com.zhengzhou.cashflow.database.api.use_case.tagUseCases.implementations.features

import com.zhengzhou.cashflow.data.TagEntry
import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import com.zhengzhou.cashflow.database.api.use_case.tagUseCases.interfaces.features.GetTagEntryListInterface
import kotlinx.coroutines.flow.Flow

class GetTagEntryList(
    private val repository: RepositoryInterface
) : GetTagEntryListInterface {
    override fun getTagEntryList(): Flow<List<TagEntry>> {
        return repository.getTagEntryList()
    }
}