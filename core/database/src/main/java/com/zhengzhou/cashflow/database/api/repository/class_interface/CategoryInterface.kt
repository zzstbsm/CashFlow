package com.zhengzhou.cashflow.database.api.repository.class_interface

import com.zhengzhou.cashflow.data.Category
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface CategoryInterface {

    suspend fun getCategory(categoryUUID: UUID) : Category?
    suspend fun addCategory(category: Category): Category
    suspend fun updateCategory(category: Category)
    suspend fun deleteCategory(category: Category)
    fun getCategoryList(): Flow<List<Category>>
    suspend fun getCategoryOccurrences(category: Category): Int

}