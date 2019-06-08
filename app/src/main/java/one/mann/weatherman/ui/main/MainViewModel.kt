package one.mann.weatherman.ui.main

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import one.mann.weatherman.api.WeatherResult
import one.mann.weatherman.framework.data.sharedprefs.WeatherSharedPref
import one.mann.weatherman.framework.data.location.GpsLocation

internal class MainViewModel(application: Application) : AndroidViewModel(application),
        SharedPreferences.OnSharedPreferenceChangeListener {

    val weatherLiveData: MutableLiveData<WeatherSharedPref> = MutableLiveData()
    val displayLoadingBar: MutableLiveData<Boolean> = MutableLiveData()
    val displayUi: MutableLiveData<Boolean> = MutableLiveData()
    val displayToast: MutableLiveData<Int> = MutableLiveData()
    val cityCount: MutableLiveData<Int> = MutableLiveData()
    private val weatherSharedPref: WeatherSharedPref = WeatherSharedPref(application)
    private val weatherResult: WeatherResult = WeatherResult(weatherSharedPref)
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
            "0" -> for (i in 1..weatherSharedPref.cityCount) {
                val lastLocation: Array<Double?> = arrayOf(
                        weatherSharedPref.getWeatherData(WeatherSharedPref.LATITUDE,
                        weatherSharedPref.cityPref(i.toString()))?.toDouble(),
                        weatherSharedPref.getWeatherData(WeatherSharedPref.LONGITUDE,
                        weatherSharedPref.cityPref(i.toString()))?.toDouble())
                weatherResult.weatherCall(lastLocation, i.toString()) { displayToast(6) }
            }
            else -> {
                weatherResult.weatherCall(location, cityPref) { displayToast(6) }
                for (i in 1..weatherSharedPref.cityCount) {
                    if (i.toString() == cityPref) continue
                    val lastLocation: Array<Double?> = arrayOf(
                            weatherSharedPref.getWeatherData(WeatherSharedPref.LATITUDE,
                            weatherSharedPref.cityPref(i.toString()))?.toDouble(),
                            weatherSharedPref.getWeatherData(WeatherSharedPref.LONGITUDE,
                            weatherSharedPref.cityPref(i.toString()))?.toDouble())
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
        when (key) { // can be split up into constituent vars and handled separately for efficiency
            WeatherSharedPref.UI_VISIBILITY -> displayUi.value = weatherSharedPref.uiVisibility
            WeatherSharedPref.LOADING_BAR -> displayLoadingBar.value = weatherSharedPref.loadingBar
            WeatherSharedPref.COUNT -> cityCount.value = weatherSharedPref.cityCount
            WeatherSharedPref.UPDATE_ALL -> weatherLiveData.value = weatherSharedPref
        }
    }
}