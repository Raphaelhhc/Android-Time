package com.example.timeapp.data.world_clock

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WorldClockDao {

    @Query("SELECT * FROM world_clock_cities")
    fun getAllCitiesFlow(): Flow<List<WorldClockCity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(city: WorldClockCity)

    @Query("DELETE FROM world_clock_cities WHERE zoneId = :zoneId")
    suspend fun delete(zoneId: String)

}