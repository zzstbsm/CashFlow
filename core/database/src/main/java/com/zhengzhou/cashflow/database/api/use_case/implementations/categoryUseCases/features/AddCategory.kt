package com.zhengzhou.cashflow.database.api.use_case.implementations.categoryUseCases.features

import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import com.zhengzhou.cashflow.database.api.use_case.interfaces.categoryUseCases.features.AddCategoryInterface

class AddCategory(
    private val repository: RepositoryInterface
): AddCategoryInterface {
    override suspend fun addCategory(category: Category): Category? {
        return repository.addCategory(category = category)
    }
}