package one.mann.interactors.data.repository

import one.mann.domain.model.location.Location
import one.mann.domain.model.location.LocationType
import one.mann.domain.model.NotificationData
import one.mann.domain.model.weather.Weather
import one.mann.interactors.data.mapToWeather
import one.mann.interactors.data.sources.*
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

    /** Returns number of rows in Db (i.e. number of cities) */
    suspend fun dbSize(): Int = dbData.getDbSize()

    /** Insert new city in Db */
    suspend fun create(apiLocation: Location? = null) {
        val location = apiLocation ?: deviceLocation.getLocation() // Use device GPS location if null
        dbData.insertWeather(mapToWeather(weatherData.getCurrentWeather(location), weatherData.getDailyForecast(location),
                weatherData.getHourlyForecast(location), timezoneData.getTimezone(location), location, prefsData.getUnits()))
    }

    /** Returns a list of all Weather data in Db */
    suspend fun read(): List<Weather> = dbData.getAllWeather()

    /** Update all rows in Db with new data */
    suspend fun update(weatherData: List<Weather>) = dbData.updateAllWeather(weatherData)

    /** Remove a row from Db */
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
            for (i in 0 until locations.size) weathers.add(mapToWeather(currentWeathers[i], dailyForecasts[i],
                    hourlyForecasts[i], timezones[i], locations[i], units))
            update(weathers)
            true
        } else { // This still uses the Teleport API for getting timezones sadly
            update(read().mapIndexed { i, it -> it.updateLastChecked(timezones[i]) })
            false
        }
    }

    /** Returns true if it has been longer than time-out period since last update */
    private suspend fun syncFromServer(): Boolean = System.currentTimeMillis() - prefsData.getLastChecked() > TIME_OUT
}