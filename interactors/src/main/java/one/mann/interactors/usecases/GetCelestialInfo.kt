package one.mann.interactors.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import one.mann.domain.models.CelestialInfo
import one.mann.interactors.data.repositories.CelestialRepository
import javax.inject.Inject

/* Created by Psmann. */

class GetCelestialInfo @Inject constructor(private val celestialRepository: CelestialRepository) {

    suspend fun invoke(
        coordinatesLat: Float,
        coordinatesLong: Float,
        timezone: String,
        lastChecked: Long
    ): CelestialInfo = withContext(Dispatchers.IO) {
        celestialRepository.compute(
            coordinatesLat.toDouble(),
            coordinatesLong.toDouble(),
            timezone,
            lastChecked
        )
    }
}
