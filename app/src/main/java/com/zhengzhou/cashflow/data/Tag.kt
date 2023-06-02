package com.zhengzhou.cashflow.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "tag")
data class Tag (
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val name: String = "",
    val count: Int = 0,
)