package one.mann.interactors.data.source

import one.mann.domain.model.CurrentWeather
import one.mann.domain.model.DailyForecast
import one.mann.domain.model.Location

interface IApiWeatherSource {

    suspend fun getCurrentWeather(location: Location): CurrentWeather // todo use interceptor for appId

    suspend fun getDailyForecast(location: Location): List<DailyForecast>

    suspend fun getAllCurrentWeather(locations: List<Location>): List<CurrentWeather>

    suspend fun getAllDailyForecast(locations: List<Location>): List<List<DailyForecast>>
}