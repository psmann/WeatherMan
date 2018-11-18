package one.mann.weatherman.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.content.SharedPreferences
import android.widget.Toast

import one.mann.weatherman.R
import one.mann.weatherman.api.WeatherResult
import one.mann.weatherman.data.WeatherData
import one.mann.weatherman.model.GpsLocation

class CurrentWeatherViewModel(application: Application) : AndroidViewModel(application),
        GpsLocation.GeoCoordinates, SharedPreferences.OnSharedPreferenceChangeListener {

    val weatherLiveData: MutableLiveData<WeatherData> = MutableLiveData()
    val displayLoadingBar: MutableLiveData<Boolean> = MutableLiveData()
    val displayUi: MutableLiveData<Boolean> = MutableLiveData()
    private val weatherResult: WeatherResult = WeatherResult(application)
    private val weatherData: WeatherData = WeatherData(application)
    private val gpsLocation: GpsLocation = GpsLocation(application, this)

    init {
        weatherLiveData.value = weatherData
        displayLoadingBar.value = weatherData.loadingBar
        displayUi.value = weatherData.uiVisibility
        weatherData.preferences.registerOnSharedPreferenceChangeListener(this)
    }

    fun getWeather(gpsEnabled: Boolean) {
        weatherData.saveLoadingBar(true)
        when {
            gpsEnabled -> gpsLocation.getLocation()
            weatherData.getWeatherData(WeatherData.LOCATION) == "" -> {
                weatherData.saveLoadingBar(false)
                Toast.makeText(getApplication(), R.string.gps_needed_for_location, Toast.LENGTH_SHORT).show()
            }
            else -> {
                val lastLocation: Array<Double?> =
                        arrayOf(java.lang.Double.parseDouble(weatherData.getWeatherData(WeatherData.LATITUDE)),
                        java.lang.Double.parseDouble(weatherData.getWeatherData(WeatherData.LONGITUDE)))
                weatherResult.weatherCall(lastLocation)
                Toast.makeText(getApplication(), R.string.no_gps_updating_previous_location, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        when (key) {
            // weatherLiveData can be split up into different objects and handled separately for efficiency
            WeatherData.UI_VISIBILITY -> displayUi.setValue(weatherData.uiVisibility)
            WeatherData.LOADING_BAR -> displayLoadingBar.setValue(weatherData.loadingBar)
            else -> weatherLiveData.setValue(weatherData)
        }
    }

    override fun getCoordinates(location: Array<Double?>) {
        weatherResult.weatherCall(location)
    }
}