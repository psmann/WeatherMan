package one.mann.weatherman.ui.detail

import one.mann.domain.models.Errors
import one.mann.domain.models.weather.Weather

/* Created by Psmann. */

internal data class DetailViewState(
        val isRefreshing: Boolean = false,
        val errorType: Errors = Errors.NoError,
        val weatherData: List<Weather> = listOf()
)