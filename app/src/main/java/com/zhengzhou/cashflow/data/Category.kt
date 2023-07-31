package com.zhengzhou.cashflow.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zhengzhou.cashflow.tools.IconsMappedForDB
import java.util.*

@Entity(tableName = "category")
data class Category (
    @PrimaryKey val id: UUID = UUID(0L,0L),
    var name: String = "",
    @ColumnInfo(name = "id_icon")
    val iconName: IconsMappedForDB = IconsMappedForDB.LOADING,
    @ColumnInfo(name = "movement_type_id")
    val transactionTypeId: Int = 0,
)