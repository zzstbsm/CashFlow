package com.zhouzheng.routes.navigation

import com.zhengzhou.cashflow.core.server.data.serverResources.routes.trees.PageTree
import com.zhengzhou.cashflow.core.server.data.serverResources.routes.trees.RoutePage

enum class Navigation(
    val pageName: String,
    val pageRoute: RoutePage,
) {
    Home(
        pageName = PageTree.Home.pageTitle,
        pageRoute = PageTree.Home.route,
    ),
    Wallets(
        pageName = PageTree.Wallet.pageTitle,
        pageRoute = PageTree.Wallet.route,
    ),
    CommonTransactions(
        pageName = PageTree.CommonTransactions.pageTitle,
        pageRoute = PageTree.CommonTransactions.route,
    ),
    ManageCategories(
        pageName = PageTree.ManageCategories.pageTitle,
        pageRoute = PageTree.ManageCategories.route,
    ),
    About(
        pageName = PageTree.About.pageTitle,
        pageRoute = PageTree.About.route,
    );

    companion object {
        fun getAllTabs(): List<Navigation> {
            return enumValues<Navigation>().toList()
        }
    }
}