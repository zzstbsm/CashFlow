package com.zhengzhou.cashflow.database.api.use_case.categoryUseCases.interfaces.features

import com.zhengzhou.cashflow.data.Category

interface UpdateCategoryInterface {
    suspend fun updateCategoryInterfaces(category: Category)
}