package one.mann.weatherman.ui.main

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import one.mann.domain.model.Location
import one.mann.domain.model.LocationType
import one.mann.domain.model.Weather
import one.mann.interactors.usecase.*
import one.mann.weatherman.ui.common.base.BaseViewModel

internal class MainViewModel(
        private val addCity: AddCity,
        private val getAllWeather: GetAllWeather,
        private val removeCity: RemoveCity,
        private val updateWeather: UpdateWeather,
        private val getCityCount: GetCityCount
) : BaseViewModel() {

    val weatherData: MutableLiveData<List<Weather>> = MutableLiveData()
    val cityCount: MutableLiveData<Int> = MutableLiveData()
    val displayUI: MutableLiveData<Boolean> = MutableLiveData()
    val loadingState: MutableLiveData<Boolean> = MutableLiveData()

    init {
        displayUI.value = false
        loadingState.value = false
        updateUI()
    }

    fun addCity(apiLocation: Location? = null) {
        loadingState.value = true
        launch(Dispatchers.IO) {
            addCity.invoke(apiLocation)
            updateUI()
        }
    }

    fun updateWeather(locationType: LocationType) {
        loadingState.value = true // Start refreshing
        launch(Dispatchers.IO) {
            updateWeather.invoke(locationType)
            updateUI()
        }
    }

    private fun updateUI() {
        launch {
            cityCount.value = getCityCount.invoke() // Update ViewPager position count
            val data = getAllWeather.invoke()
            if (data.isNotEmpty()) {
                weatherData.value = data // Update all weather data
                if (displayUI.value == false) displayUI.value = true // Show UI if hidden
            }
            loadingState.value = false // Stop refreshing
        }
    }
}