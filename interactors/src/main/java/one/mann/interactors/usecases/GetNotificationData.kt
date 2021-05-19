package one.mann.interactors.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import one.mann.domain.models.NotificationData
import one.mann.interactors.data.repositories.WeatherRepository
import javax.inject.Inject

/* Created by Psmann. */

class GetNotificationData @Inject constructor(private val weatherRepository: WeatherRepository) {

    suspend fun invoke(): NotificationData = withContext(Dispatchers.IO) { weatherRepository.readNotificationData() }
}