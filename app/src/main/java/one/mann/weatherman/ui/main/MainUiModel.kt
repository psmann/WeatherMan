package one.mann.weatherman.ui.main

import one.mann.domain.models.CitySearchResult
import one.mann.domain.models.ErrorType
import one.mann.domain.models.ViewPagerUpdateType
import one.mann.weatherman.ui.common.models.Weather

/* Created by Psmann. */

internal data class MainUiModel(
    val weatherData: List<Weather> = listOf(), // Weather data
    val cityCount: Int = -1, // Number of cities
    val citySearchResult: List<CitySearchResult> = listOf(), // City search data
    val viewState: State = State.Loading // Current state of the view
) {
    sealed class State {
        object Idle : State() // Idle state, no change
        object Loading : State() // Set whether view is visible or not
        object Refreshing : State() // Set whether data is being refreshed or not
        data class ShowError(val errorType: ErrorType) : State() // Pass error type to a Toast
        data class UpdateViewPager(val updateType: ViewPagerUpdateType) : State() // Update ViewPager with animation
    }
}