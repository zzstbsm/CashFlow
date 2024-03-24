package com.zhengzhou.cashflow.database.api.use_case.tagUseCases.implementations.features

import com.zhengzhou.cashflow.data.Tag
import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import com.zhengzhou.cashflow.database.api.use_case.tagUseCases.interfaces.features.DeleteTagInterface

class DeleteTag(
    val repository: RepositoryInterface
) : DeleteTagInterface {
    override suspend fun deleteTag(tag: Tag) {

        val (tagTransaction, tagEntry) = tag.separate()
        repository.deleteTagTransaction(tagTransaction)
        if (tagEntry.count > 0) {
            repository.updateTagEntry(tagEntry)
        } else {
            repository.deleteTagEntry(tagEntry)
        }
    }
}