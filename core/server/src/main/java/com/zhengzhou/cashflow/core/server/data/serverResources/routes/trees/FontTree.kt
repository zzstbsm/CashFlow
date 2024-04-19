package com.zhengzhou.cashflow.core.server.data.serverResources.routes.trees

enum class FontTree(
    val route: RouteFont,
    val resourcePath: String,
) {
    Roboto(
        route = "/font/Roboto.css",
        resourcePath = "core/server/core/server/src/main/resources/font/Roboto.css",
    );
    companion object {
        fun getAllRoutes(): List<FontTree> {
            return enumValues<FontTree>().toList()
        }
    }
}

typealias RouteFont = String