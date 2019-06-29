package one.mann.interactors.usecase

import one.mann.interactors.data.repository.WeatherRepository
import javax.inject.Inject

class RemoveCity @Inject constructor(private val weatherRepository: WeatherRepository) {

    suspend fun invoke(name: String) = weatherRepository.delete(name)
}