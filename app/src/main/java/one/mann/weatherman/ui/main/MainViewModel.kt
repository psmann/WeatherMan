package one.mann.weatherman.ui.main

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import one.mann.domain.models.Errors.*
import one.mann.domain.models.ViewPagerUpdateType
import one.mann.domain.models.location.Location
import one.mann.domain.models.location.LocationResponse
import one.mann.domain.models.location.LocationResponse.*
import one.mann.domain.models.location.LocationType
import one.mann.domain.models.location.LocationType.DB
import one.mann.domain.models.location.LocationType.DEVICE
import one.mann.interactors.usecases.*
import one.mann.weatherman.framework.service.workers.NotificationWorker
import one.mann.weatherman.ui.common.base.BaseViewModel
import one.mann.weatherman.ui.common.util.*
import one.mann.weatherman.ui.main.MainUiModel.State.*
import java.util.concurrent.TimeUnit.HOURS
import java.util.concurrent.TimeUnit.MINUTES
import javax.inject.Inject

/* Created by Psmann. */

internal class MainViewModel @Inject constructor(
        private val addCity: AddCity,
        private val getAllWeather: GetAllWeather,
        private val removeCity: RemoveCity,
        private val updateWeather: UpdateWeather,
        private val getCitySearch: GetCitySearch,
        private val changeUnits: ChangeUnits,
        private val settingsPrefs: SharedPreferences,
        private val workManager: WorkManager
) : BaseViewModel(), SharedPreferences.OnSharedPreferenceChangeListener {

    private val _uiModel = MutableLiveData<MainUiModel>()
    private var searchJob: Job? = null
    val uiModel: LiveData<MainUiModel>
        get() = _uiModel

    init {
        _uiModel.value = MainUiModel()
        settingsPrefs.registerOnSharedPreferenceChangeListener(this)
        workManager.getWorkInfosByTagLiveData(NOTIFICATION_WORKER_TAG).observeForever { updateUI() } // Update on change
        enqueueNotificationWork()
        updateUI(ViewPagerUpdateType.SET_SIZE)
        errorMessage = { error -> // Show error and change the state back to idle
            _uiModel.value = _uiModel.value?.copy(viewState = Error(NoResponse(error)))
            _uiModel.value = _uiModel.value?.copy(viewState = Idle)
        }
    }

    fun handleRefreshing(response: LocationResponse, firstRun: Boolean) {
        when (response) {
            NO_NETWORK -> _uiModel.value = _uiModel.value?.copy(viewState = Error(NoInternet))
            ENABLED -> if (firstRun) addCity() else updateWeather(DEVICE)
            DISABLED -> if (firstRun) _uiModel.value = _uiModel.value?.copy(viewState = Error(NoGps)) else updateWeather(DB)
            UNAVAILABLE -> _uiModel.value = _uiModel.value?.copy(viewState = Error(NoLocation))
        }
        _uiModel.value = _uiModel.value?.copy(viewState = Idle) // Change state back to idle
    }

    fun searchCity(query: String) {
        searchJob?.cancel() // Cancel previous job if any
        searchJob = launch {
            delay(750) // Debounce
            val citySearch = withContext(IO) { getCitySearch.invoke(query) }
            if (citySearch.isNotEmpty()) _uiModel.value = uiModel.value?.copy(citySearchResult = citySearch)
        }
    }

    fun addCity(apiLocation: Location? = null) {
        launch {
            _uiModel.value = _uiModel.value?.copy(citySearchResult = listOf(), viewState = Refreshing)
            withContext(IO) { addCity.invoke(apiLocation) }
            updateUI(ViewPagerUpdateType.ADD_ITEM)
        }
    }

    /**
     * Invoke updateWeather usecase, if it returns true then data has been updated from the API.
     * LAST_UPDATED_KEY is given currentTimeMillis() and updateUI() is called from onSharedPreferenceChanged().
     * This is done because weather can be updated from both MainActivity and DetailActivity.
     * If it returns false then LAST_CHECKED_KEY is changed and updateUI() is called from onSharedPreferenceChanged().
     */
    private fun updateWeather(locationType: LocationType) {
        launch {
            _uiModel.value = _uiModel.value?.copy(viewState = Refreshing)
            withContext(IO) {
                val weatherUpdated = updateWeather.invoke(locationType)
                if (weatherUpdated) settingsPrefs.edit { putLong(LAST_UPDATED_KEY, System.currentTimeMillis()) }
                else settingsPrefs.edit { putLong(LAST_CHECKED_KEY, System.currentTimeMillis()) }
            }
        }
    }

    fun removeCity(position: Int) {
        val cityName = _uiModel.value?.weatherData?.get(position)?.cityName ?: return // Return if null
        launch {
            withContext(IO) { removeCity.invoke(cityName) }
            updateUI(ViewPagerUpdateType.REMOVE_ITEM)
        }
    }

    private fun updateUI(updateViewPager: ViewPagerUpdateType = ViewPagerUpdateType.NO_CHANGE) {
        launch {
            val data = withContext(IO) { getAllWeather.invoke() }
            _uiModel.value = _uiModel.value?.copy(
                    weatherData = if (data.isEmpty()) listOf() else data,
                    cityCount = data.size,
                    viewState = UpdateViewPager(updateViewPager)
            )
            _uiModel.value = _uiModel.value?.copy(viewState = Idle) // Change state back to idle
        }
    }

    /** Start worker if notifications are turned on by the user, does nothing if worker is already running */
    private fun enqueueNotificationWork() {
        launch(IO) {
            if (settingsPrefs.getBoolean(SETTINGS_NOTIFICATIONS_KEY, true)) {
                startNotificationWork(settingsPrefs.getString(SETTINGS_FREQUENCY_KEY, "1")!!.toLong())
            }
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
                SETTINGS_NOTIFICATIONS_KEY -> if (sharedPreferences!!.getBoolean(key, true)) { // Start-Stop notifications
                    startNotificationWork(sharedPreferences.getString(SETTINGS_FREQUENCY_KEY, "1")!!.toLong())
                } else stopNotificationWork()
                SETTINGS_FREQUENCY_KEY -> { // Change notification frequency
                    stopNotificationWork() // Cancel old work
                    startNotificationWork(sharedPreferences!!.getString(key, "1")!!.toLong()) // Start new work
                }
                LAST_UPDATED_KEY, LAST_CHECKED_KEY -> updateUI() // Update UI when weather is updated from Main or Detail
            }
        }
    }
}