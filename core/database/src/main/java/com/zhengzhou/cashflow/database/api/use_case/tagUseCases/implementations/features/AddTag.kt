package com.zhengzhou.cashflow.database.api.use_case.tagUseCases.implementations.features

import com.zhengzhou.cashflow.data.Tag
import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import com.zhengzhou.cashflow.database.api.use_case.tagUseCases.interfaces.features.AddTagInterface
import java.util.UUID

class AddTag(
    private val repository: RepositoryInterface
): AddTagInterface {

    /**
     * Add a new uninitialized tag in the database and return the tag with an initialized id
     * @param tag Uninitialized Tag
     * @return Tag with a new id
     */
    override suspend fun addTag(tag: Tag): Tag {
        val initializedTag = tag.copy(
            id = UUID.randomUUID()
        )
        val (tagTransaction, tagEntry) = initializedTag.separate()
        when (initializedTag.count) {
            0 -> return initializedTag
            1 -> repository.addTagEntry(tagEntry)
            else -> repository.updateTagEntry(tagEntry)
        }
        repository.addTagTransaction(tagTransaction)
        return initializedTag
    }
}