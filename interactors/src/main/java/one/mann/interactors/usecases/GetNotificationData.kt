package one.mann.interactors.usecases

import one.mann.domain.model.NotificationData
import one.mann.interactors.data.repository.WeatherRepository
import javax.inject.Inject

class GetNotificationData @Inject constructor(private val weatherRepository: WeatherRepository) {

    suspend fun invoke(): NotificationData = weatherRepository.readNotificationData()
}