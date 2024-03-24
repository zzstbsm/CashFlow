package com.zhengzhou.cashflow.database.api.use_case.categoryUseCases.implementations.features

import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import com.zhengzhou.cashflow.database.api.use_case.categoryUseCases.interfaces.features.DeleteCategoryInterface

class DeleteCategory(
    private val repository: RepositoryInterface
): DeleteCategoryInterface {
    override suspend fun deleteCategory(category: Category) {
        repository.deleteCategory(category = category)
    }
}