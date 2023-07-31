package com.zhengzhou.cashflow.tools

import com.zhengzhou.cashflow.R

enum class IconsMappedForDB(
    val resource: Int,
    val category: Boolean,
    val wallet: Boolean,
) {
    ANALYTICS(
        resource = R.drawable.ic_analyics,
        category = true,
        wallet = false,
    ),
    CARD(
        resource = R.drawable.ic_card,
        category = false,
        wallet = true,
    ),
    CLEAR(
        resource = R.drawable.ic_clear,
        category = false,
        wallet = false,
    ),
    EATING_OUT(
        resource = R.drawable.ic_eating_out,
        category = true,
        wallet = false,
    ),
    ENTERTAINMENT(
        resource = R.drawable.ic_entertainment,
        category = true,
        wallet = false,
    ),
    GIFT(
        resource = R.drawable.ic_gift,
        category = true,
        wallet = false,
    ),
    GROCERY(
        resource = R.drawable.ic_grocery,
        category = true,
        wallet = false,
    ),
    HEALTH(
        resource = R.drawable.ic_health,
        category = true,
        wallet = false,
    ),
    HOME(
        resource = R.drawable.ic_home,
        category = true,
        wallet = false,
    ),
    LOADING(
        resource = R.drawable.ic_cloud,
        category = false,
        wallet = false,
    ),
    PHONE(
        resource = R.drawable.ic_phone,
        category = true,
        wallet = false,
    ),
    SALARY(
        resource = R.drawable.ic_arrow_double_up,
        category = true,
        wallet = false,
    ),
    SPORT(
        resource = R.drawable.ic_sport,
        category = true,
        wallet = false,
    ),
    SUBSCRIPTION(
        resource = R.drawable.ic_subscriptions,
        category = true,
        wallet = false,
    ),
    TRANSFER(
        resource = R.drawable.ic_grocery,
        category = true,
        wallet = false,
    ),
    TRANSPORTATION(
        resource = R.drawable.ic_transportation,
        category = true,
        wallet = false,
    ),
    TRASH(
        resource = R.drawable.ic_trash,
        category = true,
        wallet = false,
    ),
    TRAVEL(
        resource = R.drawable.ic_travel,
        category = true,
        wallet = false,
    ),
    TRENDING_UP(
        resource = R.drawable.ic_trending_up,
        category = true,
        wallet = false,
    ),
    WALLET(
        resource = R.drawable.ic_wallet,
        category = false,
        wallet = true,
    );

    companion object {
        fun setIcon(name: String): IconsMappedForDB? {
            val iconsList = IconsMappedForDB.values().toList()

            iconsList.forEach { icon ->
                if (icon.name == name) return icon
            }
            return null
        }
    }
}