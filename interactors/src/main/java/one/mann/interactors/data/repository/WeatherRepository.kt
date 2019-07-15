package one.mann.interactors.data.repository

import one.mann.domain.model.Location
import one.mann.domain.model.LocationType
import one.mann.domain.model.Weather
import one.mann.interactors.data.mapToWeather
import one.mann.interactors.data.source.*
import javax.inject.Inject

class WeatherRepository @Inject constructor(
        private val weatherData: WeatherDataSource,
        private val timezoneData: TimezoneDataSource,
        private val deviceLocation: DeviceLocationSource,
        private val dbData: DatabaseDataSource,
        private val preferencesData: PreferencesDataSource
) {

    suspend fun dbSize(): Int = dbData.getDbSize()

    suspend fun create(apiLocation: Location? = null) {
        val location = apiLocation ?: deviceLocation.getLocation()
        dbData.insertWeather(mapToWeather(weatherData.getCurrentWeather(location), weatherData.getDailyForecast(location),
                timezoneData.getTimezone(location), location, preferencesData.getUnits()))
    }

    suspend fun readAll(): List<Weather> = dbData.getAllWeather()

    suspend fun updateAll(locationType: LocationType) {
        val locations = dbData.getAllLocations()
        if (locationType == LocationType.DEVICE) locations[0] = deviceLocation.getLocation()
        val currentWeathers = weatherData.getAllCurrentWeather(locations)
        val dailyForecasts = weatherData.getAllDailyForecast(locations)
        val timezones = timezoneData.getAllTimezone(locations)
        val weathers: MutableList<Weather> = mutableListOf()
        for (i in 0 until locations.size)
            weathers.add(mapToWeather(currentWeathers[i], dailyForecasts[i], timezones[i], locations[i],
                    preferencesData.getUnits()))
        dbData.updateAllWeather(weathers)
    }

    suspend fun delete(name: String) = dbData.deleteWeather(name)
}