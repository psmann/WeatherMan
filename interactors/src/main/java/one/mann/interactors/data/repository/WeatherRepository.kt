package one.mann.interactors.data.repository

import one.mann.domain.model.LocationType
import one.mann.domain.model.Weather
import one.mann.interactors.data.mapToWeather
import one.mann.interactors.data.source.IApiTimezoneSource
import one.mann.interactors.data.source.IApiWeatherSource
import one.mann.interactors.data.source.IDbDataSource

class WeatherRepository(private val apiWeather: IApiWeatherSource,
                        private val apiTimezone: IApiTimezoneSource,
                        private val locationRepository: LocationRepository,
                        private val dbData: IDbDataSource) {

    suspend fun saveNew(locationType: LocationType? = null) {
        val location = if (locationType == LocationType.DEVICE) locationRepository.getDeviceLocation()
        else locationRepository.getApiLocation()
        dbData.insertWeather( mapToWeather(apiWeather.getCurrentWeather(location),
                apiWeather.getDailyForecast(location), apiTimezone.getTimezone(location), location))
    }

    suspend fun fetchAll(): List<Weather> = dbData.getAllWeather()

    suspend fun updateAll(locationType: LocationType) {
        val locations = dbData.getAllLocations()
        if (locationType == LocationType.DEVICE) locations[0] = locationRepository.getDeviceLocation()
        val currentWeathers = apiWeather.getAllCurrentWeather(locations)
        val dailyForecasts = apiWeather.getAllDailyForecast(locations)
        val timezones = apiTimezone.getAllTimezone(locations)
        val weathers: MutableList<Weather> = mutableListOf()
        for (i in 0 until locations.size)
            weathers.add(mapToWeather(currentWeathers[i], dailyForecasts[i], timezones[i], locations[i]))
        dbData.updateAllWeather(weathers)
    }

    suspend fun delete(id: Int) = dbData.deleteWeather(id)
}