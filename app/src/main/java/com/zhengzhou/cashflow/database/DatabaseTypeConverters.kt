package com.zhengzhou.cashflow.database

import android.util.Log
import androidx.room.TypeConverter
import com.zhengzhou.cashflow.data.Currency
import com.zhengzhou.cashflow.data.TransactionType
import com.zhengzhou.cashflow.tools.IconsMappedForDB
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

    @TypeConverter
    fun fromTransactionType(transactionType: TransactionType): Int {
        return transactionType.id
    }

    @TypeConverter
    fun toTransactionType(id: Int): TransactionType? {
        return TransactionType.setTransaction(id)
    }

    @TypeConverter
    fun fromIconsMappedForDB(icon: IconsMappedForDB): String {
        return "${icon.name}-DEFAULT"
    }

    @TypeConverter
    fun toIconsMappedForDB(name: String): IconsMappedForDB? {
        val splitName = name.split("-")[0]
        Log.d("DatabaseTypeConverter","toIconsMappedForDB: from $name to $splitName")
        return IconsMappedForDB.setIcon(splitName)
    }

}