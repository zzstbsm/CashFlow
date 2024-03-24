package com.zhengzhou.cashflow.database.api.use_case.categoryUseCases.implementations.features

import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import com.zhengzhou.cashflow.database.api.use_case.categoryUseCases.interfaces.features.AddCategoryInterface
import java.util.UUID

class AddCategory(
    private val repository: RepositoryInterface
): AddCategoryInterface {
    override suspend fun addCategory(category: Category): Category {

        val initializedCategory = category.copy(id = UUID.randomUUID())
        repository.addCategory(initializedCategory)
        return initializedCategory
    }
}