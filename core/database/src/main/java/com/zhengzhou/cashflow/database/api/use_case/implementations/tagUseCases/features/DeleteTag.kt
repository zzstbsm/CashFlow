package com.zhengzhou.cashflow.database.api.use_case.implementations.tagUseCases.features

import com.zhengzhou.cashflow.data.Tag
import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import com.zhengzhou.cashflow.database.api.use_case.interfaces.tagUseCases.features.DeleteTagInterface

class DeleteTag(
    val repository: RepositoryInterface
) : DeleteTagInterface {
    override suspend fun deleteTag(tag: Tag) {



        val (tagTransaction, tagEntry) = tag.separate()
        dao.deleteTagTransaction(tagTransaction)
        if (tagEntry.count > 0) {
            dao.updateTagEntry(tagEntry)
        } else {
            dao.deleteTagEntry(tagEntry)
        }
    }
}