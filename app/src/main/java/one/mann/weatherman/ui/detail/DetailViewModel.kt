package one.mann.weatherman.ui.detail

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import one.mann.domain.models.ErrorType.NoInternet
import one.mann.domain.models.ErrorType.NoResponse
import one.mann.domain.models.location.LocationServicesResponse
import one.mann.domain.models.location.LocationServicesResponse.*
import one.mann.domain.models.location.LocationType
import one.mann.domain.models.location.LocationType.DB
import one.mann.domain.models.location.LocationType.DEVICE
import one.mann.interactors.usecases.GetAllWeather
import one.mann.interactors.usecases.UpdateWeather
import one.mann.weatherman.ui.common.base.BaseViewModel
import one.mann.weatherman.common.LAST_CHECKED_KEY
import one.mann.weatherman.common.LAST_UPDATED_KEY
import one.mann.weatherman.ui.common.util.mapToUiWeather
import one.mann.weatherman.ui.detail.DetailUiModel.State.*
import javax.inject.Inject

/* Created by Psmann. */

internal class DetailViewModel @Inject constructor(
    private val getAllWeather: GetAllWeather,
    private val updateWeather: UpdateWeather,
    private val settingsPrefs: SharedPreferences
) : BaseViewModel(), SharedPreferences.OnSharedPreferenceChangeListener {

    private val _uiModel = MutableLiveData<DetailUiModel>()
    val uiModel: LiveData<DetailUiModel>
        get() = _uiModel

    init {
        _uiModel.value = DetailUiModel()
        updateUI()
        settingsPrefs.registerOnSharedPreferenceChangeListener(this)
        exceptionResponse = { error ->
            // Show error and change the state back to idle
            _uiModel.value = _uiModel.value?.copy(viewState = ShowError(NoResponse(error)))
            _uiModel.value = _uiModel.value?.copy(viewState = Idle)
        }
    }

    fun handleRefreshing(response: LocationServicesResponse) = when (response) {
        NO_NETWORK -> {
            _uiModel.value = _uiModel.value?.copy(viewState = ShowError(NoInternet))
            // Change state back to idle
            _uiModel.value = _uiModel.value?.copy(viewState = Idle)
        }
        ENABLED -> updateWeather(DEVICE)
        DISABLED -> updateWeather(DB)
        else -> run { return@run }
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

    private fun updateUI() {
        launch {
            val data = withContext(IO) { getAllWeather.invoke().map { it.mapToUiWeather() } }

            _uiModel.value = _uiModel.value?.copy(
                weatherData = if (data.isEmpty()) listOf() else data,
                viewState = Idle
            )
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