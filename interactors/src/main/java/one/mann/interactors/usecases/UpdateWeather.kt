package one.mann.interactors.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import one.mann.domain.models.location.LocationType
import one.mann.interactors.data.repositories.WeatherRepository
import javax.inject.Inject

/* Created by Psmann. */

class UpdateWeather @Inject constructor(private val weatherRepository: WeatherRepository) {

    suspend fun invoke(locationType: LocationType): Boolean = withContext(Dispatchers.IO) {
        weatherRepository.updateAllData(locationType)
    }
}