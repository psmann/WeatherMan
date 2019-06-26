package one.mann.weatherman.api.openweathermap

import one.mann.domain.model.CurrentWeather
import one.mann.domain.model.DailyForecast
import one.mann.domain.model.Location
import one.mann.domain.util.UNITS
import one.mann.interactors.data.source.IApiWeatherSource
import one.mann.weatherman.api.Keys
import one.mann.weatherman.api.mapToDomain

internal class OwmDataSource : IApiWeatherSource {

    override suspend fun getCurrentWeather(location: Location): CurrentWeather =
            RetrofitInstance.service
                    .getCurrentWeather(location.coordinates[0], location.coordinates[1], UNITS, Keys.OWM_APP_ID)
                    .mapToDomain()

    override suspend fun getDailyForecast(location: Location): List<DailyForecast> =
            RetrofitInstance.service
                    .getDailyForecast(location.coordinates[0], location.coordinates[1], UNITS, Keys.OWM_APP_ID)
                    .list
                    .map { it.mapToDomain() }

    override suspend fun getAllCurrentWeather(locations: List<Location>): List<CurrentWeather> {
        val currentWeathers: MutableList<CurrentWeather> = mutableListOf()
        for (location in locations) currentWeathers.add(RetrofitInstance.service
                .getCurrentWeather(location.coordinates[0], location.coordinates[1], UNITS, Keys.OWM_APP_ID)
                .mapToDomain())
        return currentWeathers
    }

    override suspend fun getAllDailyForecast(locations: List<Location>): List<List<DailyForecast>> {
        val dailyForecasts: MutableList<List<DailyForecast>> = mutableListOf()
        for (location in locations) dailyForecasts.add(RetrofitInstance.service
                .getDailyForecast(location.coordinates[0], location.coordinates[1], UNITS, Keys.OWM_APP_ID)
                .list
                .map { it.mapToDomain() })
        return dailyForecasts
    }
}