package com.zhengzhou.cashflow.database.api.use_case.categoryUseCases.interfaces.features

import com.zhengzhou.cashflow.data.Category
import java.util.UUID

interface GetCategoryInterface {
    suspend fun getCategory(categoryUUID: UUID): Category?
}