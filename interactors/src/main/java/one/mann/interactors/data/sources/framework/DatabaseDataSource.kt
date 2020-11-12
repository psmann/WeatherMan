package one.mann.interactors.data.sources.framework

import one.mann.domain.models.location.Location
import one.mann.domain.models.NotificationData
import one.mann.domain.models.weather.Weather

/* Created by Psmann. */

interface DatabaseDataSource {

    suspend fun insertWeather(weather: Weather)

    suspend fun getDbSize(): Int

    suspend fun getNotificationData(): NotificationData

    suspend fun getAllWeather(): List<Weather>

    suspend fun getAllLocations(): List<Location>

    suspend fun updateAllWeather(weathers: List<Weather>)

    suspend fun deleteWeather(name: String)
}