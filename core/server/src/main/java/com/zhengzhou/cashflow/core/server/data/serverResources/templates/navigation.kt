package com.zhengzhou.cashflow.core.server.data.serverResources.templates

import com.zhengzhou.cashflow.core.server.data.serverResources.routes.trees.ImgTree
import com.zhouzheng.routes.navigation.Navigation
import kotlinx.html.HtmlBlockTag
import kotlinx.html.a
import kotlinx.html.div
import kotlinx.html.img
import kotlinx.html.li
import kotlinx.html.nav
import kotlinx.html.p
import kotlinx.html.ul


fun HtmlBlockTag.navigation(
    tabList: List<Navigation>,
    currentTab: Navigation,
) {
    nav {
        div(classes = "logo") {
            a(href = "#") {
                img(
                    src = ImgTree.Logo.route,
                    alt = "Logo of Cashflow"
                )
                p {
                    +"CashFlow"
                }
            }
        }
        ul(classes = "features") {
            for (tab in tabList) {
                li(
                    classes = if (tab == currentTab) "active" else ""
                ) {
                    a(href = tab.pageRoute) {
                        +tab.pageName
                    }
                }
            }
        }
    }
}