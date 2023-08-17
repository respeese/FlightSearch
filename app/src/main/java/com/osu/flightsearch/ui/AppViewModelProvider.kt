package com.osu.flightsearch.ui

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.osu.flightsearch.FlightSearchApplication
import com.osu.flightsearch.ui.search.SearchViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            SearchViewModel(
                FlightSearchApplication().container.airportsRepository,
                FlightSearchApplication().container.favoritesRepository
            )
        }
    }
}