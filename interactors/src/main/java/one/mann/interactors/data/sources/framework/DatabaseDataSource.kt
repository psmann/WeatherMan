package one.mann.interactors.data.sources.framework

import one.mann.domain.models.NotificationData
import one.mann.domain.models.location.Location
import one.mann.domain.models.weather.City
import one.mann.domain.models.weather.Weather

/* Created by Psmann. */

interface DatabaseDataSource {

    suspend fun insertWeather(weather: Weather)

    suspend fun getAllCities(): List<City>

    suspend fun getAllWeather(): List<Weather>

    suspend fun getNotificationData(): NotificationData

    suspend fun updateUserCity(location: Location)

    suspend fun updateLastChecked(lastChecked: Long)

    suspend fun updateAllWeather(weathers: List<Weather>)

    suspend fun deleteCity(cityId: String)
}