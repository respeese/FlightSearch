package com.osu.flightsearch.data

import kotlinx.coroutines.flow.Flow

class OfflineFavoritesRepository(private val favoriteDao: FavoriteDao): FavoritesRepository {
    override suspend fun insertFavorite(favorite: Favorite) = favoriteDao.insert(favorite)

    override suspend fun deleteFavorite(favorite: Favorite) = favoriteDao.delete(favorite)

    override fun getAllFavoriteFlightsStream(): Flow<List<Favorite>> = favoriteDao.getAllFavoriteFlights()
}