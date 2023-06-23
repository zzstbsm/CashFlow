package com.zhengzhou.cashflow.database

import androidx.room.TypeConverter
import com.zhengzhou.cashflow.data.Currency
import java.util.Date

class TransactionTypeConverters {

    @TypeConverter
    fun fromDate(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun toDate(millisFromEpoch: Long): Date {
        return Date(millisFromEpoch)
    }

    @TypeConverter
    fun fromCurrency(currency: Currency): String {
        return currency.abbreviation
    }

    @TypeConverter
    fun toCurrency(abbreviation: String): Currency? {
        return Currency.setCurrency(abbreviation = abbreviation)
    }

}