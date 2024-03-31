package com.zhengzhou.cashflow.core.server.api

import android.net.ConnectivityManager

interface ServerEngineInterface {

    /**
     * Start the server engine
     * @param port port to use for the server
     */
    fun start(port: Int)

    /**
     * Stop the server engine
     */
    fun stop()

    /**
     * @return host full configuration
     */
    fun getHostConfiguration(connectivityManager: ConnectivityManager): ServerConfiguration? {

        val type = getHostType()
        val address = getHostAddress(
            connectivityManager
        )
        val port = getHostPort()

        return if (type != null && address != null && port != null) {
            ServerConfiguration(
                type = type,
                address = address,
                port = port,
            )
        } else {
            null
        }
    }

    /**
     * @return the local ip of the device
     */
    fun getHostAddress(connectivityManager: ConnectivityManager): String?

    /**
     * @return the active port
     */
    fun getHostPort(): Int?

    /**
     * @return the host type: HTTP or HTTPS
     */
    fun getHostType(): String?
}