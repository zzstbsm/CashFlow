package com.zhengzhou.cashflow.core.server.data.serverResources.routes.pages

import com.zhengzhou.cashflow.core.server.data.serverResources.routes.trees.CSSTree
import com.zhengzhou.cashflow.core.server.data.serverResources.routes.trees.PageTree
import com.zhengzhou.cashflow.core.server.data.serverResources.templates.pageTemplate
import com.zhouzheng.routes.navigation.Navigation
import io.ktor.server.application.call
import io.ktor.server.html.respondHtml
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun Route.about() {
    get(PageTree.About.route) {
        call.respondHtml {
            pageTemplate(
                pageTitle = PageTree.About.pageTitle,
                tabList = Navigation.getAllTabs(),
                currentTab = Navigation.About,
                cssList = listOf(
                    CSSTree.Base.route,
                    CSSTree.Navigation.route,
                    CSSTree.MaterialTheme.route,
                ),
                content = {

                }
            )
        }
    }
}