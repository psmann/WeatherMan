package one.mann.weatherman.ui.main

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.*
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import one.mann.domain.model.Errors.*
import one.mann.domain.model.Location
import one.mann.domain.model.LocationResponse
import one.mann.domain.model.LocationResponse.*
import one.mann.domain.model.LocationType
import one.mann.domain.model.LocationType.DB
import one.mann.domain.model.LocationType.DEVICE
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
        private val changeUnits: ChangeUnits,
        private val settingsPrefs: SharedPreferences,
        private val workManager: WorkManager
) : BaseViewModel(), SharedPreferences.OnSharedPreferenceChangeListener {

    private val _uiState = MutableLiveData<MainViewState>()
    val uiState: LiveData<MainViewState>
        get() = _uiState

    init {
        _uiState.value = MainViewState()
        settingsPrefs.registerOnSharedPreferenceChangeListener(this)
        workManager.getWorkInfosByTagLiveData(NOTIFICATION_WORKER_TAG).observeForever { updateUI() } // Update on change
        enqueueNotificationWork()
        updateUI()
    }

    fun handleRefreshing(response: LocationResponse, firstRun: Boolean) {
        when (response) {
            NO_NETWORK -> _uiState.value = _uiState.value!!.copy(error = NO_INTERNET)
            ENABLED -> if (firstRun) addCity() else updateWeather(DEVICE)
            DISABLED -> if (firstRun) _uiState.value = _uiState.value!!.copy(error = NO_GPS) else updateWeather(DB)
            UNAVAILABLE -> _uiState.value = _uiState.value!!.copy(error = NO_LOCATION)
        }
        _uiState.value = _uiState.value!!.copy(error = NO_ERROR) // Change error state back
    }

    fun addCity(apiLocation: Location? = null) {
        launch {
            try {
                _uiState.value = _uiState.value!!.copy(isRefreshing = true) // Start refreshing
                withContext(IO) { addCity.invoke(apiLocation) }
                updateUI()
            } catch (e: IOException) { // Stop refreshing, show error and change the state back
                _uiState.value = _uiState.value!!.copy(isRefreshing = false, error = NO_RESPONSE)
                _uiState.value = _uiState.value!!.copy(error = NO_ERROR)
            }
        }
    }

    /**
     * Invoke updateWeather usecase, if it returns true then data has been updated from the API.
     * LAST_UPDATED_KEY is given currentTimeMillis() and updateUI() is called from onSharedPreferenceChanged().
     * This is done because weather can be updated from both MainActivity and DetailActivity.
     * If it returns false (i.e. not updated from API) then LAST_CHECKED_KEY is changed and updateUI() is called in listener.
     */
    private fun updateWeather(locationType: LocationType) {
        launch {
            try {
                _uiState.value = _uiState.value!!.copy(isRefreshing = true) // Start refreshing
                withContext(IO) {
                    val weatherUpdated = updateWeather.invoke(locationType)
                    if (weatherUpdated) settingsPrefs.edit { putLong(LAST_UPDATED_KEY, System.currentTimeMillis()) }
                    else settingsPrefs.edit { putLong(LAST_CHECKED_KEY, System.currentTimeMillis()) }
                }
            } catch (e: IOException) { // Stop refreshing, show error and change the state back
                _uiState.value = _uiState.value!!.copy(isRefreshing = false, error = NO_RESPONSE)
                _uiState.value = _uiState.value!!.copy(error = NO_ERROR)
            }
        }
    }

    fun removeCity(position: Int) {
        val cityName = _uiState.value?.weatherData?.get(position)?.cityName ?: return // Return if null
        launch(IO) {
            removeCity.invoke(cityName)
            updateUI()
        }
    }

    private fun updateUI() {
        launch {
            val data = withContext(IO) { getAllWeather.invoke() }
            val count = withContext(IO) { getCityCount.invoke() }
            _uiState.value = if (data.isEmpty()) _uiState.value!!.copy(isRefreshing = false, cityCount = count)
            else _uiState.value!!.copy(weatherData = data, isLoading = false, isRefreshing = false, cityCount = count)
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
            when (key) { // All Settings are handled here, ideally this should be done in SettingsActivity
                SETTINGS_UNITS_KEY -> { // Change units and update UI
                    changeUnits.invoke()
                    updateUI()
                }
                SETTINGS_NOTIFICATIONS_KEY -> // Start-Stop notifications
                    if (sharedPreferences!!.getBoolean(key, true)) startNotificationWork(
                            sharedPreferences.getString(SETTINGS_FREQUENCY_KEY, "1")!!.toLong())
                    else stopNotificationWork()
                SETTINGS_FREQUENCY_KEY -> { // Change notification frequency
                    stopNotificationWork() // Cancel old work
                    startNotificationWork(sharedPreferences!!.getString(key, "1")!!.toLong()) // Start new work
                }
                LAST_UPDATED_KEY, LAST_CHECKED_KEY -> updateUI() // Update UI when weather is updated from Main or Detail
            }
        }
    }
}