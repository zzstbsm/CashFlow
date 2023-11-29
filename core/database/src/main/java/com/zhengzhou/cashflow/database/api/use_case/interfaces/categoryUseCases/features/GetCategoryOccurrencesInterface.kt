package com.zhengzhou.cashflow.database.api.use_case.interfaces.categoryUseCases.features

import com.zhengzhou.cashflow.data.Category

interface GetCategoryOccurrencesInterface {
    suspend fun getCategoryOccurrences(category: Category): Int
}