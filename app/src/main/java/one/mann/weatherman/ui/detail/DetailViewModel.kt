package one.mann.weatherman.ui.detail

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import one.mann.domain.model.LocationType
import one.mann.domain.model.Weather
import one.mann.interactors.usecases.GetAllWeather
import one.mann.interactors.usecases.UpdateWeather
import one.mann.weatherman.ui.common.base.BaseViewModel
import java.io.IOException
import javax.inject.Inject

internal class DetailViewModel @Inject constructor(
        private val getAllWeather: GetAllWeather,
        private val updateWeather: UpdateWeather
) : BaseViewModel() {

    val weatherData: MutableLiveData<List<Weather>> = MutableLiveData()
    val uiModel = MutableLiveData<UiModel>()

    init {
        updateUI()
    }

    sealed class UiModel {
        data class Refreshing(val loading: Boolean) : UiModel()
        object ShowError : UiModel()
    }

    fun updateWeather(locationType: LocationType) {
        launch {
            uiModel.value = UiModel.Refreshing(true) // Start refreshing
            try {
                withContext(IO) { updateWeather.invoke(locationType) }
            } catch (e: IOException) {
                uiModel.value = UiModel.ShowError
            }
            updateUI()
        }
    }

    private fun updateUI() {
        launch {
            val data = withContext(IO) { getAllWeather.invoke() }
            if (data.isNotEmpty()) {
                weatherData.value = data // Update all weather data
            }
            uiModel.value = UiModel.Refreshing(false) // Stop refreshing
        }
    }
}