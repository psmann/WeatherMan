package one.mann.weatherman.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.content.SharedPreferences
import android.widget.Toast
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import one.mann.weatherman.R
import one.mann.weatherman.api.WeatherResult
import one.mann.weatherman.data.WeatherData
import one.mann.weatherman.model.GpsLocation

class WeatherViewModel(application: Application) : AndroidViewModel(application),
        GpsLocation.GeoCoordinates, SharedPreferences.OnSharedPreferenceChangeListener {

    val weatherLiveData: MutableLiveData<WeatherData> = MutableLiveData()
    val displayLoadingBar: MutableLiveData<Boolean> = MutableLiveData()
    val displayUi: MutableLiveData<Boolean> = MutableLiveData()
    val cityCount: MutableLiveData<Int> = MutableLiveData()
    private val weatherResult: WeatherResult = WeatherResult(application)
    private val weatherData: WeatherData = WeatherData(application)
    private val gpsLocation: GpsLocation = GpsLocation(application, this)

    init {
        weatherLiveData.value = weatherData
        displayLoadingBar.value = weatherData.loadingBar
        displayUi.value = weatherData.uiVisibility
        cityCount.value = weatherData.cityCount
        weatherData.weatherPreferences.registerOnSharedPreferenceChangeListener(this)
        for (i in 1..weatherData.cityCount) {
            weatherData.cityPref(i.toString()).registerOnSharedPreferenceChangeListener(this)
        }
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
                gpsEnabled -> gpsLocation.getLocation()
                weatherData.getWeatherData(WeatherData.LOCATION, weatherData.cityPref("1")) == "" -> {
                    weatherData.saveLoadingBar(false)
//                    Toast.makeText(getApplication(), R.string.gps_needed_for_location, Toast.LENGTH_SHORT).show()
                }
                else -> {
                    makeWeatherCall(emptyArray(), "0")
//                    Toast.makeText(getApplication(), R.string.no_gps_updating_previous_location, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private suspend fun makeWeatherCall(location: Array<Double?>, cityPref: String) { // TODO: redo coroutines
        when (cityPref) {
            "0" -> for (i in 1..weatherData.cityCount) {
                    val lastLocation: Array<Double?> = arrayOf(weatherData.getWeatherData(WeatherData.LATITUDE,
                            weatherData.cityPref(i.toString())).toDouble(), weatherData.getWeatherData(WeatherData.LONGITUDE,
                            weatherData.cityPref(i.toString())).toDouble())
                    weatherResult.weatherCall(lastLocation, i.toString())
            }
            else -> {
                weatherResult.weatherCall(location, cityPref)
                for (i in 1..weatherData.cityCount) {
                    if (i.toString() == cityPref) continue
                        val lastLocation: Array<Double?> = arrayOf(weatherData.getWeatherData(WeatherData.LATITUDE,
                                weatherData.cityPref(i.toString())).toDouble(), weatherData.getWeatherData(WeatherData.LONGITUDE,
                                weatherData.cityPref(i.toString())).toDouble())
                        weatherResult.weatherCall(lastLocation, i.toString())
                }
            }
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        when (key) { // weatherLiveData can be split up into its constituent vars and handled separately for efficiency
            WeatherData.UI_VISIBILITY -> displayUi.value = weatherData.uiVisibility
            WeatherData.LOADING_BAR -> displayLoadingBar.value = weatherData.loadingBar
            WeatherData.COUNT -> cityCount.value = weatherData.cityCount
            else -> weatherLiveData.value = weatherData
        }
    }

    override fun getCoordinates(location: Array<Double?>) {
        GlobalScope.launch { makeWeatherCall(location, "1") }
    }
}