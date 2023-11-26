package com.zhengzhou.cashflow.database

import android.content.Context

internal class DatabaseInstance private constructor() {
    companion object {

        private var INSTANCE: RegisterDatabase? = null

        fun initialize(context: Context) {
            val database = databaseRepositoryInitializer(context = context)
            if (INSTANCE == null) {
                INSTANCE = database
            }
        }

        fun get(): RegisterDatabase {
            return INSTANCE ?: throw java.lang.IllegalStateException("DatabaseInstance must be initialized")
        }

    }

}