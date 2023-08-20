package com.zhengzhou.cashflow

import android.app.Service
import android.content.Intent
import android.os.IBinder


class ApplicationConfigurationService: Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(p0: Intent?): IBinder? {
        // TODO("Not yet implemented")
        return null
    }

    suspend fun load() {
        // TODO: add load configuration
    }

    suspend fun save() {
        // TODO: add save configuration
    }

}