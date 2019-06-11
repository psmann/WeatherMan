package one.mann.weatherman.api.openweathermap

import one.mann.domain.DailyForecast
import one.mann.domain.Weather
import one.mann.interactors.data.source.IApiWeatherSource
import one.mann.weatherman.api.mapToDomain

internal class OwmDataSource : IApiWeatherSource {

    override suspend fun getWeather(lat: Double, long: Double, units: String, appId: String): Weather =
            RetrofitInstance.service
                    .getWeatherData(lat, long, units, appId)
                    .mapToDomain()

    override suspend fun getDailyForecast(lat: Double, long: Double, units: String, appId: String
    ): List<DailyForecast> =
            RetrofitInstance.service
                    .getForecastData(lat, long, units, appId)
                    .list
                    .map { it.mapToDomain() }
}