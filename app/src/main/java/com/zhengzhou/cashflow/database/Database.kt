package com.zhengzhou.cashflow.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.zhengzhou.cashflow.data.*

@Database(
    entities = [
        Wallet::class,
        Transaction::class,
        Category::class,
        TagEntry::class,
        TagTransaction::class,
        TagLocation::class,
        BudgetCategory::class,
        BudgetPeriod::class,
    ],
    version = 1,
    exportSchema = false,
)
@TypeConverters(TransactionTypeConverters::class)
abstract class RegisterDatabase : RoomDatabase() {
    abstract fun databaseDao(): DatabaseDao
}

/*
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE wallet " +
                    "ADD COLUMN budget_enabled INTEGER NOT NULL DEFAULT 0"
        )
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS budget " +
                    "(id BLOB NOT NULL, " +
                    "start_date INTEGER NOT NULL, " +
                    "end_date INTEGER NOT NULL, " +
                    "category_id BLOB NOT NULL, " +
                    "max_expense REAL NOT NULL, " +
                    "PRIMARY KEY(id))"
        )
    }
}
 */