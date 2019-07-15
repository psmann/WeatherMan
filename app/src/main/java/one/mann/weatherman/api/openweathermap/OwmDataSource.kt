package one.mann.weatherman.api.openweathermap

import one.mann.domain.model.CurrentWeather
import one.mann.domain.model.DailyForecast
import one.mann.domain.model.Location
import one.mann.interactors.data.source.WeatherDataSource
import one.mann.weatherman.api.common.mapToDomain
import javax.inject.Inject

internal class OwmDataSource @Inject constructor(private val owmService: OwmService) : WeatherDataSource {

    override suspend fun getCurrentWeather(location: Location): CurrentWeather =
            owmService.getCurrentWeather(location.coordinates[0], location.coordinates[1])
                    .mapToDomain()

    override suspend fun getDailyForecast(location: Location): List<DailyForecast> =
            owmService.getDailyForecast(location.coordinates[0], location.coordinates[1])
                    .list
                    .map { it.mapToDomain() }

    override suspend fun getAllCurrentWeather(locations: List<Location>): List<CurrentWeather> {
        val currentWeathers: MutableList<CurrentWeather> = mutableListOf()
        for (location in locations) currentWeathers.add(
                owmService.getCurrentWeather(location.coordinates[0], location.coordinates[1])
                        .mapToDomain())
        return currentWeathers
    }

    override suspend fun getAllDailyForecast(locations: List<Location>): List<List<DailyForecast>> {
        val dailyForecasts: MutableList<List<DailyForecast>> = mutableListOf()
        for (location in locations) dailyForecasts.add(
                owmService.getDailyForecast(location.coordinates[0], location.coordinates[1])
                        .list
                        .map { it.mapToDomain() })
        return dailyForecasts
    }
}