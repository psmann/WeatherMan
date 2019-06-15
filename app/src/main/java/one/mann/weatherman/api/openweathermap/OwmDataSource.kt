package one.mann.weatherman.api.openweathermap

import one.mann.domain.model.DailyForecast
import one.mann.domain.model.CurrentWeather
import one.mann.domain.util.UNITS
import one.mann.interactors.data.source.IApiWeatherSource
import one.mann.weatherman.api.Keys
import one.mann.weatherman.api.mapToDomain

internal class OwmDataSource() : IApiWeatherSource {

    override suspend fun getCurrentWeather(location: Array<Float>): CurrentWeather =
            RetrofitInstance.service
                    .getWeatherData(location[0], location[1], UNITS, Keys.OWM_APP_ID)
                    .mapToDomain()

    override suspend fun getDailyForecast(location: Array<Float>): List<DailyForecast> =
            RetrofitInstance.service
                    .getForecastData(location[0], location[1], UNITS, Keys.OWM_APP_ID)
                    .list
                    .map { it.mapToDomain() }
}