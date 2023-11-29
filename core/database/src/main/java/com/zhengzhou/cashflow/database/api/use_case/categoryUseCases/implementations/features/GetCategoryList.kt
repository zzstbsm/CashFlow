package com.zhengzhou.cashflow.database.api.use_case.categoryUseCases.implementations.features

import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import com.zhengzhou.cashflow.database.api.use_case.categoryUseCases.interfaces.features.GetCategoryListInterface
import kotlinx.coroutines.flow.Flow

class GetCategoryList(
    private val repository: RepositoryInterface
) : GetCategoryListInterface {
    override fun getCategoryList(): Flow<List<Category>> {
        return repository.getCategoryList()
    }
}