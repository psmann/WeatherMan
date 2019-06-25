package one.mann.interactors.data.repository

import one.mann.domain.model.Location
import one.mann.domain.model.LocationType
import one.mann.domain.model.Weather
import one.mann.interactors.data.mapToWeather
import one.mann.interactors.data.source.IApiTimezoneSource
import one.mann.interactors.data.source.IApiWeatherSource
import one.mann.interactors.data.source.IDbDataSource
import one.mann.interactors.data.source.IDeviceLocationSource

class WeatherRepository(
        private val apiWeather: IApiWeatherSource,
        private val apiTimezone: IApiTimezoneSource,
        private val deviceLocation: IDeviceLocationSource,
        private val dbData: IDbDataSource
) {

    suspend fun dbSize(): Int = dbData.getDbSize()

    suspend fun create(apiLocation: Location? = null) {
        val location = apiLocation ?: deviceLocation.getLocation()
        dbData.insertWeather(mapToWeather(apiWeather.getCurrentWeather(location),
                apiWeather.getDailyForecast(location), apiTimezone.getTimezone(location), location))
    }

    suspend fun readAll(): List<Weather> = dbData.getAllWeather()

    suspend fun updateAll(locationType: LocationType) {
        val locations = dbData.getAllLocations()
        if (locationType == LocationType.DEVICE) locations[0] = deviceLocation.getLocation()
        val currentWeathers = apiWeather.getAllCurrentWeather(locations)
        val dailyForecasts = apiWeather.getAllDailyForecast(locations)
        val timezones = apiTimezone.getAllTimezone(locations)
        val weathers: MutableList<Weather> = mutableListOf()
        for (i in 0 until locations.size)
            weathers.add(mapToWeather(currentWeathers[i], dailyForecasts[i], timezones[i], locations[i]))
        dbData.updateAllWeather(weathers)
    }

    suspend fun delete(name: String) = dbData.deleteWeather(name)
}