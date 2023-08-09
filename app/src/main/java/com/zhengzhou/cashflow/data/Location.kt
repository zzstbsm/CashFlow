package com.zhengzhou.cashflow.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

// TODO: separate the location as done for the tag

@Entity(tableName = "location")
data class Location (
    @PrimaryKey val id: UUID,
    val address: String,
    @ColumnInfo(name = "coordinate_x")
    val coordinateX: Double,
    @ColumnInfo(name = "coordinate_y")
    val coordinateY: Double,
) {
    companion object {
        fun newEmpty(): Location {
            return Location(
                id = UUID(0L,0L),
                address = "",
                coordinateX = 0.0,
                coordinateY = 0.0,
            )
        }
    }
    fun equal(location: Location): Boolean {
        return address == location.address &&
                    coordinateX == location.coordinateX &&
                    coordinateY == location.coordinateY
    }
}