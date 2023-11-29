package com.zhengzhou.cashflow.database.api.use_case.categoryUseCases

import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface

class UpdateCategory(
    private val repository: RepositoryInterface
) {
    suspend operator fun invoke(category: Category) {
        repository.updateCategory(category = category)
    }
}