package com.zhengzhou.cashflow.database.api.use_case.interfaces.categoryUseCases.features

import com.zhengzhou.cashflow.data.Category
import kotlinx.coroutines.flow.Flow

interface GetCategoryListInterface {
    fun getCategoryList(): Flow<List<Category>>
}