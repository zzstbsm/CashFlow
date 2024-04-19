package com.zhengzhou.cashflow.core.server.data

import android.net.ConnectivityManager
import android.net.LinkProperties
import com.zhengzhou.cashflow.core.server.api.ServerEngineInterface
import com.zhengzhou.cashflow.core.server.data.serverResources.module
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine

internal class ServerEngineImplementation: ServerEngineInterface {

    private lateinit var _server: NettyApplicationEngine
    private var _serverInitialized: Boolean = false

    override fun start(
        port: Int
    ) {

        _server = embeddedServer(
            Netty,
            port = port,
            module = Application::module
        )
        _server.start()
        _serverInitialized = true
    }

    override fun stop() {
        _server.stop()
        _serverInitialized = false
    }

    override fun getHostAddress(connectivityManager: ConnectivityManager): String? {

        if (!_serverInitialized) return null

        val link: LinkProperties =  connectivityManager.getLinkProperties(connectivityManager.activeNetwork) as LinkProperties

        val ipV4Address = link.linkAddresses.firstOrNull { linkAddress ->
            linkAddress.address.hostAddress?.contains(".") ?: false
        }?.address?.hostAddress

        return ipV4Address

    }

    override fun getHostPort(): Int? {

        if (!_serverInitialized) return null

        return _server.environment.connectors.firstOrNull()?.port
    }

    override fun getHostType(): String? {

        if (!_serverInitialized) return null

        return _server.environment.connectors.firstOrNull()?.type?.name
    }
}