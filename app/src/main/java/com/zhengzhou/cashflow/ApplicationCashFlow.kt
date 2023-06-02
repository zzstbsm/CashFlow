package com.zhengzhou.cashflow

import android.app.Application
import com.zhengzhou.cashflow.database.DatabaseRepository

class ApplicationCashFlow: Application() {
    override fun onCreate() {
        super.onCreate()
        DatabaseRepository.initialize(this)
    }
}