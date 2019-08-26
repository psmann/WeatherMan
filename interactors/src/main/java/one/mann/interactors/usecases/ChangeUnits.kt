package one.mann.interactors.usecases

import one.mann.interactors.data.changeUnits
import one.mann.interactors.data.repository.WeatherRepository
import one.mann.interactors.data.sources.PreferencesDataSource
import javax.inject.Inject

class ChangeUnits @Inject constructor(
        private val weatherRepository: WeatherRepository,
        private val prefsData: PreferencesDataSource
) {

    suspend fun invoke() {
        val units = prefsData.getUnits()
        weatherRepository.update(weatherRepository.read().map { it.changeUnits(units) })
    }
}