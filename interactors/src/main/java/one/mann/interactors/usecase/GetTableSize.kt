package one.mann.interactors.usecase

import one.mann.interactors.data.repository.WeatherRepository

class GetCityCount(private val weatherRepository: WeatherRepository) {

    suspend fun invoke(): Int = weatherRepository.dbSize()
}