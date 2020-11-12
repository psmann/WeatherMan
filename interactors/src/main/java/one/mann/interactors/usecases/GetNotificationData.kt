package one.mann.interactors.usecases

import one.mann.domain.models.NotificationData
import one.mann.interactors.data.repositories.WeatherRepository
import javax.inject.Inject

/* Created by Psmann. */

class GetNotificationData @Inject constructor(private val weatherRepository: WeatherRepository) {

    suspend fun invoke(): NotificationData = weatherRepository.readNotificationData()
}