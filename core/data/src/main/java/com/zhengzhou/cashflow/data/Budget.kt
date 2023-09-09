package com.zhengzhou.cashflow.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

data class Budget(
    val id: UUID,
    val walletUUID: UUID,
    val currency: Currency,
    val startDate: Date,
    val endDate: Date,

    val budgetPerCategoryUUID: UUID,
    val categoryUUID: UUID,
    val maxAmount: Float,
) {
    companion object {
        fun merge(
            budgetPerPeriod: BudgetPerPeriod,
            budgetPerCategory: BudgetPerCategory,
        ): Budget {
            return Budget(
                id = budgetPerPeriod.id,
                walletUUID = budgetPerPeriod.walletUUID,
                currency = budgetPerPeriod.currency,
                startDate = budgetPerPeriod.startDate,
                endDate = budgetPerPeriod.endDate,

                budgetPerCategoryUUID = budgetPerCategory.id,
                categoryUUID = budgetPerCategory.categoryUUID,
                maxAmount = budgetPerCategory.maxAmount
            )
        }
    }

    fun separate(): Pair<BudgetPerPeriod,BudgetPerCategory> {
        val budgetPerPeriod = BudgetPerPeriod(
            id = id,
            walletUUID = walletUUID,
            currency = currency,
            startDate = startDate,
            endDate = endDate,
        )
        val budgetPerCategory = BudgetPerCategory(
            id = budgetPerCategoryUUID,
            budgetPerPeriodUUID = id,
            categoryUUID = categoryUUID,
            maxAmount = maxAmount,
        )
        return Pair(budgetPerPeriod,budgetPerCategory)
    }
}

@Entity(tableName = "budget_per_period")
data class BudgetPerPeriod(
    @ColumnInfo(name = "id")
    @PrimaryKey val id: UUID,
    @ColumnInfo(name = "wallet_id")
    val walletUUID: UUID,
    @ColumnInfo(name = "currency")
    val currency: Currency,
    @ColumnInfo(name = "start_date")
    val startDate: Date,
    @ColumnInfo(name = "end_date")
    val endDate: Date,
)

@Entity(tableName = "budget_per_category")
data class BudgetPerCategory(
    @ColumnInfo(name = "id")
    @PrimaryKey val id: UUID,
    @ColumnInfo(name = "budget_per_period_id")
    val budgetPerPeriodUUID: UUID,
    @ColumnInfo(name = "category_id")
    val categoryUUID: UUID,
    @ColumnInfo(name = "max_amount")
    val maxAmount: Float,
)