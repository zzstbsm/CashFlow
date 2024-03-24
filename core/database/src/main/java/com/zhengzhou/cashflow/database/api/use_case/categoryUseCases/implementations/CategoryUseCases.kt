package com.zhengzhou.cashflow.database.api.use_case.categoryUseCases.implementations

import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import com.zhengzhou.cashflow.database.api.use_case.categoryUseCases.implementations.features.AddCategory
import com.zhengzhou.cashflow.database.api.use_case.categoryUseCases.implementations.features.DeleteCategory
import com.zhengzhou.cashflow.database.api.use_case.categoryUseCases.implementations.features.GetCategory
import com.zhengzhou.cashflow.database.api.use_case.categoryUseCases.implementations.features.UpdateCategory
import com.zhengzhou.cashflow.database.api.use_case.categoryUseCases.interfaces.CategoryUseCasesInterface
import com.zhengzhou.cashflow.database.api.use_case.categoryUseCases.interfaces.features.AddCategoryInterface
import com.zhengzhou.cashflow.database.api.use_case.categoryUseCases.interfaces.features.DeleteCategoryInterface
import com.zhengzhou.cashflow.database.api.use_case.categoryUseCases.interfaces.features.GetCategoryInterface
import com.zhengzhou.cashflow.database.api.use_case.categoryUseCases.interfaces.features.UpdateCategoryInterface

class CategoryUseCases(
    private val repository: RepositoryInterface
) : CategoryUseCasesInterface,
        AddCategoryInterface by AddCategory(repository),
        DeleteCategoryInterface by DeleteCategory(repository),
        GetCategoryInterface by GetCategory(repository),
        UpdateCategoryInterface by UpdateCategory(repository)
