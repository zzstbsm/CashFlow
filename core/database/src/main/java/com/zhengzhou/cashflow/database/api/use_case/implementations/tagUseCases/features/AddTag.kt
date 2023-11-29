package com.zhengzhou.cashflow.database.api.use_case.implementations.tagUseCases.features

import com.zhengzhou.cashflow.data.Tag
import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import com.zhengzhou.cashflow.database.api.use_case.interfaces.tagUseCases.features.AddTagInterface


/**
 * class documentation
 */
class AddTag(
    private val repository: RepositoryInterface
): AddTagInterface {

    override suspend fun addTag(tag: Tag): Tag {
        return repository.addTag(tag = tag)
    }
}