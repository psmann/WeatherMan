package one.mann.interactors.usecases

import one.mann.domain.models.location.LocationType
import one.mann.interactors.data.repositories.WeatherRepository
import javax.inject.Inject

/* Created by Psmann. */

class UpdateWeather @Inject constructor(private val weatherRepository: WeatherRepository) {

    suspend fun invoke(locationType: LocationType): Boolean = weatherRepository.updateAllData(locationType)
}