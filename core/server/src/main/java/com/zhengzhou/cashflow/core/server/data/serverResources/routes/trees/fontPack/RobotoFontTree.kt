package com.zhengzhou.cashflow.core.server.data.serverResources.routes.trees.fontPack

enum class RobotoFontTree(
    val route: String,
    val resourcePath: String,
) {
    Roboto100(
        route = "/Roboto/roboto-v30-latin-100.woff2",
        resourcePath = "font/Roboto/roboto-v30-latin-100.woff2",
    ),
    Roboto100Italic(
        route = "/Roboto/roboto-v30-latin-100italic.woff2",
        resourcePath = "font/Roboto/roboto-v30-latin-100italic.woff2",
    ),
    Roboto300(
        route = "/Roboto/roboto-v30-latin-300.woff2",
        resourcePath = "font/Roboto/roboto-v30-latin-300.woff2",
    ),
    Roboto300Italic(
        route = "/Roboto/roboto-v30-latin-300italic.woff2",
        resourcePath = "font/Roboto/roboto-v30-latin-300italic.woff2",
    ),
    RobotoRegular(
        route = "/Roboto/roboto-v30-latin-regular.woff2",
        resourcePath = "font/Roboto/roboto-v30-latin-regular.woff2",
    ),
    RobotoItalic(
        route = "/Roboto/roboto-v30-latin-italic.woff2",
        resourcePath = "font/Roboto/roboto-v30-latin-italic.woff2",
    ),
    Roboto500(
        route = "/Roboto/roboto-v30-latin-500.woff2",
        resourcePath = "font/Roboto/roboto-v30-latin-500.woff2",
    ),
    Roboto500Italic(
        route = "/Roboto/roboto-v30-latin-500italic.woff2",
        resourcePath = "font/Roboto/roboto-v30-latin-500italic.woff2",
    ),
    Roboto700(
        route = "/Roboto/roboto-v30-latin-700.woff2",
        resourcePath = "font/Roboto/roboto-v30-latin-700.woff2",
    ),
    Roboto700Italic(
        route = "/Roboto/roboto-v30-latin-700italic.woff2",
        resourcePath = "font/Roboto/roboto-v30-latin-700italic.woff2",
    ),
    Roboto900(
        route = "/Roboto/roboto-v30-latin-900.woff2",
        resourcePath = "font/Roboto/roboto-v30-latin-900.woff2",
    ),
    Roboto900Italic(
        route = "/Roboto/roboto-v30-latin-900italic.woff2",
        resourcePath = "font/Roboto/roboto-v30-latin-900italic.woff2",
    );
    companion object {
        fun getAllRoutes(): List<RobotoFontTree> {
            return enumValues<RobotoFontTree>().toList()
        }
    }
}