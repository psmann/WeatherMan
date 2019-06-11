package one.mann.interactors.data.source

import one.mann.domain.DailyForecast
import one.mann.domain.Weather

interface IApiWeatherSource {

    suspend fun getWeather(lat: Double,
                           long: Double,
                           units: String,
                           appId: String): Weather // todo use interceptor for appId

    suspend fun getDailyForecast(lat: Double,
                           long: Double,
                           units: String,
                           appId: String): List<DailyForecast>
}