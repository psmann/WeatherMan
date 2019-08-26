package one.mann.weatherman.ui.main

import one.mann.domain.model.Weather

internal data class MainViewState(
        val isRefreshing: Boolean = false,
        val isLoading: Boolean = true,
        val showError: Boolean = false,
        val cityCount: Int = -1,
        val weatherData: List<Weather> = listOf()
)