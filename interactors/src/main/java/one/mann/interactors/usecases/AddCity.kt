package one.mann.interactors.usecases

import one.mann.domain.model.location.Location
import one.mann.interactors.data.repository.WeatherRepository
import javax.inject.Inject

class AddCity @Inject constructor(private val weatherRepository: WeatherRepository) {

    suspend fun invoke(apiLocation: Location? = null) = weatherRepository.create(apiLocation)
}