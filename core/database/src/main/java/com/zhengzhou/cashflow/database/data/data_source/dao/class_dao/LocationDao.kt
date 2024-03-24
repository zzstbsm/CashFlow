package com.zhengzhou.cashflow.database.data.data_source.dao.class_dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.zhengzhou.cashflow.data.Location
import java.util.UUID

interface LocationDao {

    // Default implementations
    @Query("SELECT * FROM location WHERE id=(:locationUUID)")
    suspend fun getLocation(locationUUID: UUID): Location?
    @Insert(entity = Location::class, onConflict = OnConflictStrategy.ABORT)
    suspend fun addLocation(location: Location)
    @Update(entity = Location::class)
    suspend fun updateLocation(location: Location)
    @Delete(entity = Location::class)
    suspend fun deleteLocation(location: Location)

    // Custom query based on the application
}