package com.zhengzhou.cashflow.database.api.use_case.implementations.categoryUseCases.features

import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import com.zhengzhou.cashflow.database.api.use_case.interfaces.categoryUseCases.features.UpdateCategoryInterface

class UpdateCategory(
    private val repository: RepositoryInterface
) : UpdateCategoryInterface {
    override suspend fun updateCategoryInterfaces(category: Category) {
        repository.updateCategory(category = category)
    }
}