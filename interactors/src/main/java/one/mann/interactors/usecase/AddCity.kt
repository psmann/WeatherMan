package one.mann.interactors.usecase

import one.mann.domain.model.LocationType
import one.mann.interactors.data.repository.WeatherRepository

class AddCity(private val weatherRepository: WeatherRepository) {

    suspend fun invoke(locationType: LocationType? = null) = weatherRepository.saveNew(locationType)
}