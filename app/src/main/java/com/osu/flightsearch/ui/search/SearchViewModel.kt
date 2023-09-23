/*
Assignment 5: Flight Search App (Data Persistence)
Ryan Speese / speeser@oregonstate.edu
OSU / CS 492
 */

package com.osu.flightsearch.ui.search

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.osu.flightsearch.data.Airport
import com.osu.flightsearch.data.AirportsRepository
import com.osu.flightsearch.data.Favorite
import com.osu.flightsearch.data.FavoritesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SearchViewModel(
    private val airportsRepository: AirportsRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    var searchStringState = mutableStateOf("")
    var selectedAirportState = mutableStateOf(Airport())
    var airportsList = mutableStateOf(mutableListOf<Airport>())
    var routesList = mutableStateOf(mutableListOf<Airport>())

    fun updateSearchString(searchString: String) {
        searchStringState.value = searchString

        getMatchingAirportsList(searchString)
    }

    fun updateSelectedAirport(selectedAirport: Airport) {
        selectedAirportState.value = selectedAirport

        getRoutesList(selectedAirport.iata_code)
    }

    fun getMatchingAirportsList(searchString: String): Job = viewModelScope.launch(Dispatchers.IO) {
        airportsList.value = mutableListOf()
        airportsRepository.getMatchingAirportsList(searchString)
            .onEach { airport ->
                airportsList.value.add(airport)
            }
    }

    fun getRoutesList(codeSelected: String): Job = viewModelScope.launch(Dispatchers.IO) {
        routesList.value = mutableListOf()
        airportsRepository.getArrivalAirportsList(codeSelected)
            .onEach { airport ->
                routesList.value.add(airport)
            }
    }

    // Holds list of airports that have flights from chosen airport
//    val routeResultsUiState: StateFlow<List<Airport>> =
//        airportsRepository.getArrivalAirportsStream(selectedAirportState.value.iata_code)
//            .filterNotNull()
//            .stateIn(
//                scope = viewModelScope,
//                started = SharingStarted.WhileSubscribed(),
//                initialValue = listOf()
//            )

    // Holds list of flights that were favorited by user
    val favoriteListUiState: StateFlow<List<Favorite>> =
        favoritesRepository.getAllFavoriteFlightsStream()
            .filterNotNull()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = listOf()
            )

    fun unFavoriteFlight(fave: Favorite?) {
        if(fave != null) {
            viewModelScope.launch {
                favoritesRepository.deleteFavorite(fave)
            }
        }
    }

    fun addFavoriteFlight(fave: Favorite?) {
        if (fave != null) {
            viewModelScope.launch {
                favoritesRepository.insertFavorite(fave)
            }
        }
    }
}
