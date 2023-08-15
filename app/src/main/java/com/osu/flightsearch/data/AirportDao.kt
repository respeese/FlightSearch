package com.osu.flightsearch.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AirportDao {
    @Query("SELECT * from airports WHERE iataCode LIKE '%'+:codeEntered+'%' OR name LIKE '%'+:nameEntered+'%' ORDER BY passengers DESC")
    fun getMatchingAirports(codeEntered: String, nameEntered: String): Flow<List<Airport>>

    @Query("SELECT * from airports WHERE NOT iataCode LIKE :codeSelected")
    fun getArrivalAirports(codeSelected: String): Flow<List<Airport>>
}