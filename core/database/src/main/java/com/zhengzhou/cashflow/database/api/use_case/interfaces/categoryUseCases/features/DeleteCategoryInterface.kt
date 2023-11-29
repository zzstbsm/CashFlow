package com.zhengzhou.cashflow.database.api.use_case.interfaces.categoryUseCases.features

import com.zhengzhou.cashflow.data.Category

interface DeleteCategoryInterface {
    suspend fun deleteCategory(category: Category)
}