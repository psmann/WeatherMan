package one.mann.weatherman.api.openweathermap

import one.mann.domain.models.location.Location
import one.mann.domain.models.weather.CurrentWeather
import one.mann.domain.models.weather.DailyForecast
import one.mann.domain.models.weather.HourlyForecast
import one.mann.interactors.data.sources.api.WeatherDataSource
import one.mann.weatherman.api.common.mapToDomain
import javax.inject.Inject

/* Created by Psmann. */

internal class OwmWeatherDataSource @Inject constructor(private val owmWeatherService: OwmWeatherService) : WeatherDataSource {

    override suspend fun getCurrentWeather(location: Location): CurrentWeather =
            owmWeatherService.getCurrentWeather(location.coordinates[0], location.coordinates[1])
                    .mapToDomain()

    override suspend fun getDailyForecast(location: Location): List<DailyForecast> =
            owmWeatherService.getDailyForecast(location.coordinates[0], location.coordinates[1])
                    .list?.map { it.mapToDomain() }!!

    override suspend fun getHourlyForecast(location: Location): List<HourlyForecast> =
            owmWeatherService.getHourlyForecast(location.coordinates[0], location.coordinates[1])
                    .list?.map { it.mapToDomain() }!!

    override suspend fun getAllCurrentWeather(locations: List<Location>): List<CurrentWeather> {
        val currentWeathers: MutableList<CurrentWeather> = mutableListOf()
        for (location in locations) currentWeathers.add(
                owmWeatherService.getCurrentWeather(location.coordinates[0], location.coordinates[1])
                        .mapToDomain())
        return currentWeathers
    }

    override suspend fun getAllDailyForecast(locations: List<Location>): List<List<DailyForecast>> {
        val dailyForecasts: MutableList<List<DailyForecast>> = mutableListOf()
        for (location in locations) dailyForecasts.add(
                owmWeatherService.getDailyForecast(location.coordinates[0], location.coordinates[1])
                        .list?.map { it.mapToDomain() }!!)
        return dailyForecasts
    }

    override suspend fun getAllHourlyForecast(locations: List<Location>): List<List<HourlyForecast>> {
        val hourlyForecasts: MutableList<List<HourlyForecast>> = mutableListOf()
        for (location in locations) hourlyForecasts.add(
                owmWeatherService.getHourlyForecast(location.coordinates[0], location.coordinates[1])
                        .list?.map { it.mapToDomain() }!!)
        return hourlyForecasts
    }
}