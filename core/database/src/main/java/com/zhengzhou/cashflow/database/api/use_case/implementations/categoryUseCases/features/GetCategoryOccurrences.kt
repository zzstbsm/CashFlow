package com.zhengzhou.cashflow.database.api.use_case.implementations.categoryUseCases.features

import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import com.zhengzhou.cashflow.database.api.use_case.interfaces.categoryUseCases.features.GetCategoryOccurrencesInterface

class GetCategoryOccurrences(
    private val repository: RepositoryInterface
) : GetCategoryOccurrencesInterface {
    override suspend fun getCategoryOccurrences(category: Category): Int {
        return repository.getCategoryOccurrences(category = category)
    }
}