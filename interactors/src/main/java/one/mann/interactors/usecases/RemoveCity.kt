package one.mann.interactors.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import one.mann.interactors.data.repositories.WeatherRepository
import javax.inject.Inject

/* Created by Psmann. */

class RemoveCity @Inject constructor(private val weatherRepository: WeatherRepository) {

    suspend fun invoke(cityId: String) = withContext(Dispatchers.IO) { weatherRepository.deleteCity(cityId) }
}