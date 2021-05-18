package one.mann.weatherman.api.openweathermap

import one.mann.weatherman.api.openweathermap.dto.CurrentWeather
import one.mann.weatherman.api.openweathermap.dto.DailyForecast
import one.mann.weatherman.api.openweathermap.dto.HourlyForecast
import retrofit2.http.GET
import retrofit2.http.Query

/* Created by Psmann. */

internal interface OwmWeatherService {

    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat") latitude: Float,
        @Query("lon") longitude: Float
    ): CurrentWeather

    @GET("forecast/daily")
    suspend fun getDailyForecast(
        @Query("lat") latitude: Float,
        @Query("lon") longitude: Float
    ): DailyForecast

    @GET("forecast")
    suspend fun getHourlyForecast(
        @Query("lat") latitude: Float,
        @Query("lon") longitude: Float,
        @Query("cnt") count: Int = 7 // Restrict to only next 7 three-hourly forecasts (= 21 hours)
    ): HourlyForecast
}