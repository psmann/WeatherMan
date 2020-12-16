package one.mann.interactors.usecases

import one.mann.domain.models.location.Location
import one.mann.interactors.data.repositories.WeatherRepository
import javax.inject.Inject

/* Created by Psmann. */

class AddCity @Inject constructor(private val weatherRepository: WeatherRepository) {

    suspend fun invoke(apiLocation: Location? = null) = weatherRepository.createCity(apiLocation)
}