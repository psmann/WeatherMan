package one.mann.interactors.data.repositories

import one.mann.domain.models.NotificationData
import one.mann.domain.models.location.Location
import one.mann.domain.models.location.LocationType
import one.mann.domain.models.weather.City
import one.mann.domain.models.weather.Weather
import one.mann.interactors.data.mapToDomainWeather
import one.mann.interactors.data.sources.api.TimezoneDataSource
import one.mann.interactors.data.sources.api.WeatherDataSource
import one.mann.interactors.data.sources.framework.DatabaseDataSource
import one.mann.interactors.data.sources.framework.DeviceLocationSource
import one.mann.interactors.data.sources.framework.PreferencesDataSource
import one.mann.interactors.data.updateLastChecked
import java.util.*
import javax.inject.Inject

/* Created by Psmann. */

class WeatherRepository @Inject constructor(
        private val weatherData: WeatherDataSource,
        private val timezoneData: TimezoneDataSource,
        private val deviceLocation: DeviceLocationSource,
        private val dbData: DatabaseDataSource,
        private val prefsData: PreferencesDataSource
) {

    companion object {
        private const val TIME_OUT = 1000000 // ~ 16.6 minutes (= 16.667 * 60 * 1000)
    }

    /** Insert new city in the database */
    suspend fun create(apiLocation: Location? = null) {
        val location = apiLocation ?: deviceLocation.getLocation() // Use device GPS location if null
        val timeCreated = System.currentTimeMillis()
        val currentWeather = weatherData.getCurrentWeather(location).copy(units = prefsData.getUnits())
        dbData.insertWeather(
                mapToDomainWeather(
                        City(
                                generateCityId(),
                                location.coordinates[0],
                                location.coordinates[1],
                                timezoneData.getTimezone(location),
                                timeCreated
                        ),
                        currentWeather,
                        weatherData.getDailyForecast(location),
                        weatherData.getHourlyForecast(location)
                )
        )
    }

    /** Returns a list of all Weather data from the database */
    suspend fun read(): List<Weather> = dbData.getAllWeather()

    /** Update all rows in the database with new data */
    suspend fun update(weatherData: List<Weather>) = dbData.updateAllWeather(weatherData)

    /** Remove a row from the database */
    suspend fun delete(cityId: String) = dbData.deleteWeather(cityId)

    /** Returns notification data */
    suspend fun readNotificationData(): NotificationData = dbData.getNotificationData()

    /** Update weather from server if syncFromServer() is true, otherwise only update lastChecked value */
    suspend fun updateAll(locationType: LocationType): Boolean {
        val citiesForUpdate = mutableListOf<City>()
        val citiesFromDb = dbData.getAllCities()

        if (locationType == LocationType.DEVICE) {
            val gpsLocation = deviceLocation.getLocation()
            citiesFromDb.mapIndexed { i, dbCity ->
                // Update coordinates for user location
                if (i == 0) {
                    citiesForUpdate.add(
                            dbCity.copy(
                                    coordinatesLat = gpsLocation.coordinates[0],
                                    coordinatesLong = gpsLocation.coordinates[1],
                            )
                    )
                } else citiesForUpdate.add(dbCity)
            }
        } else citiesFromDb.map { citiesForUpdate.add(it) }

        return if (syncFromServer()) {
            val weathers = mutableListOf<Weather>()
            citiesForUpdate.forEach {
                val location = Location(listOf(it.coordinatesLat, it.coordinatesLong))
                val currentWeather = weatherData.getCurrentWeather(location).copy(units = prefsData.getUnits())
                weathers.add(
                        mapToDomainWeather(
                                it,
                                currentWeather,
                                weatherData.getDailyForecast(location),
                                weatherData.getHourlyForecast(location)
                        )
                )
            }
            update(weathers)
            true
        } else {
            update(read().map { it.updateLastChecked() })
            false
        }
    }

    /** Creates a new unique code for cityId */
    private fun generateCityId(): String = UUID.randomUUID().toString()

    /** Returns true if it has been longer than time-out period since last update */
    private suspend fun syncFromServer(): Boolean = System.currentTimeMillis() - prefsData.getLastChecked() > TIME_OUT
}