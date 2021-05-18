package one.mann.weatherman.api.openweathermap

import one.mann.domain.models.location.Location
import one.mann.domain.models.weather.CurrentWeather
import one.mann.domain.models.weather.DailyForecast
import one.mann.domain.models.weather.HourlyForecast
import one.mann.interactors.data.sources.api.WeatherDataSource
import one.mann.weatherman.api.common.mapToDomain
import javax.inject.Inject

/* Created by Psmann. */

internal class OwmWeatherDataSource @Inject constructor(private val owmWeatherService: OwmWeatherService) :
    WeatherDataSource {

    override suspend fun getCurrentWeather(location: Location): CurrentWeather {
        return owmWeatherService.getCurrentWeather(location.coordinates[0], location.coordinates[1])
            .mapToDomain()
    }

    override suspend fun getDailyForecasts(location: Location): List<DailyForecast> {
        return owmWeatherService.getDailyForecast(location.coordinates[0], location.coordinates[1]).list
            ?.map { it.mapToDomain() }!!
    }

    override suspend fun getHourlyForecasts(location: Location): List<HourlyForecast> {
        return owmWeatherService.getHourlyForecast(location.coordinates[0], location.coordinates[1]).list
            ?.map { it.mapToDomain() }!!
    }
}