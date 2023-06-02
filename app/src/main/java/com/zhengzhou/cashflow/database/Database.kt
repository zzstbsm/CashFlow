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
        Tag::class,
        TagTransaction::class,
        TagLocation::class
    ],
    version = 1,
    exportSchema = false,
)
@TypeConverters(TransactionTypeConverters::class)
abstract class RegisterDatabase : RoomDatabase() {
    abstract fun databaseDao(): DatabaseDao
}

/*
val migration_1_2 = object : Migration(1,2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            // SQL COMMAND
        )
    }
}
*/