package com.osu.flightsearch.data

import kotlinx.coroutines.flow.Flow

class OfflineAirportsRepository(private val airportDao: AirportDao) : AirportsRepository {
    override fun getMatchingAirportsStream(codeEntered: String, nameEntered: String): Flow<List<Airport>> = airportDao.getMatchingAirports(codeEntered, nameEntered)

    override fun getArrivalAirportsStream(codeSelected: String): Flow<List<Airport>> = airportDao.getArrivalAirports(codeSelected)
}