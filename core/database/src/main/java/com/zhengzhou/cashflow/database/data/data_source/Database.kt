package com.zhengzhou.cashflow.database.data.data_source

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.zhengzhou.cashflow.data.*
import com.zhengzhou.cashflow.database.data.data_source.dao.RepositoryDao

@Database(
    entities = [
        Wallet::class,
        Transaction::class,
        Category::class,
        TagEntry::class,
        TagTransaction::class,
        Location::class,
        BudgetPerCategory::class,
        BudgetPerPeriod::class,
    ],
    exportSchema = true,
    version = 2,
    autoMigrations = [
        AutoMigration(
            from = 1,
            to = 2,
        )
    ]
)
@TypeConverters(TransactionTypeConverters::class)
internal abstract class RegisterDatabase : RoomDatabase() {
    abstract fun databaseDao(): RepositoryDao

    companion object {
        const val DATABASE_NAME = "Registry_DB"

    }
}