package one.mann.weatherman.ui.detail

import one.mann.domain.models.ErrorType
import one.mann.weatherman.ui.common.models.Weather

/* Created by Psmann. */

internal data class DetailUiModel(
        val weatherData: List<Weather> = listOf(), // Weather data
        val viewState: State = State.Idle // Current state of the view
) {
    sealed class State {
        object Idle : State() // Idle state, no change
        object Refreshing : State()// Set whether data is being refreshed or not
        data class ShowError(val errorType: ErrorType) : State() // Pass error type to a Toast
    }
}