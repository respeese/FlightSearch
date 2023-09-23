/*
Assignment 5: Flight Search App (Data Persistence)
Ryan Speese / speeser@oregonstate.edu
OSU / CS 492
 */

package com.osu.flightsearch.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.osu.flightsearch.R
import com.osu.flightsearch.data.Airport
import com.osu.flightsearch.data.Favorite
import com.osu.flightsearch.ui.AppViewModelProvider
import com.osu.flightsearch.ui.theme.FlightSearchTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Flight Search")})
        }, modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = modifier.padding(innerPadding),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val favoriteListUiState = viewModel.favoriteListUiState.collectAsState()
            val searchString = viewModel.searchStringState.value
            val selectedAirport = viewModel.selectedAirportState.value
            var text by remember { mutableStateOf(TextFieldValue("")) }
            val keyboardController = LocalSoftwareKeyboardController.current

            //search bar
            TextField(
                value = text,
                onValueChange = { newText ->
                    text = newText
                    viewModel.updateSearchString(newText.text)
                    viewModel.updateSelectedAirport(Airport())
                },
                placeholder = { Text(text = "Enter departure airport") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "searchIcon"
                    )
                },
                keyboardActions = KeyboardActions(
                    onDone = {keyboardController?.hide()},
                    onSearch = { keyboardController?.hide() }
                ),
                modifier = modifier.padding(top = 20.dp, bottom = 30.dp)
            )

            if(searchString.isEmpty()) {
                FavoritesList(
                    favesList = favoriteListUiState.value,
                    onUnfavorite = { viewModel.unFavoriteFlight(it) },
                    modifier = modifier
                )
            } else {
                if(selectedAirport.iata_code.isEmpty()) {
                    SearchSuggestionsList(
                        searchSuggestionsList = viewModel.airportsList.value,
                        onChooseAirport = { viewModel.updateSelectedAirport(it)},
                        modifier = modifier
                    )
                } else {
                    RoutesList(
                        routesList = viewModel.routesList.value,
                        departingAirport = selectedAirport,
                        onFavorite = { viewModel.addFavoriteFlight(it) },
                        onUnfavorite = { viewModel.unFavoriteFlight(it) },
                        modifier = modifier
                    )
                }
            }
        }
    }
}

@Composable
private fun FavoritesList(
    favesList: List<Favorite>,
    onUnfavorite: (Favorite?) -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text = "Favorite Routes",
        fontWeight = FontWeight.Bold,
        modifier = modifier.padding(bottom = 8.dp)
    )

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        items(favesList) {fave ->
            FavoriteRow(fave, onUnfavorite)
        }
    }
}

@Composable
private fun FavoriteRow(
    fave: Favorite,
    onUnFavorite: (Favorite?) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .padding(bottom = 6.dp)
            .background(color = colorResource(id = R.color.light_grey))
            .fillMaxSize(0.7F)
            .clip(RoundedCornerShape(topEnd = 8.dp, bottomStart = 8.dp))
    ) {
        Column() {
            Row() {
                Text(
                    text = "DEPART",
                    fontSize = 12.sp,
                    modifier = modifier.padding(8.dp)
                )
                Text(
                    text = fave.departure_code,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = modifier.padding(8.dp)
                )
            }
            Row() {
                Text(
                    text = "ARRIVE",
                    fontSize = 12.sp,
                    modifier = modifier.padding(8.dp)
                )
                Text(
                    text = fave.destination_code,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = modifier.padding(8.dp)
                )
            }
        }
        IconButton(onClick = {onUnFavorite(fave)}) {
            Icon(
                Icons.Filled.Favorite,
                contentDescription = "Favorite",
                modifier = modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun SearchSuggestionsList(
    searchSuggestionsList: List<Airport>,
    onChooseAirport: (Airport) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        items(searchSuggestionsList) { airport ->
            Row(
                modifier = modifier
                    .padding(8.dp)
                    .background(color = colorResource(id = R.color.light_grey))
                    .clickable {
                        onChooseAirport(airport)
                    }
            ){
                Text(
                    text = airport.iata_code,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "  " + airport.name
                )
            }

        }
    }
}



@Composable
private fun RoutesList(
    routesList: List<Airport>,
    departingAirport: Airport,
    onFavorite: (Favorite?) -> Unit,
    onUnfavorite: (Favorite?) -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text = "Flights from " + departingAirport.iata_code,
        fontWeight = FontWeight.Bold,
        modifier = modifier.padding(bottom = 8.dp)
    )

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        items(routesList) {airport ->
            RouteRow(
                departingAirport = departingAirport,
                arrivingAirport = airport,
                onFavorite = onFavorite,
                onUnfavorite = onUnfavorite
            )
        }
    }
}

@Composable
private fun RouteRow(
    departingAirport: Airport,
    arrivingAirport: Airport,
    onFavorite: (Favorite?) -> Unit,
    onUnfavorite: (Favorite?) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .padding(bottom = 6.dp)
            .clip(RoundedCornerShape(topEnd = 16.dp, bottomStart = 16.dp))
            .background(color = colorResource(id = R.color.light_grey))
            .fillMaxSize(0.7F)
    ) {
        var addedToFaves = false
        val fave = Favorite(
            departure_code = departingAirport.iata_code,
            destination_code = arrivingAirport.iata_code
        )

        Column() {
            Text(
                text = "DEPART",
                fontSize = 12.sp,
                modifier = modifier.padding(8.dp)
            )
            Row(
                modifier = modifier.padding(start = 8.dp)
            ) {
                Text(
                    text = departingAirport.iata_code,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = departingAirport.name,
                    fontSize = 12.sp,
                    modifier = modifier.padding(start = 6.dp)
                )
            }
            Text(
                text = "ARRIVE",
                fontSize = 12.sp,
                modifier = modifier.padding(8.dp)
            )
            Row(
                modifier = modifier.padding(start = 8.dp, bottom = 8.dp)
            ) {
                Text(
                    text = arrivingAirport.iata_code,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = arrivingAirport.name,
                    fontSize = 12.sp,
                    modifier = modifier.padding(start = 6.dp)
                )
            }
        }
        IconButton(onClick = {
            if(!addedToFaves) {
                onFavorite(fave)
                addedToFaves = true
            } else {
                onUnfavorite(fave)
                addedToFaves = false
            }
        }) {
            Icon(
                Icons.Outlined.Favorite,
                contentDescription = "Favorite",
                modifier = modifier.size(24.dp)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    FlightSearchTheme {
//        SearchBody(
//            FlightSearchUiState(listOf(
//                Airport(0, "ATH", "Athens Airport", 100),
//                Airport(1, "CHI", "O'Hare Airport", 90)
//            )),
//            FavoritesUiState(listOf(
//                Favorite(0, "ATH", "CHI"),
//                Favorite(1, "FCO", "LIS")
//            )),
//        )
    }
}