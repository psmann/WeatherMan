package one.mann.interactors.data.sources.api

import one.mann.domain.models.location.Location
import one.mann.domain.models.weather.CurrentWeather
import one.mann.domain.models.weather.DailyForecast
import one.mann.domain.models.weather.HourlyForecast

/* Created by Psmann. */

interface WeatherDataSource {

    suspend fun getCurrentWeather(location: Location): CurrentWeather

    suspend fun getDailyForecasts(location: Location): List<DailyForecast>

    suspend fun getHourlyForecasts(location: Location): List<HourlyForecast>
}