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
import one.mann.weatherman.framework.service.workers.NotificationWorker
import one.mann.weatherman.ui.common.base.BaseViewModel
import one.mann.weatherman.ui.common.util.*
import java.io.IOException
import java.util.concurrent.TimeUnit
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
    val weatherData: MutableLiveData<List<Weather>> = MutableLiveData()
    val cityCount: MutableLiveData<Int> = MutableLiveData()
    val uiModel = MutableLiveData<UiModel>()

    init {
        uiModel.value = UiModel.DisplayUi(false)
        settingsPrefs.registerOnSharedPreferenceChangeListener(this)
        workManager.getWorkInfosByTagLiveData(NOTIFICATION_WORKER_TAG).observeForever { updateUI() } // Observe worker
        updateUI()
    }

    sealed class UiModel {
        data class Refreshing(val loading: Boolean) : UiModel()
        data class DisplayUi(val display: Boolean) : UiModel()
        object ShowError : UiModel()
    }

    fun addCity(apiLocation: Location? = null) {
        launch {
            uiModel.value = UiModel.Refreshing(true) // Start refreshing
            try {
                withContext(Dispatchers.IO) { addCity.invoke(apiLocation) }
            } catch (e: IOException) {
                uiModel.value = UiModel.ShowError
            }
            updateUI()
        }
    }

    fun updateWeather(locationType: LocationType) {
        launch {
            uiModel.value = UiModel.Refreshing(true) // Start refreshing
            try {
                withContext(Dispatchers.IO) { updateWeather.invoke(locationType) }
            } catch (e: IOException) {
                uiModel.value = UiModel.ShowError
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
                if (!showUi) uiModel.value = UiModel.DisplayUi(true) // Show UI if hidden
                showUi = true
            }
            uiModel.value = UiModel.Refreshing(false) // Stop refreshing
            cityCount.value = getCityCount.invoke() // Update viewPager only after updating weatherData (if not null)
        }
    }

    private fun startNotificationWork(frequency: Long) = workManager.enqueueUniquePeriodicWork(
            NOTIFICATION_WORKER,
            ExistingPeriodicWorkPolicy.KEEP,
            PeriodicWorkRequestBuilder<NotificationWorker>(frequency * 15, TimeUnit.MINUTES)//, 15, TimeUnit.MINUTES)
                    //.setInitialDelay(frequency, TimeUnit.HOURS) // Show first notification after the duration set
                    .addTag(NOTIFICATION_WORKER_TAG)
                    .setConstraints(Constraints.Builder()
                            .setRequiredNetworkType(NetworkType.CONNECTED)
                            .build())
                    .build()
    )

    private fun stopNotificationWork() = workManager.cancelUniqueWork(NOTIFICATION_WORKER)

    /** Remove listeners and observers at destruction */
    override fun onCleared() {
        super.onCleared()
        settingsPrefs.unregisterOnSharedPreferenceChangeListener(this)
        workManager.getWorkInfosByTagLiveData(NOTIFICATION_WORKER_TAG).removeObserver { }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            SETTINGS_UNITS_KEY -> updateWeather(LocationType.DB) // Update weather and UI when units are changed by user
            SETTINGS_NOTIFICATIONS_KEY ->
                if (sharedPreferences!!.getBoolean(key, true)) startNotificationWork(
                        sharedPreferences.getString(SETTINGS_FREQUENCY_KEY, "1")!!.toLong())
                else stopNotificationWork()
            SETTINGS_FREQUENCY_KEY -> {
                stopNotificationWork() // Cancel old work
                startNotificationWork(sharedPreferences!!.getString(key, "1")!!.toLong()) // Start new work
            }
        }
    }
}