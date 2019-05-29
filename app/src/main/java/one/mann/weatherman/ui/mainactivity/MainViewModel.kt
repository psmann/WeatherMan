package one.mann.weatherman.ui.mainactivity

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import one.mann.weatherman.api.WeatherResult
import one.mann.weatherman.framework.data.SharedPreferenceStorage
import one.mann.weatherman.framework.location.GpsLocation

internal class MainViewModel(application: Application) : AndroidViewModel(application),
        SharedPreferences.OnSharedPreferenceChangeListener {

    val weatherLiveData: MutableLiveData<SharedPreferenceStorage> = MutableLiveData()
    val displayLoadingBar: MutableLiveData<Boolean> = MutableLiveData()
    val displayUi: MutableLiveData<Boolean> = MutableLiveData()
    val displayToast: MutableLiveData<Int> = MutableLiveData()
    val cityCount: MutableLiveData<Int> = MutableLiveData()
    private val sharedPrefStorage: SharedPreferenceStorage = SharedPreferenceStorage(application)
    private val weatherResult: WeatherResult = WeatherResult(sharedPrefStorage)
    private val gpsLocation: GpsLocation = GpsLocation(application)

    init {
        weatherLiveData.value = sharedPrefStorage
        displayLoadingBar.value = sharedPrefStorage.loadingBar
        displayToast.value = 0
        cityCount.value = sharedPrefStorage.cityCount
        sharedPrefStorage.weatherPreferences.registerOnSharedPreferenceChangeListener(this)
        for (i in 1..sharedPrefStorage.cityCount)
            sharedPrefStorage.cityPref(i.toString()).registerOnSharedPreferenceChangeListener(this)
        displayUi.value = sharedPrefStorage.uiVisibility
    }

    fun newCityLocation(lat: Double, lon: Double) {
        val loc: Array<Double?> = arrayOf(lat, lon)
        sharedPrefStorage.setCityCount(cityCount.value!! + 1)
        val newCity = sharedPrefStorage.cityCount.toString()
        sharedPrefStorage.cityPref(newCity).registerOnSharedPreferenceChangeListener(this)
        GlobalScope.launch { makeWeatherCall(loc, newCity) }
    }

    fun getWeather(gpsEnabled: Boolean) {
        sharedPrefStorage.saveLoadingBar(true)
        GlobalScope.launch {
            when {
                gpsEnabled -> gpsLocation.getLocation { location ->
                    GlobalScope.launch { makeWeatherCall(location, "1") }
                }
                sharedPrefStorage.getWeatherData(SharedPreferenceStorage.LOCATION,
                        sharedPrefStorage.cityPref("1")) == "" -> {
                    sharedPrefStorage.saveLoadingBar(false)
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
            "0" -> for (i in 1..sharedPrefStorage.cityCount) {
                val lastLocation: Array<Double?> = arrayOf(
                        sharedPrefStorage.getWeatherData(SharedPreferenceStorage.LATITUDE,
                        sharedPrefStorage.cityPref(i.toString()))?.toDouble(),
                        sharedPrefStorage.getWeatherData(SharedPreferenceStorage.LONGITUDE,
                        sharedPrefStorage.cityPref(i.toString()))?.toDouble())
                weatherResult.weatherCall(lastLocation, i.toString()) { displayToast(6) }
            }
            else -> {
                weatherResult.weatherCall(location, cityPref) { displayToast(6) }
                for (i in 1..sharedPrefStorage.cityCount) {
                    if (i.toString() == cityPref) continue
                    val lastLocation: Array<Double?> = arrayOf(
                            sharedPrefStorage.getWeatherData(SharedPreferenceStorage.LATITUDE,
                            sharedPrefStorage.cityPref(i.toString()))?.toDouble(),
                            sharedPrefStorage.getWeatherData(SharedPreferenceStorage.LONGITUDE,
                            sharedPrefStorage.cityPref(i.toString()))?.toDouble())
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
            SharedPreferenceStorage.UI_VISIBILITY -> displayUi.value = sharedPrefStorage.uiVisibility
            SharedPreferenceStorage.LOADING_BAR -> displayLoadingBar.value = sharedPrefStorage.loadingBar
            SharedPreferenceStorage.COUNT -> cityCount.value = sharedPrefStorage.cityCount
            SharedPreferenceStorage.UPDATE_ALL -> weatherLiveData.value = sharedPrefStorage
        }
    }
}