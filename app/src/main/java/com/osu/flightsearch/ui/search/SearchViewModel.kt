package com.osu.flightsearch.ui.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.osu.flightsearch.data.Airport
import com.osu.flightsearch.data.AirportsRepository
import com.osu.flightsearch.data.Favorite
import com.osu.flightsearch.data.FavoritesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class SearchViewModel(
    savedStateHandle: SavedStateHandle,
    private val airportsRepository: AirportsRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {
    private val codeEntered: String = checkNotNull(savedStateHandle[""]) //TODO: get codeEntered + nameEntered arg from destination
    private val nameEntered: String = checkNotNull(savedStateHandle[""])
    private val codeSelected: String = checkNotNull(savedStateHandle[""])

    val flightSearchUiState: StateFlow<FlightSearchUiState> =
        airportsRepository.getMatchingAirportsStream(codeEntered, nameEntered)
            .filterNotNull()
            .map {
                FlightSearchUiState(it)
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = FlightSearchUiState()
            )

    val routeResultsUiState: StateFlow<FlightSearchUiState> =
        airportsRepository.getArrivalAirportsStream(codeSelected)
            .filterNotNull()
            .map {
                FlightSearchUiState(it)
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = FlightSearchUiState()
            )


    val favoritesUiState: StateFlow<FavoritesUiState> =
        favoritesRepository.getAllFavoriteFlightsStream()
            .filterNotNull()
            .map {
                FavoritesUiState(it)
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = FavoritesUiState()
            )

}

data class FlightSearchUiState(val airportsList: List<Airport> = listOf())
data class FavoritesUiState(val favoritesList: List<Favorite> = listOf())