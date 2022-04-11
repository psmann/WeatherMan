package one.mann.weatherman.ui.detail

import one.mann.domain.models.ErrorType
import one.mann.weatherman.ui.common.base.BaseUiModelWithState
import one.mann.weatherman.ui.common.models.Weather

/* Created by Psmann. */

/**
 * @property weatherData: Weather data
 * @property viewState: Current state of the view
 */
internal data class DetailUiModel(
    val weatherData: List<Weather> = listOf(),
    val viewState: State = State.Idle
) : BaseUiModelWithState<DetailUiModel> {

    override fun resetState(): DetailUiModel = copy(viewState = State.Idle)

    sealed class State {
        // Idle state, no change
        object Idle : State()

        // Set whether data is being refreshed or not
        object Refreshing : State()

        // Pass error type to a Toast
        data class ShowError(val errorType: ErrorType) : State()
    }
}