package com.zhengzhou.cashflow.database.api.use_case.implementations.categoryUseCases

import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import com.zhengzhou.cashflow.database.api.use_case.implementations.categoryUseCases.features.AddCategory
import com.zhengzhou.cashflow.database.api.use_case.implementations.categoryUseCases.features.DeleteCategory
import com.zhengzhou.cashflow.database.api.use_case.implementations.categoryUseCases.features.GetCategory
import com.zhengzhou.cashflow.database.api.use_case.implementations.categoryUseCases.features.GetCategoryList
import com.zhengzhou.cashflow.database.api.use_case.implementations.categoryUseCases.features.GetCategoryOccurrences
import com.zhengzhou.cashflow.database.api.use_case.implementations.categoryUseCases.features.UpdateCategory

data class CategoryUseCases(
    val addCategory: AddCategory,
    val deleteCategory: DeleteCategory,
    val getCategory: GetCategory,
    val getCategoryList: GetCategoryList,
    val getCategoryOccurrences: GetCategoryOccurrences,
    val updateCategory: UpdateCategory,

    ) {
    constructor(repository: RepositoryInterface): this(
        addCategory = AddCategory(repository),
        deleteCategory = DeleteCategory(repository),
        getCategory = GetCategory(repository),
        getCategoryList = GetCategoryList(repository),
        getCategoryOccurrences = GetCategoryOccurrences(repository),
        updateCategory = UpdateCategory(repository),
    )
}