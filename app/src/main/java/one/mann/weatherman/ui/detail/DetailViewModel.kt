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
import one.mann.weatherman.ui.common.util.DETAIL_REFRESH_KEY
import one.mann.weatherman.ui.common.util.MAIN_REFRESH_KEY
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

    fun updateWeather(locationType: LocationType) {
        launch {
            _uiState.value = _uiState.value!!.copy(isLoading = true) // Start refreshing
            try {
                withContext(IO) {
                    updateWeather.invoke(locationType)
                    settingsPrefs.edit { putLong(DETAIL_REFRESH_KEY, System.currentTimeMillis()) }
                }
            } catch (e: IOException) {
                _uiState.value = _uiState.value!!.copy(showError = true)
            }
            updateUI()
        }
    }

    private fun updateUI() {
        launch {
            val data = withContext(IO) { getAllWeather.invoke() }
            if (data.isNotEmpty()) _uiState.value = _uiState.value!!.copy(weatherData = data, showError = false)
            _uiState.value = _uiState.value!!.copy(isLoading = false, showError = false) // Stop refreshing
        }
    }

    /** Remove listener at destruction */
    override fun onCleared() {
        super.onCleared()
        settingsPrefs.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        launch(IO) { if (key == MAIN_REFRESH_KEY) updateUI() }  // Update DetailActivity when data is refreshed from fragment
    }
}