/*
Assignment 5: Flight Search App (Data Persistence)
Ryan Speese / speeser@oregonstate.edu
OSU / CS 492
 */

package com.osu.flightsearch.data

import android.content.Context

interface AppContainer {
    val airportsRepository: AirportsRepository
    val favoritesRepository: FavoritesRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val airportsRepository: AirportsRepository by lazy {
        OfflineAirportsRepository(FlightSearchDatabase.getDatabase(context).airportDao())
    }

    override val favoritesRepository: FavoritesRepository by lazy {
        OfflineFavoritesRepository(FlightSearchDatabase.getDatabase(context).favoriteDao())
    }
}