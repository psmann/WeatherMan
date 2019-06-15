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

    suspend fun save(locationType: LocationType) {
        val location = locationRepository.getLocation(locationType)
        dbData.insertWeather(
                mapToWeather(apiWeather.getCurrentWeather(location.coordinates),
                        apiWeather.getDailyForecast(location.coordinates),
                        location,
                        apiTimezone.getTimezone(location.coordinates)))
    }

    suspend fun fetchAll(): List<Weather> {
        if (dbData.isDbEmpty()) save(LocationType.DEVICE) // get device location when used for the first time
        return dbData.getAllWeather()
    }

    suspend fun updateAll(): List<Weather> {
        dbData.updateAllWeather()
        return dbData.getAllWeather()
    }

    suspend fun delete(id: Int) = dbData.deleteWeather(id)
}