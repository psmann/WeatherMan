package one.mann.interactors.usecases

import one.mann.interactors.data.repository.WeatherRepository
import javax.inject.Inject

/* Created by Psmann. */

class GetCityCount @Inject constructor(private val weatherRepository: WeatherRepository) {

    suspend fun invoke(): Int = weatherRepository.dbSize()
}