package com.zhengzhou.cashflow.core.server.data

import android.util.Log
import com.zhengzhou.cashflow.core.server.api.ServerEngineInterface
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine


const val TAG = "ServerEngineImplementation"

internal class ServerEngineImplementation: ServerEngineInterface {

    private lateinit var server: NettyApplicationEngine

    override fun start(port: Int) {
        server = embeddedServer(
            Netty,
            port = port,
            module = Application::module
        )
        server.start()
        server.environment.connectors.forEach { engineConnectorConfig ->
            Log.d(TAG,"${engineConnectorConfig.host}:${engineConnectorConfig.port}\n${engineConnectorConfig.type}")
        }
    }
    override fun stop() {
        server.stop()
    }

    override fun getLocalIP(): String? {
        // TODO("Not yet implemented")
        return null
    }
}