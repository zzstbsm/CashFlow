package com.zhengzhou.cashflow.database

import android.content.Context
import androidx.room.Room

internal fun databaseRepositoryInitializer(
    context: Context,
): RegisterDatabase {
    return Room.databaseBuilder(
        context.applicationContext,
        RegisterDatabase::class.java,
        RegisterDatabase.DATABASE_NAME,
    ).build()
}