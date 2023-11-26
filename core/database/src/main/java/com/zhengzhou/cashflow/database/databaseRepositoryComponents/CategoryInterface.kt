package com.zhengzhou.cashflow.database.databaseRepositoryComponents

import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.database.DatabaseInstance
import kotlinx.coroutines.flow.Flow
import java.util.UUID

internal interface CategoryInterface {

    suspend fun getCategory(categoryUUID: UUID) : Category? {

        val database = DatabaseInstance.get()

        return database.databaseDao().getCategory(categoryUUID = categoryUUID)
    }
    suspend fun addCategory(category: Category): Category {

        val database = DatabaseInstance.get()

        val initializedCategory = category.copy(id = UUID.randomUUID())
        database.databaseDao().addCategory(initializedCategory)
        return initializedCategory
    }
    suspend fun updateCategory(category: Category) {

        val database = DatabaseInstance.get()

        database.databaseDao().updateCategory(category)
    }
    suspend fun deleteCategory(category: Category) {

        val database = DatabaseInstance.get()

        database.databaseDao().deleteCategory(category)
    }
    fun getCategoryList(): Flow<List<Category>> {

        val database = DatabaseInstance.get()

        return database.databaseDao().getCategoryList()
    }
    suspend fun getCategoryOccurrences(category: Category): Int {

        val database = DatabaseInstance.get()

        return database.databaseDao().getCategoryOccurrences(categoryUUID = category.id)
    }

}