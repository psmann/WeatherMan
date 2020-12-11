package one.mann.weatherman.framework.data.database

import one.mann.domain.models.NotificationData
import one.mann.domain.models.location.Location
import one.mann.domain.models.weather.Weather
import one.mann.interactors.data.sources.framework.DatabaseDataSource
import javax.inject.Inject

/* Created by Psmann. */

internal class WeatherDbDataSource @Inject constructor(db: WeatherDb) : DatabaseDataSource {

    private val dao = db.weatherDao()

    override suspend fun insertWeather(weather: Weather) = dao.insert(weather.mapToDb())

    override suspend fun getNotificationData(): NotificationData = dao.fetchNotificationData().mapToDomain()

    override suspend fun getAllWeather(): List<Weather> = dao.fetchAll().map { it.mapToDomain() }

    override suspend fun getAllLocations(): List<Location> = dao.fetchLocations().map { it.mapToDomain() }

    override suspend fun updateAllWeather(weathers: List<Weather>) = dao.updateAll(weathers.map { it.mapToDb() })

    override suspend fun deleteWeather(cityId: String) = dao.delete(cityId)
}