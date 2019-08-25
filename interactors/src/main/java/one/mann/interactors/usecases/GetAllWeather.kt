package one.mann.interactors.usecases

import one.mann.domain.model.Weather
import one.mann.interactors.data.repository.WeatherRepository
import javax.inject.Inject

class GetAllWeather @Inject constructor(private val weatherRepository: WeatherRepository) {

    suspend fun invoke(): List<Weather> = weatherRepository.read()
}