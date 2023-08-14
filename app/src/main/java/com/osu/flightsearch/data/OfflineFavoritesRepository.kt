package com.osu.flightsearch.data

import kotlinx.coroutines.flow.Flow

class OfflineFavoritesRepository(private val favoriteDao: FavoriteDao): FavoritesRepository {
    override suspend fun insertFavorite(favorite: Favorite) = favoriteDao.insert(favorite)

    override fun getAllFavoriteFlightsStream(): Flow<List<Favorite>> = favoriteDao.getAllFavoriteFlights()
}