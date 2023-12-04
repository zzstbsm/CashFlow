package com.zhengzhou.cashflow.database.api.use_case.categoryUseCases.interfaces.features

import com.zhengzhou.cashflow.data.Category
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface GetCategoryInterface {
    suspend fun getCategory(categoryUUID: UUID): Category?
    fun getCategoryList(): Flow<List<Category>>
    suspend fun getCategoryOccurrences(category: Category): Int
}