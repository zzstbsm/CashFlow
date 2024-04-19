package com.zhengzhou.cashflow.core.server.data.serverResources.routes

import com.zhengzhou.cashflow.core.server.data.serverResources.routes.pages.about
import com.zhengzhou.cashflow.core.server.data.serverResources.routes.pages.commonTransactions
import com.zhengzhou.cashflow.core.server.data.serverResources.routes.pages.home
import com.zhengzhou.cashflow.core.server.data.serverResources.routes.pages.manageCategories
import com.zhengzhou.cashflow.core.server.data.serverResources.routes.pages.wallet
import com.zhengzhou.cashflow.core.server.data.serverResources.routes.trees.CSSTree
import com.zhengzhou.cashflow.core.server.data.serverResources.routes.trees.FontTree
import com.zhengzhou.cashflow.core.server.data.serverResources.routes.trees.ImgTree
import com.zhengzhou.cashflow.core.server.data.serverResources.routes.trees.fontPack.RobotoFontTree
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respondFile
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import java.io.File

fun Application.configureRouting() {

    routing {

        cssRoutes()
        fontRoutes()
        imgRoutes()

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

fun Route.cssRoutes() {
    for (cssResource in CSSTree.getAllRoutes()) {
        get(cssResource.route) {
            call.respondFile(file = File(cssResource.resourcePath))
        }
    }
}

fun Route.fontRoutes() {
    for (fontResource in FontTree.getAllRoutes()) {
        get(fontResource.route) {
            call.respondFile(file = File(fontResource.resourcePath))
        }
    }

    for (robotoFont in RobotoFontTree.getAllRoutes()) {
        get(robotoFont.route) {
            call.respondFile(file = File(robotoFont.resourcePath))
        }
    }
}

fun Route.imgRoutes() {
    for (imgResource in ImgTree.getAllRoutes()) {
        get(imgResource.route) {
            call.respondFile(file = File(imgResource.resourcePath))
        }
    }
}