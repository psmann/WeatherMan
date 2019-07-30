package one.mann.interactors.data.sources

import one.mann.domain.model.CurrentWeather
import one.mann.domain.model.DailyForecast
import one.mann.domain.model.HourlyForecast
import one.mann.domain.model.Location

interface WeatherDataSource {

    suspend fun getCurrentWeather(location: Location): CurrentWeather

    suspend fun getDailyForecast(location: Location): List<DailyForecast>

    suspend fun getHourlyForecast(location: Location): List<HourlyForecast>

    suspend fun getAllCurrentWeather(locations: List<Location>): List<CurrentWeather>

    suspend fun getAllDailyForecast(locations: List<Location>): List<List<DailyForecast>>

    suspend fun getAllHourlyForecast(locations: List<Location>): List<List<HourlyForecast>>
}