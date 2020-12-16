package one.mann.interactors.usecases

import one.mann.interactors.data.repositories.WeatherRepository
import javax.inject.Inject

/* Created by Psmann. */

class RemoveCity @Inject constructor(private val weatherRepository: WeatherRepository) {

    suspend fun invoke(cityId: String) = weatherRepository.deleteCity(cityId)
}