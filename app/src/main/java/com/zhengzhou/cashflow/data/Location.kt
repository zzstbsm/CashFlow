package com.zhengzhou.cashflow.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

// TODO: separate the location as done for the tag

@Entity(tableName = "location")
data class Location (
    @PrimaryKey val id: UUID = UUID(0L,0L),
    val address: String = "",
    @ColumnInfo(name = "coordinate_x")
    val coordinateX: Double = 0.0,
    @ColumnInfo(name = "coordinate_y")
    val coordinateY: Double = 0.0,
) {
    fun equal(location: Location): Boolean {
        return address == location.address &&
                    coordinateX == location.coordinateX &&
                    coordinateY == location.coordinateY
    }
}