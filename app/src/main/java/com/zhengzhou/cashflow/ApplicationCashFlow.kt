package com.zhengzhou.cashflow

import android.app.Application
import com.zhengzhou.cashflow.database.DatabaseRepository
import com.zhengzhou.cashflow.database.databaseRepositoryInitializer

class ApplicationCashFlow: Application() {
    override fun onCreate() {
        super.onCreate()
        val database = databaseRepositoryInitializer(this)
        DatabaseRepository.initialize(database)
    }
}