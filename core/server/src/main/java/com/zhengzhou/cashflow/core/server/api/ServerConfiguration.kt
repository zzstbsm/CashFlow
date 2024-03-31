package com.zhengzhou.cashflow.core.server.api

data class ServerConfiguration(
    val type: String,
    val address: String,
    val port: Int,
)