package com.zhengzhou.cashflow.manage_categories.view_model

import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.data.TransactionType

internal data class ManageCategoriesUiState(
    val isLoading: Boolean = true,
    val listCategories: List<Category> = listOf(),

    val transactionType: TransactionType = TransactionType.Expense,
    val openCategory: Category? = null,
    val openCategoryOccurrences: Int = 0,
)