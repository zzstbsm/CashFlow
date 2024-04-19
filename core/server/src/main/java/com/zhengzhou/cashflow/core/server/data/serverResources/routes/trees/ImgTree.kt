package com.zhengzhou.cashflow.core.server.data.serverResources.routes.trees

enum class ImgTree(
    val route: RouteImg,
    val resourcePath: String,
) {
    Logo(
        route = "/img/logo.webp",
        resourcePath = "file:///android_asset/images/logo.webp",
    );

    companion object {
        fun getAllRoutes(): List<ImgTree> {
            return enumValues<ImgTree>().toList()
        }
    }
}

typealias RouteImg = String