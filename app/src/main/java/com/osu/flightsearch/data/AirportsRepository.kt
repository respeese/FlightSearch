/*
Assignment 5: Flight Search App (Data Persistence)
Ryan Speese / speeser@oregonstate.edu
OSU / CS 492
 */

package com.osu.flightsearch.data

import kotlinx.coroutines.flow.Flow

interface AirportsRepository {
    fun getMatchingAirportsList(searchString: String): List<Airport>

    fun getArrivalAirportsList(codeSelected: String): List<Airport>
}