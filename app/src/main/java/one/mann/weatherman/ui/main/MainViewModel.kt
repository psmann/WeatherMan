package one.mann.weatherman.ui.main

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import one.mann.domain.logic.coordinatesInString
import one.mann.domain.models.ErrorType.*
import one.mann.domain.models.ViewPagerUpdateType
import one.mann.domain.models.location.Location
import one.mann.domain.models.location.LocationServicesResponse
import one.mann.domain.models.location.LocationServicesResponse.*
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
    val uiModel: LiveData<MainUiModel> get() = _uiModel

    init {
        _uiModel.value = MainUiModel()
        settingsPrefs.registerOnSharedPreferenceChangeListener(this)
        // Update on change
        workManager.getWorkInfosByTagLiveData(NOTIFICATION_WORKER_TAG).observeForever { updateUI() }
        enqueueNotificationWork()
        updateUI(ViewPagerUpdateType.SET_SIZE)
        exceptionResponse = { error ->
            // Show error and change the state back to idle
            _uiModel.value = _uiModel.value?.copy(viewState = ShowError(NoResponse(error)))
            _uiModel.value = _uiModel.value?.copy(viewState = Idle)
        }
    }

    fun handleRefreshing(response: LocationServicesResponse, firstRun: Boolean) {
        when (response) {
            NO_NETWORK -> _uiModel.value = _uiModel.value?.copy(viewState = ShowError(NoInternet))
            ENABLED -> if (firstRun) addCity() else updateWeather(DEVICE)
            DISABLED -> if (firstRun) {
                _uiModel.value = _uiModel.value?.copy(viewState = ShowError(NoGps))
            } else updateWeather(DB)
            UNAVAILABLE -> _uiModel.value = _uiModel.value?.copy(viewState = ShowError(NoLocation))
        }
        // Change state back to idle
        _uiModel.value = _uiModel.value?.copy(viewState = Idle)
    }

    fun searchCity(query: String) {
        // Cancel previous job if any
        searchJob?.cancel()
        // Reset list after every user input
        _uiModel.value = uiModel.value?.copy(citySearchResult = listOf())
        // Return if query is less than 3 characters long
        if (query == "" || query.length < 3) return
        searchJob = launch {
            delay(750) // Debounce
            val citySearch = getCitySearch.invoke(query)
            if (citySearch.isNotEmpty()) _uiModel.value = uiModel.value?.copy(citySearchResult = citySearch)
        }
    }

    fun addCity(apiLocation: Location? = null) {
        launch {
            apiLocation?.let {
                val coordinatesInString = listOf(it.coordinates[0], it.coordinates[1]).coordinatesInString()
                val weatherData = _uiModel.value?.weatherData
                // Check if the city already exists in the list
                weatherData?.forEach { weather ->
                    if (weather.city.coordinates == coordinatesInString) {
                        _uiModel.value = _uiModel.value?.copy(viewState = ShowError(CityAlreadyExists))
                        _uiModel.value = _uiModel.value?.copy(viewState = Idle)
                        return@launch
                    }
                }
            }
            _uiModel.value = _uiModel.value?.copy(citySearchResult = listOf(), viewState = Refreshing)
            addCity.invoke(apiLocation)
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
            val weatherUpdated = updateWeather.invoke(locationType)
            withContext(IO) {
                if (weatherUpdated) settingsPrefs.edit { putLong(LAST_UPDATED_KEY, System.currentTimeMillis()) }
                else settingsPrefs.edit { putLong(LAST_CHECKED_KEY, System.currentTimeMillis()) }
            }
        }
    }

    fun removeCity(position: Int) {
        val cityId = _uiModel.value?.weatherData?.get(position)?.city?.cityId ?: return // Return if null
        launch {
            removeCity.invoke(cityId)
            updateUI(ViewPagerUpdateType.REMOVE_ITEM)
        }
    }

    private fun updateUI(updateViewPager: ViewPagerUpdateType = ViewPagerUpdateType.NO_CHANGE) {
        launch {
            val data = withContext(IO) { getAllWeather.invoke().map { it.mapToUiWeather() } }
            _uiModel.value = _uiModel.value?.copy(
                weatherData = if (data.isEmpty()) listOf() else data,
                cityCount = data.size,
                viewState = UpdateViewPager(updateViewPager)
            )
            // Change state back to idle
            _uiModel.value = _uiModel.value?.copy(viewState = Idle)
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
        workManager.enqueueUniquePeriodicWork(
            NOTIFICATION_WORKER,
            ExistingPeriodicWorkPolicy.KEEP,
            PeriodicWorkRequestBuilder<NotificationWorker>(frequency, HOURS, 15, MINUTES)
                .addTag(NOTIFICATION_WORKER_TAG)
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .build()
        )
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
        searchJob?.cancel()
        settingsPrefs.unregisterOnSharedPreferenceChangeListener(this)
        workManager.getWorkInfosByTagLiveData(NOTIFICATION_WORKER_TAG).removeObserver { }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        launch(IO) {
            when (key) {
                // Change units and update UI
                SETTINGS_UNITS_KEY -> {
                    changeUnits.invoke()
                    updateUI()
                }
                // Start-Stop notifications
                SETTINGS_NOTIFICATIONS_KEY -> if (sharedPreferences!!.getBoolean(key, true)) {
                    startNotificationWork(sharedPreferences.getString(SETTINGS_FREQUENCY_KEY, "24")!!.toLong())
                } else stopNotificationWork()
                // Change notification frequency
                SETTINGS_FREQUENCY_KEY -> {
                    // Cancel old work
                    stopNotificationWork()
                    // Start new work
                    startNotificationWork(sharedPreferences!!.getString(key, "24")!!.toLong())
                }
                // Update UI when weather is updated from Main or Detail
                LAST_UPDATED_KEY, LAST_CHECKED_KEY -> updateUI()
            }
        }
    }
}