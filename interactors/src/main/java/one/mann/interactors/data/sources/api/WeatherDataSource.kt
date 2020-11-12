package one.mann.interactors.data.sources.api

import one.mann.domain.models.weather.CurrentWeather
import one.mann.domain.models.weather.DailyForecast
import one.mann.domain.models.weather.HourlyForecast
import one.mann.domain.models.location.Location

/* Created by Psmann. */

interface WeatherDataSource {

    suspend fun getCurrentWeather(location: Location): CurrentWeather

    suspend fun getDailyForecast(location: Location): List<DailyForecast>

    suspend fun getHourlyForecast(location: Location): List<HourlyForecast>

    suspend fun getAllCurrentWeather(locations: List<Location>): List<CurrentWeather>

    suspend fun getAllDailyForecast(locations: List<Location>): List<List<DailyForecast>>

    suspend fun getAllHourlyForecast(locations: List<Location>): List<List<HourlyForecast>>
}