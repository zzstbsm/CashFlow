package com.zhengzhou.cashflow.database.api.use_case.categoryUseCases.implementations.features

import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import com.zhengzhou.cashflow.database.api.use_case.categoryUseCases.interfaces.features.GetCategoryInterface
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class GetCategory(
    private val repository: RepositoryInterface
) : GetCategoryInterface {
    override suspend fun getCategory(categoryUUID: UUID): Category? {
        return repository.getCategory(categoryUUID = categoryUUID)
    }
    override fun getCategoryList(): Flow<List<Category>> {
        return repository.getCategoryList()
    }
    override suspend fun getCategoryOccurrences(category: Category): Int {
        return repository.getCategoryOccurrences(category = category)
    }
}