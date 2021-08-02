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
    suspend fun createCity(apiLocation: Location? = null) {
        val location = apiLocation ?: deviceLocation.getLocation() // Use device GPS location if null
        val timeCreated = System.currentTimeMillis()
        val currentWeather = weatherData.getCurrentWeather(location).copy(units = prefsData.getUnits())

        dbData.insertCityAndWeather(
            mapToDomainWeather(
                City(
                    cityId = generateCityId(),
                    coordinatesLat = location.coordinates[0],
                    coordinatesLong = location.coordinates[1],
                    timezone = if (apiLocation != null) timezoneData.getTimezone(location) else "",
                    timeCreated = timeCreated
                ),
                currentWeather,
                weatherData.getDailyForecasts(location),
                weatherData.getHourlyForecasts(location)
            )
        )
    }

    /** Returns a list of all Weather data from the database */
    suspend fun readAllWeather(): List<Weather> = dbData.getAllCitiesAndWeathers().map {
        it.copy(currentWeather = it.currentWeather.copy(units = prefsData.getUnits()))
    }

    /** Update all rows in the database with new data */
    suspend fun updateAllWeather(weatherData: List<Weather>) = dbData.updateAllWeathers(weatherData)

    /** Remove a row from the database */
    suspend fun deleteCity(cityId: String) = dbData.deleteCityAndWeather(cityId)

    /** Returns notification data */
    suspend fun readNotificationData(): NotificationData = dbData.getNotificationData()

    /** Update weather from server if syncFromServer() is true, otherwise only update lastChecked value */
    suspend fun updateAllData(locationType: LocationType): Boolean = if (syncFromServer()) {
        val weathersFromDb = readAllWeather()
        val weathersForUpdate = mutableListOf<Weather>()

        weathersFromDb.forEachIndexed { index, dbWeather ->
            val location = if (index == 0 && locationType == LocationType.DEVICE) {
                deviceLocation.getLocation()
            } else Location(coordinates = listOf(dbWeather.city.coordinatesLat, dbWeather.city.coordinatesLong))
            val cityForUpdate = City(
                cityId = dbWeather.city.cityId,
                coordinatesLat = location.coordinates[0],
                coordinatesLong = location.coordinates[1],
                timeCreated = dbWeather.city.timeCreated,
                timezone = dbWeather.city.timezone
            )
            val currentWeatherForUpdate = weatherData.getCurrentWeather(location).copy(
                weatherId = dbWeather.currentWeather.weatherId,
                units = prefsData.getUnits()
            )
            val dailyForecastsFromApi = weatherData.getDailyForecasts(location)
            val hourlyForecastsFromApi = weatherData.getHourlyForecasts(location)
            val dailyForecastsForUpdate = dbWeather.dailyForecasts.mapIndexed { i, dbDailyForecast ->
                dailyForecastsFromApi[i].copy(dailyId = dbDailyForecast.dailyId)
            }
            val hourlyForecastsForUpdate = dbWeather.hourlyForecasts.mapIndexed { i, dbHourlyForecast ->
                hourlyForecastsFromApi[i].copy(hourlyId = dbHourlyForecast.hourlyId)
            }
            weathersForUpdate.add(
                mapToDomainWeather(
                    cityForUpdate,
                    currentWeatherForUpdate,
                    dailyForecastsForUpdate,
                    hourlyForecastsForUpdate
                )
            )
        }
        updateAllWeather(weathersForUpdate)
        true
    } else {
        // If time out period isn't over then just update lastChecked
        dbData.updateLastChecked(System.currentTimeMillis())
        false
    }

    /** Creates a new unique code for cityId */
    private fun generateCityId(): String = UUID.randomUUID().toString()

    /** Returns true if it has been longer than time-out period since last update */
    private suspend fun syncFromServer(): Boolean = System.currentTimeMillis() - prefsData.getLastUpdated() > TIME_OUT
}