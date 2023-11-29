package com.zhengzhou.cashflow.database.data.data_source

import androidx.room.TypeConverter
import com.zhengzhou.cashflow.data.Currency
import com.zhengzhou.cashflow.data.TransactionType
import com.zhengzhou.cashflow.themes.IconsMappedForDB
import java.util.Date

internal class TransactionTypeConverters {

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
        return currency.name
    }

    @TypeConverter
    fun toCurrency(abbreviation: String): Currency? {
        return Currency.setCurrency(name = abbreviation)
    }

    @TypeConverter
    fun fromTransactionType(transactionType: TransactionType): Int {
        return transactionType.id
    }

    @TypeConverter
    fun toTransactionType(id: Int): TransactionType? {
        return TransactionType.setTransaction(id)
    }

    @TypeConverter
    fun fromIconsMappedForDB(icon: com.zhengzhou.cashflow.themes.IconsMappedForDB): String {
        return "${icon.name}-DEFAULT"
    }

    @TypeConverter
    fun toIconsMappedForDB(name: String): com.zhengzhou.cashflow.themes.IconsMappedForDB? {
        val splitName = name.split("-")[0]
        return com.zhengzhou.cashflow.themes.IconsMappedForDB.setIcon(splitName)
    }

}