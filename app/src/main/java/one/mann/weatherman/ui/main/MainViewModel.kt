package one.mann.weatherman.ui.main

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.work.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import one.mann.domain.model.Location
import one.mann.domain.model.LocationType
import one.mann.domain.model.Weather
import one.mann.interactors.usecases.*
import one.mann.weatherman.framework.worker.NotificationWorker
import one.mann.weatherman.ui.common.base.BaseViewModel
import one.mann.weatherman.ui.common.util.NOTIFICATIONS_WORKER
import one.mann.weatherman.ui.common.util.SETTINGS_NOTIFICATIONS_KEY
import one.mann.weatherman.ui.common.util.SETTINGS_UNITS_KEY
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

internal class MainViewModel @Inject constructor(
        private val addCity: AddCity,
        private val getAllWeather: GetAllWeather,
        private val removeCity: RemoveCity,
        private val updateWeather: UpdateWeather,
        private val getCityCount: GetCityCount,
        private val settingsPrefs: SharedPreferences
) : BaseViewModel(), SharedPreferences.OnSharedPreferenceChangeListener {

    val weatherData: MutableLiveData<List<Weather>> = MutableLiveData()
    val cityCount: MutableLiveData<Int> = MutableLiveData()
    val displayUI: MutableLiveData<Boolean> = MutableLiveData()
    val loadingState: MutableLiveData<Boolean> = MutableLiveData()
    val displayError: MutableLiveData<Boolean> = MutableLiveData()
    val workManager = WorkManager.getInstance()

    init {
        displayUI.value = false // Hide UI until recyclerView has been loaded with data
        displayError.value = false
        settingsPrefs.registerOnSharedPreferenceChangeListener(this)
        updateUI()
    }

    fun resetDisplayError() {
        displayError.value = false
    }

    fun addCity(apiLocation: Location? = null) {
        launch {
            loadingState.value = true // Start refreshing
            try {
                withContext(Dispatchers.IO) { addCity.invoke(apiLocation) }
            } catch (e: IOException) {
                displayError.value = true
            }
            updateUI()
        }
    }

    fun updateWeather(locationType: LocationType) {
        launch {
            loadingState.value = true // Start refreshing
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
            cityCount.value = getCityCount.invoke() // Update viewPager only after updating weatherData (if not null)
            loadingState.value = false // Stop refreshing
        }
    }

    private fun startNotificationsWork() = workManager.beginUniqueWork(NOTIFICATIONS_WORKER,
            ExistingWorkPolicy.KEEP,
            OneTimeWorkRequestBuilder<NotificationWorker>()
                    .setInitialDelay(5000L, TimeUnit.MILLISECONDS)
                    .setConstraints(Constraints.Builder()
                            .setRequiredNetworkType(NetworkType.CONNECTED)
                            .build())
                    .build())
            .enqueue()

    private fun stopNotificationsWork() = workManager.cancelUniqueWork(NOTIFICATIONS_WORKER)

    override fun onCleared() {
        super.onCleared()
        settingsPrefs.unregisterOnSharedPreferenceChangeListener(this) // Remove listener at destruction
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            SETTINGS_UNITS_KEY -> updateWeather(LocationType.DB) // Update weather and UI when units are changed by user
            SETTINGS_NOTIFICATIONS_KEY ->
                if (sharedPreferences!!.getBoolean(key, true)) startNotificationsWork()
                else stopNotificationsWork()
        }
    }
}