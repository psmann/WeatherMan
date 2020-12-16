package one.mann.weatherman.framework.data.database

import one.mann.domain.models.NotificationData
import one.mann.domain.models.location.Location
import one.mann.interactors.data.sources.framework.DatabaseDataSource
import one.mann.weatherman.framework.data.database.entities.*
import javax.inject.Inject
import one.mann.domain.models.weather.City as DomainCity
import one.mann.domain.models.weather.Weather as DomainWeather

/* Created by Psmann. */

internal class WeatherDbDataSource @Inject constructor(db: WeatherDb) : DatabaseDataSource {

    private val dao = db.weatherDao()

    override suspend fun insertWeather(weather: DomainWeather) {
        dao.insertCity(weather.mapToDbCity())
        dao.insertCurrentWeather(weather.mapToDbCurrentWeather())
        dao.insertDailyForecasts(weather.mapToDbDailyForecasts())
        dao.insertHourlyForecasts(weather.mapToDbHourlyForecasts())
    }

    override suspend fun getAllCities(): List<DomainCity> = dao.getAllCities().map { it.mapToDomainCity() }

    override suspend fun getNotificationData(): NotificationData {
        val userCity = dao.getCityNameForUserLocation()
        val todayForecast = dao.getTodayForecastForUserLocation(userCity.cityId)
        val currentWeatherWithHourlyForecasts = dao.getHourlyForecastsForUserLocation(userCity.cityId)

        return currentWeatherWithHourlyForecasts.mapToDomain(userCity.cityName, todayForecast)
    }

    override suspend fun getAllWeather(): List<DomainWeather> {
        val cities = dao.getAllCities()
        val weathers = mutableListOf<DomainWeather>()
        cities.forEach {
            val currentWeather = dao.getCurrentWeather(it.cityId).currentWeather
            val dailyForecasts = dao.getCDailyForecasts(it.cityId).getSortedForecast()
            val hourlyForecasts = dao.getHourlyForecasts(it.cityId).getSortedForecast()
            weathers.add(it.mapToDomainWeather(currentWeather, dailyForecasts, hourlyForecasts))
        }

        return weathers
    }

    override suspend fun updateUserCity(location: Location) {
        val city = dao.getUserCity().copy(
                coordinatesLat = location.coordinates[0],
                coordinatesLong = location.coordinates[1]
        )
        dao.updateCity(city)
    }

    override suspend fun updateLastChecked(lastChecked: Long) {
        val updatedCurrentWeathers = dao.getAllCurrentWeather().map { it.copy(lastChecked = lastChecked) }
        dao.updateCurrentWeathers(updatedCurrentWeathers)
    }

    override suspend fun updateAllWeather(weathers: List<DomainWeather>) {
        val citiesDb = mutableListOf<City>()
        val currentWeathersDb = mutableListOf<CurrentWeather>()
        val dailyForecastsDb = mutableListOf<DailyForecast>()
        val hourlyForecastsDb = mutableListOf<HourlyForecast>()

        weathers.forEach {
            citiesDb.add(it.mapToDbCity())
            currentWeathersDb.add(it.mapToDbCurrentWeather())
            dailyForecastsDb.addAll(it.mapToDbDailyForecasts())
            hourlyForecastsDb.addAll(it.mapToDbHourlyForecasts())
        }
        dao.updateCities(citiesDb)
        dao.updateCurrentWeathers(currentWeathersDb)
        dao.updateDailyForecasts(dailyForecastsDb)
        dao.updateHourlyForecasts(hourlyForecastsDb)
    }

    override suspend fun deleteCity(cityId: String) {
        dao.deleteCity(cityId)
        dao.deleteCurrentWeather(cityId)
        dao.deleteDailyForecasts(cityId)
        dao.deleteHourlyForecasts(cityId)
    }
}