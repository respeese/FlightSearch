package com.osu.flightsearch.ui.search

import androidx.annotation.ColorRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.osu.flightsearch.R
import com.osu.flightsearch.data.Airport
import com.osu.flightsearch.data.Favorite
import com.osu.flightsearch.ui.theme.FlightSearchTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel
) {
    val flightSearchUiState = viewModel.flightSearchUiState.collectAsState()
    val favoritesUiState = viewModel.favoritesUiState.collectAsState()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Flight Search")})
        }, modifier = modifier
    ) { innerPadding ->
        SearchBody(
            flightSearchUiState = flightSearchUiState.value,
            favoritesUiState = favoritesUiState.value,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBody(
    flightSearchUiState: FlightSearchUiState,
    favoritesUiState: FavoritesUiState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var text by remember { mutableStateOf(TextFieldValue("")) }
        val chosenAirport: Airport = Airport(0, "FCO", "Da Vinci Airport", 100)

        //search bar
        TextField(
            value = text,
            onValueChange = { newText ->
                text = newText
            },
            placeholder = { Text(text = "Enter departure airport") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "searchIcon"
                )
            }, modifier = modifier.padding(top = 20.dp, bottom = 30.dp)
        )
        //if empty -> favoritesList()
        FavoritesList(
            favesList = favoritesUiState.favoritesList,
            modifier = modifier
        )
        // if typing -> searchSuggestionsList()
//        SearchSuggestionsList(
//            searchSuggestionsList = flightSearchUiState.airportsList,
//            modifier = modifier
//        )
        // if chosenAirport clicked -> flightsList() and fill search bar with AirportCode
//        RoutesList(
//            routesList = flightSearchUiState.airportsList,
//            departingAirport = chosenAirport,
//            modifier = modifier
//        )
    }
}

@Composable
private fun FavoritesList(
    favesList: List<Favorite>,
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
            FavoriteRow(fave, true)
        }
    }
}

@Composable
private fun FavoriteRow(
    fave: Favorite,
    favorited: Boolean,
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
                    text = fave.departureCode,
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
                    text = fave.destinationCode,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = modifier.padding(8.dp)
                )
            }
        }
        IconButton(onClick = { /*TODO*/ }) {
            //TODO: based on favorited bool, show filled or outlined
            Icon(
                Icons.Outlined.Favorite,
                contentDescription = "Favorite",
                modifier = modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun SearchSuggestionsList(
    searchSuggestionsList: List<Airport>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        items(searchSuggestionsList) { airport ->
            Row(
                modifier = modifier.padding(8.dp)
            ){
                Text(
                    text = airport.iataCode,
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
    modifier: Modifier = Modifier
) {
    Text(
        text = "Flights from " + departingAirport.iataCode,
        fontWeight = FontWeight.Bold,
        modifier = modifier.padding(bottom = 8.dp)
    )

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        items(routesList) {airport ->
            RouteRow(departingAirport = departingAirport, arrivingAirport = airport)
        }
    }
}

@Composable
private fun RouteRow(
    departingAirport: Airport,
    arrivingAirport: Airport,
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
                    text = departingAirport.iataCode,
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
                    text = arrivingAirport.iataCode,
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
        IconButton(onClick = { /*TODO*/ }) {
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
        SearchBody(
            FlightSearchUiState(listOf(
                Airport(0, "ATH", "Athens Airport", 100),
                Airport(1, "CHI", "O'Hare Airport", 90)
            )),
            FavoritesUiState(listOf(
                Favorite(0, "ATH", "CHI"),
                Favorite(1, "FCO", "LIS")
            ))
        )
    }
}