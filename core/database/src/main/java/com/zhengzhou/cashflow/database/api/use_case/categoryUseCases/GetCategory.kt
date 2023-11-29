package com.zhengzhou.cashflow.database.api.use_case.categoryUseCases

import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import java.util.UUID

class GetCategory(
    private val repository: RepositoryInterface
) {
    suspend operator fun invoke(categoryUUID: UUID): Category? {
        return repository.getCategory(categoryUUID = categoryUUID)
    }
}