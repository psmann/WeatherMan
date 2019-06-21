package one.mann.interactors.usecase

import one.mann.interactors.data.repository.WeatherRepository

class RemoveCity(private val weatherRepository: WeatherRepository) {

    suspend fun invoke(name: String) = weatherRepository.delete(name)
}