package com.zhengzhou.cashflow.data

import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zhengzhou.cashflow.R
import java.util.*

@Entity(tableName = "category")
data class Category (
    @PrimaryKey val id: UUID = UUID(0L,0L),
    var name: String = "",
    @ColumnInfo(name = "id_icon")
    @DrawableRes val idIcon: Int = R.drawable.ic_error,
    @ColumnInfo(name = "movement_type_id")
    val transactionTypeId: Int = 0,
)

val listCategoriesIconsId: List<Int> = listOf(
    R.drawable.ic_grocery,
    R.drawable.ic_health,
    R.drawable.ic_sport,
    R.drawable.ic_card,
    R.drawable.ic_analyics,
    R.drawable.ic_home,
    R.drawable.ic_transfer,
    R.drawable.ic_transportation,
    R.drawable.ic_travel,
    R.drawable.ic_wallet,
    R.drawable.ic_arrow_double_up,
    R.drawable.ic_trending_up,
    R.drawable.ic_subscriptions,
    R.drawable.ic_eating_out,
    R.drawable.ic_phone,
    R.drawable.ic_entertainment,
    R.drawable.ic_gift,
    R.drawable.ic_phone,
    R.drawable.ic_trash,
)