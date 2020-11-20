package one.mann.weatherman.ui.main

import one.mann.domain.models.CitySearchResult
import one.mann.domain.models.Errors
import one.mann.domain.models.ViewPagerUpdateType
import one.mann.domain.models.weather.Weather

/* Created by Psmann. */

internal data class MainUiModel(
        val weatherData: List<Weather> = listOf(),
        val citySearchResult: List<CitySearchResult> = listOf(),
        val viewState: State
) {
    sealed class State {
        object Idle : State() // No change
        object Loading : State() // Whether view is visible or not
        object Refreshing : State() // Whether data is being refreshed or not
        data class Error(val errorType: Errors = Errors.NoError) : State()
        data class UpdateViewPager(val updateType: ViewPagerUpdateType = ViewPagerUpdateType.NO_CHANGE) : State()
    }
}