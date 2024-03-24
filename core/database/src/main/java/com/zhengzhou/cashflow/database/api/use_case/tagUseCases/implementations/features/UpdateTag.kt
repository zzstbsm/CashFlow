package com.zhengzhou.cashflow.database.api.use_case.tagUseCases.implementations.features

import com.zhengzhou.cashflow.data.Tag
import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import com.zhengzhou.cashflow.database.api.use_case.tagUseCases.interfaces.features.UpdateTagInterface

class UpdateTag(
    private val repository: RepositoryInterface
): UpdateTagInterface {
    override suspend fun updateTag(tag: Tag) {

        val (tagTransaction, tagEntry) = tag.separate()

        if (tag.count == 0) {
            repository.deleteTagEntry(tagEntry)
            repository.deleteTagTransaction(tagTransaction)
        }
        repository.updateTagTransaction(tagTransaction)
        repository.updateTagEntry(tagEntry)
    }
}