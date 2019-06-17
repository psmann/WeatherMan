package one.mann.weatherman.ui.main

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import one.mann.weatherman.R
import one.mann.weatherman.api.openweathermap.OwmResult
import one.mann.weatherman.framework.data.location.GpsLocation
import one.mann.weatherman.framework.data.sharedprefs.WeatherSharedPref

internal class MainViewModel(application: Application) : AndroidViewModel(application),
        SharedPreferences.OnSharedPreferenceChangeListener {

    val weatherLiveData: MutableLiveData<WeatherSharedPref> = MutableLiveData()
    val displayLoadingBar: MutableLiveData<Boolean> = MutableLiveData()
    val displayUi: MutableLiveData<Boolean> = MutableLiveData()
    val displayToast: MutableLiveData<Int> = MutableLiveData()
    val cityCount: MutableLiveData<Int> = MutableLiveData()
    private val weatherSharedPref: WeatherSharedPref = WeatherSharedPref(application)
    private val owmResult: OwmResult = OwmResult(weatherSharedPref)
    private val gpsLocation: GpsLocation = GpsLocation(application)

    init {
        weatherLiveData.value = weatherSharedPref
        displayLoadingBar.value = weatherSharedPref.loadingBar
        displayToast.value = 0
        cityCount.value = weatherSharedPref.cityCount
        weatherSharedPref.mainPref.registerOnSharedPreferenceChangeListener(this)
        for (i in 1..weatherSharedPref.cityCount)
            weatherSharedPref.cityPref(i.toString()).registerOnSharedPreferenceChangeListener(this)
        displayUi.value = weatherSharedPref.uiVisibility
    }

    fun newCityLocation(lat: Double, lon: Double) {
        val loc: Array<Double?> = arrayOf(lat, lon)
        weatherSharedPref.setCityCount(cityCount.value!! + 1)
        val newCity = weatherSharedPref.cityCount.toString()
        weatherSharedPref.cityPref(newCity).registerOnSharedPreferenceChangeListener(this)
        GlobalScope.launch { makeWeatherCall(loc, newCity) }
    }

    fun getWeather(gpsEnabled: Boolean) {
        weatherSharedPref.saveLoadingBar(true)
        GlobalScope.launch {
            when {
                gpsEnabled -> gpsLocation.getLocation { location ->
                    GlobalScope.launch { makeWeatherCall(location, "1") }
                }
                weatherSharedPref.getWeatherData(WeatherSharedPref.LOCATION,
                        weatherSharedPref.cityPref("1")) == "" -> {
                    weatherSharedPref.saveLoadingBar(false)
                    withContext(Dispatchers.Main) { displayToast(R.string.gps_needed_for_location) }
                }
                else -> {
                    makeWeatherCall(emptyArray(), "0")
                    withContext(Dispatchers.Main) { displayToast(R.string.no_gps_updating_previous_location) }
                }
            }
        }
    }

    private suspend fun makeWeatherCall(location: Array<Double?>, cityPref: String) {
        when (cityPref) {
            "0" -> for (i in 1..weatherSharedPref.cityCount) {
                val lastLocation: Array<Double?> = arrayOf(
                        weatherSharedPref.getWeatherData(WeatherSharedPref.LATITUDE,
                                weatherSharedPref.cityPref(i.toString()))?.toDouble(),
                        weatherSharedPref.getWeatherData(WeatherSharedPref.LONGITUDE,
                                weatherSharedPref.cityPref(i.toString()))?.toDouble())
                owmResult.weatherCall(lastLocation, i.toString())
                { displayToast(R.string.server_not_found) }
            }
            else -> {
                owmResult.weatherCall(location, cityPref) { displayToast(R.string.server_not_found) }
                for (i in 1..weatherSharedPref.cityCount) {
                    if (i == cityPref.toInt()) continue // skip city already updated
                    val lastLocation: Array<Double?> = arrayOf(
                            weatherSharedPref.getWeatherData(WeatherSharedPref.LATITUDE,
                                    weatherSharedPref.cityPref(i.toString()))?.toDouble(),
                            weatherSharedPref.getWeatherData(WeatherSharedPref.LONGITUDE,
                                    weatherSharedPref.cityPref(i.toString()))?.toDouble())
                    owmResult.weatherCall(lastLocation, i.toString())
                    { displayToast(R.string.server_not_found) }
                }
            }
        }
    }

    private fun displayToast(value: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            displayToast.value = value
            delay(10L)
            displayToast.value = 0 // stop displaying toast message
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        when (key) { // can be split up into constituent vars and handled separately for efficiency
            WeatherSharedPref.UI_VISIBILITY -> displayUi.value = weatherSharedPref.uiVisibility
            WeatherSharedPref.LOADING_BAR -> displayLoadingBar.value = weatherSharedPref.loadingBar
            WeatherSharedPref.COUNT -> cityCount.value = weatherSharedPref.cityCount
            WeatherSharedPref.UPDATE_ALL -> weatherLiveData.value = weatherSharedPref
        }
    }
}