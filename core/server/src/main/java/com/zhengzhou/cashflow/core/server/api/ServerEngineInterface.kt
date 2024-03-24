package com.zhengzhou.cashflow.core.server.api

interface ServerEngineInterface {

    /**
     * Start the server engine
     * @param port port to use for the server
     */
    fun start(port: Int = 8080)
    /**
     * Stop the server engine
     */
    fun stop()

    /**
     * @return returns the local ip of the device
     */
    fun getLocalIP(): String?

}