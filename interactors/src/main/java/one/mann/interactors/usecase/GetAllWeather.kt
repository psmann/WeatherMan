package one.mann.interactors.usecase

import one.mann.domain.model.Weather
import one.mann.interactors.data.repository.WeatherRepository

class GetAllWeather(private val weatherRepository: WeatherRepository) {

    suspend fun invoke(): List<Weather> = weatherRepository.readAll()
}