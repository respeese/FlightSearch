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
import kotlinx.coroutines.launch

class SearchViewModel(
    private val airportsRepository: AirportsRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {
    private val codeEntered: String = checkNotNull("") //TODO: get codeEntered + nameEntered arg from destination
    private val nameEntered: String = checkNotNull("")
    private val codeSelected: String = checkNotNull("")

    // Holds list of airports that match user's search text
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

    // Holds list of airports that have flights from chosen airport
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

    fun favoriteFlight(fave: Favorite) {
        viewModelScope.launch {
            favoritesRepository.insertFavorite(fave)
        }
    }

    // Holds list of flights that were favorited by user
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

    fun unFavoriteFlight(fave: Favorite) {
        viewModelScope.launch {
            favoritesRepository.deleteFavorite(fave)
        }
    }

}

data class FlightSearchUiState(val airportsList: List<Airport> = listOf())
data class FavoritesUiState(val favoritesList: List<Favorite> = listOf())