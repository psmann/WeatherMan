package one.mann.weatherman.ui.detail

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import one.mann.domain.model.Errors.*
import one.mann.domain.model.location.LocationResponse
import one.mann.domain.model.location.LocationResponse.*
import one.mann.domain.model.location.LocationType
import one.mann.domain.model.location.LocationType.DB
import one.mann.domain.model.location.LocationType.DEVICE
import one.mann.interactors.usecases.GetAllWeather
import one.mann.interactors.usecases.UpdateWeather
import one.mann.weatherman.ui.common.base.BaseViewModel
import one.mann.weatherman.ui.common.util.LAST_CHECKED_KEY
import one.mann.weatherman.ui.common.util.LAST_UPDATED_KEY
import java.io.IOException
import javax.inject.Inject

/* Created by Psmann. */

internal class DetailViewModel @Inject constructor(
        private val getAllWeather: GetAllWeather,
        private val updateWeather: UpdateWeather,
        private val settingsPrefs: SharedPreferences
) : BaseViewModel(), SharedPreferences.OnSharedPreferenceChangeListener {

    private val _uiState = MutableLiveData<DetailViewState>()
    val uiState: LiveData<DetailViewState>
        get() = _uiState

    init {
        _uiState.value = DetailViewState()
        updateUI()
        settingsPrefs.registerOnSharedPreferenceChangeListener(this)
    }

    fun handleRefreshing(response: LocationResponse) = when (response) {
        NO_NETWORK -> {
            _uiState.value = _uiState.value?.copy(isRefreshing = false, error = NO_INTERNET)
            _uiState.value = _uiState.value?.copy(error = NO_ERROR) // Change error state back
        }
        ENABLED -> updateWeather(DEVICE)
        DISABLED -> updateWeather(DB)
        else -> run { return@run } // Workaround for lack of break support inside when statements
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
                _uiState.value = _uiState.value?.copy(isRefreshing = true) // Start refreshing
                withContext(IO) {
                    val weatherUpdated = updateWeather.invoke(locationType)
                    if (weatherUpdated) settingsPrefs.edit { putLong(LAST_UPDATED_KEY, System.currentTimeMillis()) }
                    else settingsPrefs.edit { putLong(LAST_CHECKED_KEY, System.currentTimeMillis()) }
                }
            } catch (e: IOException) { // Stop refreshing, show error and change the state back
                _uiState.value = _uiState.value?.copy(isRefreshing = false, error = NO_RESPONSE)
                _uiState.value = _uiState.value?.copy(error = NO_ERROR)
            }
        }
    }

    private fun updateUI() {
        launch {
            val data = withContext(IO) { getAllWeather.invoke() }
            _uiState.value = if (data.isEmpty()) _uiState.value?.copy(isRefreshing = false)
            else _uiState.value?.copy(isRefreshing = false, weatherData = data)
        }
    }

    /** Remove listener at destruction */
    override fun onCleared() {
        super.onCleared()
        settingsPrefs.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        launch(IO) { if (key == LAST_UPDATED_KEY || key == LAST_CHECKED_KEY) updateUI() }
    }
}