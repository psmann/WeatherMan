package one.mann.weatherman.framework.data.database

import one.mann.domain.models.NotificationData
import one.mann.domain.models.weather.City
import one.mann.domain.models.weather.Weather
import one.mann.interactors.data.sources.framework.DatabaseDataSource
import one.mann.weatherman.framework.data.database.entities.DailyForecast
import one.mann.weatherman.framework.data.database.entities.HourlyForecast
import javax.inject.Inject

/* Created by Psmann. */

internal class WeatherDbDataSource @Inject constructor(db: WeatherDb) : DatabaseDataSource {

    private val dao = db.weatherDao()

    override suspend fun insertWeather(weather: Weather) {
        dao.insertCity(weather.mapToDbCity())
        dao.insertCurrentWeather(weather.mapToDbCurrentWeather())
        dao.insertDailyForecasts(weather.mapToDbDailyForecasts())
        dao.insertHourlyForecasts(weather.mapToDbHourlyForecasts())
    }

    override suspend fun getAllCities(): List<City> = dao.getAllCities().map { it.mapToDomainCity() }

    override suspend fun getNotificationData(): NotificationData {
        val cityName = dao.getCityNameForUserLocation()
        val todayForecast = dao.getTodayForecastForUserLocation()
        val currentWeatherWithHourlyForecasts = dao.getHourlyForecastsForUserLocation()

        return currentWeatherWithHourlyForecasts.mapToDomain(cityName, todayForecast)
    }

    override suspend fun getAllWeather(): List<Weather> {
        val cities = dao.getAllCities()
        val weathers = mutableListOf<Weather>()
        cities.forEach {
            val currentWeather = dao.getCurrentWeather(it.cityId).currentWeather
            val dailyForecasts = dao.getCDailyForecasts(it.cityId).getSortedForecast()
            val hourlyForecasts = dao.getHourlyForecasts(it.cityId).getSortedForecast()
            weathers.add(it.mapToDomainWeather(currentWeather, dailyForecasts, hourlyForecasts))
        }

        return weathers
    }

    override suspend fun updateAllWeather(weathers: List<Weather>) {
        val dailyForecastsDb = mutableListOf<DailyForecast>()
        val hourlyForecastsDb = mutableListOf<HourlyForecast>()
        weathers.forEach {
            dailyForecastsDb.addAll(it.mapToDbDailyForecasts())
            hourlyForecastsDb.addAll(it.mapToDbHourlyForecasts())
        }
        dao.updateCities(weathers.map { it.mapToDbCity() })
        dao.updateCurrentWeathers(weathers.map { it.mapToDbCurrentWeather() })
        dao.updateDailyForecasts(dailyForecastsDb)
        dao.updateHourlyForecasts(hourlyForecastsDb)
    }

    override suspend fun deleteWeather(cityId: String) {
        dao.deleteCity(cityId)
        dao.deleteCurrentWeather(cityId)
        dao.deleteDailyForecasts(cityId)
        dao.deleteHourlyForecasts(cityId)
    }
}