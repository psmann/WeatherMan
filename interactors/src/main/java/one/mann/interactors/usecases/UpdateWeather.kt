package one.mann.interactors.usecases

import one.mann.domain.model.LocationType
import one.mann.interactors.data.repository.WeatherRepository
import javax.inject.Inject

class UpdateWeather @Inject constructor(private val weatherRepository: WeatherRepository) {

    suspend fun invoke(locationType: LocationType): Boolean = weatherRepository.updateAll(locationType)
}