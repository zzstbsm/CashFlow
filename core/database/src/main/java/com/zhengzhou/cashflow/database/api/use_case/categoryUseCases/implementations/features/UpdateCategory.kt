package com.zhengzhou.cashflow.database.api.use_case.categoryUseCases.implementations.features

import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import com.zhengzhou.cashflow.database.api.use_case.categoryUseCases.interfaces.features.UpdateCategoryInterface

class UpdateCategory(
    private val repository: RepositoryInterface
) : UpdateCategoryInterface {
    override suspend fun updateCategory(category: Category) {
        repository.updateCategory(category = category)
    }
}