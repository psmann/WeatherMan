package one.mann.weatherman.ui.main

import one.mann.domain.model.Errors
import one.mann.domain.model.Errors.NO_ERROR
import one.mann.domain.model.weather.Weather

/* Created by Psmann. */

internal data class MainViewState(
        val isRefreshing: Boolean = false,
        val isLoading: Boolean = true,
        val error: Errors = NO_ERROR,
        val cityCount: Int = -1,
        val weatherData: List<Weather> = listOf()
)