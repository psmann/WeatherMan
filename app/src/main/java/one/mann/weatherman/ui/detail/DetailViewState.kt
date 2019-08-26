package one.mann.weatherman.ui.detail

import one.mann.domain.model.Weather

internal data class DetailViewState(
        val isRefreshing: Boolean = false,
        val showError: Boolean = false,
        val weatherData: List<Weather> = listOf()
)