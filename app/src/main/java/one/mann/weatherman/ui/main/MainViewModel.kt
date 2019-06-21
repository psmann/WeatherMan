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

    init {
        displayUI.value = false // todo: called multiple times, not efficient read from prefs instead
        getCount()
        getWeather()
    }

    fun addCity(apiLocation: Location? = null) {
        launch(Dispatchers.IO) {
            addCity.invoke(apiLocation)
            getCount()
            getWeather()
        }
    }

    fun updateWeather(locationType: LocationType) {
        launch(Dispatchers.IO) {
            updateWeather.invoke(locationType)
            getCount()
            getWeather()

        }
    }

    private fun getCount() {
        launch {
            cityCount.value = getCityCount.invoke()
        }
    }

    private fun getWeather() {
        launch {
            val data = getAllWeather.invoke()
            if (data.isNotEmpty()) {
                weatherData.value = data
                displayUI.value = true
            }
        }
    }
}