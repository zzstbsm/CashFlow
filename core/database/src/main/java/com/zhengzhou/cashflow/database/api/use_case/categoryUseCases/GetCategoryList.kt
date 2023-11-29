package com.zhengzhou.cashflow.database.api.use_case.categoryUseCases

import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import kotlinx.coroutines.flow.Flow

class GetCategoryList(
    private val repository: RepositoryInterface
) {
    operator fun invoke(): Flow<List<Category>> {
        return repository.getCategoryList()
    }
}