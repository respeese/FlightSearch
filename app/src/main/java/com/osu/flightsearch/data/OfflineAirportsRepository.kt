/*
Assignment 5: Flight Search App (Data Persistence)
Ryan Speese / speeser@oregonstate.edu
OSU / CS 492
 */

package com.osu.flightsearch.data

import kotlinx.coroutines.flow.Flow

class OfflineAirportsRepository(private val airportDao: AirportDao) : AirportsRepository {
    override fun getMatchingAirportsList(searchString: String): List<Airport> = airportDao.getMatchingAirports(searchString)

    override fun getArrivalAirportsList(codeSelected: String): List<Airport> = airportDao.getArrivalAirports(codeSelected)
}