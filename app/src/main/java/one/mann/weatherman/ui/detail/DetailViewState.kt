package one.mann.weatherman.ui.detail

import one.mann.domain.model.Errors
import one.mann.domain.model.Errors.NO_ERROR
import one.mann.domain.model.weather.Weather

/* Created by Psmann. */

internal data class DetailViewState(
        val isRefreshing: Boolean = false,
        val error: Errors = NO_ERROR,
        val weatherData: List<Weather> = listOf()
)