package com.zhengzhou.cashflow

import android.app.Application
import com.zhengzhou.cashflow.database.api.DatabaseInstance

class ApplicationCashFlow: Application() {
    override fun onCreate() {
        super.onCreate()
        DatabaseInstance.initialize(this)
    }
}