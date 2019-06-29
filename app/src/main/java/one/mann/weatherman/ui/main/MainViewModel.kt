package one.mann.weatherman.ui.main

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import one.mann.domain.model.Location
import one.mann.domain.model.LocationType
import one.mann.domain.model.Weather
import one.mann.interactors.usecase.*
import one.mann.weatherman.ui.common.base.BaseViewModel
import java.io.IOException
import javax.inject.Inject

internal class MainViewModel @Inject constructor(
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
    val displayError: MutableLiveData<Boolean> = MutableLiveData()

    init {
        displayUI.value = false // Hide UI until recyclerView has been loaded with data
        displayError.value = false
        updateUI()
    }

    fun addCity(apiLocation: Location? = null) {
        loadingState.value = true
        launch {
            try {
                withContext(Dispatchers.IO) { addCity.invoke(apiLocation) }
            } catch (e: IOException) {
                displayError.value = true
            }
            updateUI()
        }
    }

    fun updateWeather(locationType: LocationType) {
        loadingState.value = true // Start refreshing
        launch {
            try {
                withContext(Dispatchers.IO) { updateWeather.invoke(locationType) }
            } catch (e: IOException) {
                displayError.value = true
            }
            updateUI()
        }
    }

    fun removeCity(position: Int) {
        val cityName = weatherData.value?.get(position)?.cityName ?: return // Return if null
        launch(Dispatchers.IO) {
            removeCity.invoke(cityName)
            updateUI()
        }
    }

    private fun updateUI() {
        launch {
            val data = withContext(Dispatchers.IO) { getAllWeather.invoke() }
            if (data.isNotEmpty()) {
                weatherData.value = data // Update all weather data
                if (displayUI.value == false) displayUI.value = true // Show UI if hidden
            }
            cityCount.value = getCityCount.invoke() // Update ViewPager only after updating weatherData (if not null)
            loadingState.value = false // Stop refreshing
        }
    }

    fun resetDisplayError() {
        displayError.value = false
    }
}