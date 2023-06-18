package com.zhengzhou.cashflow.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "tag_transaction")
data class TagTransaction (
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    @ColumnInfo(name = "id_movement")
    val idTransaction: UUID = UUID(0L,0L),
    @ColumnInfo(name = "id_tag")
    val idTag: UUID = UUID(0L,0L),
)