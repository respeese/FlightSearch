package com.osu.flightsearch.data

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favorite: Favorite)

    @Query("SELECT * from favorites")
    fun getAllFavoriteFlights(): Flow<List<Favorite>>
}