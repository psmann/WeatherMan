package one.mann.interactors.data.sources

import one.mann.domain.model.Location
import one.mann.domain.model.NotificationData
import one.mann.domain.model.Weather

interface DatabaseDataSource {

    suspend fun insertWeather(weather: Weather)

    suspend fun getDbSize(): Int

    suspend fun getNotificationData(): NotificationData

    suspend fun getAllWeather(): List<Weather>

    suspend fun getAllLocations(): MutableList<Location>

    suspend fun updateAllWeather(weathers: List<Weather>)

    suspend fun deleteWeather(name: String)
}