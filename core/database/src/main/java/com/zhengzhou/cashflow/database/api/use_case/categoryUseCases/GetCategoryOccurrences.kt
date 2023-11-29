package com.zhengzhou.cashflow.database.api.use_case.categoryUseCases

import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface

class GetCategoryOccurrences(
    private val repository: RepositoryInterface
) {
    suspend operator fun invoke(category: Category): Int {
        return repository.getCategoryOccurrences(category = category)
    }
}