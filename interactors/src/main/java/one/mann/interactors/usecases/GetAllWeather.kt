package one.mann.interactors.usecases

import one.mann.domain.models.weather.Weather
import one.mann.interactors.data.repositories.WeatherRepository
import javax.inject.Inject

/* Created by Psmann. */

class GetAllWeather @Inject constructor(private val weatherRepository: WeatherRepository) {

    suspend fun invoke(): List<Weather> = weatherRepository.read()
}