package one.mann.weatherman.ui.main

import one.mann.domain.models.CitySearchResult
import one.mann.domain.models.ErrorType
import one.mann.domain.models.ViewPagerUpdateType
import one.mann.weatherman.ui.common.models.Weather

/* Created by Psmann. */

/**
 * @property weatherData: Weather data
 * @property cityCount: Number of cities
 * @property citySearchResult: City search data
 * @property viewState: Current state of the view
 */
internal data class MainUiModel(
    val weatherData: List<Weather> = listOf(),
    val cityCount: Int = -1,
    val citySearchResult: List<CitySearchResult> = listOf(),
    val viewState: State = State.Loading
) {
    sealed class State {
        // Idle state, no change
        object Idle : State()

        // Set whether view is visible or not
        object Loading : State()

        // Set whether data is being refreshed or not
        object Refreshing : State()

        // Pass error type to a Toast
        data class ShowError(val errorType: ErrorType) : State()

        // Update ViewPager with animation
        data class UpdateViewPager(val updateType: ViewPagerUpdateType) : State()
    }
}