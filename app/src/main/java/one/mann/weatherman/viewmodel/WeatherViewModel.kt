package one.mann.weatherman.viewmodel

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import one.mann.weatherman.api.WeatherResult
import one.mann.weatherman.data.WeatherData
import one.mann.weatherman.util.GpsLocation

class WeatherViewModel(application: Application) : AndroidViewModel(application),
        SharedPreferences.OnSharedPreferenceChangeListener {

    val weatherLiveData: MutableLiveData<WeatherData> = MutableLiveData()
    val displayLoadingBar: MutableLiveData<Boolean> = MutableLiveData()
    val displayUi: MutableLiveData<Boolean> = MutableLiveData()
    val displayToast: MutableLiveData<Int> = MutableLiveData()
    val cityCount: MutableLiveData<Int> = MutableLiveData()
    private val weatherData: WeatherData = WeatherData(application)
    private val weatherResult: WeatherResult = WeatherResult(weatherData)
    private val gpsLocation: GpsLocation = GpsLocation(application)

    init {
        weatherLiveData.value = weatherData
        displayLoadingBar.value = weatherData.loadingBar
        displayToast.value = 0
        cityCount.value = weatherData.cityCount
        weatherData.weatherPreferences.registerOnSharedPreferenceChangeListener(this)
        for (i in 1..weatherData.cityCount)
            weatherData.cityPref(i.toString()).registerOnSharedPreferenceChangeListener(this)
        displayUi.value = weatherData.uiVisibility
    }

    fun newCityLocation(lat: Double, lon: Double) {
        val loc: Array<Double?> = arrayOf(lat, lon)
        weatherData.setCityCount(cityCount.value!! + 1)
        val newCity = weatherData.cityCount.toString()
        weatherData.cityPref(newCity).registerOnSharedPreferenceChangeListener(this)
        GlobalScope.launch { makeWeatherCall(loc, newCity) }
    }

    fun getWeather(gpsEnabled: Boolean) {
        weatherData.saveLoadingBar(true)
        GlobalScope.launch {
            when {
                gpsEnabled -> gpsLocation.getLocation { location ->
                    GlobalScope.launch { makeWeatherCall(location, "1") }
                }
                weatherData.getWeatherData(WeatherData.LOCATION, weatherData.cityPref("1")) == "" -> {
                    weatherData.saveLoadingBar(false)
                    withContext(Dispatchers.Main) { displayToast(4) }
                }
                else -> {
                    makeWeatherCall(emptyArray(), "0")
                    withContext(Dispatchers.Main) { displayToast(5) }
                }
            }
        }
    }

    private suspend fun makeWeatherCall(location: Array<Double?>, cityPref: String) {
        when (cityPref) {
            "0" -> for (i in 1..weatherData.cityCount) {
                val lastLocation: Array<Double?> = arrayOf(weatherData.getWeatherData(WeatherData.LATITUDE,
                        weatherData.cityPref(i.toString()))?.toDouble(), weatherData.getWeatherData(WeatherData.LONGITUDE,
                        weatherData.cityPref(i.toString()))?.toDouble())
                weatherResult.weatherCall(lastLocation, i.toString()) { displayToast(6) }
            }
            else -> {
                weatherResult.weatherCall(location, cityPref) { displayToast(6) }
                for (i in 1..weatherData.cityCount) {
                    if (i.toString() == cityPref) continue
                    val lastLocation: Array<Double?> = arrayOf(weatherData.getWeatherData(WeatherData.LATITUDE,
                            weatherData.cityPref(i.toString()))?.toDouble(), weatherData.getWeatherData(WeatherData.LONGITUDE,
                            weatherData.cityPref(i.toString()))?.toDouble())
                    weatherResult.weatherCall(lastLocation, i.toString()) { displayToast(6) }
                }
            }
        }
    }

    private fun displayToast(value: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            displayToast.value = value
            delay(10L)
            displayToast.value = 0
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        when (key) { // weatherLiveData can be split up into its constituent vars and handled separately for efficiency
            WeatherData.UI_VISIBILITY -> displayUi.value = weatherData.uiVisibility
            WeatherData.LOADING_BAR -> displayLoadingBar.value = weatherData.loadingBar
            WeatherData.COUNT -> cityCount.value = weatherData.cityCount
            WeatherData.UPDATE_ALL -> weatherLiveData.value = weatherData
        }
    }
}