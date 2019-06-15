package one.mann.interactors.data.source

import one.mann.domain.model.DailyForecast
import one.mann.domain.model.CurrentWeather

interface IApiWeatherSource {

    suspend fun getWeather(location: Array<Float>): CurrentWeather // todo use interceptor for appId

    suspend fun getDailyForecast(location: Array<Float>): List<DailyForecast>
}