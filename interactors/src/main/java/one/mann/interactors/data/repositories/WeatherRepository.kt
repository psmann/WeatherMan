package one.mann.interactors.data.repositories

import one.mann.domain.models.NotificationData
import one.mann.domain.models.location.Location
import one.mann.domain.models.location.LocationType
import one.mann.domain.models.weather.Weather
import one.mann.interactors.data.mapToWeather
import one.mann.interactors.data.sources.api.TimezoneDataSource
import one.mann.interactors.data.sources.api.WeatherDataSource
import one.mann.interactors.data.sources.framework.DatabaseDataSource
import one.mann.interactors.data.sources.framework.DeviceLocationSource
import one.mann.interactors.data.sources.framework.PreferencesDataSource
import one.mann.interactors.data.updateLastChecked
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
        dbData.insertWeather(
                mapToWeather(
                        weatherData.getCurrentWeather(location),
                        weatherData.getDailyForecast(location),
                        weatherData.getHourlyForecast(location),
                        timezoneData.getTimezone(location),
                        location,
                        prefsData.getUnits()
                )
        )
    }

    /** Returns a list of all Weather data from the database */
    suspend fun read(): List<Weather> = dbData.getAllWeather()

    /** Update all rows in the database with new data */
    suspend fun update(weatherData: List<Weather>) = dbData.updateAllWeather(weatherData)

    /** Remove a row from the database */
    suspend fun delete(name: String) = dbData.deleteWeather(name)

    /** Returns notification data */
    suspend fun readNotificationData(): NotificationData = dbData.getNotificationData()

    /** Update weather from server if syncFromServer() is true, else only update lastChecked value */
    suspend fun updateAll(locationType: LocationType): Boolean {
        val locations = dbData.getAllLocations().toMutableList()
        val timezones = timezoneData.getAllTimezone(locations)
        return if (syncFromServer()) {
            if (locationType == LocationType.DEVICE) locations[0] = deviceLocation.getLocation()
            val currentWeathers = weatherData.getAllCurrentWeather(locations)
            val dailyForecasts = weatherData.getAllDailyForecast(locations)
            val hourlyForecasts = weatherData.getAllHourlyForecast(locations)
            val weathers: MutableList<Weather> = mutableListOf()
            val units = prefsData.getUnits()
            for (i in 0 until locations.size) weathers.add(
                    mapToWeather(
                            currentWeathers[i],
                            dailyForecasts[i],
                            hourlyForecasts[i],
                            timezones[i],
                            locations[i],
                            units
                    )
            )
            update(weathers)
            true
        } else { // This still uses the Teleport API for getting timezones
            update(read().mapIndexed { i, it -> it.updateLastChecked(timezones[i]) })
            false
        }
    }

    /** Returns true if it has been longer than time-out period since last update */
    private suspend fun syncFromServer(): Boolean = System.currentTimeMillis() - prefsData.getLastChecked() > TIME_OUT
}