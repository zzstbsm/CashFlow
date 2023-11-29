package com.zhengzhou.cashflow.database.api.use_case.categoryUseCases.interfaces.features

import com.zhengzhou.cashflow.data.Category

interface GetCategoryOccurrencesInterface {
    suspend fun getCategoryOccurrences(category: Category): Int
}