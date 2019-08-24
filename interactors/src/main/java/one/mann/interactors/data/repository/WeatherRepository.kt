package one.mann.interactors.data.repository

import one.mann.domain.model.Location
import one.mann.domain.model.LocationType
import one.mann.domain.model.NotificationData
import one.mann.domain.model.Weather
import one.mann.interactors.data.mapToWeather
import one.mann.interactors.data.sources.*
import one.mann.interactors.data.updateLastChecked
import javax.inject.Inject

class WeatherRepository @Inject constructor(
        private val weatherData: WeatherDataSource,
        private val timezoneData: TimezoneDataSource,
        private val deviceLocation: DeviceLocationSource,
        private val dbData: DatabaseDataSource,
        private val prefsData: PreferencesDataSource
) {

    companion object {
        private const val FIFTEEN_MINUTES = 900000
    }

    suspend fun dbSize(): Int = dbData.getDbSize()

    suspend fun create(apiLocation: Location? = null) {
        val location = apiLocation ?: deviceLocation.getLocation() // Use device GPS location if null
        dbData.insertWeather(mapToWeather(weatherData.getCurrentWeather(location), weatherData.getDailyForecast(location),
                weatherData.getHourlyForecast(location), timezoneData.getTimezone(location), location, prefsData.getUnits()))
    }

    suspend fun readNotificationData(): NotificationData = dbData.getNotificationData()

    suspend fun readAll(): List<Weather> = dbData.getAllWeather()

    /** Update weather from API if it has been more than 15 minutes since last update, else only update lastChecked value */
    suspend fun updateAll(locationType: LocationType): Boolean {
        val locations = dbData.getAllLocations().toMutableList()
        val timezones = timezoneData.getAllTimezone(locations)
        return if (syncFromServer()) {
            if (locationType == LocationType.DEVICE) locations[0] = deviceLocation.getLocation()
            val currentWeathers = weatherData.getAllCurrentWeather(locations)
            val dailyForecasts = weatherData.getAllDailyForecast(locations)
            val hourlyForecasts = weatherData.getAllHourlyForecast(locations)
            val weathers: MutableList<Weather> = mutableListOf()
            for (i in 0 until locations.size) weathers.add(mapToWeather(currentWeathers[i], dailyForecasts[i],
                    hourlyForecasts[i], timezones[i], locations[i], prefsData.getUnits()))
            dbData.updateAllWeather(weathers)
            true
        } else { // This still uses the Teleport API for getting timezones sadly
            dbData.updateAllWeather(readAll().mapIndexed { i, it -> it.updateLastChecked(timezones[i]) })
            false
        }
    }

    suspend fun delete(name: String) = dbData.deleteWeather(name)

    /** Checks if it has been more than 15 minutes since last update */
    private suspend fun syncFromServer(): Boolean = System.currentTimeMillis() - prefsData.getLastChecked() > FIFTEEN_MINUTES
}