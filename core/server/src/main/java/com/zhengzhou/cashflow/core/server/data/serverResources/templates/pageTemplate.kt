package com.zhengzhou.cashflow.core.server.data.serverResources.templates

import com.zhengzhou.cashflow.core.server.data.serverResources.routes.trees.RouteCSS
import com.zhouzheng.routes.navigation.Navigation
import io.ktor.http.LinkHeader
import kotlinx.html.DIV
import kotlinx.html.HTML
import kotlinx.html.body
import kotlinx.html.head
import kotlinx.html.link
import kotlinx.html.title

fun HTML.pageTemplate(
    pageTitle: String,
    tabList: List<Navigation> = Navigation.getAllTabs(),
    currentTab: Navigation,
    cssList: List<RouteCSS>,
    content: DIV.() -> Unit,
) {
    head {
        title {
            +pageTitle
        }
        for (cssPage in cssList) {
            link(rel = LinkHeader.Rel.Stylesheet, href = cssPage)
        }
    }
    body {
        wrapper {
            navigation(
                tabList = tabList,
                currentTab = currentTab,
            )
            content()
        }
    }
}