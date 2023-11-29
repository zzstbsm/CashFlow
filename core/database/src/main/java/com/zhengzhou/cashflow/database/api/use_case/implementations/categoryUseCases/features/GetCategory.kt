package com.zhengzhou.cashflow.database.api.use_case.implementations.categoryUseCases.features

import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import com.zhengzhou.cashflow.database.api.use_case.interfaces.categoryUseCases.features.GetCategoryInterface
import java.util.UUID

class GetCategory(
    private val repository: RepositoryInterface
) : GetCategoryInterface {
    override suspend fun getCategory(categoryUUID: UUID): Category? {
        return repository.getCategory(categoryUUID = categoryUUID)
    }
}