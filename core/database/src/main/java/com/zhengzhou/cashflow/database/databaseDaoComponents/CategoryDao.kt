package com.zhengzhou.cashflow.database.databaseDaoComponents

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.zhengzhou.cashflow.data.Category
import kotlinx.coroutines.flow.Flow
import java.util.UUID

internal interface CategoryDao {

    // Default implementations
    @Query("SELECT * FROM category WHERE id=(:categoryUUID)")
    suspend fun getCategory(categoryUUID: UUID): Category?
    @Insert(entity = Category::class, onConflict = OnConflictStrategy.ABORT)
    suspend fun addCategory(category: Category)
    @Update(entity = Category::class)
    suspend fun updateCategory(category: Category)
    @Delete(entity = Category::class)
    suspend fun deleteCategory(category: Category)

    // Custom query based on the application
    @Query("SELECT * FROM category")
    fun getCategoryList(): Flow<List<Category>>
    @Query("SELECT * FROM category WHERE movement_type=(:transactionTypeId)")
    fun getCategoryListByTransactionType(transactionTypeId: Int): Flow<List<Category>>

    @Query("SELECT count(*) FROM movement WHERE id_category=(:categoryUUID)")
    suspend fun getCategoryOccurrences(categoryUUID: UUID): Int
}