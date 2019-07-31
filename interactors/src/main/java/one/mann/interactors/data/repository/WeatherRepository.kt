package one.mann.interactors.data.repository

import one.mann.domain.model.Location
import one.mann.domain.model.LocationType
import one.mann.domain.model.NotificationData
import one.mann.domain.model.Weather
import one.mann.interactors.data.mapToWeather
import one.mann.interactors.data.sources.*
import javax.inject.Inject

class WeatherRepository @Inject constructor(
        private val weatherData: WeatherDataSource,
        private val timezoneData: TimezoneDataSource,
        private val deviceLocation: DeviceLocationSource,
        private val dbData: DatabaseDataSource,
        private val prefsData: PreferencesDataSource
) {

    suspend fun dbSize(): Int = dbData.getDbSize()

    suspend fun create(apiLocation: Location? = null) {
        val location = apiLocation ?: deviceLocation.getLocation() // Use value from db if apiLocation is null
        dbData.insertWeather(mapToWeather(weatherData.getCurrentWeather(location), weatherData.getDailyForecast(location),
                weatherData.getHourlyForecast(location), timezoneData.getTimezone(location), location, prefsData.getUnits()))
    }

    suspend fun readNotificationData(): NotificationData = dbData.getNotificationData()

    suspend fun readAll(): List<Weather> = dbData.getAllWeather()

    suspend fun updateAll(locationType: LocationType) {
        val locations = dbData.getAllLocations().toMutableList()
        if (locationType == LocationType.DEVICE) locations[0] = deviceLocation.getLocation()
        val currentWeathers = weatherData.getAllCurrentWeather(locations)
        val dailyForecasts = weatherData.getAllDailyForecast(locations)
        val hourlyForecasts = weatherData.getAllHourlyForecast(locations)
        val timezones = timezoneData.getAllTimezone(locations)
        val weathers: MutableList<Weather> = mutableListOf()
        for (i in 0 until locations.size) weathers.add(mapToWeather(currentWeathers[i], dailyForecasts[i],
                hourlyForecasts[i], timezones[i], locations[i], prefsData.getUnits()))
        dbData.updateAllWeather(weathers)
    }

    suspend fun delete(name: String) = dbData.deleteWeather(name)
}