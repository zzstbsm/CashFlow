package com.zhengzhou.cashflow.core.server.data.serverResources.routes.trees

enum class PageTree(
    val route: RoutePage,
    val pageTitle: String,
) {
    Home(
        route = "/",
        pageTitle = "Home"
    ),
    Wallet(
        route = "/wallets",
        pageTitle = "Wallet"
    ),
    CommonTransactions(
        route = "/common_transactions",
        pageTitle = "Common Transactions"
    ),
    ManageCategories(
        route = "/manage_categories",
        pageTitle = "Manage Categories"
    ),
    About(
        route = "/about",
        pageTitle = "About"
    )
}

typealias RoutePage = String