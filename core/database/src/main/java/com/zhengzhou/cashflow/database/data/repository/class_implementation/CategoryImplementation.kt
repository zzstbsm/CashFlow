package com.zhengzhou.cashflow.database.data.repository.class_implementation

import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.database.api.repository.class_interface.CategoryInterface
import com.zhengzhou.cashflow.database.data.data_source.dao.RepositoryDao
import kotlinx.coroutines.flow.Flow
import java.util.UUID

internal open class CategoryImplementation(
    private val dao: RepositoryDao
) : CategoryInterface {

    override suspend fun getCategory(categoryUUID: UUID) : Category? {
        return dao.getCategory(categoryUUID = categoryUUID)
    }
    override suspend fun addCategory(category: Category): Category {

        val initializedCategory = category.copy(id = UUID.randomUUID())
        dao.addCategory(initializedCategory)
        return initializedCategory
    }
    override suspend fun updateCategory(category: Category) {
        dao.updateCategory(category)
    }
    override suspend fun deleteCategory(category: Category) {
        dao.deleteCategory(category)
    }
    override fun getCategoryList(): Flow<List<Category>> {
        return dao.getCategoryList()
    }
    override suspend fun getCategoryOccurrences(category: Category): Int {
        return dao.getCategoryOccurrences(categoryUUID = category.id)
    }

}