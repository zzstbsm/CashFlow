package com.zhengzhou.cashflow.database

import android.content.Context
import androidx.room.Room

private const val DATABASE_NAME = "Registry_DB"

internal fun databaseRepositoryInitializer(
    context: Context,
): RegisterDatabase {
    return Room.databaseBuilder(
        context.applicationContext,
        RegisterDatabase::class.java,
        DATABASE_NAME
    ).build()
}