/*
Assignment 5: Flight Search App (Data Persistence)
Ryan Speese / speeser@oregonstate.edu
OSU / CS 492
 */

package com.osu.flightsearch.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AirportDao {
    @Query("SELECT * from airport WHERE iata_code LIKE '%' || :searchString || '%' OR name LIKE '%' || :searchString || '%' ORDER BY passengers DESC")
    fun getMatchingAirports(searchString: String): List<Airport>

    @Query("SELECT * from airport WHERE NOT iata_code = :codeSelected")
    fun getArrivalAirports(codeSelected: String): List<Airport>
}