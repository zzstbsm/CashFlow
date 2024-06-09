package com.zhengzhou.cashflow.core.server.data.serverResources.routes

import com.zhengzhou.cashflow.core.server.data.serverResources.ServerAssets
import com.zhengzhou.cashflow.core.server.data.serverResources.plugins.retrieveAsset
import com.zhengzhou.cashflow.core.server.data.serverResources.routes.pages.about
import com.zhengzhou.cashflow.core.server.data.serverResources.routes.pages.commonTransactions
import com.zhengzhou.cashflow.core.server.data.serverResources.routes.pages.home
import com.zhengzhou.cashflow.core.server.data.serverResources.routes.pages.manageCategories
import com.zhengzhou.cashflow.core.server.data.serverResources.routes.pages.wallet
import com.zhengzhou.cashflow.core.server.data.serverResources.routes.trees.CSSTree
import com.zhengzhou.cashflow.core.server.data.serverResources.routes.trees.FontTree
import com.zhengzhou.cashflow.core.server.data.serverResources.routes.trees.ImgTree
import com.zhengzhou.cashflow.core.server.data.serverResources.routes.trees.fontPack.RobotoFontTree
import io.ktor.http.ContentType
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respondBytes
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun Application.configureRouting() {

    routing {

        assetRoutes()

        pageRoutes()

    }
}

fun Route.pageRoutes() {
    home()
    wallet()
    commonTransactions()
    manageCategories()
    about()
}

fun Route.assetRoutes() {

    val assetManager = ServerAssets.getAssetManager()
    if (assetManager != null) {
        for (cssResource in CSSTree.getAllRoutes()) {
            get(cssResource.route) {
                call.respondBytes(
                    bytes = retrieveAsset(
                        assetManager = assetManager,
                        position = cssResource.resourcePath
                    ),
                    contentType = ContentType.Text.CSS
                )
            }
        }
        for (imgResource in ImgTree.getAllRoutes()) {
            get(imgResource.route) {
                call.respondBytes(
                    bytes = retrieveAsset(
                        assetManager = assetManager,
                        position = imgResource.resourcePath
                    ),
                    contentType = ContentType.Image.Any
                )
            }
        }
        for (fontResource in FontTree.getAllRoutes()) {
            get(fontResource.route) {
                call.respondBytes(
                    bytes = retrieveAsset(
                        assetManager = assetManager,
                        position = fontResource.resourcePath
                    ),
                    contentType = ContentType.Text.CSS,
                )
            }
        }
        for (fontResource in RobotoFontTree.getAllRoutes()) {
            get(fontResource.route) {
                call.respondBytes(
                    bytes = retrieveAsset(
                        assetManager = assetManager,
                        position = fontResource.resourcePath,
                    ),
                    contentType = ContentType.Font.Any,
                )
            }
        }
    }
}