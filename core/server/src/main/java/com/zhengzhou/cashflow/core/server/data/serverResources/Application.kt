package com.zhengzhou.cashflow.core.server.data.serverResources

import com.zhengzhou.cashflow.core.server.data.serverResources.routes.configureRouting
import io.ktor.server.application.Application

fun Application.module() {
    configureRouting()
}
