package com.zhengzhou.cashflow.database.api.use_case.categoryUseCases.interfaces.features

import com.zhengzhou.cashflow.data.Category

interface AddCategoryInterface {
    /**
     * Insert a category in the data source
     */
    suspend fun addCategory(category: Category): Category?
}