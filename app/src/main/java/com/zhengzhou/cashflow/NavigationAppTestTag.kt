package com.zhengzhou.cashflow

class NavigationAppTestTag {
    companion object {

        const val TAG_OPEN_NAV_DRAWER = "Open NavDrawer"

        fun bottomNavBar(
            route: String
        ): String {
            return "BottomNavBar-$route"
        }

        fun drawerNavBar(
            route: String
        ): String {
            return "NavDrawer-$route"
        }

    }
}