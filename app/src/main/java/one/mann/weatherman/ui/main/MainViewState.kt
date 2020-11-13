package one.mann.weatherman.ui.main

import one.mann.domain.models.CitySearchResult
import one.mann.domain.models.Errors
import one.mann.domain.models.Errors.NO_ERROR
import one.mann.domain.models.weather.Weather

/* Created by Psmann. */

internal data class MainViewState(
        val isRefreshing: Boolean = false,
        val isLoading: Boolean = true,
        val errorType: Errors = NO_ERROR,
        val errorMessage: String = "Error: ",
        val cityCount: Int = -1,
        val weatherData: List<Weather> = listOf(),
        val citySearchResult: List<CitySearchResult> = listOf()
)