/*
Assignment 5: Flight Search App (Data Persistence)
Ryan Speese / speeser@oregonstate.edu
OSU / CS 492
 */

package com.osu.flightsearch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.osu.flightsearch.ui.AppViewModelProvider
import com.osu.flightsearch.ui.search.SearchScreen
import com.osu.flightsearch.ui.search.SearchViewModel
import com.osu.flightsearch.ui.theme.FlightSearchTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlightSearchTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    SearchScreen()
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FlightSearchTheme {
        //SearchScreen()
    }
}