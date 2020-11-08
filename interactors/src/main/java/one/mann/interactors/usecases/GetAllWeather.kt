package one.mann.interactors.usecases

import one.mann.domain.model.weather.Weather
import one.mann.interactors.data.repository.WeatherRepository
import javax.inject.Inject

/* Created by Psmann. */

class GetAllWeather @Inject constructor(private val weatherRepository: WeatherRepository) {

    suspend fun invoke(): List<Weather> = weatherRepository.read()
}