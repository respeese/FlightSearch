package com.osu.flightsearch.data

import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    suspend fun insertFavorite(favorite: Favorite)

    suspend fun deleteFavorite(favorite: Favorite)

    fun getAllFavoriteFlightsStream(): Flow<List<Favorite>>
}