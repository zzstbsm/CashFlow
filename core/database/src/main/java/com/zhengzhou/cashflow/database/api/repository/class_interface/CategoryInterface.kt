package com.zhengzhou.cashflow.database.api.repository.class_interface

import com.zhengzhou.cashflow.data.Category
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface CategoryInterface {

    /**
     * Get a category from the data source based on category.id
     */
    suspend fun getCategory(categoryUUID: UUID) : Category?

    /**
     * Add a new category in the data source and return the same category with a new id.
     * @param category Category to add
     * @return Category with the new id
     */
    suspend fun addCategory(category: Category): Category

    /**
     * Update a category in the data source
     */
    suspend fun updateCategory(category: Category)

    /**
     * Delete a category from the data source
     */
    suspend fun deleteCategory(category: Category)

    /**
     * Get the list of all categories
     */
    fun getCategoryList(): Flow<List<Category>>

    /**
     * Retrieves the number of transactions that are in the category
     * @param category Category to analyze
     * @return Number of transactions with the input category
     */
    suspend fun getCategoryOccurrences(category: Category): Int

}