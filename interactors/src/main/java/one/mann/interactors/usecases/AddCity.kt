package one.mann.interactors.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import one.mann.domain.models.location.Location
import one.mann.interactors.data.repositories.WeatherRepository
import javax.inject.Inject

/* Created by Psmann. */

class AddCity @Inject constructor(private val weatherRepository: WeatherRepository) {

    suspend fun invoke(apiLocation: Location? = null) = withContext(Dispatchers.IO) {
        weatherRepository.createCity(apiLocation)
    }
}