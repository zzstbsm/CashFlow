package com.zhengzhou.cashflow.database.api

import android.content.Context
import com.zhengzhou.cashflow.database.data.data_source.DatabaseExposer
import com.zhengzhou.cashflow.database.data.data_source.RegisterDatabase

class DatabaseInstance private constructor() {
    companion object {

        private var INSTANCE: DatabaseInstance? = null
        private var database: RegisterDatabase? = null

        fun initialize(context: Context) {
            database = DatabaseExposer.buildDatabase(context = context)
            if (INSTANCE == null) {
                INSTANCE = DatabaseInstance()
            }
        }

        fun get(): DatabaseInstance {
            return INSTANCE ?: throw java.lang.IllegalStateException("DatabaseInstance must be initialized")
        }
        internal fun getDatabase(): RegisterDatabase {
            return database ?: throw java.lang.IllegalStateException("DatabaseInstance must be initialized")
        }
    }
}