package com.zhengzhou.cashflow.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zhengzhou.cashflow.tools.getFirstDayOfCurrentMonth
import com.zhengzhou.cashflow.tools.getLastDayOfCurrentMonth
import java.util.*

/*
 *
 * The table "period" contains information regarding associated wallet, start date and end date.
 * The table "budget" contains the budget of a single category of one wallet, it is
 * identified from one periodId
 *
 */

@Entity(tableName = "budget_amount")
data class Budget(
    @PrimaryKey
    val id: UUID = UUID(0L,0L),
    @ColumnInfo(name = "period_id")
    val periodId: UUID = UUID(0L,0L),
    @ColumnInfo(name = "category_id")
    val categoryId: UUID = UUID(0L,0L),
    @ColumnInfo(name = "max_expense")
    val maxExpense: Float = 0f,
)

@Entity(tableName = "budget_period")
data class BudgetPeriod(
    @PrimaryKey
    val id: UUID = UUID(0L,0L),
    @ColumnInfo(name = "wallet_id")
    val walletId: UUID = UUID(0L,0L),
    @ColumnInfo(name = "start_date")
    val startDate: Date = getFirstDayOfCurrentMonth(),
    @ColumnInfo(name = "end_date")
    val endDate: Date = getLastDayOfCurrentMonth(),
)