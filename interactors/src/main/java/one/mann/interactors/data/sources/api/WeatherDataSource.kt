package one.mann.interactors.data.sources.api

import one.mann.domain.model.weather.CurrentWeather
import one.mann.domain.model.weather.DailyForecast
import one.mann.domain.model.weather.HourlyForecast
import one.mann.domain.model.location.Location

/* Created by Psmann. */

interface WeatherDataSource {

    suspend fun getCurrentWeather(location: Location): CurrentWeather

    suspend fun getDailyForecast(location: Location): List<DailyForecast>

    suspend fun getHourlyForecast(location: Location): List<HourlyForecast>

    suspend fun getAllCurrentWeather(locations: List<Location>): List<CurrentWeather>

    suspend fun getAllDailyForecast(locations: List<Location>): List<List<DailyForecast>>

    suspend fun getAllHourlyForecast(locations: List<Location>): List<List<HourlyForecast>>
}