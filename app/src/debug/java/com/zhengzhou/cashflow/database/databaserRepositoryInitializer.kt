package com.zhengzhou.cashflow.database

import android.content.Context
import androidx.room.Room

fun databaseRepositoryInitializer(
    context: Context,
): RegisterDatabase {
    return Room.inMemoryDatabaseBuilder(
        context.applicationContext,
        RegisterDatabase::class.java,
    ).build()
}