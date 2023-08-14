package com.osu.flightsearch

import android.app.Application
import com.osu.flightsearch.data.AppContainer
import com.osu.flightsearch.data.AppDataContainer

class FlightSearchApplication: Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}