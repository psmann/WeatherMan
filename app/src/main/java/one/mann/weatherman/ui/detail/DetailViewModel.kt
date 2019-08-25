package one.mann.weatherman.ui.detail

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import one.mann.domain.model.LocationType
import one.mann.domain.model.Weather
import one.mann.interactors.usecases.GetAllWeather
import one.mann.interactors.usecases.UpdateWeather
import one.mann.weatherman.ui.common.base.BaseViewModel
import one.mann.weatherman.ui.common.util.LAST_CHECKED_KEY
import one.mann.weatherman.ui.common.util.LAST_UPDATED_KEY
import java.io.IOException
import javax.inject.Inject

internal class DetailViewModel @Inject constructor(
        private val getAllWeather: GetAllWeather,
        private val updateWeather: UpdateWeather,
        private val settingsPrefs: SharedPreferences
) : BaseViewModel(), SharedPreferences.OnSharedPreferenceChangeListener {

    private val _uiState = MutableLiveData<ViewState>()
    val uiState: LiveData<ViewState>
        get() = _uiState

    init {
        _uiState.value = ViewState()
        updateUI()
        settingsPrefs.registerOnSharedPreferenceChangeListener(this)
    }

    data class ViewState(
            val isLoading: Boolean = false,
            val showError: Boolean = false,
            val weatherData: List<Weather> = listOf()
    )

    /**
     * Invoke updateWeather usecase, if it returns true then data has been updated from the API.
     * LAST_UPDATED_KEY is given currentTimeMillis() and updateUI() is called from onSharedPreferenceChanged().
     * This is done because weather can be updated from both MainActivity and DetailActivity.
     * If it returns false (i.e. not updated from API) then LAST_CHECKED_KEY is changed and updateUI() is called in listener.
     */
    fun updateWeather(locationType: LocationType) {
        launch {
            _uiState.value = _uiState.value!!.copy(isLoading = true) // Start refreshing
            try {
                withContext(IO) {
                    val weatherUpdated = updateWeather.invoke(locationType)
                    if (weatherUpdated) settingsPrefs.edit { putLong(LAST_UPDATED_KEY, System.currentTimeMillis()) }
                    else settingsPrefs.edit { putLong(LAST_CHECKED_KEY, System.currentTimeMillis()) }
                }
            } catch (e: IOException) { // Stop refreshing, show error and change the state back
                _uiState.value = _uiState.value!!.copy(isLoading = false, showError = true)
                _uiState.value = _uiState.value!!.copy(showError = false)
            }
        }
    }

    private fun updateUI() {
        launch {
            val data = withContext(IO) { getAllWeather.invoke() }
            if (data.isNotEmpty()) _uiState.value = _uiState.value!!.copy(weatherData = data)
            _uiState.value = _uiState.value!!.copy(isLoading = false) // Stop refreshing
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