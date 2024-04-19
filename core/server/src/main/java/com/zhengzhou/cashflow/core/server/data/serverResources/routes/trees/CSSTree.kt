package com.zhengzhou.cashflow.core.server.data.serverResources.routes.trees

enum class CSSTree(
    val route: RouteCSS,
    val resourcePath: String,
) {
    Base(
        route = "/css/base.css",
        resourcePath = "core/server/src/main/resources/css/base.css"
    ),
    Navigation(
        route = "/css/navigation.css",
        resourcePath = "core/server/src/main/resources/css/navigation.css"
    ),
    MaterialTheme(
        route = "/css/themes.css",
        resourcePath = "core/server/src/main/resources/css/themes.css",
    );

    companion object {
        fun getAllRoutes(): List<CSSTree> {
            return enumValues<CSSTree>().toList()
        }
    }
}

typealias RouteCSS = String