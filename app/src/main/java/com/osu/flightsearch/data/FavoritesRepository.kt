package com.osu.flightsearch.data

import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    suspend fun insertFavorite(favorite: Favorite)

    fun getAllFavoriteFlightsStream(): Flow<List<Favorite>>
}