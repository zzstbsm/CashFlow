package com.zhengzhou.cashflow.database.data.data_source.dao.class_dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.zhengzhou.cashflow.data.BudgetPerCategory
import com.zhengzhou.cashflow.data.BudgetPerPeriod
import java.util.UUID

internal interface BudgetPerCategoryDao {

    // Default implementations
    @Query("SELECT * FROM budget_per_category WHERE id=(:budgetPerCategoryUUID)")
    suspend fun getBudgetPerCategory(budgetPerCategoryUUID: UUID): BudgetPerCategory?
    @Insert(entity = BudgetPerCategory::class, onConflict = OnConflictStrategy.ABORT)
    suspend fun addBudgetPerCategory(budgetPerCategory: BudgetPerCategory)
    @Update(entity = BudgetPerCategory::class)
    suspend fun updateBudgetPerCategory(budgetPerCategory: BudgetPerCategory)
    @Delete(entity = BudgetPerCategory::class)
    suspend fun deleteBudgetPerCategory(budgetPerCategory: BudgetPerCategory)

    // Custom query based on the application
}

internal interface BudgetPerPeriodDao {

    // Default implementations
    @Query("SELECT * FROM budget_per_period WHERE id=(:budgetPerPeriodUUID)")
    suspend fun getBudgetPerPeriod(budgetPerPeriodUUID: UUID): BudgetPerPeriod?
    @Insert(entity = BudgetPerPeriod::class, onConflict = OnConflictStrategy.ABORT)
    suspend fun addBudgetPerPeriod(budgetPerPeriod: BudgetPerPeriod)
    @Update(entity = BudgetPerPeriod::class)
    suspend fun updateBudgetPerPeriod(budgetPerPeriod: BudgetPerPeriod)
    @Delete(entity = BudgetPerPeriod::class)
    suspend fun deleteBudgetPerPeriod(budgetPerPeriod: BudgetPerPeriod)

    // Custom query based on the application

}