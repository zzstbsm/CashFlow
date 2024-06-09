package com.zhengzhou.cashflow.core.server.data

import android.content.res.AssetManager
import android.net.ConnectivityManager
import android.net.LinkProperties
import com.zhengzhou.cashflow.core.server.api.ServerEngineInterface
import com.zhengzhou.cashflow.core.server.data.serverResources.ServerAssets
import com.zhengzhou.cashflow.core.server.data.serverResources.module
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine

internal class ServerEngineImplementation: ServerEngineInterface {

    private lateinit var _server: NettyApplicationEngine
    private var _serverStarted: Boolean = false

    override fun start(
        port: Int,
        assetManager: AssetManager,
    ) {

        ServerAssets.setAssetManager(assetManager)
        _server = embeddedServer(
            Netty,
            port = port,
            module = Application::module
        )
        _server.start()
        _serverStarted = true
    }

    override fun stop() {
        _server.stop()
        _serverStarted = false
    }

    override fun getHostAddress(connectivityManager: ConnectivityManager): String? {

        if (!_serverStarted) return null

        val link: LinkProperties =  connectivityManager.getLinkProperties(connectivityManager.activeNetwork) as LinkProperties

        val ipV4Address = link.linkAddresses.firstOrNull { linkAddress ->
            linkAddress.address.hostAddress?.contains(".") ?: false
        }?.address?.hostAddress

        return ipV4Address

    }

    override fun getHostPort(): Int? {

        if (!_serverStarted) return null

        return _server.environment.connectors.firstOrNull()?.port
    }

    override fun getHostType(): String? {

        if (!_serverStarted) return null

        return _server.environment.connectors.firstOrNull()?.type?.name
    }
}