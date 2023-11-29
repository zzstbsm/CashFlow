package com.zhengzhou.cashflow.database.api.use_case.categoryUseCases

import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface

class AddCategory(
    private val repository: RepositoryInterface
) {
    suspend operator fun invoke(category: Category): Category? {
        return repository.addCategory(category = category)
    }
}