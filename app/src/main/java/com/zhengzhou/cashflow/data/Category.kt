package com.zhengzhou.cashflow.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "category")
data class Category (
    @PrimaryKey val id: UUID = UUID(0L,0L),
    @StringRes var name: Int = 0,
    @ColumnInfo(name = "id_icon")
    @DrawableRes val idIcon: Int = 0,
    @ColumnInfo(name = "movement_type_id")
    val transactionTypeId: Int = 0,
)