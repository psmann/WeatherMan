package one.mann.interactors.usecases

import one.mann.interactors.data.applyUnits
import one.mann.interactors.data.repositories.WeatherRepository
import one.mann.interactors.data.sources.framework.PreferencesDataSource
import javax.inject.Inject

/* Created by Psmann. */

class ChangeUnits @Inject constructor(
        private val weatherRepository: WeatherRepository,
        private val prefsData: PreferencesDataSource
) {

    suspend fun invoke() {
        val units = prefsData.getUnits()
        weatherRepository.update(weatherRepository.read().map { it.applyUnits(units, false) })
    }
}