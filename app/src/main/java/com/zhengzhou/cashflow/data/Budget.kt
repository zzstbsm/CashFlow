package com.zhengzhou.cashflow.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "budget")
data class Budget(
    @PrimaryKey
    val id: UUID = UUID(0L,0L),
    @ColumnInfo(name = "start_date")
    val startDate: Date = Date(),
    @ColumnInfo(name = "end_date")
    val endDate: Date = Date(),
    @ColumnInfo(name = "category_id")
    val categoryId: UUID = UUID(0L,0L),
    @ColumnInfo(name = "max_expense")
    val maxExpense: Float = 0f,
)