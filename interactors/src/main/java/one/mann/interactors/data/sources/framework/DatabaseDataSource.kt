package one.mann.interactors.data.sources.framework

import one.mann.domain.models.NotificationData
import one.mann.domain.models.weather.Weather

/* Created by Psmann. */

interface DatabaseDataSource {

    suspend fun insertCityAndWeather(weather: Weather)

    suspend fun getAllCitiesAndWeathers(): List<Weather>

    suspend fun getNotificationData(): NotificationData

    suspend fun updateLastChecked(lastChecked: Long)

    suspend fun updateAllWeathers(weathers: List<Weather>)

    suspend fun deleteCityAndWeather(cityId: String)
}