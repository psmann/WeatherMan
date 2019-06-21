package one.mann.interactors.usecase

import one.mann.domain.model.Location
import one.mann.interactors.data.repository.WeatherRepository

class AddCity(private val weatherRepository: WeatherRepository) {

    suspend fun invoke(apiLocation: Location? = null) = weatherRepository.saveNew(apiLocation)
}