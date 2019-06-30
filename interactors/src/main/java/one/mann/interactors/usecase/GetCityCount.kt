package one.mann.interactors.usecase

import one.mann.interactors.data.repository.WeatherRepository
import javax.inject.Inject

class GetCityCount @Inject constructor(private val weatherRepository: WeatherRepository) {

    suspend fun invoke(): Int = weatherRepository.dbSize()
}