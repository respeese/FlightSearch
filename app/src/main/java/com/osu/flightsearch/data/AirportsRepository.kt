package com.osu.flightsearch.data

import kotlinx.coroutines.flow.Flow

interface AirportsRepository {
    fun getMatchingAirportsStream(codeEntered: String, nameEntered: String): Flow<List<Airport>>

    fun getArrivalAirportsStream(codeSelected: String): Flow<List<Airport>>
}