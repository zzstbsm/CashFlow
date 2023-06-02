package com.zhengzhou.cashflow.database

import androidx.room.TypeConverter
import java.util.*

class TransactionTypeConverters {

    @TypeConverter
    fun fromDate(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun toDate(millisFromEpoch: Long): Date {
        return Date(millisFromEpoch)
    }
}