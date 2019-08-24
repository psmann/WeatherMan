package one.mann.weatherman.ui.main

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import androidx.work.*
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import one.mann.domain.model.Location
import one.mann.domain.model.LocationType
import one.mann.domain.model.Weather
import one.mann.interactors.usecases.*
import one.mann.weatherman.framework.service.workers.NotificationWorker
import one.mann.weatherman.ui.common.base.BaseViewModel
import one.mann.weatherman.ui.common.util.*
import java.io.IOException
import java.util.concurrent.TimeUnit.HOURS
import java.util.concurrent.TimeUnit.MINUTES
import javax.inject.Inject

internal class MainViewModel @Inject constructor(
        private val addCity: AddCity,
        private val getAllWeather: GetAllWeather,
        private val removeCity: RemoveCity,
        private val updateWeather: UpdateWeather,
        private val getCityCount: GetCityCount,
        private val settingsPrefs: SharedPreferences,
        private val workManager: WorkManager
) : BaseViewModel(), SharedPreferences.OnSharedPreferenceChangeListener {

    private var showUi = false
    val uiState = MutableLiveData<ViewState>()

    init {
        uiState.value = ViewState()
        settingsPrefs.registerOnSharedPreferenceChangeListener(this)
        workManager.getWorkInfosByTagLiveData(NOTIFICATION_WORKER_TAG).observeForever { updateUI() } // Update on change
        enqueueNotificationWork()
        updateUI()
    }

    data class ViewState(
            val isLoading: Boolean = false,
            val hideUi: Boolean = true,
            val showError: Boolean = false,
            val cityCount: Int = 0,
            val weatherData: List<Weather> = listOf()
    )

    fun addCity(apiLocation: Location? = null) {
        launch {
            uiState.value = uiState.value!!.copy(isLoading = true) // Start refreshing
            try {
                withContext(IO) { addCity.invoke(apiLocation) }
            } catch (e: IOException) {
                uiState.value = uiState.value!!.copy(showError = true)
            }
            updateUI()
        }
    }

    fun updateWeather(locationType: LocationType) {
        launch {
            uiState.value = uiState.value!!.copy(isLoading = true) // Start refreshing
            try {
                withContext(IO) {
                    updateWeather.invoke(locationType)
                    settingsPrefs.edit { putLong(MAIN_REFRESH_KEY, System.currentTimeMillis()) }
                }
            } catch (e: IOException) {
                uiState.value = uiState.value!!.copy(showError = true)
            }
            updateUI()
        }
    }

    fun removeCity(position: Int) {
        val cityName = uiState.value?.weatherData?.get(position)?.cityName ?: return // Return if null
        launch(IO) {
            removeCity.invoke(cityName)
            updateUI()
        }
    }

    private fun updateUI() {
        launch {
            val data = withContext(IO) { getAllWeather.invoke() }
            if (data.isNotEmpty()) {
                uiState.value = uiState.value!!.copy(weatherData = data) // Update all weather data
                if (!showUi) uiState.value = uiState.value!!.copy(hideUi = false) // Show UI if hidden
                showUi = true
            } // Stop refreshing and update viewPager only after updating weatherData (if not null)
            uiState.value = uiState.value!!.copy(isLoading = false, showError = false, cityCount = getCityCount.invoke())
        }
    }

    /** Start worker if notifications are turned on by the user, does nothing if worker is already running */
    private fun enqueueNotificationWork() {
        launch(IO) {
            if (settingsPrefs.getBoolean(SETTINGS_NOTIFICATIONS_KEY, true)) startNotificationWork(
                    settingsPrefs.getString(SETTINGS_FREQUENCY_KEY, "1")!!.toLong())
        }
    }

    private fun startNotificationWork(frequency: Long) {
        launch(Default) {
            workManager.enqueueUniquePeriodicWork(
                    NOTIFICATION_WORKER,
                    ExistingPeriodicWorkPolicy.KEEP,
                    PeriodicWorkRequestBuilder<NotificationWorker>(frequency, HOURS, 15, MINUTES)
                            .addTag(NOTIFICATION_WORKER_TAG)
                            .setConstraints(Constraints.Builder()
                                    .setRequiredNetworkType(NetworkType.CONNECTED)
                                    .build())
                            .build()
            )
        }
    }

    private fun stopNotificationWork() {
        launch(Default) { workManager.cancelUniqueWork(NOTIFICATION_WORKER) }
    }

    fun navigationGuideShown(): Boolean = settingsPrefs.getBoolean(NAVIGATION_GUIDE_KEY, false)

    fun setNavigationGuideShown() {
        launch(IO) { settingsPrefs.edit { putBoolean(NAVIGATION_GUIDE_KEY, true) } }
    }

    /** Remove listeners and observers at destruction */
    override fun onCleared() {
        super.onCleared()
        settingsPrefs.unregisterOnSharedPreferenceChangeListener(this)
        workManager.getWorkInfosByTagLiveData(NOTIFICATION_WORKER_TAG).removeObserver { }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        launch(IO) {
            when (key) {
                SETTINGS_UNITS_KEY -> updateWeather(LocationType.DB) // Update weather and UI when units are changed by user
                SETTINGS_NOTIFICATIONS_KEY -> // Start/Stop notifications
                    if (sharedPreferences!!.getBoolean(key, true)) startNotificationWork(
                            sharedPreferences.getString(SETTINGS_FREQUENCY_KEY, "1")!!.toLong())
                    else stopNotificationWork()
                SETTINGS_FREQUENCY_KEY -> { // Change notification frequency
                    stopNotificationWork() // Cancel old work
                    startNotificationWork(sharedPreferences!!.getString(key, "1")!!.toLong()) // Start new work
                }
                DETAIL_REFRESH_KEY -> updateUI() // Update MainFragment when data is refreshed from DetailActivity
            }
        }
    }
}