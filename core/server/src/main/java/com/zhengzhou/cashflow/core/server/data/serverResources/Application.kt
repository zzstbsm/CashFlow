package com.zhengzhou.cashflow.core.server.data.serverResources

import android.content.res.AssetManager
import com.zhengzhou.cashflow.core.server.data.serverResources.routes.configureRouting
import io.ktor.server.application.Application

internal class ServerAssets(){
    companion object {

        private var _assetManager: AssetManager? = null

        fun setAssetManager(assetManager: AssetManager) {
            _assetManager = assetManager
        }

        fun getAssetManager(): AssetManager? {
            return _assetManager
        }
    }
}

fun Application.module() {
    configureRouting()
}
